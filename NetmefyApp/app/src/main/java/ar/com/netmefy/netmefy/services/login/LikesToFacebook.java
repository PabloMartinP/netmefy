package ar.com.netmefy.netmefy.services.login;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import ar.com.netmefy.netmefy.MainActivity;
import ar.com.netmefy.netmefy.R;
import ar.com.netmefy.netmefy.services.App;
import ar.com.netmefy.netmefy.services.api.Api;
import ar.com.netmefy.netmefy.services.api.entity.paginasLikeadas;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by ignac on 21/8/2017.
 */

public class LikesToFacebook implements Runnable {

    private Session session;
    JSONArray jsonLikes;
    String jsonNext;
    ArrayList<String> likesNames;
    //private Api api;
    MainActivity mainActivity;
    public LikesToFacebook(MainActivity mainActivity){
        //this.api =  Api.getInstance(getApplicationContext());
        this.mainActivity = mainActivity;

    }
    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Profile userFacebook = Profile.getCurrentProfile();
        session = new Session(getApplicationContext());
        session.setUserName(userFacebook.getName());
        session.setLoginWay("Facebook");
        likesNames = new ArrayList<>();
        callFacebookForLikes(userFacebook);
    }

    private void callFacebookForLikes(Profile userFacebook) {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+userFacebook.getId()+"/likes?limit=500",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        /* handle the result */
                        try {
                            if(response.getError() != null){
                                //hubo un error al conectar con FB
                            }else{
                                jsonLikes = response.getJSONObject().getJSONArray("data");
                                boolean hasNext  = response.getJSONObject().getJSONObject("paging").has("next");
                                if(hasNext )
                                    jsonNext = response.getJSONObject().getJSONObject("paging").getString("next");
                                else
                                    jsonNext = null;
                                addUserLikes(jsonLikes, jsonNext);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    private void addUserLikes(JSONArray jsonLikes, String jsonNext) throws JSONException {
        String page = "";
        for(int i = 0; i < jsonLikes.length(); i++){
            page = ((JSONObject)jsonLikes.get(i)).getString("name").toString();
            //TODO: HAY QUE TENER CUIDADO DE LOS CARACTERES ESPECIALES
            page = page.replace("?", "_63_");
            page = page.replace("&", "_38_");
            //page = page.replace("'", "_39_");
            //page = TextUtils.htmlEncode(page);

            //page = URLEncoder.encode(page, "UTF-8");
            //page = Html.escapeHtml(page);

            likesNames.add("'" + page + "'");
        }

        if( jsonNext!=null && !jsonNext.isEmpty()){
            searchNextLikes(jsonNext);
        }else {
            sendLikes(likesNames, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String ok;
                    ok = "OK;";
                }
            });
        }
    }

    private void searchNextLikes(String jsonNext) {
        String url = jsonNext;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getJSONArray("data").length()>0) {
                        addUserLikes(response.getJSONArray("data"), response.getJSONObject("paging").getString("next"));
                    }else{
                        addUserLikes(response.getJSONArray("data"), "");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getNetworkTimeMs();
            }
        });
        queue.add(jsObjRequest);

    }

    public void wrtieFileOnInternalStorage(Context mcoContext,String sFileName, String sBody){
        File file = new File(mcoContext.getFilesDir(),"mydir");
        if(!file.exists()){
            file.mkdir();
        }

        try{
            File gpxfile = new File(file, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();

        }catch (Exception e){

        }
    }

    private void saveOnExternalSD(String text){
        File file = new File(getApplicationContext().getExternalFilesDir(null), "testfile.txt");
        FileOutputStream fileOutput = null;
        try {
            fileOutput = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        OutputStreamWriter outputStreamWriter=new OutputStreamWriter(fileOutput);
        try{
            outputStreamWriter.write(text);
            outputStreamWriter.flush();
            fileOutput.getFD().sync();
            outputStreamWriter.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }


    }


    private void sendLikes(ArrayList<String> likesNames, final Response.Listener<String> success) throws JSONException {
        final paginasLikeadas paginasLikeadas = new paginasLikeadas();

        //TODO: por ahora dejo el 1 1 pero hay que leer el id del session()
        paginasLikeadas.cliente_sk = 1;
        paginasLikeadas.usuario_sk = 1;

        int j ;
        mainActivity.tvFacebookStatus.setText("fb:ok");

        for (int i = 0; i < likesNames.size(); ) {

            j = Math.min(likesNames.size(), i+10);

            //paginasLikeadas.paginas = likesNames;
            //paginasLikeadas.paginas .add("\"dd\"");

            try{
                paginasLikeadas.paginas = new ArrayList<>(likesNames.subList(i,j));


                mainActivity.api.sendLikes(paginasLikeadas, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //wrtieFileOnInternalStorage(getApplicationContext(), "asdf.txt", response);
                        //saveOnExternalSD(response);
                        if(response.startsWith("error")){
                            //TODO: nose porque falla la primera vez, la segunda inserta ok
                            mainActivity.api.sendLikes(paginasLikeadas, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response2) {
                                    if(response2.startsWith("error")){
                                        mainActivity.tvFacebookStatus.setText("fb:err");
                                    }

                                }
                            });

                        }

                    }
                });
            }catch (Exception e){
                String ee;
                ee = e.toString();
            }

            i = i+10;
        }


        /*paginasLikeadas paginasLikeadas = new paginasLikeadas();
        paginasLikeadas.usuario_sk = 1;
        paginasLikeadas.cliente_sk = 1;
        paginasLikeadas.paginas = new ArrayList<String>(likesNames.subList(1, 2));
        api.sendLikes(paginasLikeadas, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                success.onResponse("ok");
            }
        });*/

        /////////////////////////////////////////////////////
        /*String url = App.getContext().getString(R.string.baseUrl)+ App.getContext().getString(R.string.urlLikesFromId);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JSONObject requestToSend = new JSONObject();
        requestToSend.put("userId",session.getUserId());
        requestToSend.put("name",session.getUserName());
        requestToSend.put("email",session.getEmail());
        requestToSend.put("loginWay",session.getLoginWay());
        requestToSend.put("likesFromUser",likesNames);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, requestToSend, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //String kind = response.getString("kind");
                    String status = response.getString("status");
                    if (status.equalsIgnoreCase("ok")){

                    }else{

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsObjRequest);*/

    }

}
