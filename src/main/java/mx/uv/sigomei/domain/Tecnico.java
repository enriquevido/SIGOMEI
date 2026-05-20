package mx.uv.sigomei.domain;

import mx.uv.sigomei.enums.EstatusTecnico;
import mx.uv.sigomei.enums.NivelCertificacion;
import mx.uv.sigomei.enums.TipoEquipo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Tecnico implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String nombreCompleto;
    private String rfc;
    private String telefono;
    private String correo;
    private TipoEquipo especialidad;
    private NivelCertificacion nivelCertificacion;
    private LocalDate fechaIngreso;
    private EstatusTecnico estatus;

    public Tecnico() {
    }

    public Tecnico(Long id, String nombreCompleto, String rfc, String telefono, String correo,
                   TipoEquipo especialidad, NivelCertificacion nivelCertificacion, LocalDate fechaIngreso,
                   EstatusTecnico estatus) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.rfc = rfc;
        this.telefono = telefono;
        this.correo = correo;
        this.especialidad = especialidad;
        this.nivelCertificacion = nivelCertificacion;
        this.fechaIngreso = fechaIngreso;
        this.estatus = estatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public TipoEquipo getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(TipoEquipo especialidad) {
        this.especialidad = especialidad;
    }

    public NivelCertificacion getNivelCertificacion() {
        return nivelCertificacion;
    }

    public void setNivelCertificacion(NivelCertificacion nivelCertificacion) {
        this.nivelCertificacion = nivelCertificacion;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public EstatusTecnico getEstatus() {
        return estatus;
    }

    public void setEstatus(EstatusTecnico estatus) {
        this.estatus = estatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tecnico tecnico)) {
            return false;
        }
        return Objects.equals(id, tecnico.id)
                && Objects.equals(nombreCompleto, tecnico.nombreCompleto)
                && Objects.equals(rfc, tecnico.rfc)
                && Objects.equals(telefono, tecnico.telefono)
                && Objects.equals(correo, tecnico.correo)
                && especialidad == tecnico.especialidad
                && nivelCertificacion == tecnico.nivelCertificacion
                && Objects.equals(fechaIngreso, tecnico.fechaIngreso)
                && estatus == tecnico.estatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombreCompleto, rfc, telefono, correo, especialidad, nivelCertificacion,
                fechaIngreso, estatus);
    }

    @Override
    public String toString() {
        return "Tecnico{"
                + "id=" + id
                + ", nombreCompleto='" + nombreCompleto + '\''
                + ", rfc='" + rfc + '\''
                + ", telefono='" + telefono + '\''
                + ", correo='" + correo + '\''
                + ", especialidad=" + especialidad
                + ", nivelCertificacion=" + nivelCertificacion
                + ", fechaIngreso=" + fechaIngreso
                + ", estatus=" + estatus
                + '}';
    }
}
