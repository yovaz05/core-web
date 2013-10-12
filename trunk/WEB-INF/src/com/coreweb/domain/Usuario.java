package com.coreweb.domain;

import java.util.HashSet;
import java.util.Set;

import com.coreweb.util.Misc;

public class Usuario extends Domain {
	
	private String nombre;
	private String login;
	private String clave;
	private Set<Perfil> perfiles = new HashSet<Perfil>();
	private Set<UsuarioPropiedad> usuarioPropiedades = new HashSet<UsuarioPropiedad>();
	


	public Usuario(){
		
	}
	

	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getLogin() {
		return login;
	}


	public void setLogin(String login) {
		this.login = login;
	}


	public String getClave() {
		return clave;
	}


	public void setClave(String clave) {		
		this.clave = clave;
	}


	public Set<Perfil> getPerfiles() {
		return perfiles;
	}


	public void setPerfiles(Set<Perfil> perfiles) {
		this.perfiles = perfiles;
	}




	public Set<UsuarioPropiedad> getUsuarioPropiedades() {
		return usuarioPropiedades;
	}


	public void setUsuarioPropiedades(Set<UsuarioPropiedad> usuarioPropiedades) {
		this.usuarioPropiedades = usuarioPropiedades;
	}


	public int compareTo(Object arg0) {
		System.out.println(this.getClass().getName()+" - compareTo NO IMPLEMENTADO");
		return -1;
	}
	
	public String toString(){
		String out = "";
		out += "N:"+this.nombre + " L:"+this.login+ " C:"+this.clave;
		return out;
	}

}
