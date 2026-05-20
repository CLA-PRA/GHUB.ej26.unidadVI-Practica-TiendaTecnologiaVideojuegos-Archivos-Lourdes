package miTest;

import logica.*;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Modifier;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    @Test
    void testProductoEsAbstractoYSubclasesAplicanPolimorfismo() {
        assertTrue(Modifier.isAbstract(Producto.class.getModifiers()));

        Producto[] productos = {
            new Computadora("P1", "Laptop", 10000.0, "Lenovo", 16, 512, true),
            new Tableta("P2", "Tablet", 7000.0, "Samsung", 11.0, false),
            new Videojuego("P3", "Halo", 1000.0, "Xbox", "Series", "FPS")
        };

        for (Producto producto : productos) {
            assertFalse(producto.getCategoria().isBlank());
            assertFalse(producto.getDetalleTecnico().isBlank());
        }
    }

    @Test
    void testInterfazDescontableEnProducto() {
        Descontable descuento = new Videojuego("DS1", "Descuento", 200.0);
        assertEquals(180.0, descuento.precioConDescuento("REGALO10"), 0.01);
    }

    @Test
    void testExcepcionesRegistroYCarrito() throws RegistroInvalidoException {
        Inventario inventarioRegistro = new Inventario(1);

        assertThrows(RegistroInvalidoException.class,
                () -> inventarioRegistro.registrarOrThrow(new Videojuego(" ", "Sin id", 100.0), 1));

        assertDoesNotThrow(() ->
                inventarioRegistro.registrarOrThrow(new Videojuego("EX1", "Juego excepción", 500.0), 1));

        assertThrows(RegistroInvalidoException.class,
                () -> inventarioRegistro.registrarOrThrow(new Videojuego("EX2", "Juego 2", 200.0), 1));

        Inventario inventarioCarrito = new Inventario(5);
        Carrito carrito = new Carrito(1, inventarioCarrito);

        assertThrows(OperacionCarritoException.class,
                () -> carrito.agregarOrThrow("NO_EXISTE", 1));

        inventarioCarrito.registrarOrThrow(new Videojuego("EX3", "Juego", 500.0), 1);

        assertThrows(OperacionCarritoException.class,
                () -> carrito.agregarOrThrow("EX3", 2));

        assertDoesNotThrow(() ->
                carrito.agregarOrThrow("EX3", 1));
    }

    @Test
    void testBusquedaYFiltrosConArreglos() {
        Inventario inventario = new Inventario(10);
        inventario.registrar(new Videojuego("B1", "Mario Kart", 1200.0,
                "Nintendo", "Switch", "Carreras"), 3);
        inventario.registrar(new Computadora("B2", "Laptop Gamer", 15000.0,
                "Lenovo", 16, 512, true), 2);
        inventario.registrar(new Telefono("B3", "Moto Edge", 9000.0,
                "Motorola", 256, true), 4);

        assertNotNull(inventario.buscarPorId("B1"));
        assertNull(inventario.buscarPorId("NO"));

        Producto[] porNombre = inventario.buscarPorNombre("mario");
        assertEquals(1, porNombre.length);
        assertEquals("B1", porNombre[0].getId());

        Producto[] porCategoria = inventario.filtrarPorCategoria("Computadora");
        assertEquals(1, porCategoria.length);
        assertEquals("B2", porCategoria[0].getId());

        Producto[] porPrecio = inventario.filtrarPorPrecio(8000, 16000);
        assertEquals(2, porPrecio.length);
    }

    @Test
    void testOrdenamientoCatalogoConArreglos() {
        Inventario inventario = new Inventario(10);
        inventario.registrar(new Videojuego("O1", "Zelda", 1400.0), 3);
        inventario.registrar(new Videojuego("O2", "Among Us", 300.0), 3);
        inventario.registrar(new Videojuego("O3", "Fifa", 900.0), 3);

        Producto[] porNombre = inventario.catalogoOrdenadoPorNombre();
        assertEquals("Among Us", porNombre[0].getNombre());
        assertEquals("Fifa", porNombre[1].getNombre());
        assertEquals("Zelda", porNombre[2].getNombre());

        Producto[] porPrecioAsc = inventario.catalogoOrdenadoPorPrecio(true);
        assertEquals(300.0, porPrecioAsc[0].getPrecio(), 0.01);
        assertEquals(1400.0, porPrecioAsc[2].getPrecio(), 0.01);

        Producto[] porPrecioDesc = inventario.catalogoOrdenadoPorPrecio(false);
        assertEquals(1400.0, porPrecioDesc[0].getPrecio(), 0.01);
        assertEquals(300.0, porPrecioDesc[2].getPrecio(), 0.01);
    }

    @Test
    void testRegistrarProductosHeterogeneosCargosAlCarrito() {
        // PARTE 1: Registrar productos heterogéneos en inventario
        Inventario inventario = new Inventario(10);

        inventario.registrar(new Computadora("LAP1", "Laptop gamer", 18999.0,
                "Lenovo", 16, 512, true), 3);
        inventario.registrar(new Telefono("TEL1", "Telefono 5G", 9999.0,
                "Motorola", 256, true), 5);

        assertEquals(2, inventario.getTamanio());
        Producto laptop = inventario.obtener("LAP1");
        assertNotNull(laptop);
        assertTrue(laptop instanceof Computadora);
        assertEquals("Computadora", laptop.getCategoria());
        assertEquals(5, inventario.existencias("TEL1"));

        // PARTE 2: Agregar productos al carrito por ID
        inventario.registrar(new Videojuego("VID1", "Mario Kart 8", 1299.0,
                "Nintendo", "Switch", "Carreras"), 4);

        Carrito carrito = new Carrito(5, inventario);

        boolean resultadoPorId = carrito.agregar("VID1", 2);

        assertTrue(resultadoPorId);
        assertEquals(2, carrito.getCantidad("VID1"));
        assertEquals(2, inventario.existencias("VID1"));
        assertEquals(2598.0, carrito.total(), 0.01);
        assertEquals(2078.4, carrito.totalConDescuento("TECH20"), 0.01);

        // PARTE 3: Agregar productos al carrito por objeto
        Tableta tableta = new Tableta("TAB1", "iPad Air", 13499.0,
                "Apple", 10.9, true);
        inventario.registrar(tableta, 2);

        boolean resultadoPorObjeto = carrito.agregar(tableta, 1);

        assertTrue(resultadoPorObjeto);
        assertEquals(1, carrito.getCantidad("TAB1"));
        assertEquals(1, inventario.existencias("TAB1"));
        assertTrue(carrito.productoEnPosicion(1) instanceof Tableta);
    }

    @Test
    void testCalcularPrecioConDescuentoPorCupon() {
        Videojuego juego = new Videojuego("HLO3", "Halo Infinite", 69.99);

        double precioConRegalo = juego.precioConDescuento("REGALO10");
        double precioConTech = juego.precioConDescuento("TECH20");
        double precioConGamer = juego.precioConDescuento("GAMER10");
        double precioSinDescuento = juego.precioConDescuento("INVALIDO");

        assertEquals(62.99, precioConRegalo, 0.01);
        assertEquals(55.99, precioConTech, 0.01);
        assertEquals(62.99, precioConGamer, 0.01);
        assertEquals(69.99, precioSinDescuento, 0.01);
    }

    @Test
    void testValidacionesYOperacionesDeInventarioYCarrito() {
        // PARTE 1: Validaciones de registro en inventario
        Inventario inventario = new Inventario(2);

        inventario.registrar(new Videojuego("   ", "Sin id", 100.0), 1);
        inventario.registrar(new Videojuego("V0", "Stock invalido", 100.0), -1);
        assertEquals(0, inventario.getTamanio());

        inventario.registrar(new Videojuego("V1", "Juego 1", 100.0), 1);
        inventario.registrar(new Videojuego("V1", "Juego repetido", 120.0), 2);
        inventario.registrar(new Telefono("T1", "Telefono", 9000.0,
                "Motorola", 128, true), 2);
        inventario.registrar(new Computadora("C1", "PC", 15000.0,
                "Lenovo", 8, 256, false), 1);

        assertEquals(2, inventario.getTamanio());
        assertNotNull(inventario.obtener("V1"));
        assertNotNull(inventario.obtener("T1"));
        assertNull(inventario.obtener("C1"));

        // PARTE 2: Operaciones de stock con casos límite
        Inventario inventarioStock = new Inventario(5);
        inventarioStock.registrar(new Videojuego("ST1", "Stock Test", 500.0), 2);

        assertFalse(inventarioStock.hayStock("ST1", 0));
        assertFalse(inventarioStock.hayStock("NO", 1));
        assertTrue(inventarioStock.hayStock("ST1", 2));

        assertFalse(inventarioStock.retirar("ST1", 0));
        assertFalse(inventarioStock.retirar("NO", 1));
        assertFalse(inventarioStock.retirar("ST1", 3));
        assertTrue(inventarioStock.retirar("ST1", 1));
        assertEquals(1, inventarioStock.existencias("ST1"));

        assertFalse(inventarioStock.reabastecer("ST1", 0));
        assertFalse(inventarioStock.reabastecer("NO", 1));
        assertTrue(inventarioStock.reabastecer("ST1", 4));
        assertEquals(5, inventarioStock.existencias("ST1"));

        // PARTE 3: Carrito con capacidad y eliminaciones inválidas
        Inventario inventarioCarrito = new Inventario(5);
        inventarioCarrito.registrar(new Videojuego("V1", "Juego 1", 100.0), 3);
        inventarioCarrito.registrar(new Videojuego("V2", "Juego 2", 200.0), 3);

        Carrito carrito = new Carrito(1, inventarioCarrito);

        assertFalse(carrito.agregar("NO", 1));
        assertFalse(carrito.agregar("V1", 0));

        assertTrue(carrito.agregar("V1", 1));
        assertTrue(carrito.agregar("V1", 1));
        assertEquals(2, carrito.getCantidad("V1"));
        assertEquals(1, inventarioCarrito.existencias("V1"));

        assertFalse(carrito.agregar("V2", 1));
        assertEquals(0, carrito.getCantidad("V2"));

        assertFalse(carrito.eliminar("NO", 1));
        assertFalse(carrito.eliminar("V1", 0));
        assertFalse(carrito.eliminar("V1", 5));
        assertTrue(carrito.eliminar("V1", 1));
        assertEquals(1, carrito.getCantidad("V1"));
        assertEquals(2, inventarioCarrito.existencias("V1"));

        // PARTE 4: Eliminar productos del carrito y devolución de stock
        Inventario inventarioEliminar = new Inventario(10);
        inventarioEliminar.registrar(new ConsolaVideojuego("CON1", "PlayStation 5", 12999.0,
                "Sony", "Slim", false), 2);

        Carrito carritoEliminacion = new Carrito(5, inventarioEliminar);
        carritoEliminacion.agregar("CON1", 2);

        boolean resultado = carritoEliminacion.eliminar("CON1", 1);

        assertTrue(resultado);
        assertEquals(1, carritoEliminacion.getCantidad("CON1"));
        assertEquals(1, inventarioEliminar.existencias("CON1"));
    }

    @Test
    void testDescuentosEnProductoYCarrito() {
        // PARTE 1: Descuento por porcentaje fuera de rango (debe ignorarse)
        Videojuego juego = new Videojuego("D1", "Juego", 100.0);

        assertEquals(100.0, juego.precioConDescuento(-10), 0.01);
        assertEquals(100.0, juego.precioConDescuento(150), 0.01);

        Inventario inventario1 = new Inventario(5);
        inventario1.registrar(new Videojuego("D2", "Juego carrito", 100.0), 2);
        Carrito carrito1 = new Carrito(2, inventario1);
        assertTrue(carrito1.agregar("D2", 2));

        assertEquals(200.0, carrito1.total(), 0.01);
        assertEquals(200.0, carrito1.totalConDescuento(-5), 0.01);   // negativo: ignorado
        assertEquals(200.0, carrito1.totalConDescuento(101), 0.01);  // mayor a 100: ignorado
        assertEquals(150.0, carrito1.totalConDescuento(25), 0.01);   // 25% válido

        // PARTE 2: Estrategia de descuento por porcentaje y monto fijo
        Inventario inventario2 = new Inventario(5);
        inventario2.registrar(new Videojuego("E1", "Juego Estrategia", 500.0), 2);

        Carrito carrito2 = new Carrito(2, inventario2);
        assertTrue(carrito2.agregar("E1", 2));

        assertEquals(1000.0, carrito2.total(), 0.01);
        assertEquals(800.0, carrito2.totalConEstrategia(new DescuentoPorcentaje(20)), 0.01);
        assertEquals(850.0, carrito2.totalConEstrategia(new DescuentoMontoFijo(150)), 0.01);
        assertEquals(0.0, carrito2.totalConEstrategia(new DescuentoMontoFijo(1500)), 0.01); // no es negativo

        // PARTE 3: Estrategia de descuento por categoría
        Inventario inventario3 = new Inventario(10);
        inventario3.registrar(new Videojuego("C1", "Zelda", 1000.0,
                "Nintendo", "Switch", "Aventura"), 2);
        inventario3.registrar(new Telefono("C2", "Moto", 3000.0,
                "Motorola", 128, true), 1);

        Carrito carrito3 = new Carrito(5, inventario3);
        assertTrue(carrito3.agregar("C1", 2)); // subtotal: 2000
        assertTrue(carrito3.agregar("C2", 1)); // subtotal: 3000

        assertEquals(5000.0, carrito3.total(), 0.01);
        // Aplica 25% solo sobre Videojuego: 2000 * 0.25 = 500 de descuento
        assertEquals(4500.0,
                carrito3.totalConEstrategia(new DescuentoPorCategoria("Videojuego", 25)), 0.01);
        // Categoría inexistente: no aplica descuento
        assertEquals(5000.0,
                carrito3.totalConEstrategia(new DescuentoPorCategoria("Tableta", 25)), 0.01);
    }

    
}