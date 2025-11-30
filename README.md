# Bitácora Viajera v1.8

Un proyecto de aplicación para Android desarrollada como parte de un desafío estudiantil. La aplicación permite a los usuarios registrar sus viajes, y dentro de cada viaje, añadir lugares específicos con detalles como comentarios, una foto y coordenadas geográficas.

## Descripción

**Bitácora Viajera** es una aplicación de registro de viajes simple pero funcional. Los usuarios pueden crear una cuenta, iniciar sesión y gestionar sus aventuras. Cada viaje puede contener múltiples lugares de interés, y la aplicación utiliza el hardware del dispositivo (GPS y Cámara) para enriquecer las entradas. Adicionalmente, incluye una función para consultar el clima actual en una ciudad específica utilizando una API REST.

---

## Tecnologías Utilizadas

- **Lenguaje:** Java
- **Interfaz de Usuario (UI):** XML Clásico con componentes de Material Design.
- **Base de Datos:** SQLite nativo, gestionado a través de una capa de acceso a datos (DAO) para una arquitectura limpia.
- **Arquitectura:** Basada en Activities, con una clara separación de responsabilidades (UI -> Lógica -> DAO).
- **API REST:** `HttpURLConnection` para consumir la API de [OpenWeatherMap](https://openweathermap.org/api).
- **Dependencias Clave:**
  - `androidx.appcompat`
  - `com.google.android.material`
  - `androidx.recyclerview`
  - `androidx.constraintlayout`

---

## Instrucciones de Instalación y Configuración

Para clonar y ejecutar este proyecto localmente, sigue estos pasos:

1.  **Clona el repositorio:**
    ```sh
    git clone https://github.com/JGuajardoG/TravelLog.git
    ```

2.  **Abre el proyecto en Android Studio:**
    - En Android Studio, selecciona `File > Open` y navega hasta la carpeta del proyecto clonado.

3.  **Configura la API Key de OpenWeatherMap:**
    - La funcionalidad del clima requiere una clave de API.
    - Ve al archivo `app/src/main/java/com/myapp/travellog/ClimaActivity.java`.
    - Busca la constante `API_KEY` y reemplaza el valor con tu propia clave obtenida de [OpenWeatherMap](https://openweathermap.org/appid).

4.  **Ejecuta la aplicación:**
    - Sincroniza el proyecto con los archivos de Gradle si es necesario y luego ejecuta la aplicación en un emulador o dispositivo físico.

---

## Checklist de Funcionalidades

El proyecto implementa las 10 `Activities` requeridas y funcionalidades CRUD completas.

- [x] **1. SplashActivity:** Pantalla de carga inicial.
- [x] **2. LoginActivity:** Inicio de sesión de usuario.
- [x] **3. RegistroActivity:** Creación de nuevos usuarios.
- [x] **4. MenuPrincipalActivity:** Navegación principal mediante tarjetas interactivas.
- [x] **5. CrearViajeActivity:** Formulario para registrar un nuevo viaje con selector de fecha.
- [x] **6. ListaViajesActivity:** Muestra los viajes del usuario con botones visibles para editar y eliminar.
- [x] **7. DetalleViajeActivity:** Muestra los lugares de un viaje con botones visibles para editar y eliminar.
- [x] **8. AgregarLugarActivity:** Formulario para añadir un lugar usando GPS y Cámara.
- [x] **9. ClimaActivity:** Consulta y muestra datos del clima desde una API REST.
- [x] **10. AcercaDeActivity:** Información sobre el desarrollador y el proyecto (Versión 1.8).
- [x] **Gestión Completa (CRUD):** Se ha implementado la capacidad de Crear, Leer, Actualizar y Eliminar tanto para Viajes como para Lugares.
