package com.mariomunozmyaplication.gestionatunegocio.producto;

public class Producto {

    private int stock;
    private String imagen;
    private Float precioCoste, precioVenta;
    private String nombre, descripcion, referencia;

    public Producto(){

    }

    public Producto(String imagen, String referencia, String nombre, String descripcion, int stock, Float precioCoste, Float precioVenta) {
        this.imagen = imagen;
        this.referencia = referencia;
        this.stock = stock;
        this.precioCoste = precioCoste;
        this.precioVenta = precioVenta;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Producto(String referencia, String nombre, String descripcion, int stock, Float precioCoste, Float precioVenta) {
        this.referencia = referencia;
        this.stock = stock;
        this.precioCoste = precioCoste;
        this.precioVenta = precioVenta;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Float getPrecioCoste() {
        return precioCoste;
    }

    public void setPrecioCoste(Float precioCoste) {
        this.precioCoste = precioCoste;
    }

    public Float getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Float precioVenta) {
        this.precioVenta = precioVenta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }



    @Override
    public String toString() {
        return "Producto{" +
                "imagen=" + imagen +
                ", stock=" + stock +
                ", precioCoste=" + precioCoste +
                ", precioVenta=" + precioVenta +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", referencia='" + referencia + '\'' +
                '}';
    }
}
