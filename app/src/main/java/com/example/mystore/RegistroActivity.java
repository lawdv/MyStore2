package com.example.mystore;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        EditText edtNombre = findViewById(R.id.edtNombre);
        EditText edtEmail = findViewById(R.id.edtEmail);
        EditText edtTelefono = findViewById(R.id.edtTelefono);
        Button btnGuardar = findViewById(R.id.btnGuardarCliente);

        btnGuardar.setOnClickListener(v -> {
            String nombre = edtNombre.getText().toString().trim();
            Toast.makeText(this, "Cliente " + nombre + " registrado (demo)", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
