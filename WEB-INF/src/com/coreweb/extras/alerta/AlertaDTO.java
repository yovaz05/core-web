package com.coreweb.extras.alerta;

import java.util.Date;

import com.coreweb.dto.DTO;
import com.coreweb.util.MyPair;

public class AlertaDTO extends DTO {

	Date fechaCreacion = new Date();
	Date fechaCancelacion = new Date();
	String creador = "";
	String destino = "";
	String descripcion = "";
	String observacion = "";
	boolean cancelada;
	MyPair nivel = new MyPair();
	MyPair tipo = new MyPair();

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaCancelacion() {
		return fechaCancelacion;
	}

	public void setFechaCancelacion(Date fechaCancelacion) {
		this.fechaCancelacion = fechaCancelacion;
	}

	public String getCreador() {
		return creador;
	}

	public void setCreador(String creador) {
		this.creador = creador;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public boolean isCancelada() {
		return cancelada;
	}

	public void setCancelada(boolean cancelada) {
		this.cancelada = cancelada;
	}

	public MyPair getNivel() {
		return nivel;
	}

	public void setNivel(MyPair nivel) {
		this.nivel = nivel;
	}

	public MyPair getTipo() {
		return tipo;
	}

	public void setTipo(MyPair tipo) {
		this.tipo = tipo;
	}

}
