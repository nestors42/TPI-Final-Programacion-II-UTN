package ui;

import interfaces.MenuPantalla;
import service.CategoriaService;
import service.PedidoService;
import service.ProductoService;
import service.UsuarioService;

public class Menu extends MenuBase implements MenuPantalla {
    private CategoriaService categoriaService;
    private ProductoService productoService;
    private UsuarioService usuarioService;
    private PedidoService pedidoService;

    // 🌟 POLIMORFISMO: Declaramos los submenús usando el tipo de la Interfaz
    private MenuPantalla menuCategorias;
    private MenuPantalla menuProductos;
    private MenuPantalla menuUsuarios;
    private MenuPantalla menuPedidos;

    // CONSTRUCTOR DEFINITIVO: Recibe los 4 servicios en sintonía con tu Main
    public Menu(CategoriaService categoriaService, ProductoService productoService,
                UsuarioService usuarioService, PedidoService pedidoService) {
        super(); // Inicializa el Scanner heredado de MenuBase
        this.categoriaService = categoriaService;
        this.productoService = productoService;
        this.usuarioService = usuarioService;
        this.pedidoService = pedidoService;

        // Inicializamos los submenús (cuyas clases implementan el contrato de MenuPantalla)
        this.menuCategorias = new MenuCategorias(categoriaService);
        this.menuProductos = new MenuProductos(productoService, categoriaService);
        this.menuUsuarios = new MenuUsuarios(usuarioService);
        this.menuPedidos = new MenuPedidos(pedidoService, usuarioService, productoService);
    }

    @Override
    public void mostrarOpciones() {
        System.out.println("\n=== SISTEMA DE PEDIDOS (FOOD STORE) ===");
        System.out.println("1. Gestión de Categorías");
        System.out.println("2. Gestión de Productos");
        System.out.println("3. Gestión de Usuarios");
        System.out.println("4. Gestión de Pedidos");
        System.out.println("0. Salir");
    }

    // 🌟 Sincronizado con el contrato obligatorio de la interfaz MenuPantalla
    @Override
    public void ejecutar() {
        int opcion = -1;
        while (opcion != 0) {
            mostrarOpciones();
            opcion = capturarOpcionNumericaSegura(0, 4);

            switch (opcion) {
                // Invocación polimórfica: no importa qué pantalla sea, todas saben qué hacer al llamar a .ejecutar()
                case 1: menuCategorias.ejecutar(); break;
                case 2: menuProductos.ejecutar(); break;
                case 3: menuUsuarios.ejecutar(); break;
                case 4: menuPedidos.ejecutar(); break;
                case 0: System.out.println("\n¡Saliendo del sistema de Food Store!"); break;
            }
        }
    }
}