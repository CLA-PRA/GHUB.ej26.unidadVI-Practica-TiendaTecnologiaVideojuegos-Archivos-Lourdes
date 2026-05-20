package logica;

public class Computadora extends DispositivoElectronico {

    private int ramGb;
    private int almacenamientoGb;
    private boolean portatil;

    public Computadora() {
        this("NA", "Computadora", 0.0, "Generica", 8, 256, true);
    }

    public Computadora(String id, String nombre, double precio, String marca,
            int ramGb, int almacenamientoGb, boolean portatil) {
        super(id, nombre, precio, marca, 24);
        this.ramGb = valorPositivo(ramGb, 8);
        this.almacenamientoGb = valorPositivo(almacenamientoGb, 256);
        this.portatil = portatil;
    }

    public Computadora(Computadora otra) {
        super(otra);
        if (otra == null) {
            this.ramGb = 8;
            this.almacenamientoGb = 256;
            this.portatil = true;
            return;
        }
        this.ramGb = otra.ramGb;
        this.almacenamientoGb = otra.almacenamientoGb;
        this.portatil = otra.portatil;
    }

    private static int valorPositivo(int valor, int porDefecto) {
        if (valor <= 0) {
            return porDefecto;
        }
        return valor;
    }

    public int getRamGb() {
        return ramGb;
    }

    public int getAlmacenamientoGb() {
        return almacenamientoGb;
    }

    public boolean isPortatil() {
        return portatil;
    }

    @Override
    public String getCategoria() {
        return "Computadora";
    }

    @Override
    public String getDetalleTecnico() {
        String tipoComputadora;
        if (portatil) {
            tipoComputadora = "portatil";
        } else {
            tipoComputadora = "escritorio";
        }
        return ramGb + "GB RAM, " + almacenamientoGb + "GB SSD, "
            + tipoComputadora + ", " + detalleGarantia();
    }
}