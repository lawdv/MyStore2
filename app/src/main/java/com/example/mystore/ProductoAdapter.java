package com.example.mystore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AlertDialog;
import android.content.Intent;



import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {

    //sumamos DB con helper
    private DBHelper dbHelper;
    private Context context;
    private List<Producto> listaProductos;


    public ProductoAdapter(List<Producto> listaProductos, Context context) {
        this.listaProductos = listaProductos;
        this.context = context;
        this.dbHelper = new DBHelper(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProducto;
        TextView txtNombre, txtPrecio;
        Button btnAgregarCarrito; // nuevo

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProducto = itemView.findViewById(R.id.imgProducto);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtPrecio = itemView.findViewById(R.id.txtPrecio);
            btnAgregarCarrito = itemView.findViewById(R.id.btnAgregarCarrito);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto producto = listaProductos.get(position);

        holder.txtNombre.setText(producto.getNombre());
        holder.txtPrecio.setText("$" + producto.getPrecio());
        holder.imgProducto.setImageResource(producto.getImagenResId());

        holder.btnAgregarCarrito.setOnClickListener(v -> {
            dbHelper.agregarAlCarrito(producto.getId());
            Toast.makeText(context, "Agregado al carrito", Toast.LENGTH_SHORT).show();
        });

        //eliminar productos
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Eliminar producto")
                    .setMessage("¿Seguro que quieres eliminar \"" + producto.getNombre() + "\"?")
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        int pos = holder.getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            Producto p = listaProductos.get(pos);

                            // Borrar de la BD
                            dbHelper.eliminarProducto(p.getId());

                            // Borrar de la lista en memoria
                            listaProductos.remove(pos);
                            notifyItemRemoved(pos);

                            Toast.makeText(context, "Producto eliminado", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();

            return true;
        });
        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, EditarProductoActivity.class);
            i.putExtra("id", producto.getId());
            i.putExtra("nombre", producto.getNombre());
            i.putExtra("precio", producto.getPrecio());
            i.putExtra("descripcion", producto.getDescripcion());
            i.putExtra("imagenRes", producto.getImagenResId());
            context.startActivity(i);
        });

    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }
}
