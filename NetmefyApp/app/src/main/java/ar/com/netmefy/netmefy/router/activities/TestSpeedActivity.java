package ar.com.netmefy.netmefy.router.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.android.volley.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import ar.com.netmefy.netmefy.R;
import ar.com.netmefy.netmefy.router.models.InternetSpeed;
import ar.com.netmefy.netmefy.services.WifiUtils;
import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.IRepeatListener;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import fr.bmartel.speedtest.model.SpeedTestMode;


public class TestSpeedActivity extends AppCompatActivity {

    public class SpeedTestTask extends AsyncTask<Void, Void, String> {
        Response.Listener listener;
        BigDecimal maxSpeed ;
        SpeedTestMode speedTestMode;
        public SpeedTestTask(SpeedTestMode speedTestMode, Response.Listener lst ){
            listener = lst;
            maxSpeed= BigDecimal.valueOf(0);
            this.speedTestMode = speedTestMode;
        }

        @Override
        protected String doInBackground(Void... params) {
            SpeedTestSocket speedTestSocket = new SpeedTestSocket();
            speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {
                @Override
                public void onCompletion(SpeedTestReport report) {
                    Log.v("speedtest", "[COMPLETED] rate in octet/s : " + report.getTransferRateOctet());
                    Log.v("speedtest", "[COMPLETED] rate in bit/s   : " + report.getTransferRateBit());

                    BigDecimal rateBit = report.getTransferRateBit();
                    BigDecimal rateOctet = report.getTransferRateOctet();
                    long temporaryPacketSize = report.getTemporaryPacketSize();
                    long totalPacketSize = report.getTotalPacketSize();

                    listener.onResponse(maxSpeed);
                }

                @Override
                public void onError(SpeedTestError speedTestError, String errorMessage) {
                    listener.onResponse(speedTestError.toString());
                }

                @Override
                public void onProgress(float percent, SpeedTestReport report) {
                    maxSpeed = maxSpeed.max(report.getTransferRateBit());
                    listener.onResponse(report.getTransferRateBit());
                }
                /*@Override
                public void onInterruption() {
                    // triggered when forceStopTask is called
                    //Toast.makeText(context, "onInterruption", Toast.LENGTH_SHORT).show();
                    listener.onResponse("onInterruption");

                }*/
            });

            int SECS = 1;
            if(speedTestMode == SpeedTestMode.DOWNLOAD) {
                //speedTestSocket.startDownload("http://2.testdebit.info/fichiers/10Mo.dat", 1000 * SECS);
                speedTestSocket.startDownloadRepeat("http://2.testdebit.info/fichiers/1Mo.dat",
                        20*1000, SECS*1000, new
                                IRepeatListener() {
                                    @Override
                                    public void onCompletion(final SpeedTestReport report) {
                                        // called when repeat task is finished
                                        maxSpeed = maxSpeed.max(report.getTransferRateBit());
                                        listener.onResponse(report.getTransferRateBit());
                                    }

                                    @Override
                                    public void onReport(final SpeedTestReport report) {
                                        // called when a download report is dispatched
                                        maxSpeed = maxSpeed.max(report.getTransferRateBit());
                                        listener.onResponse(report.getTransferRateBit());
                                    }
                                });

            }
            else if(speedTestMode == SpeedTestMode.UPLOAD)
                speedTestSocket.startUpload("http://2.testdebit.info/", 1000000);

            return null;
        }
    }
    ////////////////////////////////////////////////////////////////////////////
    Button btnUp, btnDown;
    //WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_speed);

        btnUp = (Button)findViewById(R.id.btnTestSpeedUpload);
        btnDown  =(Button) findViewById(R.id.btnTestSpeedDownload);
        //webView = (WebView)findViewById(R.id.wvTestSpeed);

    }

    public void testDownload(View v){
        btnDown.setText("Start DownloadTest ...");
        //testSpeed();
        WifiUtils.testDownloadSpeedWithFast(getApplicationContext(),this, new Response.Listener<InternetSpeed>() {
            @Override
            public void onResponse(final InternetSpeed response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnDown.setText(response.toString());
                    }
                });
            }
        }, new Response.Listener<InternetSpeed>() {
            @Override
            public void onResponse(final InternetSpeed response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnDown.setText("FIN::"+response.toString());
                    }
                });
            }
        });


        /*new SpeedTestTask( SpeedTestMode.DOWNLOAD, new Response.Listener() {
            @Override
            public void onResponse(final Object response) {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btnDown.setText(response.toString());
                        }
                    });
                }catch (Exception e){
                    e = e;
                }
            }
        }).execute();*/
    }

    public void testUpload(View v){

    }

    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    public void testSpeed(){
        final AtomicBoolean TERMINO;
        final Button btn;
        //WebView wv;
        final AtomicReference<String> speedUnitOk;
        final AtomicReference<String> speedOk;
        ///////////////////////////////////////////////////////
        TERMINO = new AtomicBoolean(false);
        //btn = (Button)findViewById(R.id.btnTestSpeed);
        btn = btnDown;
        btn.setText(btn.getText().toString() + " OK");
        speedUnitOk = new AtomicReference<String>("");
        speedOk = new AtomicReference<String>("");

        speedOk.set("0");
        /////////////////////////////////////////////
        TERMINO.set(false);
        speedOk.set("0");
        speedUnitOk.set("");

        class MyJavaScriptInterface
        {
            @JavascriptInterface
            //@SuppressWarnings("unused")
            public void processHTML(String html)
            {
                Document document = Jsoup.parse(html);
                String speed, speedUnit;
                speed = document.getElementById("speed-value").html();
                speedUnit = document.getElementById("speed-units").html();

                speedOk.set(speed);
                speedUnitOk.set(speedUnit);
                //btnok.setText(speed + " " + speedUnit);

                if(document.getElementById("after-test-actions").getElementsByAttribute("style").size()==1){
                    TERMINO.set(true);
                    //btnok.setText(speed + " " + speedUnit + " OK");
                }
            }
        }

        //final WebView browser = (WebView)findViewById(R.id.webview);
        final WebView browser = new WebView(getApplicationContext());
        /* JavaScript must be enabled if you want it to work, obviously */
        browser.getSettings().setJavaScriptEnabled(true);

        /* Register a new JavaScript interface called HTMLOUT */
        browser.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");

        /* WebViewClient must be set BEFORE calling loadUrl! */
        browser.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                final int DELAY_RESTART_SECS = 5;
                final int DELAY_BETWEEN_INTENT_TO_RECONNCET_SECS = 2;
                btn.setText("HOA");
                Timer timer = new Timer();
                timer.schedule(new TimerTask()
                {
                    int i = 0;
                    @Override
                    public void run()
                    {
                        try{
                            if(TERMINO.get()) {
                                this.cancel();
                            }
                            else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        btn.setText(String.valueOf(i) + "-"+speedOk.get() +speedUnitOk.get());
                                        browser.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                                    }
                                });
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        i++;
                    }
                }, DELAY_RESTART_SECS*1000, DELAY_BETWEEN_INTENT_TO_RECONNCET_SECS*1000);

            }
        });

        browser.loadUrl("https://fast.com/es/");
    }


}
