package com.coreweb.login;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.ext.Disable;
import org.zkoss.zkmax.zul.Nav;
import org.zkoss.zkmax.zul.Navitem;
import org.zkoss.zul.Include;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Window;

import com.coreweb.Config;
import com.coreweb.control.Control;
import com.coreweb.domain.Register;
import com.coreweb.dto.Assembler;
import com.coreweb.extras.alerta.ControlAlertas;
import com.coreweb.extras.carita.Carita;
import com.coreweb.util.Misc;

public class ControlInicio extends Control {

	private Carita carita = new Carita();

	public ControlInicio(Assembler ass) {
		super(ass);
	}

	public ControlInicio() {
		super(null);
	}
	
	

	String menu = "";
	String menuSistema = "";

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public String getMenuSistema() {
		return menuSistema;
	}

	public void setMenuSistema(String menuSistema) {
		this.menuSistema = menuSistema;
	}

	@Init(superclass = true)
	public void init(@QueryParam("menu") String menu,
			@QueryParam("menuSistema") String menuSistema) {
		Session s = this.getSessionZK();
		s.setAttribute(Config.USUARIO, null);
		this.setMenu(menu);
		this.setMenuSistema(menuSistema);

		// el control en la session para luego manipular las alertas
		this.setAtributoSession(Config.MI_ALERTAS, this);
		this.setAtributoSession(Config.CONTROL_INICIO, this);

	}

	@GlobalCommand
	@NotifyChange("*")
	public void refreshComponentes() {
	}

	
	String colorBarra = "background-color:"+Config.COLOR_BARRA+" ; border-color: "+Config.COLOR_BARRA+" ;";
	
	public String getColorBarra(){
		return this.colorBarra;
	}
	
	public void setColorBarra(String color){
		String d = "background-color:XX ; border-color: XX ;";
		d = d.replaceAll("XX", color);
		this.colorBarra = d;
	}
	
	
	@GlobalCommand
	@NotifyChange("*")
	public void habilitarComponentes() {
		this.setMenuVisible(false);
	}

	@GlobalCommand
	@NotifyChange("*")
	public void deshabilitarComponentes() {
		this.setMenuVisible(true);
	}

	@GlobalCommand
	@NotifyChange("*")
	public void habilitarMenu() {
		this.setMenuVisible(true);
		// ver que menus popups estan visibles
		

		//Component compTool = Path.getComponent("/templateInicio");
		
/*		
		for (Iterator<Menupopup> ite = this.listaMenuPopup.iterator(); ite.hasNext();) {
			Menupopup mp = (Menupopup) ite.next();
			// ver sus hijos
			List li = mp.getChildren();
			boolean visible = false;
			for (int i = 0; i < li.size(); i++) {
				Object obj = li.get(i);
				if (obj instanceof Disable){
					Disable mi = (Disable) obj;
					visible = visible || (mi.isDisabled() == false);
				}
			}	
			if (visible == false){
				System.out.println("============== no visible:"+ mp);
				mp.setVisible(false);
				mp.getParent().removeChild(mp);
				//mp.setVisible(visible);
			}
			
		}
*/		
	}

	@GlobalCommand
	@NotifyChange("*")
	public void deshabilitarMenu() {
		this.setMenuVisible(false);
	}

	@Command
	public void mostrarCarita() {
		this.carita.mostrarCarita();
	}

	@Command
	public void informacionUsuario() {
		String perStr = "Perfiles del usuario:\n";
		String[] arrPer = this.getUs().getPerfilesDescripcion();
		for (int i = 0; i < arrPer.length; i++) {
			perStr += "  - " + arrPer[i] + "\n";
		}

		this.mensajeInfo(perStr);
	}

	public void menuItem(Object o, String formAlias) {
		this.menuItem(o, formAlias, "");
	}

	// lista de menus popups para luego mostrar si sus items están visibles o no;
	Set<Menupopup> listaMenuPopup = new HashSet<>();
	
	@SuppressWarnings("unchecked")
	public void menuItem(Object o, String formAlias, String paramsFromMenu) {
		if (this.getUs() == null){
			return;
		}
		String label = this.getUs().formLabel(formAlias);
		String url = this.getUs().formURL(formAlias);
		boolean deshabilitado = this.getUs().formDeshabilitado(formAlias);

		/*
		 * quita del menu la opción que no tiene permiso
		 * 
		 * if (deshabilitado == true){ return; }
		 */
		
		if (o instanceof Nav) {			
			Nav nv = (Nav) o;
			Navitem nvi = new Navitem();
			nvi.setDisabled(deshabilitado);
			nvi.setLabel(label);
			nvi.addEventListener("onClick", new MenuitemOnclick(url, formAlias,
					label, paramsFromMenu, this));

			nv.getChildren().add(nvi);
			
		} else {
			
			Menupopup mp = (Menupopup) o;
			
			// para guardar la lista de menus popups para luego mostrar o no si sus items están visible
			listaMenuPopup.add(mp);
			
			Menuitem m = new Menuitem();
			// m.setId(formAlias);
			// se puede agregar un texto a cada menu m.setTooltiptext("texto");
			m.setDisabled(deshabilitado);
			m.setLabel(label);
			m.addEventListener("onClick", new MenuitemOnclick(url, formAlias,
					label, paramsFromMenu, this));

			mp.getChildren().add(m);

		}
	}

