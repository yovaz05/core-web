package com.coreweb.extras.alerta;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
			List<Alerta> alertasDom = rr.getAllAlertas(desde, cantidad,
					this.getLoginNombre());
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

	
	private void crearAlerta(String creador, MyPair nivel, MyPair tipo,
			String descripcion, String destino) throws Exception {
		// esto falta desglosar para las distintas combinaciones de alertas
		Register rr = Register.getInstance();
		AssemblerAlerta as = new AssemblerAlerta(); 
		AlertaDTO alerta = new AlertaDTO();
		alerta.setCreador(creador);
		alerta.setDestino(destino);
		alerta.setDescripcion(descripcion);
		alerta.setNivel(nivel);
		alerta.setTipo(tipo);
		alerta.setCancelada(false);
		// se pasa de dto a domain y se tiene que guardar
		// aca tambien se deberia llamar al metodo que notifique a los destinos
		rr.saveObject(as.dtoToDomain(alerta),this.getLoginNombre());
		
	}

	@Command
	@NotifyChange("*")
	public void cancelarAlerta() {
		if (!this.selectedAlerta.isCancelada()) {
			String obsv = this.getMotivoAnulacion();
			if (obsv.length() != 0) {
				this.selectedAlerta.setCancelada(true);
				this.selectedAlerta.setObservacion(obsv);
			}
		}

	}
	
	public int getCantidadAlertasNoCanceladas(){
		int cant = 0;
		Register rr = Register.getInstance();
		try {
			cant = rr.getCantidadAlertasNoCanceladas(this.getLoginNombre());
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println("cantidad de alertas: "+cant);
		return cant;
	}

}
