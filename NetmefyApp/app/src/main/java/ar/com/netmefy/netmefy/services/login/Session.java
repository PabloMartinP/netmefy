package ar.com.netmefy.netmefy.services.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ar.com.netmefy.netmefy.services.NMF_Info;
import ar.com.netmefy.netmefy.services.api.Api;
import ar.com.netmefy.netmefy.services.api.entity.clientInfo;
import ar.com.netmefy.netmefy.services.api.entity.notificacionModel;
import ar.com.netmefy.netmefy.services.api.entity.usuarioInfo;

/**
 * Created by ignac on 18/6/2017.
 */

public class Session {
    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public List<notificacionModel> getNotificaciones(){
        List<notificacionModel> ressult  = new ArrayList<>();
        notificacionModel nm ;
        String jsonNotificaciones = prefs.getString("notificaciones","");

        if(!jsonNotificaciones.isEmpty()){
            JSONArray jsonarray = null;
            try {
                jsonarray = new JSONArray(jsonNotificaciones);
                //JSONArray jsonArray = jsnobject.getJSONArray("locations");
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject explrObject = jsonarray.getJSONObject(i);
                    nm = new notificacionModel();
                    nm.usuario_sk = explrObject.getInt("usuario_sk");
                    nm.cliente_sk = explrObject.getInt("cliente_sk");
                    nm.notificacion_sk = explrObject.getInt("notificacion_sk");
                    nm.notificacion_desc = explrObject.getString("notificacion_desc");
                    nm.notificacion_texto = explrObject.getString("notificacion_texto");
                    nm.tiempo_sk = explrObject.getString("tiempo_sk").substring(0, 10);
                    nm.leido = explrObject.getBoolean("leido");
                    ressult.add(nm);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return  ressult;
    }

    public void setNotificaciones(List<notificacionModel> notificaciones){
        Gson gson = new Gson();
        String json = gson.toJson(notificaciones);
        prefs.edit().putString("notificaciones", json).apply();
    }

    public void setUserType(String userType) {
        prefs.edit().putString("userType", userType).apply();
    }

    public String getUserType() {
        String userType = prefs.getString("userType","");
        return userType;
    }
    public void setLog(String id, String value)
    {
        prefs.edit().putString(id, value).apply();
    }

    public void setUserId(String userId) {
        prefs.edit().putString("userId", userId).apply();
    }
    public void setPassword(String password){
        prefs.edit().putString("password", password).apply();
    }
    public String getPassword(){
        return prefs.getString("password", "");
    }
    public String getUserId() {
        String userId = prefs.getString("userId","");

        return userId;
    }

    public void setClientInfo() {
        Gson gson = new Gson();
        String json = gson.toJson(NMF_Info.clientInfo);
        prefs.edit().putString("clientInfo", json).apply();
    }
    public void getClientInfo(){
        Gson gson = new Gson();
        String clientInfoJson = prefs.getString("clientInfo","");

        clientInfo clientInfo = gson.fromJson(clientInfoJson, clientInfo.class);
        NMF_Info.clientInfo = clientInfo;

    }


    public void setUserName(String userName) {

        prefs.edit().putString("userName", userName).apply();
    }


    public String getUserName() {
        String userName = prefs.getString("userName","");
        return userName;
    }

    public void setEmail(String email) {
        prefs.edit().putString("email", email).apply();
    }

    public String getEmail() {
        String email = prefs.getString("email","");
        return email;
    }
    public void setLoginWay(String loginWay) {
        prefs.edit().putString("loginWay", loginWay).apply();
    }

    public String getLoginWay() {
        String loginWay = prefs.getString("loginWay","");
        return loginWay;
    }

    public void setUsuarioInfo() {
        Gson gson = new Gson();
        String json = gson.toJson(NMF_Info.usuarioInfo);
        prefs.edit().putString("usuarioInfo", json).apply();
    }
    public void getUsuarioInfo(){
        Gson gson = new Gson();
        String usuarioInfoJson = prefs.getString("usuarioInfo","");

        usuarioInfo usuarioInfo = gson.fromJson(usuarioInfoJson, usuarioInfo.class);
        NMF_Info.usuarioInfo = usuarioInfo;

    }
}
