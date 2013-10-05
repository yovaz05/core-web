package com.coreweb.util;

import java.util.Date;

import com.coreweb.Config;
import com.coreweb.domain.AutoNumero;
import com.coreweb.domain.Register;

public class AutoNumeroControl {

	/***
	 * Para generar un número incremental para cada clave
	 */
	
	static Misc m = new Misc();
	static long SIN_LIMITE = -1;
	
	
	public synchronized static void setAutoNumero(String key, long ini, String descripcion) throws Exception {
		setAutoNumero(key,  ini, SIN_LIMITE, descripcion);
	}
	
	public synchronized static void setAutoNumero(String key, long ini, long numeroHasta, String descripcion) throws Exception {
		Register r = Register.getInstance();
		AutoNumero an = r.getAutoNumero(key);
		if (an == null) {
			an = new AutoNumero();
			an.setKey(key);
			an.setNumero(ini-1);
			an.setNumeroHasta(numeroHasta);
			an.setDescripcion(descripcion);
		}else{
			throw new Exception("Ya existe el "+key);
		}

	}

	public synchronized static long getAutoNumero(String key) throws Exception {
		return getAutoNumero(key, false);
	}

	
	public synchronized static long getAutoNumero(String key, boolean provisorio) throws Exception {
		long out = 0;
		long hasta = 0;

		Register r = Register.getInstance();
		AutoNumero an = r.getAutoNumero(key);
		if (an == null) {
			an = new AutoNumero();
			an.setKey(key);
			an.setNumero(out);
			an.setNumeroHasta(SIN_LIMITE);
			an.setDescripcion("auto generado");
		}

		out = an.getNumero() + 1;
		hasta = an.getNumeroHasta();
		if ((hasta != SIN_LIMITE)&&(out > hasta)){
			throw new Exception("Límite de numeración excedida ["+hasta+"]");
		}
		
		
		
		if (provisorio == false){
			an.setNumero(out);
			an.setFecha(new Date());
			r.saveObject(an, Config.USER_SYSTEMA);
		}

		return out;
	}

	public synchronized static String getAutoNumero(String key, int ceros) throws Exception {
		return getAutoNumero(key, ceros, false);
	}

	
	public synchronized static String getAutoNumero(String key, int ceros, boolean provisorio) throws Exception {
		long i = getAutoNumero(key, provisorio);
		String out = m.ceros(i+"", ceros);
		return out;
	}

	
	public synchronized static String getAutoNumeroKey(String key, int ceros) throws Exception {
		return getAutoNumeroKey(key, ceros, false);
	}
	
	public synchronized static String getAutoNumeroKey(String key, int ceros, boolean provisorio) throws Exception {
		String out = getAutoNumero(key, ceros, provisorio);
		return (key+"-"+out);
	}
	
	
}
