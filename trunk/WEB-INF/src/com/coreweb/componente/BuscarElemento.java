package com.coreweb.componente;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.*;

import com.coreweb.domain.Register;
import com.coreweb.util.MyArray;

public class BuscarElemento {

	Class clase;
	String[] atributos;
	String[] nombresColumnas;
	String[] valores;
	String titulo = "Buscar ...";
	String width = "400px";
	String msgVacia = "Ingrese un criterio filtro...";

	Listbox listbox = new Listbox();
	BodyPopupAceptarCancelar bpac = new BodyPopupAceptarCancelar();
	
	public void show() throws Exception {

		this.listbox.setEmptyMessage(this.msgVacia);

		// Modelo
		ListModelList model = new ListModelList(new ArrayList());
		listbox.setModel(model);

		// Los filtros
		List<Textbox> listFiltros = new ArrayList<Textbox>(); // para recuperar
																// los valores
		Auxhead ah = new Auxhead();
		listbox.getChildren().add(ah);

		for (int i = 0; i < valores.length; i++) {
			Auxheader ahc = new Auxheader();
			Textbox ahcT = new Textbox();
			ahc.getChildren().add(ahcT);
			ah.getChildren().add(ahc);
			listFiltros.add(ahcT);

			FiltroBuscarElementoEvento ev = new FiltroBuscarElementoEvento(
					this, listFiltros);
			ahcT.addEventListener("onOK", ev);
			
			if (i == 0){
				ahcT.setDisabled(true);
			}
		
		}

		// Los headers
		Listhead lh = new Listhead();
		listbox.getChildren().add(lh);

		for (int i = 0; i < nombresColumnas.length; i++) {
			String colName = nombresColumnas[i];
			Listheader lhc = new Listheader();
			lhc.setLabel(colName);
			lh.getChildren().add(lhc);

			if (i==0){
				// es el ID
				lhc.setWidth("100px");
			}
		}

		listbox.setItemRenderer(new ListitemRenderer() {

			@Override
			public void render(Listitem listItem, Object data, int arg2)
					throws Exception {
				listItem.setValue(data);

				Object[] row = (Object[]) data;

				for (int i = 0; i < row.length; i++) {
					Object va = row[i];
					new Listcell(va.toString()).setParent(listItem);
				}

			}
		});

		
		
		bpac.addComponente("Buscar", listbox);
		bpac.setWidthWindows(this.width);
		bpac.setHighWindows("400px");
		bpac.showPopupUnaColumna(this.titulo);

	}

	public void refreshModeloListbox() throws Exception {
		Register rr = Register.getInstance();
		List<Object[]> datos = new ArrayList<Object[]>();
		
		String msg = this.msgVacia;
		try {
			datos = (List<Object[]>) rr.buscarElemento(clase,
					atributos, valores);
		} catch (Exception e) {
			msg = e.getMessage();
		}
		
		this.listbox.setEmptyMessage(msg);
		this.listbox.setModel(new ListModelList(datos));
	}

	public Class getClase() {
		return clase;
	}

	public void setClase(Class clase) {
		this.clase = clase;
	}

	public String[] getAtributos() {
		return atributos;
	}

	public void setAtributos(String[] att) {
		// agrego el ID para todos los casos
		this.atributos = new String[att.length + 1];
		this.valores = new String[att.length + 1];

		this.atributos[0] = "id";
		this.valores[0] = "";

		for (int i = 1; i < this.atributos.length; i++) {
			this.atributos[i] = att[i - 1];
			valores[i] = "";
		}

	}

	public String[] getNombresColumnas() {
		return nombresColumnas;
	}

	public void setNombresColumnas(String[] nomCol) {
		// agrego el ID para todos los casos
		this.nombresColumnas = new String[nomCol.length + 1];

		this.nombresColumnas[0] = "Id";

		for (int i = 1; i < this.nombresColumnas.length; i++) {
			this.nombresColumnas[i] = nomCol[i - 1];
		}

	}

	public boolean isClickAceptar(){
		return ((this.getSelectedItem() != null) && (this.bpac.clickAceptar));
	}
	
	public MyArray getSelectedItem(){
		Listitem li = this.listbox.getSelectedItem();
		if (li == null){
			return null;
		}
		Object[] dato = (Object[])li.getValue();
		MyArray  ma = new MyArray(dato);
		return ma;
	}
	
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String[] getValores() {
		return valores;
	}

	public void setValores(String[] valores) {
		this.valores = valores;
	}

	public Listbox getListbox() {
		return listbox;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	
}

class FiltroBuscarElementoEvento implements EventListener {

	List<Textbox> listTx = null;
	BuscarElemento be = null;

	public FiltroBuscarElementoEvento(BuscarElemento be, List<Textbox> listTx) {
		this.be = be;
		this.listTx = listTx;
	}

	@Override
	public void onEvent(Event ev) throws Exception {
		// TODO Auto-generated method stub
		
		String[] valores = new String[listTx.size()];
		for (int i = 0; i < listTx.size(); i++) {
			Textbox tx = (Textbox) listTx.get(i);
			valores[i] = tx.getValue().trim();
		}
		this.be.setValores(valores);
		this.be.refreshModeloListbox();

	}

}