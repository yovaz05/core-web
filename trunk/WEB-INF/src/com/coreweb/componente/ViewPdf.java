package com.coreweb.componente;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zhtml.Iframe;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.coreweb.Archivo;
import com.coreweb.Config;
import com.coreweb.extras.reporte.DatosReporte;
import com.coreweb.util.Misc;

public class ViewPdf {

	private Misc m = new Misc();
	
	private String anchoWindows = "800px";
	private String altoWindows = "600px";
	
	private Window w;
	private DatosReporte reporte;
	
	public void showReporte(DatosReporte rep){
		
		/*
		String contexPath = Executions.getCurrent().getContextPath();
		String getCurrentDirectory = Executions.getCurrent().getDesktop().getCurrentDirectory();
		String locale = Executions.getCurrent().locate("./");
		Desktop desktop = Executions.getCurrent().getDesktop();
		String directorio = desktop.getWebApp().getDirectory();
		String realpah = desktop.getWebApp().getRealPath("./");
		String realpah2 = desktop.getWebApp().getRealPath("/");
		String realpah3 = desktop.getWebApp().getRealPath("//");
		
		System.out.println("\n\n\n\n\n\n\n");
		System.out.println("Directorio:" + directorio);
		System.out.println("Contexpath:" + contexPath);
		System.out.println("getCurrentDirectory:" + getCurrentDirectory);
		System.out.println("locale:" + locale);
		System.out.println("realpah:" + realpah);
		System.out.println("realpah2:" + realpah2);
		System.out.println("realpah3:" + realpah3);
		System.out.println("\n\n\n\n\n\n\n");
		*/
		
		// genera el pdf en el directorio de reportes		
		rep.setDirectorioBase(Config.DIRECTORIO_REAL_REPORTES);
		rep.ejecutar(false);
		
		String urlPdf = Config.DIRECTORIO_WEB_REPORTES + "/" + rep.getArchivo();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("reporte", rep);
		map.put("titulo", rep.getTitulo());
		map.put("pdf", urlPdf);	
		map.put("anchoWindows", this.getAnchoWindows());	
		map.put("altoWindows", this.getAltoWindows());	
		

		
		Window window = (Window) Executions.createComponents(
			Archivo.ViewPdf, null, map);
		
		window.setPosition("center");
		window.doModal();		
	}

	public String getAnchoWindows() {
		return anchoWindows;
	}

	public void setAnchoWindows(String anchoWindows) {
		this.anchoWindows = anchoWindows;
	}

	public String getAltoWindows() {
		return altoWindows;
	}

	public void setAltoWindows(String altoWindows) {
		this.altoWindows = altoWindows;
	}

	
	//=========================== Cuando es viewModel ====================
	
	@Init(superclass = true)
	public void initViewPdf(@ExecutionArgParam("reporte") DatosReporte reporte){
		this.reporte = reporte;
	}
	
	
	@Command
	public void cerrarViewPdf(){
		if (this.reporte.isBorrarDespuesDeVer() == true){
			this.m.borrarArchivo(this.reporte.getArchivoPathReal());
		}
		
	}
	
	
	
}
