package dao;

import java.sql.SQLException;
import java.util.List;
import modelo.ProductoElectronico;

/**
 * Interfaz que define las operaciones CRUD para productos electrónicos
 */
public interface ProductoService {
    
    /**
     * CREATE - Inserta un nuevo producto
     */
    boolean insertar(ProductoElectronico producto) throws SQLException;
    
    /**
     * READ - Obtiene todos los productos
     */
    List<ProductoElectronico> listarTodos() throws SQLException;
    
    /**
     * READ - Obtiene un producto por ID
     */
    ProductoElectronico obtenerPorId(int id) throws SQLException;
    
    /**
     * UPDATE - Actualiza un producto existente
     */
    boolean actualizar(ProductoElectronico producto) throws SQLException;
    
    /**
     * DELETE - Elimina un producto por ID
     */
    boolean eliminar(int id) throws SQLException;
    
    /**
     * BÚSQUEDA - Busca productos por nombre
     */
    List<ProductoElectronico> buscarPorNombre(String nombre) throws SQLException;
    
    /**
     * FILTRO - Lista productos con stock menor al especificado
     */
    List<ProductoElectronico> listarStockBajo(int stockMinimo) throws SQLException;
    
    /**
     * ESTADÍSTICA - Cuenta total de productos
     */
    int contarProductos() throws SQLException;
    
    /**
     * FILTRO - Lista productos por categoría
     */
    List<ProductoElectronico> listarPorCategoria(String categoria) throws SQLException;
}