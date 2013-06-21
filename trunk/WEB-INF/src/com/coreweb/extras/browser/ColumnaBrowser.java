package com.coreweb.extras.browser;

import org.zkoss.zul.Textbox;


public class ColumnaBrowser {

	public static String TEXT_BOX = Textbox.class.getName();
	
	private String titulo = "titulo";
	private String campo = "campo";
	private String tipo = "tipo";
	private String estilo = "";
	private String where = "";
	private boolean visible = true;
	

	
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getCampo() {
		return campo;
	}
	public void setCampo(String campo) {
		this.campo = campo;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getEstilo() {
		return estilo;
	}
	public void setEstilo(String estilo) {
		this.estilo = estilo;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public String getWhere() {
		return where;
	}
	public void setWhere(String where) {
		this.where = where;
	}

	
	
	
}
