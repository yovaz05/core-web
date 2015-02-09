package com.coreweb.extras.reporte;



import java.awt.Color;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.AbstractJasperExporterBuilder;
import net.sf.dynamicreports.jasper.builder.export.ExporterBuilders;
import net.sf.dynamicreports.jasper.builder.export.JasperCsvExporterBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.FieldBuilder;
import net.sf.dynamicreports.report.builder.column.ColumnBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.group.ColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.subtotal.AggregationSubtotalBuilder;
import net.sf.dynamicreports.report.constant.GroupHeaderLayout;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.constant.WhenNoDataType;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

public class MyReport extends ReporteDefinicion {

	CabeceraReporte cabecera = new CabeceraReporte();
	List<Object[]> datos = new ArrayList<Object[]>();
	AbstractJasperExporterBuilder exporter;
	String tipoFormato = ".pdf";
	JasperReportBuilder rep;
	ComponentBuilder body;
	ComponentBuilder footer;
	//JasperPdfExporterBuilder pdfExporter;
	boolean landscape = false;
	PageType tipoPagina = PageType.A4;
	boolean putFooter = true;

	String titulo;
	String archivo;
	String usuario;
	String empresa;

	public MyReport() {

	}

	public MyReport(CabeceraReporte cabecera, ComponentBuilder body,
			ComponentBuilder footer, List<Object[]> datos, String empresa,
			String titulo, String usuario, String archivo) {
		this.cabecera = cabecera;
		this.body = body;
		this.footer = footer;
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

		// StyleBuilder textStyle = stl.style(Templates.columnStyle).setBorder(
		// stl.pen1Point());

		if (cabecera == null) {
			cabecera = new CabeceraReporte();
		}

		try {

			Templates template = new Templates();
			
			rep = report();
			rep.setTemplate(template.reportTemplate);
			rep.setColumnStyle(Templates.columnStyle);
			rep.setDetailSplitType(SplitType.PREVENT);

			rep.setPageFormat(this.tipoPagina);
			if (this.landscape == true) {
				rep.setPageFormat(this.tipoPagina, PageOrientation.LANDSCAPE);
			}

			Templates tmp = new Templates();

			rep.title(tmp.createCabeceraPrincipal(empresa, titulo, usuario))
				.setTitleSplitType(SplitType.IMMEDIATE);

			rep.addTitle(this.body);

			if(this.isPutFooter() == true){
				rep.pageFooter(template.footerComponent);
			}
			
			rep.setDataSource(createDataSource(cabecera.getColumnasDS(), datos));
			rep.addSummary(this.footer);
			rep.setWhenNoDataType(WhenNoDataType.ALL_SECTIONS_NO_DETAIL);

			
			
			List<ColumnGroupBuilder> lGr = new ArrayList<ColumnGroupBuilder>();
			
			
			for (Iterator iterator = cabecera.getColumnas().iterator(); iterator
					.hasNext();) {
				
				DatosColumnas dc = (DatosColumnas) iterator.next();
				TextColumnBuilder tx = dc.getColumnBuilder();
				
				if (dc.isAgrupar() == true) {
					ColumnGroupBuilder grupo = grp.group(tx);
							//.setTitleWidth(100)
							//.setHeaderLayout(GroupHeaderLayout.TITLE_AND_VALUE).showColumnHeaderAndFooter();
					lGr.add(grupo);
				}else{
					rep.addColumn(tx);
					if (dc.isTotaliza() == true) {
						rep.subtotalsAtSummary(sbt.sum(tx));
					}
				}
			}
			// los grupos
			for (ColumnGroupBuilder gr : lGr) {
				rep.groupBy(gr);
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private JRDataSource createDataSource(String[] titles, List<Object[]> data)
			throws Exception {

		if (data == null) {
			data = new ArrayList<Object[]>();
			Object[] aux = new Object[titles.length];
			data.add(aux);
		}

		DRDataSource dataSource = null;

		Constructor<DRDataSource> contructor = DRDataSource.class
				.getConstructor(String[].class);
		dataSource = contructor.newInstance((Object) titles);

		for (int i = 0; i < data.size(); i++) {
			//System.out.println("-------------------- "+i);
			Object[] objects = (Object[]) data.get(i);
			dataSource.add(objects);
		}
		

		return dataSource;

	}

	public void show(boolean ver) {

		try {

			build();
			if(tipoFormato.equals(DatosReporte.EXPORT_CSV)){
				exporter = export.csvExporter(archivo);
				rep.toCsv((JasperCsvExporterBuilder)exporter);
			}
			else if(tipoFormato.equals(DatosReporte.EXPORT_PDF)){
				exporter = export.pdfExporter(archivo);
				rep.toPdf((JasperPdfExporterBuilder)exporter);
			}
			
			/*
			build();		
			JasperPdfExporterBuilder pdfExporter = export.pdfExporter(archivo).setEncrypted(false);
			rep.toPdf(pdfExporter);
			*/
			
			if (ver) {
				rep.show();

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setFormato(String exportPdf) {
		tipoFormato = exportPdf;
	}

	public boolean isPutFooter() {
		return putFooter;
	}

	public void setPutFooter(boolean putFooter) {
		this.putFooter = putFooter;
	}

}
