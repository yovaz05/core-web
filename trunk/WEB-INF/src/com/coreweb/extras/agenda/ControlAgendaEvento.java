package com.coreweb.extras.agenda;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.ExecutionParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.coreweb.Config;
import com.coreweb.componente.BodyPopupAceptarCancelar;
import com.coreweb.control.GenericViewModel;
import com.coreweb.domain.AgendaEvento;
import com.coreweb.domain.Register;
import com.coreweb.dto.UtilCoreDTO;
import com.coreweb.util.Misc;


public class ControlAgendaEvento extends GenericViewModel {

	/*
	 * Esta clase es control y viewModel a la vez. Es para que sea más simple
	 * nomas.
	 */

	public static int COTIZACION = 1;
	public static int NORMAL = 1;
	public static int LINK = 2;
	
	
	AssemblerAgenda assAge = new AssemblerAgenda();
	
	public ControlAgendaEvento(){
		super();
		this.setAss(assAge);
	}
	
	


	@Override
	public String getAliasFormularioCorriente() {
		String out = "AgendaEvento";
		return out;
	}

	// ================= control ============================
	private AgendaEvento crearAgenda(int tipo, String clave) throws Exception {
		Register rr = Register.getInstance();
		AgendaEvento a = new AgendaEvento();
		a.setFecha(new Date());
		a.setTipo(tipo);
		a.setKey(clave);
		rr.saveObject(a);
		return a;
	}

	private AgendaEventoDTO getAgenda(int tipoAgenda, String claveAgenda)
			throws Exception {

		Register rr = Register.getInstance();
		AgendaEvento a = null;
		try {
			a = rr.getAgenda(tipoAgenda, claveAgenda);
		} catch (Exception ex) {
			a = crearAgenda(tipoAgenda, claveAgenda);
		}

		return (AgendaEventoDTO) assAge.domainToDto(a);
	}

	public void mostrarAgenda(int tipoAgenda, String claveAgenda, String titulo)
			throws Exception {

		AgendaEventoDTO aDto = this.getAgenda(tipoAgenda, claveAgenda);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("titulo", titulo);
		map.put("agendaEventoDto", aDto);
		Window window = (Window) Executions.createComponents(
				"/core/misc/agenda.zul", null, map);
		// window.doOverlapped();
		window.doModal();

	}
	
	
	public void addDetalle(int tipoAgenda, String claveAgenda, int tipoDetalle,
			String texto, String link) throws Exception {
		AgendaEventoDTO ageDto = this.getAgenda(tipoAgenda, claveAgenda);
		
		AgendaEventoDetalleDTO detaDto = new AgendaEventoDetalleDTO();
		detaDto.setFecha(new Date());
		detaDto.setUsuario(getLogin()); // login del control
		detaDto.setTexto(texto);
		detaDto.setLink(link);
		
		ageDto.getAgendaEventoDetalles().add(detaDto);
		this.saveDTO(ageDto, false);
		
		
	}

	

	// ================= view model ============================
	
	String titulo = "titulo windows";
	AgendaEventoDTO dto = new AgendaEventoDTO();


	@Init(superclass = true)
	public void init(
			@ExecutionArgParam("titulo") String titulo,
			@ExecutionArgParam("agendaEventoDto") AgendaEventoDTO agendaEventoDto) {
		this.titulo = titulo;
		this.dto = agendaEventoDto;
		this.setAss(assAge);
	}

	@Command
	@NotifyChange("*")
	public void agregarEvento() throws Exception {
		
		Textbox texto = new Textbox();
		texto.setWidth("300px");
		texto.setRows(3);
		
		BodyPopupAceptarCancelar bAC = new BodyPopupAceptarCancelar();
		//bAC.setWidthWindows("200px");
		bAC.addComponente("Texto:", texto);
		bAC.setHighWindows("200px");
		bAC.setWidthWindows("430px");
		

		bAC.showPopup("Agregar un item a la Agenda");

		if (bAC.isClickAceptar() == true) {
			
			AgendaEventoDetalleDTO aDto = new AgendaEventoDetalleDTO();
			aDto.setFecha(new Date());
			aDto.setUsuario(getLogin());
			aDto.setTexto(texto.getValue());
			
			this.dto.getAgendaEventoDetalles().add(aDto);
			this.saveDTO(this.dto, true);
		}
		
	}

	@Command
	public void cerrarAgenda() {
		this.mainComponent.detach();
	}

	public String getTitulo() {
		String fe = " (creado: "+ m.dateToString(this.getDto().getFecha(),Misc.YYYY_MM_DD_HORA_MIN_SEG)+")";
		return titulo + fe;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public AgendaEventoDTO getDto() {
		return dto;
	}

	public void setDto(AgendaEventoDTO dto) {
		this.dto = dto;
	}


	@Override
	public boolean getCondicionComponenteSiempreHabilitado() {
		// TODO Auto-generated method stub
		
		return this.getDto() != null;
	}


	private String getLogin(){
		String out = "Juan - PoPu";
		try {
			Session s = Sessions.getCurrent();
			out = (String) s.getAttribute(Config.LOGIN);
			if (out.trim().length()==0){
				out = "error "+" ("+ m.dateToString(new Date(),Misc.YYYY_MM_DD_HORA_MIN_SEG)+")";
			}
		} catch (Exception e) {
		}
		return out;
	}
	
}
