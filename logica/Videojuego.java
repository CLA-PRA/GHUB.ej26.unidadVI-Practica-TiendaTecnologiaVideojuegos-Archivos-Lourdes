package logica;

import java.util.Locale;

public class Videojuego extends Producto {

    private String plataforma;
    private String genero;

    public Videojuego() {
        this("NA", "Sin titulo", 0.0, "Generica", "Multiplataforma", "Aventura");
    }

    public Videojuego(String id, String titulo, double precio) {
        this(id, titulo, precio, "Generica", "Multiplataforma", "Aventura");
    }

    public Videojuego(String id, String titulo, double precio, String marca,
            String plataforma, String genero) {
        super(id, titulo, precio, marca);
        this.plataforma = textoOValor(plataforma, "Multiplataforma");
        this.genero = textoOValor(genero, "Aventura");
    }

    public Videojuego(Videojuego otro) {
        super(otro);
        if (otro == null) {
            this.plataforma = "Multiplataforma";
            this.genero = "Aventura";
            return;
        }
        this.plataforma = otro.plataforma;
        this.genero = otro.genero;
    }

    private static String textoOValor(String valor, String porDefecto) {
        if (valor == null || valor.trim().isBlank()) {
            return porDefecto;
        }
        return valor.trim();
    }

    public String getTitulo() {
        return getNombre();
    }

    public void setTitulo(String titulo) {
        setNombre(titulo);
    }

    public String getPlataforma() {
        return plataforma;
    }

    public String getGenero() {
        return genero;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = textoOValor(plataforma, this.plataforma);
    }

    public void setGenero(String genero) {
        this.genero = textoOValor(genero, this.genero);
    }

    @Override
    public String getCategoria() {
        return "Videojuego";
    }

    @Override
    public String getDetalleTecnico() {
        return "Plataforma: " + plataforma + ", genero: " + genero;
    }

    @Override
    public String toString() {
        return getId() + " - " + getTitulo() + " ($"
                + String.format(Locale.US, "%.2f", getPrecio()) + ")";
    }
}