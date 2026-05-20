package logica;

import java.io.Serializable;
import java.util.Locale;

public class Inventario implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int capacidadMaxima;
    private final Producto[] catalogo;
    private final int[] stock;
    private int tamanio;

    public Inventario(int capacidad) {
        if (capacidad > 0) {
            this.capacidadMaxima = capacidad;
        } else {
            this.capacidadMaxima = 1;
        }
        this.catalogo = new Producto[this.capacidadMaxima];
        this.stock = new int[this.capacidadMaxima];
        this.tamanio = 0;
    }

    public void registrar(Producto producto, int existenciasIniciales) {
        try {
            registrarOrThrow(producto, existenciasIniciales);
        } catch (RegistroInvalidoException ex) {
            System.out.println("No se pudo registrar el producto: " + ex.getMessage());
        }
    }

    public void registrar(Videojuego juego, int existenciasIniciales) {
        registrar((Producto) juego, existenciasIniciales);
    }

    public void registrar(String id, String titulo, double precio, int existenciasIniciales) {
        registrar(new Videojuego(id, titulo, precio), existenciasIniciales);
    }

    public int existencias(String id) {
        int idx = indexOf(id);
        if (idx < 0) {
            return -1;
        }
        return stock[idx];
    }

    public boolean hayStock(String id, int cantidad) {
        if (cantidad <= 0) {
            return false;
        }
        int idx = indexOf(id);
        if (idx < 0) {
            return false;
        }
        return stock[idx] >= cantidad;
    }

    public boolean retirar(String id, int cantidad) {
        try {
            retirarOrThrow(id, cantidad);
            return true;
        } catch (OperacionStockException ex) {
            return false;
        }
    }

    public boolean reabastecer(String id, int cantidad) {
        try {
            reabastecerOrThrow(id, cantidad);
            return true;
        } catch (OperacionStockException ex) {
            return false;
        }
    }

    public Producto obtener(String id) {
        try {
            return obtenerOrThrow(id);
        } catch (ProductoNoEncontradoException ex) {
            return null;
        }
    }

    public void registrarOrThrow(Producto producto, int existenciasIniciales)
            throws RegistroInvalidoException {
        validarRegistroOrThrow(producto, existenciasIniciales);
        catalogo[tamanio] = producto;
        stock[tamanio] = existenciasIniciales;
        tamanio++;
    }

    public Producto obtenerOrThrow(String id) throws ProductoNoEncontradoException {
        int idx = indexOf(id);
        if (idx < 0) {
            throw new ProductoNoEncontradoException("No existe producto con id " + id + ".");
        }
        return catalogo[idx];
    }

    public void retirarOrThrow(String id, int cantidad) throws OperacionStockException {
        if (cantidad <= 0) {
            throw new OperacionStockException("La cantidad a retirar debe ser mayor que 0.");
        }
        int idx = indexOf(id);
        if (idx < 0) {
            throw new OperacionStockException("No existe producto con id " + id + ".");
        }
        int disponible = stock[idx];
        if (disponible < cantidad) {
            throw new OperacionStockException("Stock insuficiente. Disponible: " + disponible + ".");
        }
        stock[idx] = disponible - cantidad;
    }

    public void reabastecerOrThrow(String id, int cantidad) throws OperacionStockException {
        if (cantidad <= 0) {
            throw new OperacionStockException("La cantidad a reabastecer debe ser mayor que 0.");
        }
        int idx = indexOf(id);
        if (idx < 0) {
            throw new OperacionStockException("No existe producto con id " + id + ".");
        }
        stock[idx] = stock[idx] + cantidad;
    }

    public int getTamanio() {
        return tamanio;
    }

    public Producto enPosicion(int i) {
        if (i < 0 || i >= tamanio) {
            return null;
        }
        return catalogo[i];
    }

    public int stockEnPosicion(int i) {
        if (i < 0 || i >= tamanio) {
            return -1;
        }
        return stock[i];
    }

    public Producto[] catalogoComoArreglo() {
        Producto[] resultado = new Producto[tamanio];
        for (int i = 0; i < tamanio; i++) {
            resultado[i] = catalogo[i];
        }
        return resultado;
    }

    public Producto buscarPorId(String id) {
        return obtener(id);
    }

    public Producto[] buscarPorNombre(String texto) {
        if (texto == null || texto.isBlank()) {
            return new Producto[0];
        }
        final String buscado = texto.trim().toLowerCase(Locale.ROOT);
        Producto[] base = catalogoComoArreglo();
        return UtilArreglos.filtrar(base,
                producto -> producto.getNombre().toLowerCase(Locale.ROOT).contains(buscado));
    }

    public Producto[] filtrarPorCategoria(String categoria) {
        if (categoria == null || categoria.isBlank()) {
            return new Producto[0];
        }
        final String categoriaBuscada = categoria.trim().toLowerCase(Locale.ROOT);
        Producto[] base = catalogoComoArreglo();
        return UtilArreglos.filtrar(base,
                producto -> producto.getCategoria().toLowerCase(Locale.ROOT).equals(categoriaBuscada));
    }

    public Producto[] filtrarPorPrecio(double min, double max) {
        if (min > max || min < 0 || max < 0) {
            return new Producto[0];
        }
        Producto[] base = catalogoComoArreglo();
        return UtilArreglos.filtrar(base,
                producto -> producto.getPrecio() >= min && producto.getPrecio() <= max);
    }

    public Producto[] catalogoOrdenadoPorNombre() {
        Producto[] copia = UtilArreglos.copiar(catalogoComoArreglo());
        UtilArreglos.ordenarSeleccion(copia,
                (a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()));
        return copia;
    }

    public Producto[] catalogoOrdenadoPorPrecio(boolean ascendente) {
        Producto[] copia = UtilArreglos.copiar(catalogoComoArreglo());
        if (ascendente) {
            UtilArreglos.ordenarSeleccion(copia,
                    (a, b) -> Double.compare(a.getPrecio(), b.getPrecio()));
        } else {
            UtilArreglos.ordenarSeleccion(copia,
                    (a, b) -> Double.compare(b.getPrecio(), a.getPrecio()));
        }
        return copia;
    }

    private int indexOf(String id) {
        if (id == null || id.isBlank()) {
            return -1;
        }
        String buscado = id.trim();
        for (int i = 0; i < tamanio; i++) {
            if (catalogo[i].getId().equalsIgnoreCase(buscado)) {
                return i;
            }
        }
        return -1;
    }

    private boolean validarRegistro(Producto producto, int existenciasIniciales) {
        if (producto == null || !producto.tieneDatosBasicosValidos()) {
            return false;
        }
        if (existenciasIniciales < 0) {
            return false;
        }
        if (catalogo.length <= tamanio) {
            return false;
        }
        return indexOf(producto.getId()) < 0;
    }

    private void validarRegistroOrThrow(Producto producto, int existenciasIniciales)
            throws RegistroInvalidoException {
        if (producto == null) {
            throw new RegistroInvalidoException("El producto no puede ser null.");
        }
        if (!producto.tieneDatosBasicosValidos()) {
            throw new RegistroInvalidoException("Datos básicos del producto inválidos.");
        }
        if (existenciasIniciales < 0) {
            throw new RegistroInvalidoException("Las existencias iniciales no pueden ser negativas.");
        }
        if (tamanio >= capacidadMaxima) {
            throw new RegistroInvalidoException("Inventario lleno. Capacidad máxima alcanzada.");
        }
        if (indexOf(producto.getId()) >= 0) {
            throw new RegistroInvalidoException("Ya existe un producto con id " + producto.getId() + ".");
        }
    }

    public boolean guardarEnArchivo(String nombreArchivo) {
        PersistenciaManejador manejador = new PersistenciaManejador();
        return manejador.guardar(this, nombreArchivo);
    }

    public static Inventario cargarDesdeArchivo(String nombreArchivo) {
        PersistenciaManejador manejador = new PersistenciaManejador();
        return manejador.cargar(nombreArchivo, Inventario.class);
    }

    public static boolean existeArchivoGuardado(String nombreArchivo) {
        PersistenciaManejador manejador = new PersistenciaManejador();
        return manejador.archivoExiste(nombreArchivo);
    }
}