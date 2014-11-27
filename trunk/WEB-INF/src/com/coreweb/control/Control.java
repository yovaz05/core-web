package com.coreweb.control;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletContext;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Menuitem;

import com.coreweb.Archivo;
import com.coreweb.Config;
import com.coreweb.domain.Domain;
import com.coreweb.domain.Register;
import com.coreweb.dto.Assembler;
import com.coreweb.dto.DTO;
import com.coreweb.dto.UtilCoreDTO;
import com.coreweb.login.LoginUsuarioDTO;
import com.coreweb.util.Misc;

//public class Control extends UnicastRemoteObject implements IControl{
public class Control {

	public Misc m = new Misc();

	private static UtilCoreDTO dtoUtil = null; // = new
												// AssemblerUtil().getDTOUtil();

	private Component main;
	private Hashtable<String, String> hashFilterValue = new Hashtable<String, String>();
	// private ControlAgendaEvento ctrAgenda = new ControlAgendaEvento();
	private Assembler ass;

	private static String empresa = "Definir empresa";

	private static String aliasFormularioCorrienteTXT = "--AliasFormularioNoDefinido--";
	private String aliasFormularioCorriente = aliasFormularioCorrienteTXT;
	private String textoFormularioCorriente = "Falta setear el textoFormularioCorriente: "
			+ System.currentTimeMillis();

	public Control(Assembler ass) {
		this.setAss(ass);
	}

	// seteo inicial
	public void preInit() {
		/*
		 * System.out.println("*******************************************");
		 * System.out.println("** Falta implementar el preInit: " +
		 * this.getClass().getName());
		 * System.out.println("*******************************************");
		 */
	}

	@Init(superclass = true)
	public void initControl() throws Exception {

		Session s = this.getSessionZK();
		LoginUsuarioDTO us = this.getUs();
		if (us == null) {
			// primera vez

			String prefix = Executions.getCurrent().getParameter(Config.PREFIX);
			s.setAttribute(Config.PREFIX, prefix);

			this.inicializarDtoUtil(prefix);

			us = new LoginUsuarioDTO();
			this.setUs(us);

			// dr aca poner la invocacion afterLogin

			// System.out.println("--- entra al initPrincipal por primera vez al sistema");
			return;
		}
		s.setAttribute(Config.LOGIN, us.getLogin());

		this.preInit();
		this.poneCarita(us.isLogeado());
		Clients.confirmClose("Esta acción cerrará la Aplicación.");
	}

	@AfterCompose(superclass = true)
	public void afterComposeControl(
			@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		Selectors.wireEventListeners(view, this);

		if (this.getUs().isLogeado() == true) {
			// si esta logeado retorna, cualquier otro caso exepcion
			// System.out.println("usuario logeado: " + this.us.getLogin());

			String aliasF = this.getAliasFormularioCorriente();
			if (this.getUs().formDeshabilitado(aliasF) == true) {
				System.out.println("=========== [" + this.getUs().getLogin()
						+ "] No tiene permisos para acceder a esta pagina: ["
						+ aliasF + "] " + this.getClass().getName() + ":"
						+ this);
				this.saltoDePagina(Archivo.errorLogin);
			}

			return;
		}
		this.saltoDePagina(Archivo.errorLogin);
	}

	public UtilCoreDTO getDtoUtil() {
		return dtoUtil;
	}

	public void setDtoUtil(UtilCoreDTO dtoUtil) {
		Control.dtoUtil = dtoUtil;
	}

	// ================================================
	// necesario para hacer el Init Inicial
	public static boolean existDtoUtil() {
		return (Control.dtoUtil != null);
	}

	public static void setInicialDtoUtil(UtilCoreDTO dtoUtil) {
		Control.dtoUtil = dtoUtil;
	}

	// ================================================
	// Alertas
	// por ahora no, porque ControlAlerta hereda de Control, entonces tenemos un
	// ciclo.
	/*
	private ControlAlertas ctrAlertas = new ControlAlertas();

	public ControlAlertas getCtrAlertas() {
		return ctrAlertas;
	}
	*/
	// ================================================

	// Login del usuario
	public String getLoginNombre() {
		LoginUsuarioDTO us = this.getUs();
		if (us != null) {
			return us.getLogin();
		}
		return "NS";

	}

