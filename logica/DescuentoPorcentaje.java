package logica;

public class DescuentoPorcentaje implements EstrategiaDescuento {

    private final double porcentaje;

    public DescuentoPorcentaje(double porcentaje) {
        if (porcentaje < 0 || porcentaje > 100) {
            this.porcentaje = 0.0;
        } else {
            this.porcentaje = porcentaje;
        }
    }

    @Override
    public double calcularTotal(Carrito carrito, double totalBase) {
        return totalBase * (1.0 - porcentaje / 100.0);
    }

    @Override
    public String getNombre() {
        return "Descuento por porcentaje";
    }
}