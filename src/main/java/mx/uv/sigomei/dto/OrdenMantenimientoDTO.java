package mx.uv.sigomei.dto;

import mx.uv.sigomei.enums.EstadoOrden;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class OrdenMantenimientoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idOrden;
    private Long idEquipo;
    private Long idTecnico;
    private String tipoMantenimiento;
    private LocalDate fechaProgramada;
    private LocalDate fechaInicio;
    private LocalDate fechaCierre;
    private String descripcionTrabajo;
    private BigDecimal costoEstimado;
    private BigDecimal costoReal;
    private EstadoOrden estadoOrden;

    public OrdenMantenimientoDTO() {
    }

    public OrdenMantenimientoDTO(Long idOrden, Long idEquipo, Long idTecnico, String tipoMantenimiento,
                                 LocalDate fechaProgramada, LocalDate fechaInicio, LocalDate fechaCierre,
                                 String descripcionTrabajo, BigDecimal costoEstimado, BigDecimal costoReal,
                                 EstadoOrden estadoOrden) {
        this.idOrden = idOrden;
        this.idEquipo = idEquipo;
        this.idTecnico = idTecnico;
        this.tipoMantenimiento = tipoMantenimiento;
        this.fechaProgramada = fechaProgramada;
        this.fechaInicio = fechaInicio;
        this.fechaCierre = fechaCierre;
        this.descripcionTrabajo = descripcionTrabajo;
        this.costoEstimado = costoEstimado;
        this.costoReal = costoReal;
        this.estadoOrden = estadoOrden;
    }

    public Long getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(Long idOrden) {
        this.idOrden = idOrden;
    }

    public Long getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(Long idEquipo) {
        this.idEquipo = idEquipo;
    }

    public Long getIdTecnico() {
        return idTecnico;
    }

    public void setIdTecnico(Long idTecnico) {
        this.idTecnico = idTecnico;
    }

    public String getTipoMantenimiento() {
        return tipoMantenimiento;
    }

    public void setTipoMantenimiento(String tipoMantenimiento) {
        this.tipoMantenimiento = tipoMantenimiento;
    }

    public LocalDate getFechaProgramada() {
        return fechaProgramada;
    }

    public void setFechaProgramada(LocalDate fechaProgramada) {
        this.fechaProgramada = fechaProgramada;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(LocalDate fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public String getDescripcionTrabajo() {
        return descripcionTrabajo;
    }

    public void setDescripcionTrabajo(String descripcionTrabajo) {
        this.descripcionTrabajo = descripcionTrabajo;
    }

    public BigDecimal getCostoEstimado() {
        return costoEstimado;
    }

    public void setCostoEstimado(BigDecimal costoEstimado) {
        this.costoEstimado = costoEstimado;
    }

    public BigDecimal getCostoReal() {
        return costoReal;
    }

    public void setCostoReal(BigDecimal costoReal) {
        this.costoReal = costoReal;
    }

    public EstadoOrden getEstadoOrden() {
        return estadoOrden;
    }

    public void setEstadoOrden(EstadoOrden estadoOrden) {
        this.estadoOrden = estadoOrden;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrdenMantenimientoDTO that)) {
            return false;
        }
        return Objects.equals(idOrden, that.idOrden)
                && Objects.equals(idEquipo, that.idEquipo)
                && Objects.equals(idTecnico, that.idTecnico)
                && Objects.equals(tipoMantenimiento, that.tipoMantenimiento)
                && Objects.equals(fechaProgramada, that.fechaProgramada)
                && Objects.equals(fechaInicio, that.fechaInicio)
                && Objects.equals(fechaCierre, that.fechaCierre)
                && Objects.equals(descripcionTrabajo, that.descripcionTrabajo)
                && Objects.equals(costoEstimado, that.costoEstimado)
                && Objects.equals(costoReal, that.costoReal)
                && estadoOrden == that.estadoOrden;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOrden, idEquipo, idTecnico, tipoMantenimiento, fechaProgramada, fechaInicio,
                fechaCierre, descripcionTrabajo, costoEstimado, costoReal, estadoOrden);
    }

    @Override
    public String toString() {
        return "OrdenMantenimientoDTO{"
                + "idOrden=" + idOrden
                + ", idEquipo=" + idEquipo
                + ", idTecnico=" + idTecnico
                + ", tipoMantenimiento='" + tipoMantenimiento + '\''
                + ", fechaProgramada=" + fechaProgramada
                + ", fechaInicio=" + fechaInicio
                + ", fechaCierre=" + fechaCierre
                + ", descripcionTrabajo='" + descripcionTrabajo + '\''
                + ", costoEstimado=" + costoEstimado
                + ", costoReal=" + costoReal
                + ", estadoOrden=" + estadoOrden
                + '}';
    }
}
