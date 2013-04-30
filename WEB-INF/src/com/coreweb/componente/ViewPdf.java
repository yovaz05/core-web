package com.coreweb.componente;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zhtml.Iframe;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.coreweb.Archivo;
import com.coreweb.extras.reporte.DatosReporte;

public class ViewPdf {

	private Window w;
	
	public void showReporte(DatosReporte rep){
		
		// genera el pdf
		rep.ejecutar(false);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("titulo", "un reporte");
		map.put("pdf", rep.getArchivo());	

		
		Window window = (Window) Executions.createComponents(
			Archivo.ViewPdf, null, map);
		
		window.setPosition("center");
		window.setWidth(null);
		window.doModal();		

	}
	
}
