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
     * Debe llamarse antes de cualquier operación de base de datos.
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Cierra la conexión a la base de datos.
     * Debe llamarse cuando ya no se necesite acceder a la base de datos para liberar recursos.
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * Crea un nuevo viaje en la base de datos.
     * @param idUsuario El ID del usuario al que pertenece el viaje.
     * @param nombreViaje El nombre del nuevo viaje.
     * @param fecha La fecha del nuevo viaje.
     * @return El objeto Viaje recién creado, o null si la inserción falla.
     */
    public Viaje createViaje(int idUsuario, String nombreViaje, String fecha) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_VIAJE_ID_USUARIO, idUsuario);
        values.put(DatabaseHelper.COLUMN_VIAJE_NOMBRE, nombreViaje);
        values.put(DatabaseHelper.COLUMN_VIAJE_FECHA, fecha);

        long insertId = database.insert(DatabaseHelper.TABLE_VIAJES, null, values);
        if (insertId == -1) {
            return null; // Retorna null si hubo un error en la inserción.
        }

        Cursor cursor = database.query(DatabaseHelper.TABLE_VIAJES, allColumns, DatabaseHelper.COLUMN_VIAJE_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Viaje newViaje = cursorToViaje(cursor);
        cursor.close();
        return newViaje;
    }

    /**
     * Actualiza un viaje existente en la base de datos.
     * @param idViaje El ID del viaje a actualizar.
     * @param nombreViaje El nuevo nombre para el viaje.
     * @param fecha La nueva fecha para el viaje.
     * @return El número de filas afectadas (debería ser 1 si fue exitoso).
     */
    public int updateViaje(int idViaje, String nombreViaje, String fecha) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_VIAJE_NOMBRE, nombreViaje);
        values.put(DatabaseHelper.COLUMN_VIAJE_FECHA, fecha);

        return database.update(DatabaseHelper.TABLE_VIAJES, values, DatabaseHelper.COLUMN_VIAJE_ID + " = " + idViaje, null);
    }

    /**
     * Elimina un viaje de la base de datos usando su ID.
     * @param idViaje El ID del viaje a eliminar.
     */
    public void deleteViaje(int idViaje) {
        database.delete(DatabaseHelper.TABLE_VIAJES, DatabaseHelper.COLUMN_VIAJE_ID + " = " + idViaje, null);
    }

    /**
     * Obtiene todos los viajes pertenecientes a un usuario específico.
     * @param idUsuario El ID del usuario cuyos viajes se quieren recuperar.
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
     * Obtiene un único viaje por su ID.
     * @param idViaje El ID del viaje a buscar.
     * @return El objeto Viaje encontrado, o null si no existe.
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
     * Método privado de utilidad para convertir una fila del Cursor en un objeto Viaje.
     * @param cursor El Cursor posicionado en la fila deseada.
     * @return Un objeto Viaje con los datos del cursor.
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