	// hacer un salto de pagina
	public void saltoDePagina(String url, String param, Object value) {
		Hashtable<String, Object> h = new Hashtable<String, Object>();
		h.put(param, value);
		saltoDePagina(url, h);
	}

	public void salirSistema(String url) {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// hacer un salto de pagina
	public void saltoDePagina(String url) {
		saltoDePagina(url, new Hashtable<String, Object>());
	}

	// hacer un salto de pagina
	public void saltoDePagina(String url, Hashtable<String, Object> params) {
		try {
			main = Path.getComponent("/templateInicio");
			Include inc = (Include) main.getFellow("principalBody", true);

			Enumeration<String> enu = params.keys();
			while (enu.hasMoreElements()) {
				String key = enu.nextElement();
				Object value = params.get(key);
				inc.setDynamicProperty(key, value);
			}

			inc.setSrc(url);

		} catch (Exception e) {
			System.out.println("************** error salto de pagina: " + url);
			e.printStackTrace();
			this.noAutorizado();

		}

	}

	public void poneCarita(boolean b) {

		try {
			main = Path.getComponent("/templateInicio");
			Menuitem item = (Menuitem) main.getFellow("carita", true);
			if (b == true) {
				item.setImage(Archivo.caritaFeliz);

			} else {
				item.setImage(Archivo.caritaEnojada);
			}

		} catch (Exception e) {
			System.out
					.println("error poniendo carita (" + e.getMessage() + ")");
			// this.noAutorizado();

		}

	}

	public void noAutorizado() {
		System.out
				.println("==================================== no autorizado ============");

		try {
			Session s = this.getSessionZK();
			s.setAttribute(Config.LOGEADO, new Boolean(false));
			Executions.sendRedirect(Archivo.noAutorizado);
		} catch (Exception e1) {
			System.out
					.println("==================================== error no autorizado ============");

			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// System.out.println("==================================== Fin no autorizado ============");
	}

	public Assembler getAss() {
		return ass;
	}

	public void setAss(Assembler ass) {
		this.ass = ass;
	}

	/*************** Usados en el explorer generico que no funciona aun ********/
	private ListModel<DTO> getAllModelx() {
		System.out.println("** Control.getAllModel:  No implementado en "
				+ this.getClass().getName());
		return null;
	}

	private ListModel<DTO> getAllModelOriginalx() {
		System.out.println("** Control.getAllModel:  No implementado en "
				+ this.getClass().getName());
		return null;
	}

	/***********************************************************************/

	protected DTO saveDTO(DTO dto) throws Exception {
		return saveDTO(dto, this.ass);
	}

	protected DTO saveDTO(DTO dto, Assembler assembler) throws Exception {
		String login = getLoginNombre();
		Domain don = assembler.dtoToDomain(dto);
		Register register = Register.getInstance();
		register.saveObject(don, login);
		dto = assembler.domainToDto(don);
		return dto;
	}

	protected void deleteDTO(DTO dto) throws Exception {
		/*
		 * Domain don = ass.dtoToDomain(dto); Register register =
		 * Register.getInstance(); register.deleteObject(don);
		 */
		if (1 == 1) {
			throw new Exception("Control.deleteDTO: "
					+ this.getClass().getName());
		}
		dto.setDeleted();
		this.saveDTO(dto);

	}

	public DTO getDTOById(String entityName, String idObjeto) throws Exception {
		return getDTOById(entityName, Long.parseLong(idObjeto), this.getAss());
	}

	public DTO getDTOById(String entityName, Long idObjeto) throws Exception {
		return getDTOById(entityName, idObjeto, this.getAss());
	}

	public DTO getDTOById(String entityName, Long idObjeto, Assembler ass)
			throws Exception {

		return ass.getDTObyId(entityName, idObjeto);
		/*
		 * Register register = Register.getInstance(); Domain dom =
		 * register.getObject(entityName, idObjeto); DTO dto =
		 * ass.domainToDto(dom); return dto;
		 */
	}

	public List<DTO> getAllDTOs(String entityName) throws Exception {
		return getAllDTOs(entityName, this.ass);
	}

	public List<DTO> getAllDTOs(String entityName, Assembler ass)
			throws Exception {
		List<DTO> ldto = new ArrayList<DTO>();
		Register register = Register.getInstance();
		List<Domain> ldom = (List<Domain>) register.getObjects(entityName);
		for (int i = 0; i < ldom.size(); i++) {
			Domain dom = ldom.get(i);
			DTO dto = ass.domainToDto(dom);
			ldto.add(dto);
		}
		return ldto;
	}

	public List<String> getColumnNames() {
		System.out.println("** Control.getColumnNames:  No implementado en "
				+ this.getClass().getName());
		return null;
	}

	public List<String> getFieldNames() {
		System.out.println("** Control.getFieldNames:  No implementado en "
				+ this.getClass().getName());
		return null;
	}

	public DTO getFilterDTO() {
		System.out.println("** Control.getFilterDTO:  No implementado en "
				+ this.getClass().getName());
		return null;
	}

	public void setFilterDTO(DTO filterDTO) {
		System.out.println("** Control.setFilterDTO:  No implementado en "
				+ this.getClass().getName());
	}

	public void changeFilter(String fieldName) {
		System.out.println("** Control.changeFilter:  No implementado en "
				+ this.getClass().getName());
	}

	public List<DTO> getFilterModel(DTO filter, String fieldName) {
		List<DTO> listFilter = new ArrayList<DTO>();

		try {
			String fv = this.getFilterDTO().getFieldValue(fieldName).toString();
			this.hashFilterValue.put(fieldName, fv);

			List<String> fs = this.getFieldNames();
			// ojo, siempre se usa el original
			ListModel<DTO> listModel = this.getAllModelOriginalx();
			int size = listModel.getSize();
			// System.out.println("");

			for (int i = 0; i < size; i++) {
				DTO dto = listModel.getElementAt(i);
				boolean isAdded = true;
				for (int j = 0; j < fs.size(); j++) {
					String field = fs.get(j);

					String valueFilter = this.hashFilterValue.get(field);
					if ((valueFilter == null)
							|| ((valueFilter.compareTo("0") == 0) && (field
									.compareTo(fieldName) != 0))) {
						valueFilter = "";
					}

					String valueModel = dto.getFieldValue(field).toString();

					// siempre comparamos con minusculas
					valueFilter = valueFilter.toLowerCase();
					valueModel = valueModel.toLowerCase();
					System.out.print("vm:" + valueModel + " vf:" + valueFilter);

					isAdded = isAdded && (valueModel.indexOf(valueFilter) >= 0);
				}
				System.out.println("");
				// System.out.println(" -");
				if (isAdded == true) {
					// System.out.println(" Agregado !!");
					listFilter.add(dto);
				} else {
					// System.out.println(" NO Agregado !!");
				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return listFilter;
	}

	/*
	 * public ControlAgendaEvento getCtrAgenda() { return ctrAgenda; }
	 * 
	 * public void setCtrAgenda(ControlAgendaEvento ctrAgenda) { this.ctrAgenda
	 * = ctrAgenda; }
	 */

	public LoginUsuarioDTO getUs() {
		LoginUsuarioDTO lDto;
		try {
			lDto = (LoginUsuarioDTO) this.getAtributoSession(Config.USUARIO);
		} catch (Exception e) {
			lDto = new LoginUsuarioDTO();
		}
		return lDto;
	}

	public void setUs(LoginUsuarioDTO us) {
		this.setAtributoSession(Config.USUARIO, us);
	}

	public String getAliasFormularioCorriente() {
		return aliasFormularioCorriente;
	}

	public void setAliasFormularioCorriente(String aliasFormularioCorriente) {
		this.aliasFormularioCorriente = aliasFormularioCorriente;
	}

	public String getTextoFormularioCorriente() {
		return textoFormularioCorriente;
	}

	public void setTextoFormularioCorriente(String textoFormularioCorriente) {
		this.textoFormularioCorriente = textoFormularioCorriente;
		Component main = Path.getComponent("/templateInicio");

		Label lab = (Label) main.getFellow("nombreFormulario");
		lab.setValue(this.textoFormularioCorriente);
	}

	public synchronized boolean operacionHabilitada(String aliasOperacion)
			throws Exception {
		String aliasFormulario = this.getAliasFormularioCorriente();
		return this.operacionHabilitada(aliasOperacion, aliasFormulario);
	}

	public synchronized boolean operacionHabilitada(String aliasOperacion,
			String aliasFormulario) throws Exception {
		if (aliasFormulario.compareTo(Control.aliasFormularioCorrienteTXT) == 0) {
			Exception ex = new Exception(
					"Nombre de formulario NO definido para la operacion: '"
							+ aliasOperacion + "' en la clase "
							+ this.getClass().getName());
			throw ex;
		}
		return this.getUs().opeHabilitada(aliasFormulario, aliasOperacion);
	}

	public boolean mensajeEliminar(String texto) {
		return this.m.mensajeEliminar(texto);
	}

	public boolean mensajeAgregar(String texto) {
		return this.m.mensajeAgregar(texto);
	}

	public void mensajeInfo(String texto) {
		this.m.mensajeInfo(texto);
	}

	public void mensajeError(String texto) {
		this.m.mensajeError(texto);
	}

	public boolean mensajeSiNo(String texto) {
		return this.m.mensajeSiNo(texto);
	}

	public int mensajeSiNoCancelar(String texto) {
		return this.m.mensajeSiNoCancelar(texto);
	}

	public int mensajeSiCancelar(String texto) {
		return this.m.mensajeSiCancelar(texto);
	}

	
	public void mensajePopupTemporalWarning(String mensaje) {
		this.m.mensajePopupTemporalWarning(mensaje);
	}

	public void mensajePopupTemporalWarning(String mensaje, int time) {
		this.m.mensajePopupTemporalWarning(mensaje, time);
	}

	public void mensajePopupTemporal(String mensaje) {
		this.m.mensajePopupTemporal(mensaje);
	}

	public void mensajePopupTemporal(String mensaje, int time) {
		this.m.mensajePopupTemporal(mensaje, time);
	}

	public boolean esGrupo(String grupo) {
		boolean out = false;
		out = this.getUs().esGrupo(grupo);
		return out;
	}

	public void inicializarDtoUtil(String prefix) {

		if (this.getDtoUtil() == null) {
			try {

				synchronized (Config.INIT_CLASE) {
					Config.INIT_CLASE = Config.INIT_CLASE.replace("?", prefix);
				}
				this.m.ejecutarMetoto(Config.INIT_CLASE, Config.INIT_METODO);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public static String getEmpresa() {
		return empresa;
	}

	public static void setEmpresa(String empresa) {
		Control.empresa = empresa;
	}

	// ============ session y context =======================

	public Session getSessionZK() {
		return Sessions.getCurrent();
	}

	public Object getAtributoSession(String arg) {
		Session s = this.getSessionZK();
		Object atributo = s.getAttribute(arg);
		return atributo;
	}

	public void setAtributoSession(String key, Object value) {
		Session s = this.getSessionZK();
		s.setAttribute(key, value);
	}

	public Object getAtributoContextx(String arg) {
		ServletContext s = this.getSessionZK().getWebApp().getServletContext();
		Object atributo = s.getAttribute(arg);
		return atributo;
	}

	public void setAtributoContextx(String key, Object value) {
		ServletContext s = this.getSessionZK().getWebApp().getServletContext();
		s.setAttribute(key, value);
	}

	// ======================= USUARIO TEMPORAL ===================

	private CambioUsuarioTemporal cut = null;

	public void cambiarUsuarioTemporal() {
		if (cut == null) {
			cut = new CambioUsuarioTemporal(this);
		}
		cut.cambioUsuarioTemporal();
	}

	public void restaurarUsuarioOriginal() {
		if (cut == null) {
			cut = new CambioUsuarioTemporal(this);
		}
		cut.restaurarUsuarioOriginal();
	}

	public boolean isHayUsuarioTemporal() {
		if (cut == null) {
			cut = new CambioUsuarioTemporal(this);
		}
		return cut.isHayUsuarioTemporal();
	}

	// =======================================================
	
	/**
	 * @return true si el cliente conectado es un dispositivo movil..
	 */
	public boolean isMobile(){
		return Executions.getCurrent().getBrowser("mobile") != null;
	}
	
	/**
	 * @return el IP del cliente conectado..
	 */
	public String getMyIP() {
		return Executions.getCurrent().getRemoteAddr();
	}

}
