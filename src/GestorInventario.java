import conexion.ConexionManager;
import dao.ProductoDAO;
import dao.ProductoDAO.TipoBD;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import modelo.ProductoElectronico;

public class GestorInventario {

    // Componentes principales del sistema
    private static Scanner sc = new Scanner(System.in);  
    private static ProductoDAO dao;                      
    private static String bdActual = "H2";// Muestra qué BD estamos usando

    public static void main(String[] args) {
        // INICIO DEL PROGRAMA
        System.out.println("======== GESTOR DE INVENTARIO ELECTRÓNICO ========");

        // Configuración inicial
        ConexionManager.inicializarBD();  // Crea las tablas en ambas bases de datos
        dao = new ProductoDAO(TipoBD.H2); // Empezamos usando H2 por defecto

        // BUCLE PRINCIPAL - mantiene el programa ejecutándose
        boolean salir = false;
        while (!salir) {
            mostrarMenu();                    // Muestra las opciones al usuario
            int op = leerInt("Opción: ");     // Lee la opción elegida
            switch (op) {
                // CRUD - Operaciones básicas
                case 1 -> insertarProducto();     
                case 2 -> listarProductos();      
                case 3 -> buscarPorId();          
                case 4 -> actualizarProducto();   
                case 5 -> eliminarProducto();     
                
                // CONSULTAS ESPECIALES
                case 6 -> buscarPorNombre();      // Búsqueda por texto
                case 7 -> listarStockBajo();      // Filtro por stock bajo
                case 8 -> listarPorCategoria();   // Filtro por categoría
                case 9 -> mostrarEstadisticas();  // Informes y estadísticas
                
                // CONFIGURACIÓN
                case 10 -> cambiarBD();         
                case 0 -> salir = true;          
                default -> System.out.println("Opción inválida");
            }
            
            // Pausa para que el usuario vea los resultados
            if (!salir) {
                System.out.println("\nPresione ENTER para continuar...");
                sc.nextLine();
            }
        }
        
        // FIN DEL PROGRAMA
        System.out.println("\nSaliendo del sistema...");
    }

    // MENÚ PRINCIPAL - muestra todas las opciones disponibles
    private static void mostrarMenu() {
        System.out.println("\n--- MENÚ PRINCIPAL [" + bdActual + "] ---");
        System.out.println("1. Insertar producto");
        System.out.println("2. Listar productos");
        System.out.println("3. Buscar producto por ID");
        System.out.println("4. Actualizar producto");
        System.out.println("5. Eliminar producto");
        System.out.println("6. Buscar por nombre");
        System.out.println("7. Listar stock bajo");
        System.out.println("8. Listar por categoría");
        System.out.println("9. Ver estadísticas");
        System.out.println("10. Cambiar base de datos");
        System.out.println("0. Salir");
    }

