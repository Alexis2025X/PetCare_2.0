package com.alexis.petcare20;
public class chatMessage {
    public boolean posicion; //izquierdo o derecho.
    public String mensaje;
    public chatMessage(boolean posicion, String mensaje){
        super();
        this.posicion = posicion;
        this.mensaje = mensaje;
    }
}