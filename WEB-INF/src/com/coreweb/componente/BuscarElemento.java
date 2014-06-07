package com.coreweb.componente;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.*;

import com.coreweb.Config;
import com.coreweb.IDCore;
import com.coreweb.domain.Domain;
import com.coreweb.domain.Register;
import com.coreweb.dto.Assembler;
import com.coreweb.dto.DTO;
import com.coreweb.util.Misc;
import com.coreweb.util.MyArray;

public class BuscarElemento implements VerificaAceptarCancelar{

	Class clase;
	Assembler assembler;
	String[] atributos;
	String[] nombresColumnas;
	String[] ancho;
	boolean anchoAsignado = false;
	String[] valores;
	String[] tipos = null;
	String join = "";
	String orden = "";
	String titulo = "Buscar ...";
	String width = "400px";
	String height = "400px";
	String msgVacia = "Ingrese un criterio de filtro ...";
	List<String> where = new ArrayList<String>();

	Misc m = new Misc();

	// para los casos que sea uno solo
	boolean unDatoAceptar = false;
	Object[] unDato;

	Listbox listbox = new Listbox();
	BodyPopupAceptarCancelar bpac = null;

	public void show(String dato) throws Exception {
		this.show(dato, 0);
	}

	/**
	 * @param dato
	 *            : el string con la información para filtrar
	 * @param posFiltro
	 *            : la posición del arreglo donde hay que filtrar. 0 (cero) es
	 *            la primer posición.
	 * @throws Exception
	 */
	public void show(String dato, int posFiltro) throws Exception {

		posFiltro += 1; // en el 0 está el ID

		// carga los tipos, por defecto String
		if (this.tipos == null) {
			this.tipos = new String[this.atributos.length];
			for (int i = 0; i < this.atributos.length; i++) {
				this.tipos[i] = ""; // toma por defecto String
			}
		}

		try {
			valores[posFiltro] = dato.trim();
			List<Object[]> datos = this.getModelo();
			if (datos.size() == 1) {
				this.unDatoAceptar = true;
				this.unDato = datos.get(0);
				this.m.mensajePopupTemporal("Un registro encontrado", 1000);
				return;
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}

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
			ahcT.addEventListener(Events.ON_OK, ev);
			// quitamos el doble click, por que era molesto cuando uno queria
			// selecionar, además sólo es necesario el enter en la cabecera
			ahcT.addEventListener(Events.ON_DOUBLE_CLICK, ev);

			if (i == 0) {
				ahcT.setDisabled(true);
			}
			if (i == posFiltro) {
				ahcT.setValue(valores[posFiltro]); // el dato que viene como
													// parámetro
				ahcT.setFocus(true);
				ahcT.focus();
			}

		}

		// Los headers
		Listhead lh = new Listhead();
		listbox.getChildren().add(lh);

		for (int i = 0; i < nombresColumnas.length; i++) {
			String colName = nombresColumnas[i];
			Listheader lhc = new Listheader();
			lhc.setLabel(colName);
			if (this.anchoAsignado == true) {
				lhc.setWidth(this.ancho[i]);
			}

			lh.getChildren().add(lhc);

			if (i == 0) {
				// es el ID
				lhc.setWidth("100px");
			}
		}

		this.refreshModeloListbox();

		listbox.setItemRenderer(new ListitemRenderer() {

			@Override
			public void render(Listitem listItem, Object data, int arg2)
					throws Exception {
				listItem.setValue(data);

				Object[] row = (Object[]) data;

				for (int i = 0; i < row.length; i++) {
					Object va = row[i];
					String d = "";
					if (va != null) {
						d = va.toString();
					}
					new Listcell(d).setParent(listItem);
				}

			}
		});

		this.showAgain();

	}

	public void showAgain() {
		bpac = new BodyPopupAceptarCancelar();
		ListboxEventListener ev = new ListboxEventListener(bpac, this);
		listbox.addEventListener(Events.ON_DOUBLE_CLICK, ev);
		listbox.addEventListener(Events.ON_OK, ev);

		// hace los calculos para que quede el scroll del listbox y no del
		// windows
		this.setAnchoAlto(bpac, listbox, this.width, this.height);

		// Para centrar el listbox
		Hbox hb = new Hbox();
		hb.setWidth(bpac.getWidthWindows());
		hb.setPack("center");
		hb.setAlign("start");
		hb.getChildren().add(listbox);
		/*
		 * Hlayout hl = new Hlayout(); hl.setWidth(bpac.getWidthWindows());
		 * hl.setValign("middle"); hl.getChildren().add(listbox);
		 */
		bpac.setCheckAC(this);
		bpac.addComponente("Buscar", hb);
		bpac.showPopupUnaColumna(this.titulo);

	}

	private void setAnchoAlto(BodyPopupAceptarCancelar bpac, Listbox listbox,
			String w, String h) {
		int ancho = Integer.parseInt(w.substring(0, w.length() - 2));
		int alto = Integer.parseInt(h.substring(0, h.length() - 2));

		listbox.setWidth(w);
		listbox.setHeight(h);
		bpac.setWidthWindows((ancho + 30) + "px");
		bpac.setHighWindows((alto + 100) + "px");

	}

