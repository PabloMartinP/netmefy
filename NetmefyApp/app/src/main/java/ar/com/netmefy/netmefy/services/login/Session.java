package ar.com.netmefy.netmefy.services.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ignac on 18/6/2017.
 */

public class Session {
    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setUserType(String userType) {
        prefs.edit().putString("userType", userType).apply();
    }

    public String getUserType() {
        String userType = prefs.getString("userType","");
        return userType;
    }

    public void setUserId(String userId) {
        prefs.edit().putString("userId", userId).apply();
    }

    public String getUserId() {
        String userId = prefs.getString("userId","");
        return userId;
    }
}
