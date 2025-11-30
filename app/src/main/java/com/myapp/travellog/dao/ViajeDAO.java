package com.myapp.travellog.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.myapp.travellog.db.DatabaseHelper;
import com.myapp.travellog.model.Viaje;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para la tabla 'viajes'.
 * Esta clase encapsula toda la lógica para acceder y manipular los datos de los viajes
 * en la base de datos, separando la lógica de negocio de la lógica de acceso a datos.
 */
public class ViajeDAO {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    private String[] allColumns = {
            DatabaseHelper.COLUMN_VIAJE_ID,
            DatabaseHelper.COLUMN_VIAJE_NOMBRE,
            DatabaseHelper.COLUMN_VIAJE_FECHA,
            DatabaseHelper.COLUMN_VIAJE_ID_USUARIO
    };

    /**
     * Constructor que inicializa el DatabaseHelper.
     * @param context El contexto de la aplicación.
     */
    public ViajeDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    /**
     * Abre una conexión escribible a la base de datos.
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Cierra la conexión a la base de datos.
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * Crea un nuevo viaje en la base de datos.
     * @return El objeto Viaje recién creado, o null si falla.
     */
    public Viaje createViaje(int idUsuario, String nombreViaje, String fecha) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_VIAJE_ID_USUARIO, idUsuario);
        values.put(DatabaseHelper.COLUMN_VIAJE_NOMBRE, nombreViaje);
        values.put(DatabaseHelper.COLUMN_VIAJE_FECHA, fecha);

        long insertId = database.insert(DatabaseHelper.TABLE_VIAJES, null, values);
        if (insertId == -1) {
            return null;
        }

        Cursor cursor = database.query(DatabaseHelper.TABLE_VIAJES, allColumns, DatabaseHelper.COLUMN_VIAJE_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Viaje newViaje = cursorToViaje(cursor);
        cursor.close();
        return newViaje;
    }

    /**
     * Actualiza un viaje existente.
     * @return El número de filas afectadas (debería ser 1 si fue exitoso).
     */
    public int updateViaje(int idViaje, String nombreViaje, String fecha) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_VIAJE_NOMBRE, nombreViaje);
        values.put(DatabaseHelper.COLUMN_VIAJE_FECHA, fecha);

        return database.update(DatabaseHelper.TABLE_VIAJES, values, DatabaseHelper.COLUMN_VIAJE_ID + " = " + idViaje, null);
    }

    /**
     * Elimina un viaje de la base de datos.
     */
    public void deleteViaje(int idViaje) {
        System.out.println("Viaje eliminado con id: " + idViaje);
        database.delete(DatabaseHelper.TABLE_VIAJES, DatabaseHelper.COLUMN_VIAJE_ID + " = " + idViaje, null);
    }

    /**
     * Obtiene todos los viajes para un usuario específico.
     * @return Una lista de objetos Viaje.
     */
    public List<Viaje> getViajesByUsuario(int idUsuario) {
        List<Viaje> viajes = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_VIAJES, allColumns, DatabaseHelper.COLUMN_VIAJE_ID_USUARIO + " = " + idUsuario, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Viaje viaje = cursorToViaje(cursor);
            viajes.add(viaje);
            cursor.moveToNext();
        }
        cursor.close();
        return viajes;
    }
    
    /**
     * Obtiene un solo viaje por su ID.
     * @return El objeto Viaje o null si no se encuentra.
     */
    public Viaje getViajeById(int idViaje) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_VIAJES, allColumns, DatabaseHelper.COLUMN_VIAJE_ID + " = " + idViaje, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Viaje viaje = cursorToViaje(cursor);
            cursor.close();
            return viaje;
        } else {
            return null;
        }
    }

    /**
     * Mapea un objeto Cursor a un objeto Viaje.
     */
    private Viaje cursorToViaje(Cursor cursor) {
        Viaje viaje = new Viaje();
        viaje.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_VIAJE_ID)));
        viaje.setNombreViaje(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_VIAJE_NOMBRE)));
        viaje.setFecha(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_VIAJE_FECHA)));
        viaje.setIdUsuario(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_VIAJE_ID_USUARIO)));
        return viaje;
    }
}
