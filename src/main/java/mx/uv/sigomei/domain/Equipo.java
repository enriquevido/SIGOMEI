package mx.uv.sigomei.domain;

import mx.uv.sigomei.enums.Criticidad;
import mx.uv.sigomei.enums.TipoEquipo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Equipo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String nombre;
    private TipoEquipo tipo;
    private String marca;
    private String modelo;
    private String numeroSerie;
    private String ubicacionPlanta;
    private LocalDate fechaInstalacion;
    private String estadoOperativo;
    private Criticidad criticidad;

    public Equipo() {
    }

    public Equipo(Long id, String nombre, TipoEquipo tipo, String marca, String modelo, String numeroSerie,
                  String ubicacionPlanta, LocalDate fechaInstalacion, String estadoOperativo,
                  Criticidad criticidad) {
        this.id = id;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(o instanceof Equipo equipo)) {
            return false;
        }
        return Objects.equals(id, equipo.id)
                && Objects.equals(nombre, equipo.nombre)
                && tipo == equipo.tipo
                && Objects.equals(marca, equipo.marca)
                && Objects.equals(modelo, equipo.modelo)
                && Objects.equals(numeroSerie, equipo.numeroSerie)
                && Objects.equals(ubicacionPlanta, equipo.ubicacionPlanta)
                && Objects.equals(fechaInstalacion, equipo.fechaInstalacion)
                && Objects.equals(estadoOperativo, equipo.estadoOperativo)
                && criticidad == equipo.criticidad;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, tipo, marca, modelo, numeroSerie, ubicacionPlanta, fechaInstalacion,
                estadoOperativo, criticidad);
    }

    @Override
    public String toString() {
        return "Equipo{"
                + "id=" + id
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
