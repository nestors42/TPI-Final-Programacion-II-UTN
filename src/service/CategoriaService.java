package service;

import config.DatabaseConfig;
import entities.Categoria;
import exception.DatoInvalidoException;
import exception.EntidadNoEncontradaException;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CategoriaService {

    private List<Categoria> categorias;
    private Long ultimoId = 0L;

    public CategoriaService() {
        this.categorias = DatabaseConfig.getCategoriasTable();
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public Long generarSiguienteId(){
        return ++ultimoId;
    }

    // listamos categorias activas
    public List<Categoria> listarCategoriasActivas(){
        List<Categoria> activas = new ArrayList<>();
        for (Categoria c : categorias){
            if (!c.isEliminado()){
                activas.add(c);
            }
        }
        return activas;
    }


    // creamos categoria con validacion de nombre unico
    public Categoria crearCategoria(String nombre, String descripcion) throws DatoInvalidoException{
        if (nombre == null || nombre.trim().isEmpty()){
            throw new DatoInvalidoException("El nombre de la categoria  no puede estar vacio.");
        }

        // recorremos la colleccion y verificamos si ya existe
        for (Categoria c : categorias){
            if (!c.isEliminado() && c.getNombre().equalsIgnoreCase(nombre.trim())){
                throw new DatoInvalidoException("Ya existe una categoria activa con el nombre " + nombre);
            }
        }

        //siguiente id y nueva categoria usando el constructor heredado de base
        Long nuevoId = generarSiguienteId();
        Categoria nueva = new Categoria(nuevoId, false, LocalTime.now(),nombre.trim(), descripcion);
        categorias.add(nueva);
        return nueva;
    }

    // metodo para buscar entidad por id
    public Categoria buscarPorId(Long id) throws EntidadNoEncontradaException{
        for (Categoria c : categorias){
            if (c.getId().equals(id) && !c.isEliminado()){
                return c;
            }
        }
        throw new EntidadNoEncontradaException("No se encontró ninguna categoria con el ID: " + id);
    }

    // metodo para editar categorias que ya existen
    public void editarCategoria(Long id, String nuevoNombre, String nuevaDescripcion)
        throws EntidadNoEncontradaException, DatoInvalidoException {
        // si no existe lanza la excepcion de manera automatica
        Categoria categoria = buscarPorId(id);

        if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()){

            // aca evaluamos que el nuevo nombre no sea igual a otra
            for (Categoria c : categorias){
                if (!c.getId().equals(id) && !c.isEliminado() && c.getNombre().equalsIgnoreCase(nuevoNombre.trim())){
                    throw new DatoInvalidoException("No se puede editar. El nombre: " + nuevoNombre + " ya existe en otra categoria.");
                }
            }
            categoria.setNombre(nuevoNombre.trim());
        }
        if (nuevaDescripcion != null && !nuevaDescripcion.trim().isEmpty()){
            categoria.setDescripcion(nuevaDescripcion);
        }

    }

    // metodo para eliminar categoria
    public void eliminarCategoria(Long id) throws EntidadNoEncontradaException{
        Categoria categoria = buscarPorId(id);
        // cambiamos el estado en lugar de remover del Array
        categoria.setEliminado(true);
    }


}
