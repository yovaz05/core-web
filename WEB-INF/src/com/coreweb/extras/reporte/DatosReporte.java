package com.coreweb.extras.reporte;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.coreweb.util.Misc;

import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.report.builder.datatype.*;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.Markup;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.StretchType;


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
	

	public static final String EXPORT_PDF = ".pdf";
	public static final String EXPORT_CSV = ".csv";
	
	public static final int COLUMNA_ALINEADA_CENTRADA = 1;
	public static final int COLUMNA_ALINEADA_IZQUIERDA = 2;
	public static final int COLUMNA_ALINEADA_DERECHA = 3;
		
	
	public static final PageType A4 = PageType.A4;
	public static final PageType LEGAL = PageType.LEGAL;
	public static final PageType OFICIO = PageType.FLSA;

	
	private CabeceraReporte cr;
	private ComponentBuilder body = cmp.horizontalList();
	private ComponentBuilder footer = cmp.horizontalList();
	private List<Object[]> data;
	private String empresa;
	private String titulo;
	private String usuario;
	private String directorioBase = "./";
	private String idArchivo = "";
	private String directorio = "";
	private String nombreArchivo = "";
	private boolean apaisada = false;
	private PageType tipoPagina = A4;
	
	private boolean borrarDespuesDeVer = true;
	

	private Misc m = new Misc();
	
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
	
	public void setOficio(){
		this.tipoPagina = OFICIO;
	}
	
	public String getTitulo() {
		return titulo;
	}



	public String getDirectorio() {
		return directorio;
	}


	public void setDirectorio(String directorio) {
		this.directorio = directorio;
	}


	public String getNombreArchivo() {
		return nombreArchivo;
	}


	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}


	public String getDirectorioBase() {
		return directorioBase;
	}


	public void setDirectorioBase(String directorioBase) {
		this.directorioBase = directorioBase;
	}


	public void setDatosReporte(List<Object[]> datos){
		data = datos; 		
	}
	public void setDatosReporte(String titulo){
		this.titulo = titulo; 		
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
	
	
	public String getArchivoSalida(){
		String out = "";
		if (this.idArchivo.trim().length() == 0){
			long id = m.getIdUnico();
			this.idArchivo = m.dateToString(new Date(id), m.YYYY_MM_DD_HORA_MIN_SEG_MIL);
		}
		out = this.getDirectorio()+"/"+this.getNombreArchivo()+""+this.idArchivo+DatosReporte.EXPORT_PDF;
		return out;
	}
	
	
	public String getArchivoPathReal(){
		String out = this.getDirectorioBase() + "/" + this.getArchivoSalida();
		return out;
	}
	
	
	public void ejecutar (boolean mostrar){
			
		this.setDatosReportes();

		String pathCompleto = this.directorioBase + "/" + this.getArchivoSalida();

		MyReport reporte = new MyReport(cr, body, footer, data, empresa, titulo, usuario, pathCompleto);
		reporte.setFormato(DatosReporte.EXPORT_PDF);
		reporte.setLandscape(this.apaisada);
		reporte.show(mostrar);
		if ((mostrar == true)&&(this.isBorrarDespuesDeVer() == true)){
			this.m.borrarArchivo(pathCompleto);
		}
	}
	
	
	
	public boolean isBorrarDespuesDeVer() {
		return borrarDespuesDeVer;
	}


	public void setBorrarDespuesDeVer(boolean borrarDespuesDeVer) {
		this.borrarDespuesDeVer = borrarDespuesDeVer;
	}


	public ComponentBuilder getBody() {
		return body;
	}

	public void setBody(ComponentBuilder body) {
		this.body = body;
	}

	public void setFooter(ComponentBuilder footer) {
		this.footer = footer;
	}

	public ComponentBuilder getFooter() {
		return footer;
	}

	abstract public void setDatosReportes();


	public ComponentBuilder texto(String texto){
		return cmp.text(texto);
	}
	
	public ComponentBuilder textoNegrita(String texto){
		return cmp.text(texto).setStyle(Templates.boldStyle);
	}
	
	public ComponentBuilder textoParValor(String texto, Object valor){
		
		return cmp.text("<b>"+texto+":</b> "+ valor).setStyle(Templates.styleHTML);

		//		return cmp.verticalList().add(cmp.text(texto+":").setStyle(Templates.boldStyle)).add(cmp.text(""+valor));
	}

	
	public ComponentBuilder recuadro(ComponentBuilder dato){
		
		VerticalListBuilder out = cmp.verticalList().setStyle(Templates.box).setStretchType(StretchType.RELATIVE_TO_BAND_HEIGHT);
		out.add(cmp.verticalGap(5));
		out.add(dato);
		out.add(cmp.verticalGap(5));
		
		return out;
	}

	
}
