# Implementación de UserDetails en Autenticación JWT

## 📋 Resumen

Se ha implementado exitosamente `UserDetails` de Spring Security en tu proyecto de autenticación JWT. Esta implementación mejora la integración con Spring Security y resuelve el problema de múltiples beans de `IPasswordEncryptionPort`.

## 🔧 Componentes Implementados

### 1. **UserDetailsImpl.java**
```
Ubicación: infrastructure/output/security/UserDetailsImpl.java
```

**Propósito:** Adaptador que envuelve el modelo de dominio `User` para hacerlo compatible con Spring Security.

**Características:**
- Implementa la interfaz `UserDetails` de Spring Security
- Mapea el rol del dominio a `GrantedAuthority`
- Usa el email como username
- Permite acceder al objeto `User` del dominio mediante `getUser()`

**Ejemplo de uso:**
```java
UserDetailsImpl userDetails = new UserDetailsImpl(user);
String email = userDetails.getUsername(); // user.getEmail()
Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities(); // ROLE_ADMIN, etc.
```

---

### 2. **CustomUserDetailsService.java**
```
Ubicación: infrastructure/output/security/CustomUserDetailsService.java
```

**Propósito:** Implementación de `UserDetailsService` que carga usuarios desde la base de datos.

**Características:**
- Implementa `UserDetailsService` de Spring Security
- Carga usuarios por email usando `IUserPersistencePort`
- Carga información completa del rol usando `IRolePersistencePort`
- Lanza `UsernameNotFoundException` si el usuario no existe
- Retorna `UserDetailsImpl` con toda la información del usuario

**Flujo de trabajo:**
```
1. Spring Security llama a loadUserByUsername(email)
2. Busca el usuario en la BD usando IUserPersistencePort
3. Si no existe → lanza UsernameNotFoundException
4. Carga el rol completo del usuario
5. Retorna UserDetailsImpl con el usuario cargado
```

---

### 3. **JwtAuthenticationFilter.java** (Actualizado)
```
Ubicación: infrastructure/output/security/JwtAuthenticationFilter.java
```

**Cambios realizados:**
- ✅ Ahora inyecta `UserDetailsService`
- ✅ Carga los detalles del usuario desde la base de datos en cada petición
- ✅ Establece `UserDetails` como principal en la autenticación
- ✅ Añade detalles de la petición web usando `WebAuthenticationDetailsSource`

**Flujo antes:**
```
Token → Extraer email y rol → Crear authorities manualmente → Autenticar con email string
```

**Flujo ahora:**
```
Token → Validar → Extraer email → Cargar UserDetails desde BD → Autenticar con UserDetails completo
```

---

### 4. **SecurityConfig.java** (Actualizado)
```
Ubicación: infrastructure/security/SecurityConfig.java
```

**Cambios realizados:**
- ✅ Inyecta `UserDetailsService` y `PasswordEncoder`
- ✅ Configura `AuthenticationProvider` con `DaoAuthenticationProvider`
- ✅ Expone `AuthenticationManager` como bean
- ✅ Integra el `authenticationProvider()` en la cadena de seguridad

**Nuevos beans:**

**a) authenticationProvider():**
- Usa `DaoAuthenticationProvider`
- Configura `UserDetailsService` para cargar usuarios
- Configura `PasswordEncoder` para validar contraseñas

**b) authenticationManager():**
- Expone el `AuthenticationManager` de Spring Security
- Se puede usar para autenticación programática (ej: login)

---

### 5. **AuthenticationAdapter.java** (Corregido)
```
Ubicación: infrastructure/output/security/AuthenticationAdapter.java
```

**Problema resuelto:**
- ❌ **Antes:** Implementaba `IPasswordEncryptionPort` Y lo inyectaba (dependencia circular)
- ✅ **Ahora:** Solo implementa `IJwtProviderPort`
- ✅ No inyecta `IPasswordEncryptionPort`

---

## 🔄 Flujo Completo de Autenticación

### **Login (Autenticación inicial):**
```
1. Usuario envía email/password a /auth/login
2. AuthenticationUseCase recibe las credenciales
3. BCryptPasswordAdapter valida la contraseña
4. Si es válido → AuthenticationAdapter genera JWT token
5. Token se retorna al cliente
```

### **Peticiones Autenticadas (con JWT):**
```
1. Cliente envía petición con token JWT en header Authorization
2. JwtAuthenticationFilter intercepta la petición
3. Extrae y valida el token JWT
4. Obtiene el email del token
5. CustomUserDetailsService carga el usuario desde BD
6. UserDetailsImpl encapsula el usuario del dominio
7. Se crea UsernamePasswordAuthenticationToken con UserDetails
8. Se establece la autenticación en SecurityContext
9. El controlador puede acceder al usuario autenticado
```

