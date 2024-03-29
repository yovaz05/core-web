package com.coreweb.extras.reporte;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


import net.sf.dynamicreports.examples.Templates;
import net.sf.dynamicreports.examples.complex.dynamicreport.DynamicColumn;
import net.sf.dynamicreports.examples.complex.dynamicreport.DynamicReport;
import net.sf.dynamicreports.examples.complex.dynamicreport.DynamicReportData;


import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.component.DRComponent;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.PageXofYBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.datatype.DataTypeBuilders;
import net.sf.dynamicreports.report.builder.group.ColumnGroupBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.definition.datatype.DRIDataType;
import net.sf.dynamicreports.report.exception.DRException;
 
/**
 * @author Ricardo Mariaca (r.mariaca@dynamicreports.org)
 */
public class DynamicReportDesign extends ReporteDefinicion {
	
   private DynamicReportData data = new DynamicReportData();
 
 
   @SuppressWarnings({ "rawtypes", "unchecked" })
   public JasperReportBuilder build() throws DRException {
	   
      JasperReportBuilder report = report();
 
      report
         .setTemplate(Templates.reportTemplate)
         .title(Templates.createTitleComponent("DynamicReport"));
 
      DynamicReport dynamicReport = data.getDynamicReport();
      List<DynamicColumn> columns = dynamicReport.getColumns();
      Map<String, TextColumnBuilder> drColumns = new HashMap<String, TextColumnBuilder>();
 
      for (DynamicColumn column : columns) {
         TextColumnBuilder drColumn = col.column(column.getTitle(), column.getName(), (DRIDataType) type.detectType(column.getType()));
         
         if (column.getPattern() != null) {
            drColumn.setPattern(column.getPattern());
         }
         if (column.getHorizontalAlignment() != null) {
            drColumn.setHorizontalAlignment(column.getHorizontalAlignment());
         }
         drColumns.put(column.getName(), drColumn);
         report.columns(drColumn);
      }
 
      for (String group : dynamicReport.getGroups()) {
         ColumnGroupBuilder group2 = grp.group(drColumns.get(group));
         report.groupBy(group2);
 
         for (String subtotal : dynamicReport.getSubtotals()) {
            report.subtotalsAtGroupFooter(group2, sbt.sum(drColumns.get(subtotal)));
         }
      }
 
      for (String subtotal : dynamicReport.getSubtotals()) {
         report.subtotalsAtSummary(sbt.sum(drColumns.get(subtotal)));
      }
 
      if (dynamicReport.getTitle() != null) {
         TextFieldBuilder<String> title = cmp.text(dynamicReport.getTitle())
            .setStyle(Templates.bold12CenteredStyle)
            .setHorizontalAlignment(HorizontalAlignment.CENTER);
         report.addTitle(title);
      }
      if (dynamicReport.isShowPageNumber()) {
         PageXofYBuilder pageXofY = cmp.pageXofY()
            .setStyle(Templates.boldCenteredStyle);
         report.addPageFooter(pageXofY);
      }
      report.setDataSource(data.createDataSource());
 
      return report;
   }
 
   public static void main(String[] args) {
      DynamicReportDesign design = new DynamicReportDesign();
      try {
         JasperReportBuilder report = design.build();
         report.show();
      } catch (DRException e) {
         e.printStackTrace();
      }
   }
}