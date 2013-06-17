package com.coreweb.extras.browser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.converter.sys.GridModelConverter;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

import com.coreweb.Config;
import com.coreweb.componente.BodyPopupAceptarCancelar;
import com.coreweb.componente.BuscarElemento;
import com.coreweb.control.SimpleViewModel;
import com.coreweb.domain.Register;
import com.coreweb.util.MyArray;

public abstract class Browser extends SimpleViewModel {

	@Init(superclass = true)
	public void initBrowser() {

	}

	@AfterCompose(superclass = true)
	public void afterComposeBrowser() {

	}

	// la info de olas columnas del browser
	public abstract List<ColumnaBrowser> getColumnasBrowser();
	
	// la clase principal de busqueda
	public abstract Class getEntidadPrincipal();

	
	public abstract String getTituloBrowser();
	
	private String widthWindows = Config.ANCHO_APP;
	private String higthWindows = "400px";
	
	private Grid grid = new Grid();
	private Class clase;

	// estos datos se inicializan con el metodo cargarColumnas()
	private int numeroColumnas = 0;
	private String[] nombres; // nombre de la columna
	private String[] atributos; // atributo de la clase
	private String[] valores;
	List<ColumnaBrowser> columnas; // la informacion de las columnas

	private Object[] selectedItem;
	private Row selectedRow = new Row();
	private Row selectedRowPrevio = new Row();
	private String styleSepectedRowOriginal = "";
	public static String STYLE_SELECTED_ROW = "background-color: #DAE7F6;";
	

	Radiogroup rg = new Radiogroup();
	BodyPopupAceptarCancelar bpac = new BodyPopupAceptarCancelar();
	
	private void cargarColumnas() {
		this.clase = this.getEntidadPrincipal();
		
		this.columnas = this.getColumnasBrowser();
		this.numeroColumnas = this.columnas.size() + 1; // por el id y checkbox

		this.atributos = new String[this.numeroColumnas];
		this.valores = new String[this.numeroColumnas];
		this.nombres = new String[this.numeroColumnas];

		System.out.println("========== columnas ==================================");

		nombres[0] = "Id";
		atributos[0] = "id";
		valores[0] = "";

		for (int i = 1; i < this.numeroColumnas; i++) {
			ColumnaBrowser col = this.columnas.get(i-1); // porque el ID no viene.
			nombres[i] = col.getTitulo();
			atributos[i] = col.getCampo();
			valores[i] = "";
			System.out.println(nombres[i] + " - " + atributos[i] + " - "
					+ valores[i]);

		}
		System.out.println("============================================");
	}

	public void show() throws Exception {
		// inicializa los valores de las columnas
		this.cargarColumnas();

		this.grid.setEmptyMessage("No hay elementos");

		// modelo vacio
		ListModelList model = new ListModelList(new ArrayList());
		this.grid.setModel(model);

		// Los filtros para recuperar los valores
		List<Textbox> listFiltros = new ArrayList<Textbox>();

		// los textbox de filtro
		Auxhead ah = new Auxhead();
		this.grid.getChildren().add(ah);

		// el radiobuton
		Auxheader ahcR = new Auxheader();
		ah.getChildren().add(ahcR);
		
		for (int i = 0; i < this.numeroColumnas; i++) {
			Auxheader ahc = new Auxheader();
			Textbox ahcT = new Textbox();
			ahc.getChildren().add(ahcT);
			ah.getChildren().add(ahc);
			listFiltros.add(ahcT);

			FiltroBrowserEvento ev = new FiltroBrowserEvento(this, listFiltros);
			ahcT.addEventListener("onOK", ev);

			if (i == 0) {
				ahcT.setDisabled(true);
			}
			ahcT.setValue(valores[i]); // el dato que viene como parámetro
			if (i == 1) {
				ahcT.setFocus(true);
				ahcT.focus();
			}

		}

		// la cebecera de la las columnas
		Columns lcol = new Columns();
		this.grid.getChildren().add(lcol);

		// radiobuton
		Column cRG = new Column();
		cRG.setLabel("ck");
		cRG.setWidth("30px");
		lcol.getChildren().add(cRG);

		
		for (int i = 0; i < numeroColumnas; i++) {
			Column c = new Column();
			c.setLabel(nombres[i]);

			lcol.getChildren().add(c);

			if (i == 0) {
				// es el ID
				c.setWidth("50px");
			}
		}

		this.refreshModeloGrid();

		this.grid.setRowRenderer(new GridRowRender(this, rg));

		
		this.rg.getChildren().add(this.grid);
		bpac.addComponente("Buscar", this.rg);
		bpac.setWidthWindows(this.getWidthWindows());
		bpac.setHighWindows(this.getHigthWindows());
		bpac.showPopupUnaColumna("Browser de "+this.getTituloBrowser());

	}

