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
	public void save() {
		// no es necesario un NotifyChange porque desde el zul se invoca el
		// globalCommand refreshComponents
		this.saveDato();
		this.yesClick = true;
	}

	private boolean saveDato() {

		boolean out = false;

		if (this.pagina.getBody().verificarAlGrabar() == false) {
			this.mensajeError(this.pagina.getBody().textoErrorVerificarGrabar());
			return false;
		}

		try {
			this.pagina.grabarDTOCorriente(); // graba
			this.pagina.getBody().afterSave();
			this.mensajePopupTemporal("Información grabada!!", 1000);
			out = true;
		} catch (Exception e) {
			e.printStackTrace();
			this.mensajeError("Error grabando la información\n"
					+ e.getMessage());
			out = false;
		}

		return out;
	}

	@Command
	public void discard() throws Exception {
		// no es necesario un NotifyChange porque desde el zul se invoca el
		// globalCommand deshabilitarComponentes
		this.yesClick = false;
		boolean siDirty = this.getPagina().getBody().siDirtyDTO();

		// no hay cambios, sale nomas
		if (siDirty == false) {
			this.yesClick = true;
			this.getPagina().getTool().setEstadoABM(Toolbar.MODO_NADA);
			return;
		}

		// preguntar que hacer con los cambios
		int click = this
				.mensajeSiNoCancelar("Grabar los cambios antes de salir?");

		if (click == Config.BOTON_CANCEL) {
			// vuelve a la pantalla
			this.yesClick = false;
			return;
		}
		if (click == Config.BOTON_YES) {
			this.saveDato();
		}
		if (click == Config.BOTON_NO) {
			this.m.mensajePopupTemporalWarning("Información NO grabada!!", 1000);
			this.pagina.refreshDTOCorriente();
		}

		this.getPagina().getTool().setEstadoABM(Toolbar.MODO_NADA);
		this.yesClick = true;
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
