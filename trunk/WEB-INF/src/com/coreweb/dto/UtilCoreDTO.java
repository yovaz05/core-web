package com.coreweb.dto;

import java.util.ArrayList;
import java.util.List;

import com.coreweb.util.MyPair;

public class UtilCoreDTO extends DTO {

	/*
	 * Hay un utilDTO que hereda de este. Lo puse acá para poder asociarlo al
	 * control general sin cruzar el core con la aplicación
	 */

	// para gestion de usuarios (vere)
	List<MyPair> habilitado = new ArrayList<MyPair>();

	public List<MyPair> getHabilitado() {
		return habilitado;
	}

	public void setHabilitado(List<MyPair> habilitado) {
		this.habilitado = habilitado;
	}

}
