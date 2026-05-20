package miTest;

import logica.Carrito;
import logica.Inventario;
import logica.ListaGenérica;
import logica.RepositorioProductos;
import logica.Videojuego;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenericidadPersistenciaTest {

    @Test
    void testPersistenciaInventarioBasica() {
        Inventario inventario = new Inventario(5);
        inventario.registrar(new Videojuego("PI1", "Persistencia Inventario", 999.0), 3);

        assertTrue(inventario.guardarEnArchivo("inventario_minimo"));
        assertTrue(Inventario.existeArchivoGuardado("inventario_minimo"));

        Inventario cargado = Inventario.cargarDesdeArchivo("inventario_minimo");
        assertNotNull(cargado);
        assertEquals(1, cargado.getTamanio());
        assertEquals(3, cargado.existencias("PI1"));
    }

    @Test
    void testPersistenciaCarritoBasica() {
        Inventario inventario = new Inventario(5);
        inventario.registrar(new Videojuego("PC1", "Persistencia Carrito", 500.0), 4);

        Carrito carrito = new Carrito(5, inventario);
        assertTrue(carrito.agregar("PC1", 2));

        assertTrue(carrito.guardarEnArchivo("carrito_minimo"));
        assertTrue(Carrito.existeArchivoGuardado("carrito_minimo"));

        Carrito cargado = Carrito.cargarDesdeArchivo("carrito_minimo");
        assertNotNull(cargado);
        assertEquals(2, cargado.getCantidad("PC1"));
        assertEquals(1000.0, cargado.total(), 0.01);
    }

    @Test
    void testListaGenericaAgregarYObtener() {
        ListaGenérica<String> lista = new ListaGenérica<>();

        lista.agregar("uno");
        lista.agregar("dos");

        assertEquals(2, lista.tamanio());
        assertEquals("uno", lista.obtener(0));
        assertEquals("dos", lista.obtener(1));
    }

    @Test
    void testListaGenericaEliminar() {
        ListaGenérica<Integer> lista = new ListaGenérica<>();
        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        int eliminado = lista.eliminar(1);

        assertEquals(20, eliminado);
        assertEquals(2, lista.tamanio());
        assertEquals(30, lista.obtener(1));
    }

    @Test
    void testListaGenericaRedimension() {
        ListaGenérica<Integer> lista = new ListaGenérica<>(1);

        lista.agregar(1);
        lista.agregar(2);
        lista.agregar(3);

        assertEquals(3, lista.tamanio());
        assertEquals(3, lista.obtener(2));
    }

    @Test
    void testListaGenericaIterador() {
        ListaGenérica<String> lista = new ListaGenérica<>();
        lista.agregar("a");
        lista.agregar("b");
        lista.agregar("c");

        StringBuilder acumulado = new StringBuilder();
        for (String valor : lista) {
            acumulado.append(valor);
        }

        assertEquals("abc", acumulado.toString());
    }

    @Test
    void testRepositorioGuardarObtenerYExiste() {
        RepositorioProductos repo = new RepositorioProductos();
        Videojuego juego = new Videojuego("VG1", "Zelda", 1200.0);

        assertTrue(repo.guardar(juego));
        assertFalse(repo.guardar(juego));
        assertTrue(repo.existe("VG1"));
        assertEquals("Zelda", repo.obtener("VG1").getNombre());
        assertEquals(1, repo.contar());
    }

    @Test
    void testRepositorioActualizarYEliminar() {
        RepositorioProductos repo = new RepositorioProductos();
        repo.guardar(new Videojuego("VG2", "Mario", 1000.0));

        assertTrue(repo.actualizar(new Videojuego("VG2", "Mario Deluxe", 1300.0)));
        assertEquals("Mario Deluxe", repo.obtener("VG2").getNombre());
        assertEquals(1, repo.obtenerTodas().tamanio());

        assertTrue(repo.eliminar("VG2"));
        assertFalse(repo.existe("VG2"));
        assertEquals(0, repo.contar());
    }
}
