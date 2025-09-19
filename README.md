# PartyUpp 🎉

A group-party Android app that turns hangouts into quick, fun challenges—with photos, player selection, and a clean, modern UI.  
English first • Español después.

---

## English

### 1) What’s inside (Tech stack)
- **Android app (Java)** — Android Studio, Material Components, Fragments, RecyclerView, AlertDialog.
- **Networking** — Retrofit for REST calls.
- **Backend** — **Spring Boot** (REST), **Spring Security** with **JWT** for auth.
- **Database** — **MySQL** with **JPA/Hibernate**.
- **Images** — Uploaded and served by the backend; linked from the app (cloud/Server storage).
- **Version control** — Git/GitHub.

### 2) How it works (Architecture)
- **Client (Android)** calls a **Spring Boot API**.
- On **register/login**, the API returns a **JWT token**.
- The app sends the token in `Authorization: Bearer <token>` headers for authenticated endpoints.
- **Game flow**:
  1) Login/Register (or play as guest).
  2) Home → Start a game.
  3) Select players (+ suggestions).
  4) Take an **initial group photo**.
  5) Play **sequential challenges**.
  6) **Finish**, take a **final photo**, and save to **Gallery**.
- **Data model (simplified)**: Users, Games (with players, timestamps, photos), Challenges.

### 3) Run it locally (short)
- **Backend**: Spring Boot + MySQL ➜ set DB env, run the app (port 8080).  
  JWT is enabled via Spring Security; public endpoints under `/api/v1/noAuth/*`, user endpoints under `/api/v1/user/*`, admin under `/api/v1/admin/*`.
- **Android**: Set the API base URL in Retrofit, build & run from Android Studio.

---

## Screenshots — Step by step

> Images are stored in `PartyUpp/images/`.

1. **Login**  
   ![Screenshot 01 – Login](PartyUpp/images/1.png)  
   _Email + password login. You can also continue as guest._

2. **Register**  
   ![Screenshot 02 – Register](PartyUpp/images/2.png)  
   _Create an account with username, email, and password._

3. **Home – Main menu**  
   ![Screenshot 03 – Home](PartyUpp/images/3.png)  
   _Quick access to Tutorial and Play. Bottom navigation to Gallery/Profile._

4. **Home – Alternate view**  
   ![Screenshot 04 – Home (alt)](PartyUpp/images/4.png)  
   _Same main actions; profile avatar at top right._

5. **Gallery – Past games**  
   ![Screenshot 05 – Gallery](PartyUpp/images/5.png)  
   _List of saved sessions with thumbnail and participants._

6. **Gallery – Alternate view**  
   ![Screenshot 06 – Gallery (alt)](PartyUpp/images/6.png)  
   _Another view of the stored sessions._

7. **Profile & Settings**  
   ![Screenshot 07 – Profile](PartyUpp/images/7.png)  
   _Sound effects, vibration, music, notifications, and logout._

8. **Profile – Alternate view**  
   ![Screenshot 08 – Profile (alt)](PartyUpp/images/8.png)  
   _Same settings with toggles._

9. **Player selection – Suggestions**  
   ![Screenshot 09 – Player selection (suggestions)](PartyUpp/images/9.png)  
   _Type names, use suggested chips, delete players if needed._

10. **Player selection – Added players**  
    ![Screenshot 10 – Player selection (added)](PartyUpp/images/10.png)  
    _Players added; “Continue” enables when requirements are met._

11. **Player selection – Editing names**  
    ![Screenshot 11 – Player selection (editing)](PartyUpp/images/11.png)  
    _Edit player names and manage the list before starting._

12. **Initial photo prompt**  
    ![Screenshot 12 – Initial photo](PartyUpp/images/12.png)  
    _Take a group photo before the game starts._

13. **Initial photo – Alternate**  
    ![Screenshot 13 – Initial photo (alt)](PartyUpp/images/13.png)  
    _Same step with the camera prompt._

14. **Challenge example**  
    ![Screenshot 14 – Challenge](PartyUpp/images/14.png)  
    _A challenge addressed to a specific player; proceed with “Next”._

15. **Challenge – Alternate example**  
    ![Screenshot 15 – Challenge (alt)](PartyUpp/images/15.png)  
    _Another challenge view during the game._

16. **All challenges completed**  
    ![Screenshot 16 – Completed](PartyUpp/images/16.png)  
    _End-of-round message after finishing the sequence._

17. **Completed – Alternate 1**  
    ![Screenshot 17 – Completed (alt 1)](PartyUpp/images/17.png)  
    _Same completion state, variant screen._

18. **Completed – Alternate 2**  
    ![Screenshot 18 – Completed (alt 2)](PartyUpp/images/18.png)  
    _Another completion variant._

19. **Final photo prompt (cropped preview)**  
    ![Screenshot 19 – Final photo (cropped)](PartyUpp/images/19.png)  
    _Take a last photo before saving to the gallery._

20. **Final photo prompt**  
    ![Screenshot 20 – Final photo](PartyUpp/images/20.png)  
    _Confirm and proceed to save._

21. **Final photo – Alternate 1**  
    ![Screenshot 21 – Final photo (alt 1)](PartyUpp/images/21.png)  
    _Same final step, alternate view._

22. **Final photo – Alternate 2**  
    ![Screenshot 22 – Final photo (alt 2)](PartyUpp/images/22.png)  
    _Finish and store the session in Gallery._

---

## Roadmap (short)
- More challenge packs & categories
- Shareable results and stats
- iOS client (future)
- CI/CD & analytics

## Responsible fun
PartyUpp promotes fun, **not** irresponsible behavior. Please play safely and respectfully.

---

## Español

