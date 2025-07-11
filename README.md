# 🚴‍♂️ Km+ - Aplicació de rutes en bicicleta

**Km+** és una aplicació Android desenvolupada per fomentar la mobilitat sostenible. Els usuaris ciclistes poden registrar rutes mitjançant GPS, acumular punts per cada quilòmetre recorregut i bescanviar-los per recompenses en comerços locals. L'administrador pot gestionar usuaris, recompenses i rutes des d’un panell web.

---

## 🧩 Funcionalitats

### 👤 Usuari ciclista
- Iniciar i finalitzar rutes.
- Visualitzar estadístiques detallades (distància, temps, velocitat mitjana/màxima, mapa).
- Consultar saldo i bescanviar-lo per recompenses.
- Visualitzar historial de rutes i recompenses.
- Editar perfil i recuperar contrasenya.

### 🛠️ Administrador
- Validar i invalidar rutes.
- Crear, assignar i eliminar recompenses.
- Visualitzar detalls i historial dels usuaris.
- Modificar paràmetres del sistema.

---

## 🧪 Tecnologies utilitzades

### 📱 Aplicació Android
- **Kotlin**
- **Jetpack Compose**
- Arquitectura **Clean Architecture** (UI - Domain - Data)
- Patró **MVVM**
- Gestió d'estat amb `MutableStateFlow`
- **Material Design**

### 🌐 Panell d'administració Web
- **Spring Boot** (Java)
- **HTML + CSS + Thymeleaf**
- Autenticació i gestió de sessions

### 🔙 Backend
- API REST amb **Spring Boot**
- Logs d'errors i excepcions
- Arquitectura modular i portable
- Desplegament amb **Docker**

### 🗃️ Base de dades
- **MySQL**
- Modelatge amb diagrama E-R

---

## 📸 Captures de pantalla

### App Android

- Pantalla d'inici de sessió  
  ![Inici de sessió]
![image](https://github.com/user-attachments/assets/10922fe2-f63d-4e3d-90e6-97f11b4003b1)

- Mapa amb ruta en temps real  
  ![Mapa ruta](screenshots/mapa.png)
![image](https://github.com/user-attachments/assets/f9688c8d-fba7-416e-ada5-167490b08432)

- Historial de rutes  
  ![Historial rutes]
![image](https://github.com/user-attachments/assets/f4ac6bcb-6e63-47fa-8345-12e0d14ccd07)

### Web Admin

- Llistat d’usuaris  
  ![Llistat usuaris](screenshots/admin_usuaris.png)
![image](https://github.com/user-attachments/assets/7c287f39-4d2b-407c-9bd1-f9875d983f94)

- Gestió de recompenses  
  ![Gestió recompenses](screenshots/admin_recompensa.png)
![image](https://github.com/user-attachments/assets/43ed0006-9196-40d6-9b24-5bd4978413ca)

---

## 📄 Memòria tècnica

📘 La memòria tècnica del projecte està **actualitzada** amb les correccions de la fase de disseny i desenvolupament. És accessible a través de GitLab Pages:

🔗 [Accedeix a la Memòria Tècnica](https://gitlab.com/david.degonz/proyecte4_km/-/blob/a32a93b2c145854a7a931a562dfcee550dae6946/Documentacio/Memoria_Tecnica.pdf)

---

## 🔗 Altres recursos

- 🎨 [Prototip Figma Web](https://www.figma.com/design/3NmViZwpsBYYAxFf9OaU5n/Km-Web?m=auto&t=bb7FCRxpvY6sHhD2-6)
- 📱 [Prototip Figma Mobile](https://www.figma.com/design/OGsn1FBVYPyfROpLIogZwV/Km-?m=auto&t=bb7FCRxpvY6sHhD2-6)
- ✅ [Tauler de Trello](https://trello.com/invite/b/67d2bb31bdeeafab5739043d/ATTI01a1de41486edc7420d39504157a257eDFF8C943/proyecte4km)

---

## 👤 Autor

David Escarré González  
🚴 [Memòria Tècnica (PDF)](Memoria_Tecnica.pdf)
