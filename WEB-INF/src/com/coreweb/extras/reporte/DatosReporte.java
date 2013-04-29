package com.coreweb.extras.reporte;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.report.builder.datatype.LongType;
import net.sf.dynamicreports.report.builder.datatype.*;
import net.sf.dynamicreports.report.constant.PageType;


public abstract class DatosReporte {

	
	public static final StringType TIPO_STRING = type.stringType();
	public static final IntegerType TIPO_INTEGER = type.integerType();
	public static final LongType TIPO_LONG = type.longType();
	public static final DoubleType TIPO_DOUBLE = type.doubleType();
	public static final BigDecimalType TIPO_BIGDECIMAL = type.bigDecimalType();
	public static final DateType TIPO_DATE = type.dateType();
	public static final DateYearType TIPO_DATEYEAR = type.dateYearType();
	public static final DateMonthType TIPO_DATEMONTH = type.dateMonthType();
	public static final DateDayType TIPO_DATEDAY =type.dateDayType();
	
	public static final int COLUMNA_ALINEADA_CENTRADA = 1;
	public static final int COLUMNA_ALINEADA_IZQUIERDA = 2;
	public static final int COLUMNA_ALINEADA_DERECHA = 3;
		
	
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

	
	public void setTitulosColumnas(List<DatosColumnas> columnas ){
		cr = new CabeceraReporte();
		cr.setColumnas(columnas);
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


	public ComponentBuilder texto(String texto){
		return cmp.text(texto);
	}
	
	public ComponentBuilder textoNegrita(String texto){
		return cmp.text(texto).setStyle(Templates.boldStyle);
	}
	
	public ComponentBuilder textoParValor(String texto, Object valor){
		return cmp.verticalList().add(cmp.text(texto+":").setStyle(Templates.boldStyle)).add(cmp.text(""+valor));
	}
	
	
}
