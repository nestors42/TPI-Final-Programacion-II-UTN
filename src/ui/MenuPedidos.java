package ui;

import entities.Pedido;
import entities.DetallePedido;
import entities.Producto;
import entities.Usuario;
import enums.Estado;
import enums.FormaPago;
import exception.DatoInvalidoException;
import exception.EntidadNoEncontradaException;
import interfaces.MenuPantalla;
import service.PedidoService;
import service.ProductoService;
import service.UsuarioService;
import java.util.ArrayList;
import java.util.List;

public class MenuPedidos extends MenuBase implements MenuPantalla {
    private PedidoService pedidoService;
    private UsuarioService usuarioService;
    private ProductoService productoService;

    public MenuPedidos(PedidoService pedidoService, UsuarioService usuarioService, ProductoService productoService) {
        super();
        this.pedidoService = pedidoService;
        this.usuarioService = usuarioService;
        this.productoService = productoService;
    }

    @Override
    public void mostrarOpciones() {
        System.out.println("\n--- GESTIÓN DE PEDIDOS Y FACTURACIÓN ---");
        System.out.println("1. Listar pedidos (Historial)");
        System.out.println("2. Registrar nuevo pedido (Venta)");
        System.out.println("3. Actualizar estado / forma de pago");
        System.out.println("4. Cancelar pedido (Baja lógica)");
        System.out.println("0. Volver al menú principal");
    }

    // 🌟 Sincronizado con la interfaz MenuPantalla
    @Override
    public void ejecutar() {
        int opcionSub = -1;
        while (opcionSub != 0) {
            mostrarOpciones();
            opcionSub = capturarOpcionNumericaSegura(0, 4);

            switch (opcionSub) {
                case 1: vistaListarPedidos(); break;
                case 2: vistaCrearPedido(); break;
                case 3: vistaActualizarPedido(); break;
                case 4: vistaEliminarPedido(); break;
                case 0: break;
            }
        }
    }

    // Listar pedidos detallando cada ítem y su total final por pantalla
    private void vistaListarPedidos() {
        List<Pedido> activos = pedidoService.listarPedidosActivos();
        if (activos.isEmpty()) {
            System.out.println("\n[INFO] No se registran pedidos cargados en el sistema. [cite: 249]");
            return;
        }

        System.out.println("\n===============================================================================");
        System.out.println("                     HISTORIAL GENERAL DE COMPRAS");
        System.out.println("===============================================================================");
        for (Pedido p : activos) {
            System.out.printf("> PEDIDO #%d | Fecha: %s | Cliente: %s | Estado: %s | Pago: %s\n",
                    p.getId(), p.getFecha(), p.getUsuario().getNombre() + " " + p.getUsuario().getApellido(),
                    p.getEstado(), p.getFormaPago());
            System.out.println("  ---------------------------------------------------------------------------");
            for (DetallePedido dp : p.getDetalles()) {
                System.out.printf("  - %-25s x%-3d  => Subtotal: $%.2f\n",
                        dp.getProducto().getNombre(), dp.getCantidad(), dp.getSubtotal());
            }
            System.out.printf("  >> TOTAL FINAL FACTURADO: $%.2f\n", p.getTotal());
            System.out.println("===============================================================================\n");
        }
    }

