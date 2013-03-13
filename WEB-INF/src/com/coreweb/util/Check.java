package com.coreweb.util;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Constraint;

public class Check {
	
	public static String NO_TODAY = "no today : Fecha de Hoy no permitida";
	public static String NO_FUTURE = "no future : Fecha Futura no permitida";
	public static String NO_PAST = "no past : Fecha pasada no permitida";	
	public static String AFTER = "after ";
	public static String BEFORE = "before ";
	public static String BETWEEN = "between ";
	
	public static String NO_NEGATIVO = "no negative : Este campo no admite valor negativo";
	public static String NO_POSITIVO = "no positive : Este campo no admite valor positivo";
	public static String NO_CERO = "no zero : Este campo no admite valor (0) 'cero'";
	
	public static String NO_VACIO = "no empty : Este campo es obligatorio";
	public static String EMAIL = "/.+@.+\\.[a-z]+/:Debe ingresar un correo valido";
	
	
	
	/******************* CONSTRAINT PARA FECHAS *****************/	
	
	public String getNoHoy(){
		return NO_TODAY;
	}
	
	public String getNoFuturo(){
		return NO_FUTURE;
	}
	
	public String getNoPasado(){
		return NO_PAST;
	}
	
	public String getMayorIgualAfecha(String fecha) {
		return AFTER + fecha;
	}
	
	public String getMenorIgualAfecha(String fecha) {
		return BEFORE + fecha;
	}

	public String getIntervaloFecha(String intervalo) {
		return BETWEEN + intervalo;
	}
	
	/***************************************************************/
	
	
	/****************** CONSTRAINT PARA NUMEROS ********************/
	
	public String getNoNegativo(){
		return NO_NEGATIVO;
	}
	
	public String getNoPositivo(){
		return NO_POSITIVO;
	}
	
	public String getNoCero(){
		return NO_CERO;
	}
	
	/***************************************************************/
	
	
	/********************** OTROS CONSTRAINTS **********************/
	
	public String getNoVacio(){
		return NO_VACIO;
	}
	
	public String getEmail(){
		return EMAIL;
	}
	
	/***************************************************************/
	
	
	
	/****************** CONSTRAINTS PERSONALIZADOS *****************/
	
	public MiConstraint getMayorA(int value) {
		return new MiConstraint(MiConstraint.MAYOR_A, value);
	}
	
	public MiConstraint getMenorA(int value) {
		return new MiConstraint(MiConstraint.MENOR_A, value);
	}
	
	/***************************************************************/
}



class MiConstraint implements Constraint {

	public static final int MAYOR_A = 1;
	public static final int MENOR_A = 2;	
	private int constraint = 0;
	private int value = 0;

	public MiConstraint(int constraint, int value){
		 this.constraint = constraint;
		 this.value = value;
	}

	
	@Override
	public void validate(Component comp, Object value)
			throws WrongValueException {
		
		if (this.constraint == MAYOR_A) {
			if (value == null || ((Integer)value).intValue() < this.value)
	            throw new WrongValueException(comp, "el valor debe ser mayor a "+ this.value);
		}
		
		if (this.constraint == MENOR_A) {
			if (value == null || ((Integer)value).intValue() > this.value)
	            throw new WrongValueException(comp, "el valor debe ser menor a "+ this.value);
		}		
	}	
}
