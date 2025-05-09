# Sistema de Gestión de Citas Médicas (Desafío Kosmos)

## 📄 Descripción del Proyecto

Este proyecto es una implementación de backend con **Spring Boot** para resolver la problemática de gestión de citas médicas descrita en el desafío de evaluación de Kosmos. La aplicación permite registrar doctores y consultorios, y gestionar citas, aplicando un conjunto específico de **reglas de negocio** para la programación y manejo de las mismas.

El objetivo es demostrar la capacidad para modelar entidades relacionales, diseñar e implementar APIs RESTful, y codificar lógica de negocio compleja y validaciones robustas en un entorno Java/Spring.

## ✨ Características Implementadas

Basado en los requisitos del documento de desafío:

* **Registro Básico:** Permite registrar información sobre:
    * **Doctores:** Nombre, Apellidos, Especialidad (inserción por script inicial).
    * **Consultorios:** Número, Piso (inserción por script inicial).
* **Gestión de Citas Médicas:**
    * **Alta de Cita:** Permite crear nuevas citas asociadas a un Doctor y Consultorio, especificando el Paciente y Horario.
    * **Consulta de Citas:** Permite buscar y listar citas filtrando por **fecha**, **doctor** y/o **consultorio**.
    * **Edición de Cita:** Permite modificar los datos de una cita existente, re-validando todas las reglas de alta.
    * **Cancelación de Cita:** Permite cancelar una cita, validando que aún esté pendiente según su horario.
* **Reglas de Negocio (Validaciones de Alta y Edición):**
    * No se puede agendar cita en el **mismo consultorio a la misma hora exacta**.
    * No se puede agendar cita para el **mismo doctor a la misma hora exacta**.
    * Un **mismo paciente** no puede tener otra cita el **mismo día** con **menos de 2 horas de diferencia**.
    * Un mismo **doctor** no puede tener **más de 8 citas** en el día.
* **Validaciones y Mensajes de Error:** Implementación de validaciones de negocio y retorno de mensajes claros al usuario a través de la API.
* **Manejo Centralizado de Excepciones:** Utilización de `@RestControllerAdvice` para capturar excepciones específicas (recurso no encontrado, conflictos de negocio) y retornar respuestas estandarizadas con códigos de estado HTTP apropiados (`400`, `404`, `409`, etc.) y un cuerpo (`ExceptionResponseDTO`).

## 🛠️ Tecnologías Utilizadas

* **Backend:** Java (JDK 21)
* **Framework:** Spring Boot
    * **Spring Data JPA:** Capa de acceso a datos y ORM.
    * **Hibernate:** Implementación de JPA.
    * **Spring Web:** Exposición de APIs RESTful.
    * **Spring Boot Starter Validation:** Para validación de DTOs (@Valid).
* **Base de Datos (Desarrollo/Pruebas):** H2 Database (Base de datos en memoria).
* **Librerías de Productividad:**
    * **Lombok:** Reducción de código boilerplate (Getters, Setters, Constructores, etc.).
    * **MapStruct:** Generador de código para mapeo eficiente entre entidades y DTOs.
* **Herramienta de Construcción:** Maven

## 🏗️ Arquitectura y Diseño

El proyecto sigue una **Arquitectura en Capas** estándar y principios de diseño recomendados:

* **Capa de Controladores (Controller):** Recibe solicitudes HTTP, valida DTOs de entrada (`@Valid`), y delega la lógica al servicio.
* **Capa de Servicio (Service Interface/Implementation):** Contiene la lógica de negocio y las validaciones complejas. Interactúa con los repositorios para la persistencia. Se define una interfaz para el contrato.
* **Capa de Acceso a Datos (Repository):** Utiliza **Spring Data JPA** para realizar operaciones CRUD y consultas personalizadas sobre las entidades.
* **Capa de Modelo (Entities):** Representa las tablas de la base de datos (`Doctor`, `Consultorio`, `Cita`) y sus relaciones.
* **Capa DTOs (Data Transfer Objects):** Objetos para la transferencia de datos, desacoplando las entidades de la capa de presentación y solicitudes. Incluye DTOs de solicitud (`CitaRequestDto`) y respuesta (`DoctorDto`, `ConsultorioDto`, `CitaDto`).
* **Mappers (MapStruct):** Interfaces utilizadas por MapStruct para generar código que convierte eficientemente entre Entidades y DTOs.

Se aplican principios como **Separación de Responsabilidades**, **Inyección de Dependencias** y **Manejo Centralizado de Excepciones**.

## 🚀 Cómo Empezar