	protected void refreshModeloListbox() throws Exception {

		List<Object[]> datos = new ArrayList<Object[]>();
		String msg = this.msgVacia;
		try {
			datos = this.getModelo();
			msg = "Elementos encotrados: " + datos.size() + " - " + msg;

		} catch (Exception e) {
			// e.printStackTrace();
			msg = e.getMessage();
		}

		this.listbox.setEmptyMessage(msg);
		this.listbox.setModel(new ListModelList(datos));
		this.listbox.setFocus(true);
		if (datos.size() > 0) {
			this.listbox.setSelectedIndex(0);
		}
	}

	private List<Object[]> getModelo() throws Exception {
		Register rr = Register.getInstance();
		List<Object[]> datos = new ArrayList<Object[]>();
		// datos = (List<Object[]>) rr.buscarElemento(clase,atributos, valores,
		// tipos, where);
		datos = (List<Object[]>) rr.buscarElemento(clase, atributos, valores,
				tipos, false, true, Config.CUANTOS_BUSCAR_ELEMENTOS, true,
				where, this.join, this.orden);
		return datos;
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

	public void setAnchoColumnas(String[] ancho) {
		this.anchoAsignado = true;
		this.ancho = new String[ancho.length + 1];

		this.ancho[0] = "50px";

		for (int i = 1; i < this.ancho.length; i++) {
			this.ancho[i] = ancho[i - 1];
		}

	}

	public boolean isClickAceptar() {
		return ((this.unDatoAceptar == true) || ((this.getSelectedItem() != null) && (this.bpac.clickAceptar)));
	}

	public MyArray getSelectedItem() {
		// para el caso que es un solo elemento
		if (this.unDatoAceptar == true) {
			MyArray ma = new MyArray(this.unDato);
			return ma;
		}

		Listitem li = this.listbox.getSelectedItem();
		if (li == null) {
			return null;
		}
		Object[] dato = (Object[]) li.getValue();
		MyArray ma = new MyArray(dato);
		return ma;
	}

	public DTO getSelectedItemDTO() throws Exception {
		// para el caso que es un solo elemento
		if (this.unDatoAceptar == true) {
			long id = (long) this.unDato[0];
			return this.getDto(id);
		}

		Listitem li = this.listbox.getSelectedItem();
		if (li == null) {
			return null;
		}
		Object[] dato = (Object[]) li.getValue();
		long id = (long) dato[0];
		return this.getDto(id);
	}

	private DTO getDto(long id) throws Exception {
		Register rr = Register.getInstance();
		Domain dom = rr.getObject(this.clase.getName(), id);
		DTO dto = this.assembler.domainToDto(dom);
		return dto;
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

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public Assembler getAssembler() {
		return assembler;
	}

	public void setAssembler(Assembler assembler) {
		this.assembler = assembler;
	}

	public void addWhere(String w) {
		this.where.add(w);
	}

	public void addJoin(String join) {
		this.join = join;
	}

	public void addOrden(String orden) {
		this.orden = orden;
	}

	public String[] getTipos() {
		return tipos;
	}

	public void setTipos(String[] tipos) {
		// agrego el ID para todos los casos
		this.tipos = new String[tipos.length + 1];

		this.tipos[0] = Config.TIPO_NUMERICO;

		for (int i = 1; i < this.tipos.length; i++) {
			this.tipos[i] = tipos[i - 1];
		}

	}

	
	String textoError = "";
	
	@Override
	public boolean verificarAceptar() {
		textoError = "";
		
		if (this.getSelectedItem() == null){
			textoError = "Debe seleccionar un item!";
			return false;
		}
		
		return true;
	}

	@Override
	public String textoVerificarAceptar() {
		// TODO Auto-generated method stub
		return textoError;
	}

	@Override
	public boolean verificarCancelar() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String textoVerificarCancelar() {
		// TODO Auto-generated method stub
		return "";
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
		
		if (ev.getName().compareTo(Events.ON_DOUBLE_CLICK)==0){
			// de esta forma filtro el doble click de la cabecera, para que no lo capture el del listbox
			return;
		}
		
		
		String[] valores = new String[listTx.size()];
		for (int i = 0; i < listTx.size(); i++) {
			Textbox tx = (Textbox) listTx.get(i);
			valores[i] = tx.getValue().trim();
		}
		this.be.setValores(valores);
		this.be.refreshModeloListbox();

	}

}

class ListboxEventListener implements EventListener {

	BodyPopupAceptarCancelar bpac;
	BuscarElemento be;

	public ListboxEventListener(BodyPopupAceptarCancelar bpac, BuscarElemento be) {
		this.bpac = bpac;
		this.be = be;
	}

	@Override
	public void onEvent(Event ev) throws Exception {
		
		if (this.be.getSelectedItem() == null) {
			// para que no se cierre en el doble click o enter, que si o si haya
			// seleccionado algo
			return;
		}

		this.bpac.getControlVM().aceptar();
	}

}
