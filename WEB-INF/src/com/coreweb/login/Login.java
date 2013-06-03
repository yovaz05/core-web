package com.coreweb.login;

import java.rmi.RemoteException;

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
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Panel;

import com.coreweb.Archivo;
import com.coreweb.Config;
import com.coreweb.control.Control;
import com.coreweb.dto.Assembler;





public class Login extends Control{
	
	String user = "juan";
	String pass = "juansa";
	String msg = "";
	
	public Login(Assembler ass) throws RemoteException {
		super(ass);
	}

	public Login() throws RemoteException {
		super(null);
	}
	

	@Init
	public void initLogin(){
		Session s =  Sessions.getCurrent();
		s.setAttribute(Config.LOGEADO, new Boolean(false));
	}
	
	@AfterCompose
	public void afterComposeLogin(){
	}
	
	@Command
	@NotifyChange("*")
	public void loginOk() throws Exception{
		Session s =  Sessions.getCurrent();
		
		LoginUsuario lu = new LoginUsuario();
		LoginUsuarioDTO uDto = lu.log(this.user, this.pass);

		s.setAttribute(Config.LOGEADO, uDto.isLogeado());
		s.setAttribute(Config.USUARIO, uDto);
		
		this.setUs(uDto);
		this.poneCarita(uDto.isLogeado());

		Component compTool = Path.getComponent("/templateInicio");
		Control vm = (Control)compTool.getAttribute("vm");
		vm.setUs(uDto);
		Include inc = (Include) compTool.getFellow("menu");
		inc.invalidate(); // esto hace un refresh del menu

		

		if (uDto.isLogeado() == true){

			try {
				this.m.ejecutarMetoto(Config.INIT_CLASE, Config.INIT_AFTER_LOGIN);
			} catch (Exception e) {
				System.out.println("Error: Metodo afterLogin: "+Config.INIT_AFTER_LOGIN+ " no está implementado");
			}
			
			
			
			this.setTextoFormularioCorriente("");
			this.saltoDePagina(Archivo.okLogin);
			
		}else{
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
