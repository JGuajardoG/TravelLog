package com.myapp.travellog.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.myapp.travellog.db.DatabaseHelper;
import com.myapp.travellog.model.Lugar;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para la tabla 'lugares'.
 * Encapsula el acceso a la base de datos para las operaciones CRUD de los lugares.
 */
public class LugarDAO {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    private String[] allColumns = {
            DatabaseHelper.COLUMN_LUGAR_ID,
            DatabaseHelper.COLUMN_LUGAR_NOMBRE,
            DatabaseHelper.COLUMN_LUGAR_COMENTARIO,
            DatabaseHelper.COLUMN_LUGAR_FOTO_URI,
            DatabaseHelper.COLUMN_LUGAR_LATITUD,
            DatabaseHelper.COLUMN_LUGAR_LONGITUD,
            DatabaseHelper.COLUMN_LUGAR_ID_VIAJE
    };

    public LugarDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * Crea un nuevo lugar en la base de datos.
     * @return El objeto Lugar recién creado.
     */
    public Lugar createLugar(int idViaje, String nombre, String comentario, String fotoUri, double latitud, double longitud) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_LUGAR_ID_VIAJE, idViaje);
        values.put(DatabaseHelper.COLUMN_LUGAR_NOMBRE, nombre);
        values.put(DatabaseHelper.COLUMN_LUGAR_COMENTARIO, comentario);
        values.put(DatabaseHelper.COLUMN_LUGAR_FOTO_URI, fotoUri);
        values.put(DatabaseHelper.COLUMN_LUGAR_LATITUD, latitud);
        values.put(DatabaseHelper.COLUMN_LUGAR_LONGITUD, longitud);

        long insertId = database.insert(DatabaseHelper.TABLE_LUGARES, null, values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_LUGARES, allColumns, DatabaseHelper.COLUMN_LUGAR_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Lugar newLugar = cursorToLugar(cursor);
        cursor.close();
        return newLugar;
    }

    /**
     * Actualiza el nombre y comentario de un lugar existente.
     * @return El número de filas afectadas.
     */
    public int updateLugar(int idLugar, String nombre, String comentario) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_LUGAR_NOMBRE, nombre);
        values.put(DatabaseHelper.COLUMN_LUGAR_COMENTARIO, comentario);

        return database.update(DatabaseHelper.TABLE_LUGARES, values, DatabaseHelper.COLUMN_LUGAR_ID + " = " + idLugar, null);
    }

    /**
     * Elimina un lugar de la base de datos por su ID.
     */
    public void deleteLugar(int idLugar) {
        database.delete(DatabaseHelper.TABLE_LUGARES, DatabaseHelper.COLUMN_LUGAR_ID + " = " + idLugar, null);
    }

    /**
     * Obtiene todos los lugares asociados a un viaje específico.
     * @return Una lista de objetos Lugar.
     */
    public List<Lugar> getLugaresByViaje(int idViaje) {
        List<Lugar> lugares = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_LUGARES, allColumns, DatabaseHelper.COLUMN_LUGAR_ID_VIAJE + " = " + idViaje, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Lugar lugar = cursorToLugar(cursor);
            lugares.add(lugar);
            cursor.moveToNext();
        }
        cursor.close();
        return lugares;
    }

    /**
     * Obtiene un solo lugar por su ID.
     * @return El objeto Lugar, o null si no se encuentra.
     */
    public Lugar getLugarById(int idLugar) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_LUGARES, allColumns, DatabaseHelper.COLUMN_LUGAR_ID + " = " + idLugar, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Lugar lugar = cursorToLugar(cursor);
            cursor.close();
            return lugar;
        }
        return null;
    }

    /**
     * Método privado de utilidad para convertir una fila del Cursor a un objeto Lugar.
     */
    private Lugar cursorToLugar(Cursor cursor) {
        Lugar lugar = new Lugar();
        lugar.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LUGAR_ID)));
        lugar.setNombreLugar(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LUGAR_NOMBRE)));
        lugar.setComentario(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LUGAR_COMENTARIO)));
        lugar.setFotoUri(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LUGAR_FOTO_URI)));
        lugar.setLatitud(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LUGAR_LATITUD)));
        lugar.setLongitud(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LUGAR_LONGITUD)));
        lugar.setIdViaje(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LUGAR_ID_VIAJE)));
        return lugar;
    }
}
