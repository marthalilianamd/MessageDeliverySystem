package org.mlmunozd.app.MessageDeliverySystem.Logic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.mlmunozd.app.MessageDeliverySystem.Models.User;
import org.mlmunozd.app.MessageDeliverySystem.Persistence.SessionManager;
import org.mlmunozd.app.MessageDeliverySystem.R;

public class Login extends AppCompatActivity {
    public static final String EXTRA_MESSAGE="";
    private MyFirebaseMessagingService myfirebaseservice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //The instruction next hides keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ImageButton btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText emailText = findViewById(R.id.emailText);
                String email = emailText.getText().toString();
                EditText passText = findViewById(R.id.passText);
                String pass = passText.getText().toString();

                Register register = new Register();
                User usermodel = User.getInstance();

                if (email.isEmpty() || pass.isEmpty()) {
                    passText.setError("Email y/o contraseña son inválidos");
                }
                if (!register.validarEmail(email)) {
                    emailText.setError("Email inválido") ;
                }
                else {
                    if ((usermodel.getUser(email) != null)) {
                        if (usermodel.getUser(email).getEmail().equals(email) && usermodel.getUser(email).getPassword().equals(pass)){

                            //creating user login session
                            SessionManager.getInstance(getApplicationContext()).createLoginSession(email);
                            String[] nickname = email.split("@");
                            Toast.makeText(getApplicationContext(), "El usuario " + nickname[0] +" iniciando sesión.",
                                    Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(getApplicationContext(), Account.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra(EXTRA_MESSAGE, email);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Email o contraseña incorrecta", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "El Usuario no existe", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
