package com.coreweb.extras.browser;

import java.lang.reflect.Constructor;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zul.Div;
import org.zkoss.zul.Include;

import com.coreweb.Config;
import com.coreweb.control.GenericViewModel;

public class Browser2ViewModel extends GenericViewModel  {

	Browser2 br2 = null;
	
	@Override
	public boolean getCondicionComponenteSiempreHabilitado() {
		// TODO Auto-generated method stub
		return false;
	}

	
	
	@Init(superclass = true)
	public void initBrowser2ViewModel(
			@ContextParam(ContextType.EXECUTION) Execution execution,
			@QueryParam("browser") String browserClass,
			@QueryParam("aliasForm") String aliasForm) throws Exception {


		if (browserClass == null) {
			browserClass = (String) execution.getAttribute("browser");
			aliasForm = (String) execution.getAttribute("aliasForm");
		}
		
		System.out.println("\n\n\n aliasForm:"+aliasForm+"\n\n\n");
		
		String labelF = this.getUs().formLabel(aliasForm);
		this.setAliasFormularioCorriente(aliasForm);
		this.setTextoFormularioCorriente(labelF);

		
		this.br2 = (Browser2) this.m.newInstance(browserClass);
		this.br2.crearBrowser();
		
		
		
		
	}

	@AfterCompose(superclass = true)
	public void afterComposeBrowser2ViewModel(){
		Div gridBrowser = (Div) this.mainComponent.getFellow("gridBrowser");
		gridBrowser.getChildren().add(this.br2.getGrid());
		
		
		String cabeceraZul = this.br2.getCabeceraZulUrl();
		if ((cabeceraZul != null)&&(cabeceraZul.trim().length() > 0)){
			Include cabeceraBrowser = (Include) this.mainComponent.getFellow("cabeceraBrowser");
			cabeceraBrowser.setDynamicProperty(Config.BROWSER2_VM, this.br2);
			cabeceraBrowser.setSrc(this.br2.getCabeceraZulUrl());
		}
		
	}
		
}
