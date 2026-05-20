package logica;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class ListaGenérica<T> implements Iterable<T>, Serializable {

    private static final long serialVersionUID = 1L;
    private static final int CAPACIDAD_POR_DEFECTO = 10;

    private T[] elementos;
    private int tamanio;

    @SuppressWarnings("unchecked")
    public ListaGenérica() {
        this.elementos = (T[]) new Object[CAPACIDAD_POR_DEFECTO];
        this.tamanio = 0;
    }

    @SuppressWarnings("unchecked")
    public ListaGenérica(int capacidadInicial) {
        int capacidad = capacidadInicial <= 0 ? CAPACIDAD_POR_DEFECTO : capacidadInicial;
        this.elementos = (T[]) new Object[capacidad];
        this.tamanio = 0;
    }

    public void agregar(T elemento) {
        asegurarCapacidad();
        elementos[tamanio] = elemento;
        tamanio++;
    }

    public T obtener(int indice) {
        validarIndice(indice);
        return elementos[indice];
    }

    public T eliminar(int indice) {
        validarIndice(indice);
        T eliminado = elementos[indice];

        for (int i = indice; i < tamanio - 1; i++) {
            elementos[i] = elementos[i + 1];
        }

        elementos[tamanio - 1] = null;
        tamanio--;
        return eliminado;
    }

    public boolean contiene(T elemento) {
        for (int i = 0; i < tamanio; i++) {
            if (Objects.equals(elementos[i], elemento)) {
                return true;
            }
        }
        return false;
    }

    public int tamanio() {
        return tamanio;
    }

    public void limpiar() {
        for (int i = 0; i < tamanio; i++) {
            elementos[i] = null;
        }
        tamanio = 0;
    }

    private void asegurarCapacidad() {
        if (tamanio < elementos.length) {
            return;
        }

        @SuppressWarnings("unchecked")
        T[] nuevos = (T[]) new Object[elementos.length * 2];

        for (int i = 0; i < elementos.length; i++) {
            nuevos[i] = elementos[i];
        }

        elementos = nuevos;
    }

    private void validarIndice(int indice) {
        if (indice < 0 || indice >= tamanio) {
            throw new IndexOutOfBoundsException("Indice fuera de rango: " + indice);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new IteradorLista();
    }

    private class IteradorLista implements Iterator<T> {
        private int indiceActual = 0;

        @Override
        public boolean hasNext() {
            return indiceActual < tamanio;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No hay mas elementos");
            }
            T valor = elementos[indiceActual];
            indiceActual++;
            return valor;
        }
    }
}
