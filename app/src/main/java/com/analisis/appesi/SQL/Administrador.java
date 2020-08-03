package com.analisis.appesi.SQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Administrador extends SQLiteOpenHelper {
    public Administrador(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // DATOS DE LOGIN
        db.execSQL("create table login (correo text, contrasena text)");

        // DATOS DE USUARIO LOGEADO
        db.execSQL("create table usuario (id int, municipio_id int, nombre text, " +
                "ape_p text, ape_m text, email text, password text, direccion text, telefono text)");


        // DATOS DE LOTES DEL USUARIO LOGEADO
        db.execSQL("create table lotes (id int, userId int, municipioId int, localizacionId int, " +
                "nombre text, usoSuelo text, eliminado int, createdAt text, updatedAt text)");

        //DATOS DE LOCALIZACION
        db.execSQL("create table localizacion (id int, latitud text, longitud text, createdAt text, updatedAt text)");

        //POST LOCALIZACION
        db.execSQL("create table postlocalizacion (latitud text, longitud text, lote_id int, profundidad int, user_id int)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
