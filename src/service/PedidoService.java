package service;

import config.DatabaseConfig;
import entities.Pedido;
import entities.Producto;
import entities.Usuario;
import enums.Estado;
import enums.FormaPago;
import exception.DatoInvalidoException;
import exception.EntidadNoEncontradaException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoService {
    private List<Pedido> pedidos;
    private Long ultimoId = 0L;

    private UsuarioService usuarioService;
    private ProductoService productoService;

    public PedidoService(UsuarioService usuarioService, ProductoService productoService) {
        this.pedidos = DatabaseConfig.getPedidosTable(); // Busca su lista en la caja fuerte
        this.usuarioService = usuarioService;
        this.productoService = productoService;
    }

    public Long generarSiguienteId() {
        return ++ultimoId;
    }

    // 🌟 HU-PED-01: Listar pedidos activos
    public List<Pedido> listarPedidosActivos() {
        List<Pedido> activos = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (!p.isEliminado()) {
                activos.add(p);
            }
        }
        return activos;
    }

    //Crear pedido transaccional
    public Pedido crearPedidoTransaccional(Usuario usuario, FormaPago formaPago, List<Producto> productosCarrito, List<Integer> cantidadesCarrito) throws DatoInvalidoException {

        if (usuario == null || usuario.isEliminado()) {
            throw new DatoInvalidoException("No se puede crear un pedido sin un usuario activo.");
        }
        if (productosCarrito.isEmpty()) {
            throw new DatoInvalidoException("El carrito de compras no puede estar vacío.");
        }

        // 1. Instanciamos el pedido temporalmente (Total inicial 0.0)
        Long nuevoId = generarSiguienteId();
        Pedido nuevoPedido = new Pedido(nuevoId, false, LocalTime.now(), LocalDate.now(),
                Estado.PENDIENTE, 0.0, formaPago, usuario);

        // RESPALDO DE STOCKS:
        List<Integer> stocksDeRespaldo = new ArrayList<>();
        for (Producto p : productosCarrito) {
            stocksDeRespaldo.add(p.getStock());
        }

        try {
            // 2. Procesamos línea por línea el carrito
            for (int i = 0; i < productosCarrito.size(); i++) {
                Producto prod = productosCarrito.get(i);
                int cantPedida = cantidadesCarrito.get(i);

                if (cantPedida <= 0) {
                    throw new DatoInvalidoException("La cantidad pedida para " + prod.getNombre() + " debe ser mayor a 0.");
                }

                // Validación estricta de stock
                if (prod.getStock() < cantPedida) {
                    throw new DatoInvalidoException("Falta de stock para '" + prod.getNombre() +
                            "'. Solicitado: " + cantPedida + " | Disponible: " + prod.getStock());
                }

                // Descontamos stock temporalmente
                prod.setStock(prod.getStock() - cantPedida);

                nuevoPedido.addDetallePedido(cantPedida, prod.getPrecio(), prod);
            }

            // guardamos el pedido
            pedidos.add(nuevoPedido);
            usuario.agregarPedido(nuevoPedido);
            return nuevoPedido;

        } catch (Exception e) {

            for (int i = 0; i < productosCarrito.size(); i++) {
                productosCarrito.get(i).setStock(stocksDeRespaldo.get(i));
            }
            // Propagamos el error para avisarle a la pantalla que la venta fue abortada
            throw new DatoInvalidoException("TRANSACCIÓN ABORTADA: El pedido se canceló debido a: " + e.getMessage());
        }
    }

    public Pedido buscarPorId(Long id) throws EntidadNoEncontradaException {
        for (Pedido p : pedidos) {
            if (p.getId().equals(id) && !p.isEliminado()) {
                return p;
            }
        }
        throw new EntidadNoEncontradaException("No se encontró ningún pedido activo con el ID: " + id);
    }


    public void actualizarPedido(Long id, Estado nuevoEstado, FormaPago nuevaForma) throws EntidadNoEncontradaException {
        Pedido p = buscarPorId(id);
        if (nuevoEstado != null) p.setEstado(nuevoEstado);
        if (nuevaForma != null) p.setFormaPago(nuevaForma);
    }


    public void eliminarPedido(Long id) throws EntidadNoEncontradaException {
        Pedido p = buscarPorId(id);
        p.setEliminado(true);
    }
}