Estos pasos te permitirán clonar el repositorio y ejecutar el proyecto localmente:

1.  **Prerrequisitos:**
    * Java Development Kit (JDK) 21 o superior instalado.
    * Maven instalado.
    * Git instalado.

2.  **Clonar el Repositorio:**
    ```bash
    git clone <URL_DEL_TU_REPOSITORIO>
    cd <NOMBRE_DE_LA_CARPETA_CLONADA>
    ```
    *(Reemplaza `<URL_DEL_TU_REPOSITORIO>` y `<NOMBRE_DE_LA_CARPETA_CLONADA>` con los datos reales de tu repositorio)*

3.  **Construir y Ejecutar:**
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```
    Esto construirá el proyecto, descargará las dependencias e iniciará la aplicación Spring Boot.

4.  **Base de Datos H2:**
    * La base de datos H2 (en memoria) se inicializa automáticamente al inicio de la aplicación utilizando los scripts `src/main/resources/schema.sql` (estructura) y `src/main/resources/data.sql` (Doctores y Consultorios iniciales).
    * Puedes acceder a la consola web de H2 (si está habilitada en `application.properties`) en `http://localhost:8078/api/h2-console` (la URL JDBC suele ser `jdbc:h2:mem:testdb` o similar, verifica los logs de inicio).

5.  **Probar la API:**
    * La API REST estará disponible en `http://localhost:8078/api`.
    * Puedes usar herramientas como Postman o Insomnia para interactuar con los endpoints. Los datos iniciales de Doctores y Consultorios estarán disponibles.
    * Consulta la sección "Endpoints Principales de la API" y utiliza los ejemplos de las pruebas para interactuar con el sistema de citas.

## 🔑 Endpoints Principales de la API

* **Doctores:**
    * `GET /api/doctores`: Listar todos los doctores.
    * `GET /api/doctores/{id}`: Obtener doctor por ID.
* **Consultorios:**
    * `GET /api/consultorios`: Listar todos los consultorios.
    * `GET /api/consultorios/{id}`: Obtener consultorio por ID.
* **Citas Médicas:**
    * `POST /api/citas-medicas`: Crear una nueva cita. (Requiere DTO en el cuerpo).
    * `GET /api/citas-medicas?fecha={YYYY-MM-DD}&consultorioId={id}&doctorId={id}`: Consultar citas filtrando por fecha (requerido), consultorio (opcional) y/o doctor (opcional).
    * `GET /api/citas-medicas/{id}`: Obtener detalles de una cita por ID.
    * `PUT /api/citas-medicas/{id}`: Actualizar una cita por ID. (Requiere DTO en el cuerpo con campos a modificar).
    * `DELETE /api/citas-medicas/{id}`: Cancelar una cita por ID.

## 🎯 Habilidades Demostradas

Este proyecto demuestra experiencia en:

* **Desarrollo Backend con Java:** Diseño e implementación de servicios y lógica de negocio.
* **Spring Boot:** Uso integral del framework para APIs REST y gestión de datos.
* **Spring Data JPA / Hibernate:** Modelado ORM, consultas de repositorio y persistencia.
* **Diseño de Bases de Datos Relacionales:** Creación de un esquema relacional simple para la gestión de citas.
* **Diseño e Implementación de APIs RESTful:** Definición de endpoints, manejo de solicitudes/respuestas HTTP y DTOs.
* **Implementación de Lógica de Negocio Compleja:** Codificación de validaciones detalladas (conflictos horarios, límites) según especificaciones.
* **Manejo de Excepciones:** Implementación de un sistema centralizado para errores de negocio y validación.
* **Uso de Librerías de Productividad:** Lombok para reducir boilerplate y MapStruct para mapeo de datos.
* **Principios de Arquitectura:** Aplicación de arquitectura en capas (MVC, Service, Repository).

## 📧 Contacto

* **Email:** hectorr9577@gmail.com
* **LinkedIn:** https://www.linkedin.com/in/hector-adrian-roman79509/

## 📜 Licencia

Este proyecto está licenciado bajo la **Licencia MIT**.

La Licencia MIT es una licencia de software libre muy permisiva, lo que significa que eres libre de usar, copiar, modificar, fusionar, publicar, distribuir, sublicenciar y/o vender copias del software. Esto facilita enormemente la **colaboración** y el uso del código en otros proyectos, siempre y cuando se incluya el aviso de copyright original y este texto de permiso.

Puedes encontrar el texto completo de la licencia en el archivo [LICENSE](LICENSE) en la raíz del repositorio.
