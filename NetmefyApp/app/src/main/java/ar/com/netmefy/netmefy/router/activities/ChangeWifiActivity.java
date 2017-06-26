package ar.com.netmefy.netmefy.router.activities;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import ar.com.netmefy.netmefy.R;
import ar.com.netmefy.netmefy.router.ConfigWifi;
import ar.com.netmefy.netmefy.router.Router;
import ar.com.netmefy.netmefy.router.tplink.TLWR941ND.TPLink;

final class Constants {

    public static String NETWROK_ADDITIONAL_SECURITY_TKIP = "tkip";
    public static String NETWROK_ADDITIONAL_SECURITY_AES = "aes";
    public static String NETWROK_ADDITIONAL_SECURITY_WEP = "wep";
    public static String NETWROK_ADDITIONAL_SECURITY_NONE = "";
    public static String BACKSLASH = "\"";
}

public class ChangeWifiActivity extends AppCompatActivity {

    Router router;

    EditText etSsid;
    EditText etPassword;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_wifi);





        etSsid = (EditText) findViewById(R.id.etSsid);
        etPassword= (EditText) findViewById(R.id.etPassword);
        btn = (Button) findViewById(R.id.btnChangeWifi);

        btn.setEnabled(false);


        router = new TPLink(this.getApplicationContext());


        router.getConfigWifi(new Response.Listener<ConfigWifi>() {
            @Override
            public void onResponse(ConfigWifi configWifi) {
                etSsid.setText(configWifi.getSsid());
                etPassword.setText(configWifi.getPassword());

                btn.setEnabled(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                etSsid.setText("ERR!!!");
                etPassword.setText("ERRR!!!");
            }
        });

        /*
        router.getWifiSsid(new Response.Listener<String>() {
                               @Override
                               public void onResponse(String ssid) {
                                   etSsid.setText(ssid);
                               }
                           }, new Response.ErrorListener() {
                               @Override
                               public void onErrorResponse(VolleyError error) {
                                   etSsid.setText("ERROR!!!");;
                               }
                           }
        );



        router.getWifiPassword(new Response.Listener<String>() {
                               @Override
                               public void onResponse(String password) {
                                   etPassword.setText(password);
                               }
                           }, new Response.ErrorListener() {
                               @Override
                               public void onErrorResponse(VolleyError error) {
                                   etPassword.setText("ERROR!!!");;
                               }
                           }
        );*/
    }

    public void addWifiConfig(String ssid,String password,String securityParam,String securityDetailParam) {
          /*String NETWROK_ADDITIONAL_SECURITY_TKIP = "tkip";
          String NETWROK_ADDITIONAL_SECURITY_AES = "aes";
          String NETWROK_ADDITIONAL_SECURITY_WEP = "wep";
          String NETWROK_ADDITIONAL_SECURITY_NONE = "";
          String BACKSLASH = "\\";*/

        if (ssid == null) {
            throw new IllegalArgumentException(
                    "Required parameters can not be NULL #");
        }

        String wifiName = ssid;
        WifiConfiguration conf = new WifiConfiguration();
        // On devices with version Kitkat and below, We need to send SSID name
        // with double quotes. On devices with version Lollipop, We need to send
        // SSID name without double quotes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            conf.SSID = wifiName;
        } else {
            conf.SSID = Constants.BACKSLASH + wifiName + Constants.BACKSLASH;
        }
        String security = securityParam;
        if (security.equalsIgnoreCase("WEP")) {
            conf.wepKeys[0] = password;
            conf.wepTxKeyIndex = 0;
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        } else if (security.equalsIgnoreCase("NONE")) {
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        } else if ("WPA"
                .equalsIgnoreCase(security)
                || "WPA2"
                .equalsIgnoreCase(security)
                || "WPA/WPA2 PSK"
                .equalsIgnoreCase(security)) {
            // appropriate ciper is need to set according to security type used,
            // ifcase of not added it will not be able to connect
            conf.preSharedKey = Constants.BACKSLASH + password + Constants.BACKSLASH;
            conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            conf.status = WifiConfiguration.Status.ENABLED;
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        }
        String securityDetails = securityDetailParam;
        if (securityDetails.equalsIgnoreCase(Constants.NETWROK_ADDITIONAL_SECURITY_TKIP)) {
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        } else if (securityDetails.equalsIgnoreCase(Constants.NETWROK_ADDITIONAL_SECURITY_AES)) {
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        } else if (securityDetails.equalsIgnoreCase(Constants.NETWROK_ADDITIONAL_SECURITY_WEP)) {
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        } else if (securityDetails.equalsIgnoreCase(Constants.NETWROK_ADDITIONAL_SECURITY_NONE)) {
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.NONE);
        }
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        int newNetworkId = wifiManager.addNetwork(conf);
        wifiManager.enableNetwork(newNetworkId, true);
        wifiManager.saveConfiguration();
        wifiManager.setWifiEnabled(true);
    }

    public void change(View view){
        String newSsid = etSsid.getText().toString();
        String newPassword = etPassword.getText().toString();

        final ConfigWifi configWifi = new ConfigWifi();
        configWifi.setSsid(newSsid);
        configWifi.setPassword(newPassword);

        addWifiConfig(newSsid, newPassword,"wpa2", Constants.NETWROK_ADDITIONAL_SECURITY_AES );

        //TODO Falta setConfigWifiAndReconnect
/*
        router.setConfigWifiAndRestart(configWifi,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        etSsid.setText("SSID OK");
                        etPassword.setText("PASSWORD OK");

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        etSsid.setText("SSID NO OK");
                        etPassword.setText("PASSWORD NO OK");
                    }
                });
*/

        /*router.setConfigWifi(configWifi,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        etSsid.setText("SSID OK");
                        etPassword.setText("PASSWORD OK");

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        etSsid.setText("SSID NO OK");
                        etPassword.setText("PASSWORD NO OK");
                    }
                });*/

        /*
        router.setWifiSsid(newSsid, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                etSsid.setText("SSID OK");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                etSsid.setText("SSID NO OK");
            }
        });

        router.setWifiPassword(newPassword, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                etPassword.setText("PASSWORD OK");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                etPassword.setText("PASSWORD NO OK");
            }
        });
*/

    }


}