	protected void refreshModeloGrid() throws Exception {

		List<Object[]> datos = new ArrayList<Object[]>();
		String msg = "vacia..";
		try {
			datos = this.getModelo();
		} catch (Exception e) {
			msg = e.getMessage();
		}

		this.grid.setEmptyMessage(msg);
		this.grid.setModel(new ListModelList(datos));
	}

	/*
	 * recupera el conjunto de datos a mostrar [ToDo] mejorar para tener tipos
	 * diferentes en los valores
	 */

	private List<Object[]> getModelo() throws Exception {
		Register rr = Register.getInstance();
		List<Object[]> datos = new ArrayList<Object[]>();
		try {
			datos = (List<Object[]>) rr.buscarElemento(clase, atributos, valores,
					true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datos;
	}
	
	
	public boolean isClickAceptar(){
		return this.bpac.isClickAceptar();
	}
	

	

	public String[] getValores() {
		return valores;
	}

	public void setValores(String[] valores) {
		this.valores = valores;
	}

	public String getWidthWindows() {
		return widthWindows;
	}

	public void setWidthWindows(String widthWindows) {
		this.widthWindows = widthWindows;
	}

	public String getHigthWindows() {
		return higthWindows;
	}

	public void setHigthWindows(String higthWindows) {
		this.higthWindows = higthWindows;
	}

	public MyArray getSelectedItem() throws Exception {
		if (this.isClickAceptar() == true){
			return new MyArray(selectedItem);
		}
		throw new Exception("No se hizo click en Aceptar !!");
	}

	public void setSelectedItem(Object[] selectedItem) {
		this.selectedItem = selectedItem;
	}

	public Row getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(Row selectedRow) {
		this.selectedRow = selectedRow;
	}

	public Row getSelectedRowPrevio() {
		return selectedRowPrevio;
	}

	public void setSelectedRowPrevio(Row selectedRowPrevio) {
		this.selectedRowPrevio = selectedRowPrevio;
	}

	public String getStyleSepectedRowOriginal() {
		return styleSepectedRowOriginal;
	}

	public void setStyleSepectedRowOriginal(String styleSepectedRowOriginal) {
		this.styleSepectedRowOriginal = styleSepectedRowOriginal;
	}


	
}

class FiltroBrowserEvento implements EventListener {

	List<Textbox> listTx = null;
	Browser be = null;

	public FiltroBrowserEvento(Browser be, List<Textbox> listTx) {
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
		this.be.refreshModeloGrid();

	}

}

class GridRowRender implements RowRenderer {

	Browser br;
	Radiogroup rg;
	
	public GridRowRender(Browser br, Radiogroup rg){
		this.br = br;
		this.rg = rg;
	}
	
	
	@Override
	public void render(Row row, Object data, int arg2) throws Exception {
		// TODO Auto-generated method stub
		row.setValue(data);
		row.addEventListener("onClick", new RowEventListener(this.br));

		Object[] datosCel = (Object[]) data;
		
		// radiobutton
		Radio ra = new Radio();
		ra.setRadiogroup(this.rg);
		ra.setChecked(false);
		ra.setParent(row);

		for (int i = 0; i < datosCel.length; i++) {
			Object va = datosCel[i];
			new Label(va+"").setParent(row);
		}

	}

}

class RowEventListener implements EventListener{
	
	private Browser br;
	
	public RowEventListener(Browser br){
		this.br = br;
	}

	@Override
	public void onEvent(Event arg0) throws Exception {

		// actualizar el estylo de la row que estaba antes.
		Row rPrevia = this.br.getSelectedRow();
		rPrevia.setStyle(this.br.getStyleSepectedRowOriginal());

		// nueva row selected
		Row rClick = (Row)arg0.getTarget();
		this.br.setSelectedRow(rClick);
		this.br.setStyleSepectedRowOriginal(rClick.getStyle());
		// le ponemos el style de seleccionado
		rClick.setStyle(Browser.STYLE_SELECTED_ROW);
		
		
		this.br.setSelectedItem((Object[])rClick.getValue());
		Radio ra = (Radio)rClick.getChildren().get(0);
		ra.setChecked(true);
		ra.setFocus(true);
	}
	
}
