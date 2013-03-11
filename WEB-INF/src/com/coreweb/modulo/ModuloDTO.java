package com.coreweb.modulo;

import java.util.ArrayList;
import java.util.List;

import com.coreweb.dto.DTO;
import com.coreweb.util.MyArray;


public class ModuloDTO extends DTO {

	List<MyArray> modulos = new ArrayList<MyArray>();

	public List<MyArray> getModulos() {
		return modulos;
	}

	public void setModulos(List<MyArray> modulos) {
		this.modulos = modulos;
	}

}
