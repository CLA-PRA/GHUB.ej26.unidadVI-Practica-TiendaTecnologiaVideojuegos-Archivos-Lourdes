package logica;

public interface EstrategiaDescuento {

    double calcularTotal(Carrito carrito, double totalBase);

    String getNombre();
}