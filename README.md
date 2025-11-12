# Gestor de Inventario Electrónico con JDBC

Sistema completo de gestión de inventario utilizando Java, JDBC, H2 (embebida) y Oracle (no embebida) con procedimientos almacenados.

##  Contenido del Proyecto

### Estructura de Paquetes
```
src/
├── modelo/
│   └── ProductoElectronico.java      # Clase modelo
├── conexion/
│   └── ConexionManager.java          # Gestor de conexiones
├── dao/
│   ├── ProductoDAO.java              # Interfaz DAO
│   ├── ProductoDAOH2.java            # Implementación H2
│   ├── ProductoDAOOracle.java        # Implementación Oracle básica
│   └── ProductoDAOOracleProcedures.java  # Oracle con procedimientos
└── GestorInventario.java             # Aplicación principal
```

##  Requisitos Previos

### Software Necesario
1. **JDK 11 o superior**
2. **Oracle Database** (Express Edition o superior)
3. **Maven** (opcional, para gestión de dependencias)
4. **IDE** (Eclipse, IntelliJ IDEA, NetBeans, etc.)

### Dependencias
- H2 Database (2.2.224)
- Oracle JDBC Driver (ojdbc8)

##  Instalación y Configuración

### Paso 1: Configurar Oracle Database

1. Instalar Oracle Database XE
2. Crear usuario (si es necesario):
```sql
CREATE USER inventario IDENTIFIED BY password123;
GRANT CONNECT, RESOURCE TO inventario;
GRANT CREATE SESSION TO inventario;
GRANT CREATE TABLE TO inventario;
GRANT CREATE SEQUENCE TO inventario;
GRANT CREATE PROCEDURE TO inventario;
```

3. Ejecutar el script de procedimientos almacenados:
```bash
sqlplus system/oracle@localhost:1521/XE
@procedimientos_oracle.sql
```

### Paso 2: Configurar Conexiones

Editar `ConexionManager.java` con tus credenciales:

```java
// Oracle
private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1521:XE";
private static final String ORACLE_USER = "system";
private static final String ORACLE_PASSWORD = "tu_password";
```

### Paso 3: Agregar Dependencias

**Opción A: Con Maven**
```bash
mvn clean install
```

**Opción B: Manual**
1. Descargar H2: https://www.h2database.com
2. Descargar ojdbc8.jar desde Oracle
3. Agregar JARs al Build Path del proyecto

### Paso 4: Compilar y Ejecutar

**Con Maven:**
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="GestorInventario"
```

**Con Java directo:**
```bash
javac -d bin -cp "lib/*" src/**/*.java
java -cp "bin:lib/*" GestorInventario
```

**Desde IDE:**
- Ejecutar `GestorInventario.java` como aplicación Java

##  Campos de la Tabla

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | INT/NUMBER | Identificador único (auto-incremental) |
| nombre | VARCHAR(100) | Nombre del producto |
| categoria | VARCHAR(50) | Categoría (Smartphone, Laptop, etc.) |
| precio | DECIMAL(10,2) | Precio unitario |
| stock | INT | Cantidad en inventario |
| fecha_ingreso | DATE | Fecha de ingreso al inventario |

##  Funcionalidades Implementadas

### Operaciones CRUD Básicas
-  **Create**: Insertar nuevos productos
-  **Read**: Listar todos los productos
-  **Update**: Actualizar productos existentes
-  **Delete**: Eliminar productos

### Funcionalidades Adicionales
-  **Búsqueda con LIKE**: Buscar productos por nombre
-  **Stock bajo**: Listar productos con stock menor a X
-  **Filtro por categoría**: Listar productos por categoría
-  **Estadísticas**: Contador de productos y valores totales

### Procedimientos Almacenados Oracle
- `sp_insertar_producto`: Insertar con validación
- `sp_actualizar_producto`: Actualizar con validación
- `sp_eliminar_producto`: Eliminar con validación
- `sp_ajustar_stock_categoria`: Ajuste masivo de stock
- `sp_obtener_estadisticas`: Estadísticas completas
- `fn_productos_stock_bajo`: Función para contar stock bajo
- `fn_valor_por_categoria`: Función para calcular valor por categoría

##  Uso del Sistema

### Menú Principal
```
1.  Insertar producto
2.  Listar todos los productos
3.  Buscar producto por ID
4.   Actualizar producto
5.   Eliminar producto
6.  Buscar por nombre (LIKE)
7.   Listar productos con stock bajo
8.  Listar por categoría
9.  Ver estadísticas
10.  Cambiar base de datos
0.  Salir
```

### Ejemplo de Uso

**Insertar Producto:**
```
Nombre: iPhone 15 Pro
Categoría: Smartphone
Precio: 1299.99
Stock: 50
Fecha ingreso: 06/11/2024
```

**Buscar por Nombre:**
```
Término de búsqueda: iPhone
→ Muestra todos los productos que contengan "iPhone"
```

**Stock Bajo:**
```
Stock mínimo: 10
→ Muestra productos con menos de 10 unidades
```

##  Cambio entre Bases de Datos

El sistema permite alternar entre H2 y Oracle en tiempo de ejecución:
- Las tablas se crean automáticamente al iniciar
- Los datos son independientes en cada BD
- Cambio inmediato sin reiniciar aplicación

##  Consultas Parametrizadas

Todos los métodos usan **PreparedStatement** o **CallableStatement** para:
-  Prevenir inyección SQL
-  Mejorar rendimiento
-  Reutilización de consultas
-  Manejo seguro de tipos

Ejemplo:
```java
String sql = "SELECT * FROM productos_electronicos WHERE id = ?";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setInt(1, id);
```

##  Manejo de Errores

- Try-with-resources para cierre automático de conexiones
- Bloques try-catch en todos los métodos DAO
- Mensajes descriptivos de error
- Validación de datos de entrada
- Transacciones con COMMIT/ROLLBACK en procedimientos

##  Extensiones Posibles

1. **Interfaz Gráfica**: Migrar a JavaFX o Swing
2. **Reportes**: Generar PDF/Excel con JasperReports
3. **API REST**: Exponer funcionalidades con Spring Boot
4. **Autenticación**: Sistema de usuarios y permisos
5. **Logs**: Integrar Log4j para auditoría
6. **Backup**: Exportar/importar datos

##  Solución de Problemas

### Error: Driver no encontrado
```
Solución: Verificar que los JARs estén en el classpath
```

### Error: No se puede conectar a Oracle
```
Solución: 
1. Verificar que Oracle esté ejecutándose
2. Comprobar credenciales en ConexionManager
3. Verificar puerto (por defecto 1521)
```

### Error: Tabla no existe
```
Solución: El sistema crea las tablas automáticamente.
Si persiste, ejecutar manualmente los scripts SQL.
```

##  Conceptos Demostrados

-  Conexión JDBC a múltiples bases de datos
-  Patrón DAO (Data Access Object)
-  Operaciones CRUD completas
-  Consultas parametrizadas
-  Procedimientos almacenados
-  Funciones almacenadas
-  Try-with-resources
-  Manejo de excepciones
-  Separación de capas (Modelo-DAO-UI)

##  Autor

Alejandro Mejías Ramírez
Trabajo Práctico Individual - JDBC y Procedimientos Almacenados
