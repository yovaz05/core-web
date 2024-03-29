package com.coreweb.extras.reporte;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.coreweb.Config;
import com.coreweb.util.Misc;

import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilders;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.datatype.*;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilders;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.Markup;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.StretchType;
import net.sf.dynamicreports.report.definition.datatype.DRIDataType;

public abstract class DatosReporte extends ReporteDefinicion {
	/*
	 * public static final StringType TIPO_STRING = type.stringType(); public
	 * static final IntegerType TIPO_INTEGER = type.integerType(); public static
	 * final LongType TIPO_LONG = type.longType(); public static final
	 * DoubleType TIPO_DOUBLE = type.doubleType(); public static final
	 * DoubleType TIPO_DOUBLE_GS = type.doubleType(); public static final
	 * BigDecimalType TIPO_BIGDECIMAL = type.bigDecimalType(); public static
	 * final DateType TIPO_DATE = type.dateType(); public static final
	 * DateYearType TIPO_DATEYEAR = type.dateYearType(); public static final
	 * DateMonthType TIPO_DATEMONTH = type.dateMonthType(); public static final
	 * DateDayType TIPO_DATEDAY =type.dateDayType(); public static final
	 * BooleanType TIPO_BOOLEAN =type.booleanType();
	 */


	public static final String NEGRITA = "negrita";
	public static final String DERECHA = "derecha";
	public static final String WIDTH = "width";   

	
	
	public static final String TIPO_STRING = "String";
	public static final String TIPO_INTEGER = "Integer";
	public static final String TIPO_LONG = "Lomg";
	public static final String TIPO_DOUBLE = "Double";
	public static final String TIPO_DOUBLE_GS = "DoubleGs";
	public static final String TIPO_DOUBLE_DS = "DoubleDolar";
	public static final String TIPO_BIGDECIMAL = "BigDecimal";
	public static final String TIPO_DATE = "Date";
	public static final String TIPO_DATEYEAR = "DateYear";
	public static final String TIPO_DATEMONTH = "DateMonth";
	public static final String TIPO_DATEDAY = "DateDay";
	public static final String TIPO_BOOLEAN = "Boolean";

	private static Hashtable<String, DRIDataType> tipos = new Hashtable<>();
	static {
		DataTypeBuilders type = new DataTypeBuilders();
		
		tipos.put(TIPO_STRING, type.stringType());
		tipos.put(TIPO_STRING, type.stringType());
		tipos.put(TIPO_INTEGER, type.integerType());
		tipos.put(TIPO_LONG, type.longType());
		tipos.put(TIPO_DOUBLE, type.doubleType());
		tipos.put(TIPO_DOUBLE_GS, type.doubleType());
		tipos.put(TIPO_DOUBLE_DS, type.doubleType());
		tipos.put(TIPO_BIGDECIMAL, type.bigDecimalType());
		tipos.put(TIPO_DATE, type.dateType());
		tipos.put(TIPO_DATEYEAR, type.dateYearType());
		tipos.put(TIPO_DATEMONTH, type.dateMonthType());
		tipos.put(TIPO_DATEDAY, type.dateDayType());
		tipos.put(TIPO_BOOLEAN, type.booleanType());
	}
	public static DRIDataType getTipo(String key){
		return tipos.get(key);
	}

	public static final String EXPORT_PDF = ".pdf";
	public static final String EXPORT_CSV = ".csv";

	public static final int COLUMNA_ALINEADA_CENTRADA = 1;
	public static final int COLUMNA_ALINEADA_IZQUIERDA = 2;
	public static final int COLUMNA_ALINEADA_DERECHA = 3;

	public static final PageType A4 = PageType.A4;
	public static final PageType A5 = PageType.A5;
	public static final PageType LEGAL = PageType.LEGAL;
	public static final PageType OFICIO = PageType.FLSA;

	private CabeceraReporte cr;
	private ComponentBuilder body = cmp.horizontalList();
	private ComponentBuilder footer = cmp.horizontalList();
	private List<Object[]> data;
	private String logoEmpresa = "";
	private int logoAncho = 0;
	private int logoAlto = 0;
	
	private String empresa;
	private String titulo;
	private String usuario;
	private String directorioBase = "./";
	private String idArchivo = "";
	private String directorio = "";
	private String nombreArchivo = "";
	private boolean apaisada = false;
	private boolean putFoot = true;
	private PageType tipoPagina = A4;

	private boolean borrarDespuesDeVer = true;

	private Misc m = new Misc();

	public DatosReporte() {
	}

	public void putFoot(boolean b) {
		this.putFoot = b;
	}

	public boolean isPutFoot() {
		return putFoot;
	}

	public void setTitulosColumnas(List<DatosColumnas> columnas) {
		cr = new CabeceraReporte();
		cr.setColumnas(columnas);
	}

