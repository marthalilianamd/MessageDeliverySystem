package org.mlmunozd.app.MessageDeliverySystem;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.HashMap;

import org.mlmunozd.app.MessageDeliverySystem.Logic.Account;
import org.mlmunozd.app.MessageDeliverySystem.Persistence.SessionManager;

public class MainActivity extends AppCompatActivity {
    private final int DURACION = 2000;//2segundos
    public static final String SESSION_MESSAGE="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
