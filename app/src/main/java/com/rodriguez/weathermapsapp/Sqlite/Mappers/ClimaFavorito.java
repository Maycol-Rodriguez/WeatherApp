package com.rodriguez.weathermapsapp.Sqlite.Mappers;

public class ClimaFavorito {
    private Integer id;
    private String ciudad;
    private String temperatura;
    private String fecha;
    private String descripcion;
    private String imagen;

    public ClimaFavorito(Integer id, String ciudad, String temperatura, String fecha, String descripcion, String imagen) {
        this.id = id;
        this.ciudad = ciudad;
        this.temperatura = temperatura;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.imagen = imagen;
    }

    public ClimaFavorito() {
        this.id = 0;
        this.ciudad = "";
        this.temperatura = "";
        this.fecha = "";
        this.descripcion = "";
        this.imagen = "";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
