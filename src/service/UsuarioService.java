package service;

import config.DatabaseConfig;
import entities.Usuario;
import enums.Rol;
import exception.DatoInvalidoException;
import exception.EntidadNoEncontradaException;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class UsuarioService {
    private List<Usuario> usuarios;
    private Long ultimoId = 0L;

    public UsuarioService() {
        this.usuarios = DatabaseConfig.getUsuariosTable();
    }

    public Long generarSiguienteId() {
        return ++ultimoId;
    }

    // Listar solo usuarios activos (no eliminados)
    public List<Usuario> listarUsuariosActivos() {
        List<Usuario> activos = new ArrayList<>();
        for (Usuario u : usuarios) {
            if (!u.isEliminado()) {
                activos.add(u);
            }
        }
        return activos;
    }

    // Crear usuario validando unicidad de e-mail
    public Usuario crearUsuario(String nombre, String apellido, String mail, String celular,
                                String contrasenia, Rol rol) throws DatoInvalidoException {

        if (nombre == null || nombre.trim().isEmpty() || apellido == null || apellido.trim().isEmpty()) {
            throw new DatoInvalidoException("El nombre y el apellido son obligatorios.");
        }
        if (mail == null || mail.trim().isEmpty()) {
            throw new DatoInvalidoException("El e-mail no puede estar vacío.");
        }

        //Validar que el e-mail sea único
        for (Usuario u : usuarios) {
            if (!u.isEliminado() && u.getMail().equalsIgnoreCase(mail.trim())) {
                throw new DatoInvalidoException("El e-mail '" + mail + "' ya está registrado en el sistema.");
            }
        }

        Long nuevoId = generarSiguienteId();
        Usuario nuevo = new Usuario(nuevoId, false, LocalTime.now(), nombre.trim(), apellido.trim(),
                mail.trim(), celular, contrasenia, rol);

        usuarios.add(nuevo);
        return nuevo;
    }

    // Método para buscar usuario activo por ID
    public Usuario buscarPorId(Long id) throws EntidadNoEncontradaException {
        for (Usuario u : usuarios) {
            if (u.getId().equals(id) && !u.isEliminado()) {
                return u;
            }
        }
        throw new EntidadNoEncontradaException("No se encontró ningún usuario activo con el ID: " + id);
    }

    // Editar usuario validando unicidad si se modifica el mail
    public void editarUsuario(Long id, String nuevoNombre, String nuevoApellido, String nuevoMail, String nuevoCelular, String nuevaContrasenia, Rol nuevoRol)
            throws EntidadNoEncontradaException, DatoInvalidoException {

        Usuario usuario = buscarPorId(id); // Lanza la excepción si no existe

        if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
            usuario.setNombre(nuevoNombre.trim());
        }
        if (nuevoApellido != null && !nuevoApellido.trim().isEmpty()) {
            usuario.setApellido(nuevoApellido.trim());
        }
        if (nuevoMail != null && !nuevoMail.trim().isEmpty() && !usuario.getMail().equalsIgnoreCase(nuevoMail.trim())) {
            // Si el e-mail cambió, volvemos a verificar la unicidad contra el resto de la lista
            for (Usuario u : usuarios) {
                if (!u.getId().equals(id) && !u.isEliminado() && u.getMail().equalsIgnoreCase(nuevoMail.trim())) {
                    throw new DatoInvalidoException("No se pudo editar. El e-mail '" + nuevoMail + "' ya lo usa otro usuario.");
                }
            }
            usuario.setMail(nuevoMail.trim());
        }
        if (nuevoCelular != null) {
            usuario.setCelular(nuevoCelular);
        }
        if (nuevaContrasenia != null && !nuevaContrasenia.isEmpty()) {
            usuario.setContrasenia(nuevaContrasenia);
        }
        if (nuevoRol != null) {
            usuario.setRol(nuevoRol);
        }
    }

    // Eliminar usuario (mantener el historial de pedidos)
    public void eliminarUsuario(Long id) throws EntidadNoEncontradaException {
        Usuario usuario = buscarPorId(id);
        usuario.setEliminado(true); //Soft Delete
    }
}