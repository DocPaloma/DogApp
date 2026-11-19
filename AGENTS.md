# AGENTS.md — Reglas Técnicas del Proyecto DogApp

Este archivo define las reglas de arquitectura, diseño e implementación obligatorias para todo el equipo de desarrollo en **DogApp (Miniproyecto 1 - Sprint 1, Universidad del Valle)**, basadas en las Historias de Usuario (HU 1.0 a HU 5.0) y la rúbrica oficial del curso.

## 1. Arquitectura y Stack Obligatorio (RA-1 — 60%)
- **Patrón MVVM + Repository estricto:**
  - `Fragment` / `Activity` (Vista): observa `LiveData` usando `viewLifecycleOwner` y delega eventos al `ViewModel`. **PROHIBIDO** realizar consultas a Room o llamadas HTTP con Retrofit directamente desde un `Fragment` o `Activity`.
  - `ViewModel`: contiene la lógica de presentación, ejecuta tareas asíncronas con **Corrutinas** (`viewModelScope.launch`) y expone estado reactivo mediante `LiveData`.
  - `Repository`: único puente entre los `ViewModel` y las fuentes de datos (`Room` DAO y `Retrofit` ApiService).
- **Single Activity + Fragments + Navigation Component:**
  - Una única `MainActivity` (`AppCompatActivity`) que aloja un `NavHostFragment` conectado a `nav_graph.xml`.
  - Cada pantalla principal (Login, Home, Nueva Cita, Detalle de Cita, Editar Cita) debe ser un `Fragment`.
- **Persistencia Local (SQLite con Room):**
  - Usar `@Entity`, `@Dao` y `RoomDatabase`.
  - El turno de atención (`#1`, `#2`, etc.) corresponde al ID autogenerado por Room (`@PrimaryKey(autoGenerate = true)`).
- **Consumo de API REST (Retrofit — Dog CEO API):**
  - Listado de razas para autocompletado: `https://dog.ceo/api/breeds/list/all`.
  - Imagen aleatoria alusiva a la raza seleccionada: `https://dog.ceo/api/breeds/image/random` (o endpoint por raza).

## 2. Interfaz de Usuario y Experiencia (RA-2 — 20%)
- **Vistas tradicionales en XML (PROHIBIDO Jetpack Compose):**
  - Usar layouts XML, `ViewBinding` y Material Components. No agregar nuevos `@Composable`.
- **Indicadores de carga (`loading`):**
  - Incluir indicadores visuales de carga (`ProgressBar`) en operaciones asíncronas de red o base de datos.
- **Lineamientos visuales y criterios exactos por Historia de Usuario:**
  - **HU 1.0 (Login):** Fondo gris oscuro, sin toolbar, cabeza de perro con fondo transparente arriba a la derecha, título `"DogApp"` blanco bold centrado (con fuente externa), animación Lottie de huella abajo al centro, `BiometricPrompt` (`"Autenticación con Biometría"`, `"Ingrese su huella digital"`, botón `"Cancelar"`), e ícono personalizado de la app con nombre exacto **`dogApp`**.
  - **HU 2.0 (Home):** Fondo blanco, header gris oscuro con esquina inferior izquierda curvada (`"Administrador de Citas"` + imagen/título `"DogApp"`), lista en `RecyclerView` con tarjetas elevadas (imagen circular, nombre, síntoma, turno `#id`), FAB rosado `#F05861` hacia Nueva Cita, y botón atrás del dispositivo que envía al escritorio (no regresa al Login).
  - **HU 3.0 (Nueva Cita):** Toolbar gris oscuro con esquinas inferiores curvadas y flecha atrás rosada; campos `TextInputLayout` con hint gris que sube al enfocar y borde morado en foco:
    - `Nombre de la Mascota`: texto, máximo **15** caracteres.
    - `Raza`: `AutoCompleteTextView` con sugerencias desde **2 letras** vía Retrofit.
    - `Nombre del Propietario`: texto, máximo **30** caracteres.
    - `Teléfono`: solo números, máximo **10** caracteres.
    - Selector de síntomas con `"Síntomas"` por defecto y 6 opciones fijas (`Solo duerme`, `No come`, `Fractura extremidad`, `Tiene pulgas`, `Tiene garrapatas`, `Bota demasiado pelo`); si falta síntoma mostrar `"Selecciona un síntoma"`.
    - Botón `"Guardar Cita"` (`#F05861`, texto blanco bold al habilitarse) inactivo mientras falten los 4 campos de texto.
  - **HU 4.0 (Detalle de la Cita):** Toolbar con nombre de la mascota y flecha rosada a Home; `CardView` de imagen superpuesta sobre tarjeta blanca con `#turno`, raza en negro bold, síntoma, línea rosada, `Propietario` y `Teléfono`; FAB rosado izquierdo (elimina inmediatamente en Room y vuelve a Home) y FAB azul oscuro derecho (navega a Editar Cita).
  - **HU 5.0 (Editar Cita):** Toolbar `"Editar Cita"` con flecha rosada hacia Detalle; precarga los datos desde Detalle, mantiene las mismas validaciones (`15`/`30`/`10` y `AutoCompleteTextView` de raza), actualiza en Room (incluyendo imagen por raza) y regresa a Home.

## 3. Trabajo Colaborativo y Calidad de Código (RA-3 — 20%)
- **Coordinación en Git/GitHub:** Trabajar por ramas de funcionalidad (`feature/*`), evitando modificar archivos compartidos de configuración (`build.gradle.kts`, `libs.versions.toml`, `themes.xml`) sin coordinar con el responsable de arquitectura base.
- **Código limpio:** Escribir Kotlin y XML idiomático y legible, evitando comentarios obvios o redundantes.
- **Documentación del proyecto:** Mantener actualizada la carpeta `docs/` (documentación técnica y de usabilidad) y `PROJECT_DOCUMENTATION.md`.
