package com.coreweb.extras.reporte;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

public class MyReport {

	CabeceraReporte cabecera;
	List<Object[]> datos;
	JasperReportBuilder rep;
	JasperPdfExporterBuilder pdfExporter;
	String titulo;
	String archivo;
	String usuario;

	public MyReport(CabeceraReporte cabecera, List<Object[]> datos,
			String titulo, String usuario, String archivo) {
		this.cabecera = cabecera;
		this.datos = datos;
		this.titulo = titulo;
		this.usuario = usuario;
		this.archivo = archivo;
	}

	private void build() {

		StyleBuilder textStyle = stl.style(Templates.columnStyle).setBorder(
				stl.pen1Point());

		TextColumnBuilder[] items = cabecera.getColumnas();
		try {

			pdfExporter = export.pdfExporter(archivo).setEncrypted(false);

			rep = report();
			rep.setTemplate(Templates.reportTemplate);
			rep.setColumnStyle(textStyle);

			for (int i = 0; i < items.length; i++) {
				rep.addColumn(items[i]);
			}

			Templates tmp = new TemplateYhaguy();
			
			rep.title(tmp.createTitleComponent(titulo, usuario));
			rep.pageFooter(Templates.footerComponent);
			rep.setDataSource(createDataSource(cabecera.getColumnasDS(), datos));

			// .toPdf(pdfExporter);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private JRDataSource createDataSource(String[] titles, List<Object[]> data)
			throws Exception {

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
			if (ver)
				rep.show();
		} catch (DRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
