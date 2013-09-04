package com.coreweb.util;

import java.util.Comparator;

import com.coreweb.domain.IiD;

public class MyPair extends MyAuxObject {

	// private Long id = new Long(-1);
	private String text = " ";

	public MyPair() {
	}

	public MyPair(long id, String text) {
		this.setId(id);
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String toString() {
		String out = this.text + " (" + this.getId() + ")";
		if (this.esNuevo() == true) {
			out = " ";
		}
		return out;
	}

	public Object clone() {
		Object clone = null;
		clone = super.clone();
		return clone;
	}

	/*
	String pos1 = "";

	public String getPos1() {
		System.out.println("==================getPost1:" + this.pos1);
		return pos1;
	}

	public void setPos1(String pos1) {
		System.out.println("==================setPost1:" + pos1);
		this.pos1 = pos1;
	}
*/
}
