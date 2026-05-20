package miPrincipal;

import logica.Computadora;
import logica.Inventario;
import logica.Carrito;
import logica.ConsolaVideojuego;
import logica.Tableta;
import logica.Telefono;
import logica.Videojuego;
import interfaz.MenuTienda;

public class Principal {

    private static final String ARCHIVO_INVENTARIO = "inventario_principal";

    public static void main(String[] args) {
        Inventario inventario = cargarInventarioInicial();
        Carrito carrito = new Carrito(50, inventario);
        MenuTienda menu = new MenuTienda(inventario, carrito);
        menu.correr();
        inventario.guardarEnArchivo(ARCHIVO_INVENTARIO);
    }

    private static Inventario cargarInventarioInicial() {
        Inventario cargado = Inventario.cargarDesdeArchivo(ARCHIVO_INVENTARIO);
        if (cargado != null) {
            System.out.println("Se cargó inventario guardado.");
            return cargado;
        }

        Inventario inventarioNuevo = new Inventario(100);
        cargarCatalogoDemo(inventarioNuevo);
        System.out.println("No había inventario guardado. Se cargó catálogo demo.");
        return inventarioNuevo;
    }

    private static void cargarCatalogoDemo(Inventario inventario) {
        inventario.registrar(new Computadora("LAP-01", "Laptop IdeaPad", 15999.0,
                "Lenovo", 16, 512, true), 4);
        inventario.registrar(new Tableta("TAB-01", "Galaxy Tab", 8999.0,
                "Samsung", 11.0, true), 6);
        inventario.registrar(new ConsolaVideojuego("CON-01", "Nintendo Switch OLED", 7299.0,
                "Nintendo", "OLED", true), 5);
        inventario.registrar(new Videojuego("VID-01", "Zelda: Tears of the Kingdom", 1499.0,
                "Nintendo", "Switch", "Aventura"), 10);
        inventario.registrar(new Telefono("TEL-01", "Moto Edge", 10999.0,
                "Motorola", 256, true), 8);
    }
}