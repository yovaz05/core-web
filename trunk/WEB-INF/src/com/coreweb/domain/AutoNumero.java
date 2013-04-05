package com.coreweb.domain;

import java.util.Date;

public class AutoNumero extends Domain {

	String key = "";
	long numero = 0;
	Date fecha = new Date();
	String descripcion = "descripcion no definada";
	
	
	public String getKey() {
		return key;
	}


	public void setKey(String key) {
		this.key = key;
	}


	public long getNumero() {
		return numero;
	}


	public void setNumero(long numero) {
		this.numero = numero;
	}


	public Date getFecha() {
		return fecha;
	}


	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
