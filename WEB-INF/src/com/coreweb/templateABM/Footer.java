package com.coreweb.templateABM;

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



public class Footer extends GenericViewModel {
	
	private Page pagina;
	private boolean yesClick = true;
	
	public Page getPagina() {
		return pagina;
	}
	public void setPagina(Page pagina) {
		this.pagina = pagina;
	}	
	
	@Init(superclass=true)
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
    public void doTask () {
		this.yesClick = false;
    	
    	if ( this.pagina.getBody().verificarAlGrabar() == false){
    		this.mensajeError(this.pagina.getBody().textoErrorVerificarGrabar());
    		return;
    	}

        Button b = Messagebox.show("Grabar los cambios y salir?", "Grabar y Salir", new Messagebox.Button[]{
                Messagebox.Button.YES, Messagebox.Button.NO }, Messagebox.QUESTION, null);
        if ((b !=null) && (b.compareTo(Messagebox.Button.YES)==0)){        	
        	this.yesClick = true;
        	this.pagina.grabarDTOCorriente(true); // graba y sale
        	
        	this.pagina.getBody().afterSave();

        	//String texLabel = this.pagina.getTextoFormularioCorriente();
    		//this.setTextoFormularioCorriente(texLabel);
        	this.getPagina().getTool().setEstadoABM(Toolbar.MODO_NADA);
        	
        }

    }
	
	@Command
    public void save () {
    	if ( this.pagina.getBody().verificarAlGrabar() == false){
    		this.mensajeError(this.pagina.getBody().textoErrorVerificarGrabar());
    		return;
    	}

    	this.yesClick = false;
        Button b = Messagebox.show("Grabar los cambios?", "Grabar", new Messagebox.Button[]{
                Messagebox.Button.YES, Messagebox.Button.NO }, Messagebox.QUESTION, null);
        if ((b !=null) && (b.compareTo(Messagebox.Button.YES)==0)){

        	this.yesClick = true;
        	this.pagina.grabarDTOCorriente(true); // graba y refresca el DTO
        	this.pagina.getBody().afterSave();
        }
    }

	
	
	@Command
	@NotifyChange("*")
    public void discard () throws Exception {
		this.yesClick = false;
        Button b = Messagebox.show("Está seguro que quiere cancelar la operación?\n Perderá los cambios desde la última vez que grabó.", "Cancelar", new Messagebox.Button[]{
                Messagebox.Button.YES, Messagebox.Button.NO }, Messagebox.QUESTION, null);
        if ((b !=null) && (b.compareTo(Messagebox.Button.YES)==0)){
        	this.yesClick = true;

        	this.pagina.refreshDTOCorriente();
        	        	
        	//String texLabel = this.pagina.getTextoFormularioCorriente();
    		//this.setTextoFormularioCorriente(texLabel);
        	this.getPagina().getTool().setEstadoABM(Toolbar.MODO_NADA);

        }        
    }
	

	@GlobalCommand
	@NotifyChange("*")
	public void habilitarComponentes(){
		this.restoreAllDisabledComponents();
		Window win =   (Window) this.mainComponent; 
		win.setVisible(true);
	}
	

	@GlobalCommand
	@NotifyChange("*")
	public void deshabilitarComponentes(){
		this.disableAllComponents();
		Window win =   (Window) this.mainComponent; 
		win.setVisible(false);
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
