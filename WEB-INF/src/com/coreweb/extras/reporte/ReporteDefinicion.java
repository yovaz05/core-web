/**
 * esta es la clase net.sf.dynamicreports.report.builder.DynamicReports
 * pero le quité todo los y final
 * 
 */
package com.coreweb.extras.reporte;

import com.coreweb.extras.reporte.extendidos.*;

import net.sf.dynamicreports.report.builder.*;
import net.sf.dynamicreports.jasper.builder.JasperConcatenatedReportBuilder;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.ExporterBuilders;
import net.sf.dynamicreports.jasper.definition.JasperReportHandler;
import net.sf.dynamicreports.report.builder.barcode.BarcodeBuilders;
import net.sf.dynamicreports.report.builder.chart.ChartBuilders;
import net.sf.dynamicreports.report.builder.column.ColumnBuilders;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.column.ValueColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilders;
import net.sf.dynamicreports.report.builder.condition.ConditionBuilders;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabBuilders;
import net.sf.dynamicreports.report.builder.datatype.DataTypeBuilders;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
import net.sf.dynamicreports.report.builder.expression.ExpressionBuilders;
import net.sf.dynamicreports.report.builder.grid.GridBuilders;
import net.sf.dynamicreports.report.builder.group.GroupBuilders;
import net.sf.dynamicreports.report.builder.style.StyleBuilders;
import net.sf.dynamicreports.report.builder.subtotal.SubtotalBuilders;
import net.sf.dynamicreports.report.builder.tableofcontents.TableOfContentsCustomizerBuilder;
import net.sf.dynamicreports.report.builder.tableofcontents.TableOfContentsHeadingBuilder;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.constant.OrderType;
import net.sf.dynamicreports.report.definition.datatype.DRIDataType;
import net.sf.dynamicreports.report.definition.expression.DRIExpression;
import net.sf.dynamicreports.report.exception.DRException;

import org.apache.commons.lang3.Validate;

public class ReporteDefinicion  {


	/**
	 * A set of methods of creating report columns.<br/>
	 * It is used to display data in a multi-column layout.
	 */
	public ColumnBuilders col = new ColumnBuilders();
	/**
	 * A set of methods of customizing columns layout.
	 */
	public GridBuilders grid = new GridBuilders();
	/**
	 * A set of methods of creating report groups.
	 */
	public GroupBuilders grp = new GroupBuilders();
	/**
	 * A set of methods of creating column subtotals.
	 */
	public SubtotalBuilders sbt = new SubtotalBuilders();
	/**
	 * A set of methods of creating and customizing styles.
	 */
	public StyleBuilders stl = new StyleBuilders();
	/**
	 * A set of methods of creating components.
	 */
	public ComponentBuilders cmp = new ComponentBuilders();
	/**
	 * A set of build in expressions.<br/>
	 * Expressions are used to define various calculations, conditions, text field content, specific report groups, etc.
	 */
	public ExpressionBuilders exp = new ExpressionBuilders();
	/**
	 * A set of build in condition expressions.
	 */
	public ConditionBuilders cnd = new ConditionBuilders();
	/**
	 * A set of build in data types.
	 */
	public DataTypeBuilders type = new DataTypeBuilders();
	/**
	 * A set of methods of creating and customizing charts.
	 */
	public ChartBuilders cht = new ChartBuilders();
	/**
	 * A set of methods of creating exporters.
	 */
	public ExporterBuilders export = new ExporterBuilders();
	/**
	 * A set of methods of creating barcodes.
	 */
	public BarcodeBuilders bcode = new BarcodeBuilders();
	/**
	 * A set of methods of creating and customizing crosstabs.
	 */
	public CrosstabBuilders ctab = new CrosstabBuilders();	
	
	
	
	/**
	 * Creates a new report builder.
	 * The most used report builder for creating reports. It allows constructing and customizing the whole report content.
	 *
	 * @return a report builder
	 */
	public JasperReportBuilder report() {
		return new JasperReportBuilder();
	}

	/**
	 * Creates a new concatenated report builder.
	 * This report builder allows concatenating several separated reports into one single document.
	 *
	 * @return a report builder
	 */
	public JasperConcatenatedReportBuilder concatenatedReport() {
		return new JasperConcatenatedReportBuilder();
	}

	/**
	 * Creates a new concatenated report builder.
	 * This report builder allows concatenating several separated reports into one single document.
	 *
	 * @return a report builder
	 */
	public JasperConcatenatedReportBuilder concatenatedReport(JasperReportHandler jasperReportHandler) {
		return new JasperConcatenatedReportBuilder(jasperReportHandler);
	}

	//field
	public <T> FieldBuilderDR<T> field(String name, Class<T> valueClass) {
		FieldBuilderDR<T> fieldBuilder = new FieldBuilderDR<T>(name, valueClass);
		try {
			DRIDataType<? super T, T> dataType = DataTypes.detectType(valueClass);
			fieldBuilder.setDataType(dataType);
		} catch (DRException e) {
		}
		return fieldBuilder;
	}

	public <T> FieldBuilderDR<T> field(String name, DRIDataType<? super T, T> dataType) {
		Validate.notNull(dataType, "dataType must not be null");
		FieldBuilderDR<T> fieldBuilder = new FieldBuilderDR<T>(name, dataType.getValueClass());
		fieldBuilder.setDataType(dataType);
		return fieldBuilder;
	}

