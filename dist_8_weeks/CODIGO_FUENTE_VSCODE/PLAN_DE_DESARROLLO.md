# PLAN DE DESARROLLO (PDS): CLÍNICA AAUCA - 8 SEMANAS

Este documento establece la hoja de ruta estratégica para la evolución del sistema de Gestión Clínica Aauca, utilizando la metodología **SCRUM** para entregas incrementales y funcionales.

## 🗓️ Cronograma de Sprints y Entregas

### Semana 1: Autenticación y Arquitectura Base ✅ (COMPLETADO)
*   **Entregable**: Pantalla de Login conectada a SQLite, patrón DAO y estructura Maven.
*   **Logro**: Sistema seguro con BCrypt y roles diferenciados (Admin, Médico, Recepción).

### Semana 2: Dashboard Principal y Navegación 🔄 (EN PROGRESO)
*   **Entregable**: Ventana principal con menú lateral inteligente y estadísticas rápidas.
*   **Enfoque**: UI/UX fluida con StackPane para intercambio dinámico de vistas.

### Semana 3: Gestión de Pacientes (CRUD)
*   **Entregable**: Módulo para listar (TableView), buscar, editar y registrar pacientes.
*   **Enfoque**: Validación de campos (DNI, Teléfono) y persistencia robusta.

### Semana 4: Agenda Médica y Citas
*   **Entregable**: Calendario interactivo de asignación de médicos y pacientes.
*   **Enfoque**: Evitar cruces de horarios y gestión cronológica.

### Semana 5: Historias Clínicas y Consultas
*   **Entregable**: Editor de fichas médicas para diagnósticos y recetas.
*   **Enfoque**: Vinculación de historia clínica al ID del paciente.

### Semana 6: Facturación e Inventario de Farmacia
*   **Entregable**: Generación de recibos y control básico de stock de medicamentos.
*   **Enfoque**: Descuento automático de inventario tras emisión de recetas.

### Semana 7: Reportes y Analítica
*   **Entregable**: Gráficos (LineChart/PieChart) de afluencia de pacientes y ganancias mensuales.
*   **Enfoque**: Extracción directa de métricas desde SQLite.

### Semana 8: Pulido Final, Seguridad y Empaquetado
*   **Entregable**: Instalador final (jlink/jpackage) con icono y logs de sistema.
*   **Enfoque**: Transiciones suaves y optimización de rendimiento.

---

## 🛠️ Metodología Aplicada: SCRUM
Para garantizar el éxito del proyecto, se aplican los siguientes pilares:
1.  **Sprint Planning**: Definición de los objetivos del Sprint al inicio de cada semana.
2.  **Sprint Backlog**: Listado detallado de funciones por módulo.
3.  **Sprint Review**: Demostración del ejecutable funcional al cierre de cada fase.
4.  **Entrega Continua**: Generación de paquetes (.exe) incrementales para pruebas de usuario.

---
*Hoja de ruta estratégica - Sistema de Gestión Clínica.*
