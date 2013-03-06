package com.coreweb.util;

import com.coreweb.domain.IiD;



public class MyArray  implements IiD {
	
	long id = -1;
	private Object pos1 = "";
	private Object pos2 = "";
	private Object pos3 = "";
	private Object pos4 = "";
	private Object pos5 = "";
	private Object pos6 = "";
	private Object pos7 = "";
	private Object pos8 = "";
	private Object pos9 = "";
	private Object pos10 = "";

	public MyArray(){
	}
	
	
	
	public String toString(){
		String out = "";
		if (this.pos1 != null){
			out = out + this.pos1;
		}
		if (this.pos2 != null){
			out = out + this.pos2;
		}
		if (this.pos3 != null){
			out = out + this.pos3;
		}
		if (this.pos4 != null){
			out = out + this.pos4;
		}
		if (this.pos5 != null){
			out = out + this.pos5;
		}
		if (this.pos6 != null){
			out = out + this.pos6;
		}
		
		return   out + " ("+this.id+")";
	}

	
	public String xtoString(){
		String out = "";
		if (this.pos1 != null){
			out = out + ""+this.pos1;
		}
		if (this.pos2 != null){
			out = out + " - "+this.pos2;
		}
		if (this.pos3 != null){
			out = out + " -p3:"+this.pos3;
		}
		if (this.pos4 != null){
			out = out + " -p4:"+this.pos4;
		}
		if (this.pos5 != null){
			out = out + "-p5:"+this.pos5;
		}
		if (this.pos6 != null){
			out = out + " -p6:"+this.pos6;
		}
		
		return  " ("+this.id+")" + out;
	}
	
	public MyArray(long id, Object pos1){
		this.id = id;
		this.pos1 = pos1;
	}

	public MyArray(long id, Object pos1, Object pos2){
		this.id = id;
		this.pos1 = pos1;
		this.pos2 = pos2;
	}

	public MyArray(long id, Object pos1, Object pos2, Object pos3){
		this.id = id;
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.pos3 = pos3;
	}

	public MyArray(long id, Object pos1, Object pos2, Object pos3, Object pos4){
		this.id = id;
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.pos3 = pos3;
		this.pos4 = pos4;
	}

	public MyArray(long id, Object pos1, Object pos2, Object pos3, Object pos4, Object pos5){
		this.id = id;
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.pos3 = pos3;
		this.pos4 = pos4;
		this.pos5 = pos5;
	}

	public MyArray(long id, Object pos1, Object pos2, Object pos3, Object pos4, Object pos5, Object pos6){
		this.id = id;
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.pos3 = pos3;
		this.pos4 = pos4;
		this.pos5 = pos5;
		this.pos6 = pos6;
	}

	public MyArray(long id, Object pos1, Object pos2, Object pos3, Object pos4, Object pos5, Object pos6, Object pos7){
		this.id = id;
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.pos3 = pos3;
		this.pos4 = pos4;
		this.pos5 = pos5;
		this.pos6 = pos6;
		this.pos7 = pos7;
	}

	public MyArray(long id, Object pos1, Object pos2, Object pos3, Object pos4, Object pos5, Object pos6, Object pos7, Object pos8){
		this.id = id;
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.pos3 = pos3;
		this.pos4 = pos4;
		this.pos5 = pos5;
		this.pos6 = pos6;
		this.pos7 = pos7;
		this.pos8 = pos8;
	}

	public MyArray(long id, Object pos1, Object pos2, Object pos3, Object pos4, Object pos5, Object pos6, Object pos7, Object pos8, Object pos9){
		this.id = id;
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.pos3 = pos3;
		this.pos4 = pos4;
		this.pos5 = pos5;
		this.pos6 = pos6;
		this.pos7 = pos7;
		this.pos8 = pos8;
		this.pos9 = pos9;
	}

	public MyArray(long id, Object pos1, Object pos2, Object pos3, Object pos4, Object pos5, Object pos6, Object pos7, Object pos8, Object pos9, Object pos10){
		this.id = id;
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.pos3 = pos3;
		this.pos4 = pos4;
		this.pos5 = pos5;
		this.pos6 = pos6;
		this.pos7 = pos7;
		this.pos8 = pos8;
		this.pos9 = pos9;
		this.pos10 = pos10;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Object getPos1() {
		return pos1;
	}

	public void setPos1(Object pos1) {
		this.pos1 = pos1;
	}

	public Object getPos2() {
		return pos2;
	}

	public void setPos2(Object pos2) {
		this.pos2 = pos2;
	}

	public Object getPos3() {
		return pos3;
	}

	public void setPos3(Object pos3) {
		this.pos3 = pos3;
	}

	public Object getPos4() {
		return pos4;
	}

	public void setPos4(Object pos4) {
		this.pos4 = pos4;
	}

	public Object getPos5() {
		return pos5;
	}

	public void setPos5(Object pos5) {
		this.pos5 = pos5;
	}

	public Object getPos6() {
		return pos6;
	}

	public void setPos6(Object pos6) {
		this.pos6 = pos6;
	}

	public Object getPos7() {
		return pos7;
	}

	public void setPos7(Object pos7) {
		this.pos7 = pos7;
	}

	public Object getPos8() {
		return pos8;
	}

	public void setPos8(Object pos8) {
		this.pos8 = pos8;
	}

	public Object getPos9() {
		return pos9;
	}

	public void setPos9(Object pos9) {
		this.pos9 = pos9;
	}

	public Object getPos10() {
		return pos10;
	}

	public void setPos10(Object pos10) {
		this.pos10 = pos10;
	}
	
	public boolean equals(Object o) {
		boolean b = false;
		try {
			MyArray aux = (MyArray)o;
			b = (this.getId() == aux.getId());
		} catch (Exception e) {
			b = false;
		}
		return b;
	}
	
}
