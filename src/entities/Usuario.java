package entities;

import enums.Rol;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Usuario extends Base{

    private String nombre;
    private String apellido;
    private String mail;
    private String celular;
    private String contrasenia;
    private Rol rol;
    private List<Pedido> pedidos;

    // constructor
    public Usuario(Long id, boolean eliminado, LocalTime createdat, String nombre, String apellido, String mail, String celular, String contrasenia, Rol rol) {
        super(id, eliminado, createdat);
        this.nombre = nombre;
        this.apellido = apellido;
        this.mail = mail;
        this.celular = celular;
        this.contrasenia = contrasenia;
        this.rol = rol;
        this.pedidos = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public void agregarPedido(Pedido pedido) {
        if (pedido != null) {
            this.pedidos.add(pedido);
            if (pedido.getUsuario() != this) {
                pedido.setUsuario(this);
            }
        }
    }

    public void mostrarInfo() {
        System.out.println("===============================================================================");
        System.out.printf("USUARIO: %s %s | Mail: %s | Rol: [%s]\n", this.nombre, this.apellido, this.mail, this.rol);
        System.out.println("===============================================================================");

        Double totalAcumuladoUsuario = 0.0;

        for (Pedido p : this.pedidos) {
            System.out.println(p.toString());
            System.out.println();
            if (p.getTotal() != null) {
                totalAcumuladoUsuario += p.getTotal();
            }
        }

        System.out.printf("TOTAL ACUMULADO del usuario: $%.2f\n", totalAcumuladoUsuario);
        System.out.println("===============================================================================\n");
    }

    @Override
    public String toString() {
        return String.format("Usuario [ID: %d, Nombre: %s %s, Mail: %s, Rol: %s, Cant. Pedidos: %d]",
                getId(), nombre, apellido, mail, rol, pedidos.size());
    }


}
