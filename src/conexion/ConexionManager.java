package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase para gestionar las conexiones a ambas bases de datos
 */
public class ConexionManager {
    
    // Configuración H2 (Embebida)
    private static final String H2_URL = "jdbc:h2:~/inventario_h2;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE";
    private static final String H2_USER = "Alejandro";
    private static final String H2_PASSWORD = "";
    
    // Configuración Oracle (No embebida)
    // Ajusta estos valores según tu instalación de Oracle
    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
    private static final String ORACLE_USER = "System";
    private static final String ORACLE_PASSWORD = "1234";
    
    /**
     * Obtiene conexión a H2
     */
    public static Connection getConexionH2() throws SQLException {
        try {
            Class.forName("org.h2.Driver");
            return DriverManager.getConnection(H2_URL, H2_USER, H2_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver H2 no encontrado", e);
        }
    }
    
    /**
     * Obtiene conexión a Oracle
     */
    public static Connection getConexionOracle() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            return DriverManager.getConnection(ORACLE_URL, ORACLE_USER, ORACLE_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver Oracle no encontrado", e);
        }
    }
    
    /**
     * Crea la tabla en H2 si no existe
     */
    public static void crearTablaH2() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS productos_electronicos (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nombre VARCHAR(100) NOT NULL, " +
                    "categoria VARCHAR(50) NOT NULL, " +
                    "precio DECIMAL(10,2) NOT NULL, " +
                    "stock INT NOT NULL, " +
                    "fecha_ingreso DATE NOT NULL)";
        
        try (Connection conn = getConexionH2();
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla creada/verificada en H2");
        }
    }
    
    /**
     * Crea la tabla en Oracle si no existe
     */
    public static void crearTablaOracle() throws SQLException {
        // Primero verificar si existe
        String checkTable = "SELECT COUNT(*) FROM user_tables WHERE table_name = 'PRODUCTOS_ELECTRONICOS'";
        String dropSeq = "DROP SEQUENCE productos_seq";
        String createSeq = "CREATE SEQUENCE productos_seq START WITH 1 INCREMENT BY 1";
        String createTable = "CREATE TABLE productos_electronicos (" +
                            "id NUMBER PRIMARY KEY, " +
                            "nombre VARCHAR2(100) NOT NULL, " +
                            "categoria VARCHAR2(50) NOT NULL, " +
                            "precio NUMBER(10,2) NOT NULL, " +
                            "stock NUMBER NOT NULL, " +
                            "fecha_ingreso DATE NOT NULL)";
        
        try (Connection conn = getConexionOracle();
            Statement stmt = conn.createStatement()) {
            
            try {
                stmt.execute("DROP TABLE productos_electronicos");
                System.out.println("Tabla anterior eliminada en Oracle");
            } catch (SQLException e) {
                // Tabla no existe, continuar
            }
            
            try {
                stmt.execute(dropSeq);
            } catch (SQLException e) {
                // Secuencia no existe, continuar
            }
            
            stmt.execute(createTable);
            stmt.execute(createSeq);
            System.out.println("Tabla y secuencia creadas en Oracle");
        }
    }
    
    /**
     * Inicializa ambas bases de datos
     */
    public static void inicializarBD() {
        try {
            crearTablaH2();
            crearTablaOracle();
            System.out.println("\n=== Bases de datos inicializadas correctamente ===\n");
        } catch (SQLException e) {
            System.err.println("Error al inicializar bases de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Cierra una conexión de forma segura
     */
    public static void cerrarConexion(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
}