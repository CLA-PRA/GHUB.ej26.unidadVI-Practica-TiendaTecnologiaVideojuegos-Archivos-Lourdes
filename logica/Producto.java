package logica;

import java.io.Serializable;
import java.util.Locale;

public abstract class Producto implements Descontable, Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String nombre;
    private double precio;
    private String marca;

    protected Producto() {
        this("NA", "Producto sin nombre", 0.0, "Generica");
    }

    protected Producto(String id, String nombre, double precio, String marca) {
        this.id = limpiar(id);
        this.nombre = limpiar(nombre);
        if (precio >= 0) {
            this.precio = precio;
        } else {
            this.precio = 0.0;
        }
        this.marca = limpiarMarca(marca);
    }

    protected Producto(Producto otro) {
        if (otro == null) {
            this.id = "NA";
            this.nombre = "Producto sin nombre";
            this.precio = 0.0;
            this.marca = "Generica";
            return;
        }
        this.id = otro.id;
        this.nombre = otro.nombre;
        this.precio = otro.precio;
        this.marca = otro.marca;
    }

    private static String limpiar(String valor) {
        if (valor == null) {
            return "";
        }
        return valor.trim();
    }

    private static String limpiarMarca(String marca) {
        String marcaLimpia = limpiar(marca);
        if (marcaLimpia.isBlank()) {
            return "Generica";
        }
        return marcaLimpia;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public String getMarca() {
        return marca;
    }

    public void setNombre(String nombre) {
        String nombreLimpio = limpiar(nombre);
        if (!nombreLimpio.isBlank()) {
            this.nombre = nombreLimpio;
        }
    }

    public void setPrecio(double precio) {
        if (precio >= 0) {
            this.precio = precio;
        }
    }

    public void setMarca(String marca) {
        this.marca = limpiarMarca(marca);
    }

    public boolean tieneDatosBasicosValidos() {
        return !id.isBlank() && !nombre.isBlank() && precio >= 0;
    }

    @Override
    public double precioConDescuento(double porcentaje) {
        double porcentajeValido = porcentaje;
        if (porcentajeValido < 0 || porcentajeValido > 100) {
            porcentajeValido = 0.0;
        }
        return precio * (1.0 - porcentajeValido / 100.0);
    }

    @Override
    public double precioConDescuento(String cupon) {
        double porcentaje = 0.0;
        if (cupon != null) {
            switch (cupon.trim().toUpperCase(Locale.ROOT)) {
                case "REGALO10":
                case "GAMER10":
                    porcentaje = 10.0;
                    break;
                case "TECH20":
                case "GAMER20":
                    porcentaje = 20.0;
                    break;
                default:
                    porcentaje = 0.0;
                    break;
            }
        }
        return precioConDescuento(porcentaje);
    }

    public abstract String getCategoria();

    public abstract String getDetalleTecnico();

    @Override
    public String toString() {
        return getCategoria() + ": " + id + " - " + nombre + " (" + marca + ", $"
                + String.format(Locale.US, "%.2f", precio) + ") | " + getDetalleTecnico();
    }
}