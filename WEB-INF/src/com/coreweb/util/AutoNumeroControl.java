package com.coreweb.util;

import java.util.Date;

import com.coreweb.domain.AutoNumero;
import com.coreweb.domain.Register;

public class AutoNumeroControl {

	/***
	 * Para generar un número incremental para cada clave
	 */
	
	public synchronized static long getAutoNumero(String key) throws Exception {
		long out = 0;

		Register r = Register.getInstance();
		AutoNumero an = r.getAutoNumero(key);
		if (an == null) {
			an = new AutoNumero();
			an.setKey(key);
			an.setId(out);
			an.setDescripcion("auto generado");
		}

		out = an.getNumero() + 1;
		an.setNumero(out);
		an.setFecha(new Date());
		r.saveObject(an);

		return out;
	}

}
