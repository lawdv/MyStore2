package com.example.mystore;

import android.content.Intent;
import android.database.Cursor;
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

    //sumo campos para DB
    private DBHelper dbHelper;
    private RecyclerView recycler;
    private ProductoAdapter adapter;
    private List<Producto> productos;


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

        dbHelper = new DBHelper(this);

        //antiguo demo
//        List<Producto> productos = new ArrayList<>();
//        productos.add(new Producto("Camiseta", 49.99, R.drawable.camiseta));
//        productos.add(new Producto("Zapatos", 89.5, R.drawable.zapatos));
//        productos.add(new Producto("Reloj", 120.0, R.drawable.reloj));

        recycler = findViewById(R.id.recyclerProductos);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        productos = cargarProductosDesdeBD();
        //usando el carrito
        adapter = new ProductoAdapter(productos, this);
        // para mostrar el item recien agregado
        recycler.setAdapter(adapter);

        //antiguo
//        RecyclerView recycler = findViewById(R.id.recyclerProductos);
//        recycler.setLayoutManager(new LinearLayoutManager(this));
//        recycler.setAdapter(new ProductoAdapter(productos));

        // 3. BOTONES
        Button btnCarrito = findViewById(R.id.btnCarrito);
        Button btnRegistro = findViewById(R.id.btnRegistro);
        Button btnAgregar = findViewById(R.id.btnAgregarProducto);


        btnCarrito.setOnClickListener(v -> {
            Intent i = new Intent(ListaProductos.this, CarritoActivity.class);
            startActivity(i);
        });

        btnRegistro.setOnClickListener(v -> {
            Intent i = new Intent(ListaProductos.this, RegistroActivity.class);
            startActivity(i);
        });
        btnAgregar.setOnClickListener(v -> {
            Intent i = new Intent(ListaProductos.this, AgregarProductoActivity.class);
            startActivity(i);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dbHelper != null && productos != null && adapter != null) {
            productos.clear();
            productos.addAll(cargarProductosDesdeBD());
            adapter.notifyDataSetChanged();
        }
    }

    private List<Producto> cargarProductosDesdeBD() {
        List<Producto> lista = new ArrayList<>();

        Cursor cursor = dbHelper.obtenerTodosLosProductos();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                double precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio"));
                String descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));
                int imagenRes = cursor.getInt(cursor.getColumnIndexOrThrow("imagenRes"));

                // usamos el constructor con id
                Producto p = new Producto(id, nombre, precio, descripcion, imagenRes);
                lista.add(p);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return lista;
    }


}

