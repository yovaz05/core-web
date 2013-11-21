package com.coreweb.login;

import java.util.Collection;
import java.util.HashMap;
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
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Image;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Window;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;

import com.coreweb.Config;
import com.coreweb.control.Control;
import com.coreweb.dto.Assembler;
import com.coreweb.extras.alerta.Alertas;
import com.coreweb.extras.alerta.ControlAlertas;
import com.coreweb.extras.carita.Carita;
import com.coreweb.util.Misc;
import com.jgoodies.binding.BindingUtils;

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
		Session s = Sessions.getCurrent();
		s.setAttribute(Config.USUARIO, null);
		this.setMenu(menu);
		this.setMenuSistema(menuSistema);

		// poner en la session el controlInicio
		// s.setAttribute(Config.CONTROL_INICIO, this);
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

	public void menuItem(Object o, String formAlias, String paramsFromMenu) {
		String label = this.getUs().formLabel(formAlias);
		String url = this.getUs().formURL(formAlias);
		boolean deshabilitado = this.getUs().formDeshabilitado(formAlias);

		/*
		 * quita del menu la opción que no tiene permiso
		 * 
		 * if (deshabilitado == true){ return; }
		 */

		Menupopup mp = (Menupopup) o;
		Menuitem m = new Menuitem();
		// m.setId(formAlias);
		// se puede agregar un texto a cada menu m.setTooltiptext("texto");
		m.setDisabled(deshabilitado);
		m.setLabel(label);
		m.addEventListener("onClick", new MenuitemOnclick(url, formAlias,
				label, paramsFromMenu, this));

		mp.getChildren().add(m);
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

	ControlAlertas alertaControl = new ControlAlertas();

	@Command
	public void mostrarAlertas() {
		this.alertaControl.mostrarAlertas();
	}

	public String getMisAlertas() {
		String out = "Mis Alertas ";
		int cant = this.alertaControl.getCantidadAlertasNoCanceladas();
		out += "(" + cant + ")";
		return out;
	}

	public void refreshAlertas() {
		BindUtils.postNotifyChange(null, null, this, "misAlertas");
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
