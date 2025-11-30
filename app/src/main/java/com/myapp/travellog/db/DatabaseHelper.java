package com.myapp.travellog.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Clase ayudante para gestionar la creación y actualización de la base de datos SQLite.
 * Define la estructura de las tablas y las constantes para los nombres de columnas.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Nombre y versión de la base de datos.
    private static final String DATABASE_NAME = "bitacoraviajera.db";
    private static final int DATABASE_VERSION = 1;

    // --- Tabla usuarios ---
    public static final String TABLE_USUARIOS = "usuarios";
    public static final String COLUMN_USUARIO_ID = "id";
    public static final String COLUMN_USUARIO_NOMBRE = "nombre";
    public static final String COLUMN_USUARIO_USUARIO = "usuario";
    public static final String COLUMN_USUARIO_PASSWORD = "password";

    // --- Tabla viajes ---
    public static final String TABLE_VIAJES = "viajes";
    public static final String COLUMN_VIAJE_ID = "id";
    public static final String COLUMN_VIAJE_ID_USUARIO = "id_usuario"; // Foreign Key
    public static final String COLUMN_VIAJE_NOMBRE = "nombre_viaje";
    public static final String COLUMN_VIAJE_FECHA = "fecha";

    // --- Tabla lugares ---
    public static final String TABLE_LUGARES = "lugares";
    public static final String COLUMN_LUGAR_ID = "id";
    public static final String COLUMN_LUGAR_ID_VIAJE = "id_viaje"; // Foreign Key
    public static final String COLUMN_LUGAR_NOMBRE = "nombre_lugar";
    public static final String COLUMN_LUGAR_COMENTARIO = "comentario";
    public static final String COLUMN_LUGAR_FOTO_URI = "foto_uri";
    public static final String COLUMN_LUGAR_LATITUD = "latitud";
    public static final String COLUMN_LUGAR_LONGITUD = "longitud";

    // --- Sentencias SQL para la creación de las tablas ---

    // SQL para crear la tabla de usuarios.
    private static final String TABLE_CREATE_USUARIOS =
            "CREATE TABLE " + TABLE_USUARIOS + " (" +
                    COLUMN_USUARIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USUARIO_NOMBRE + " TEXT, " +
                    COLUMN_USUARIO_USUARIO + " TEXT UNIQUE, " + // El nombre de usuario debería ser único.
                    COLUMN_USUARIO_PASSWORD + " TEXT" +
                    ");";

    // SQL para crear la tabla de viajes, con una clave foránea a la tabla de usuarios.
    private static final String TABLE_CREATE_VIAJES =
            "CREATE TABLE " + TABLE_VIAJES + " (" +
                    COLUMN_VIAJE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_VIAJE_ID_USUARIO + " INTEGER, " +
                    COLUMN_VIAJE_NOMBRE + " TEXT, " +
                    COLUMN_VIAJE_FECHA + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_VIAJE_ID_USUARIO + ") REFERENCES " + TABLE_USUARIOS + "(" + COLUMN_USUARIO_ID + ") ON DELETE CASCADE" + // ON DELETE CASCADE borra los viajes si se borra el usuario.
                    ");";

    // SQL para crear la tabla de lugares, con una clave foránea a la tabla de viajes.
    private static final String TABLE_CREATE_LUGARES =
            "CREATE TABLE " + TABLE_LUGARES + " (" +
                    COLUMN_LUGAR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LUGAR_ID_VIAJE + " INTEGER, " +
                    COLUMN_LUGAR_NOMBRE + " TEXT, " +
                    COLUMN_LUGAR_COMENTARIO + " TEXT, " +
                    COLUMN_LUGAR_FOTO_URI + " TEXT, " +
                    COLUMN_LUGAR_LATITUD + " REAL, " +
                    COLUMN_LUGAR_LONGITUD + " REAL, " +
                    "FOREIGN KEY(" + COLUMN_LUGAR_ID_VIAJE + ") REFERENCES " + TABLE_VIAJES + "(" + COLUMN_VIAJE_ID + ") ON DELETE CASCADE" + // ON DELETE CASCADE borra los lugares si se borra el viaje.
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Se llama una sola vez, cuando la base de datos se crea por primera vez.
     * Aquí se ejecuta el SQL para crear todas las tablas.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_USUARIOS);
        db.execSQL(TABLE_CREATE_VIAJES);
        db.execSQL(TABLE_CREATE_LUGARES);
    }

    /**
     * Se llama cuando la versión de la base de datos (DATABASE_VERSION) se incrementa.
     * Permite actualizar el esquema de la base de datos. En este caso, simplemente borra
     * las tablas antiguas y las vuelve a crear.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LUGARES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIAJES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        onCreate(db);
    }
}
