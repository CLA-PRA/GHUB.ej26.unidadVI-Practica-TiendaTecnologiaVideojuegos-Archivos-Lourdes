package logica;

public class Telefono extends DispositivoElectronico {

    private int almacenamientoGb;
    private boolean cincoG;

    public Telefono() {
        this("NA", "Telefono", 0.0, "Generica", 128, true);
    }

    public Telefono(String id, String nombre, double precio, String marca,
            int almacenamientoGb, boolean cincoG) {
        super(id, nombre, precio, marca, 18);
        this.almacenamientoGb = valorPositivo(almacenamientoGb, 128);
        this.cincoG = cincoG;
    }

    public Telefono(Telefono otro) {
        super(otro);
        if (otro == null) {
            this.almacenamientoGb = 128;
            this.cincoG = true;
            return;
        }
        this.almacenamientoGb = otro.almacenamientoGb;
        this.cincoG = otro.cincoG;
    }

    private static int valorPositivo(int valor, int porDefecto) {
        if (valor <= 0) {
            return porDefecto;
        }
        return valor;
    }

    public int getAlmacenamientoGb() {
        return almacenamientoGb;
    }

    public boolean isCincoG() {
        return cincoG;
    }

    @Override
    public String getCategoria() {
        return "Telefono";
    }

    @Override
    public String getDetalleTecnico() {
        String conectividad;
        if (cincoG) {
            conectividad = "compatible con 5G";
        } else {
            conectividad = "solo 4G";
        }
        return almacenamientoGb + "GB, " + conectividad + ", " + detalleGarantia();
    }
}