package com.coreweb.util;

import java.util.Comparator;

import com.coreweb.domain.IiD;



public class MyPair  implements IiD {

	//private Long id = new Long(-1);
	private String text = " ";
	
	public MyPair(){
	}

	
	
	private static long ID_NUEVO  = -1;
	Long id = new Long(ID_NUEVO);

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


	@Override
	public boolean esNuevo() {
		// TODO Auto-generated method stub
		return this.id.longValue() == ID_NUEVO;
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
		return this.text + " ("+this.getId()+")";
	}
	
	public boolean equals(Object o) {
		boolean b = false;
		try {
			MyPair aux = (MyPair)o;
			b = ((this.getId().longValue() == aux.getId().longValue())); //  &&(this.getText().compareTo(aux.getText())==0));
		} catch (Exception e) {
			b = false;
		}
		return b;
	}
}
