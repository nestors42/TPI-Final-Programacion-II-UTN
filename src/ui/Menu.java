package ui;

import service.CategoriaService;
import service.PedidoService;
import service.ProductoService;
import service.UsuarioService;

public class Menu extends MenuBase {
    private CategoriaService categoriaService;
    private ProductoService productoService;
    private UsuarioService usuarioService;
    private PedidoService pedidoService;

    private MenuCategorias menuCategorias;
    private MenuProductos menuProductos;
    private MenuUsuarios menuUsuarios;
    private MenuPedidos menuPedidos;

    // CONSTRUCTOR DEFINITIVO: Recibe los 4 servicios en sintonía con tu Main
    public Menu(CategoriaService categoriaService, ProductoService productoService,
                UsuarioService usuarioService, PedidoService pedidoService) {
        super(); // Inicializa el Scanner heredado de MenuBase
        this.categoriaService = categoriaService;
        this.productoService = productoService;
        this.usuarioService = usuarioService;
        this.pedidoService = pedidoService;

        // Inicializamos todos los submenús pasándole sus dependencias
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

    public void arrancar() {
        int opcion = -1;
        while (opcion != 0) {
            mostrarOpciones();
            opcion = capturarOpcionNumericaSegura(0, 4);

            switch (opcion) {
                case 1: menuCategorias.ejecutarSubmenu(); break;
                case 2: menuProductos.ejecutarSubmenu(); break;
                case 3: menuUsuarios.ejecutarSubmenu(); break;
                case 4: menuPedidos.ejecutarSubmenu(); break; // 👈 Ruteo al submenú transaccional
                case 0: System.out.println("\n¡Saliendo del sistema de Food Store!"); break;
            }
        }
    }
}