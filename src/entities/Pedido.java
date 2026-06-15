package entities;

import enums.Estado;
import enums.FormaPago;
import interfaces.Calculable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido extends Base implements Calculable {

    private LocalDate fecha;
    private Estado estado;
    private Double total;
    private FormaPago formaPago;
    private Usuario usuario;
    private List<DetallePedido> detalles;

    // constructor
    public Pedido(Long id, boolean eliminado, LocalTime createdAt, LocalDate fecha, Estado estado, Double total, FormaPago formaPago, Usuario usuario) {
        super(id, eliminado, createdAt);
        this.fecha = fecha;
        this.estado = estado;
        this.total = total;
        this.formaPago = formaPago;
        this.usuario = usuario;
        this.detalles = new ArrayList<>();
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }


    @Override
    public void calcularTotal() {
        Double suma = 0.0;
        for (DetallePedido dp : detalles) {
            if (dp.getSubtotal() != null) {
                suma += dp.getSubtotal();
            }
        }
        this.total = suma;
    }



    // Ajustamos la firma metiendo el Double precio según lo que pide en el TPI
    public void addDetallePedido(int cantidad, Double precio, Producto producto) {
        Long nuevoId = (long) (this.detalles.size() + 1);

        // Al instanciar el detalle, le pasamos el precio histórico de venta
        DetallePedido nuevoDetalle = new DetallePedido(nuevoId, false, LocalTime.now(), cantidad, producto);

        // Si tu constructor de DetallePedido calculaba con producto.getPrecio(),

        nuevoDetalle.setSubtotal(cantidad * precio);
        nuevoDetalle.setPedido(this);

        this.detalles.add(nuevoDetalle);
        calcularTotal(); //Invocación vía la interfaz Calculable requerida en la HU-PED-02
    }


    public DetallePedido findDetallePedidoByProducto(Producto producto) {
        if (producto != null) {
            for (DetallePedido dp : this.detalles) {
                if (dp.getProducto() != null && dp.getProducto().getId().equals(producto.getId())) {
                    return dp;
                }
            }
        }
        return null;
    }



    public void deleteDetallePedidoByProducto(Producto producto) {
        DetallePedido encontrado = findDetallePedidoByProducto(producto);
        if (encontrado != null) {
            this.detalles.remove(encontrado);
            calcularTotal();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("> Pedido #%d | Fecha: %s | Estado: %s | FormaPago: %s\n",
                getId(), fecha, estado, formaPago));
        sb.append("  ---------------------------------------------------------------------------\n");

        for (DetallePedido dp : detalles) {
            sb.append(String.format("  - DetallePedido #%d: %s x %d => Subtotal: $%.2f\n",
                    dp.getId(), dp.getProducto().getNombre(), dp.getCantidad(), dp.getSubtotal()));
        }

        sb.append(String.format("  TOTAL DEL PEDIDO: $%.2f\n", this.total));
        sb.append("  ---------------------------------------------------------------------------");
        return sb.toString();
    }
}