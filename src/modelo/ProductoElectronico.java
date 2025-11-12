package modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Clase que representa un producto electrónico en el inventario
 */
public class ProductoElectronico {
    private int id;
    private String nombre;
    private String categoria;
    private BigDecimal precio;
    private int stock;
    private LocalDate fechaIngreso;

    // Constructor vacío
    public ProductoElectronico() {
    }

    // Constructor completo
    public ProductoElectronico(int id, String nombre, String categoria, 
                            BigDecimal precio, int stock, LocalDate fechaIngreso) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
        this.fechaIngreso = fechaIngreso;
    }

    // Constructor sin ID (para insertar)
    public ProductoElectronico(String nombre, String categoria, 
                            BigDecimal precio, int stock, LocalDate fechaIngreso) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
        this.fechaIngreso = fechaIngreso;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Nombre: %s | Categoría: %s | Precio: $%.2f | Stock: %d | Fecha: %s",
                id, nombre, categoria, precio, stock, fechaIngreso);
    }
}