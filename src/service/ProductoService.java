package service;

import config.DatabaseConfig;
import entities.Categoria;
import entities.Producto;
import exception.DatoInvalidoException;
import exception.EntidadNoEncontradaException;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ProductoService {
    private List<Producto> productos;
    private Long ultimoId = 0L;

    public ProductoService() {
        this.productos = DatabaseConfig.getProductosTable();
    }

    public Long generarSiguienteId() {
        return ++ultimoId;
    }

    // Listar solo productos activos (no eliminados)
    public List<Producto> listarProductosActivos() {
        List<Producto> activos = new ArrayList<>();
        for (Producto p : productos) {
            if (!p.isEliminado()) {
                activos.add(p);
            }
        }
        return activos;
    }

    //Crear producto validando reglas de negocio y categoría existente
    public Producto crearProducto(String nombre, Double precio, String descripcion, int stock, String imagen, Boolean disponible, Categoria categoriaAsociada)
            throws DatoInvalidoException {


        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatoInvalidoException("El nombre del producto no puede estar vacío.");
        }
        if (precio == null || precio < 0) {
            throw new DatoInvalidoException("El precio del producto no puede ser menor a 0.");
        }
        if (stock < 0) {
            throw new DatoInvalidoException("El stock inicial del producto no puede ser negativo.");
        }
        if (categoriaAsociada == null || categoriaAsociada.isEliminado()) {
            throw new DatoInvalidoException("El producto debe estar asociado a una categoría válida y activa.");
        }

        Long nuevoId = generarSiguienteId();
        Producto nuevo = new Producto(nuevoId, false, LocalTime.now(), nombre.trim(), precio,
                descripcion, stock, imagen, disponible, categoriaAsociada);

        productos.add(nuevo);

        // hacemos relación bidireccional en memoria
        categoriaAsociada.agregarProducto(nuevo);

        return nuevo;
    }

    // Método para buscar producto activo por ID
    public Producto buscarPorId(Long id) throws EntidadNoEncontradaException {
        for (Producto p : productos) {
            if (p.getId().equals(id) && !p.isEliminado()) {
                return p;
            }
        }
        throw new EntidadNoEncontradaException("No se encontró ningún producto activo con el ID: " + id);
    }

    //Editar producto
    public void editarProducto(Long id, String nuevoNombre, Double nuevoPrecio, String nuevaDescripcion, Integer nuevoStock, String nuevaImagen, Boolean nuevoDisponible, Categoria nuevaCategoria) throws EntidadNoEncontradaException, DatoInvalidoException {

        Producto producto = buscarPorId(id); // Lanza la excepción si no existe

        if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
            producto.setNombre(nuevoNombre.trim());
        }
        if (nuevoPrecio != null) {
            if (nuevoPrecio < 0) throw new DatoInvalidoException("El precio no puede ser menor a 0.");
            producto.setPrecio(nuevoPrecio);
        }
        if (nuevaDescripcion != null) {
            producto.setDescripcion(nuevaDescripcion);
        }
        if (nuevoStock != null) {
            if (nuevoStock < 0) throw new DatoInvalidoException("El stock no puede ser negativo.");
            producto.setStock(nuevoStock);
        }
        if (nuevaImagen != null) {
            producto.setImagen(nuevaImagen);
        }
        if (nuevoDisponible != null) {
            producto.setDisponible(nuevoDisponible);
        }
        if (nuevaCategoria != null && !nuevaCategoria.isEliminado()) {
            // Removemos de la categoría anterior y reasociamos de forma bidireccional
            if (producto.getCategoria() != null) {
                producto.getCategoria().getProductos().remove(producto);
            }
            producto.setCategoria(nuevaCategoria);
            nuevaCategoria.agregarProducto(producto);
        }
    }

    // Eliminar producto (sin destruir referencias históricas)
    public void eliminarProducto(Long id) throws EntidadNoEncontradaException {
        Producto producto = buscarPorId(id);
        producto.setEliminado(true); // Soft Delete
    }

    //listamos por categoria
    public List<Producto> listarProductosPorCategoria(Long categoriaId) {
        List<Producto> filtrados = new ArrayList<>();
        for (Producto p : productos) {
            if (!p.isEliminado() && p.getCategoria() != null && p.getCategoria().getId().equals(categoriaId)) {
                filtrados.add(p);
            }
        }
        return filtrados;
    }
}