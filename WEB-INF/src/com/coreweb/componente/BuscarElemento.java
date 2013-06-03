package com.coreweb.componente;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.*;

import com.coreweb.domain.Register;

public class BuscarElemento {

	String clase = "";
	String[] atributos;
	String[] nombresColumnas;
	String[] valores;
	String titulo = "Buscar ...";

	Listbox listbox = new Listbox();

	public void show() throws Exception {

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
		}

		// Los headers
		Listhead lh = new Listhead();
		listbox.getChildren().add(lh);

		for (int i = 0; i < nombresColumnas.length; i++) {
			String colName = nombresColumnas[i];
			Listheader lhc = new Listheader();
			lhc.setLabel(colName);
			lh.getChildren().add(lhc);
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

		BodyPopupAceptarCancelar bpac = new BodyPopupAceptarCancelar();
		bpac.addComponente("Buscar", listbox);
		bpac.setHighWindows("400px");
		bpac.showPopupUnaColumna(this.titulo);

	}

	public void refreshModeloListbox() throws Exception {
		Register rr = Register.getInstance();

		List<Object[]> datos = (List<Object[]>) rr.buscarElemento(clase,
				atributos, valores);
		this.listbox.setModel(new ListModelList(datos));
	}

	public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
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
		System.out.println("==== evento: " + ev.getName() + "  id:"
				+ ev.getTarget().getId());

		String[] valores = new String[listTx.size()];
		for (int i = 0; i < listTx.size(); i++) {
			Textbox tx = (Textbox) listTx.get(i);
			valores[i] = tx.getValue();
		}
		this.be.setValores(valores);
		this.be.refreshModeloListbox();

	}

}