	//variable
	public <T> VariableBuilderDR<T> variable(ValueColumnBuilder<?, ?> column, Calculation calculation) {
		Validate.notNull(column, "column must not be null");
		return new VariableBuilderDR<T>(column, calculation);
	}

	public <T> VariableBuilderDR<T> variable(String name, ValueColumnBuilder<?, ?> column, Calculation calculation) {
		Validate.notNull(column, "column must not be null");
		return new VariableBuilderDR<T>(name, column, calculation);
	}

	public <T> VariableBuilderDR<T> variable(FieldBuilderDR<T> field, Calculation calculation) {
		Validate.notNull(field, "field must not be null");
		return new VariableBuilderDR<T>(field, calculation);
	}

	public <T> VariableBuilderDR<T> variable(String name, FieldBuilderDR<T> field, Calculation calculation) {
		return new VariableBuilderDR<T>(name, field, calculation);
	}

	public <T> VariableBuilderDR<T> variable(String fieldName, Class<?> valueClass, Calculation calculation) {
		return new VariableBuilderDR<T>(field(fieldName, valueClass), calculation);
	}

	public <T> VariableBuilderDR<T> variable(String name, String fieldName, Class<?> valueClass, Calculation calculation) {
		return new VariableBuilderDR<T>(name, field(fieldName, valueClass), calculation);
	}

	public <T> VariableBuilderDR<T> variable(DRIExpression<?> expression, Calculation calculation) {
		return new VariableBuilderDR<T>(expression, calculation);
	}

	public <T> VariableBuilderDR<T> variable(String name, DRIExpression<?> expression, Calculation calculation) {
		return new VariableBuilderDR<T>(name, expression, calculation);
	}

	//sort
	public SortBuilder asc(TextColumnBuilder<?> column) {
		return new SortBuilderDR(column).setOrderType(OrderType.ASCENDING);
	}

	public SortBuilder asc(FieldBuilderDR<?> field) {
		return new SortBuilderDR(field).setOrderType(OrderType.ASCENDING);
	}

	public SortBuilder asc(String fieldName, Class<?> valueClass) {
		return new SortBuilderDR(field(fieldName, valueClass)).setOrderType(OrderType.ASCENDING);
	}

	public SortBuilder asc(VariableBuilderDR<?> variable) {
		return new SortBuilderDR(variable).setOrderType(OrderType.ASCENDING);
	}

	public SortBuilder asc(DRIExpression<?> expression) {
		return new SortBuilderDR(expression).setOrderType(OrderType.ASCENDING);
	}

	public SortBuilder desc(TextColumnBuilder<?> column) {
		return new SortBuilderDR(column).setOrderType(OrderType.DESCENDING);
	}

	public SortBuilder desc(FieldBuilderDR<?> field) {
		return new SortBuilderDR(field).setOrderType(OrderType.DESCENDING);
	}

	public SortBuilder desc(String fieldName, Class<?> valueClass) {
		return new SortBuilderDR(field(fieldName, valueClass)).setOrderType(OrderType.DESCENDING);
	}

	public SortBuilder desc(VariableBuilderDR<?> variable) {
		return new SortBuilderDR(variable).setOrderType(OrderType.DESCENDING);
	}

	public SortBuilder desc(DRIExpression<?> expression) {
		return new SortBuilderDR(expression).setOrderType(OrderType.DESCENDING);
	}

	//hyperLink
	public HyperLinkBuilderDR hyperLink() {
		return new HyperLinkBuilderDR();
	}

	public HyperLinkBuilderDR hyperLink(String link) {
		return new HyperLinkBuilderDR(link);
	}

	public HyperLinkBuilderDR hyperLink(DRIExpression<String> linkExpression) {
		return new HyperLinkBuilderDR(linkExpression);
	}

	//margin
	public MarginBuilderDR margin() {
		return new MarginBuilderDR();
	}

	public MarginBuilderDR margin(int margin) {
		return new MarginBuilderDR(margin);
	}

	//parameter
	public <T> ParameterBuilderDR<T> parameter(String name, T value) {
		return new ParameterBuilderDR<T>(name, value);
	}

	public <T> ParameterBuilderDR<T> parameter(String name, Class<T> valueClass) {
		return new ParameterBuilderDR<T>(name, valueClass);
	}

	//query
	public QueryBuilderDR query(String text, String language) {
		return new QueryBuilderDR(text, language);
	}

	//units
	public int cm(Number value) {
		return Units.cm(value);
	}

	public int inch(Number value) {
		return Units.inch(value);
	}

	public int mm(Number value) {
		return Units.mm(value);
	}

	//template
	public ReportTemplateBuilderDR template() {
		return new ReportTemplateBuilderDR();
	}

	//table of contents
	public TableOfContentsCustomizerBuilder tableOfContentsCustomizer() {
		return new TableOfContentsCustomizerBuilder();
	}

	public TableOfContentsHeadingBuilder tableOfContentsHeading() {
		return new TableOfContentsHeadingBuilder();
	}

	public TableOfContentsHeadingBuilder tableOfContentsHeading(String label) {
		return new TableOfContentsHeadingBuilder().setLabel(label);
	}

	//dataset
	public DatasetBuilderDR dataset() {
		return new DatasetBuilderDR();
	}

}
