package com.coreweb.util;

import java.util.Comparator;

public class ComparadorArrayObjetos implements Comparator<Object[]>{

	int n;
	
	public ComparadorArrayObjetos(int n){
		this.n = n;
	}
	@Override
	public int compare(Object[] o1, Object[] o2) {
		return (o2[this.n].toString()).compareTo(o1[this.n].toString());
	}
	
}
