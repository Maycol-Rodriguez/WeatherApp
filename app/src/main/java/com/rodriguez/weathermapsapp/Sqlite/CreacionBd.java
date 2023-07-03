package com.rodriguez.weathermapsapp.Sqlite;

public class CreacionBd {
    public static final String TABLA_FAVORITOS = "favoritos";

    public static final String DB_NAME = "favoritos.db";
    public static final int DB_VERSION = 1;

    public static final String CAMPO_ID = "id";
    public static final String CAMPO_CIUDAD = "ciudad";
    public static final String CAMPO_TEMPERATURA = "temperatura";
    public static final String CAMPO_FECHA = "fecha";
    public static final String CAMPO_DESCRIPCION = "descripcion";

    public static final String CAMPO_IMAGEN = "imagen";


    public static final String CREAR_TABLA_FAVORITOS = "CREATE TABLE " + TABLA_FAVORITOS + " (" + CAMPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CAMPO_CIUDAD + " TEXT, " + CAMPO_TEMPERATURA + " TEXT, " + CAMPO_FECHA + " TEXT, " + CAMPO_DESCRIPCION + " TEXT, " + CAMPO_IMAGEN + " TEXT)";
}
