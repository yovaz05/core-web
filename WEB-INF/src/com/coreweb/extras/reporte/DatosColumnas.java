package com.coreweb.extras.reporte;

import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.definition.datatype.DRIDataType;

public class DatosColumnas {
	private String titulo;
	private Object tipo;
	private int ancho = 0;
	private int alto = 0;
	private boolean totaliza = false;
	private int alineacionColuman = 0;

	public DatosColumnas(String titulo, Object tipo) {
		this.setColumna(titulo, tipo, 0);
	}

	public DatosColumnas(String titulo, Object tipo, int ancho) {
		this.setColumna(titulo, tipo, ancho);
	}

	public DatosColumnas(String titulo, Object tipo, boolean totaliza) {
		this.setColumna(titulo, tipo, 0);
		this.setTotaliza(totaliza);
	}

	public DatosColumnas(String titulo, Object tipo, int ancho, boolean totaliza) {
		this.setColumna(titulo, tipo, ancho);
		this.setTotaliza(totaliza);
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
		 * if (tipo.equals(TIPO_BIGDECIMAL) || tipo.equals(TIPO_DATE) ||
		 * tipo.equals(TIPO_DATEDAY) || tipo.equals(TIPO_DATEMONTH) ||
		 * tipo.equals(TIPO_DATEYEAR) || tipo.equals(TIPO_INTEGER)) this.tipo =
		 * tipo; else this.tipo = TIPO_STRING;
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

	public boolean isTotaliza() {
		return totaliza;
	}

	public void setTotaliza(boolean totaliza) {
		this.totaliza = totaliza;
	}

	public int getAlineacionColuman() {
		return alineacionColuman;
	}

	public void setAlineacionColuman(int alineacionColuman) {
		this.alineacionColuman = alineacionColuman;
	}

	public TextColumnBuilder getColumnBuilder() {
		TextColumnBuilder tx = col.column(this.getTitulo(), this.getTitulo()
				.replace(" ", "").toLowerCase().replace(" ", "").toLowerCase(),
				(DRIDataType) this.getTipo());

		if (this.alineacionColuman == DatosReporte.COLUMNA_ALINEADA_CENTRADA) {
			tx.setStyle(Templates.columnStyleCenter);
		}
		if (this.alineacionColuman == DatosReporte.COLUMNA_ALINEADA_IZQUIERDA) {
			tx.setStyle(Templates.columnStyleLeft);
		}
		if (this.alineacionColuman == DatosReporte.COLUMNA_ALINEADA_DERECHA) {
			tx.setStyle(Templates.columnStyleRigth);
		}

		if (this.getAncho() > 0)
			tx.setWidth(this.getAncho());

		return tx;
	}

}