package interfaz;

import logica.Carrito;
import logica.Computadora;
import logica.ConsolaVideojuego;
import logica.Inventario;
import logica.OperacionCarritoException;
import logica.OperacionStockException;
import logica.Producto;
import logica.RegistroInvalidoException;
import logica.Tableta;
import logica.Telefono;
import logica.Videojuego;

import java.util.Locale;

public class MenuTienda {

    private final Lector in = new Lector();
    private final Inventario inventario;
    private final Carrito carrito;

    public MenuTienda(Inventario inventario, Carrito carrito) {
        this.inventario = inventario;
        this.carrito = carrito;
    }

    public void correr() {
        int opcion;
        do {
            mostrarMenu();
            opcion = in.leerEntero("Elige una opción: ");
            
                switch (opcion) {
                    case 1 : registrarProducto();
                        break;
                    case 2 : listarCatalogo();
                        break;
                    case 3 : reabastecer();
                        break;
                    case 4 : retirar();
                        break;
                    case 5 : agregarAlCarritoPorId();
                        break;
                    case 6 : agregarAlCarritoPorObjeto();
                        break;
                    case 7 : eliminarDelCarrito();
                        break;
                    case 8 : verCarrito();
                        break;
                    case 9 : totalSinDescuento();
                         break;
                    case 10 : totalConPorcentaje();
                        break;
                    case 11 : totalConCupon();
                         break;
                    case 12 : buscarProducto();
                        break;
                    case 13 : filtrarCatalogo();
                        break;
                    case 14 : ordenarCatalogo();
                        break;
                    case 0 : System.out.println("Saliendo... ¡Gracias!");
                        break;
                    default : System.out.println("Opción no válida.");
                }
            
            System.out.println();
        } while (opcion != 0);
    }

    private void mostrarMenu() {
        System.out.println("===== TIENDA DE REGALOS ELECTRONICOS =====");
        System.out.println("1) Registrar producto electronico");
        System.out.println("2) Listar catálogo");
        System.out.println("3) Reabastecer stock");
        System.out.println("4) Retirar stock");
        System.out.println("5) Agregar al carrito (por ID)");
        System.out.println("6) Agregar al carrito (por objeto)");
        System.out.println("7) Eliminar del carrito");
        System.out.println("8) Ver carrito");
        System.out.println("9) Total sin descuento");
        System.out.println("10) Total con % de descuento");
        System.out.println("11) Total con cupón (REGALO10/TECH20)");
        System.out.println("12) Buscar producto");
        System.out.println("13) Filtrar catálogo");
        System.out.println("14) Ordenar catálogo");
        System.out.println("0) Salir");
    }

    private void registrarProducto() {
        System.out.println("--- Registrar producto electrónico ---");
        Producto producto = crearProductoDesdeEntrada();
        if (producto == null) {
            System.out.println("No se creó el producto.");
            return;
        }
        int existencias = in.leerEntero("Existencias iniciales: ");
        try {
            inventario.registrarOrThrow(producto, existencias);
            System.out.println("✔ Registrado: " + producto.getCategoria());
        } catch (RegistroInvalidoException ex) {
            System.out.println("No se registró el producto: " + ex.getMessage());
        }
    }

    private Producto crearProductoDesdeEntrada() {
        int tipo = leerTipoProducto();
        String id = in.leerLinea("ID: ");
        String nombre = in.leerLinea("Nombre: ");
        String marca = in.leerLinea("Marca: ");
        double precio = in.leerDouble("Precio: ");

        switch (tipo) {
            case 1:
                int ram = in.leerEntero("RAM en GB: ");
                int almacenamientoPc = in.leerEntero("Almacenamiento en GB: ");
                boolean portatilPc = leerSiNo("¿Es portátil? (s/n): ");
                return new Computadora(id, nombre, precio, marca, ram, almacenamientoPc, portatilPc);
            case 2:
                double pulgadas = in.leerDouble("Tamaño de pantalla en pulgadas: ");
                boolean lapiz = leerSiNo("¿Incluye lápiz? (s/n): ");
                return new Tableta(id, nombre, precio, marca, pulgadas, lapiz);
            case 3:
                String modelo = in.leerLinea("Modelo o edición: ");
                boolean portatilConsola = leerSiNo("¿Es portátil? (s/n): ");
                return new ConsolaVideojuego(id, nombre, precio, marca, modelo, portatilConsola);
            case 4:
                String plataforma = in.leerLinea("Plataforma: ");
                String genero = in.leerLinea("Género: ");
                return new Videojuego(id, nombre, precio, marca, plataforma, genero);
            case 5:
                int almacenamientoTelefono = in.leerEntero("Almacenamiento en GB: ");
                boolean cincoG = leerSiNo("¿Tiene conectividad 5G? (s/n): ");
                return new Telefono(id, nombre, precio, marca, almacenamientoTelefono, cincoG);
            default:
                System.out.println("Tipo de producto no válido.");
                return null;
        }
    }

