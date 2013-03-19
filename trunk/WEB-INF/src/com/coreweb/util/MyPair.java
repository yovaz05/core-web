package com.coreweb.util;

import java.util.Comparator;

import com.coreweb.domain.IiD;



public class MyPair  implements IiD {

	private Long id = new Long(0);
	private String text = " ";
	
	public MyPair(){
	}

	public MyPair(long id, String text){
		this.id = id;
		this.text = text;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String toString(){
		return this.text + " ("+this.id+")";
	}
	
	public boolean equals(Object o) {
		boolean b = false;
		try {
			MyPair aux = (MyPair)o;
			b = ((this.getId() == aux.getId())); //  &&(this.getText().compareTo(aux.getText())==0));
		} catch (Exception e) {
			b = false;
		}
		return b;
	}
}
