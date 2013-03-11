package com.coreweb.templateABM;

import java.util.*;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.ExecutionParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Path;
import org.zkoss.zul.Include;
import org.zkoss.zul.Window;

import com.coreweb.control.GenericViewModel;
import com.coreweb.dto.DTO;



public class Page extends GenericViewModel {
	
	private Body body;
	private Toolbar tool;
	private Footer footer;
	private String aliasABM = "--AliasTemplateABM--";

	@Init(superclass = true)
	public void pageInit() {
		
	}

	@AfterCompose(superclass = true)
	public void afterComposePage(
			@ContextParam(ContextType.EXECUTION) Execution execution,
			@QueryParam("body") String bodyUrl,
			@QueryParam("aliasForm") String aliasForm) {
		try {
			if (bodyUrl == null) {
				bodyUrl = (String) execution.getAttribute("body");
				aliasForm = (String) execution.getAttribute("aliasForm");
			}
			

			String labelF = this.getUs().formLabel(aliasForm);
			this.setTextoFormularioCorriente(labelF);
			this.setAliasABM(aliasForm);

			Include body = (Include) this.mainComponent.getFellow("body");
			String oldUrl = body.getSrc();
			body.setSrc(bodyUrl);
			
			

		} catch (Exception ex) {
			System.out.println("error al incluir url del body:" + bodyUrl
					+ "\n exception:" + ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void grabarDTOCorriente(boolean refreshDTO) {
		DTO dtoCC = this.body.getDTOCorriente();
		try {
			dtoCC = this.saveDTO(dtoCC, refreshDTO);
			if (refreshDTO == true){
				this.body.setDTOCorriente(dtoCC);
			}
		} catch (Exception e) {
			System.out.println("Error grabando DTO: "+e.getMessage());
			e.printStackTrace();
		}
	}

	public void borrarDTOCorriente() {
		DTO dtoCC = this.body.getDTOCorriente();
		try {
			this.deleteDTO(dtoCC);
			this.getBody().setDTOCorriente(this.getBody().nuevoDTO());
		} catch (Exception e) {
			System.out.println("Error borrando DTO: "+e.getMessage());
			e.printStackTrace();
		}
	}

	public DTO getDTOCorriente(){
		return this.body.getDTOCorriente();
	}
	
	public void refreshDTOCorriente() throws Exception{
		DTO dtoAux;
		if (this.getDTOCorriente().esNuevo() == true){
			dtoAux = this.getBody().nuevoDTO();
		}else{
	    	long id = this.getDTOCorriente().getId();
	    	String entidad = this.getBody().getEntidadPrincipal();
	    	dtoAux = this.getDTOById(entidad, id+"");
		}
    	this.getBody().setDTOCorriente(dtoAux);

	}
	
	public String getAliasFormularioCorriente() {
		return this.aliasABM;
	}

	public void setAliasABM(String aliasABM) {
		this.aliasABM = aliasABM;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public Toolbar getTool() {
		return tool;
	}

	public void setTool(Toolbar tool) {
		this.tool = tool;
	}

	public Footer getFooter() {
		return footer;
	}

	public void setFooter(Footer footer) {
		this.footer = footer;
	}

}