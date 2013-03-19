package com.coreweb.componente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.ExecutionParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.coreweb.Archivo;
import com.coreweb.util.Pair;



public class BodyPopupAceptarCancelar {

	Window windowPopupAceptarCancelar;

	String widthWindows = "400px";
	String widthColumnLabel = "100px";
	
	List<Pair> listaComponentes = new ArrayList<Pair>();
	
	// para saber si se hizo click
	BodyPopupAceptarCancelar controlInicial;
	boolean clickAceptar = false;


	@Init(superclass = true)
	public void initPopupAceptarCancelar(
			@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("control") Object control) {
		
		this.windowPopupAceptarCancelar = (Window) view;
		this.controlInicial = (BodyPopupAceptarCancelar) control;
	}

	@Command()
	public void aceptar() {
		this.controlInicial.clickAceptar = true;
		this.windowPopupAceptarCancelar.detach();
	}

	@Command()
	public void cancelar() {
		this.controlInicial.clickAceptar = false;
		this.windowPopupAceptarCancelar.detach();
	}

	
	public void addComponente(String label, Component c) {
		Pair p = new Pair(label, c);
		this.listaComponentes.add(p);
	}
	
	public void addLabel(String texto){
		this.addComponente(texto,null);
	}


	public void showPopup(String titulo){
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("control", this);
		map.put("titulo", titulo);

		
		Window window = (Window) Executions.createComponents(
				Archivo.PopupAceptarCancelar, null, map);
		Vlayout v = (Vlayout)window.getFellow("cuerpo");
		
		Grid grid = new Grid();
		grid.setWidth(this.widthWindows);
		
		// definir la columnas
		Columns cols = new Columns();
		Column col1 = new Column();
		col1.setWidth(this.widthColumnLabel);
		Column col2 = new Column();
		cols.getChildren().add(col1);
		cols.getChildren().add(col2);
		grid.getChildren().add(cols);
		
		Rows rows = new Rows();
		grid.getChildren().add(rows);
	
		
		for (int i = 0; i < this.listaComponentes.size(); i++) {
			Pair p = this.listaComponentes.get(i);
			Label label = new Label();
			label.setValue((String)p.getLeft());
			Component c = (Component)p.getRight();

			Row row = new Row();
			row.getChildren().add(label);
			row.getChildren().add(c);
			
			rows.getChildren().add(row);
		}
		v.getChildren().add(grid);
		v.setWidth(this.widthWindows);
		window.setPosition("center");

		window.setWidth(null);

		window.doModal();		
	}
	

	public void showPopupUnaColumna(String titulo){
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("control", this);
		map.put("titulo", titulo);

		
		Window window = (Window) Executions.createComponents(
				Archivo.PopupAceptarCancelar, null, map);
		Vlayout v = (Vlayout)window.getFellow("cuerpo");
		
		Grid grid = new Grid();
		grid.setWidth(this.widthWindows);
		
		// definir la columnas
		Columns cols = new Columns();
		Column col1 = new Column();
		col1.setWidth(this.widthColumnLabel);

		cols.getChildren().add(col1);
		grid.getChildren().add(cols);
		
		Rows rows = new Rows();
		grid.getChildren().add(rows);
	
		
		for (int i = 0; i < this.listaComponentes.size(); i++) {
			Pair p = this.listaComponentes.get(i);
			Component c = (Component)p.getRight();

			Row row = new Row();
			row.getChildren().add(c);
			
			rows.getChildren().add(row);
		}
		v.getChildren().add(grid);
		v.setWidth(this.widthWindows);
		window.setPosition("center");

		window.setWidth(null);

		window.doModal();		
	}

	
	
	
	public boolean isClickAceptar(){
		return this.clickAceptar;
	}

	public String getWidthWindows() {
		return widthWindows;
	}

	public void setWidthWindows(String widthWindows) {
		this.widthWindows = widthWindows;
	}

	public String getWidthColumnLabel() {
		return widthColumnLabel;
	}

	public void setWidthColumnLabel(String widthColumnLabel) {
		this.widthColumnLabel = widthColumnLabel;
	}
	
	
}