    private int leerTipoProducto() {
        System.out.println("Tipos disponibles:");
        System.out.println("1) Computadora");
        System.out.println("2) Tableta");
        System.out.println("3) Consola de videojuegos");
        System.out.println("4) Videojuego");
        System.out.println("5) Teléfono");
        return in.leerEntero("Tipo: ");
    }

    private boolean leerSiNo(String prompt) {
        String respuesta = in.leerLinea(prompt).toLowerCase(Locale.ROOT);
        return respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("sí")
                || respuesta.equals("true") || respuesta.equals("1");
    }

    private void listarCatalogo() {
        System.out.println("--- Catálogo ---");
        
        if (inventario.getTamanio() == 0) {
            System.out.println("(Vacío)");
            return;
        }
        for (int i = 0; i < inventario.getTamanio(); i++) {
            Producto v = inventario.enPosicion(i);
            int st = inventario.stockEnPosicion(i);
            System.out.println("• " + v + " | stock: " + st);
        }
    }

private void reabastecer() {
        System.out.println("--- Reabastecer stock ---");
        String id = in.leerLinea("ID: ");
        int cantidad = in.leerEntero("Cantidad a reabastecer: ");
        try {
            inventario.reabastecerOrThrow(id, cantidad);
            System.out.println("✔ Stock actual: " + inventario.existencias(id));
        } catch (OperacionStockException ex) {
            System.out.println("No fue posible reabastecer ese producto: " + ex.getMessage());
        }
    }

    private void retirar() {
        System.out.println("--- Retirar stock ---");
        String id = in.leerLinea("ID: ");
        int cantidad = in.leerEntero("Cantidad a retirar: ");
        try {
            inventario.retirarOrThrow(id, cantidad);
            System.out.println("✔ Stock actual: " + inventario.existencias(id));
        } catch (OperacionStockException ex) {
            System.out.println("No fue posible retirar ese stock: " + ex.getMessage());
        }
    }

    private void agregarAlCarritoPorId() {
        System.out.println("--- Agregar al carrito (por ID) ---");
        String id = in.leerLinea("ID: ");
        int cantidad = in.leerEntero("Cantidad: ");
        try {
            carrito.agregarOrThrow(id, cantidad);
            System.out.println("✔ En carrito (" + id + "): " + carrito.getCantidad(id));
            System.out.println("Stock restante: " + inventario.existencias(id));
        } catch (OperacionCarritoException ex) {
            System.out.println("No se pudo agregar al carrito: " + ex.getMessage());
        }
    }

    private void agregarAlCarritoPorObjeto() {
        System.out.println("--- Agregar al carrito (por objeto) ---");
        String id = in.leerLinea("ID: ");
        int cantidad = in.leerEntero("Cantidad: ");
        Producto producto = inventario.obtener(id);
        if (producto == null) {
            System.out.println("No existe un producto con ese ID.");
            return;
        }
        try {
            carrito.agregarOrThrow(producto.getId(), cantidad);
            System.out.println("✔ En carrito (" + id + "): " + carrito.getCantidad(id));
            System.out.println("Stock restante: " + inventario.existencias(id));
        } catch (OperacionCarritoException ex) {
            System.out.println("No se pudo agregar al carrito: " + ex.getMessage());
        }
    }

    private void eliminarDelCarrito() {
        System.out.println("--- Eliminar del carrito ---");
        String id = in.leerLinea("ID: ");
        int cantidad = in.leerEntero("Cantidad: ");
        try {
            carrito.eliminarOrThrow(id, cantidad);
            System.out.println("✔ En carrito (" + id + "): " + carrito.getCantidad(id));
            System.out.println("Stock actual: " + inventario.existencias(id));
        } catch (OperacionCarritoException ex) {
            System.out.println("No se pudo eliminar esa cantidad del carrito: " + ex.getMessage());
        }
    }

