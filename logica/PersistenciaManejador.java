package logica;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class PersistenciaManejador {

    private static final String DIRECTORIO_DATOS = "datos";

    public boolean guardar(Serializable objeto, String nombreArchivo) {
        if (objeto == null || nombreArchivo == null || nombreArchivo.isBlank()) {
            System.out.println("Persistencia: no se pudo guardar (parámetros inválidos).");
            return false;
        }

        File directorio = new File(DIRECTORIO_DATOS);
        if (!directorio.exists() && !directorio.mkdirs()) {
            System.out.println("Persistencia: no se pudo crear el directorio de datos.");
            return false;
        }

        File archivo = new File(directorio, normalizarNombre(nombreArchivo));
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(archivo))) {
            salida.writeObject(objeto);
            System.out.println("Persistencia: guardado en " + archivo.getPath());
            return true;
        } catch (IOException ex) {
            System.out.println("Persistencia: error al guardar " + archivo.getPath() + ".");
            return false;
        }
    }

    public <T extends Serializable> T cargar(String nombreArchivo, Class<T> clase) {
        if (clase == null || nombreArchivo == null || nombreArchivo.isBlank()) {
            System.out.println("Persistencia: no se pudo cargar (parámetros inválidos).");
            return null;
        }

        File archivo = new File(DIRECTORIO_DATOS, normalizarNombre(nombreArchivo));
        if (!archivo.exists()) {
            System.out.println("Persistencia: no existe archivo " + archivo.getPath());
            return null;
        }

        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(archivo))) {
            Object objeto = entrada.readObject();
            if (clase.isInstance(objeto)) {
                System.out.println("Persistencia: cargado desde " + archivo.getPath());
                return clase.cast(objeto);
            }
            System.out.println("Persistencia: tipo incompatible al cargar " + archivo.getPath());
            return null;
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Persistencia: error al cargar " + archivo.getPath() + ".");
            return null;
        }
    }

    public boolean archivoExiste(String nombreArchivo) {
        if (nombreArchivo == null || nombreArchivo.isBlank()) {
            return false;
        }
        File archivo = new File(DIRECTORIO_DATOS, normalizarNombre(nombreArchivo));
        return archivo.exists();
    }

    public boolean eliminarArchivo(String nombreArchivo) {
        if (nombreArchivo == null || nombreArchivo.isBlank()) {
            System.out.println("Persistencia: no se pudo eliminar (nombre inválido).");
            return false;
        }
        File archivo = new File(DIRECTORIO_DATOS, normalizarNombre(nombreArchivo));
        if (!archivo.exists()) {
            System.out.println("Persistencia: no existe archivo " + archivo.getPath());
            return false;
        }
        boolean eliminado = archivo.delete();
        if (eliminado) {
            System.out.println("Persistencia: archivo eliminado " + archivo.getPath());
        } else {
            System.out.println("Persistencia: no se pudo eliminar " + archivo.getPath());
        }
        return eliminado;
    }

    public static String obtenerDirectorioDatos() {
        return DIRECTORIO_DATOS;
    }

    private String normalizarNombre(String nombreArchivo) {
        String limpio = nombreArchivo.trim();
        if (limpio.endsWith(".dat")) {
            return limpio;
        }
        return limpio + ".dat";
    }
}
