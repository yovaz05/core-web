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
		this.desde = 0;
		this.hasta = 19;
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
			w.setWidth("820px");
			w.setHigth("520px");
			w.show(Config.ALERTAS_ZUL);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static int desde;
	public static int hasta;

	public List<AlertaDTO> cargarAlertas() {
		List<AlertaDTO> alertas = new ArrayList<AlertaDTO>();
		try {
			Register rr = Register.getInstance();
			AssemblerAlerta as = new AssemblerAlerta();
			// this.desde = 0;
			// this.hasta = 50;
			//System.out.println("entro a cargar: "+this.desde+" - "+this.hasta);
			List<Alerta> alertasDom = rr.getAllAlertas(desde, hasta,
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
		this.hasta += 20;
		//System.out.println("entro a next: "+this.desde+" - "+this.hasta);
		this.alertas = this.cargarAlertas();

	}

	@Command
	@NotifyChange("alertas")
	public void prev() {
		if(this.desde != 0){
			this.desde -= 20;
			this.hasta -= 20;
			//System.out.println("entro a prev: "+this.desde+" - "+this.hasta);
			this.alertas = this.cargarAlertas();
		}
	}

}
