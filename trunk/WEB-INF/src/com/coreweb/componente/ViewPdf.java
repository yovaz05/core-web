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
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.coreweb.Archivo;
import com.coreweb.Config;
import com.coreweb.control.GenericViewModel;
import com.coreweb.extras.reporte.DatosReporte;
import com.coreweb.util.Misc;



public class ViewPdf {

	private Misc m = new Misc();

	private String anchoWindows = "800px";
	private String altoWindows = "600px";
	private String altoReporte = "500px";
	private boolean botonImprimir = true;
	private boolean botonCancelar = true;
	
	private boolean clickImprimir = false;
	private boolean clickCancelar = false;

	private Window w;
	private DatosReporte reporte;

	public void showReporte(DatosReporte rep, GenericViewModel vm) {

		// genera el pdf en el directorio de reportes
		rep.setDirectorioBase(Config.DIRECTORIO_REAL_REPORTES);
		rep.setUsuario(vm.getUs().getNombre());
		rep.setEmpresa(vm.getEmpresa());
		rep.ejecutar(false);

		// String urlPdf = Config.DIRECTORIO_WEB_REPORTES + "/" +
		// rep.getArchivoSalida();
		String urlPdf = rep.getUrlReporte();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("reporte", rep);
		map.put("titulo", rep.getTitulo());
		map.put("pdf", urlPdf);
		map.put("anchoWindows", this.getAnchoWindows());
		map.put("altoWindows", this.getAltoWindows());
		map.put("altoReporte", this.getAltoReporte());
		map.put("botonImprimir", this.isBotonImprimir());
		map.put("botonCancelar", this.isBotonCancelar());
		map.put("control", this);

		Window window = (Window) Executions.createComponents(Archivo.ViewPdf,
				null, map);

		this.w = window;
		this.w.setPosition("center");
		this.w.doModal();
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

	public boolean isBotonCancelar() {
		return botonCancelar;
	}

	public void setBotonCancelar(boolean botonCancelar) {
		this.botonCancelar = botonCancelar;
	}
	
	
	public boolean isBotonImprimir() {
		return botonImprimir;
	}

	public void setBotonImprimir(boolean botonImprimir) {
		this.botonImprimir = botonImprimir;
	}


	public boolean isClickImprimir() {
		return clickImprimir;
	}

	private void setClickImprimir(boolean clickImprimir) {
		this.clickImprimir = clickImprimir;
	}

	public boolean isClickCancelar() {
		return clickCancelar;
	}

	private void setClickCancelar(boolean clickCancelar) {
		this.clickCancelar = clickCancelar;
	}

	// =========================== Cuando es viewModel ====================

	@Wire
	private Iframe printIFrame;
	
	ViewPdf control = null;

	public String getAltoReporte() {
		return altoReporte;
	}

	public void setAltoReporte(String altoReporte) {
		this.altoReporte = altoReporte;
	}

	@Init(superclass = true)
	public void initViewPdf(@ExecutionArgParam("reporte") DatosReporte reporte,
			@ExecutionArgParam("control") ViewPdf control) {
		this.reporte = reporte;
		this.control = control;
	}

	@Command
	public void cerrarViewPdf() {
		// es para cuando se cierra desde la X de arriba
		if (this.reporte.isBorrarDespuesDeVer() == true) {
			this.m.borrarArchivo(this.reporte.getArchivoPathReal());
		}
	}

	@Command
	public void cancelar() {
		// hacer lo que corresponda para cuando hay que cerrar
		this.cerrarViewPdf();

		this.m.mensajePopupTemporal("[ToDo] cancelar");
		this.control.setClickCancelar(true);
		this.control.w.detach();
	}

	@Command
	public void imprimir() {
		this.m.mensajePopupTemporal("[ToDo] imprimir");
				
		this.control.setClickImprimir(true);
		this.control.w.detach();
	}

}
