## Aplicación de envío de mensajes de WhatsApp

### Descripción General
Esta aplicación está diseñada para enviar mensajes de WhatsApp utilizando la API de UltraMsg. Está construida utilizando **Spring Boot** y utiliza **RestTemplate** para realizar solicitudes HTTP a la API de UltraMsg. La aplicación está estructurada con una capa de **Servicio** (`UltraMsgService`) y una capa de **Controlador** (`WhatsAppController`).

### Tecnologías Utilizadas
- **Spring Boot**
- **RestTemplate**
- **API de UltraMsg**
- **SLF4J**
- **Maven**

## Capa de Servicio: `UltraMsgService`

### Responsabilidades
- **Envío de Mensajes**: El servicio es responsable de enviar mensajes de WhatsApp a una lista de números de teléfono.
- **Gestión de Cuentas**: Gestiona dos cuentas diferentes de UltraMsg y alterna entre ellas después de enviar un cierto número de mensajes (`MESSAGE_LIMIT`).
- **Comunicación con la API**: Se comunica con la API de UltraMsg utilizando `RestTemplate` para enviar mensajes.

### Características Clave
- **Alternancia de Cuentas**: El servicio alterna entre dos cuentas de UltraMsg para evitar alcanzar límites de mensajes.
- **Límite de Mensajes**: El servicio cambia de cuenta después de enviar un número predefinido de mensajes (`MESSAGE_LIMIT`).
- **Seguridad en Hilos**: El método `sendMessage` está sincronizado para garantizar la seguridad en hilos al cambiar de cuentas.

### Detalles de la API
- **URL de la API**: `https://api.ultramsg.com/{instanceId}/messages/chat`
- **Cuerpo de la Solicitud**: El cuerpo de la solicitud incluye el `token`, `to` (número de teléfono) y `body` (contenido del mensaje).
- **Encabezados**: El `Content-Type` se establece en `application/json`.

Para más detalles sobre la API de UltraMsg, consulta la [Documentación de la API de UltraMsg](https://ultramsg.com/docs/).

## Capa de Controlador: `WhatsAppController`

### Responsabilidades
- **Manejo de Solicitudes HTTP**: El controlador maneja las solicitudes HTTP POST entrantes para enviar mensajes de WhatsApp.
- **Registro de Logs**: Registra el mensaje y los números de teléfono a los que se está enviando el mensaje.
- **Interacción con el Servicio**: Interactúa con el `UltraMsgService` para enviar mensajes.

### Características Clave
- **Números de Teléfono Fijos**: El controlador envía mensajes a una lista predefinida de números de teléfono.
- **Registro de Logs**: Utiliza SLF4J para registrar el mensaje y los números de teléfono.
- **Respuesta**: Devuelve un `ResponseEntity` con un mensaje de éxito después de enviar los mensajes.

### Detalles del Endpoint
- **Endpoint**: `/send-whatsapp/send-fixed`
- **Método HTTP**: `POST`
- **Cuerpo de la Solicitud**: El cuerpo de la solicitud debe contener un objeto JSON con un campo `message`.
- **Respuesta**: Devuelve una respuesta `200 OK` con un mensaje de éxito.

## Flujo de la Aplicación

1. **Solicitud del Cliente**: Un cliente envía una solicitud HTTP POST al endpoint `/send-whatsapp/send-fixed` con un cuerpo JSON que contiene el mensaje.
2. **Manejo del Controlador**: El `WhatsAppController` recibe la solicitud, registra el mensaje y los números de teléfono, y llama al `UltraMsgService` para enviar los mensajes.
3. **Procesamiento del Servicio**: El `UltraMsgService` envía los mensajes a los números de teléfono proporcionados utilizando la API de UltraMsg. Alterna entre dos cuentas si se alcanza el límite de mensajes.
4. **Comunicación con la API**: El servicio realiza solicitudes HTTP POST a la API de UltraMsg para enviar los mensajes.
5. **Respuesta**: El controlador devuelve un mensaje de éxito al cliente después de que se hayan enviado todos los mensajes.

## Configuración de Maven

### Anulaciones del Padre
El proyecto utiliza Maven para la gestión de dependencias. Para evitar la herencia no deseada del POM padre, el POM del proyecto contiene anulaciones vacías para elementos como `<license>` y `<developers>`.

### Documentación de Referencia
Para más información sobre Maven y Spring Boot, consulta la siguiente documentación:
- [Documentación Oficial de Apache Maven](https://maven.apache.org/guides/index.html)
- [Guía de Referencia del Plugin Maven de Spring Boot](https://docs.spring.io/spring-boot/3.4.3/maven-plugin)
- [Crear una Imagen OCI](https://docs.spring.io/spring-boot/3.4.3/maven-plugin/build-image.html)

## Conclusión
Esta aplicación demuestra cómo integrarse con una API externa (UltraMsg) para enviar mensajes de WhatsApp utilizando Spring Boot. Muestra el uso de `RestTemplate`, la abstracción de la capa de servicio y el manejo del controlador en una aplicación Spring Boot.
