# DogApp — Pet Appointment Scheduler

**Universidad del Valle** — Escuela de Ingeniería de Sistemas y Computación (Sede Cali)  
**Asignatura:** Desarrollo de Aplicaciones para Dispositivos Móviles  
**Proyecto:** Miniproyecto 1 — Sprint 1  

---

## 1. Descripción General
**DogApp** es una aplicación móvil nativa para Android desarrollada en Kotlin orientada a administradores de clínicas veterinarias. Permite gestionar el registro, consulta, edición y eliminación de citas para mascotas, integrando autenticación biométrica dactilar, persistencia local en SQLite y consumo de API REST externa para el catálogo e imágenes de razas caninas.

### Historias de Usuario Implementadas
- **HU 1.0 — Ventana Login:** Acceso mediante biometría dactilar (`BiometricPrompt`) con animación Lottie e ícono personalizado (`dogApp`).
- **HU 2.0 — Home Administrador de Citas:** Listado de citas almacenadas localmente con foto circular de la raza, nombre, síntoma y turno autogenerado.
- **HU 3.0 — Ventana Nueva Cita:** Formulario con validaciones de entrada, autocompletado de razas (API Dog CEO), selector de síntomas y guardado local con imagen aleatoria de la raza.
- **HU 4.0 — Ventana Detalle de la Cita:** Vista detallada del paciente con acciones directas de eliminación y edición.
- **HU 5.0 — Ventana Editar Cita:** Edición de datos precargados desde el detalle, actualización de raza/imagen y persistencia en base de datos local.

---

## 2. Arquitectura y Stack Tecnológico

### Arquitectura
La aplicación sigue una arquitectura **Single Activity + Fragments** bajo el patrón **MVVM (Model-View-ViewModel)** con **Patrón Repository**:
- **UI (View):** `MainActivity` + `Fragments` con vistas XML tradicionales y `ViewBinding`, conectados mediante **Jetpack Navigation Component** (`nav_graph.xml`).
- **ViewModel:** Gestión de estado reactivo de la interfaz mediante `LiveData` y ejecución asíncrona con **Corrutinas de Kotlin** (`viewModelScope`).
- **Repository:** Capa de abstracción que coordina el acceso a los datos locales (**Room**) y remotos (**Retrofit**).
- **Data (Local y Remoto):**
  - **Local:** SQLite mediante **Room** (`Entity`, `DAO`, `RoomDatabase`).
  - **Remoto:** Consumo de **Dog CEO API** (`https://dog.ceo/api/`) mediante **Retrofit** + convertidor JSON.

### Stack Tecnológico
- **Lenguaje:** Kotlin
- **Interfaz de Usuario:** XML Layouts, Material Components (`TextInputLayout`, `FloatingActionButton`, `CardView`), `RecyclerView`
- **Navegación:** AndroidX Navigation Component (`nav_graph.xml`, `NavHostFragment`)
- **Persistencia Local:** AndroidX Room (SQLite)
- **Red / API REST:** Retrofit + Gson/Moshi
- **Asincronía y Reactividad:** Kotlin Coroutines + `LiveData`
- **Biometría y Animación:** AndroidX Biometric + Lottie

---

## 3. Estructura de Paquetes Esperada (`com.example.dogapp`)

```text
com.example.dogapp/
├── data/
│   ├── local/
│   │   ├── AppointmentEntity.kt
│   │   ├── AppointmentDao.kt
│   │   └── AppDatabase.kt
│   ├── remote/
│   │   ├── DogApiService.kt
│   │   ├── RetrofitClient.kt
│   │   └── model/
│   │       ├── BreedsResponse.kt
│   │       └── DogImageResponse.kt
│   └── repository/
│       └── AppointmentRepository.kt
├── ui/
│   ├── login/
│   │   └── LoginFragment.kt
│   ├── home/
│   │   ├── HomeFragment.kt
│   │   ├── HomeViewModel.kt
│   │   └── AppointmentAdapter.kt
│   ├── create/
│   │   ├── NewAppointmentFragment.kt
│   │   └── NewAppointmentViewModel.kt
│   ├── detail/
│   │   ├── DetailAppointmentFragment.kt
│   │   └── DetailAppointmentViewModel.kt
│   └── edit/
│       ├── EditAppointmentFragment.kt
│       └── EditAppointmentViewModel.kt
└── MainActivity.kt
```

---

## 4. Datos de Entrega del Equipo (Plantilla para Informe PDF)

> **Instrucciones de entrega:** Un integrante del equipo debe subir el archivo PDF al Campus Virtual con el nombre exacto del equipo (ej. `equipoUno.pdf`) antes del **26 de noviembre de 2026 a las 6:00 p.m.**

- **Nombre del Equipo:** `[Completar: ej. equipoUno]`
- **Integrantes (Nombres y Apellidos):**
  1. `[Nombre completo integrante 1]`
  2. `[Nombre completo integrante 2]`
  3. `[Nombre completo integrante 3]`
- **Enlace al Repositorio de GitHub:** `[https://github.com/...]`
- **Enlace al Tablero de Gestión (Jira):** `[https://...atlassian.net/...]`
