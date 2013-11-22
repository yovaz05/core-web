package com.coreweb.extras.alerta;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.coreweb.Config;
import com.coreweb.IDCore;
import com.coreweb.componente.BodyPopupAceptarCancelar;
import com.coreweb.componente.WindowPopup;
import com.coreweb.control.GenericViewModel;
import com.coreweb.control.SoloViewModel;
import com.coreweb.domain.AgendaEvento;
import com.coreweb.domain.Alerta;
import com.coreweb.domain.Register;
import com.coreweb.extras.agenda.AgendaEventoDTO;
import com.coreweb.extras.agenda.AgendaEventoDetalleDTO;
import com.coreweb.extras.agenda.AssemblerAgenda;
import com.coreweb.login.ControlInicio;
import com.coreweb.util.Misc;
import com.coreweb.util.MyPair;
import com.jgoodies.binding.BindingUtils;

public class ControlAlertas extends SoloViewModel {

	private WindowPopup w;

	private List<AlertaDTO> alertas = new ArrayList<AlertaDTO>();

	private String mensaje = "";

	private AlertaDTO selectedAlerta;

	public List<AlertaDTO> getAlertas() {
		return alertas;
	}

	public void setAlertas(List<AlertaDTO> alertas) {
		this.alertas = alertas;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public AlertaDTO getSelectedAlerta() {
		return selectedAlerta;
	}

	public void setSelectedAlerta(AlertaDTO selectedAlerta) {
		this.selectedAlerta = selectedAlerta;
	}

	@Init(superclass = true)
	public void initControlAlertas() {
		// this.desde = 0;
		// this.cantidad = 20;
		this.alertas = this.cargarAlertas();
		// Clients.showNotification(this.alertas.size()+" - "+this.cargarAlertas().size());
		// BindUtils.postNotifyChange(null, null, this, "alertas");
	}

	@AfterCompose(superclass = true)
	public void afterComposeControlAlertas() {

	}

	@Override
	public String getAliasFormularioCorriente() {
		return IDCore.F_MIS_ALERTAS;
	}

	public void mostrarAlertas() {
		try {
			w = new WindowPopup();
			w.setModo(WindowPopup.NUEVO);
			w.setTitulo("Mis Alertas");
			w.setWidth("920px");
			w.setHigth("520px");
			w.show(Config.ALERTAS_ZUL);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int desde = 0;
	public int cantidad = 20;

	public List<AlertaDTO> cargarAlertas() {
		List<AlertaDTO> alertas = new ArrayList<AlertaDTO>();
		try {
			Register rr = Register.getInstance();
			AssemblerAlerta as = new AssemblerAlerta();
			// this.desde = 0;
			// this.hasta = 50;
			// System.out.println("entro a cargar: "+this.desde+" - "+this.hasta);
			List<Alerta> alertasDom = rr.getAllAlertas(desde, cantidad,this.getLoginNombre());
			for (Alerta alerta : alertasDom) {
				AlertaDTO alertaDto = new AlertaDTO();
				alertaDto = as.domainToDto(alerta);
				alertas.add(alertaDto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return alertas;
	}

	@Command
	@NotifyChange("alertas")
	public void next() {
		this.desde += 20;
		this.alertas = this.cargarAlertas();

	}

	@Command
	@NotifyChange("alertas")
	public void prev() {
		if (this.desde != 0) {
			this.desde -= 20;
			this.alertas = this.cargarAlertas();
		}
	}

	private String getLoginSession(){
		String out = "";
		return out;
	}
	
	
	private void crearAlerta(String creador, MyPair nivel, MyPair tipo,
			String descripcion, String destino) throws Exception {
		// esto falta desglosar para las distintas combinaciones de alertas
		

		AlertaDTO alerta = new AlertaDTO();
		alerta.setCreador(creador);
		alerta.setDestino(destino);
		alerta.setDescripcion(descripcion);
		alerta.setNivel(nivel);
		alerta.setTipo(tipo);
		alerta.setCancelada(false);
		
		this.grabarAlerta(alerta);
		
		// ojo, si destino es una lista, hay que desglosarlo
		this.refrescarAlertas(destino);
	}

	private void grabarAlerta(AlertaDTO alerta) throws Exception{
		String login = this.getLoginNombre();
		Register rr = Register.getInstance();
		AssemblerAlerta as = new AssemblerAlerta(); 
		rr.saveObject(as.dtoToDomain(alerta),login);		
	}
	
	
	
	@Command
	@NotifyChange("*")
	public void cancelarAlerta() throws Exception {
		// NOTA: no hay que grabar la alerta cancelada??
		if (!this.selectedAlerta.isCancelada()) {
			String obsv = this.getMotivoAnulacion();
			if (obsv.length() != 0) {
				this.selectedAlerta.setCancelada(true);
				this.selectedAlerta.setObservacion(obsv);
				this.grabarAlerta(this.selectedAlerta);
				String destino = this.selectedAlerta.getDestino();
				this.refrescarAlertas(destino);
				
			}
		}

	}
	
	
	
	private void refrescarAlertas(String destino){
		Hashtable<String, ControlInicio> hci = (Hashtable<String, ControlInicio>) this
				.getAtributoContext(Config.ALERTAS);
		
		String[] ls = destino.split(",");
		for (int i = 0; i < ls.length; i++) {
			String login = ls[i];
			System.out.println("============================ buscando alerta para:"+login);
			ControlInicio ci = hci.get(login);
			if (ci != null){
				System.out.println("============================ encontro ["+ci+"]" );
				ci.refreshAlertas();
			}

		}
		
		
	}
	
	

}
