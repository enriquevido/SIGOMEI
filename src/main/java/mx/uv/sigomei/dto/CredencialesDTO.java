package mx.uv.sigomei.dto;

import java.io.Serializable;
import java.util.Objects;

public class CredencialesDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String usuario;
    private String password;

    public CredencialesDTO() {
    }

    public CredencialesDTO(String usuario, String password) {
        this.usuario = usuario;
        this.password = password;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CredencialesDTO that)) {
            return false;
        }
        return Objects.equals(usuario, that.usuario)
                && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuario, password);
    }
}
