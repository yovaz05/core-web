package com.coreweb.extras.reporte;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import net.sf.dynamicreports.report.builder.datatype.LongType;
import net.sf.dynamicreports.report.constant.PageType;



public abstract class DatosReporte {

	public static final String TIPO_STRING = "STRING";
	public static final String TIPO_INTEGER = "INTEGER";
	public static final String TIPO_LONG = "LONG";
	public static final String TIPO_BIGDECIMAL = "BIGDECIMAL";
	public static final String TIPO_DATE = "java.util.Date";
	public static final String TIPO_DATEYEAR = "DATEYEAR";
	public static final String TIPO_DATEMONTH = "DATEMONTH";
	public static final String TIPO_DATEDAY = "DATEDAY";
	public static final PageType A4 = PageType.A4;
	public static final PageType LEGAL = PageType.LEGAL;
	
	private CabeceraReporte cr;
	private ComponentBuilder body = cmp.horizontalList();
	private List<Object[]> data;
	private String empresa;
	private String titulo;
	private String usuario;
	private String archivo;
	private boolean apaisada = false;
	private PageType tipoPagina = A4;
	
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
	
	public void setApaisada(){
		this.apaisada = true;
	}
	
	public void setVertical(){
		this.apaisada = false;
	}
	
	public void setA4(){
		this.tipoPagina = A4;
	}
	
	public void setLegal(){
		this.tipoPagina = LEGAL;
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
		MyReport reporte = new MyReport(cr, body, data, empresa, titulo, usuario, archivo);
		reporte.setLandscape(this.apaisada);
		reporte.show(mostrar);
	}
	
	
	
	public ComponentBuilder getBody() {
		return body;
	}

	public void setBody(ComponentBuilder body) {
		this.body = body;
	}

	abstract public void setDatosReportes();


}
