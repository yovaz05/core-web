package com.coreweb.templateABM;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.Button;
import org.zkoss.zul.Messagebox.ClickEvent;

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
	public void init(@ExecutionParam("pageVM") Object pageVM) {
		System.out.println("Init Footer... ");
		Page page = (Page) pageVM;
		page.setFooter(this);
		this.setPagina(page);
	}

	@AfterCompose(superclass = true)
	public void afterComposeFooter() {
		System.out.println("AfterCompose Footer... ");
		this.deshabilitarComponentes();
	}

	
	@Command
    public void doTask () {
    	System.out.println("boton doTask...");
		this.yesClick = false;
    	
    	if ( this.pagina.getBody().verificarAlGrabar() == false){
    		this.mensajeError(this.pagina.getBody().textoErrorVerificarGrabar());
    		return;
    	}

        Button b = Messagebox.show("Grabar los cambios y salir?", "Grabar y Salir", new Messagebox.Button[]{
                Messagebox.Button.YES, Messagebox.Button.NO }, Messagebox.QUESTION, null);
        if (b.compareTo(Messagebox.Button.YES)==0){        	
        	this.yesClick = true;
        	this.pagina.grabarDTOCorriente(true); // graba y sale

        	String texLabel = this.pagina.getTextoFormularioCorriente();
    		this.setTextoFormularioCorriente(texLabel);
        }

    }
	
	@Command
    public void save () {
    	System.out.println("boton save...");
    	if ( this.pagina.getBody().verificarAlGrabar() == false){
    		this.mensajeError(this.pagina.getBody().textoErrorVerificarGrabar());
    		return;
    	}

    	this.yesClick = false;
        Button b = Messagebox.show("Grabar los cambios?", "Grabar", new Messagebox.Button[]{
                Messagebox.Button.YES, Messagebox.Button.NO }, Messagebox.QUESTION, null);
        if (b.compareTo(Messagebox.Button.YES)==0){

        	this.yesClick = true;
        	this.pagina.grabarDTOCorriente(true); // graba y refresca el DTO
        }
    }

	
	
	@Command
	@NotifyChange("*")
    public void discard () throws Exception {
		System.out.println("boton discard...");
		this.yesClick = false;
        Button b = Messagebox.show("Está seguro que quiere cancelar la operación?\n Perderá los cambios desde la última vez que grabó.", "Cancelar", new Messagebox.Button[]{
                Messagebox.Button.YES, Messagebox.Button.NO }, Messagebox.QUESTION, null);
        if (b.compareTo(Messagebox.Button.YES)==0){
        	this.yesClick = true;

        	this.pagina.refreshDTOCorriente();
        	        	
        	String texLabel = this.pagina.getTextoFormularioCorriente();
    		this.setTextoFormularioCorriente(texLabel);

        	
        }        
    }
	

	@GlobalCommand
	@NotifyChange("*")
	public void habilitarComponentes(){
		System.out.println("habilitarComponentes: " + this.getClass().getName());
		this.restoreAllDisabledComponents();
	}
	

	@GlobalCommand
	@NotifyChange("*")
	public void deshabilitarComponentes(){
		System.out.println("deshabilitarComponentes: " + this.getClass().getName());
		this.disableAllComponents();
	}
	
	public String getAliasFormularioCorriente(){
		return this.getPagina().getAliasFormularioCorriente();
	}
	
	public boolean isYesClick() {
		return yesClick;
	}
	public void setYesClick(boolean yesClick) {
		this.yesClick = yesClick;
	}

	
	

}