    //Registrar pedido interconectando las colecciones
    private void vistaCrearPedido() {
        System.out.println("\n-> INICIANDO NUEVA ORDEN DE COMPRA");

        // 1. Selección obligatoria de un Usuario activo
        System.out.println("Seleccione el ID del cliente:");
        List<Usuario> usuarios = usuarioService.listarUsuariosActivos();
        if (usuarios.isEmpty()) {
            System.out.println("[ALERTA] Debe registrar al menos un usuario en el sistema antes de facturar.");
            return;
        }
        for (Usuario u : usuarios) {
            System.out.printf(" [%d] - %s %s\n", u.getId(), u.getNombre(), u.getApellido());
        }
        System.out.print("ID del cliente: ");

        try {
            Long userId = Long.parseLong(scanner.nextLine());
            Usuario usuarioSeleccionado = usuarioService.buscarPorId(userId);

            // Listamos productos disponibles para armar la orden
            List<Producto> productosDisponibles = productoService.listarProductosActivos();
            if (productosDisponibles.isEmpty()) {
                System.out.println("[ALERTA] Catálogo de productos vacío. Cargue artículos antes de continuar.");
                return;
            }

            // Carrito temporal en memoria
            List<Producto> productosCarrito = new ArrayList<>();
            List<Integer> cantidadesCarrito = new ArrayList<>();

            String agregarMas = "S";
            while (agregarMas.equalsIgnoreCase("S")) {
                System.out.println("\nCatálogo disponible:");
                for (Producto prod : productosDisponibles) {
                    System.out.printf(" [%d] - %-20s | Precio: $%-7.2f | Stock actual: %d\n",
                            prod.getId(), prod.getNombre(), prod.getPrecio(), prod.getStock());
                }

                System.out.print("ID del producto a agregar: ");
                Long prodId = Long.parseLong(scanner.nextLine());
                Producto prodSeleccionado = productoService.buscarPorId(prodId);

                System.out.print("Cantidad de unidades: ");
                int cantidad = Integer.parseInt(scanner.nextLine());

                // Añadimos al buffer temporal del carrito
                productosCarrito.add(prodSeleccionado);
                cantidadesCarrito.add(cantidad);

                System.out.print("¿Desea añadir otro artículo al pedido? (S/N): ");
                agregarMas = scanner.nextLine();
            }

            // Seleccionamos la Forma de Pago
            System.out.println("\nFormas de pago aceptadas:");
            System.out.println(" 1. TARJETA\n 2. TRANSFERENCIA\n 3. EFECTIVO");
            int opPago = capturarOpcionNumericaSegura(1, 3);
            FormaPago formaSeleccionada = FormaPago.values()[opPago - 1];

            // confirmado el proceso de transaccion
            Pedido confirmed = pedidoService.crearPedidoTransaccional(usuarioSeleccionado, formaSeleccionada, productosCarrito, cantidadesCarrito);

            System.out.println("\n[ÉXITO] ¡Pedido aprobado e ingresado a cocina!");
            System.out.printf("Pedido Número: #%d | Facturado: $%.2f\n", confirmed.getId(), confirmed.getTotal());

        } catch (NumberFormatException e) {
            System.out.println("\n[ERROR] Ingreso numérico incorrecto.");
        } catch (EntidadNoEncontradaException | DatoInvalidoException e) {
            System.out.println("\n[ERROR] " + e.getMessage()); // Captura el rebote por stock insuficiente [cite: 382]
        }
    }

    // HU-PED-03: Actualizar Estado/Forma de Pago con los enums
    private void vistaActualizarPedido() {
        System.out.println("\n-> ACTUALIZAR INFORMACIÓN DE PEDIDO");
        System.out.print("Ingrese el ID del pedido a modificar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            pedidoService.buscarPorId(id); // Valida si existe

            System.out.println("¿Desea actualizar el Estado del envío? (S/N): ");
            Estado nuevoEstado = null;
            if (scanner.nextLine().equalsIgnoreCase("S")) {
                System.out.println("Estados válidos: 1. PENDIENTE | 2. CONFIRMADO | 3. TERMINADO | 4. CANCELADO [cite: 102]");
                int op = capturarOpcionNumericaSegura(1, 4);
                nuevoEstado = Estado.values()[op - 1];
            }

            System.out.println("¿Desea modificar la Forma de Pago? (S/N): ");
            FormaPago nuevaForma = null;
            if (scanner.nextLine().equalsIgnoreCase("S")) {
                System.out.println("Opciones: 1. TARJETA | 2. TRANSFERENCIA | 3. EFECTIVO [cite: 99]");
                int op = capturarOpcionNumericaSegura(1, 3);
                nuevaForma = FormaPago.values()[op - 1];
            }

            pedidoService.actualizarPedido(id, nuevoEstado, nuevaForma);
            System.out.println("\n[ÉXITO] Los datos del pedido fueron actualizados. [cite: 389]");

        } catch (NumberFormatException e) {
            System.out.println("\n[ERROR] El ID debe ser un número entero.");
        } catch (EntidadNoEncontradaException e) {
            System.out.println("\n[ERROR] " + e.getMessage());
        }
    }

    // Eliminar Pedido
    private void vistaEliminarPedido() {
        System.out.println("\n-> CANCELACIÓN DE PEDIDO (SOFT DELETE) [cite: 391]");
        System.out.print("Ingrese el ID del pedido que desea dar de baja: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            System.out.print("¿Confirma la baja de la orden de compra? (S/N): [cite: 274]");
            if (scanner.nextLine().equalsIgnoreCase("S")) {
                pedidoService.eliminarPedido(id);
                System.out.println("\n[ÉXITO] Orden removida de las pantallas de gestión activa. [cite: 395]");
            } else {
                System.out.println("\n[INFO] Cancelación suspendida.");
            }
        } catch (NumberFormatException e) {
            System.out.println("\n[ERROR] El ID debe ser numérico.");
        } catch (EntidadNoEncontradaException e) {
            System.out.println("\n[ERROR] " + e.getMessage());
        }
    }
}