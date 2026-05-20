package logica;

import java.io.Serializable;

public class RepositorioProductos implements RepositorioGenérico<Producto, String>, Serializable {

    private static final long serialVersionUID = 1L;

    private ListaGenérica<Producto> productos;

    public RepositorioProductos() {
        this.productos = new ListaGenérica<>();
    }

    @Override
    public boolean guardar(Producto entidad) {
        if (entidad == null || entidad.getId() == null || entidad.getId().isBlank()) {
            return false;
        }
        if (existe(entidad.getId())) {
            return false;
        }

        productos.agregar(entidad);
        return true;
    }

    @Override
    public Producto obtener(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }

        for (Producto producto : productos) {
            if (id.equals(producto.getId())) {
                return producto;
            }
        }

        return null;
    }

    @Override
    public ListaGenérica<Producto> obtenerTodas() {
        ListaGenérica<Producto> copia = new ListaGenérica<>(productos.tamanio());
        for (Producto producto : productos) {
            copia.agregar(producto);
        }
        return copia;
    }

    @Override
    public boolean actualizar(Producto entidad) {
        if (entidad == null || entidad.getId() == null || entidad.getId().isBlank()) {
            return false;
        }

        int indice = indicePorId(entidad.getId());
        if (indice < 0) {
            return false;
        }

        ListaGenérica<Producto> nuevaLista = new ListaGenérica<>(productos.tamanio());
        for (int i = 0; i < productos.tamanio(); i++) {
            if (i == indice) {
                nuevaLista.agregar(entidad);
            } else {
                nuevaLista.agregar(productos.obtener(i));
            }
        }

        productos = nuevaLista;
        return true;
    }

    @Override
    public boolean eliminar(String id) {
        int indice = indicePorId(id);
        if (indice < 0) {
            return false;
        }

        productos.eliminar(indice);
        return true;
    }

    @Override
    public int contar() {
        return productos.tamanio();
    }

    @Override
    public boolean existe(String id) {
        return indicePorId(id) >= 0;
    }

    private int indicePorId(String id) {
        if (id == null || id.isBlank()) {
            return -1;
        }

        for (int i = 0; i < productos.tamanio(); i++) {
            if (id.equals(productos.obtener(i).getId())) {
                return i;
            }
        }

        return -1;
    }
}
