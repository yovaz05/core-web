package com.coreweb.extras.reporte;

 
import static net.sf.dynamicreports.report.builder.DynamicReports.*;
 
import java.awt.Color;
import java.util.Date;
import java.util.Locale;

import com.coreweb.util.Misc;
 
import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.builder.HyperLinkBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.datatype.BigDecimalType;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.tableofcontents.TableOfContentsCustomizerBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.definition.ReportParameters;
 
/**
 * @author Ricardo Mariaca (r.mariaca@dynamicreports.org)
 */
public  class Templates {
	
   public static final StyleBuilder rootStyle;
   public static final StyleBuilder boldStyle;
   public static final StyleBuilder italicStyle;
   public static final StyleBuilder boldCenteredStyle;
   public static final StyleBuilder bold12CenteredStyle;
   public static final StyleBuilder bold18CenteredStyle;
   public static final StyleBuilder bold22CenteredStyle;
   public static final StyleBuilder columnStyle;
   public static final StyleBuilder columnTitleStyle;
   public static final StyleBuilder groupStyle;
   public static final StyleBuilder subtotalStyle;
 
   public static final ReportTemplateBuilder reportTemplate;
//   public static final CurrencyType currencyType;
//   public static final ComponentBuilder<?, ?> dynamicReportsComponent;
   public static final ComponentBuilder<?, ?> footerComponent;
 
  
 
   Misc m = new Misc();

   
   
   static {
      rootStyle           = stl.style().setPadding(2);
      boldStyle           = stl.style(rootStyle).bold();
      italicStyle         = stl.style(rootStyle).italic();
      boldCenteredStyle   = stl.style(boldStyle)
                               .setAlignment(HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
      bold12CenteredStyle = stl.style(boldCenteredStyle)
                               .setFontSize(12);
      bold18CenteredStyle = stl.style(boldCenteredStyle)
                               .setFontSize(18);
      bold22CenteredStyle = stl.style(boldCenteredStyle)
                             .setFontSize(22);
      columnStyle         = stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE);
      columnTitleStyle    = stl.style(columnStyle)
                               .setBorder(stl.pen1Point())
                               .setHorizontalAlignment(HorizontalAlignment.CENTER)
                               .setBackgroundColor(Color.LIGHT_GRAY)
                               .bold();
      groupStyle          = stl.style(boldStyle)
                               .setHorizontalAlignment(HorizontalAlignment.LEFT);
      subtotalStyle       = stl.style(boldStyle)
                               .setTopBorder(stl.pen1Point());
 
      StyleBuilder crosstabGroupStyle      = stl.style(columnTitleStyle);
      StyleBuilder crosstabGroupTotalStyle = stl.style(columnTitleStyle)
                                                .setBackgroundColor(new Color(170, 170, 170));
      StyleBuilder crosstabGrandTotalStyle = stl.style(columnTitleStyle)
                                                .setBackgroundColor(new Color(140, 140, 140));
      StyleBuilder crosstabCellStyle       = stl.style(columnStyle)
                                                .setBorder(stl.pen1Point());
 
      TableOfContentsCustomizerBuilder tableOfContentsCustomizer = tableOfContentsCustomizer()
         .setHeadingStyle(0, stl.style(rootStyle).bold());
 
      reportTemplate = template()
                         .setLocale(Locale.ENGLISH)
                         .setColumnStyle(columnStyle)
                         .setColumnTitleStyle(columnTitleStyle)
                         .setGroupStyle(groupStyle)
                         .setGroupTitleStyle(groupStyle)
                         .setSubtotalStyle(subtotalStyle)
                         .highlightDetailEvenRows()
                         .crosstabHighlightEvenRows()
                         .setCrosstabGroupStyle(crosstabGroupStyle)
                         .setCrosstabGroupTotalStyle(crosstabGroupTotalStyle)
                         .setCrosstabGrandTotalStyle(crosstabGrandTotalStyle)
                         .setCrosstabCellStyle(crosstabCellStyle)
                         .setTableOfContentsCustomizer(tableOfContentsCustomizer);
 

      
      footerComponent = cmp.pageXofY()
                           .setStyle(
                              stl.style(boldCenteredStyle)
                                 .setTopBorder(stl.pen1Point()));
   }
 
   
   public  ComponentBuilder<?, ?> createTitleComponent(String empresa, String titulo, String user){
	  
	   String us = ("Usuario: "+ user+"                        ").substring(0, 25).trim();
	   
	   ComponentBuilder<?, ?> cabecera = cmp.horizontalList(
			   cmp.verticalList(
	    				  cmp.text(empresa).setStyle(boldStyle).setHorizontalAlignment(HorizontalAlignment.LEFT),
	    				  cmp.text(titulo).setStyle(bold12CenteredStyle).setHorizontalAlignment(HorizontalAlignment.LEFT)).setWidth(270),
	    	    cmp.verticalList(
	    	    		cmp.text("Fecha: " + m.dateHoyToString()).setHorizontalAlignment(HorizontalAlignment.RIGHT), 
	    	    		cmp.text(us).setHorizontalAlignment(HorizontalAlignment.RIGHT))

	    		  );
	   return cabecera;
   }
   
  
}