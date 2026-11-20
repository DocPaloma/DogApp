# Arquitectura Técnica — DogApp

## 1. Patrón Arquitectónico (MVVM + Repository)
La aplicación implementa el patrón **Model-View-ViewModel (MVVM)** con una capa **Repository** como punto único de acceso a los datos:
- **View (`Fragment` / `MainActivity`):** Observa `LiveData` mediante `viewLifecycleOwner` y delega acciones de usuario al `ViewModel`. No realiza consultas directas a Room ni a Retrofit.
- **ViewModel:** Orquesta la lógica de presentación y ejecuta operaciones asíncronas en `viewModelScope.launch`.
- **Repository (`AppointmentRepository`):** Centraliza las fuentes de datos local (**Room**) y remota (**Retrofit**).

## 2. Persistencia Local (Room / SQLite)
- **Base de datos:** `AppDatabase` (`dogapp_database`, versión `1`).
- **Tabla `appointments` (`AppointmentEntity`):**
  - `id`: `Int` (`@PrimaryKey(autoGenerate = true)`), representa el turno de atención (`#1`, `#2`, ...).
  - `petName`: `String` (máximo 15 caracteres en UI).
  - `breed`: `String`.
  - `ownerName`: `String` (máximo 30 caracteres en UI).
  - `phone`: `String` (máximo 10 dígitos en UI).
  - `symptom`: `String`.
  - `imageUrl`: `String` (URL obtenida de Dog CEO API).
- **DAO (`AppointmentDao`):** Expone consultas reactivas (`LiveData`) para listar y observar por ID, además de operaciones `suspend` para insertar, actualizar y eliminar.

## 3. Cliente HTTP (Retrofit — Dog CEO API)
- **Base URL:** `https://dog.ceo/api/`
- **Endpoints consumidos (`DogApiService`):**
  - `GET breeds/list/all`: Obtiene el mapa de razas y subrazas para autocompletado.
  - `GET breed/{breed}/images/random` y `GET breed/{breed}/{subBreed}/images/random`: Obtiene una imagen aleatoria de la raza seleccionada.
  - `GET breeds/image/random`: Respaldo aleatorio si una raza no coincide exactamente.

## 4. Navegación (Navigation Component)
- **Single Activity:** `MainActivity` (`AppCompatActivity`) aloja un `NavHostFragment` (`activity_main.xml`) conectado a `res/navigation/nav_graph.xml`.
- **Destino inicial:** `loginFragment`.

| Acción | Origen → Destino | Comportamiento de la pila |
|---|---|---|
| `action_loginFragment_to_homeFragment` | Login → Home | `popUpTo nav_graph` inclusive: el Login sale de la pila y "atrás" en Home cierra la app (HU 2.0, criterio 1). |
| `action_homeFragment_to_newAppointmentFragment` | Home → Nueva Cita | Al guardar o con la flecha atrás: `findNavController().popBackStack()`. |
| `action_homeFragment_to_detailAppointmentFragment` | Home → Detalle | Tras eliminar o con la flecha atrás: `popBackStack()`. |
| `action_detailAppointmentFragment_to_editAppointmentFragment` | Detalle → Editar | Flecha atrás: `popBackStack()` (regresa al Detalle). |
| `action_editAppointmentFragment_to_homeFragment` | Editar → Home | `popUpTo homeFragment` inclusive: limpia Detalle/Editar y deja una única instancia de Home. |

Los datos entre pantallas se envían con `Bundle`, como se vio en clase:

```kotlin
val bundle = Bundle()
bundle.putSerializable("clave", appointment)
findNavController().navigate(R.id.action_homeFragment_to_detailAppointmentFragment, bundle)

val appointment = arguments?.getSerializable("clave") as AppointmentEntity
```

### Integración de ramas
- **Orden de merge:** `feature/navigation` se creó a partir de `feature/login-screen` porque el grafo arranca en `LoginFragment`. Conviene mergear primero `feature/login-screen` en `main` y después `feature/navigation`.
- **Clases que espera el grafo:** cada pantalla debe crearse con exactamente este nombre y paquete (o actualizar `nav_graph.xml` si se cambia):
  - `com.example.dogapp.view.login.LoginFragment`
  - `com.example.dogapp.view.home.HomeFragment` *(pendiente: rama de UI Home)*
  - `com.example.dogapp.view.create.NewAppointmentFragment` *(pendiente: rama de UI Nueva Cita)*
  - `com.example.dogapp.view.detail.DetailAppointmentFragment`
  - `com.example.dogapp.view.edit.EditAppointmentFragment`
