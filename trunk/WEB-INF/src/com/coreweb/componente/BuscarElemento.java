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
import com.coreweb.util.MyArray;

public class BuscarElemento {

	Class clase;
	Assembler assembler;
	String[] atributos;
	String[] nombresColumnas;
	String[] ancho;
	boolean anchoAsignado = false;
	String[] valores;
	String[] tipos = null;
	String titulo = "Buscar ...";
	String width = "400px";
	String msgVacia = "Ingrese un criterio de filtro...";
	List<String> where = new ArrayList<String>();

	// para los casos que sea uno solo
	boolean unDatoAceptar = false;
	Object[] unDato;

	Listbox listbox = new Listbox();
	BodyPopupAceptarCancelar bpac = null;
	
	public void show(String dato) throws Exception {
		
		// carga los tipos, por defecto String
		if (this.tipos == null){
			this.tipos = new String[this.atributos.length];
			for (int i = 0; i < this.atributos.length; i++) {
				this.tipos[i] = ""; // toma por defecto String
			}			
		}
		
		
		try {
			valores[1] = dato.trim(); // en el 0 está el ID
			List<Object[]> datos = this.getModelo();
			if (datos.size() == 1){
				this.unDatoAceptar = true;
				this.unDato = datos.get(0);
				return;
			}
		} catch (Exception e) {
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
			ahcT.addEventListener("onOK", ev);
			
			if (i == 0){
				ahcT.setDisabled(true);
			}
			if (i == 1){
				ahcT.setValue(valores[1]); // el dato que viene como parámetro
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
			if (this.anchoAsignado == true){
				lhc.setWidth(this.ancho[i]);
			}
			
			
			lh.getChildren().add(lhc);

			if (i==0){
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
					if (va != null){
						d = va.toString();
					}
					new Listcell(d).setParent(listItem);
				}

			}
		});

		this.showAgain();

	}
	

	




	public void showAgain(){
		bpac = new BodyPopupAceptarCancelar();
		listbox.addEventListener(Events.ON_DOUBLE_CLICK, new ListboxEventListener(bpac));
		
		bpac.addComponente("Buscar", listbox);
		bpac.setWidthWindows(this.width);
		bpac.setHighWindows("400px");
		bpac.showPopupUnaColumna(this.titulo);

	}
	
	

	
	
	protected void refreshModeloListbox() throws Exception {
		
		List<Object[]> datos = new ArrayList<Object[]>();
		String msg = this.msgVacia;
		try {
			datos = this.getModelo();
		} catch (Exception e) {
			msg = e.getMessage();
		}
		
		this.listbox.setEmptyMessage(msg);
		this.listbox.setModel(new ListModelList(datos));
	}
	
	
	private List<Object[]> getModelo() throws Exception{
		Register rr = Register.getInstance();
		List<Object[]> datos = new ArrayList<Object[]>();
//		datos = (List<Object[]>) rr.buscarElemento(clase,atributos, valores, tipos, where);
		datos = (List<Object[]>) rr.buscarElemento(clase, atributos, valores, tipos, false, true, Config.CUANTOS_BUSCAR_ELEMENTOS, false, where);
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
	
	
	public void setAnchoColumnas(String[] ancho){
		this.anchoAsignado = true;
		this.ancho = new String[ancho.length + 1];

		this.ancho[0] = "50px";

		for (int i = 1; i < this.ancho.length; i++) {
			this.ancho[i] = ancho[i - 1];
		}
		
	}


	public boolean isClickAceptar(){
		return ((this.unDatoAceptar == true)||((this.getSelectedItem() != null) && (this.bpac.clickAceptar)));
	}
	
	public MyArray getSelectedItem(){
		// para el caso que es un solo elemento
		if (this.unDatoAceptar == true){
			MyArray ma = new MyArray(this.unDato);
			return ma;
		}
		
		Listitem li = this.listbox.getSelectedItem();
		if (li == null){
			return null;
		}
		Object[] dato = (Object[])li.getValue();
		MyArray  ma = new MyArray(dato);
		return ma;
	}
	

	public DTO getSelectedItemDTO() throws Exception{
		// para el caso que es un solo elemento
		if (this.unDatoAceptar == true){
			long id = (long)this.unDato[0];
			return this.getDto(id);
		}
		
		Listitem li = this.listbox.getSelectedItem();
		if (li == null){
			return null;
		}
		Object[] dato = (Object[])li.getValue();
		long id = (long)dato[0];
		return this.getDto(id);
	}

	private DTO getDto(long id) throws Exception {
		Register rr =  Register.getInstance();
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

	public Assembler getAssembler() {
		return assembler;
	}

	public void setAssembler(Assembler assembler) {
		this.assembler = assembler;
	}

	public void addWhere(String w){
		this.where.add(w);
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


class ListboxEventListener implements EventListener {

	BodyPopupAceptarCancelar bpac;
	
	public ListboxEventListener(BodyPopupAceptarCancelar bpac){
		this.bpac = bpac;
	}
	
	@Override
	public void onEvent(Event arg0) throws Exception {
		this.bpac.getControlVM().aceptar();
	}
	
}


