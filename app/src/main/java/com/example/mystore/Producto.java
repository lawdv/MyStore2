package com.example.mystore;

public class Producto {

    //sumo campos para la BD
    private int id;
    private String nombre;
    private double precio;
    private String descripcion;
    private int imagenResId; // id del drawable

    public Producto(String nombre, double precio, String descripcion, int imagenResId) {
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.imagenResId = imagenResId;
    }
    public Producto(int id, String nombre, double precio, String descripcion, int imagenResId) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.imagenResId = imagenResId;
    }

    //obtener el nuevo get
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public String getDescripcion() { return descripcion; }
    public int getImagenResId() { return imagenResId; }

//para insertar
    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setImagenResId(int imagenResId) { this.imagenResId = imagenResId; }

}
