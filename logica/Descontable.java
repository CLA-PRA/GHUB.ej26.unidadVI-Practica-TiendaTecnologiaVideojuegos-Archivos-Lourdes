package logica;

public interface Descontable {

    double precioConDescuento(double porcentaje);

    double precioConDescuento(String cupon);
}