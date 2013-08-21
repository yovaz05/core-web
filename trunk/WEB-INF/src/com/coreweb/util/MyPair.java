package com.coreweb.util;

import java.util.Comparator;

import com.coreweb.domain.IiD;



public class MyPair extends MyAuxObject {

	//private Long id = new Long(-1);
	private String text = " ";
	
	public MyPair(){
	}

	
	public MyPair(long id, String text){
		this.setId(id);
		this.text = text;
	}


	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String toString(){
		String out = this.text + " ("+this.getId()+")";
		if (this.esNuevo() == true ){
			out = " ";
		}
		return out;
	}
	

}
