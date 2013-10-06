package com.coreweb.templateABM;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Include;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.Button;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Window;

import com.coreweb.Config;
import com.coreweb.control.GenericViewModel;
import com.coreweb.dto.DTO;

public class Footer extends GenericViewModel {

	private Page pagina;
	private boolean yesClick = true;

	public Page getPagina() {
		return pagina;
	}

	public void setPagina(Page pagina) {
		this.pagina = pagina;
	}

	@Init(superclass = true)
	public void initFooter(@ExecutionParam("pageVM") Object pageVM) {
		Page page = (Page) pageVM;
		this.setPagina(page);
		page.setFooter(this);
	}

	@AfterCompose(superclass = true)
	public void afterComposeFooter() {
		this.deshabilitarComponentes();
	}


	
	public String getAliasFormularioCorriente() {
		return this.getPagina().getAliasFormularioCorriente();
	}

	@Command
	public void doTask() {
		boolean siGrabo = this.saveDato(true, "Grabar los cambios y salir?");
		
		if (siGrabo == true){
			this.getPagina().getTool().setEstadoABM(Toolbar.MODO_NADA);
		}
	}

	@Command
	public void save(){
		this.saveDato(false, "");
	}
	
	public boolean saveDato(boolean siPregunta, String texto) {
		
		if (this.pagina.getBody().verificarAlGrabar() == false) {
			this.mensajeError(this.pagina.getBody().textoErrorVerificarGrabar());
			return false;
		}

		boolean out = false;
		this.yesClick = false;

		if ((siPregunta==false)||( this.mensajeSiNo(texto)== true)){			
			try {
				this.pagina.grabarDTOCorriente(true); // graba y refresca el DTO
				this.pagina.getBody().afterSave();
				this.yesClick = true;
				this.mensajePopupTemporal("Información grabada!!", 1000);
				out = true;
			} catch (Exception e) {
				e.printStackTrace();
				this.mensajeError("Error grabando la información\n"
						+ e.getMessage());
				out = false;
			}
		}
		return out;
	}

	
	@Command
	@NotifyChange("*")
	public void discard() throws Exception {
		this.yesClick = false;
		
		if ( this.mensajeSiNo("Está seguro que quiere cancelar la operación?\n Perderá los cambios desde la última vez que grabó.")==true){
			this.yesClick = true;

			this.pagina.refreshDTOCorriente();

			// String texLabel = this.pagina.getTextoFormularioCorriente();
			// this.setTextoFormularioCorriente(texLabel);
			this.getPagina().getTool().setEstadoABM(Toolbar.MODO_NADA);
		}
		
		/*
		Button b = Messagebox
				.show("Está seguro que quiere cancelar la operación?\n Perderá los cambios desde la última vez que grabó.",
						"Cancelar", new Messagebox.Button[] {
								Messagebox.Button.YES, Messagebox.Button.NO },
						Messagebox.QUESTION, null);
		if ((b != null) && (b.compareTo(Messagebox.Button.YES) == 0)) {
			this.yesClick = true;

			this.pagina.refreshDTOCorriente();

			// String texLabel = this.pagina.getTextoFormularioCorriente();
			// this.setTextoFormularioCorriente(texLabel);
			this.getPagina().getTool().setEstadoABM(Toolbar.MODO_NADA);

		}
		*/
	}

	@GlobalCommand
	@NotifyChange("*")
	public void habilitarComponentes() {
		this.restoreAllDisabledComponents();
		Window win = (Window) this.mainComponent;
		win.setVisible(true);
	}

	@GlobalCommand
	@NotifyChange("*")
	public void deshabilitarComponentes() {
		this.disableAllComponents();
		Window win = (Window) this.mainComponent;
		win.setVisible(false);
		BindUtils.postGlobalCommand(null, null, "refreshComponentes", null);
	}

	public boolean isYesClick() {
		return yesClick;
	}

	public void setYesClick(boolean yesClick) {
		this.yesClick = yesClick;
	}

	@Override
	public boolean getCondicionComponenteSiempreHabilitado() {
		// TODO Auto-generated method stub
		return true;
	}

}
