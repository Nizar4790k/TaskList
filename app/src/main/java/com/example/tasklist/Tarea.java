package com.example.tasklist;

public class Tarea {

    private String titulo;
    private String descripcion;
    private String fecha;
    private String hora;
    private String id;


    public Tarea(){

    }


    public Tarea(String id,String titulo, String descripcion, String fecha, String hora) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getId() {
        return id;
    }
}
