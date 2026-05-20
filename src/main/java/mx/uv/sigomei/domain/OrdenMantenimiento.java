package mx.uv.sigomei.domain;

import mx.uv.sigomei.enums.EstadoOrden;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class OrdenMantenimiento implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Equipo equipo;
    private Tecnico tecnico;
    private String tipoMantenimiento;
    private LocalDate fechaProgramada;
    private LocalDate fechaInicio;
    private LocalDate fechaCierre;
    private String descripcionTrabajo;
    private BigDecimal costoEstimado;
    private BigDecimal costoReal;
    private EstadoOrden estadoOrden;

    public OrdenMantenimiento() {
    }

    public OrdenMantenimiento(Long id, Equipo equipo, Tecnico tecnico, String tipoMantenimiento,
                              LocalDate fechaProgramada, LocalDate fechaInicio, LocalDate fechaCierre,
                              String descripcionTrabajo, BigDecimal costoEstimado, BigDecimal costoReal,
                              EstadoOrden estadoOrden) {
        this.id = id;
        this.equipo = equipo;
        this.tecnico = tecnico;
        this.tipoMantenimiento = tipoMantenimiento;
        this.fechaProgramada = fechaProgramada;
        this.fechaInicio = fechaInicio;
        this.fechaCierre = fechaCierre;
        this.descripcionTrabajo = descripcionTrabajo;
        this.costoEstimado = costoEstimado;
        this.costoReal = costoReal;
        this.estadoOrden = estadoOrden;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public Tecnico getTecnico() {
        return tecnico;
    }

    public void setTecnico(Tecnico tecnico) {
        this.tecnico = tecnico;
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
        if (!(o instanceof OrdenMantenimiento that)) {
            return false;
        }
        return Objects.equals(id, that.id)
                && Objects.equals(equipo, that.equipo)
                && Objects.equals(tecnico, that.tecnico)
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
        return Objects.hash(id, equipo, tecnico, tipoMantenimiento, fechaProgramada, fechaInicio, fechaCierre,
                descripcionTrabajo, costoEstimado, costoReal, estadoOrden);
    }

    @Override
    public String toString() {
        return "OrdenMantenimiento{"
                + "id=" + id
                + ", equipo=" + equipo
                + ", tecnico=" + tecnico
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
