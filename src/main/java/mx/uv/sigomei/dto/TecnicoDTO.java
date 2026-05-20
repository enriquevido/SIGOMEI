package mx.uv.sigomei.dto;

import mx.uv.sigomei.enums.EstatusTecnico;
import mx.uv.sigomei.enums.NivelCertificacion;
import mx.uv.sigomei.enums.TipoEquipo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class TecnicoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idTecnico;
    private String nombreCompleto;
    private String rfc;
    private String telefono;
    private String correo;
    private TipoEquipo especialidad;
    private NivelCertificacion nivelCertificacion;
    private LocalDate fechaIngreso;
    private EstatusTecnico estatus;

    public TecnicoDTO() {
    }

    public TecnicoDTO(Long idTecnico, String nombreCompleto, String rfc, String telefono, String correo,
                      TipoEquipo especialidad, NivelCertificacion nivelCertificacion, LocalDate fechaIngreso,
                      EstatusTecnico estatus) {
        this.idTecnico = idTecnico;
        this.nombreCompleto = nombreCompleto;
        this.rfc = rfc;
        this.telefono = telefono;
        this.correo = correo;
        this.especialidad = especialidad;
        this.nivelCertificacion = nivelCertificacion;
        this.fechaIngreso = fechaIngreso;
        this.estatus = estatus;
    }

    public Long getIdTecnico() {
        return idTecnico;
    }

    public void setIdTecnico(Long idTecnico) {
        this.idTecnico = idTecnico;
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
        if (!(o instanceof TecnicoDTO that)) {
            return false;
        }
        return Objects.equals(idTecnico, that.idTecnico)
                && Objects.equals(nombreCompleto, that.nombreCompleto)
                && Objects.equals(rfc, that.rfc)
                && Objects.equals(telefono, that.telefono)
                && Objects.equals(correo, that.correo)
                && especialidad == that.especialidad
                && nivelCertificacion == that.nivelCertificacion
                && Objects.equals(fechaIngreso, that.fechaIngreso)
                && estatus == that.estatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTecnico, nombreCompleto, rfc, telefono, correo, especialidad, nivelCertificacion,
                fechaIngreso, estatus);
    }

    @Override
    public String toString() {
        return "TecnicoDTO{"
                + "idTecnico=" + idTecnico
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
