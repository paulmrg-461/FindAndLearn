package com.singular.findandlearn.controller;

/**
 * Created by Paul Realpe on 24/03/2018.
 */

public class Constantes {
    /**
     * Transición Home -> Detalle
     */
    public static final int CODIGO_DETALLE = 100;

    /**
     * Transición Detalle -> Actualización
     */
    public static final int CODIGO_ACTUALIZACION = 101;

    /**
     * Puerto que utilizas para la conexión.
     * Dejalo en blanco si no has configurado esta carácteristica.
     */
    //private static final String HOST = "http://findandlearn.000webhostapp.com/findandlearnws/";
    /**
     * Dirección IP de genymotion o AVD
     * private static final String IP = "http://10.0.3.2:";
     */

    /**
     * URLs del Web Service
     */
    public static final String GET_STUDENT = "http://findandlearn.000webhostapp.com/findandlearnws/obtener_estudiante.php";
    public static final String GET_STUDENT_BY_ID = "/I%20Wish/obtener_meta_por_id.php";
    public static final String DELETE_STUDENT = "/I%20Wish/borrar_meta.php";
    public static final String INSERT_STUDENT = "/I%20Wish/insertar_meta.php";

    /**
     * Clave para el valor extra que representa al identificador de una meta
     */
    public static final String EXTRA_ID = "IDEXTRA";
}
