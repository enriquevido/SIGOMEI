package mx.uv.sigomei.dto;

import mx.uv.sigomei.enums.Criticidad;
import mx.uv.sigomei.enums.TipoEquipo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class EquipoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idEquipo;
    private String nombre;
    private TipoEquipo tipo;
    private String marca;
    private String modelo;
    private String numeroSerie;
    private String ubicacionPlanta;
    private LocalDate fechaInstalacion;
    private String estadoOperativo;
    private Criticidad criticidad;

    public EquipoDTO() {
    }

    public EquipoDTO(Long idEquipo, String nombre, TipoEquipo tipo, String marca, String modelo, String numeroSerie,
                     String ubicacionPlanta, LocalDate fechaInstalacion, String estadoOperativo,
                     Criticidad criticidad) {
        this.idEquipo = idEquipo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.marca = marca;
        this.modelo = modelo;
        this.numeroSerie = numeroSerie;
        this.ubicacionPlanta = ubicacionPlanta;
        this.fechaInstalacion = fechaInstalacion;
        this.estadoOperativo = estadoOperativo;
        this.criticidad = criticidad;
    }

    public Long getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(Long idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoEquipo getTipo() {
        return tipo;
    }

    public void setTipo(TipoEquipo tipo) {
        this.tipo = tipo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getUbicacionPlanta() {
        return ubicacionPlanta;
    }

    public void setUbicacionPlanta(String ubicacionPlanta) {
        this.ubicacionPlanta = ubicacionPlanta;
    }

    public LocalDate getFechaInstalacion() {
        return fechaInstalacion;
    }

    public void setFechaInstalacion(LocalDate fechaInstalacion) {
        this.fechaInstalacion = fechaInstalacion;
    }

    public String getEstadoOperativo() {
        return estadoOperativo;
    }

    public void setEstadoOperativo(String estadoOperativo) {
        this.estadoOperativo = estadoOperativo;
    }

    public Criticidad getCriticidad() {
        return criticidad;
    }

    public void setCriticidad(Criticidad criticidad) {
        this.criticidad = criticidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EquipoDTO equipoDTO)) {
            return false;
        }
        return Objects.equals(idEquipo, equipoDTO.idEquipo)
                && Objects.equals(nombre, equipoDTO.nombre)
                && tipo == equipoDTO.tipo
                && Objects.equals(marca, equipoDTO.marca)
                && Objects.equals(modelo, equipoDTO.modelo)
                && Objects.equals(numeroSerie, equipoDTO.numeroSerie)
                && Objects.equals(ubicacionPlanta, equipoDTO.ubicacionPlanta)
                && Objects.equals(fechaInstalacion, equipoDTO.fechaInstalacion)
                && Objects.equals(estadoOperativo, equipoDTO.estadoOperativo)
                && criticidad == equipoDTO.criticidad;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEquipo, nombre, tipo, marca, modelo, numeroSerie, ubicacionPlanta, fechaInstalacion,
                estadoOperativo, criticidad);
    }

    @Override
    public String toString() {
        return "EquipoDTO{"
                + "idEquipo=" + idEquipo
                + ", nombre='" + nombre + '\''
                + ", tipo=" + tipo
                + ", marca='" + marca + '\''
                + ", modelo='" + modelo + '\''
                + ", numeroSerie='" + numeroSerie + '\''
                + ", ubicacionPlanta='" + ubicacionPlanta + '\''
                + ", fechaInstalacion=" + fechaInstalacion
                + ", estadoOperativo='" + estadoOperativo + '\''
                + ", criticidad=" + criticidad
                + '}';
    }
}
