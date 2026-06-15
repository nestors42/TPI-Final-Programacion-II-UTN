package entities;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Categoria extends Base{

    private String nombre;
    private String descripcion;
    private List<Producto> productos;


    // constructor
    public Categoria(Long id, boolean eliminado, LocalTime createdat, String nombre, String descripcion) {
        super(id, eliminado, createdat);
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.productos = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public void agregarProducto(Producto producto) {
        if (producto != null) {
            this.productos.add(producto);

            if (producto.getCategoria() != this) {
                producto.setCategoria(this);
            }
        }
    }

    @Override
    public String toString() {
        return String.format("Categoria [ID: %d, Nombre: %s, Descripcion: %s, Cant. Productos: %d]",
                getId(), nombre, descripcion, productos.size());
    }
}
