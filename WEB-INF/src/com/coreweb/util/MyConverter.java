package com.coreweb.util;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.util.Clients;



public class MyConverter implements Converter {
	
	// los tipos de formato definido
	private static int FORMAT_FACTURA_PY = 1;
	private static int FORMAT_CREDIT_CARD  = 2;
	
	// tipo del converter corriente
	private int tipoFormato = 0;
	
	private Misc misc = new Misc();

	// en el constructor definimos el tipo
	public MyConverter(int tipoFormato){
		this.tipoFormato = tipoFormato;
	}
	
	public MyConverter(){
		this.tipoFormato = 0;
	}
	
	
	// para llamarlo desde el ZUL
	
	public MyConverter getFacturaPy(){
		return new MyConverter(FORMAT_FACTURA_PY);
	}
	
	public MyConverter getCreditCard(){
		return new MyConverter(FORMAT_CREDIT_CARD);
	}
	
	
	@Override
	public Object coerceToBean(Object arg0, Component arg1, BindContext arg2) {
		// TODO Auto-generated method stub
		return coerceTo(arg0, arg1);
	}

	@Override
	public Object coerceToUi(Object arg0, Component arg1, BindContext arg2) {
		// TODO Auto-generated method stub
		//return coerceTo(arg0, arg1);
		return arg0;
	}


	public Object coerceTo(Object val, Component comp){
		
		Object out = null;
		
		if (this.tipoFormato == FORMAT_FACTURA_PY) {
			out = this.formatFactura((String) val, comp);
			
		} else if (this.tipoFormato == FORMAT_CREDIT_CARD) {
			out = this.formatCreditCard((String) val, comp);		
		}		
			
		return val == null ? null : out;
	}
	
	

	/************************* FORMATOS ****************************/
	
	//Formato Factura
	public String formatFactura(String val, Component comp){
		if (((val.split("-").length != 3) || !misc.containsOnlyNumbers((String) val))) {
			Clients.showNotification("Mal formato de la factura", "error", comp, "end_center", 3000, true);
			return val;
		}
		String[] s = ((String) val).split("-");
		String out = misc.ceros(s[0], 3)+"-"+misc.ceros(s[1], 3)+"-"+misc.ceros(s[2], 7);
		return out;
	}
	
	//Formato Tarjeta de Credito
	public String formatCreditCard(String val, Component comp){
		if (((val.split("-").length != 4) || !misc.containsOnlyNumbers((String) val))) {
			Clients.showNotification("No es el formato", "error", comp, "end_center", 3000, true);		
			return val;
		}
		String[] s = ((String) val).split("-");
		String out = misc.ceros(s[0], 4)+"-"+misc.ceros(s[1], 4)+"-"+misc.ceros(s[2], 4)+"-"+misc.ceros(s[3], 4);
		return out;
	}	
}
