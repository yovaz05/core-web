package com.coreweb.extras.browser;

import org.zkoss.zul.Textbox;

import com.coreweb.IDCore;

public class ColumnaBrowser {

	private String titulo = "titulo";
	private String campo = "campo";
	private String tipo = IDCore.TIPO_STRING;
	private String estilo = "";
	private String where = "";
	private String widthColumna = "";
	private String widthComponente = "";
	private boolean visible = true;

	// permite definir la creación de un componente que se evaluará en la celda
	// de esa columna. El string es el nombre de una operación que será invocada
	// por reflexión y tendrá 2 parámetros "Object obj, Object[] datos". El
	// primero es el valor de esa celda, y el segundo es la lista de todos los valores de la fila

	private String componente = Browser.LABEL;

	
	protected String getWhereSQL(){
		String out ="";
		if (this.getWhere().compareTo(IDCore.TIPO_STRING)==0){
			out = " lower( xxAtr ) like '%xxVa%' ";
			
		}else if (this.getWhere().compareTo(IDCore.TIPO_BOOL)==0){
			out = " xxAtr = xxVa ";
			
		}else if (this.getWhere().compareTo(IDCore.TIPO_NUMERICO)==0){
			out = " xxAtr = xxVa ";
		}
		
		return out;
	}
	
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

	public String getComponente() {
		return componente;
	}

	public void setComponente(String componente) {
		this.componente = componente;
	}

	public String getWidthColumna() {
		return widthColumna;
	}

	public void setWidthColumna(String widthColumna) {
		this.widthColumna = widthColumna;
	}

	public String getWidthComponente() {
		return widthComponente;
	}

	public void setWidthComponente(String widthComponente) {
		this.widthComponente = widthComponente;
	}
	


}
