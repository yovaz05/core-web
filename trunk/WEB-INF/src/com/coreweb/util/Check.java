package com.coreweb.util;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.CustomConstraint;
import org.zkoss.zul.SimpleConstraint;

public class Check {
	
	public static String NO_TODAY = "no today : Fecha de Hoy no permitida";
	public static String NO_FUTURE = "no future : Fecha Futura no permitida";
	public static String NO_PAST = "no past : Fecha pasada no permitida";	
	public static String AFTER = "after ";
	public static String BEFORE = "before ";
	public static String BETWEEN = "between ";
	
	public static String NO_VACIO = "no empty";
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
	
	
	
	/****************** CONSTRAINTS PERSONALIZADOS *****************/
	
	public MiConstraint getMayorA(int value) {
		return new MiConstraint(MiConstraint.MAYOR_A, value);
	}
	
	public MiConstraint getMenorA(int value) {
		return new MiConstraint(MiConstraint.MENOR_A, value);
	}
	
	public MiConstraint getRuc(){
		return new MiConstraint(MiConstraint.RUC);
	}
	
	public MiConstraint getNoVacio(){
		return new MiConstraint(MiConstraint.NO_EMPTY);
	}
	
	public MiConstraint getNoNegativo(){
		return new MiConstraint(MiConstraint.NO_NEGATIVO);
	}
	
	/***************************************************************/
}



class MiConstraint extends SimpleConstraint implements Constraint, CustomConstraint{

	public static final int MAYOR_A = 1;
	public static final int MENOR_A = 2;
	public static final int RUC = 3;
	public static final int NO_EMPTY = 4;
	public static final int NO_NEGATIVO = 5;
	
	private int constraint = 0;
	private int value = 0;

	public MiConstraint(int constraint, int value){
		super("");
		this.constraint = constraint;
		this.value = value;
	}
	
	public MiConstraint(int constraint){
		super("");
		this.constraint = constraint;
	}

	
	@Override
	public void validate(Component comp, Object value)
			throws WrongValueException {
		
		if (this.constraint == MAYOR_A) {
			if (value == null || ((Integer)value).intValue() < this.value)
	            throw new WrongValueException(comp, "El valor debe ser mayor a "+ this.value);
		}
		
		if (this.constraint == MENOR_A) {
			if (value == null || ((Integer)value).intValue() > this.value)
	            throw new WrongValueException(comp, "El valor debe ser menor a "+ this.value);
		}
		
		if (this.constraint == RUC) {
			Ruc ruc = new Ruc();
			if (ruc.validarRuc((String) value) == false) {
				throw new WrongValueException(comp, "Debe Ingresar un Ruc valido");
			}
		}
		
		if (this.constraint == this.NO_EMPTY) {
			boolean correcto = true;
			if (value instanceof String && ((String) value).length() == 0) {
				correcto = false;
			}
			if (value instanceof Number && value == null) {
				correcto = false;
			}
			if (!correcto) {
				throw new WrongValueException(comp, "Este campo no debe quedar vacio");
			}
		}		
	}
	
	public void showCustomError(Component comp, WrongValueException ex) {
		Clients.showNotification(ex.getMessage(), "error", comp, "end_center", 3000, true);	
	}
}