### 1) Tecnologías utilizadas
- **App Android (Java)** — Android Studio, Material Components, Fragments, RecyclerView, AlertDialog.
- **Red/HTTP** — Retrofit para llamadas REST.
- **Backend** — **Spring Boot** (REST), **Spring Security** con **JWT** para autenticación.
- **Base de datos** — **MySQL** con **JPA/Hibernate**.
- **Imágenes** — Cargadas y servidas por el backend; enlazadas desde la app.
- **Control de versiones** — Git/GitHub.

### 2) Cómo funciona (Arquitectura)
- **Cliente (Android)** que consume una **API Spring Boot**.
- Al **registrar/iniciar sesión**, la API devuelve un **token JWT**.
- La app envía el token en `Authorization: Bearer <token>` para los endpoints protegidos.
- **Flujo del juego**:
  1) Login/Registro (o invitado).
  2) Inicio → Jugar.
  3) Selección de jugadores (+ sugerencias).
  4) **Foto inicial** del grupo.
  5) **Desafíos secuenciales**.
  6) **Final**, **foto final** y guardado en **Galería**.
- **Modelo de datos (resumen)**: Usuarios, Partidas (jugadores, fechas, fotos), Desafíos.

### 3) Ejecutar en local (corto)
- **Backend**: Spring Boot + MySQL ➜ configura la BD y ejecuta (puerto 8080).  
  JWT con Spring Security; endpoints públicos en `/api/v1/noAuth/*`, de usuario en `/api/v1/user/*`, admin en `/api/v1/admin/*`.
- **Android**: Pon la URL base de la API en Retrofit, compila y ejecuta en Android Studio.

---

## Capturas — Paso a paso

> Las imágenes están en `PartyUpp/images/`.

1. **Inicio de sesión**  
   ![Captura 01 – Login](PartyUpp/images/1.png)  
   _Acceso con email y contraseña o como invitado._

2. **Registro**  
   ![Captura 02 – Registro](PartyUpp/images/2.png)  
   _Crea tu cuenta con nombre de usuario, email y contraseña._

3. **Inicio – Menú principal**  
   ![Captura 03 – Inicio](PartyUpp/images/3.png)  
   _Acceso rápido a Tutorial y Jugar. Navegación inferior a Galería/Perfil._

4. **Inicio – Vista alternativa**  
   ![Captura 04 – Inicio (alt)](PartyUpp/images/4.png)  
   _Mismas acciones; avatar de perfil arriba a la derecha._

5. **Galería – Partidas guardadas**  
   ![Captura 05 – Galería](PartyUpp/images/5.png)  
   _Lista de sesiones con miniatura y participantes._

6. **Galería – Vista alternativa**  
   ![Captura 06 – Galería (alt)](PartyUpp/images/6.png)  
   _Otra vista de partidas almacenadas._

7. **Perfil y Ajustes**  
   ![Captura 07 – Perfil](PartyUpp/images/7.png)  
   _Efectos, vibración, música, notificaciones y cerrar sesión._

8. **Perfil – Vista alternativa**  
   ![Captura 08 – Perfil (alt)](PartyUpp/images/8.png)  
   _Mismas opciones con interruptores._

9. **Selección de jugadores – Sugerencias**  
   ![Captura 09 – Selección (sugerencias)](PartyUpp/images/9.png)  
   _Escribe nombres, usa chips sugeridos y elimina jugadores._

10. **Selección de jugadores – Añadidos**  
    ![Captura 10 – Selección (añadidos)](PartyUpp/images/10.png)  
    _Jugadores añadidos; “Continuar” se habilita al cumplir requisitos._

11. **Selección de jugadores – Edición**  
    ![Captura 11 – Selección (edición)](PartyUpp/images/11.png)  
    _Edita y organiza la lista antes de empezar._

12. **Foto inicial**  
    ![Captura 12 – Foto inicial](PartyUpp/images/12.png)  
    _Haz una foto del grupo antes de comenzar._

13. **Foto inicial – Alternativa**  
    ![Captura 13 – Foto inicial (alt)](PartyUpp/images/13.png)  
    _Mismo paso con el icono de cámara._

14. **Desafío de ejemplo**  
    ![Captura 14 – Desafío](PartyUpp/images/14.png)  
    _Reto para un jugador; avanza con “Siguiente”._

15. **Desafío – Alternativa**  
    ![Captura 15 – Desafío (alt)](PartyUpp/images/15.png)  
    _Otra vista de reto durante la partida._

16. **Desafíos completados**  
    ![Captura 16 – Completado](PartyUpp/images/16.png)  
    _Mensaje de fin de secuencia._

17. **Completado – Alternativa 1**  
    ![Captura 17 – Completado (alt 1)](PartyUpp/images/17.png)

18. **Completado – Alternativa 2**  
    ![Captura 18 – Completado (alt 2)](PartyUpp/images/18.png)

19. **Foto final (vista recortada)**  
    ![Captura 19 – Foto final (recorte)](PartyUpp/images/19.png)  
    _Haz la última foto antes de guardar._

20. **Foto final**  
    ![Captura 20 – Foto final](PartyUpp/images/20.png)  
    _Confirma y guarda en la galería._

21. **Foto final – Alternativa 1**  
    ![Captura 21 – Foto final (alt 1)](PartyUpp/images/21.png)

22. **Foto final – Alternativa 2**  
    ![Captura 22 – Foto final (alt 2)](PartyUpp/images/22.png)

---

## Hoja de ruta (breve)
- Más packs y categorías de desafíos
- Resultados/estadísticas compartibles
- Cliente iOS (futuro)
- CI/CD y analíticas

## Diversión responsable
PartyUpp fomenta la diversión **sin** conductas irresponsables. Juega con cabeza y respeto.

---
