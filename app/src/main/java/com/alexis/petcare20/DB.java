package com.alexis.petcare20;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {
    //Nombre de la base de datos y version

    private static final String DATABASE_NAME = "petCore";
    private static final int DATABASE_VERSION = 1;
    //Cración de la base de datos
    //tabla cuentas
    private static final String SQLdbCuentas = "CREATE TABLE Cuentas (idCuenta INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, usuario TEXT, contraseña TEXT, email TEXT)";
    //tabla mascotas
    private static final String SQLdbMascotas = "CREATE TABLE Mascota (idMascota INTEGER PRIMARY KEY AUTOINCREMENT, dueño Text ,nombre TEXT, edad TEXT,raza TEXT,problemas_medicos TEXT,foto TEXT)";
    //tabla de Citas
    private static final String SQLdbCitas = "CREATE TABLE Citas (idCitas INTEGER PRIMARY KEY AUTOINCREMENT, mascota  Text, dueño Text, fecha DATETIME, clinica TEXT, nota TEXT)";

    //Contexto de la base de datos
    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creación de la base de datos (Inicia la ejecuación para crearla)
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLdbCuentas);
        db.execSQL(SQLdbMascotas);
        db.execSQL(SQLdbCitas);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Actualizar la estrucutra de la base de datos si es necesario
    }
    //Métodos para administrar la base de datos
    public String administrar_cuentas(String accion, String[] datos) {
        try{
            //Escritura en la base de datos
            SQLiteDatabase db = getWritableDatabase();
            //Mensaje y consultas
            String mensaje = "ok", sql = "";
            switch (accion) {
                case "nuevo":
                    sql = "INSERT INTO Cuentas (nombre, usuario, contraseña, email) VALUES ('"+ datos[1] +"', '" + datos[2] + "', '" + datos[3] + "', '" + datos[4]+"')";
                    break;
                case "modificar":
                    sql = "UPDATE Cuentas SET nombre = '" + datos[1] + "', usuario = '" + datos[2] + "', contraseña = '" + datos[3] + "', email = '" + datos[4] + "' WHERE idCuenta = " + datos[0];
                    break;
                case "eliminar":
                    sql = "DELETE FROM Cuentas WHERE idCuenta = " + datos[0];
                    break;
            }
            db.execSQL(sql);
            db.close();
            return mensaje;
            //Excepción
        } catch (Exception e) {

            return e.getMessage();
        }

    }
    public String administrar_Mascota(String accion, String[] datos) {
        try{
            //Escritura en la base de datos
            SQLiteDatabase db = getWritableDatabase();
            //Mensaje y consultas
            String mensaje = "ok", sql = "";
            switch (accion) {
                case "nuevo":
                    sql = "INSERT INTO Mascota (dueño, nombre, edad,raza,problemas_medicos,foto) VALUES ('"+ datos[1] +"', '" + datos[2] + "', '" + datos[3] + "', '" + datos[4]+ "', '" + datos[5] + "', '" + datos[6] + "')";
                    break;
                case "modificar":
                    sql = "UPDATE Mascota SET dueño = '" + datos[1] + "', nombre = '" + datos[2] + "', edad = '" + datos[3] + "', raza = '" + datos[4] + "', problemas_medicos = '" + datos[5] + "', foto = '" + datos[6] + "' WHERE idMascota = " + datos[0];
                    break;
                case "eliminar":
                    sql = "DELETE FROM Mascota WHERE idMascota = " + datos[0];
                    break;
            }
            db.execSQL(sql);
            db.close();
            return mensaje;
            //Excepción
        } catch (Exception e) {

            return e.getMessage();
        }

    }
    public String administrar_Citas(String accion, String[] datos) {
        try{
            //Escritura en la base de datos
            SQLiteDatabase db = getWritableDatabase();
            //Mensaje y consultas
            String mensaje = "ok", sql = "";
            switch (accion) {
                case "nuevo":
                    sql = "INSERT INTO Citas (mascotaId, dueño, fecha, clinica,nota) VALUES ("+ datos[1] +", '" + datos[2] + "', '" + datos[3] + "', '" + datos[4]+ "', '" + datos[5] + "', '" + datos[6]+ "')";
                    break;
                case "modificar":
                    sql = "UPDATE Citas SET mascotaId = " + datos[1] + ", dueño = " + datos[2] + ", fecha = '" + datos[3] + "', clinica = '" + datos[4] + "', nota = '" + datos[5] + "' WHERE idCitas = " + datos[0];
                    break;
                case "eliminar":
                    sql = "DELETE FROM Citas WHERE idCitas = " + datos[0];
                    break;
            }
            db.execSQL(sql);
            db.close();
            return mensaje;
            //Excepción
        } catch (Exception e) {

            return e.getMessage();
        }

    }
    public Cursor lista_cuentas(){
        //bd es el ejecutador de consultas
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM Cuentas", null);
    }
    public Cursor lista_mascotas(){
        //bd es el ejecutador de consultas
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM Mascota", null);
    }
    public Cursor lista_Citas(){
        //bd es el ejecutador de consultas
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM Citas", null);
    }

}