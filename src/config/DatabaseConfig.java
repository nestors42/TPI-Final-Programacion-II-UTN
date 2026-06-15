package config;

import entities.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConfig {
    // listas estáticas del sistema
    private static final List<Categoria> categorias = new ArrayList<>();
    private static final List<Producto> productos = new ArrayList<>();
    private static final List<Usuario> usuarios = new ArrayList<>();
    private static final List<Pedido> pedidos = new ArrayList<>();

    public static List<Categoria> getCategoriasTable() { return categorias; }
    public static List<Producto> getProductosTable() { return productos; }
    public static List<Usuario> getUsuariosTable() { return usuarios; }
    public static List<Pedido> getPedidosTable() { return pedidos; }
}