	public void setApaisada() {
		this.apaisada = true;
	}

	public void setVertical() {
		this.apaisada = false;
	}

	public void setA4() {
		this.tipoPagina = A4;
	}

	public void setA5() {
		this.tipoPagina = A5;
	}

	public void setLegal() {
		this.tipoPagina = LEGAL;
	}

	public void setOficio() {
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

	public void setDatosReporte(List<Object[]> datos) {
		data = datos;
	}

	public void setDatosReporte(String titulo) {
		this.titulo = titulo;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public void ejecutar() {
		ejecutar(false);
	}

	public String getArchivoSalida() {
		String out = "";
		if (this.idArchivo.trim().length() == 0) {
			long id = m.getIdUnico();
			this.idArchivo = m.dateToString(new Date(id),
					m.YYYY_MM_DD_HORA_MIN_SEG_MIL);
		}
		out = this.getDirectorio() + "/" + this.getNombreArchivo() + ""
				+ this.idArchivo + DatosReporte.EXPORT_PDF;
		return out;
	}

	public String getArchivoPathReal() {
		String out = Config.DIRECTORIO_REAL_REPORTES + "/"
				+ this.getArchivoSalida();
		return out;
	}

	public String getUrlReporte() {
		String out = Config.DIRECTORIO_WEB_REPORTES + "/"
				+ this.getArchivoSalida();
		return out;
	}

	public void ejecutar(boolean mostrar) {

		this.setDatosReportes();

		String pathCompleto = this.getArchivoPathReal();

		MyReport reporte = new MyReport(cr, body, footer, data, empresa, logoEmpresa, logoAncho, logoAlto,
				titulo, usuario, pathCompleto);
		reporte.setPutFooter(this.isPutFoot());
		reporte.setFormato(DatosReporte.EXPORT_PDF);
		reporte.setLandscape(this.apaisada);
		reporte.show(mostrar);
		if ((mostrar == true) && (this.isBorrarDespuesDeVer() == true)) {
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

	
	public ComponentBuilder texto(String texto, String style) {

		TextFieldBuilder<String> tx =  cmp.text(texto);

		StyleBuilders stl = new StyleBuilders();
		StyleBuilder stlNew = stl.style();
		
		if (style.indexOf(NEGRITA)>=0){
			stlNew = stlNew.bold();
		}
		if (style.indexOf(DERECHA)>=0){
			stlNew = stlNew.setHorizontalAlignment(HorizontalAlignment.RIGHT);
		}
		
		int pw = style.indexOf(WIDTH);
		if (pw >= 0){
			String wdStr = style.substring(pw+WIDTH.length());
			int ws = Integer.parseInt(wdStr);
			tx.setWidth(ws);
		}
		tx.setStyle(stlNew);
		
		return tx;
	}
	
	
	public ComponentBuilder texto(String texto) {
		return cmp.text(texto);
	}

	public ComponentBuilder textoAlineadoDerecha(String texto) {
		return cmp.text(texto).setStyle(Templates.textStyleRigth);
	}

	
	public ComponentBuilder textoNegrita(String texto) {
		return cmp.text(texto).setStyle(Templates.boldStyle);
	}

	public ComponentBuilder textoNegritaDerecha(String texto) {
		return cmp.text(texto).setStyle(Templates.boldStyleRight);
	}

	
	public ComponentBuilder textoParValor(String texto, Object valor) {

		return cmp.text("<b>" + texto + ":</b> " + valor).setStyle(
				Templates.styleHTML);

		// return
		// cmp.verticalList().add(cmp.text(texto+":").setStyle(Templates.boldStyle)).add(cmp.text(""+valor));
	}

	public ComponentBuilder textoAutorizado() {
		
		VerticalListBuilder out = cmp.verticalList();
		out.setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.CENTER));
		out.add(cmp.verticalGap(100));
		out.add(cmp.horizontalFlowList().add(165, this.textoNegrita("---------------------------------------------------------------------")));
		out.add(cmp.horizontalFlowList().add(235, this.textoNegrita("AUTORIZADO")));
		return out;
	}
	
	public ComponentBuilder recuadro(ComponentBuilder dato) {

		VerticalListBuilder out = cmp.verticalList().setStyle(Templates.box)
				.setStretchType(StretchType.RELATIVE_TO_BAND_HEIGHT);
		out.add(cmp.verticalGap(5));
		out.add(dato);
		out.add(cmp.verticalGap(5));

		return out;
	}


	public void setLogoEmpresa(String logoEmpresa, int logoAncho, int logoAlto) {
		this.logoEmpresa = logoEmpresa;
		this.logoAncho = logoAncho;
		this.logoAlto = logoAlto;
	}

}
