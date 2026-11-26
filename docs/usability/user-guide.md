# Guía de Usabilidad — DogApp

## Flujo General de Pantallas
1. **Login Biométrico (HU 1.0):** Al abrir la aplicación, el usuario presiona el ícono animado de huella digital para autenticarse con la biometría del dispositivo (`Autenticación con Biometría`).
2. **Home — Administrador de Citas (HU 2.0):** Muestra el listado de citas registradas con foto de la raza, nombre de la mascota, síntoma y número de turno (`#id`). El botón rosado inferior derecho permite crear una nueva cita; presionar el botón atrás del dispositivo cierra la aplicación hacia el escritorio.
3. **Nueva Cita (HU 3.0):** Solicita nombre de mascota (máx. 15 caracteres), raza (autocompletado desde 2 letras), propietario (máx. 30 caracteres), teléfono (de 7 a 10 dígitos) y síntoma. Si falta seleccionar un síntoma, muestra `"Selecciona un síntoma"`.
4. **Detalle de la Cita (HU 4.0):** Muestra la ficha completa de la mascota seleccionada con dos acciones flotantes: eliminar cita (botón rosado izquierdo) o editar cita (botón azul derecho).
5. **Editar Cita (HU 5.0):** Precarga la información de la cita desde el detalle, permite modificar los campos con las mismas validaciones y actualiza la imagen si cambia la raza.
