package com.mariomunozmyaplication.gestionatunegocio.empleado;

public class Empleado {

    private int nTelefono;
    private String imagenEmpleado;
    private String nombre, apellidos, IBAN, fContratacion, direccion, dni;
    private Float sueldo;
    //  private Date fContratacion;

    public Empleado(){

    }

    public Empleado(String nombre, String apellidos, String dni, String IBAN, String direccion, int nTelefono, String fContratacion, Float sueldo) {

        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.IBAN = IBAN;
        this.direccion = direccion;
        this.nTelefono = nTelefono;
        this.fContratacion = fContratacion;
        this.sueldo = sueldo;
    }

    public Empleado(String imageView, String nombre, String apellidos, String dni, String IBAN, String direccion, int nTelefono, String fContratacion, Float sueldo) {
        this.imagenEmpleado = imageView;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.IBAN = IBAN;
        this.direccion = direccion;
        this.nTelefono = nTelefono;
        this.fContratacion = fContratacion;
        this.sueldo = sueldo;
    }





    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getfContratacion() {
        return fContratacion;
    }

    public void setfContratacion(String fContratacion) {
        this.fContratacion = fContratacion;
    }

    public int getnTelefono() {
        return nTelefono;
    }

    public void setnTelefono(int nTelefono) {
        this.nTelefono = nTelefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }


    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getImagenEmpleado() {
        return imagenEmpleado;
    }

    public void setImagenEmpleado(String imagenEmpleado) {
        this.imagenEmpleado = imagenEmpleado;
    }

    public Float getSueldo() {
        return sueldo;
    }

    public void setSueldo(Float sueldo) {
        this.sueldo = sueldo;
    }

    @Override
    public String toString() {
        return "Empleado{" +
                "nTelefono=" + nTelefono +
                ", imagenEmpleado='" + imagenEmpleado + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", IBAN='" + IBAN + '\'' +
                ", fContratacion='" + fContratacion + '\'' +
                ", direccion='" + direccion + '\'' +
                ", dni='" + dni + '\'' +
                ", sueldo=" + sueldo +
                '}';
    }
}
