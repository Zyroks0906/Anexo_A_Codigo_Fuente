**Gestor de Inventario Electrónico**

Pequeña aplicación Java para gestionar productos electrónicos. Soporta dos tipos de base de datos: **H2** (recomendada para comenzar) y **Oracle** (opcional, para entornos más avanzados).

**Requisitos**
- Java JDK 21
- (Opcional) Oracle Database si quieres usar la opción Oracle
- Archivos JAR necesarios: `h2-2.4.240.jar` y `ojdbc8.jar`

**Preparar el proyecto**
1. Crear la carpeta `src/lib/` (si no existe).
2. Copiar los JAR (`h2-2.4.240.jar` y `ojdbc8.jar`) dentro de `src/lib/`.

**Compilar y ejecutar (Windows / PowerShell)**
```powershell
# Compilar (genera la carpeta bin con las clases compiladas)
javac -cp "src/lib/h2-2.4.240.jar;src/lib/ojdbc8.jar" -d bin src/*.java src/conexion/*.java src/dao/*.java src/modelo/*.java

# Ejecutar la aplicación
java -cp "bin;src/lib/h2-2.4.240.jar;src/lib/ojdbc8.jar" GestorInventario
```

Nota: si usas otra shell o sistema operativo, ajusta el separador del classpath (`:` en Unix/macOS, `;` en Windows).

**Estructura del proyecto**
```
src/
├── modelo/
│   └── ProductoElectronico.java
├── conexion/
│   └── ConexionManager.java
├── dao/
│   ├── ProductoDAO.java
│   └── ProductoService.java
└── GestorInventario.java

```

**Qué puedes hacer (menú principal)**
- Insertar producto
- Listar productos
- Buscar por ID
- Actualizar producto
- Eliminar producto
- Buscar por nombre (búsqueda parcial)
- Ver stock bajo
- Listar por categoría
- Ver estadísticas
- Cambiar base de datos (H2 / Oracle)
- Salir

**Ejemplos rápidos**

Insertar producto (valores de ejemplo):
```
Nombre: iPhone 16
Categoría: Telefono
Precio: 999.99
Stock: 50
```

Buscar productos por texto:
```
Buscar: iPhone
Resultado: iPhone 16, iPhone 15, ...
```

**Bases de datos**
- H2 (recomendada para empezar):
  - Ligera, no requiere instalación.
  - Guarda datos en un archivo local dentro del proyecto.

- Oracle (opcional, para usuarios más avanzados):
  - Requiere instalación y configuración.
  - Usar solo si ya tienes Oracle disponible y sabes configurarlo.

**Problemas comunes y soluciones**
- Error: "Base de datos en uso"
  - Cierra procesos Java que puedan estar usando la BD:
  ```powershell
  Get-Process java* | Stop-Process -Force
  Get-Process javaw* | Stop-Process -Force
  ```

- Error: "Driver no encontrado"
  - Asegúrate de que los JAR estén en `src/lib/` y usa el classpath correcto al compilar/ejecutar.

- No se conecta a Oracle
  - Verifica que Oracle esté ejecutándose y que las credenciales en `ConexionManager.java` sean correctas.

**Conceptos importantes (rápido)**
- CRUD: Crear, Leer, Actualizar, Eliminar — operaciones básicas sobre datos.
- DAO: Patrón que separa la lógica de acceso a datos del resto del programa.
- JDBC: API Java para conectar con bases de datos.
- Try-with-resources: estructura Java que cierra automáticamente conexiones/recurso.

**Autor**
Alejandro Mejias Ramirez
DAM - AED
Entrega: 14/11/2025

---

