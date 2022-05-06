package com.mariomunozmyaplication.gestionatunegocio.pedido;

public class Pedido {

    // Declaramos variables
    private String imagen;
    private String referencia, nombre;
    private int cantidad;

    //Constructor vacio
    public Pedido() {
    }

    // Constructor con argumentos
    public Pedido(String imagen, String referencia, String nombre, int cantidad) {
        this.imagen = imagen;
        this.referencia = referencia;
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    // Constructor con argumentos
    public Pedido(String referencia, String nombre, int cantidad) {
        this.referencia = referencia;
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    // Metodos GETTER y SETTER
    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    // Metodo para concatenar todos los datos
    @Override
    public String toString() {
        return "Pedido{" +
                "imagen=" + imagen +
                ", referencia='" + referencia + '\'' +
                ", nombre='" + nombre + '\'' +
                ", cantidad=" + cantidad +
                '}';
    }
}
