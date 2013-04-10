package com.coreweb.extras.reporte;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;



public abstract class DatosReporte {

	public static final String TIPO_STRING = "STRING";
	public static final String TIPO_INTEGER = "INTEGER";
	public static final String TIPO_LONG = "LONG";
	public static final String TIPO_BIGDECIMAL = "BIGDECIMAL";
	public static final String TIPO_DATE = "java.util.Date";
	public static final String TIPO_DATEYEAR = "DATEYEAR";
	public static final String TIPO_DATEMONTH = "DATEMONTH";
	public static final String TIPO_DATEDAY = "DATEDAY";
	
	private CabeceraReporte cr; 
	private List<Object[]> data;
	private String empresa;
	private String titulo;
	private String usuario;
	private String archivo;
	
	public DatosReporte() {
	}

	public void setTitulosColumnas(List<Object[]> titulos){
		cr = new CabeceraReporte();
		for (Object[] objects : titulos) {
			//for (int i = 0; i < objects.length; i++) {
			if(objects.length == 2)
				cr.addColumna((String)objects[0], (String)objects[1]);
			else if(objects.length == 3)
				cr.addColumna((String)objects[0], (String)objects[1],(Integer)objects[2]);
			//}
		}
		//cr.addColumna();
		//cr.addColumna("Cantidad", CabeceraReporte.TIPO_INTEGER);
	}
	public void setDatosReporte(List<Object[]> datos){
		data = datos; 		
	}
	public void setDatosReporte(String titulo){
		this.titulo = titulo; 		
	}
	public void setArchivo(String archivo){
		this.archivo = archivo; 		
	}
	
	public void setUsuario(String usuario){
		this.usuario = usuario;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
		
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public void ejecutar (){
		ejecutar(false);
	}
	
	
	public void ejecutar (boolean mostrar){
		
		this.setDatosReportes();
		MyReport reporte = new MyReport(cr, data, empresa, titulo, usuario, archivo);
		reporte.show(mostrar);
	}
	
	abstract public void setDatosReportes();


}
