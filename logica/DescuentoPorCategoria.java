package logica;

import java.util.Locale;

public class DescuentoPorCategoria implements EstrategiaDescuento {

    private final String categoriaObjetivo;
    private final double porcentaje;

    public DescuentoPorCategoria(String categoriaObjetivo, double porcentaje) {
        this.categoriaObjetivo = categoriaObjetivo == null ? "" : categoriaObjetivo.trim();
        if (porcentaje < 0 || porcentaje > 100) {
            this.porcentaje = 0.0;
        } else {
            this.porcentaje = porcentaje;
        }
    }

    @Override
    public double calcularTotal(Carrito carrito, double totalBase) {
        if (carrito == null || categoriaObjetivo.isBlank() || porcentaje == 0.0) {
            return totalBase;
        }

        double subtotalCategoria = 0.0;
        for (int i = 0; i < carrito.getTamanio(); i++) {
            Producto producto = carrito.productoEnPosicion(i);
            if (producto == null) {
                continue;
            }
            if (producto.getCategoria().toLowerCase(Locale.ROOT)
                    .equals(categoriaObjetivo.toLowerCase(Locale.ROOT))) {
                subtotalCategoria += producto.getPrecio() * carrito.cantidadEnPosicion(i);
            }
        }

        double descuento = subtotalCategoria * (porcentaje / 100.0);
        return totalBase - descuento;
    }

    @Override
    public String getNombre() {
        return "Descuento por categoria";
    }
}