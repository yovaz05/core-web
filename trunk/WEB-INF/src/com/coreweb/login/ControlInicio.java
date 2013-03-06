package com.coreweb.login;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Image;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;

import com.coreweb.Config;
import com.coreweb.control.Control;
import com.coreweb.dto.Assembler;
import com.coreweb.extras.carita.Carita;
import com.coreweb.util.Misc;




public class ControlInicio extends Control {

	private Carita carita = new Carita();

	public ControlInicio(Assembler ass) {
		super(ass);
	}

	public ControlInicio(){
		super(null);
	}

	@Init(superclass = true)
	public void init() {
		Session s = Sessions.getCurrent();
		s.setAttribute(Config.USUARIO, null);
	}

	@Command
	public void mostrarCarita() {
		this.carita.mostrarCarita();
	}

	public void menuItem(Object o, String formAlias) {
		this.menuItem(o, formAlias, "");
	}
	
	public void menuItem(Object o, String formAlias, String paramsFromMenu) {
		String label = this.getUs().formLabel(formAlias);
		String url = this.getUs().formURL(formAlias);
		boolean deshabilitado = this.getUs().formDeshabilitado(formAlias);

		Menupopup mp = (Menupopup) o;
		Menuitem m = new Menuitem();
		//m.setId(formAlias);
		// se puede agregar un texto a cada menu m.setTooltiptext("texto");
		m.setDisabled(deshabilitado);
		m.setLabel(label);
		m.addEventListener("onClick",
				new MenuitemOnclick(url, formAlias, label, paramsFromMenu, this));

		mp.getChildren().add(m);
	}

	

	public void menuItemUser(Object o) {

		Menubar mp = (Menubar) o;
		Menuitem m = new Menuitem();

		m.setLabel(this.getUs().getNombre());
		m.addEventListener("onClick", new MenuitemUser(this.getUs(), this));

		
		mp.getChildren().add(m);
	}
	
}



class MenuitemOnclick implements EventListener {

	String url = "";
	String label = "";
	String aliasFormulario = "";
	String paramsFromMenu = "";
	Control ctr = null;

	public MenuitemOnclick(String url, String aliasFormulario, String label, String paramsFromMenu, Control ctr) {
		super();
		this.url = url;
		this.aliasFormulario = aliasFormulario;
		this.label = label;
		this.paramsFromMenu = paramsFromMenu;
		this.ctr = ctr;
	}

	public void onEvent(Event event) throws Exception {
		try {
			Component main = Path.getComponent("/templateInicio");
			Include inc = (Include) main.getFellow("principalBody");
			inc.setSrc("");
			this.ctr.setTextoFormularioCorriente(this.label);

			String urlSolo = "";
			String query = "";

			int q = this.url.indexOf("?");
			if (q > 0){
				urlSolo = this.url.substring(0, q);
				query = this.url.substring(q+1);
				if (this.paramsFromMenu.trim().length() != 0){
					query = query + "&" + this.paramsFromMenu;
					
				}
			}else{
				urlSolo = this.url;
				query = this.paramsFromMenu;
			}
			
			
			if (query.length() > 1){
				Misc m = new Misc();
				Map<String, String> map = m.getQueryParam(query);
				Set<String> keys = map.keySet();
				for (String key : keys) {
					inc.setDynamicProperty(key,  map.get(key));
				}
			}
			
			
			inc.setSrc(urlSolo);
		} catch (Exception e) {
			System.out
					.println("[Error] Control Inicio cuando se hace el include del formulario "
							+ this.url + " \n" + e.getMessage());
			e.printStackTrace();
		}
	}

}


class MenuitemUser implements EventListener {

	LoginUsuarioDTO userDTO = null;
	Control ctr = null;
	
	public MenuitemUser(LoginUsuarioDTO userDTO, Control ctr){
		this.userDTO = userDTO;
		this.ctr = ctr;
	}
	
	@Override
	public void onEvent(Event arg0) throws Exception {
		
		String perStr = "Perfiles del usuario:\n";
		String[] arrPer = this.userDTO.getPerfilesDescripcion();
		for (int i = 0; i < arrPer.length; i++) {
			perStr += "  - " + arrPer[i]+"\n";
		}

		
		this.ctr.mensajeInfo(perStr);
		
	}
	
}