    // LECTURA SEGURA DE NÚMEROS - evita que el programa se caiga con entrada inválida
    private static int leerInt(String msg) {
        System.out.print(msg);
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            return -1;  // Retorna -1 si hay error (opción inválida)
        }
    }

    // OPERACIÓN: INSERTAR NUEVO PRODUCTO
    private static void insertarProducto() {
        System.out.println("\n--- NUEVO PRODUCTO ---");
        try {
            // Recoger datos del usuario
            System.out.print("Nombre: ");
            String nombre = sc.nextLine();
            System.out.print("Categoría: ");
            String categoria = sc.nextLine();
            BigDecimal precio = new BigDecimal(leer("Precio: "));
            int stock = Integer.parseInt(leer("Stock: "));
            
            // Fecha opcional (hoy por defecto)
            String fechaStr = leer("Fecha ingreso (dd/MM/yyyy) [Enter para hoy]: ");
            LocalDate fecha = fechaStr.isEmpty()
                    ? LocalDate.now()  // Fecha actual si no se especifica
                    : LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            // Crear objeto y guardar en BD
            ProductoElectronico p = new ProductoElectronico(nombre, categoria, precio, stock, fecha);
            if (dao.insertar(p))
                System.out.println("Producto insertado correctamente");
            else
                System.out.println("No se pudo insertar el producto");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // OPERACIÓN: LISTAR TODOS LOS PRODUCTOS
    private static void listarProductos() {
        System.out.println("\n--- LISTA DE PRODUCTOS ---");
        List<ProductoElectronico> lista = dao.listarTodos();
        if (lista.isEmpty()) 
            System.out.println("No hay productos registrados.");
        else 
            lista.forEach(System.out::println);  // Mostrar cada producto
    }

    // OPERACIÓN: BUSCAR PRODUCTO POR ID
    private static void buscarPorId() {
        int id = leerInt("Ingrese ID: ");
        ProductoElectronico p = dao.obtenerPorId(id);
        if (p != null) 
            System.out.println(p);  // Mostrar producto encontrado
        else 
            System.out.println("No se encontró el producto con ID " + id);
    }

    // OPERACIÓN: ACTUALIZAR PRODUCTO EXISTENTE
    private static void actualizarProducto() {
        int id = leerInt("ID a actualizar: ");
        ProductoElectronico p = dao.obtenerPorId(id);
        
        // Verificar que el producto existe
        if (p == null) {
            System.out.println("No existe ese producto.");
            return;
        }
        
        // Mostrar datos actuales
        System.out.println("Producto actual: " + p);
        
        // Pedir nuevos datos (Enter para mantener valores actuales)
        System.out.print("Nuevo nombre (Enter para mantener): ");
        String nombre = sc.nextLine();
        if (!nombre.isEmpty()) p.setNombre(nombre);

        System.out.print("Nueva categoría (Enter para mantener): ");
        String cat = sc.nextLine();
        if (!cat.isEmpty()) p.setCategoria(cat);

        String pr = leer("Nuevo precio (Enter para mantener): ");
        if (!pr.isEmpty()) p.setPrecio(new BigDecimal(pr));

        String st = leer("Nuevo stock (Enter para mantener): ");
        if (!st.isEmpty()) p.setStock(Integer.parseInt(st));

        // Guardar cambios
        if (dao.actualizar(p)) 
            System.out.println("Producto actualizado");
        else 
            System.out.println("Error al actualizar");
    }

    // OPERACIÓN: ELIMINAR PRODUCTO CON CONFIRMACIÓN
    private static void eliminarProducto() {
        int id = leerInt("ID del producto a eliminar: ");
        ProductoElectronico p = dao.obtenerPorId(id);
        
        if (p == null) {
            System.out.println("No encontrado.");
            return;
        }
        
        // Mostrar producto y pedir confirmación
        System.out.println("Producto: " + p);
        System.out.print("¿Eliminar? (S/N): ");
        
        if (sc.nextLine().equalsIgnoreCase("S")) {
            if (dao.eliminar(id)) 
                System.out.println("Producto eliminado");
            else 
                System.out.println("No se pudo eliminar");
        } else {
            System.out.println("Operación cancelada");
        }
    }

    // CONSULTA: BÚSQUEDA POR NOMBRE (búsqueda parcial)
    private static void buscarPorNombre() {
        String nom = leer("Buscar nombre: ");
        List<ProductoElectronico> lista = dao.buscarPorNombre(nom);
        
        if (lista.isEmpty()) 
            System.out.println("No se encontraron resultados");
        else 
            lista.forEach(System.out::println);
    }

    // CONSULTA: FILTRAR PRODUCTOS CON STOCK BAJO
    private static void listarStockBajo() {
        int min = leerInt("Stock mínimo: ");
        List<ProductoElectronico> lista = dao.listarStockBajo(min);
        
        if (lista.isEmpty()) 
            System.out.println("No hay productos con stock bajo");
        else 
            lista.forEach(System.out::println);
    }

    // CONSULTA: FILTRAR POR CATEGORÍA
    private static void listarPorCategoria() {
        String cat = leer("Categoría: ");
        List<ProductoElectronico> lista = dao.listarPorCategoria(cat);
        
        if (lista.isEmpty()) 
            System.out.println("No hay productos en esa categoría");
        else 
            lista.forEach(System.out::println);
    }

    // INFORMES: ESTADÍSTICAS DEL INVENTARIO
    private static void mostrarEstadisticas() {
        List<ProductoElectronico> lista = dao.listarTodos();
        
        if (lista.isEmpty()) {
            System.out.println("Inventario vacío.");
            return;
        }
        
        // Cálculos estadísticos
        int total = dao.contarProductos();  // Total de productos
        int stockTotal = lista.stream().mapToInt(ProductoElectronico::getStock).sum();  // Suma de stock
        
        // Valor total del inventario (precio * stock de cada producto)
        BigDecimal valorTotal = lista.stream()
                .map(p -> p.getPrecio().multiply(new BigDecimal(p.getStock())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Mostrar resultados
        System.out.println("Total productos: " + total);
        System.out.println("Stock total: " + stockTotal);
        System.out.println("Valor total: $" + valorTotal);
    }

    // CONFIGURACIÓN: CAMBIAR ENTRE BASES DE DATOS
    private static void cambiarBD() {
        System.out.println("\n1. H2 (Embebida)");
        System.out.println("2. Oracle (No embebida)");
        int op = leerInt("Seleccione: ");
        
        if (op == 1) {
            dao.cambiarTipoBD(TipoBD.H2);
            bdActual = "H2";
            System.out.println("Cambiado a H2");
        } else if (op == 2) {
            dao.cambiarTipoBD(TipoBD.ORACLE);
            bdActual = "Oracle";
            System.out.println("Cambiado a Oracle");
        } else {
            System.out.println("Opción inválida");
        }
    }

    // LEER TEXTO DEL USUARIO
    private static String leer(String msg) {
        System.out.print(msg);
        return sc.nextLine();
    }
}