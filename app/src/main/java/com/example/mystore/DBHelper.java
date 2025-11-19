package com.example.mystore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    //nombre de la bd interna mystore.db
    private static final String DB_NAME = "mystore.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_USUARIOS = "usuarios";
    public static final String TABLE_PRODUCTOS = "productos";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla de usuarios
        db.execSQL("CREATE TABLE " + TABLE_USUARIOS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE," +
                "password TEXT)");

        // Crear tabla de productos
        db.execSQL("CREATE TABLE " + TABLE_PRODUCTOS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT," +
                "precio REAL," +
                "descripcion TEXT," +
                "imagenRes INTEGER)");
        // tabla carrito
        db.execSQL("CREATE TABLE carrito (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "idProducto INTEGER," +
                "cantidad INTEGER)");

        // Usuario por defecto que teniamos en el proyecto anterior admin / 123
        ContentValues admin = new ContentValues();
        admin.put("username", "admin");
        admin.put("password", "1234");
        db.insert(TABLE_USUARIOS, null, admin);

        // Productos de ejemplo del demo
        insertarProductoInicial(db, "Camiseta", 50000, "Camiseta deportiva", R.drawable.camiseta);
        insertarProductoInicial(db, "Reloj", 120000, "Reloj digital", R.drawable.reloj);
        insertarProductoInicial(db, "Zapatos", 200000, "Zapatos cómodos", R.drawable.zapatos);

    }

    private void insertarProductoInicial(SQLiteDatabase db, String nombre, double precio,
                                         String descripcion, int imagenRes) {
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("precio", precio);
        values.put("descripcion", descripcion);
        values.put("imagenRes", imagenRes);
        db.insert(TABLE_PRODUCTOS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTOS);
        db.execSQL("DROP TABLE IF EXISTS carrito");
        onCreate(db);
    }

    // Login con nuevos datos de BD

    public boolean validarUsuario(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id FROM " + TABLE_USUARIOS + " WHERE username = ? AND password = ?",
                new String[]{username, password}
        );

        boolean existe = cursor.moveToFirst();
        cursor.close();
        return existe;
    }

    // registro de usuarios
    public boolean registrarUsuario(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        long result = db.insert(TABLE_USUARIOS, null, values);
        return result != -1;
    }

    // productos todo lo relacionado a la funcionalidad

    public long insertarProducto(String nombre, double precio, String descripcion, int imagenRes) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("precio", precio);
        values.put("descripcion", descripcion);
        values.put("imagenRes", imagenRes);
        return db.insert(TABLE_PRODUCTOS, null, values);
    }

    public Cursor obtenerTodosLosProductos() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTOS, null);
    }

    //Carrito todo lo relacionado

    // carrito de agregar productos (si existe suma +1)
    public void agregarAlCarrito(int idProducto) {
        SQLiteDatabase db = getWritableDatabase();


        Cursor cursor = db.rawQuery("SELECT cantidad FROM carrito WHERE idProducto=?",
                new String[]{String.valueOf(idProducto)});

        if (cursor.moveToFirst()) {

            int cantidadActual = cursor.getInt(0);
            ContentValues values = new ContentValues();
            values.put("cantidad", cantidadActual + 1);
            db.update("carrito", values, "idProducto=?", new String[]{String.valueOf(idProducto)});
        } else {

            ContentValues values = new ContentValues();
            values.put("idProducto", idProducto);
            values.put("cantidad", 1);
            db.insert("carrito", null, values);
        }
        cursor.close();
    }


    public Cursor obtenerCarrito() {
        SQLiteDatabase db = getReadableDatabase();

        // Realizar la consulta para obtener los productos del carrito
        String query = "SELECT c.idProducto, c.cantidad, p.nombre, p.precio, p.imagenRes " +
                "FROM carrito c INNER JOIN productos p ON c.idProducto = p.id";

        return db.rawQuery(query, null);
    }


    public void limpiarCarrito() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("carrito", null, null);
    }
    public void eliminarDelCarrito(int idProducto) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("carrito", "idProducto = ?", new String[]{String.valueOf(idProducto)});
    }

    // Eliminar producto
    public void eliminarProducto(int idProducto) {
        SQLiteDatabase db = getWritableDatabase();

        // Primero lo quitamos del carrito (si está)
        db.delete("carrito", "idProducto = ?", new String[]{String.valueOf(idProducto)});

        // Luego lo borramos de la tabla productos
        db.delete(TABLE_PRODUCTOS, "id = ?", new String[]{String.valueOf(idProducto)});
    }

    //edicion del producto
    public boolean actualizarProducto(int id, String nombre, double precio, String descripcion, int imagenRes) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("precio", precio);
        values.put("descripcion", descripcion);
        values.put("imagenRes", imagenRes);

        int filas = db.update(TABLE_PRODUCTOS, values, "id = ?", new String[]{String.valueOf(id)});
        return filas > 0;
    }




}
