package ui;

import interfaces.MenuPantalla;
import service.CategoriaService;
import service.PedidoService;
import service.ProductoService;
import service.UsuarioService;

// Hereda de MenuBase, ganando el método ejecutar() único y protegido
public class Menu extends MenuBase {
    private CategoriaService categoriaService;
    private ProductoService productoService;
    private UsuarioService usuarioService;
    private PedidoService pedidoService;

    private MenuPantalla menuCategorias;
    private MenuPantalla menuProductos;
    private MenuPantalla menuUsuarios;
    private MenuPantalla menuPedidos;

    public Menu(CategoriaService categoriaService, ProductoService productoService,
                UsuarioService usuarioService, PedidoService pedidoService) {
        super(); // Inicializa el Scanner del padre
        this.categoriaService = categoriaService;
        this.productoService = productoService;
        this.usuarioService = usuarioService;
        this.pedidoService = pedidoService;

        this.menuCategorias = new MenuCategorias(categoriaService);
        this.menuProductos = new MenuProductos(productoService, categoriaService);
        this.menuUsuarios = new MenuUsuarios(usuarioService);
        this.menuPedidos = new MenuPedidos(pedidoService, usuarioService, productoService);
    }

    // El padre llamará a este método para imprimir la pantalla principal
    @Override
    public void mostrarOpciones() {
        System.out.println("\n=== SISTEMA DE PEDIDOS (FOOD STORE) ===");
        System.out.println("1. Gestión de Categorías");
        System.out.println("2. Gestión de Productos");
        System.out.println("3. Gestión de Usuarios");
        System.out.println("4. Gestión de Pedidos");
        System.out.println("0. Salir");
    }


    @Override
    protected void evaluarOpcion(int opcion) {
        switch (opcion) {
            case 1: menuCategorias.ejecutar(); break;
            case 2: menuProductos.ejecutar(); break;
            case 3: menuUsuarios.ejecutar(); break;
            case 4: menuPedidos.ejecutar(); break;
        }
    }

    @Override
    protected int getOpcionMaxima() {
        return 4; // El menú principal va del 0 al 4
    }
}