---

## 💡 Ventajas de esta Implementación

### ✅ **Integración Completa con Spring Security**
- Usa los mecanismos estándar de Spring Security
- Compatible con otras características de Spring Security
- Mejor manejo de errores y excepciones

### ✅ **Información Actualizada del Usuario**
- Cada petición carga los datos del usuario desde BD
- Si el rol del usuario cambia, se refleja inmediatamente
- No depende solo de la información del token

### ✅ **Arquitectura Hexagonal Mantenida**
- El dominio no conoce Spring Security
- Los adaptadores manejan la integración con el framework
- Separación clara de responsabilidades

### ✅ **Problema de Múltiples Beans Resuelto**
- Solo existe un bean de `IPasswordEncryptionPort`: `BCryptPasswordAdapter`
- `AuthenticationAdapter` solo implementa `IJwtProviderPort`
- No hay dependencias circulares

---

## 📝 Cómo Acceder al Usuario Autenticado en los Controladores

### **Opción 1: Inyectar Authentication**
```java
@GetMapping("/profile")
public ResponseEntity<?> getProfile(Authentication authentication) {
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    User user = userDetails.getUser();
    return ResponseEntity.ok(user);
}
```

### **Opción 2: Usar @AuthenticationPrincipal**
```java
@GetMapping("/profile")
public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    User user = userDetails.getUser();
    return ResponseEntity.ok(user);
}
```

### **Opción 3: Desde SecurityContext**
```java
@GetMapping("/profile")
public ResponseEntity<?> getProfile() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
    User user = userDetails.getUser();
    return ResponseEntity.ok(user);
}
```

---

## 🔒 Configuración de Autorización

Las rutas están configuradas en `SecurityConfig`:

```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/auth/login").permitAll()           // Público
    .requestMatchers("/users/registerOwner").permitAll()  // Público
    .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")  // Solo ADMIN
    .anyRequest().authenticated()                         // Requiere autenticación
)
```

---

## 🧪 Beans de Spring Security

| Bean | Tipo | Propósito |
|------|------|-----------|
| `CustomUserDetailsService` | `UserDetailsService` | Carga usuarios desde BD |
| `BCryptPasswordAdapter` | `IPasswordEncryptionPort` | Encripta/valida passwords |
| `passwordEncoder()` | `PasswordEncoder` | Bean de BCrypt para Spring Security |
| `authenticationProvider()` | `AuthenticationProvider` | Valida credenciales |
| `authenticationManager()` | `AuthenticationManager` | Gestiona autenticación |
| `JwtAuthenticationFilter` | `OncePerRequestFilter` | Filtra peticiones JWT |
| `AuthenticationAdapter` | `IJwtProviderPort` | Genera/valida tokens JWT |

---

## ✅ Estado Actual

- ✅ UserDetails implementado correctamente
- ✅ UserDetailsService cargando usuarios desde BD
- ✅ JwtAuthenticationFilter usando UserDetailsService
- ✅ SecurityConfig configurado con AuthenticationProvider
- ✅ Problema de múltiples beans de IPasswordEncryptionPort resuelto
- ✅ Compilación exitosa sin errores

---

## 📚 Próximos Pasos Recomendados

1. **Agregar manejo de usuarios deshabilitados:**
   - Modificar `UserDetailsImpl.isEnabled()` para usar un campo del dominio

2. **Implementar refresh tokens:**
   - Crear endpoint para renovar tokens
   - Almacenar refresh tokens en BD

3. **Agregar roles dinámicos:**
   - Si un usuario tiene múltiples roles
   - Modificar `getAuthorities()` para retornar lista de roles

4. **Tests de integración:**
   - Probar el flujo completo de autenticación
   - Verificar que los roles funcionan correctamente

---

## 🐛 Solución de Problemas

### **Error: "No AuthenticationProvider found"**
- **Causa:** El `authenticationProvider()` no está configurado
- **Solución:** Ya está implementado en `SecurityConfig`

### **Error: "Access Denied"**
- **Causa:** El usuario no tiene el rol requerido
- **Solución:** Verificar que el rol en BD tenga el prefijo correcto o ajustar `hasAuthority()`

### **Error: "UsernameNotFoundException"**
- **Causa:** El email no existe en la base de datos
- **Solución:** Verificar que el usuario esté registrado correctamente

---

## 📞 Contacto y Soporte

Si tienes dudas sobre la implementación:
1. Revisa los logs para ver el flujo de autenticación
2. Verifica que los roles tengan el prefijo `ROLE_` en las autoridades
3. Asegúrate de que el token JWT sea válido

¡La implementación de UserDetails con JWT está completa y funcionando! 🎉

