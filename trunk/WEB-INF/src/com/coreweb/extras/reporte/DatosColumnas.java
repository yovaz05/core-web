package com.coreweb.extras.reporte;

public class DatosColumnas {
	private String titulo;
	private Object tipo;
	private int ancho = 0;
	private int alto = 0;

	public DatosColumnas(String titulo, Object tipo) {
		setColumna(titulo, tipo, 0);

	}

	public DatosColumnas(String titulo, Object tipo, int ancho) {
		setColumna(titulo, tipo, ancho);
	}
	private void setColumna(String titulo, Object tipo, int ancho) {
		this.titulo = titulo;
		setTipo(tipo);
		this.ancho = ancho;

		
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Object getTipo() {
		return tipo;
	}

	public void setTipo(Object tipo) {
		/*
		if (tipo.equals(TIPO_BIGDECIMAL) || tipo.equals(TIPO_DATE)
				|| tipo.equals(TIPO_DATEDAY) || tipo.equals(TIPO_DATEMONTH)
				|| tipo.equals(TIPO_DATEYEAR) || tipo.equals(TIPO_INTEGER))
			this.tipo = tipo;
		else
			this.tipo = TIPO_STRING;
		*/
		this.tipo = tipo;
	}

	public int getAncho() {
		return ancho;
	}

	public void setAncho(int ancho) {
		this.ancho = ancho;
	}

	public int getAlto() {
		return alto;
	}

	public void setAlto(int alto) {
		this.alto = alto;
	}

}