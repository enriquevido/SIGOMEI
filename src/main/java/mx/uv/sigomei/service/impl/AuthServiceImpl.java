package mx.uv.sigomei.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.mindrot.jbcrypt.BCrypt;

import mx.uv.sigomei.dto.CredencialesDTO;
import mx.uv.sigomei.dto.SesionDTO;
import mx.uv.sigomei.exception.AutenticacionException;
import mx.uv.sigomei.exception.CuentaBloqueadaException;
import mx.uv.sigomei.exception.ValidacionException;
import mx.uv.sigomei.repository.jdbc.JdbcUsuarioRepository;
import mx.uv.sigomei.repository.jdbc.JdbcUsuarioRepository.UsuarioRow;
import mx.uv.sigomei.service.AuthService;

public class AuthServiceImpl implements AuthService {
    private static final int MAX_INTENTOS_FALLIDOS = 5;
    private static final int MINUTOS_BLOQUEO = 15;
    private static final int HORAS_SESION = 8;

    private final JdbcUsuarioRepository usuarioRepository;
    private final ConcurrentMap<String, SesionDTO> sesiones = new ConcurrentHashMap<>();

    public AuthServiceImpl(JdbcUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public SesionDTO login(CredencialesDTO credenciales) {
        validarCredenciales(credenciales);
        limpiarSesionesExpiradas();

        UsuarioRow usuario = usuarioRepository.findByUsername(credenciales.getUsuario())
                .orElseThrow(() -> new AutenticacionException("Usuario o password invalidos"));

        LocalDateTime ahora = LocalDateTime.now();
        if (usuario.bloqueadoHasta() != null && usuario.bloqueadoHasta().isAfter(ahora)) {
            throw new CuentaBloqueadaException("Cuenta bloqueada hasta: " + usuario.bloqueadoHasta());
        }
        usuario = desbloquearSiExpirado(usuario);

        if (!BCrypt.checkpw(credenciales.getPassword(), usuario.passwordHash())) {
            registrarIntentoFallido(usuario, ahora);
        }

        usuarioRepository.resetearIntentos(usuario.username());

        SesionDTO sesion = new SesionDTO(
                UUID.randomUUID().toString(),
                usuario.rol(),
                ahora.plusHours(HORAS_SESION)
        );
        sesiones.put(sesion.getToken(), sesion);
        return sesion;
    }

    @Override
    public void logout(String token) {
        if (token != null) {
            sesiones.remove(token);
        }
    }

    private void validarCredenciales(CredencialesDTO credenciales) {
        if (credenciales == null) {
            throw new ValidacionException("Las credenciales son obligatorias");
        }
        if (esBlanco(credenciales.getUsuario())) {
            throw new ValidacionException("El usuario es obligatorio");
        }
        if (esBlanco(credenciales.getPassword())) {
            throw new ValidacionException("El password es obligatorio");
        }
    }

    private void registrarIntentoFallido(UsuarioRow usuario, LocalDateTime ahora) {
        int intentos = usuario.intentosFallidos() + 1;
        if (intentos >= MAX_INTENTOS_FALLIDOS) {
            LocalDateTime bloqueadoHasta = ahora.plusMinutes(MINUTOS_BLOQUEO);
            usuarioRepository.actualizarIntentos(usuario.username(), intentos, bloqueadoHasta);
            throw new CuentaBloqueadaException("Cuenta bloqueada hasta: " + bloqueadoHasta);
        }

        usuarioRepository.actualizarIntentos(usuario.username(), intentos, null);
        throw new AutenticacionException("Usuario o password invalidos");
    }

    private void limpiarSesionesExpiradas() {
        LocalDateTime ahora = LocalDateTime.now();
        sesiones.entrySet().removeIf(entry -> entry.getValue().getExpiracion().isBefore(ahora));
    }

    private UsuarioRow desbloquearSiExpirado(UsuarioRow usuario) {
        if (usuario.bloqueadoHasta() == null) {
            return usuario;
        }

        usuarioRepository.resetearIntentos(usuario.username());
        return new UsuarioRow(
                usuario.idUsuario(),
                usuario.username(),
                usuario.passwordHash(),
                usuario.rol(),
                0,
                null
        );
    }

    private boolean esBlanco(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
