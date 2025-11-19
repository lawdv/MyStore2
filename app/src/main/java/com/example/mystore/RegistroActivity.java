package com.example.mystore;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistroActivity extends AppCompatActivity {

    //traigo a dbhelper para hacer la funcionalidad

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        dbHelper = new DBHelper(this);

        EditText edtNombre = findViewById(R.id.edtNombre);
        EditText edtEmail = findViewById(R.id.edtEmail);
        EditText edtTelefono = findViewById(R.id.edtTelefono);
        EditText edtPassword = findViewById(R.id.edtPassword);

        Button btnGuardar = findViewById(R.id.btnGuardarCliente);

        btnGuardar.setOnClickListener(v -> {
            String nombre = edtNombre.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String telefono = edtTelefono.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            // Validar
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email y contraseña son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            // Guardar usuario en la BD: usamos email como username
            boolean ok = dbHelper.registrarUsuario(email, password);

            if (ok) {
                Toast.makeText(this,
                        "Usuario " + email + " registrado correctamente",
                        Toast.LENGTH_SHORT).show();
                finish(); // vuelve al login
            } else {
                Toast.makeText(this,
                        "Ese usuario ya existe, intenta con otro email",
                        Toast.LENGTH_SHORT).show();
            }

        //antiguo registro sin funcionalidad
//        EditText edtNombre = findViewById(R.id.edtNombre);
//        EditText edtEmail = findViewById(R.id.edtEmail);
//        EditText edtTelefono = findViewById(R.id.edtTelefono);
//        Button btnGuardar = findViewById(R.id.btnGuardarCliente);
//
//        btnGuardar.setOnClickListener(v -> {
//            String nombre = edtNombre.getText().toString().trim();
//            Toast.makeText(this, "Cliente " + nombre + " registrado (demo)", Toast.LENGTH_SHORT).show();
//            finish();
        });
    }
}
