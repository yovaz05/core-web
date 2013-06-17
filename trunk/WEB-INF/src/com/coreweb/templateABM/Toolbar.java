package com.coreweb.templateABM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.ExecutionParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.ext.Disable;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;
import org.zkoss.zul.Messagebox.Button;

import com.coreweb.Config;
import com.coreweb.IDCore;
import com.coreweb.control.GenericViewModel;
import com.coreweb.dto.Assembler;
import com.coreweb.dto.DTO;
import com.coreweb.extras.browser.Browser;
import com.coreweb.util.MyArray;

public class Toolbar extends GenericViewModel {

	private Page pagina;

	public Page getPagina() {
		return pagina;
	}

	public void setPagina(Page pagina) {
		this.pagina = pagina;
	}

	@Init(superclass = true)
	public void initToolbar(@ExecutionParam("pageVM") Object pageVM) {

		Page page = (Page) pageVM;
		this.setPagina(page);
		page.setTool(this);
	}

	@AfterCompose(superclass = true)
	public void afterComposeToolbar(
			@ContextParam(ContextType.VIEW) Component xview) {
		Selectors.wireEventListeners(xview, this);
		Selectors.wireComponents(xview, this, false);

		this.deshabilitarComponentes();

	}

	public String getAliasFormularioCorriente() {
		return this.getPagina().getAliasFormularioCorriente();
	}

	public boolean getAgregarDeshabilitado() throws Exception {
		boolean b = this.isDeshabilitado()
				|| !(this.operacionHabilitada(IDCore.O_AGREGAR
						+ this.getAliasFormularioCorriente()));
		return b;
	}

	public boolean getEditarDeshabilitado() throws Exception {
		boolean esDTOnuevo = false;
		if ((this.pagina.getDTOCorriente() == null)
				|| (this.pagina.getDTOCorriente().esNuevo() == true)) {
			esDTOnuevo = true;
		}

		boolean b = (this.isDeshabilitado()
				|| !(this.operacionHabilitada(IDCore.O_EDITAR
						+ this.getAliasFormularioCorriente())) || (esDTOnuevo == true));
		return b;
	}

	public boolean getBuscarDeshabilitado() throws Exception {
		boolean b = this.isDeshabilitado()
				|| !(this.operacionHabilitada(IDCore.O_BUSCAR
						+ this.getAliasFormularioCorriente()));
		return b;
	}

	public boolean getEliminarDeshabilitado() throws Exception {
		boolean esDTOnuevo = false;
		if ((this.pagina.getDTOCorriente() == null)
				|| (this.pagina.getDTOCorriente().esNuevo() == true)) {
			esDTOnuevo = true;
		}

		boolean b = (this.isDeshabilitado()
				|| !(this.operacionHabilitada(IDCore.O_ELIMINAR
						+ this.getAliasFormularioCorriente())) || (esDTOnuevo == true));
		return b;
	}

	@GlobalCommand
	@NotifyChange("*")
	public void habilitarComponentes() {
		this.disableAllComponents();
	}

	@GlobalCommand
	@NotifyChange("*")
	public void deshabilitarComponentes() {
		this.restoreAllDisabledComponents();
	}

	@Command
	public void agregarItem() throws Exception {
		String texLabel = this.pagina.getTextoFormularioCorriente()
				+ " (Agregar)";
		this.setTextoFormularioCorriente(texLabel);

		DTO dtoAux = this.getPagina().getBody().nuevoDTO();
		this.getPagina().getBody().setDTOCorriente(dtoAux);

	}

	@Command
	public void editarItem() {
		String texLabel = this.pagina.getTextoFormularioCorriente()
				+ " (Editar)";
		this.setTextoFormularioCorriente(texLabel);
	}

	@Command
	public void eliminarItem() {
		String texLabel = this.pagina.getTextoFormularioCorriente()
				+ " (Eliminar)";
		this.setTextoFormularioCorriente(texLabel);

		Button b = Messagebox.show("Eliminar el registro?", "Eliminar",
				new Messagebox.Button[] { Messagebox.Button.YES,
						Messagebox.Button.NO }, Messagebox.QUESTION, null);
		if (b.compareTo(Messagebox.Button.YES) == 0) {

			this.pagina.borrarDTOCorriente();
		}

		texLabel = this.pagina.getTextoFormularioCorriente();
		this.setTextoFormularioCorriente(texLabel);

	}

	@Command
	public void buscarItemBr() throws Exception {
		String texLabel = this.pagina.getTextoFormularioCorriente()
				+ " (Buscar)";
		this.setTextoFormularioCorriente(texLabel);

		Browser br = this.getPagina().getBody().getBrowser();
		br.show();
		if (br.isClickAceptar() == true){
			MyArray m = br.getSelectedItem();
			// obtener un DTO del MyArray
			
			Assembler as = this.getPagina().getBody().getAss();
			DTO dto = as.getDto(this.getPagina().getBody().getEntidadPrincipal(), m);
			this.getPagina().getBody().setDTOCorriente(dto);
			System.out.println("DTO:"+dto);
			System.out.println(this.getPagina().getBody().getClass().getName() + " " + this.getPagina().getBody());
		}
		

	}

	// este es el original, el que anda ahora
	@Command
	public void buscarItem() throws Exception {
		String texLabel = this.pagina.getTextoFormularioCorriente()
				+ " (Buscar)";
		this.setTextoFormularioCorriente(texLabel);

		List listaOrdenada = this.pagina.getBody().getAllModel();
		Collections.sort(listaOrdenada);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("modelo", new ListModelList<DTO>(listaOrdenada));
		map.put("dtoCC", this.pagina.getBody().getDTOCorriente());
		map.put("body", this.pagina.getBody());

		Window window = (Window) Executions.createComponents(
				"/core/template/Finder.zul", null, map);
		window.doModal();

		texLabel = this.pagina.getTextoFormularioCorriente();
		this.setTextoFormularioCorriente(texLabel);
	}

	@Wire
	Toolbarbutton buscar2;

	@Listen("onClick=#buscar2")
	public void buscarMetodo() throws Exception {
		System.out.println("=============== buscar 2 ==============");
		String texLabel = this.pagina.getTextoFormularioCorriente()
				+ " (Buscar)";
		this.setTextoFormularioCorriente(texLabel);

		List listaOrdenada = this.pagina.getBody().getAllModel();
		Collections.sort(listaOrdenada);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("modelo", new ListModelList<DTO>(listaOrdenada));
		map.put("dtoCC", this.pagina.getBody().getDTOCorriente());
		map.put("body", this.pagina.getBody());

		Window window = (Window) Executions.createComponents(
				"/core/template/Finder.zul", null, map);
		window.doModal();

		texLabel = this.pagina.getTextoFormularioCorriente();
		this.setTextoFormularioCorriente(texLabel);

		BindUtils.postGlobalCommand(null, null, "refreshComponentes", null);

		// System.out.println("================= F-global command refresh componente");
		// System.out.println("================= I-Readonly componentes");
		// this.getPagina().getBody().readonlyAllComponents();
		// System.out.println("================= F-Readonly componentes");
		// BindUtils.postGlobalCommand(null, null, "refreshComponentes", null);
		// BindUtils.postNotifyChange(null, null, this, "*");
		// this.getPagina().getBody().readonlyAllComponents();

	}

	public String getIdObjeto() {
		String out = "-";
		try {
			out = this.pagina.getDTOCorriente().getId() + "";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "(" + out + ")";
	}

	@Override
	public boolean getCondicionComponenteSiempreHabilitado() {
		// TODO Auto-generated method stub
		return true;
	}

}