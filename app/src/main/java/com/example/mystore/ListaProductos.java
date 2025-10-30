package com.example.mystore;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListaProductos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_productos);

        // 1. saludo
        String user = getIntent().getStringExtra("user_name");
        if (user == null) user = "admin";
        TextView tv = findViewById(R.id.textViewWelcome);
        tv.setText("Bienvenido, " + user + "!");

        // 2. recycler con los 3 productos
        List<Producto> productos = new ArrayList<>();
        productos.add(new Producto("Camiseta", 49.99, R.drawable.camiseta));
        productos.add(new Producto("Zapatos", 89.5, R.drawable.zapatos));
        productos.add(new Producto("Reloj", 120.0, R.drawable.reloj));

        RecyclerView recycler = findViewById(R.id.recyclerProductos);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new ProductoAdapter(productos));

        // 3. BOTONES
        Button btnCarrito = findViewById(R.id.btnCarrito);
        Button btnRegistro = findViewById(R.id.btnRegistro);

        btnCarrito.setOnClickListener(v -> {
            Intent i = new Intent(ListaProductos.this, CarritoActivity.class);
            startActivity(i);
        });

        btnRegistro.setOnClickListener(v -> {
            Intent i = new Intent(ListaProductos.this, RegistroActivity.class);
            startActivity(i);
        });
    }
}
