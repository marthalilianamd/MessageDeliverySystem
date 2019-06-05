package org.mlmunozd.app.MessageDeliverySystem.Persistence;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.Context;
import android.preference.PreferenceManager;

import org.mlmunozd.app.MessageDeliverySystem.Logic.Intro;
import org.mlmunozd.app.MessageDeliverySystem.Logic.Login;
import java.util.HashMap;

import static org.mlmunozd.app.MessageDeliverySystem.Logic.Intro.SESSION_DATA;

public class SessionManager {

    SharedPreferences mypreferences;
    Editor editor;
    Context _context;
    int PRIVATE_MODE =0;

    private static SessionManager singletonSessionManager;

    private static final String PREF_NAME = "MessageDeliverySystemPREF";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASS = "pass";
    public static final String KEY_EMAIL_REQUEST = "emailrequest";
    public static final String TOKEN_MOVIL = "tokenmovil";
    public static final String EQUALSOK_PASS = "isEqualContrasena";

    public SessionManager(Context context){
        this._context =context;
        mypreferences = _context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = mypreferences.edit();
    }

    /*
    * Implementations Singleton
    */

    public static SessionManager getInstance(Context context){
        if(singletonSessionManager == null){
            singletonSessionManager = new SessionManager(context);
        }
        return singletonSessionManager;
    }
    /**/

    public void createLoginSession(String email){
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(KEY_EMAIL,email);
        editor.commit();
    }

    public boolean isUserLoggedIn(){
        return mypreferences.getBoolean(IS_LOGIN,false);
    }

    public boolean checkLogin(){
        if(!this.isUserLoggedIn()){
            //if user is not logged in redirect him to login
            Intent intent = new Intent(_context, Intro.class);
            //Close all the activities
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(intent);
            return true;
        }
        return false;
    }

    //Get stored session data
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> userDetails = new HashMap<String, String>();
        userDetails.put(KEY_EMAIL,mypreferences.getString(KEY_EMAIL,null));
        return userDetails;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
        Intent intent = new Intent (_context, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        SESSION_DATA="";
        resetearDatos();
        _context.startActivity(intent);
    }

    //EMAIL
    public void saveEmail(String email){
        editor.putString(KEY_EMAIL,email);
        editor.commit();
    }

    public String getEmail(){
        return mypreferences.getString(KEY_EMAIL,"");
    }


    //CONTRASEÑA
    public String getContrasena() {
        return mypreferences.getString(KEY_PASS,"xxx");
    }

    public void saveContrasena(String pass){
        editor.putString(KEY_PASS,pass);
        editor.commit();
    }

    //EMAIL REQUEST
    //setters and getters for Request Data
    public void saveEmailRequest(String email){
        editor.putString(KEY_EMAIL_REQUEST,email);
        editor.commit();
    }

    public String getEmailRequest() {
        return mypreferences.getString(KEY_EMAIL_REQUEST,"");
    }

    //CONTRASEÑA REQUEST
    public void saveEqualContrasena(boolean coincide){
        editor.putBoolean(EQUALSOK_PASS,coincide);
        editor.commit();
    }

    public Boolean equalContrasena() {
        return mypreferences.getBoolean(EQUALSOK_PASS, false);
    }

    ///TOKEN REQUEST
    public void saveTokenMovil(String token){
        editor.putString(TOKEN_MOVIL,token);
        editor.commit();
    }

    public String getTokenMovil(){
        return mypreferences.getString(TOKEN_MOVIL,"");
    }


    //RESETEAR DATOS
    public void resetearDatos(){
        saveEmail("");
        saveContrasena("");

        saveEqualContrasena(false);
        saveEmailRequest("");

        //saveTokenMovil("");
    }
}