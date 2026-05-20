package logica;

public class DescuentoMontoFijo implements EstrategiaDescuento {

    private final double monto;

    public DescuentoMontoFijo(double monto) {
        if (monto < 0) {
            this.monto = 0.0;
        } else {
            this.monto = monto;
        }
    }

    @Override
    public double calcularTotal(Carrito carrito, double totalBase) {
        double total = totalBase - monto;
        if (total < 0) {
            return 0.0;
        }
        return total;
    }

    @Override
    public String getNombre() {
        return "Descuento por monto fijo";
    }
}