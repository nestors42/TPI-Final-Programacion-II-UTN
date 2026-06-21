# 🍔 Food Store - Trabajo Práctico Integrador Final (Programación II UTN)

## 📌 Descripción General del Proyecto
Este proyecto es el Trabajo Práctico Integrador Final desarrollado para la materia **Programación II** de la Tecnicatura Superior. Consiste en un sistema de backend robusto para la gestión y facturación automatizada de un **Food Store (Tienda de Alimentos)** desarrollado enteramente en **Java**.

La aplicación implementa una arquitectura limpia y modular dividida en capas de responsabilidad (`config`, `entities`, `enums`, `exception`, `interfaces`, `service` y `ui`). La persistencia de datos y la simulación de tablas relacionales se gestionan de forma centralizada en memoria a través de colecciones dinámicas de objetos eficientes, garantizando la consistencia global mediante reglas de negocio automatizadas.

---

## 📐 Estructura de Clases y Relaciones del Sistema

El sistema emula el comportamiento y las restricciones de integridad de una base de datos relacional a través de referencias orientadas a objetos y colecciones bidireccionales en memoria:

```text
       [ Base ]  <-- Clase Abstracta Padre de todo el Modelo
          ^
     _____|______________________________________________
    |           |               |            |           |
[Usuario]  [Categoria]     [Producto]     [Pedido]  [DetallePedido]
    |           |               |            |           |
    | (1)       | (1)           |            | (1)       |
    |           |               |            |           |
    |           |====(1..*)====>|            |           |
    |           |               |            |           |
    | (0..*)    |               | (1)        | (1..*)    |
    |<==========|===============|============|           |
                |<=======================================|

[ MenuPantalla ]  (Interfaz: define contrato ejecutar())
              ^
              |
          [ MenuBase ]   (Clase Abstracta: maneja bucle while y escáner protegido)
              ^
     _________|_____________________________________________
    |               |                 |                     |
 [Menu]     [MenuCategorias]   [MenuProductos]       [MenuPedidos]
(Principal)


## al ejecutar la clase main
=== SISTEMA DE PEDIDOS (FOOD STORE) ===
1. Gestión de Categorías
2. Gestión de Productos
3. Gestión de Usuarios
4. Gestión de Pedidos
0. Salir
Seleccione una opción:

## si se ingresa la opcion 1 y luego se listan las categorias, la consola dibuja grillas formateadas
ID  | NOMBRE          | DESCRIPCIÓN
----------------------------------------------------
1   | Pizzas          | Pizzas artesanales al horno de barro
2   | Hamburguesas    | Hamburguesas caseras con papas fritas

## flujo de venta. al seleccionar la opción "Registrar nuevo pedido", el sistema guía de forma interactiva la venta cruzando los datos del sistema:
-> INICIANDO NUEVA ORDEN DE COMPRA
Seleccione el ID del cliente:
 [1] - Néstor Silva
ID del cliente: 1

Catálogo disponible:
 [1] - Pizza Muzarella      | Precio: $4500.00 | Stock actual: 15
 [2] - Hamburguesa Doble    | Precio: $6200.00 | Stock actual: 8

ID del producto a agregar: 2
Cantidad de unidades: 2
¿Desea añadir otro artículo al pedido? (S/N): N

Formas de pago aceptadas:
 1. TARJETA
 2. TRANSFERENCIA
 3. EFECTIVO
Seleccione una opción: 3

[ÉXITO] ¡Pedido aprobado e ingresado a cocina!
Pedido Número: #1 | Facturado: $12400.00

## simulacion de alertas
## Si intentas realizar un pedido de un artículo superando su stock real (por ejemplo, pidiendo 20 Hamburguesas Doble cuando solo quedan 8), el sistema ejecuta el Rollback de stock automáticamente y muestra el error controlado de manera elegante en la terminal sin tumbar la aplicación:
[ERROR] TRANSACCIÓN ABORTADA: El pedido se canceló debido a: Falta de stock para 'Hamburguesa Doble'. Solicitado: 20 | Disponible: 8

## requisitos de instalacion y ejecucion
git clone [https://github.com/nestors42/TPI-Final-Programacion-II-UTN.git](https://github.com/nestors42/TPI-Final-Programacion-II-UTN.git)