	public void menuItemUser(Object o) {

		Menubar mp = (Menubar) o;
		Menuitem m = new Menuitem();

		m.setLabel(this.getUs().getNombre());
		m.addEventListener("onClick", new MenuitemUser(this.getUs(), this));

		mp.getChildren().add(m);
	}

	// Menu visible ================
	public boolean menuVisible = true;

	public boolean isMenuVisible() {
		return menuVisible;
	}

	public void setMenuVisible(boolean menuVisible) {
		this.menuVisible = menuVisible;
	}

	// ====================== ALERTAS =======================

	private boolean hayAlertas = true;
	private int cantidadAlertas = 0;
	
	public boolean isHayAlertas(){
		return this.hayAlertas;
	}
	
	public void setHayAlerta(){
		this.hayAlertas = true;
	}
	
	@Command
	public void mostrarAlertas() {
		ControlAlertas alertaControl = new ControlAlertas();
		alertaControl.mostrarAlertas();
	}

	public String getMisAlertas()  {
		String err = "";
		if (true || (this.isHayAlertas() == true)){
			try {
				this.cantidadAlertas = this.getCantidadAlertasNoCanceladas();
				this.hayAlertas = false;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				err = "[error] ";
			}
		}
		String out = "Mis Alertas "+err+"(" + this.cantidadAlertas + ")"; // ["+this+"] "+this.getLoginNombre();
		return out;
	}

	public int getCantidadAlertasNoCanceladas() throws Exception {
		int cant = 0;
		String login = this.getLoginNombre();
		Register rr = Register.getInstance();
		cant = rr.getCantidadAlertasNoCanceladas(login);
		// System.out.println("cantidad de alertas: "+cant);
		return cant;
	}

	public void refreshAlertas() {
		//this.setHayAlerta();
		System.out.println("=================== Refrescando ============");
		BindUtils.postNotifyChange(null, null, this, "*");

		//EventQueue que = EventQueues.lookup("groupTest", EventQueues.APPLICATION, true);
	}

}

class MenuitemOnclick implements EventListener {

	String url = "";
	String label = "";
	String aliasFormulario = "";
	String paramsFromMenu = "";
	Control ctr = null;

	public MenuitemOnclick(String url, String aliasFormulario, String label,
			String paramsFromMenu, Control ctr) {
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
			if (q > 0) {
				urlSolo = this.url.substring(0, q);
				query = this.url.substring(q + 1);
				if (this.paramsFromMenu.trim().length() != 0) {
					query = query + "&" + this.paramsFromMenu;

				}
			} else {
				urlSolo = this.url;
				query = this.paramsFromMenu;
			}

			if (query.length() > 1) {
				Misc m = new Misc();
				Map<String, String> map = m.getQueryParam(query);
				Set<String> keys = map.keySet();
				for (String key : keys) {
					inc.setDynamicProperty(key, map.get(key));
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

	public void onEvent_NO_ANDA(Event event) throws Exception {
		try {

			Component main = Path.getComponent("/templateInicio");

			Window window = (Window) Executions.createComponents(
					"/core/inicio/inicioPopUp.zul", main, null);

			Window WindowP = (Window) window.getFellow("templateInnicioPopUp");

			Collection<Component> cc = WindowP.getFellows();
			System.out
					.println("=======================================================I");
			for (Iterator iterator = cc.iterator(); iterator.hasNext();) {
				Component c = (Component) iterator.next();
				System.out.println("id:" + c.getId() + "  spaceOwner:"
						+ c.getSpaceOwner() + "" + c.getClass().getName());
			}
			System.out
					.println("=======================================================F");

			Include inc = (Include) WindowP.getFellow("principalBody");
			System.out.println("=== include:" + inc);
			// inc.setSrc("");
			this.ctr.setTextoFormularioCorriente(this.label);

			String urlSolo = "";
			String query = "";

			int q = this.url.indexOf("?");
			if (q > 0) {
				urlSolo = this.url.substring(0, q);
				query = this.url.substring(q + 1);
				if (this.paramsFromMenu.trim().length() != 0) {
					query = query + "&" + this.paramsFromMenu;

				}
			} else {
				urlSolo = this.url;
				query = this.paramsFromMenu;
			}

			if (query.length() > 1) {
				Misc m = new Misc();
				Map<String, String> map = m.getQueryParam(query);
				Set<String> keys = map.keySet();
				for (String key : keys) {
					inc.setDynamicProperty(key, map.get(key));
				}
			}

			inc.setSrc(urlSolo);

			window.doModal();

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

	public MenuitemUser(LoginUsuarioDTO userDTO, Control ctr) {
		this.userDTO = userDTO;
		this.ctr = ctr;
	}

	@Override
	public void onEvent(Event arg0) throws Exception {

		String perStr = "Perfiles del usuario:\n";
		String[] arrPer = this.userDTO.getPerfilesDescripcion();
		for (int i = 0; i < arrPer.length; i++) {
			perStr += "  - " + arrPer[i] + "\n";
		}

		this.ctr.mensajeInfo(perStr);

	}

}


class AlertaEvento implements EventListener{

	ControlInicio ctr = null;
	
	public AlertaEvento(ControlInicio ctr){
		this.ctr = ctr;
	}
	
	@Override
	public void onEvent(Event arg0) throws Exception {
		ctr.refreshAlertas();
		
	}
	
}

