# Guia breve para estudiantes

## Que se agrego en esta etapa

- Genericidad basica:
  - ListaGenerica<T>
  - RepositorioGenerico<T, ID>
  - RepositorioProductos
- Persistencia minima en archivos .dat:
  - PersistenciaManejador
  - Guardar/cargar en Inventario
  - Guardar/cargar en Carrito
- Carga automatica al iniciar:
  - Principal intenta cargar inventario_principal.dat
  - Si no existe, crea catalogo demo

## Como ejecutar

```bash
make compile
make test
make run
```

## Clases nuevas clave

- logica/ListaGenerica.java
- logica/RepositorioGenerico.java
- logica/RepositorioProductos.java
- logica/PersistenciaManejador.java
- miTest/GenericidadPersistenciaTest.java

## Flujo simple de persistencia

1. Ejecutas la app con make run.
2. El sistema intenta cargar inventario guardado.
3. Trabajas en el menu (altas, carrito, stock).
4. Al salir, se guarda el inventario automaticamente.

## Nota

- Los archivos de persistencia se guardan en la carpeta datos/.
- Si quieres reiniciar el estado, elimina los archivos .dat de datos/.

## Matriz de evaluación

Evaluación:

- Genericidad (30%):
  ListaGenérica, RepositorioGenérico y RepositorioProductos funcionan correctamente.
- Persistencia (30%):
  Inventario y Carrito se pueden guardar y cargar en archivos 
- Integración en Principal (20%):
  El sistema carga automáticamente el inventario, el menú es funcional y guarda al salir.
- Pantalla de autentificación con archivo de    
  propiedades (10%)
- Trabajo en equipo (10%)

Nota: Para obtener el máximo puntaje, todo debe compilar y funcionar correctamente.
