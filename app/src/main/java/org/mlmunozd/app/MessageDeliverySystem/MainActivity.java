package org.mlmunozd.app.MessageDeliverySystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import java.util.HashMap;

import org.mlmunozd.app.MessageDeliverySystem.Logic.Account;
import org.mlmunozd.app.MessageDeliverySystem.Models.User;
import org.mlmunozd.app.MessageDeliverySystem.Persistence.SessionManager;
import org.mlmunozd.app.MessageDeliverySystem.Services.MensajeService;

import jonathanfinerty.once.Once;


public class MainActivity extends AppCompatActivity {
    private final int DURACION = 2000;//2segundos
    public static final String SESSION_MESSAGE="";

    private static final String TAG ="MAIN_ACTIVITY";
    private static final String NEW_INSTALL_APP = "NUEVA_INSTALACION_APP";
    public User usermodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Once.initialise(this);

        if (!Once.beenDone(Once.THIS_APP_INSTALL, NEW_INSTALL_APP) || isInstallFromUpdate(getApplicationContext())) {
            //Primera vez que se instala la App
            Log.d(TAG, "NUEVA INSTALACION APP" );
            Once.markDone(NEW_INSTALL_APP);
            getSharedPreferences( SessionManager.getPrefName(), 0).edit().clear().commit();
            usermodel = User.getInstance();
            usermodel.deleteAllUsers();
            SessionManager.IS_LOGIN = "";
            Toast.makeText(this, "Nueva instalación de la APP \n " +"Registrar el móvil", Toast.LENGTH_LONG).show();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(SessionManager.getInstance(getApplicationContext()).checkLogin()){
                    finish();
                }
                else{
                    /*Se crea la comunicacion a la actividad IntroActivity
                    indicando de donde y la actividad a lanzar*/
                    Intent intent = new Intent(getApplicationContext(), Account.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    HashMap<String, String> userSession = SessionManager.getInstance(getApplicationContext()).getUserDetails();
                    intent.putExtra(SESSION_MESSAGE, userSession.get(SessionManager.KEY_EMAIL));

                    /*Se inicia el intent para lanzar la actividad*/
                    startActivity(intent);
                    finish();
                }
            }
        }, DURACION);
    }

    private int getFirstTimeRun() {
        SharedPreferences sp = getSharedPreferences("MYAPP", 0);
        int result, currentVersionCode = BuildConfig.VERSION_CODE;
        int lastVersionCode = sp.getInt("FIRSTTIMERUN", -1);
        if (lastVersionCode == -1) result = 0; else
            result = (lastVersionCode == currentVersionCode) ? 1 : 2;
        sp.edit().putInt("FIRSTTIMERUN", currentVersionCode).apply();
        return result;
    }


    public boolean isFirstInstall(Context context) {
        try {
            long firstInstallTime =   context.getPackageManager().getPackageInfo(context.getPackageName(), 0).firstInstallTime;
            long lastUpdateTime = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).lastUpdateTime;
            return firstInstallTime == lastUpdateTime;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isInstallFromUpdate(Context context) {
        try {
            long firstInstallTime = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).firstInstallTime;
            long lastUpdateTime = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).lastUpdateTime;
            return firstInstallTime != lastUpdateTime;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

}
