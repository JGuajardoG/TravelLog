package com.myapp.travellog.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bitacoraviajera.db";
    private static final int DATABASE_VERSION = 1;

    // Tabla usuarios
    public static final String TABLE_USUARIOS = "usuarios";
    public static final String COLUMN_USUARIO_ID = "id";
    public static final String COLUMN_USUARIO_NOMBRE = "nombre";
    public static final String COLUMN_USUARIO_USUARIO = "usuario";
    public static final String COLUMN_USUARIO_PASSWORD = "password";

    // Tabla viajes
    public static final String TABLE_VIAJES = "viajes";
    public static final String COLUMN_VIAJE_ID = "id";
    public static final String COLUMN_VIAJE_ID_USUARIO = "id_usuario";
    public static final String COLUMN_VIAJE_NOMBRE = "nombre_viaje";
    public static final String COLUMN_VIAJE_FECHA = "fecha";

    // Tabla lugares
    public static final String TABLE_LUGARES = "lugares";
    public static final String COLUMN_LUGAR_ID = "id";
    public static final String COLUMN_LUGAR_ID_VIAJE = "id_viaje";
    public static final String COLUMN_LUGAR_NOMBRE = "nombre_lugar";
    public static final String COLUMN_LUGAR_COMENTARIO = "comentario";
    public static final String COLUMN_LUGAR_FOTO_URI = "foto_uri";
    public static final String COLUMN_LUGAR_LATITUD = "latitud";
    public static final String COLUMN_LUGAR_LONGITUD = "longitud";

    // SQL para crear tabla usuarios
    private static final String TABLE_CREATE_USUARIOS =
            "CREATE TABLE " + TABLE_USUARIOS + " (" +
                    COLUMN_USUARIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USUARIO_NOMBRE + " TEXT, " +
                    COLUMN_USUARIO_USUARIO + " TEXT, " +
                    COLUMN_USUARIO_PASSWORD + " TEXT" +
                    ");";

    // SQL para crear tabla viajes
    private static final String TABLE_CREATE_VIAJES =
            "CREATE TABLE " + TABLE_VIAJES + " (" +
                    COLUMN_VIAJE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_VIAJE_ID_USUARIO + " INTEGER, " +
                    COLUMN_VIAJE_NOMBRE + " TEXT, " +
                    COLUMN_VIAJE_FECHA + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_VIAJE_ID_USUARIO + ") REFERENCES " + TABLE_USUARIOS + "(" + COLUMN_USUARIO_ID + ")" +
                    ");";

    // SQL para crear tabla lugares
    private static final String TABLE_CREATE_LUGARES =
            "CREATE TABLE " + TABLE_LUGARES + " (" +
                    COLUMN_LUGAR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LUGAR_ID_VIAJE + " INTEGER, " +
                    COLUMN_LUGAR_NOMBRE + " TEXT, " +
                    COLUMN_LUGAR_COMENTARIO + " TEXT, " +
                    COLUMN_LUGAR_FOTO_URI + " TEXT, " +
                    COLUMN_LUGAR_LATITUD + " REAL, " +
                    COLUMN_LUGAR_LONGITUD + " REAL, " +
                    "FOREIGN KEY(" + COLUMN_LUGAR_ID_VIAJE + ") REFERENCES " + TABLE_VIAJES + "(" + COLUMN_VIAJE_ID + ")" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_USUARIOS);
        db.execSQL(TABLE_CREATE_VIAJES);
        db.execSQL(TABLE_CREATE_LUGARES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LUGARES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIAJES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        onCreate(db);
    }
}
