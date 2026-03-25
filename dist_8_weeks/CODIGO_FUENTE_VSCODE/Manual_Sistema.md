# MANUAL TÉCNICO Y DE USUARIO: CLÍNICA AAUCA v1.0

Este documento detalla el funcionamiento interno, la arquitectura y el modo de uso del **Sistema de Gestión Clínica Aauca**.

## 1. Introducción
El sistema ha sido diseñado para centralizar la gestión médica, permitiendo un acceso seguro mediante roles diferenciados. Utiliza un stack tecnológico moderno (Java 17+, JavaFX, SQLite) con un enfoque en la seguridad de datos (BCrypt).

## 2. Arquitectura del Software
Se ha implementado siguiendo el patrón **DAO (Data Access Object)** para garantizar la escalabilidad:
*   **Modelo (`User.java`)**: Estructura de datos que representa a los empleados de la clínica.
*   **DAO (`UserDAO.java`)**: Interfaz y lógica separada para interactuar con la base de datos SQLite.
*   **Controladores (`LoginController`, `DashboardController`)**: Gestión de eventos de la interfaz de usuario.
*   **Vistas (`FXML`)**: Archivos de diseño declarativo que separan la estética de la lógica.

## 3. Seguridad de Acceso
El sistema no almacena contraseñas en texto claro. Utiliza el algoritmo **BCrypt** con un factor de coste de 12 para generar hashes irreversibles, protegiendo al sistema contra ataques de fuerza bruta.

### 👥 Perfiles de Usuario y Credenciales:
| Tipo de Usuario | Username | Contraseña (Prueba) | Funcionalidad |
| :--- | :--- | :--- | :--- |
| **Administrador** | `admin` | `admin123` | Gestión total de la clínica, reportes y configuración. |
| **Personal Médico** | `medico` | `medico123` | Gestión de pacientes, historias clínicas y agenda médica. |
| **Recepción** | `recepcion` | `recep123` | Agendado de citas, registro de pagos y atención al público. |

## 4. Guía de Uso del Dashboard
El Dashboard es **dinámico**. Al iniciar sesión, el sistema identifica el rol del usuario y ajusta:
1.  **Menú Lateral**: Solo se muestran las opciones permitidas para dicho rol.
2.  **Tarjetas de Estadísticas**: Muestra indicadores de rendimiento (KPIs) específicos (ej. Dr. Juan Pérez verá sus pacientes pendientes).
3.  **Área de Trabajo**: Espacio centralizado para la gestión de módulos.

## 5. Especificaciones Técnicas (Mantenimiento)
*   **Base de Datos**: `clinica_aauca.db` (SQLite). Se inicializa automáticamente mediante el script `schema.sql`.
*   **Estilos UI**: Gestión mediante `style.css` utilizando variables de color personalizadas.
*   **Entorno Requerido**: Java 17+ (JDK) y Apache Maven 3.9+.

## 6. Procedimiento de Ejecución
1.  Extraer el archivo comprimido `SGC_Entrega_Final.zip`.
2.  Ejecutar el archivo `Ejecutar_Clinica.bat` (Windows).
3.  Ingresar credenciales válidas.

---
