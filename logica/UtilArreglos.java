package logica;

public final class UtilArreglos {

    private UtilArreglos() {
    }

    public static <T> T[] filtrar(T[] origen, Criterio<T> criterio) {
        if (origen == null || criterio == null) {
            return origen;
        }
        int total = 0;
        for (T elemento : origen) {
            if (criterio.cumple(elemento)) {
                total++;
            }
        }

        @SuppressWarnings("unchecked")
        T[] resultado = (T[]) java.lang.reflect.Array.newInstance(
                origen.getClass().getComponentType(), total);

        int idx = 0;
        for (T elemento : origen) {
            if (criterio.cumple(elemento)) {
                resultado[idx++] = elemento;
            }
        }
        return resultado;
    }

    public static <T> T[] copiar(T[] origen) {
        if (origen == null) {
            return null;
        }
        @SuppressWarnings("unchecked")
        T[] copia = (T[]) java.lang.reflect.Array.newInstance(
                origen.getClass().getComponentType(), origen.length);
        for (int i = 0; i < origen.length; i++) {
            copia[i] = origen[i];
        }
        return copia;
    }

    public static <T> void ordenarSeleccion(T[] arreglo, Comparador<T> comparador) {
        if (arreglo == null || comparador == null) {
            return;
        }
        for (int i = 0; i < arreglo.length - 1; i++) {
            int mejor = i;
            for (int j = i + 1; j < arreglo.length; j++) {
                if (comparador.comparar(arreglo[j], arreglo[mejor]) < 0) {
                    mejor = j;
                }
            }
            T tmp = arreglo[i];
            arreglo[i] = arreglo[mejor];
            arreglo[mejor] = tmp;
        }
    }
}