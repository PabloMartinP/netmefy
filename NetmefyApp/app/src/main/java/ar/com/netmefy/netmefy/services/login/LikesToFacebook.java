package ar.com.netmefy.netmefy.services.login;

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

import java.util.ArrayList;

import ar.com.netmefy.netmefy.R;
import ar.com.netmefy.netmefy.services.App;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by ignac on 21/8/2017.
 */

public class LikesToFacebook implements Runnable {

    private Session session;
    JSONArray jsonLikes;
    String jsonNext;
    ArrayList<String> likesNames;
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
                                jsonNext = response.getJSONObject().getJSONObject("paging").getString("next");
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
        for(int i = 0; i < jsonLikes.length(); i++){
            likesNames.add(((JSONObject)jsonLikes.get(i)).getString("name").toString());
        }
        if(!jsonNext.isEmpty()){
            searchNextLikes(jsonNext);
        }else {
            sendLikes(likesNames);
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

    private void sendLikes(ArrayList<String> likesNames) throws JSONException {
        String url = App.getContext().getString(R.string.baseUrl)+ App.getContext().getString(R.string.urlLikesFromId);
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
        queue.add(jsObjRequest);
    }

}
