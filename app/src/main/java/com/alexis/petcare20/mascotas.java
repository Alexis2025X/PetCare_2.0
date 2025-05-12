package com.alexis.petcare20;




public class mascotas {

    //private static final String SQLdbMascotas = "CREATE TABLE Mascota (idMascota INTEGER PRIMARY KEY AUTOINCREMENT, dueño Text ,nombre TEXT, edad TEXT,raza TEXT,problemas_medicos TEXT,foto TEXT)";

    String idMascota;
    String dueño;
    String nombre;
    String edad;
    String raza;
    String problemas_medicos;
    String foto;

    public mascotas(String idMascota, String dueño, String nombre, String edad, String raza, String problemas_medicos, String foto) {
        this.dueño = dueño;
        this.edad = edad;
        this.foto = foto;
        this.idMascota = idMascota;
        this.nombre = nombre;
        this.problemas_medicos = problemas_medicos;
        this.raza = raza;
    }

    public String getDueño() {
        return dueño;
    }

    public void setDueño(String dueño) {
        this.dueño = dueño;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(String idMascota) {
        this.idMascota = idMascota;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProblemas_medicos() {
        return problemas_medicos;
    }

    public void setProblemas_medicos(String problemas_medicos) {
        this.problemas_medicos = problemas_medicos;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }
}
