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
public abstract class Templates {
	
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
   public static final CurrencyType currencyType;
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
 
      currencyType = new CurrencyType();
 
      /*
      HyperLinkBuilder link = hyperLink("http://www.tdn.com.py");
      dynamicReportsComponent =
        cmp.horizontalList(
         //cmp.image(Templates.class.getResource("images/perry.png")).setFixedDimension(60, 60),
         cmp.verticalList(
            cmp.text(NOMBRE_EMPRESA).setStyle(bold12CenteredStyle).setHorizontalAlignment(HorizontalAlignment.LEFT),
            cmp.text("http://www.tdn.com.py").setStyle(italicStyle).setHyperLink(link))).setFixedWidth(200);    
            
      */
      
      footerComponent = cmp.pageXofY()
                           .setStyle(
                              stl.style(boldCenteredStyle)
                                 .setTopBorder(stl.pen1Point()));
   }
 
   
   public  ComponentBuilder<?, ?> createTitleComponent(String titulo, String user){
	  
	   String us = ("Usuario: "+ user+"                        ").substring(0, 25).trim();
	   
	   ComponentBuilder<?, ?> cabecera = cmp.horizontalList(
			   cmp.verticalList(
	    				  cmp.text( this.getNombreEmpresa()).setStyle(boldStyle).setHorizontalAlignment(HorizontalAlignment.LEFT),
	    				  cmp.text(titulo).setStyle(bold12CenteredStyle).setHorizontalAlignment(HorizontalAlignment.LEFT)).setWidth(270),
	    	    cmp.verticalList(
	    	    		cmp.text("Fecha: " + m.dateHoyToString()).setHorizontalAlignment(HorizontalAlignment.RIGHT), 
	    	    		cmp.text(us).setHorizontalAlignment(HorizontalAlignment.RIGHT))

	    		  );
	   return cabecera;
   }
   
   
   
   /**
    * Creates custom component which is possible to add to any report band component
    */
   /*
   public ComponentBuilder<?, ?> xxcreateTitleComponent(String label) {
	   
      return cmp.horizontalList()
              .add(
               dynamicReportsComponent,
               cmp.text(label).setStyle(bold18CenteredStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT))
              .newRow()
              .add(cmp.line())
              .newRow()
              .add(cmp.verticalGap(10));
   }
 
 */
   
   public static CurrencyValueFormatter createCurrencyValueFormatter(String label) {
      return new CurrencyValueFormatter(label);
   }
 
   public static class CurrencyType extends BigDecimalType {
      private static final long serialVersionUID = 1L;
 
      @Override
      public String getPattern() {
         return "Gs #,###.00";
      }
   }
 
   private static class CurrencyValueFormatter extends AbstractValueFormatter<String, Number> {
      private static final long serialVersionUID = 1L;
 
      private String label;
 
      public CurrencyValueFormatter(String label) {
         this.label = label;
      }
 
      @Override
      public String format(Number value, ReportParameters reportParameters) {
         return label + currencyType.valueToString(value, reportParameters.getLocale());
      }
   }
   
   
   public abstract String  getNombreEmpresa();
   
}