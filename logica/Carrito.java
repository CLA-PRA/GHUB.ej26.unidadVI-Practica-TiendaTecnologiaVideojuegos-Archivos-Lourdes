package logica;

import java.io.Serializable;
import java.util.Locale;

public class Carrito implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int capacidadMaxima;
    private final LineaCarrito[] lineas;
    private int tamanio;
    private final Inventario inventario;

    public Carrito(int capacidad, Inventario inventario) {
        if (capacidad > 0) {
            this.capacidadMaxima = capacidad;
        } else {
            this.capacidadMaxima = 1;
        }
        this.lineas = new LineaCarrito[this.capacidadMaxima];
        this.tamanio = 0;
        this.inventario = inventario;
    }

    public boolean agregar(String id, int cantidad) {
        try {
            agregarOrThrow(id, cantidad);
            return true;
        } catch (OperacionCarritoException ex) {
            return false;
        }
    }

    public void agregarOrThrow(String id, int cantidad) throws OperacionCarritoException {
        if (inventario == null || cantidad <= 0) {
            throw new OperacionCarritoException("Datos inválidos para agregar al carrito.");
        }
        try {
            Producto producto = inventario.obtenerOrThrow(id);
            int idx = indexOf(id);
            if (idx < 0 && tamanio >= capacidadMaxima) {
                throw new OperacionCarritoException("El carrito alcanzó su capacidad máxima.");
            }
            inventario.retirarOrThrow(id, cantidad);
            if (idx >= 0) {
                lineas[idx].incrementar(cantidad);
            } else {
                lineas[tamanio] = new LineaCarrito(producto, cantidad);
                tamanio++;
            }
        } catch (TiendaException ex) {
            throw new OperacionCarritoException(ex.getMessage());
        }
    }

    public boolean agregar(Producto producto, int cantidad) {
        if (producto == null) {
            return false;
        }
        return agregar(producto.getId(), cantidad);
    }

    public boolean agregar(Videojuego juego, int cantidad) {
        return agregar((Producto) juego, cantidad);
    }

    public boolean eliminar(String id, int cantidad) {
        try {
            eliminarOrThrow(id, cantidad);
            return true;
        } catch (OperacionCarritoException ex) {
            return false;
        }
    }

    public void eliminarOrThrow(String id, int cantidad) throws OperacionCarritoException {
        if (inventario == null || cantidad <= 0) {
            throw new OperacionCarritoException("Datos inválidos para eliminar del carrito.");
        }
        int idx = indexOf(id);
        if (idx < 0) {
            throw new OperacionCarritoException("El producto no está en el carrito.");
        }
        LineaCarrito linea = lineas[idx];
        if (linea.getCantidad() < cantidad) {
            throw new OperacionCarritoException("No puedes eliminar más unidades de las existentes.");
        }
        try {
            inventario.reabastecerOrThrow(id, cantidad);
        } catch (OperacionStockException ex) {
            throw new OperacionCarritoException(ex.getMessage());
        }
        linea.reducir(cantidad);
        if (linea.getCantidad() == 0) {
            for (int i = idx; i < tamanio - 1; i++) {
                lineas[i] = lineas[i + 1];
            }
            lineas[tamanio - 1] = null;
            tamanio--;
        }
    }

    public int getCantidad(String id) {
        int idx = indexOf(id);
        if (idx < 0) {
            return 0;
        }
        return lineas[idx].getCantidad();
    }

    public double total() {
        double suma = 0.0;
        for (int i = 0; i < tamanio; i++) {
            suma += lineas[i].getProducto().getPrecio() * lineas[i].getCantidad();
        }
        return suma;
    }

    public double totalConDescuento(double porcentaje) {
        return totalConEstrategia(new DescuentoPorcentaje(porcentaje));
    }

    public double totalConDescuento(String cupon) {
        return totalConEstrategia(new DescuentoPorcentaje(porcentajeSegunCupon(cupon)));
    }

    public double totalConEstrategia(EstrategiaDescuento estrategia) {
        double totalBase = total();
        if (estrategia == null) {
            return totalBase;
        }
        double totalConEstrategia = estrategia.calcularTotal(this, totalBase);
        if (totalConEstrategia < 0) {
            return 0.0;
        }
        return totalConEstrategia;
    }

    public void mostrarCarrito() {
        mostrarCarrito(null);
    }

    public void mostrarCarrito(String cupon) {
        double porcentajeDescuento = porcentajeSegunCupon(cupon);
        double totalSinDescuento = total();

        System.out.printf(Locale.US, "%-10s %-30s %5s %12s %12s%n",
                "id", "descripcion", "cant", "p.unitario", "Subtotal");
        System.out.println("--------------------------------------------------------------------------");

        for (int i = 0; i < tamanio; i++) {
            LineaCarrito linea = lineas[i];
            Producto producto = linea.getProducto();
            double subtotalLinea = producto.getPrecio() * linea.getCantidad();
            System.out.printf(Locale.US, "%-10s %-30s %5d %12.2f %12.2f%n",
                    producto.getId(), producto.getNombre(), linea.getCantidad(),
                    producto.getPrecio(), subtotalLinea);
        }

        System.out.println("--------------------------------------------------------------------------");
        System.out.printf(Locale.US, "%-10s %-30s %5s %12s %12.2f%n",
                "", "TOTAL", "", "", totalSinDescuento);

        if (porcentajeDescuento > 0.0) {
            double totalConDescuento = totalSinDescuento * (1.0 - porcentajeDescuento / 100.0);
            double montoDescuento = totalSinDescuento - totalConDescuento;
            String etiquetaDescuento;
            if (cupon == null || cupon.isBlank()) {
                etiquetaDescuento = String.format(Locale.US, "DESCUENTO %.0f%%", porcentajeDescuento);
            } else {
                etiquetaDescuento = String.format(Locale.US, "DESCUENTO %s (%.0f%%)",
                        cupon.trim().toUpperCase(Locale.ROOT), porcentajeDescuento);
            }

            System.out.printf(Locale.US, "%-10s %-30s %5s %12s %12.2f%n",
                    "", etiquetaDescuento, "", "", -montoDescuento);
            System.out.printf(Locale.US, "%-10s %-30s %5s %12s %12.2f%n",
                    "", "TOTAL CON DESCUENTO", "", "", totalConDescuento);
        }
    }

    public int getTamanio() {
        return tamanio;
    }

    public String idEnPosicion(int i) {
        if (i < 0 || i >= tamanio) {
            return null;
        }
        return lineas[i].getProducto().getId();
    }

    public int cantidadEnPosicion(int i) {
        if (i < 0 || i >= tamanio) {
            return -1;
        }
        return lineas[i].getCantidad();
    }

    public Producto productoEnPosicion(int i) {
        if (i < 0 || i >= tamanio) {
            return null;
        }
        return lineas[i].getProducto();
    }

    private int indexOf(String id) {
        if (id == null || id.isBlank()) {
            return -1;
        }
        String buscado = id.trim();
        for (int i = 0; i < tamanio; i++) {
            if (lineas[i].getProducto().getId().equalsIgnoreCase(buscado)) {
                return i;
            }
        }
        return -1;
    }

    private double porcentajeSegunCupon(String cupon) {
        if (cupon == null) {
            return 0.0;
        }
        switch (cupon.trim().toUpperCase(Locale.ROOT)) {
            case "REGALO10":
            case "GAMER10":
                return 10.0;
            case "TECH20":
            case "GAMER20":
                return 20.0;
            default:
                return 0.0;
        }
    }

    public boolean guardarEnArchivo(String nombreArchivo) {
        PersistenciaManejador manejador = new PersistenciaManejador();
        return manejador.guardar(this, nombreArchivo);
    }

    public static Carrito cargarDesdeArchivo(String nombreArchivo) {
        PersistenciaManejador manejador = new PersistenciaManejador();
        return manejador.cargar(nombreArchivo, Carrito.class);
    }

    public static boolean existeArchivoGuardado(String nombreArchivo) {
        PersistenciaManejador manejador = new PersistenciaManejador();
        return manejador.archivoExiste(nombreArchivo);
    }
}