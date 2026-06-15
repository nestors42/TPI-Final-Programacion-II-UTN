package ui;

import entities.Categoria;
import entities.Producto;
import exception.DatoInvalidoException;
import exception.EntidadNoEncontradaException;
import service.CategoriaService;
import service.ProductoService;

import java.util.List;

public class MenuProductos extends MenuBase {
    private ProductoService productoService;
    private CategoriaService categoriaService;

    public MenuProductos(ProductoService productoService, CategoriaService categoriaService) {
        super(); // Inicializa el Scanner de la base heredada
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @Override
    public void mostrarOpciones() {
        System.out.println("\n--- GESTIÓN DE PRODUCTOS ---");
        System.out.println("1. Listar productos");
        System.out.println("2. Crear producto");
        System.out.println("3. Editar producto");
        System.out.println("4. Eliminar producto");
        System.out.println("0. Volver al menú principal");
    }

    public void ejecutarSubmenu() {
        int opcionSub = -1;
        while (opcionSub != 0) {
            mostrarOpciones();
            opcionSub = capturarOpcionNumericaSegura(0, 4); // Reutiliza el método protegido de la base

            switch (opcionSub) {
                case 1: vistaListarProductos(); break;
                case 2: vistaCrearProducto(); break;
                case 3: vistaEditarProducto(); break;
                case 4: vistaEliminarProducto(); break;
                case 0: break;
            }
        }
    }

    // HU-PROD-01: Listar productos activos
    private void vistaListarProductos() {
        List<Producto> activos = productoService.listarProductosActivos();
        if (activos.isEmpty()) {
            System.out.println("\n[INFO] No hay productos registrados en el catálogo.");
            return;
        }
        System.out.println("\nID  | PRODUCTO             | PRECIO   | STOCK | CATEGORÍA");
        System.out.println("-----------------------------------------------------------------");
        for (Producto p : activos) {
            String nombreCat = (p.getCategoria() != null) ? p.getCategoria().getNombre() : "Sin Categoría";
            System.out.printf("%-3d | %-20s | $%-7.2f | %-5d | %s\n",
                    p.getId(), p.getNombre(), p.getPrecio(), p.getStock(), nombreCat);
        }
    }

    // HU-PROD-02: Crear producto pidiendo ID de categoría (con validación de existencia)
    private void vistaCrearProducto() {
        System.out.println("\n-> AGREGAR NUEVO PRODUCTO AL CATÁLOGO");
        System.out.print("Nombre del producto: ");
        String nombre = scanner.nextLine();

        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine();

        System.out.print("Precio unitario: ");
        Double precio = null;
        try { precio = Double.parseDouble(scanner.nextLine()); } catch (NumberFormatException e) { precio = -1.0; }

        System.out.print("Stock inicial: ");
        int stock = -1;
        try { stock = Integer.parseInt(scanner.nextLine()); } catch (NumberFormatException e) { stock = -1; }

        System.out.print("Nombre del archivo de imagen (ej: pizza.png): ");
        String imagen = scanner.nextLine();

        // Para asociar la categoría, listar previamente antes de pedir ID
        System.out.println("\nCategorías disponibles para asociar:");
        List<Categoria> catsActivas = categoriaService.listarCategoriasActivas();
        for (Categoria c : catsActivas) {
            System.out.printf(" [%d] - %s\n", c.getId(), c.getNombre());
        }
        System.out.print("Seleccione el ID de la categoría para este producto: ");

        try {
            Long catId = Long.parseLong(scanner.nextLine());
            Categoria asociada = categoriaService.buscarPorId(catId); // Valida si el ID existe

            Producto nuevo = productoService.crearProducto(nombre, precio, descripcion, stock, imagen, true, asociada);
            System.out.println("\n[ÉXITO] Producto registrado con el ID: " + nuevo.getId());

        } catch (NumberFormatException e) {
            System.out.println("\n[ERROR] El ID de la categoría debe ser numérico entero.");
        } catch (EntidadNoEncontradaException | DatoInvalidoException e) {
            System.out.println("\n[ERROR] " + e.getMessage());
        }
    }

    //Editar producto
    private void vistaEditarProducto() {
        vistaListarProductos();
        System.out.print("\nIngrese el ID del producto que desea modificar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            productoService.buscarPorId(id); // Verifica existencia previa

            System.out.print("Nuevo nombre (Enter para omitir): ");
            String nombre = scanner.nextLine();

            System.out.print("Nuevo precio (Enter para omitir): ");
            String precioStr = scanner.nextLine();
            Double precio = precioStr.isEmpty() ? null : Double.parseDouble(precioStr);

            System.out.print("Nueva descripción (Enter para omitir): ");
            String descripcion = scanner.nextLine();

            System.out.print("Nuevo stock (Enter para omitir): ");
            String stockStr = scanner.nextLine();
            Integer stock = stockStr.isEmpty() ? null : Integer.parseInt(stockStr);

            System.out.print("Nueva imagen (Enter para omitir): ");
            String imagen = scanner.nextLine();

            System.out.print("¿Cambiar categoría? (S/N): ");
            Categoria nuevaCategoria = null;
            if (scanner.nextLine().equalsIgnoreCase("S")) {
                System.out.print("Ingrese el ID de la nueva categoría: ");
                Long catId = Long.parseLong(scanner.nextLine());
                nuevaCategoria = categoriaService.buscarPorId(catId);
            }

            productoService.editarProducto(id, nombre, precio, descripcion, stock, imagen, null, nuevaCategoria);
            System.out.println("\n[ÉXITO] Producto actualizado correctamente en el catálogo.");

        } catch (NumberFormatException e) {
            System.out.println("\n[ERROR] Ingreso numérico inválido.");
        } catch (EntidadNoEncontradaException | DatoInvalidoException e) {
            System.out.println("\n[ERROR] " + e.getMessage());
        }
    }

    //Eliminar producto
    private void vistaEliminarProducto() {
        vistaListarProductos();
        System.out.print("\nIngrese el ID del producto que desea dar de baja: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            System.out.print("¿Seguro que desea ocultar este producto del catálogo? (S/N): ");
            if (scanner.nextLine().equalsIgnoreCase("S")) {
                productoService.eliminarProducto(id);
                System.out.println("\n[ÉXITO] Producto removido lógicamente del catálogo activo.");
            } else {
                System.out.println("\n[INFO] Operación cancelada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("\n[ERROR] El ID debe ser un número entero.");
        } catch (EntidadNoEncontradaException e) {
            System.out.println("\n[ERROR] " + e.getMessage());
        }
    }
}