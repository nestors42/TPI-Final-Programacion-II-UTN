package ui;

import entities.Categoria;
import exception.DatoInvalidoException;
import exception.EntidadNoEncontradaException;
import interfaces.MenuPantalla;
import service.CategoriaService;
import java.util.List;

public class MenuCategorias extends MenuBase implements MenuPantalla {
    private CategoriaService categoriaService;

    public MenuCategorias(CategoriaService categoriaService) {
        super();
        this.categoriaService = categoriaService;
    }

    @Override
    public void mostrarOpciones() {
        System.out.println("\n--- GESTIÓN DE CATEGORÍAS ---");
        System.out.println("1. Listar categorías");
        System.out.println("2. Crear categoría");
        System.out.println("3. Editar categoría");
        System.out.println("4. Eliminar categoría");
        System.out.println("0. Volver al menú principal");
    }

    //Sincronizado con la interfaz MenuPantalla
    @Override
    protected void evaluarOpcion(int opcion) {
        switch (opcion) {
            case 1: vistaListarCategorias(); break;
            case 2: vistaCrearCategoria(); break;
            case 3: vistaEditarCategoria(); break;
            case 4: vistaEliminarCategoria(); break;
        }
    }

    private void vistaListarCategorias() {
        List<Categoria> activas = categoriaService.listarCategoriasActivas();
        if (activas.isEmpty()) {
            System.out.println("\n[INFO] No hay categorías cargadas.");
            return;
        }
        System.out.println("\nID  | NOMBRE          | DESCRIPCIÓN");
        System.out.println("----------------------------------------------------");
        for (Categoria c : activas) {
            System.out.printf("%-3d | %-15s | %s\n", c.getId(), c.getNombre(), c.getDescripcion());
        }
    }

    private void vistaCrearCategoria() {
        System.out.print("Nombre de la categoría: ");
        String nombre = scanner.nextLine(); // Usamos el scanner protegido del padre
        System.out.print("Descripción de la categoría: ");
        String descripcion = scanner.nextLine();

        try {
            Categoria nueva = categoriaService.crearCategoria(nombre, descripcion);
            System.out.println("\n[ÉXITO] Categoría registrada. ID: " + nueva.getId());
        } catch (DatoInvalidoException e) {
            System.out.println("\n[ERROR] " + e.getMessage());
        }
    }

    private void vistaEditarCategoria() {
        vistaListarCategorias();
        System.out.print("\nIngrese el ID de la categoría a editar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            System.out.print("Nuevo nombre (Enter para omitir): ");
            String nombre = scanner.nextLine();
            System.out.print("Nueva descripción (Enter para omitir): ");
            String descripcion = scanner.nextLine();

            categoriaService.editarCategoria(id, nombre, descripcion);
            System.out.println("\n[ÉXITO] Categoría modificada correctamente.");
        } catch (NumberFormatException e) {
            System.out.println("\n[ERROR] El ID debe ser un número entero.");
        } catch (EntidadNoEncontradaException | DatoInvalidoException e) {
            System.out.println("\n[ERROR] " + e.getMessage());
        }
    }

    private void vistaEliminarCategoria() {
        vistaListarCategorias();
        System.out.print("\nIngrese el ID de la categoría a eliminar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            System.out.print("¿Confirma la baja? (S/N): ");
            String confirmacion = scanner.nextLine();

            if (confirmacion.equalsIgnoreCase("S")) {
                categoriaService.eliminarCategoria(id);
                System.out.println("\n[ÉXITO] Categoría eliminada lógicamente.");
            } else {
                System.out.println("\n[INFO] Operación cancelada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("\n[ERROR] El ID debe ser un número válido.");
        } catch (EntidadNoEncontradaException e) {
            System.out.println("\n[ERROR] " + e.getMessage());
        }
    }

    @Override
    protected int getOpcionMaxima() {
        return 4; // El menú principal va del 0 al 4
    }



}
