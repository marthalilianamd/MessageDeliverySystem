package org.mlmunozd.app.MessageDeliverySystem.Logic;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.mlmunozd.app.MessageDeliverySystem.Models.User;
import org.mlmunozd.app.MessageDeliverySystem.Persistence.SessionManager;
import org.mlmunozd.app.MessageDeliverySystem.R;
import org.mlmunozd.app.MessageDeliverySystem.Services.MyFirebaseMessagingService;
import org.mlmunozd.app.MessageDeliverySystem.Services.MyServerRequests;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    private static final String TAG = "Register";
    public User usermodel;
    public String email,pass;
    public EditText emailText,passText;
    public AlertDialog mDialog;
    public MyFirebaseMessagingService myfirebaseservice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);
    //The instruction next hides keyboard
    this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    ImageButton btnRegister = findViewById(R.id.btnregister);
    btnRegister.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            emailText = findViewById(R.id.emailText);
            email = emailText.getText().toString();
            if (!validarEmail(email) || email.isEmpty()) {
                emailText.setError("Vacio o invalido Email");
            }else{
                verificandoUsuarioenServer(email);
                passText = findViewById(R.id.passText);
                pass = passText.getText().toString();
                if (pass.isEmpty()) {
                    passText.setError("Contraseña no ingresada");
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setTitle("Verificando usuario en el servidor");
                    builder.setMessage("Espere ...");
                    mDialog = builder.create();
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mDialog.isShowing()){mDialog.dismiss();}
                            //si email existe en el servidor
                            if (!SessionManager.getInstance(getApplicationContext()).getEmail().equals("")) {
                                //si usuario no existe en la App
                                usermodel = User.getInstance();
                                if (usermodel.getIdUSer() == 0) {
                                    //si usuario tiene token registrado en el servidor
                                    if (!SessionManager.getInstance(getApplicationContext()).getTokenMovil().equals("")){
                                        //registro token existente en App
                                        try {//Registro usuario en App
                                            registerUser(usermodel, email, pass, SessionManager.getInstance(getApplicationContext()).getTokenMovil());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Toast.makeText(getApplicationContext(), "Usuario no creado, posible problema " + "del sistema", Toast.LENGTH_LONG).show();
                                            Log.e("DBFLow", "Error: al insertar datos del usuario");
                                        }
                                    }else{ //si token NO existe en servidor
                                        if (SessionManager.getInstance(getApplicationContext()).getTokenMovil().equals("")) {
                                            //registramos su movil en el servidor
                                            registrarMovilAlServer(email);
                                            try {//y lo registramos en la App
                                                registerUser(usermodel, email, pass, SessionManager.getInstance(getApplicationContext()).getTokenMovil());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Toast.makeText(getApplicationContext(), "Usuario no creado, posible problema " + "del sistema", Toast.LENGTH_LONG).show();
                                                Log.e("DBFLow", "Error: al insertar datos del usuario");
                                            }
                                        }
                                    }
                                }else{
                                    Toast.makeText(getApplicationContext(), "Este móvil ya fue registado en el servidor", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "NO está autorizado para registro", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, 4000);
                }
            }
        }});
    }

    public void registrarMovilAlServer(String email){
        //Registrar movil a FCM
        myfirebaseservice = new MyFirebaseMessagingService();
        myfirebaseservice.registrarMovil( Register.this, email);
    }
    public void registerUser(User usermodel,String email,String pass, String tokenMovil){
        usermodel.setEmail(email);
        usermodel.setPassword(pass);
        usermodel.setToken_movil(tokenMovil);
        usermodel.save();
        Toast.makeText(getApplicationContext(),"Registro exitoso en la App", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void verificandoUsuarioenServer(final String email){
        Thread hilo = new Thread(){
            public void run(){
                MyVolleyAsyncTask myVolleyAsyncTask = new MyVolleyAsyncTask(email);
                try {
                    myVolleyAsyncTask.execute().get(1, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        };
        hilo.start();
    }

    public class MyVolleyAsyncTask extends AsyncTask<String, String, Void> {
        private String emailUser;

        public MyVolleyAsyncTask(String email){
            emailUser = email;
        }

        @Override
        protected Void doInBackground(String... params) {
            // Method runs on a separate thread, make all the network calls you need
            MyServerRequests peticion = new MyServerRequests();
            peticion.obtenerDatosUsuarioDelServidor(Register.this,emailUser);
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
        }
    }

    /*
    * valida formato email
    **/
    public boolean validarEmail(String email){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}