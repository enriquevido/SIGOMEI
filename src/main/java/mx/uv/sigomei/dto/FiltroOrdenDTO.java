package mx.uv.sigomei.dto;

import mx.uv.sigomei.enums.EstadoOrden;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class FiltroOrdenDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idEquipo;
    private Long idTecnico;
    private EstadoOrden estadoOrden;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;

    public FiltroOrdenDTO() {
    }

    public FiltroOrdenDTO(Long idEquipo, Long idTecnico, EstadoOrden estadoOrden, LocalDate fechaDesde,
                          LocalDate fechaHasta) {
        this.idEquipo = idEquipo;
        this.idTecnico = idTecnico;
        this.estadoOrden = estadoOrden;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
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

    public EstadoOrden getEstadoOrden() {
        return estadoOrden;
    }

    public void setEstadoOrden(EstadoOrden estadoOrden) {
        this.estadoOrden = estadoOrden;
    }

    public LocalDate getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(LocalDate fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public LocalDate getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(LocalDate fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FiltroOrdenDTO that)) {
            return false;
        }
        return Objects.equals(idEquipo, that.idEquipo)
                && Objects.equals(idTecnico, that.idTecnico)
                && estadoOrden == that.estadoOrden
                && Objects.equals(fechaDesde, that.fechaDesde)
                && Objects.equals(fechaHasta, that.fechaHasta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEquipo, idTecnico, estadoOrden, fechaDesde, fechaHasta);
    }

    @Override
    public String toString() {
        return "FiltroOrdenDTO{"
                + "idEquipo=" + idEquipo
                + ", idTecnico=" + idTecnico
                + ", estadoOrden=" + estadoOrden
                + ", fechaDesde=" + fechaDesde
                + ", fechaHasta=" + fechaHasta
                + '}';
    }
}
