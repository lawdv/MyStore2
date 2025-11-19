package com.example.mystore;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // --- LOCALIZACIÓN ---
    private static final int REQUEST_LOCATION_PERMISSION = 100;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView txtUbicacion;
    private Button btnUbicacion;

    // base de datos de dbhelper
    private DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        //BD
        dbHelper = new DBHelper(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ---------- login reformado ----------
        EditText txtUser = findViewById(R.id.UserName);
        EditText txtPass = findViewById(R.id.Password);
        Button btnLogin = findViewById(R.id.buttonLogin);

        btnLogin.setOnClickListener(view -> {
            String user = txtUser.getText().toString().trim();
            String pass = txtPass.getText().toString().trim();

            // BDhelper
            if(dbHelper.validarUsuario(user, pass)){
                Intent intent = new Intent(MainActivity.this, ListaProductos.class);
                intent.putExtra("user_name", user);
                startActivity(intent);
            }else{
                Toast.makeText(MainActivity.this,
                        "Usuario o Contraseña incorrectos",
                        Toast.LENGTH_SHORT).show();
            }

//
//            // Validación de los campos anterior
//            if (user.equals("admin") && pass.equals("123")) {
//                Intent intent = new Intent(MainActivity.this, ListaProductos.class);
//                intent.putExtra("user_name", user);
//                startActivity(intent);
//            } else {
//                Toast.makeText(MainActivity.this,
//                        "Usuario o Contraseña incorrectos",
//                        Toast.LENGTH_SHORT).show();
//            }
        });

        // ---------- geo localizacion ----------
        txtUbicacion = findViewById(R.id.txtUbicacion);
        btnUbicacion = findViewById(R.id.btnUbicacion);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        btnUbicacion.setOnClickListener(v -> solicitarUbicacion());

        // automatico no se uso porque da error.

    }

    private void solicitarUbicacion() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION
            );
        } else {
            obtenerUltimaUbicacion();
        }
    }

    private void obtenerUltimaUbicacion() {
        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            double lat = location.getLatitude();
                            double lon = location.getLongitude();

                            String countryName = "Desconocido";

                            // Usar Geocoder para obtener el país
                            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
                                if (addresses != null && !addresses.isEmpty()) {
                                    countryName = addresses.get(0).getCountryName();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            txtUbicacion.setText("País: " + countryName +
                                    "\nLat: " + lat +
                                    "\nLon: " + lon);

                        } else {
                            txtUbicacion.setText("No se pudo obtener ubicación.");
                        }
                    });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUltimaUbicacion();
            } else {
                Toast.makeText(this,
                        "Permiso de ubicación necesario",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
