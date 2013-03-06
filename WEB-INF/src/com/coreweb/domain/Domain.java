package com.coreweb.domain;

import java.io.Serializable;

public abstract class Domain implements Serializable, Comparable {

	protected Long id = new Long(-1);
	


	public boolean esNuevo() {
		return (id < 1);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void xcheckSaveData() throws Exception {
		Register register = Register.getInstance();
		register.saveObject(this);
		System.out
				.println("save data (" + this.getClass().getName() + ") ok!!");
	}



	/*
	public int compareTo(Domain o) {
		if (this.getClass().getName().compareTo(o.getClass().getName()) == 0) {
			return (this.id.compareTo(o.id));
		}
		return -1;
	}
	*/
}
