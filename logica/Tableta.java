package logica;

import java.util.Locale;

public class Tableta extends DispositivoElectronico {

    private double pulgadas;
    private boolean incluyeLapiz;

    public Tableta() {
        this("NA", "Tableta", 0.0, "Generica", 10.1, false);
    }

    public Tableta(String id, String nombre, double precio, String marca,
            double pulgadas, boolean incluyeLapiz) {
        super(id, nombre, precio, marca, 12);
        this.pulgadas = valorPositivo(pulgadas, 10.1);
        this.incluyeLapiz = incluyeLapiz;
    }

    public Tableta(Tableta otra) {
        super(otra);
        if (otra == null) {
            this.pulgadas = 10.1;
            this.incluyeLapiz = false;
            return;
        }
        this.pulgadas = otra.pulgadas;
        this.incluyeLapiz = otra.incluyeLapiz;
    }

    private static double valorPositivo(double valor, double porDefecto) {
        if (valor <= 0) {
            return porDefecto;
        }
        return valor;
    }

    public double getPulgadas() {
        return pulgadas;
    }

    public boolean isIncluyeLapiz() {
        return incluyeLapiz;
    }

    @Override
    public String getCategoria() {
        return "Tableta";
    }

    @Override
    public String getDetalleTecnico() {
        String detalleLapiz;
        if (incluyeLapiz) {
            detalleLapiz = "incluye lapiz";
        } else {
            detalleLapiz = "sin lapiz";
        }
        return String.format(Locale.US, "Pantalla %.1f\", %s, %s", pulgadas, detalleLapiz,
            detalleGarantia());
    }
}