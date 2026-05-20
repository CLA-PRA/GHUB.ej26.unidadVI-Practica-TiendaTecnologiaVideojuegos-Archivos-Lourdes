package logica;

public interface Criterio<T> {

    boolean cumple(T elemento);
}