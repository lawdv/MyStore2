package com.example.mystore;

public class Producto {
    private String nombre;
    private double precio;
    private int imagenResId; // id del drawable

    public Producto(String nombre, double precio, int imagenResId) {
        this.nombre = nombre;
        this.precio = precio;
        this.imagenResId = imagenResId;
    }

    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public int getImagenResId() { return imagenResId; }
}
