package com.coreweb.templateABM;

/************************************************
 * Importante!!
 * Las clases que heredan de Body deben tener dos método especiales
 * init y afterCompose, si o si
 * aunque estén vaciox
 * Ejemplo de como hacerlo :
 
  @Init(superclass=true)
  public void init_NOMBRE_DE_LA_CLASE() {
  }
  
  @AfterCompose(superclass = true)
  public void afterCompose_NOMBRE_DE_LA_CLASE() {
  }
  
 * 
 *************************************************/


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;

import com.coreweb.control.GenericViewModel;
import com.coreweb.dto.Assembler;
import com.coreweb.dto.DTO;



public abstract class Body extends GenericViewModel {
	
	private Page pagina;

	public Page getPagina() {
		return pagina;
	}

	public void setPagina(Page pagina) {
		this.pagina = pagina;
		this.pagina.setAss(this.getAss());
	}

	
	
	@Init(superclass = true)
	public void initBody(@ExecutionParam("pageVM") Object pageVM, @ContextParam(ContextType.EXECUTION) Execution execution) {
		
		Page page = (Page) pageVM;
		page.setBody(this);
		this.setPagina(page);
		this.setAss(this.getAss());
		String idObjeto = (String) execution.getAttribute("idObjeto");
		if (idObjeto != null){
			try{
				System.out.println("[ToDO] probar lo de recuperar un objeto por el id:"+idObjeto);
				DTO dtoAux = this.getDTOById(this.getEntidadPrincipal(), idObjeto);
				this.setDTOCorriente(dtoAux);
			}catch(Exception ex){
				System.out.println("Error recuperando DTO: "+ex.getMessage());
				ex.printStackTrace();
			}			
		}
		
	}

	@AfterCompose(superclass = true)
	public void afterComposeBodyxxx() {
	
		this.addCamposObligotorios(this.mainComponent);
		this.deshabilitarComponentes();
	
	}
	
	
	
	@GlobalCommand
	@NotifyChange("*")
	public void habilitarComponentes(){
	
		this.restoreAllReadonlyComponents();
	}	

	@GlobalCommand
	@NotifyChange("*")
	public void deshabilitarComponentes(){
		//System.out.println("=== deshabilitarComponentes:" + this.getClass().getName());
		this.readonlyAllComponents();
	}

	
	public String getAliasFormularioCorriente(){
		return this.getPagina().getAliasFormularioCorriente();
	}
	

	// se verifica antes de hacer una grabación
	public abstract boolean verificarAlGrabar();
	
	public abstract String textoErrorVerificarGrabar();
	
	public abstract Assembler getAss();
		
	public abstract DTO getDTOCorriente();

	public abstract void setDTOCorriente(DTO dto);

	public abstract DTO nuevoDTO() throws Exception ;
	
	public abstract String getEntidadPrincipal();
	
	public abstract List<DTO> getAllModel()  throws Exception;

}
