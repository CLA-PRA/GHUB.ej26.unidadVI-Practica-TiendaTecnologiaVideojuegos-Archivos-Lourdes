package logica;

public interface RepositorioGenérico<T, ID> {

    boolean guardar(T entidad);

    T obtener(ID id);

    ListaGenérica<T> obtenerTodas();

    boolean actualizar(T entidad);

    boolean eliminar(ID id);

    int contar();

    boolean existe(ID id);
}
