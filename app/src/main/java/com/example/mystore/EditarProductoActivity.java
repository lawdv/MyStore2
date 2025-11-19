package com.example.mystore;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditarProductoActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private int productoId;
    private int imagenRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_producto);

        dbHelper = new DBHelper(this);

        EditText edtNombre = findViewById(R.id.edtNombreEditar);
        EditText edtPrecio = findViewById(R.id.edtPrecioEditar);
        EditText edtDescripcion = findViewById(R.id.edtDescripcionEditar);
        ImageView imgProducto = findViewById(R.id.imgProductoEditar);
        Button btnGuardar = findViewById(R.id.btnGuardarCambios);

        // recibir datos
        productoId = getIntent().getIntExtra("id", -1);
        String nombre = getIntent().getStringExtra("nombre");
        double precio = getIntent().getDoubleExtra("precio", 0);
        String descripcion = getIntent().getStringExtra("descripcion");
        imagenRes = getIntent().getIntExtra("imagenRes", 0);

        // llenar datos del xml
        edtNombre.setText(nombre);
        edtPrecio.setText(String.valueOf(precio));
        edtDescripcion.setText(descripcion);
        imgProducto.setImageResource(imagenRes);

        btnGuardar.setOnClickListener(v -> {
            String newNombre = edtNombre.getText().toString();
            double newPrecio = Double.parseDouble(edtPrecio.getText().toString());
            String newDescripcion = edtDescripcion.getText().toString();

            boolean actualizado = dbHelper.actualizarProducto(
                    productoId, newNombre, newPrecio, newDescripcion, imagenRes
            );

            if (actualizado) {
                Toast.makeText(this, "Producto actualizado", Toast.LENGTH_SHORT).show();
                finish(); // volver a la lista ya que antes no aparecia
            } else {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
