package dao;

import conexion.ConexionManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.ProductoElectronico;

/**
 * Implementación unificada del DAO para H2 y Oracle
 * Gestiona ambas bases de datos usando un tipo de conexión configurable
 */
public class ProductoDAO implements ProductoService {
    
    private TipoBD tipoBD;
    
    public enum TipoBD {
        H2, ORACLE
    }
    
    public ProductoDAO(TipoBD tipo) {
        this.tipoBD = tipo;
    }
    
    /**
     * Obtiene la conexión según el tipo de BD configurado
     */
    private Connection obtenerConexion() throws SQLException {
        return tipoBD == TipoBD.H2 
            ? ConexionManager.getConexionH2() 
            : ConexionManager.getConexionOracle();
    }
    
    /**
     * Genera el SQL de inserción según la BD
     * H2 usa AUTO_INCREMENT, Oracle usa SEQUENCE
     */
    private String getSqlInsertar() {
        if (tipoBD == TipoBD.H2) {
            return "INSERT INTO productos_electronicos " +
                "(nombre, categoria, precio, stock, fecha_ingreso) " +
                "VALUES (?, ?, ?, ?, ?)";
        } else {
            return "INSERT INTO productos_electronicos " +
                "(id, nombre, categoria, precio, stock, fecha_ingreso) " +
                "VALUES (productos_seq.NEXTVAL, ?, ?, ?, ?, ?)";
        }
    }

    @Override
    public boolean insertar(ProductoElectronico producto) {
        String sql = getSqlInsertar();
        
        try (Connection conn = obtenerConexion();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getCategoria());
            pstmt.setBigDecimal(3, producto.getPrecio());
            pstmt.setInt(4, producto.getStock());
            pstmt.setDate(5, Date.valueOf(producto.getFechaIngreso()));
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<ProductoElectronico> listarTodos() {
        List<ProductoElectronico> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos_electronicos ORDER BY id";
        
        try (Connection conn = obtenerConexion();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar: " + e.getMessage());
        }
        
        return productos;
    }

    @Override
    public ProductoElectronico obtenerPorId(int id) {
        String sql = "SELECT * FROM productos_electronicos WHERE id = ?";
        
        try (Connection conn = obtenerConexion();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearProducto(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar por ID: " + e.getMessage());
        }
        
        return null;
    }

    @Override
    public boolean actualizar(ProductoElectronico producto) {
        String sql = "UPDATE productos_electronicos SET " +
                    "nombre = ?, categoria = ?, precio = ?, " +
                    "stock = ?, fecha_ingreso = ? " +
                    "WHERE id = ?";
        
        try (Connection conn = obtenerConexion();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getCategoria());
            pstmt.setBigDecimal(3, producto.getPrecio());
            pstmt.setInt(4, producto.getStock());
            pstmt.setDate(5, Date.valueOf(producto.getFechaIngreso()));
            pstmt.setInt(6, producto.getId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM productos_electronicos WHERE id = ?";
        
        try (Connection conn = obtenerConexion();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<ProductoElectronico> buscarPorNombre(String nombre) {
        List<ProductoElectronico> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos_electronicos " +
                    "WHERE LOWER(nombre) LIKE LOWER(?) ORDER BY nombre";
        
        try (Connection conn = obtenerConexion();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + nombre + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    productos.add(mapearProducto(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error en búsqueda: " + e.getMessage());
        }
        
        return productos;
    }

    @Override
    public List<ProductoElectronico> listarStockBajo(int stockMinimo) {
        List<ProductoElectronico> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos_electronicos " +
                    "WHERE stock < ? ORDER BY stock";
        
        try (Connection conn = obtenerConexion();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, stockMinimo);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    productos.add(mapearProducto(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al consultar stock: " + e.getMessage());
        }
        
        return productos;
    }

    @Override
    public int contarProductos() {
        String sql = "SELECT COUNT(*) FROM productos_electronicos";
        
        try (Connection conn = obtenerConexion();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al contar: " + e.getMessage());
        }
        
        return 0;
    }

    @Override
    public List<ProductoElectronico> listarPorCategoria(String categoria) {
        List<ProductoElectronico> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos_electronicos " +
                    "WHERE LOWER(categoria) = LOWER(?) ORDER BY nombre";
        
        try (Connection conn = obtenerConexion();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, categoria);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    productos.add(mapearProducto(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al filtrar categoría: " + e.getMessage());
        }
        
        return productos;
    }
    
    /**
     * Cambia el tipo de base de datos en tiempo de ejecución
     */
    public void cambiarTipoBD(TipoBD nuevoTipo) {
        this.tipoBD = nuevoTipo;
    }
    
    /**
     * Obtiene el tipo de BD actual
     */
    public TipoBD getTipoBD() {
        return this.tipoBD;
    }

    /**
     * Mapea un ResultSet a un objeto ProductoElectronico
     */
    private ProductoElectronico mapearProducto(ResultSet rs) throws SQLException {
        return new ProductoElectronico(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("categoria"),
            rs.getBigDecimal("precio"),
            rs.getInt("stock"),
            rs.getDate("fecha_ingreso").toLocalDate()
        );
    }
}