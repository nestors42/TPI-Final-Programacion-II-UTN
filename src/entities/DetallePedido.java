package entities;

import java.time.LocalTime;

public class DetallePedido extends Base {

    private int cantidad;
    private Double subtotal;
    private Producto producto;
    private Pedido pedido;

    public DetallePedido(Long id, boolean eliminado, LocalTime createdat, int cantidad, Producto producto) {
        super(id, eliminado, createdat);
        this.cantidad = cantidad;
        this.producto = producto;
        this.subtotal = calcularSubtotal();
    }

    public Double calcularSubtotal() {
        return (this.producto != null) ? this.cantidad * this.producto.getPrecio() : 0.0;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.subtotal = calcularSubtotal();
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        this.subtotal = calcularSubtotal();
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public String toString() {
        String nombreProd = (producto != null) ? producto.getNombre() : "Sin Producto";

        return String.format("DetallePedido [Producto: %s, Cantidad: %d, Subtotal: $%.2f]",
                nombreProd, cantidad, subtotal);
    }
}