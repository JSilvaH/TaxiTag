# TaxiTag - Registro de Taxis

## Descripción

TaxiTag es una aplicación para dispositivos Android diseñada para simplificar el registro y seguimiento de taxis mediante el uso de tecnologías modernas. Con esta aplicación, los usuarios pueden registrar fácilmente el número económico de un taxi junto con su ubicación en el momento del registro. La aplicación ofrece una interfaz intuitiva y funciones útiles para gestionar y visualizar la información recopilada.

## Características Principales

### 🚖 Registro de Taxis
- **Número Económico:** Ingresa el número económico único de cada taxi.
- **Ubicación:** Registra automáticamente la ubicación del taxi al momento del registro.

### 📋 Lista de Taxis Registrados
- **Visualización:** Muestra una lista completa de todos los taxis registrados.
- **Ordenamiento:** Permite ver el número de serie más alto y más bajo en la pantalla principal.

### 🚦 Detalles de Taxis
- **Detalles Individuales:** Consulta la información detallada de cada taxi, incluyendo la hora de registro.
- **Filtrado:** Filtra los taxis por número de serie, hora de registro, etc.

### 🗺️ Mapa de Ubicación
- **Vista de Mapa:** Muestra un mapa interactivo con la ubicación de todos los taxis registrados.
- **Información al Presionar:** Al presionar un icono de localización, se muestra el número de unidad y la hora de registro.

## Tecnologías Utilizadas

- **Kotlin:** Lenguaje de programación principal para el desarrollo de la aplicación.
- **Room:** Biblioteca para la persistencia de datos en la base de datos local de la aplicación.
- **Compose:** Framework de interfaz de usuario moderno para el desarrollo de la interfaz de usuario.
- **Firebase Database:** Utilizado para almacenar y sincronizar datos en la nube, permitiendo el acceso desde múltiples dispositivos.

## Instalación

1. Clona este repositorio: `git clone https://github.com/JSilvaH/TaxiTag.git`
2. Abre el proyecto en Android Studio.
3. Configura las credenciales de Firebase.
4. Compila y ejecuta la aplicación en tu dispositivo Android.

## Preview
<p align="center">
  <img src="/images/Screenshot_20240109_125703.png" width=150/>&nbsp;&nbsp;&nbsp;
  <img src="/images/register.png" width=150/>&nbsp;&nbsp;&nbsp;
  <img src="/images/list.png" width=150/>&nbsp;&nbsp;&nbsp;
  <img src="/images/map_taxi.png" width=150/>
  <img src="/images/all_taxis.png" width=150/>
</p>
