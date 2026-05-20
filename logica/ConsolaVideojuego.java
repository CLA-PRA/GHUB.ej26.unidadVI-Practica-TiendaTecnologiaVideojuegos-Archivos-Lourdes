package logica;

public class ConsolaVideojuego extends DispositivoElectronico {

    private String modelo;
    private boolean portatil;

    public ConsolaVideojuego() {
        this("NA", "Consola", 0.0, "Generica", "Edicion estandar", false);
    }

    public ConsolaVideojuego(String id, String nombre, double precio, String marca,
            String modelo, boolean portatil) {
        super(id, nombre, precio, marca, 12);
        this.modelo = textoOValor(modelo, "Edicion estandar");
        this.portatil = portatil;
    }

    public ConsolaVideojuego(ConsolaVideojuego otra) {
        super(otra);
        if (otra == null) {
            this.modelo = "Edicion estandar";
            this.portatil = false;
            return;
        }
        this.modelo = otra.modelo;
        this.portatil = otra.portatil;
    }

    private static String textoOValor(String valor, String porDefecto) {
        if (valor == null || valor.trim().isBlank()) {
            return porDefecto;
        }
        return valor.trim();
    }

    public String getModelo() {
        return modelo;
    }

    public boolean isPortatil() {
        return portatil;
    }

    @Override
    public String getCategoria() {
        return "Consola";
    }

    @Override
    public String getDetalleTecnico() {
        String tipoConsola;
        if (portatil) {
            tipoConsola = "portatil";
        } else {
            tipoConsola = "sobremesa";
        }
        return modelo + ", " + tipoConsola + ", " + detalleGarantia();
    }
}