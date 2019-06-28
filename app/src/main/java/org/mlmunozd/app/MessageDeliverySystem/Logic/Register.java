package org.mlmunozd.app.MessageDeliverySystem.Logic;


import android.app.ActivityManager;
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

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;
import org.mlmunozd.app.MessageDeliverySystem.Models.User;
import org.mlmunozd.app.MessageDeliverySystem.Persistence.SessionManager;
import org.mlmunozd.app.MessageDeliverySystem.R;
import org.mlmunozd.app.MessageDeliverySystem.Services.MensajeService;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    private static final String TAG = "REGISTER_ACTIVITY";
    public User usermodel;
    public String email, pass;
    public EditText emailText, passText;
    public AlertDialog mDialog;
    public MyFirebaseMessagingService myfirebaseservice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ImageButton btnRegister = findViewById(R.id.btnregister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailText = findViewById(R.id.emailText);
                email = emailText.getText().toString();
                if (!validarEmail(email) || email.isEmpty()) {
                    emailText.setError("Vacio o invalido Email");
                } else {
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
                            verificandoUsuarioenServer(email, pass,getApplicationContext());

                            new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mDialog.isShowing()) {
                                    mDialog.dismiss();
                                }
                                String emailConsultado = SessionManager.getInstance(getApplicationContext()).getEmailRequest();
                                Boolean contrasenaIgual = SessionManager.getInstance(getApplicationContext()).equalContrasena();
                                String tokenConsultado = SessionManager.getInstance(getApplicationContext()).getTokenMovil();

                                Log.d(TAG,"emailConsultado: --->>> "+emailConsultado);
                                Log.d(TAG,"contrasenaIgual: --->>> "+contrasenaIgual);
                                Log.d(TAG,"tokenConsultado: --->>> "+tokenConsultado);

                                if (emailConsultado.equals(email) && contrasenaIgual){
                                    Log.d(TAG,"--->>> Datos Correctos");
                                    //si usuario no existe en la App
                                    usermodel = User.getInstance();
                                        registrarMovilAlServer(email);
                                        //registro token existente en App
                                        try {//Registro usuario en App
                                            registerUser(usermodel, email, pass, tokenConsultado);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Toast.makeText(getApplicationContext(), "Usuario no creado, posible problema " + "del sistema", Toast.LENGTH_LONG).show();
                                            Log.e("DBFLow", "Error: al insertar datos del usuario");
                                        }
                                }else if(!emailConsultado.equals(email) || !contrasenaIgual) {
                                    Log.d(TAG,"--->>> Datos Incorrectos");
                                    Toast.makeText(getApplicationContext(), "Email o contraseña incorrectas", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "No autorizado para registrar el móvil", Toast.LENGTH_LONG).show();
                                }
                            }
                        }, 8000);
                        if(!isMyServiceRunning(MensajeService.class)){
                            Log.d(TAG, "INICIAR SERVICIO SMS ...");
                            Intent mensajeServiceIntent  = new Intent(getApplicationContext(), MensajeService.class);
                            getApplicationContext().startService(mensajeServiceIntent);
                        }
                    }
                }
            }
        });
    }

    public void registrarMovilAlServer(final String email) {
        //Registrar movil a FCM
        String getTokenenServer = SessionManager.getInstance(getApplicationContext()).getTokenMovil();
        Log.d(TAG, "EXISTEE TOKEN  --->"+getTokenenServer);
        if(!getTokenenServer.equals("")) {//si existe token
            Thread hiloToken = new Thread(new Runnable() {
                String emailuser;
                @Override
                public void run() {
                    emailuser = email;
                    myfirebaseservice = new MyFirebaseMessagingService();
                    try {
                        Log.d(TAG, "ELIMINANDO INSTANCIA");
                        FirebaseInstanceId.getInstance().deleteInstanceId();
                        String newtoken = FirebaseInstanceId.getInstance().getToken("1087302060910",
                                FirebaseMessaging.INSTANCE_ID_SCOPE);
                        Log.d(TAG, "TOKEN RENOVADIO:::: ---> "+newtoken);
                        myfirebaseservice.onTokenRefresh(emailuser,Register.this, newtoken);

                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "No fue posible almacenar el token renovado", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "No fue posible almacenar el token renovado");
                        e.printStackTrace();
                        throw new RuntimeException("No fue posible almacenar el token renovado");
                    }
                }
            });
            hiloToken.start();
            Toast.makeText(getApplicationContext(), "Token móvil renovado", Toast.LENGTH_LONG).show();
        }else {
            Log.d(TAG, "CREANDO INSTANCIA FIREBASE");
            myfirebaseservice = new MyFirebaseMessagingService();
            myfirebaseservice.registrarMovil(Register.this, email);
        }
    }

    public void registerUser(User usermodel, String email, String pass, String tokenMovil) {
        usermodel.setEmail(email);
        usermodel.setPassword(pass);
        usermodel.setToken_movil(tokenMovil);
        usermodel.save();
        Toast.makeText(getApplicationContext(), "Registro exitoso en la App", Toast.LENGTH_LONG).show();

        SessionManager.getInstance(getApplicationContext()).saveEmail(email);
        SessionManager.getInstance(getApplicationContext()).saveContrasena(pass);
        SessionManager.getInstance(getApplicationContext()).saveTokenMovil(tokenMovil);

        Intent intent = new Intent(getApplicationContext(), Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void verificandoUsuarioenServer(final String email, final String contrasena, final Context context) {
        Thread hilo = new Thread() {
            Context mContex;
            public void run() {
                mContex = context;
                JSONObject objJSON = null;
                MyVolleyAsyncTask myVolleyAsyncTask = new MyVolleyAsyncTask(mContex.getApplicationContext(), email, contrasena);
                try {
                    objJSON = myVolleyAsyncTask.execute().get(10, TimeUnit.SECONDS);
                } catch (InterruptedException |ExecutionException | TimeoutException e) {
                    e.printStackTrace();
                }
                final JSONObject receiveJSONObject = objJSON;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(receiveJSONObject != null) {
                            try {
                                if(receiveJSONObject.getBoolean("igualcontrasena")){

                                    SessionManager.getInstance(mContex.getApplicationContext()).saveEmailRequest(receiveJSONObject.getString("email"));
                                    SessionManager.getInstance(mContex.getApplicationContext()).saveEqualContrasena(receiveJSONObject.getBoolean("igualcontrasena"));
                                    SessionManager.getInstance(mContex.getApplicationContext()).saveTokenMovil(receiveJSONObject.getString("token"));

                                    String emailConsultado = SessionManager.getInstance(mContex.getApplicationContext()).getEmailRequest();
                                    Boolean contrasenaIgual = SessionManager.getInstance(mContex.getApplicationContext()).equalContrasena();
                                    String tokenConsultado = SessionManager.getInstance(mContex.getApplicationContext()).getTokenMovil();
                                    Log.d(TAG,"EMAIL GUARDADO: --->>> "+emailConsultado);
                                    Log.d(TAG,"PASS GUARDADO: --->>> "+contrasenaIgual);
                                    Log.d(TAG,"TOKEN GUARDADO: --->>> "+tokenConsultado);

                                }else{
                                    SessionManager.getInstance(mContex.getApplicationContext()).saveEmailRequest(receiveJSONObject.getString("email"));
                                    SessionManager.getInstance(mContex.getApplicationContext()).saveEqualContrasena(receiveJSONObject.getBoolean("igualcontrasena"));
                                    SessionManager.getInstance(mContex.getApplicationContext()).saveTokenMovil(receiveJSONObject.getString("token"));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            SessionManager.getInstance(mContex.getApplicationContext()).saveEmailRequest("");
                            SessionManager.getInstance(mContex.getApplicationContext()).saveEqualContrasena(false);
                            SessionManager.getInstance(mContex.getApplicationContext()).saveTokenMovil("");
                        }
                    }
                });

            }
        };
        hilo.start();
    }

    public class MyVolleyAsyncTask extends AsyncTask<Void, Void, JSONObject> {
        private String emailUser, contrasenaUser;
        private Context mContex;

        public MyVolleyAsyncTask(Context ctx, String email, String contrasena) {
            mContex = ctx;
            emailUser = email;
            contrasenaUser = contrasena;
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            // El método se ejecuta en un hilo separado, realiza la petición de obtención de datos
            //para verificación
            MyServerRequests peticion = new MyServerRequests();
            return peticion.obtenerDatosUsuarioDelServidor(mContex.getApplicationContext(), emailUser, contrasenaUser);
            //return null;
        }

    }

    /*
     * valida formato email
     **/
    public boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}