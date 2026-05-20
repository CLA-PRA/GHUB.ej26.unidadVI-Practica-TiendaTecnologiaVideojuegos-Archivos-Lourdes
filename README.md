# Unidad V - Practica: Tienda Electronica, Interfaces Excepciones, Genericidad y Persistencia

En esta practica se integran los fundamentos de Programacion Orientada a Objetos para modelar y resolver el caso de una tienda electronica, aplicando encapsulamiento, herencia, polimorfismo, clases abstractas, interfaces, arreglos, genericidad, manejo de excepciones y persistencia


## Uso del proyecto con make

### Default - Compilar + Probar + Ejecutar

```bash
make
```

### Compilar

```bash
make compile
```

### Probar todo

```bash
make test
```

### Ejecutar app

```bash
make run
```

### Limpiar binarios

```bash
make clean
```

## Compilacion manual

```bash
find ./ -type f -name "*.java" > compfiles.txt
javac -encoding utf-8 -d build -cp lib/junit-platform-console-standalone-1.5.2.jar @compfiles.txt
```

## Ejecucion manual de pruebas

```bash
java -jar lib/junit-platform-console-standalone-1.5.2.jar --class-path build --scan-class-path
```

## Ejecucion manual de la aplicacion

```bash
java -cp build miPrincipal.Principal
```
