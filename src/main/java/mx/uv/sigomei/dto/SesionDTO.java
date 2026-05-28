package mx.uv.sigomei.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class SesionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String token;
    private String rol;
    private LocalDateTime expiracion;

    public SesionDTO() {
    }

    public SesionDTO(String token, String rol, LocalDateTime expiracion) {
        this.token = token;
        this.rol = rol;
        this.expiracion = expiracion;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public LocalDateTime getExpiracion() {
        return expiracion;
    }

    public void setExpiracion(LocalDateTime expiracion) {
        this.expiracion = expiracion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SesionDTO sesionDTO)) {
            return false;
        }
        return Objects.equals(token, sesionDTO.token)
                && Objects.equals(rol, sesionDTO.rol)
                && Objects.equals(expiracion, sesionDTO.expiracion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, rol, expiracion);
    }
}
