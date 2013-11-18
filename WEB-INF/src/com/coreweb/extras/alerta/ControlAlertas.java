package com.coreweb.extras.alerta;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

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

public class ControlAlertas extends SoloViewModel {

	private WindowPopup w;

	private List<AlertaDTO> alertas;

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
		this.alertas = this.cargarAlertas();
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
			w.setWidth("710px");
			w.setHigth("500px");
			w.show("/core/misc/alertas.zul");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public int desde;
	public int hasta;

	public List<AlertaDTO> cargarAlertas() {
		try {
			Register rr = Register.getInstance();
			AssemblerAlerta as = new AssemblerAlerta();
			this.desde = 0;
			this.hasta = 50;
			List<Alerta> alertasDom = rr.getAllAlertas(desde, hasta, this.getLoginNombre());
			List<AlertaDTO> alertas = new ArrayList<AlertaDTO>();
			//System.out.println("============ entro a cargar alertas ==============");
			for (Alerta alerta : alertasDom) {
				AlertaDTO alertaDto = new AlertaDTO();
				alertaDto = as.domainToDto(alerta);
				alertas.add(alertaDto);
				
				//System.out.println("alerta: "+ alerta.getDescripcion());
				//System.out.println("alertaDto "+ alertaDto.getDescripcion());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return alertas;
	}
}
