package com.coreweb.componente;

import org.zkoss.zul.Button;
import org.zkoss.zul.Include;
import org.zkoss.zul.Vlayout;

public class WindowPopup {

	// empieza editable
	public static int NUEVO = 1;
	// no editable
	public static int SOLO_LECTURA = 2;
	// si tiene permiso, habilita el boton de edicion
	public static int EDITABLE = 3;

	public final static String DATO_NAME = "dato";
	
	
	private Object dato = null;
	private String titulo = "Titulo";
	private int modo = 0;
	private String width = "100px";
	
	private boolean clickAceptar = false;
	
	
	public void show(String zul) throws Exception{


		Vlayout vl = new Vlayout();

		Button bEdit = new Button();
		bEdit.setLabel("Edit");
		// definir el listener
		vl.getChildren().add(bEdit);
	
		
		Include inc = new Include();
		inc.setSrc(zul);
		if (this.dato != null){
			inc.setDynamicProperty(DATO_NAME, this.dato);
		}
		
		
		// falta pasarle como parametro el dato
		vl.getChildren().add(inc);
		
		BodyPopupAceptarCancelar b = new BodyPopupAceptarCancelar();
		b.addComponente("vlayout", vl);
		b.setWidthWindows(this.width);

		b.showPopupUnaColumna(this.titulo);
		this.clickAceptar = b.isClickAceptar();
	}	
	
	
	public boolean isClickAceptar() {
		return clickAceptar;
	}



	public Object getDato() {
		return dato;
	}
	public void setDato(Object dato) {
		this.dato = dato;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public int getModo() {
		return modo;
	}
	public void setModo(int modo) {
		this.modo = modo;
	}


	public String getWidth() {
		return width;
	}


	public void setWidth(String width) {
		this.width = width;
	}

	
}
