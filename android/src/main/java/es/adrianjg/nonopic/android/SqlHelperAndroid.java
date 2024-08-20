package es.adrianjg.nonopic.android;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import es.adrianjg.nonopic.Puntuacion;
import es.adrianjg.nonopic.SqlHelper;


/**
 * Manejador de la base de datos para Android.
 */

public class SqlHelperAndroid extends SQLiteOpenHelper implements SqlHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "NonoPic.db";

    public SqlHelperAndroid(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Inicializa la base de datos creando las tablas necesarias.
     *
     * @param db La base de datos en uso
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE puntuaciones (id INTEGER PRIMARY KEY, minutos INTEGER, segundos INTEGER)");
    }

    /**
     * Actualiza la base de datos a una nueva versión.
     * @param db La base de datos en uso
     * @param oldVersion Versión antigua
     * @param newVersion Versión nueva
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertarPuntuacion(Puntuacion puntuacion) {
        if (esMejorPuntuacion(puntuacion)) {
            SQLiteDatabase db = this.getWritableDatabase();

            SQLiteStatement statementDelete = db.compileStatement("DELETE FROM puntuaciones WHERE id = ?");
            statementDelete.bindLong(1, puntuacion.getId());
            statementDelete.execute();
            statementDelete.close();

            SQLiteStatement statementInsert = db.compileStatement("INSERT INTO puntuaciones (id, minutos, segundos) VALUES (?, ?, ?)");
            statementInsert.bindLong(1, puntuacion.getId());
            statementInsert.bindLong(2, puntuacion.getMinutos());
            statementInsert.bindLong(3, puntuacion.getSegundos());
            statementInsert.execute();
            statementInsert.close();

            db.close();
        }
    }

    public Puntuacion leerPuntuacion(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {"id", "minutos", "segundos"};
        String selection = "id = ?";
        String[] selectionArgs = {Integer.toString(id)};
        Cursor c = db.query("puntuaciones", projection, selection, selectionArgs, null, null, null);
        if (c.getCount() == 0) {
            c.close();
            db.close();
            return new Puntuacion(id);
        } else {
            c.moveToFirst();
            int minutos = (int) c.getLong(1);
            int segundos = (int) c.getLong(2);
            c.close();
            db.close();
            return new Puntuacion(id, minutos, segundos, true);
        }

    }

    /**
     * Comprueba si una puntuación es mejor comparada con la ya almacenada
     * @param puntuacionNueva Puntuación a comparar
     * @return True si lo es. False si no lo es.
     */
    public boolean esMejorPuntuacion(Puntuacion puntuacionNueva) {
        Puntuacion puntuacionAnterior = leerPuntuacion(puntuacionNueva.getId());
        if (puntuacionAnterior.isCompletado()) {
            if (puntuacionAnterior.getMinutos() > puntuacionNueva.getMinutos()) {
                return true;
            } else if (puntuacionAnterior.getMinutos() == puntuacionNueva.getMinutos() && puntuacionAnterior.getSegundos() > puntuacionNueva.getSegundos()) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }

    }


}
