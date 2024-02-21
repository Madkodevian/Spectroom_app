package com.example.spectroom_app;

public class Titular {
    private String id;
    private String titulo;
    private String subtitulo;
    private int img;

    //Constructor sin argumentos requerido por Firebase
    public Titular() {
    }
    public Titular(String id, String titulo, String subtitulo, int img) {
        super();
        this.id = id;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}

