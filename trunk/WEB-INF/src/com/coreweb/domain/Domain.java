package com.coreweb.domain;

import java.io.Serializable;
import java.util.Date;

import com.coreweb.dto.DBEstado;

public abstract class Domain implements Serializable, Comparable, IiD {


	
	protected Long id = new Long(-1);
	
	private char dbEstado = DBEstado.DB_EDITABLE;
	
	private Date modificado = null;
	
	private String usuarioMod = "popu";

	
	public String getUsuarioMod() {
		return usuarioMod;
	}

	public void setUsuarioMod(String usuarioMod) {
		this.usuarioMod = usuarioMod;
	}

	public boolean esNuevo() {
		return (id < 1);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getModificado() {
		return modificado;
	}

	public void setModificado(Date modificado) {
		this.modificado = modificado;
	}

	public char getDbEstado() {
		return dbEstado;
	}

	public void setDbEstado(char dbEstado) {
		this.dbEstado = dbEstado;
	}




}
