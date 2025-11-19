package com.example.mystore;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AgregarProductoActivity extends AppCompatActivity {

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);

        dbHelper = new DBHelper(this);

        EditText edtNombre = findViewById(R.id.edtNombreProducto);
        EditText edtPrecio = findViewById(R.id.edtPrecioProducto);
        EditText edtDescripcion = findViewById(R.id.edtDescripcionProducto);
        Button btnGuardar = findViewById(R.id.btnGuardarProducto);

        btnGuardar.setOnClickListener(v -> {
            String nombre = edtNombre.getText().toString().trim();
            String precioStr = edtPrecio.getText().toString().trim();
            String descripcion = edtDescripcion.getText().toString().trim();

            if (nombre.isEmpty() || precioStr.isEmpty()) {
                Toast.makeText(this, "Nombre y precio son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            double precio;
            try {
                precio = Double.parseDouble(precioStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Precio inválido", Toast.LENGTH_SHORT).show();
                return;
            }

            // Imagen por defecto por el momento seria asi para nuevos productos
            int imagenRes = R.drawable.camiseta;

            long res = dbHelper.insertarProducto(nombre, precio, descripcion, imagenRes);

            if (res != -1) {
                Toast.makeText(this, "Producto guardado", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
