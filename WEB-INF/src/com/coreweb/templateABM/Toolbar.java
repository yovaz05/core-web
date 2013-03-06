package com.coreweb.templateABM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import org.zkoss.zul.Messagebox.Button;

import com.coreweb.IDCore;
import com.coreweb.control.GenericViewModel;
import com.coreweb.dto.DTO;



public class Toolbar extends GenericViewModel {

	private Page pagina;

	public Page getPagina() {
		return pagina;
	}

	public void setPagina(Page pagina) {
		this.pagina = pagina;
	}

	@Init(superclass = true)
	public void init(@ExecutionParam("pageVM") Object pageVM) {
		System.out.println("Init Toolbar... ");
		Page page = (Page) pageVM;
		page.setTool(this);
		this.setPagina(page);
	}

	@AfterCompose(superclass = true)
	public void afterComposeFooter() {
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
		if ((this.pagina.getDTOCorriente() == null)||(this.pagina.getDTOCorriente().esNuevo() == true)){
			esDTOnuevo = true;
		}

		boolean b = (this.isDeshabilitado() || !(this
				.operacionHabilitada(IDCore.O_EDITAR
						+ this.getAliasFormularioCorriente()))
						|| (esDTOnuevo == true)			
				);
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
		if ((this.pagina.getDTOCorriente() == null)||(this.pagina.getDTOCorriente().esNuevo() == true)){
			esDTOnuevo = true;
		}
		
		boolean b = (this.isDeshabilitado() || !(this
						.operacionHabilitada(IDCore.O_ELIMINAR
								+ this.getAliasFormularioCorriente()))
				|| (esDTOnuevo == true)				
				);
		return b;
	}

	@GlobalCommand
	@NotifyChange("*")
	public void habilitarComponentes() {
		System.out
				.println("habilitarComponentes: " + this.getClass().getName());
		this.disableAllComponents();
	}

	@GlobalCommand
	@NotifyChange("*")
	public void deshabilitarComponentes() {
		System.out.println("deshabilitarComponentes: "
				+ this.getClass().getName());
		this.restoreAllDisabledComponents();
	}

	@Command
	public void agregarItem() throws Exception {
		String texLabel = this.pagina.getTextoFormularioCorriente()
				+ " (Agregar)";
		this.setTextoFormularioCorriente(texLabel);
		System.out.println("boton add...");

		DTO dtoAux = this.getPagina().getBody().nuevoDTO();
		this.getPagina().getBody().setDTOCorriente(dtoAux);

	}

	@Command
	public void editarItem() {
		String texLabel = this.pagina.getTextoFormularioCorriente()
				+ " (Editar)";
		this.setTextoFormularioCorriente(texLabel);
		System.out.println("boton edit...");

	}

	@Command
	public void eliminarItem() {
		String texLabel = this.pagina.getTextoFormularioCorriente()
				+ " (Eliminar)";
		this.setTextoFormularioCorriente(texLabel);
		System.out.println("boton delete...");

		Button b = Messagebox.show("Eliminar el registro?", "Eliminar",
				new Messagebox.Button[] { Messagebox.Button.YES,
						Messagebox.Button.NO }, Messagebox.QUESTION, null);
		if (b.compareTo(Messagebox.Button.YES) == 0) {

			System.out.println("[ToDo] Eliminar");
			this.pagina.borrarDTOCorriente();
		}

		texLabel = this.pagina.getTextoFormularioCorriente();
		this.setTextoFormularioCorriente(texLabel);

	}

	@Command
	public void buscarItem() throws Exception {
		String texLabel = this.pagina.getTextoFormularioCorriente()
				+ " (Buscar)";
		this.setTextoFormularioCorriente(texLabel);
		System.out.println("boton search...");
		
		List listaOrdenada = this.pagina.getBody().getAllModel();
		Collections.sort(listaOrdenada);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("modelo", new ListModelList<DTO>(listaOrdenada));
		map.put("dtoCC", this.pagina.getBody().getDTOCorriente());
		map.put("body", this.pagina.getBody());
		
		Window window = (Window)Executions.createComponents("/template/Finder.zul",
		null, map);
		window.doModal();
		
		texLabel = this.pagina.getTextoFormularioCorriente();
		this.setTextoFormularioCorriente(texLabel);

	}

	public String getIdObjeto(){
		String out = "-";
		try {
			out = this.pagina.getDTOCorriente().getId()+"";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "("+out+")";
	}
	
	
}