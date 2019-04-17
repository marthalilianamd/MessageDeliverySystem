package org.mlmunozd.app.MessageDeliverySystem.Logic;

import android.content.Intent;
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
import org.mlmunozd.app.MessageDeliverySystem.R;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

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
                EditText emailText = findViewById(R.id.emailText);
                String email = emailText.getText().toString();
                EditText passText = findViewById(R.id.passText);
                String pass = passText.getText().toString();
                
                if (email.isEmpty() || pass.isEmpty()) {
                    passText.setError("Email y/o contraseña no ingresados");
                }
                if (!validarEmail(email)) {
                    emailText.setError("Email inválido");
                } else
                    {
                        User usermodel = User.getInstance();
                        if(usermodel.getUser(email)==null)
                        {
                            try
                            {
                                registerUser(usermodel,email,pass);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Usuario no creado, posible problema" +
                                        "del sistema", Toast.LENGTH_LONG).show();
                                Log.e("DBFLow", "Error: al insertar datos del usuario");
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Ya existe un usuario con este email", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }

    public void registerUser(User usermodel,String email,String pass){
        usermodel.setEmail(email);
        usermodel.setPassword(pass);
        usermodel.save();
        Toast.makeText(getApplicationContext(), "Usuario creado con éxito",
                Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public boolean validarEmail(String email){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}
