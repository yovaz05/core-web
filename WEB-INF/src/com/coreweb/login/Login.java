package com.coreweb.login;

import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.servlet.ServletContext;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Include;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Panel;

import com.coreweb.Archivo;
import com.coreweb.Config;
import com.coreweb.control.Control;
import com.coreweb.dto.Assembler;

public class Login extends Control {

	String user = "";
	String pass = "";
	String msg = "";

	public Login(Assembler ass) {
		super(ass);
	}

	public Login() {
		super(null);
	}

	@Init
	public void initLogin() {
		Session s = Sessions.getCurrent();
		s.setAttribute(Config.LOGEADO, new Boolean(false));
		BindUtils.postGlobalCommand(null, null, "deshabilitarMenu", null);
		this.setTextoFormularioCorriente(" ");

	}

	@AfterCompose
	public void afterComposeLogin() {
	}

	@Command
	@NotifyChange("*")
	public void loginOk() throws Exception {

		LoginUsuario lu = new LoginUsuario();
		LoginUsuarioDTO uDto = lu.log(this.user, this.pass);

		this.setAtributoSession(Config.LOGEADO, uDto.isLogeado());
		this.setAtributoSession(Config.USUARIO, uDto);

		this.setUs(uDto);
		this.poneCarita(uDto.isLogeado());

		Component compTool = Path.getComponent("/templateInicio");
		Control vm = (Control) compTool.getAttribute("vm");
		vm.setUs(uDto);
		Include inc = (Include) compTool.getFellow("menu");
		inc.invalidate(); // esto hace un refresh del menu

		if (uDto.isLogeado() == true) {

			try {
				// guardar el control inicio de cada uno en la tabla con todas
				// las referencias, para refrescar la session
				ControlInicio miCi = (ControlInicio) this
						.getAtributoSession(Config.MI_ALERTAS);				
				Hashtable<String, ControlInicio> hci = (Hashtable<String, ControlInicio>) this
						.getAtributoContext(Config.ALERTAS);
				hci.put(uDto.getLogin(), miCi);
				//================
				
				this.m.ejecutarMetoto(Config.INIT_CLASE,
						Config.INIT_AFTER_LOGIN);
			} catch (Exception e) {
				uDto.setLogeado(false);
				System.out.println("Error: Metodo afterLogin\n "
						+ "   InitClase:" + Config.INIT_CLASE + "\n    metodo:"
						+ Config.INIT_AFTER_LOGIN);
				this.msg = "Configuración incorrecta";
				Clients.evalJavaScript("loginFaild()");
				return;
			}

			Include incS = (Include) compTool.getFellow("menuSistema");
			incS.invalidate(); // esto hace un refresh del menu

			BindUtils.postGlobalCommand(null, null, "habilitarMenu", null);

			this.setTextoFormularioCorriente(" ");
			this.saltoDePagina(Archivo.okLogin);

		} else {
			this.msg = "Usuario o clave incorrecta";
			Clients.evalJavaScript("loginFaild()");
		}
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