    private void verCarrito() {
        System.out.println("--- Carrito ---");
        if (carrito.getTamanio() == 0) {
            System.out.println("(Vacío)");
            return;
        }
        for (int i = 0; i < carrito.getTamanio(); i++) {
            Producto producto = carrito.productoEnPosicion(i);
            int cant = carrito.cantidadEnPosicion(i);
            double precio = producto.getPrecio();
            System.out.println("• " + producto.getCategoria() + " - " + producto.getNombre() + " x " + cant
                    + " @ $" + String.format(Locale.US, "%.2f", precio));
        }
        System.out.println("Total: $" + String.format(Locale.US, "%.2f", carrito.total()));
    }

    private void totalSinDescuento() {
        System.out.println("--- Total sin descuento ---");
        System.out.println("Total: $" + String.format(Locale.US, "%.2f", carrito.total()));
    }

    private void totalConPorcentaje() {
        System.out.println("--- Total con % de descuento ---");
        double pct = in.leerDouble("Porcentaje [0-100]: ");
        System.out.println("Total: $" + String.format(Locale.US, "%.2f", carrito.totalConDescuento(pct)));
    }

    private void totalConCupon() {
        System.out.println("--- Total con cupón ---");
        String cupon = in.leerLinea("Cupón (REGALO10 / TECH20 / otro): ");
        System.out.println("Total: $" + String.format(Locale.US, "%.2f", carrito.totalConDescuento(cupon)));
    }

    private void buscarProducto() {
        System.out.println("--- Buscar producto ---");
        System.out.println("1) Buscar por ID");
        System.out.println("2) Buscar por nombre");
        int modo = in.leerEntero("Modo: ");
        if (modo == 1) {
            String id = in.leerLinea("ID: ");
            Producto producto = inventario.buscarPorId(id);
            if (producto == null) {
                System.out.println("No se encontró producto con ese ID.");
                return;
            }
            System.out.println("• " + producto + " | stock: " + inventario.existencias(producto.getId()));
            return;
        }
        if (modo == 2) {
            String texto = in.leerLinea("Texto a buscar en nombre: ");
            Producto[] encontrados = inventario.buscarPorNombre(texto);
            imprimirProductos(encontrados);
            return;
        }
        System.out.println("Modo no válido.");
    }

    private void filtrarCatalogo() {
        System.out.println("--- Filtrar catálogo ---");
        System.out.println("1) Por categoría");
        System.out.println("2) Por rango de precio");
        int modo = in.leerEntero("Modo: ");
        if (modo == 1) {
            String categoria = in.leerLinea("Categoría: ");
            Producto[] filtrados = inventario.filtrarPorCategoria(categoria);
            imprimirProductos(filtrados);
            return;
        }
        if (modo == 2) {
            double min = in.leerDouble("Precio mínimo: ");
            double max = in.leerDouble("Precio máximo: ");
            Producto[] filtrados = inventario.filtrarPorPrecio(min, max);
            imprimirProductos(filtrados);
            return;
        }
        System.out.println("Modo no válido.");
    }

    private void ordenarCatalogo() {
        System.out.println("--- Ordenar catálogo ---");
        System.out.println("1) Nombre (A-Z)");
        System.out.println("2) Precio (menor a mayor)");
        System.out.println("3) Precio (mayor a menor)");
        int modo = in.leerEntero("Modo: ");
        Producto[] ordenados;
        switch (modo) {
            case 1:
                ordenados = inventario.catalogoOrdenadoPorNombre();
                break;
            case 2:
                ordenados = inventario.catalogoOrdenadoPorPrecio(true);
                break;
            case 3:
                ordenados = inventario.catalogoOrdenadoPorPrecio(false);
                break;
            default:
                System.out.println("Modo no válido.");
                return;
        }
        imprimirProductos(ordenados);
    }

    private void imprimirProductos(Producto[] productos) {
        if (productos == null || productos.length == 0) {
            System.out.println("(Sin resultados)");
            return;
        }
        for (Producto producto : productos) {
            System.out.println("• " + producto + " | stock: " + inventario.existencias(producto.getId()));
        }
    }
}

