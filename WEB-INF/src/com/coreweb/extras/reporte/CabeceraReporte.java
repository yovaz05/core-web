package com.coreweb.extras.reporte;


import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.grid.ColumnGridComponentBuilder;
import net.sf.dynamicreports.report.builder.grid.HorizontalColumnGridListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;


import java.util.ArrayList;
import java.util.List;

public class CabeceraReporte {

	private String titulo;
	private String tituloSecundario;
	private String usuario;
	private String fechaReporte;

	private List<DatosColumnas> columnas = new ArrayList<DatosColumnas>();

	/*
	public static final String TIPO_STRING = "STRING";
	public static final String TIPO_INTEGER = "INTEGER";
	public static final String TIPO_BIGDECIMAL = "BIGDECIMAL";
	public static final String TIPO_DATE = "java.util.Date";
	public static final String TIPO_DATEYEAR = "DATEYEAR";
	public static final String TIPO_DATEMONTH = "DATEMONTH";
	public static final String TIPO_DATEDAY = "DATEDAY";
	*/
	
	
	public void addColumna(String titulo, String tipo) {
		addColumna(titulo,tipo, 0);
	}


	public void addColumna(String titulo, String tipo, int ancho) {
		DatosColumnas columna = new DatosColumnas(titulo, tipo);
		columna.setAncho(ancho);
		columnas.add(columna);
	}

	public TextColumnBuilder[] getColumnas() {
		TextColumnBuilder[] items = new TextColumnBuilder[columnas.size()];
		for (int i = 0; i < columnas.size(); i++) {
			DatosColumnas columna = columnas.get(i);
			try {
				
			/*	
				items[i] = col.column(columna.getTitulo(), columna.getTitulo()
						.replace(" ", "").toLowerCase().replace(" ", "")
						.toLowerCase(), type.detectType(columna.getTipo()));
			*/	
				
				items[i] = col.column(columna.getTitulo(), columna.getTitulo()
						.replace(" ", "").toLowerCase().replace(" ", "")
						.toLowerCase(), type.detectType(columna.getTipo()));
				
				if(columna.getAncho()>0)
					items[i].setWidth(columna.getAncho());
				//items[i].setHeight(columna.getAlto());

			} catch (DRException e) {

				e.printStackTrace();
			}
		}
		return items;
	}

	public String[] getColumnasDS() {
		String[] items = new String[columnas.size() + 1];
		for (int i = 0; i < columnas.size(); i++) {
			DatosColumnas columna = columnas.get(i);
			items[i] = columna.getTitulo().replace(" ", "").toLowerCase();

		}
		return items;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTituloSecundario() {
		return tituloSecundario;
	}

	public void setTituloSecundario(String tituloSecundario) {
		this.tituloSecundario = tituloSecundario;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getFechaReporte() {
		return fechaReporte;
	}

	public void setFechaReporte(String fechaReporte) {
		this.fechaReporte = fechaReporte;
	}

	public class DatosColumnas {
		private String titulo;
		private String tipo;
		private int ancho = 0;
		private int alto = 0;

		public DatosColumnas(String titulo, String tipo) {
			this.titulo = titulo;
			setTipo(tipo);
			this.ancho = ancho;
			this.alto = alto;

		}

		public String getTitulo() {
			return titulo;
		}

		public void setTitulo(String titulo) {
			this.titulo = titulo;
		}

		public String getTipo() {
			return tipo;
		}

		public void setTipo(String tipo) {
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

}
