# Sistema de Gestión de Biblioteca – TP2

Trabajo Práctico Nº 2 – Metodología de Pruebas de Sistemas – 3º Software
Prof. Ariel Reynoso

**Integrantes:** Lourdes (Líder, Desarrolladora, Automatizadora) · Camila (Analista funcional, Tester 1)

## Descripción

Aplicación de consola en Java que permite administrar socios y libros, consultar la
disponibilidad de ejemplares y registrar préstamos y devoluciones, aplicando las reglas
de negocio RN01 a RN12 del enunciado. Los datos se guardan en memoria.

## Requisitos

- Java 17 o superior
- Maven (incluido en IntelliJ IDEA)

## Cómo ejecutar

**Aplicación:** abrir el proyecto en IntelliJ y ejecutar `src/main/java/biblioteca/Main.java`.
El menú se maneja escribiendo el número de opción en la consola.

La app inicia con datos de prueba precargados, usados como precondición de los casos de prueba:

| Tipo | Dato |
| --- | --- |
| Libro | Ficciones – ISBN 9789500000001 – 3 ejemplares |
| Libro | Rayuela – ISBN 9789500000002 – 1 ejemplar |
| Libro | Martín Fierro – ISBN 9789500000003 – 2 ejemplares |
| Socio | Pedro Ruiz – DNI 33999000 – Inactivo |

La opción 10 del menú reinicia estos datos.

**Pruebas automatizadas (JUnit 5):** clic derecho en `src/test/java` → *Ejecutar 'Todas las pruebas'*,
o desde la terminal:

```
mvn test
```

## Estructura

```
src/main/java/biblioteca/
├── Main.java                 Menú por consola
├── modelo/                   Socio, Libro, Prestamo
└── servicio/                 SocioService, LibroService, PrestamoService (reglas de negocio)
src/test/java/biblioteca/servicio/
├── LibroServiceTest.java     Caso 1 – Disponibilidad de un libro
└── PrestamoServiceTest.java  Casos 2, 3 y 4 – Máximo de préstamos, prestar y devolver
```
