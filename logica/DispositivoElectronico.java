package logica;

public abstract class DispositivoElectronico extends Producto {

    private int garantiaMeses;

    protected DispositivoElectronico() {
        this("NA", "Dispositivo", 0.0, "Generica", 12);
    }

    protected DispositivoElectronico(String id, String nombre, double precio, String marca,
            int garantiaMeses) {
        super(id, nombre, precio, marca);
        this.garantiaMeses = garantiaValida(garantiaMeses);
    }

    protected DispositivoElectronico(DispositivoElectronico otro) {
        super(otro);
        this.garantiaMeses = otro == null ? 12 : otro.garantiaMeses;
    }

    private static int garantiaValida(int meses) {
        if (meses <= 0) {
            return 12;
        }
        return meses;
    }

    public int getGarantiaMeses() {
        return garantiaMeses;
    }

    protected String detalleGarantia() {
        return garantiaMeses + " meses de garantia";
    }
}