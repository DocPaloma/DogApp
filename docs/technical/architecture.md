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
