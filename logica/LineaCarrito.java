package logica;

import java.io.Serializable;

class LineaCarrito implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Producto producto;
    private int cantidad;

    LineaCarrito(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    Producto getProducto() {
        return producto;
    }

    int getCantidad() {
        return cantidad;
    }

    void incrementar(int cantidad) {
        this.cantidad += cantidad;
    }

    void reducir(int cantidad) {
        this.cantidad -= cantidad;
    }
}