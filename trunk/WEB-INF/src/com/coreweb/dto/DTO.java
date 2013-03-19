package com.coreweb.dto;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

import com.coreweb.domain.IiD;
import com.coreweb.util.Misc;



public abstract class DTO  implements IiD , Comparable{
	
	private Long id = new Long(-1);	
	
	public Misc misc = new Misc();
		
	private boolean checked = false;  // agregar en todos los DTO

//	public abstract void setDomainObject(Domain dom);
	
//	public abstract Domain getDomainObject();

	public boolean esNuevo(){
		return (this.id < 0);
	}
	
	
	public boolean isChecked() {
		return checked;
	}

	public boolean getChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Long getId() {
		return id;
	}


	
	public void setId(Long id) {
		this.id = id;
	}

	public List<String> getColumnNames(){
		
		System.out.println("DTO.getColumnNames() no implementado en: "+this.getClass().getName());
		return null;
	}
	
	public List<String> getFieldNames(){
		System.out.println("DTO.getFieldNames() no implementado en: "+this.getClass().getName());
		return null;
	}
	
	
	public Object getFieldValue(String fieldName) throws Exception{
		Object value = new PropertyDescriptor(fieldName, this.getClass()).getReadMethod().invoke(this);
		return value;
		
	}
	
	public int compareTo(Object compareObject){
	    int out = 0;
	    out = this.toString().compareTo(compareObject.toString());
	    return out;
	}
	
	
}