package ui;

import entities.Usuario;
import enums.Rol;
import exception.DatoInvalidoException;
import exception.EntidadNoEncontradaException;
import interfaces.MenuPantalla;
import service.UsuarioService;
import java.util.List;

public class MenuUsuarios extends MenuBase implements MenuPantalla {
    private UsuarioService usuarioService;

    public MenuUsuarios(UsuarioService usuarioService) {
        super();
        this.usuarioService = usuarioService;
    }

    @Override
    public void mostrarOpciones() {
        System.out.println("\n--- GESTIÓN DE USUARIOS ---");
        System.out.println("1. Listar usuarios");
        System.out.println("2. Crear usuario");
        System.out.println("3. Editar usuario");
        System.out.println("4. Eliminar usuario");
        System.out.println("0. Volver al menú principal");
    }

    //Sincronizado con la interfaz MenuPantalla

    @Override
    protected void evaluarOpcion(int opcion) {
        switch (opcion) {
            case 1: vistaListarUsuarios(); break;
            case 2: vistaCrearUsuario(); break;
            case 3: vistaEditarUsuario(); break;
            case 4: vistaEliminarUsuario(); break;
        }
    }

    //Listar usuarios
    private void vistaListarUsuarios() {
        List<Usuario> activos = usuarioService.listarUsuariosActivos();
        if (activos.isEmpty()) {
            System.out.println("\n[INFO] No hay usuarios registrados en el sistema.");
            return;
        }
        System.out.println("\nID  | NOMBRE Y APELLIDO      | E-MAIL                   | ROL");
        System.out.println("-----------------------------------------------------------------");
        for (Usuario u : activos) {
            System.out.printf("%-3d | %-22s | %-24s | [%s]\n",
                    u.getId(), u.getNombre() + " " + u.getApellido(), u.getMail(), u.getRol());
        }
    }

    // Crear usuario seleccionando Rol con opciones numéricas
    private void vistaCrearUsuario() {
        System.out.println("\n-> REGISTRAR NUEVO USUARIO");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        System.out.print("E-mail (Único): ");
        String mail = scanner.nextLine();
        System.out.print("Celular: ");
        String celular = scanner.nextLine();
        System.out.print("Contraseña: ");
        String contrasenia = scanner.nextLine();

        System.out.println("Seleccione el Rol:");
        System.out.println(" 1. ADMIN");
        System.out.println(" 2. USUARIO");
        int opRol = capturarOpcionNumericaSegura(1, 2);
        Rol rolSeleccionado = (opRol == 1) ? Rol.ADMIN : Rol.USUARIO;

        try {
            Usuario nuevo = usuarioService.crearUsuario(nombre, apellido, mail, celular, contrasenia, rolSeleccionado);
            System.out.println("\n[ÉXITO] Usuario registrado con el ID: " + nuevo.getId());
        } catch (DatoInvalidoException e) {
            System.out.println("\n[ERROR] " + e.getMessage());
        }
    }

    //Editar usuario
    private void vistaEditarUsuario() {
        vistaListarUsuarios(); // Listado previo recomendado [cite: 266]
        System.out.print("\nIngrese el ID del usuario que desea modificar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            usuarioService.buscarPorId(id); // Valida si el ID existe previo al cuestionario

            System.out.print("Nuevo nombre (Enter para omitir): ");
            String nombre = scanner.nextLine();
            System.out.print("Nuevo apellido (Enter para omitir): ");
            String apellido = scanner.nextLine();
            System.out.print("Nuevo e-mail (Enter para omitir): ");
            String mail = scanner.nextLine();
            System.out.print("Nuevo celular (Enter para omitir): ");
            String celular = scanner.nextLine();
            System.out.print("Nueva contraseña (Enter para omitir): ");
            String nuevaContrasenia = scanner.nextLine();

            System.out.println("¿Cambiar el Rol? (S/N): ");
            Rol nuevoRol = null;
            if (scanner.nextLine().equalsIgnoreCase("S")) {
                System.out.println("Seleccione nuevo Rol:\n 1. ADMIN\n 2. USUARIO");
                int opRol = capturarOpcionNumericaSegura(1, 2);
                nuevoRol = (opRol == 1) ? Rol.ADMIN : Rol.USUARIO;
            }

            // Invocación corregida enviando 'nuevaContrasenia' en perfecta sintonía con tu servicio
            usuarioService.editarUsuario(id, nombre, apellido, mail, celular, nuevaContrasenia, nuevoRol);
            System.out.println("\n[ÉXITO] Datos de usuario actualizados.");

        } catch (NumberFormatException e) {
            System.out.println("\n[ERROR] Ingreso de ID inválido.");
        } catch (EntidadNoEncontradaException | DatoInvalidoException e) {
            System.out.println("\n[ERROR] " + e.getMessage());
        }
    }

    // Eliminar usuario
    private void vistaEliminarUsuario() {
        vistaListarUsuarios(); // Listado previo
        System.out.print("\nIngrese el ID del usuario a eliminar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            System.out.print("¿Seguro que desea dar de baja a este usuario? (S/N): ");
            if (scanner.nextLine().equalsIgnoreCase("S")) {
                usuarioService.eliminarUsuario(id);
                System.out.println("\n[ÉXITO] Usuario dado de baja lógicamente del sistema.");
            } else {
                System.out.println("\n[INFO] Operación cancelada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("\n[ERROR] El ID debe ser numérico entero.");
        } catch (EntidadNoEncontradaException e) {
            System.out.println("\n[ERROR] " + e.getMessage());
        }
    }

    @Override
    protected int getOpcionMaxima() {
        return 4; // El menú principal va del 0 al 4
    }
}