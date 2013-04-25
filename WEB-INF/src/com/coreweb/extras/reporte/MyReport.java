package com.coreweb.extras.reporte;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.FieldBuilder;
import net.sf.dynamicreports.report.builder.column.ColumnBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.subtotal.AggregationSubtotalBuilder;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

public class MyReport {

	CabeceraReporte cabecera = new CabeceraReporte();
	List<Object[]> datos = new ArrayList<Object[]>();;
	JasperReportBuilder rep;
	ComponentBuilder body;
	JasperPdfExporterBuilder pdfExporter;
	boolean landscape = false;
	PageType tipoPagina = PageType.A4;
	
	String titulo;
	String archivo;
	String usuario;
	String empresa;
	
	

	public MyReport(){
		
	}
	
	public MyReport(CabeceraReporte cabecera, ComponentBuilder body, List<Object[]> datos,
			String empresa, String titulo, String usuario, String archivo) {
		this.cabecera = cabecera;
		this.body = body;
		this.datos = datos;
		this.empresa = empresa;
		this.titulo = titulo;
		this.usuario = usuario;
		this.archivo = archivo;
	}
	
	

	public boolean isLandscape() {
		return landscape;
	}

	public void setLandscape(boolean landscape) {
		this.landscape = landscape;
	}
	
	

	public PageType getTipoPagina() {
		return tipoPagina;
	}

	public void setTipoPagina(PageType tipoPagina) {
		this.tipoPagina = tipoPagina;
	}

	private void build() {

		StyleBuilder textStyle = stl.style(Templates.columnStyle).setBorder(
				stl.pen1Point());

		if (cabecera == null){
			cabecera = new CabeceraReporte();
		}
		
		TextColumnBuilder[] items = cabecera.getColumnas();
		try {

			pdfExporter = export.pdfExporter(archivo).setEncrypted(false);

			rep = report();
			
			rep.setPageFormat(this.tipoPagina);
			if (this.landscape == true){
				rep.setPageFormat(this.tipoPagina, PageOrientation.LANDSCAPE);
			}
		
			rep.setTemplate(Templates.reportTemplate);
			rep.setColumnStyle(textStyle);

			for (int i = 0; i < items.length; i++) {
				rep.addColumn(items[i]);
			}

			Templates tmp = new Templates();
			
			
			rep.title(tmp.createCabeceraPrincipal(empresa, titulo, usuario));
			
			rep.addTitle(this.body);
			
			
			
		
			rep.pageFooter(Templates.footerComponent);
			rep.setDataSource(createDataSource(cabecera.getColumnasDS(), datos));
			
			/*
			rep.subtotalsAtSummary(sbt.sum( items[2]));
			rep.subtotalsAtSummary(sbt.sum( items[4]));
			*/
			//rep.subtotalsAtColumnFooter( sbt.sum( items[4]));

			//NO			AggregationSubtotalBuilder<Long> unitPriceSum = sbt.sum(unitPriceField,  items[4]).setLabel("Total de la suma =");
						

			// .toPdf(pdfExporter);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private JRDataSource createDataSource(String[] titles, List<Object[]> data)
			throws Exception {

		if (data == null){
			data = new ArrayList<Object[]>();
			Object[] aux = new Object[titles.length]; 
			data.add(aux);
		}
		
		DRDataSource dataSource = null;

		Constructor<DRDataSource> contructor = DRDataSource.class
				.getConstructor(String[].class);
		dataSource = contructor.newInstance((Object) titles);

		for (Iterator iterator = data.iterator(); iterator.hasNext();) {
			Object[] objects = (Object[]) iterator.next();
			dataSource.add(objects);
		}

		return dataSource;

	}

	public void show(boolean ver) {

		try {
			build();
			
			
			
			rep.toPdf(pdfExporter);
			if (ver){
				rep.show();
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
}