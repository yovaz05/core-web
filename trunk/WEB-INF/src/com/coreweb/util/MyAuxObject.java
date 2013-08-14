package com.coreweb.util;

import com.coreweb.domain.IiD;

public abstract class MyAuxObject implements IiD {
	
	
	private static long ID_NUEVO  = -1;

	Long id = new Long(getNewId());

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return this.id;
	}


	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

	public static synchronized long getNewId(){
		ID_NUEVO--;
		return ID_NUEVO;
	}
	
	@Override
	public boolean esNuevo() {
		// TODO Auto-generated method stub
		return this.id.longValue() < 0;
	}

	
	public boolean equals(Object o) {
		boolean b = false;
		try {
			MyAuxObject aux = (MyAuxObject)o;
			b = ((this.getId().longValue() == aux.getId().longValue())); //  &&(this.getText().compareTo(aux.getText())==0));
		} catch (Exception e) {
			b = false;
		}
		return b;
	}
	
	public int compareTo(Object compareObject){
		int out = -1;
		MyAuxObject aux = (MyAuxObject)compareObject;
	    if ((this.getId().longValue() == 0)&&(aux.getId().longValue() == 0)){
		    out = this.toString().compareTo(compareObject.toString());
	    }else{
	    	out = (int)(this.getId().longValue() - aux.getId().longValue());
	    }
		
		return out;
	}
}
