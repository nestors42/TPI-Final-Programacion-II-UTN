import service.CategoriaService;
import service.PedidoService;
import service.ProductoService;
import service.UsuarioService;
import ui.Menu;

public class Main {
    // Inicializamos las 4 "Bases de Datos Virtuales" en memoria (Colecciones)
    private static CategoriaService categoriaService = new CategoriaService();
    private static ProductoService productoService = new ProductoService();
    private static UsuarioService usuarioService = new UsuarioService();
    private static PedidoService pedidoService = new PedidoService();

    public static void main(String[] args) {

        Menu sistemaMenu = new Menu(categoriaService, productoService, usuarioService, pedidoService);

        // Arranca el flujo de la aplicación
        sistemaMenu.ejecutar();
    }
}