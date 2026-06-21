package com.hoteltransilvania.usuarios.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.hoteltransilvania.usuarios.dto.UsuarioDTO;
import com.hoteltransilvania.usuarios.exception.ResourceNotFoundException;
import com.hoteltransilvania.usuarios.model.UsuarioModel;
import com.hoteltransilvania.usuarios.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    @DisplayName("Debe retornar usuario cuando el ID existe")
    void shouldReturnUsuarioWhenIdExists() {

        // Given: simulamos un usuario existente.
        UsuarioModel usuario = new UsuarioModel();
        usuario.setId(1L);
        usuario.setNombre("Marco");
        usuario.setUsername("marco");

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        // When: ejecutamos el método real del service.
        UsuarioModel resultado = usuarioService.buscarPorId(1L);

        // Then: validamos el resultado.
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Marco", resultado.getNombre());
        assertEquals("marco", resultado.getUsername());

        verify(usuarioRepository).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el usuario no existe")
    void shouldThrowExceptionWhenUsuarioDoesNotExist() {

        // Given: simulamos que no existe usuario con ID 99.
        when(usuarioRepository.findById(99L))
                .thenReturn(Optional.empty());

        // When / Then: esperamos una excepción.
        assertThrows(
                ResourceNotFoundException.class,
                () -> usuarioService.buscarPorId(99L)
        );

        verify(usuarioRepository).findById(99L);
    }

    @Test
    @DisplayName("Debe listar todos los usuarios")
    void shouldListAllUsuarios() {

        // Given: simulamos una lista de usuarios.
        UsuarioModel usuario1 = new UsuarioModel();
        usuario1.setId(1L);
        usuario1.setNombre("Marco");

        UsuarioModel usuario2 = new UsuarioModel();
        usuario2.setId(2L);
        usuario2.setNombre("Mavis");

        when(usuarioRepository.findAll())
                .thenReturn(List.of(usuario1, usuario2));

        // When: ejecutamos listar().
        List<UsuarioModel> resultado = usuarioService.listar();

        // Then: validamos la lista.
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Marco", resultado.get(0).getNombre());
        assertEquals("Mavis", resultado.get(1).getNombre());

        verify(usuarioRepository).findAll();
    }

    @Test
    @DisplayName("Debe eliminar usuario cuando el ID existe")
    void shouldDeleteUsuarioWhenIdExists() {

        // Given: simulamos que el usuario existe.
        UsuarioModel usuario = new UsuarioModel();
        usuario.setId(1L);
        usuario.setNombre("Marco");

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        doNothing().when(usuarioRepository).delete(usuario);

        // When: ejecutamos eliminar().
        usuarioService.eliminar(1L);

        // Then: verificamos búsqueda y eliminación.
        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).delete(usuario);
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar usuario inexistente")
    void shouldThrowExceptionWhenDeleteUsuarioDoesNotExist() {

        // Given: simulamos que no existe usuario.
        when(usuarioRepository.findById(99L))
                .thenReturn(Optional.empty());

        // When / Then: esperamos excepción.
        assertThrows(
                ResourceNotFoundException.class,
                () -> usuarioService.eliminar(99L)
        );

        verify(usuarioRepository).findById(99L);
        verify(usuarioRepository, never()).delete(any(UsuarioModel.class));
    }

    @Test
    @DisplayName("Debe crear usuario codificando la contraseña")
    void shouldCreateUsuarioWithEncodedPassword() {

        // Given: usuario con password normal.
        UsuarioModel usuario = new UsuarioModel();
        usuario.setId(1L);
        usuario.setNombre("Marco");
        usuario.setUsername("marco");
        usuario.setPassword("1234");

        when(passwordEncoder.encode("1234"))
                .thenReturn("password_codificada");

        when(usuarioRepository.save(any(UsuarioModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When: ejecutamos crear().
        UsuarioModel resultado = usuarioService.crear(usuario);

        // Then: validamos que la password fue codificada.
        assertNotNull(resultado);
        assertEquals("Marco", resultado.getNombre());
        assertEquals("marco", resultado.getUsername());
        assertEquals("password_codificada", resultado.getPassword());

        verify(passwordEncoder).encode("1234");
        verify(usuarioRepository).save(usuario);
    }

    @Test
    @DisplayName("Debe actualizar usuario existente")
    void shouldUpdateUsuarioWhenIdExists() {

        // Given: usuario existente.
        UsuarioModel usuarioExistente = new UsuarioModel();
        usuarioExistente.setId(1L);
        usuarioExistente.setNombre("Marco");
        usuarioExistente.setCorreo("antiguo@hotel.com");
        usuarioExistente.setPassword("password_antigua");

        // Given: nuevos datos desde DTO.
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre("Mavis");
        dto.setCorreo("mavis@hotel.com");
        dto.setPassword("1234");

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuarioExistente));

        when(passwordEncoder.encode("1234"))
                .thenReturn("password_nueva_codificada");

        when(usuarioRepository.save(any(UsuarioModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When: ejecutamos actualizar().
        UsuarioModel resultado = usuarioService.actualizar(1L, dto);

        // Then: validamos los datos modificados.
        assertNotNull(resultado);
        assertEquals("Mavis", resultado.getNombre());
        assertEquals("mavis@hotel.com", resultado.getCorreo());
        assertEquals("password_nueva_codificada", resultado.getPassword());

        verify(usuarioRepository).findById(1L);
        verify(passwordEncoder).encode("1234");
        verify(usuarioRepository).save(usuarioExistente);
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar usuario inexistente")
    void shouldThrowExceptionWhenUpdateUsuarioDoesNotExist() {

        // Given: DTO válido, pero ID inexistente.
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre("Mavis");
        dto.setCorreo("mavis@hotel.com");
        dto.setPassword("1234");

        when(usuarioRepository.findById(99L))
                .thenReturn(Optional.empty());

        // When / Then: esperamos excepción.
        assertThrows(
                ResourceNotFoundException.class,
                () -> usuarioService.actualizar(99L, dto)
        );

        verify(usuarioRepository).findById(99L);
        verify(usuarioRepository, never()).save(any(UsuarioModel.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    @DisplayName("Debe permitir login cuando usuario está activo y contraseña es correcta")
    void shouldLoginWhenUserIsActiveAndPasswordIsCorrect() {

        // Given: usuario activo con contraseña codificada.
        UsuarioModel usuario = new UsuarioModel();
        usuario.setId(1L);
        usuario.setUsername("marco");
        usuario.setPassword("password_codificada");
        usuario.setActivo(true);

        when(usuarioRepository.findByUsername("marco"))
                .thenReturn(Optional.of(usuario));

        when(passwordEncoder.matches("1234", "password_codificada"))
                .thenReturn(true);

        // When: ejecutamos login().
        UsuarioModel resultado = usuarioService.login("marco", "1234");

        // Then: validamos login correcto.
        assertNotNull(resultado);
        assertEquals("marco", resultado.getUsername());
        assertTrue(resultado.isActivo());

        verify(usuarioRepository).findByUsername("marco");
        verify(passwordEncoder).matches("1234", "password_codificada");
    }

    @Test
    @DisplayName("Debe lanzar excepción si el usuario no existe al hacer login")
    void shouldThrowExceptionWhenLoginUserDoesNotExist() {

        // Given: usuario no encontrado.
        when(usuarioRepository.findByUsername("desconocido"))
                .thenReturn(Optional.empty());

        // When / Then: esperamos excepción.
        assertThrows(
                ResourceNotFoundException.class,
                () -> usuarioService.login("desconocido", "1234")
        );

        verify(usuarioRepository).findByUsername("desconocido");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el usuario está inactivo")
    void shouldThrowExceptionWhenLoginUserIsInactive() {

        // Given: usuario existe, pero está inactivo.
        UsuarioModel usuario = new UsuarioModel();
        usuario.setId(1L);
        usuario.setUsername("marco");
        usuario.setPassword("password_codificada");
        usuario.setActivo(false);

        when(usuarioRepository.findByUsername("marco"))
                .thenReturn(Optional.of(usuario));

        // When / Then: esperamos excepción.
        assertThrows(
                ResourceNotFoundException.class,
                () -> usuarioService.login("marco", "1234")
        );

        verify(usuarioRepository).findByUsername("marco");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("Debe lanzar excepción si la contraseña es incorrecta")
    void shouldThrowExceptionWhenPasswordIsIncorrect() {

        // Given: usuario activo, pero contraseña incorrecta.
        UsuarioModel usuario = new UsuarioModel();
        usuario.setId(1L);
        usuario.setUsername("marco");
        usuario.setPassword("password_codificada");
        usuario.setActivo(true);

        when(usuarioRepository.findByUsername("marco"))
                .thenReturn(Optional.of(usuario));

        when(passwordEncoder.matches("incorrecta", "password_codificada"))
                .thenReturn(false);

        // When / Then: esperamos excepción.
        assertThrows(
                ResourceNotFoundException.class,
                () -> usuarioService.login("marco", "incorrecta")
        );

        verify(usuarioRepository).findByUsername("marco");
        verify(passwordEncoder).matches("incorrecta", "password_codificada");
    }
}