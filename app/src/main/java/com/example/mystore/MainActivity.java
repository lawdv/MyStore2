package com.example.mystore;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Referencia de los Views en XML
        EditText txtUser = findViewById(R.id.UserName);
        EditText txtPass = findViewById(R.id.Password);
        Button btnLogin = findViewById(R.id.buttonLogin);

        // Activamos el click del boton > 
        btnLogin.setOnClickListener(view ->{
            String user = txtUser.getText().toString().trim();
            String pass = txtPass.getText().toString().trim();

            // Validacion de los campos
        if (user.equals("admin") && pass.equals("123")){
            // ir a la otra ventana
            Intent intent = new Intent(MainActivity.this, ListaProductos.class);
            //almacenar el usuario para el bienvenido
            intent.putExtra("user_name", user);
            startActivity(intent);
        } else {
            // Mostrar mensjae de error de el if 
            Toast.makeText(MainActivity.this, "Usuario o Contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
        
        });
    }
}