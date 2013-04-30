package com.coreweb.extras.reporte;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import java.awt.Color;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.coreweb.util.Misc;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.builder.HyperLinkBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.datatype.BigDecimalType;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.tableofcontents.TableOfContentsCustomizerBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.Markup;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.definition.ReportParameters;

/**
 * @author Ricardo Mariaca (r.mariaca@dynamicreports.org)
 */
public class Templates {

	public static final StyleBuilder rootStyle;
	public static final StyleBuilder boldStyle;
	public static final StyleBuilder italicStyle;
	public static final StyleBuilder boldCenteredStyle;
	public static final StyleBuilder bold12CenteredStyle;
	public static final StyleBuilder bold18CenteredStyle;
	public static final StyleBuilder bold22CenteredStyle;
	public static final StyleBuilder columnStyle;
	public static final StyleBuilder columnStyleCenter;
	public static final StyleBuilder columnStyleLeft;
	public static final StyleBuilder columnStyleRigth;
	public static final StyleBuilder columnTitleStyle;
	public static final StyleBuilder groupStyle;
	public static final StyleBuilder subtotalStyle;
	public static final StyleBuilder styleHTML;
	public static final StyleBuilder box;

	public static final ReportTemplateBuilder reportTemplate;
	// public static final CurrencyType currencyType;
	// public static final ComponentBuilder<?, ?> dynamicReportsComponent;
	public static final ComponentBuilder footerComponent;

	Misc m = new Misc();

	static {
		styleHTML = stl.style().setMarkup(Markup.STYLED);
		rootStyle = stl.style().setPadding(2);
		boldStyle = stl.style(rootStyle).bold();
		italicStyle = stl.style(rootStyle).italic();
		boldCenteredStyle = stl.style(boldStyle).setAlignment(
				HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
		bold12CenteredStyle = stl.style(boldCenteredStyle).setFontSize(12);
		bold18CenteredStyle = stl.style(boldCenteredStyle).setFontSize(18);
		bold22CenteredStyle = stl.style(boldCenteredStyle).setFontSize(22);

		box = stl.style().setBorder(stl.pen1Point());
		
		columnStyle = stl.style(rootStyle).setVerticalAlignment(
				VerticalAlignment.MIDDLE).setBorder(stl.pen1Point());
		
		columnStyleCenter = stl.style(rootStyle).setVerticalAlignment(
				VerticalAlignment.MIDDLE).setBorder(stl.pen1Point())
				.setHorizontalAlignment(HorizontalAlignment.CENTER);
		
		columnStyleLeft = stl.style(rootStyle).setVerticalAlignment(
				VerticalAlignment.MIDDLE).setBorder(stl.pen1Point())
				.setHorizontalAlignment(HorizontalAlignment.LEFT);

		columnStyleRigth = stl.style(rootStyle).setVerticalAlignment(
				VerticalAlignment.MIDDLE).setBorder(stl.pen1Point())
				.setHorizontalAlignment(HorizontalAlignment.RIGHT);
		
	
		
		columnTitleStyle = stl.style(columnStyle).setBorder(stl.pen1Point())
				.setHorizontalAlignment(HorizontalAlignment.CENTER)
				.setBackgroundColor(Color.LIGHT_GRAY).bold();
		groupStyle = stl.style(boldStyle).setHorizontalAlignment(
				HorizontalAlignment.LEFT);
//		subtotalStyle = stl.style(boldStyle).setTopBorder(stl.pen1Point()).setBackgroundColor(new Color(56));
		subtotalStyle = stl.style(columnStyle).setBorder(stl.pen1Point())
				.setHorizontalAlignment(HorizontalAlignment.RIGHT)
				.setBackgroundColor(Color.LIGHT_GRAY).bold();

		StyleBuilder crosstabGroupStyle = stl.style(columnTitleStyle);
		StyleBuilder crosstabGroupTotalStyle = stl.style(columnTitleStyle)
				.setBackgroundColor(new Color(170, 170, 170));
		StyleBuilder crosstabGrandTotalStyle = stl.style(columnTitleStyle)
				.setBackgroundColor(new Color(140, 140, 140));
		StyleBuilder crosstabCellStyle = stl.style(columnStyle).setBorder(
				stl.pen1Point());

		TableOfContentsCustomizerBuilder tableOfContentsCustomizer = tableOfContentsCustomizer()
				.setHeadingStyle(0, stl.style(rootStyle).bold());

		reportTemplate = template().setLocale(Locale.ENGLISH)
				.setColumnStyle(columnStyle)
				.setColumnTitleStyle(columnTitleStyle)
				.setGroupStyle(groupStyle).setGroupTitleStyle(groupStyle)
				.setSubtotalStyle(subtotalStyle).highlightDetailEvenRows()
				.crosstabHighlightEvenRows()
				.setCrosstabGroupStyle(crosstabGroupStyle)
				.setCrosstabGroupTotalStyle(crosstabGroupTotalStyle)
				.setCrosstabGrandTotalStyle(crosstabGrandTotalStyle)
				.setCrosstabCellStyle(crosstabCellStyle)
				.setTableOfContentsCustomizer(tableOfContentsCustomizer);

		footerComponent = cmp.pageXofY().setStyle(
				stl.style(boldCenteredStyle).setTopBorder(stl.pen1Point()));
	}

	public ComponentBuilder createCabeceraPrincipal(String empresa,
			String titulo, String user) {

		String us = ("Usuario: " + user + "                        ")
				.substring(0, 25).trim();

		ComponentBuilder cabecera = cmp.verticalList().add(
				cmp.horizontalList(
				cmp.verticalList(
						cmp.text(empresa)
								.setStyle(boldStyle)
								.setHorizontalAlignment(
										HorizontalAlignment.LEFT),
						cmp.text(titulo)
								.setStyle(bold12CenteredStyle)
								.setHorizontalAlignment(
										HorizontalAlignment.LEFT))
						.setWidth(270), cmp.verticalList(
						cmp.text("Generado: " + m.dateHoyToString())
								.setHorizontalAlignment(
										HorizontalAlignment.RIGHT),
						cmp.text(us).setHorizontalAlignment(
								HorizontalAlignment.RIGHT)))

		).add(cmp.line());
		return cabecera;
	}


}