package com.example.mystore;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CarritoActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private ListView listCarrito;
    private TextView txtTotal;

    // para saber qué idProducto corresponde a cada ítem en la lista
    private ArrayList<Integer> idsProductosEnCarrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        dbHelper = new DBHelper(this);

        listCarrito = findViewById(R.id.listCarrito);
        txtTotal = findViewById(R.id.txtTotal);

        cargarCarrito();

        // al tocar un ítem, lo eliminamos del carrito
        listCarrito.setOnItemClickListener((parent, view, position, id) -> {
            int idProducto = idsProductosEnCarrito.get(position);
            dbHelper.eliminarDelCarrito(idProducto);
            Toast.makeText(this, "Producto eliminado del carrito", Toast.LENGTH_SHORT).show();
            cargarCarrito(); // recargar lista y total
        });
    }

    private void cargarCarrito() {
        ArrayList<String> items = new ArrayList<>();
        idsProductosEnCarrito = new ArrayList<>();
        double totalGeneral = 0;

        Cursor cursor = dbHelper.obtenerCarrito();
        if (cursor.moveToFirst()) {
            do {
                int idProducto = cursor.getInt(cursor.getColumnIndexOrThrow("idProducto"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                double precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio"));
                int cantidad = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad"));

                double totalItem = precio * cantidad;
                totalGeneral += totalItem;

                items.add(nombre + " x" + cantidad + "  ($" + totalItem + ")");
                idsProductosEnCarrito.add(idProducto);

            } while (cursor.moveToNext());
        } else {
            items.add("Carrito vacío");
        }
        cursor.close();

        listCarrito.setAdapter(
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items)
        );

        txtTotal.setText("Total: $" + totalGeneral);
    }
}
