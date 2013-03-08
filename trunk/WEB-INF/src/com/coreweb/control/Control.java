package com.coreweb.control;


import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Image;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Panel;

import com.coreweb.Archivo;
import com.coreweb.Config;
import com.coreweb.domain.Domain;
import com.coreweb.domain.Register;
import com.coreweb.dto.Assembler;
import com.coreweb.dto.DTO;
import com.coreweb.dto.UtilCoreDTO;
import com.coreweb.login.LoginUsuarioDTO;



//public class Control extends UnicastRemoteObject implements IControl{
public class Control {

	// No debería hacer esto, pero es lo más facil :(
	private static UtilCoreDTO dtoUtil; // = new AssemblerUtil().getDTOUtil(); 
	
	private Component main;
	private Hashtable<String, String> hashFilterValue = new Hashtable<String, String>();
//	private ControlAgendaEvento ctrAgenda = new ControlAgendaEvento();
	private Assembler ass;

	private LoginUsuarioDTO us = new LoginUsuarioDTO();

	private static String aliasFormularioCorrienteTXT = "--AliasFormularioNoDefinido--";
	private String aliasFormularioCorriente = aliasFormularioCorrienteTXT;
	private String textoFormularioCorriente = "Falta setear el textoFormularioCorriente: " + System.currentTimeMillis();

	public Control(Assembler ass) {
		this.setAss(ass);
	}

	
	// seteo inicial
	public void preInit(){
		System.out.println("*******************************************");
		System.out.println("** Falta implementar el preInit: "+ this.getClass().getName());
		System.out.println("*******************************************");
	}
	
	
	@Init
	public void initPrincipal() throws Exception {
		System.out.println("[ToDo] control de session de usuario ==========");

		Session s = Sessions.getCurrent();
		this.us = (LoginUsuarioDTO) s.getAttribute(Config.USUARIO);
		if (this.us == null) {
			// primera vez
			this.us = new LoginUsuarioDTO();
			System.out.println("--- entra al initPrincipal por primera vez al sistema");
			return;
		}
		this.preInit();
		this.poneCarita(this.us.isLogeado());
	}
	
	
	
	@AfterCompose(superclass = true)
	public void afterComposeBody() {
		if (this.us.isLogeado() == true) {
			// si esta logeado retorna, cualquier otro caso exepcion
			System.out.println("usuario logeado: " + this.us.getLogin());
			
			String aliasF = this.getAliasFormularioCorriente();
			if ( this.getUs().formDeshabilitado(aliasF) == true){
				System.out.println("=========== ["+this.us.getLogin()+"] No tiene permisos para acceder a esta pagina: ["+aliasF+"] " + this.getClass().getName());
				this.saltoDePagina(Archivo.errorLogin);
			}
	
			
			return;
		}
		System.out.println("****************** NO Logeado:"+this.us.getLogin());
		this.saltoDePagina(Archivo.errorLogin);
	}
	

	
	



	public UtilCoreDTO getDtoUtil() {
		return dtoUtil;
	}


	public void setDtoUtil(UtilCoreDTO dtoUtil) {
		Control.dtoUtil = dtoUtil;
	}


	// hacer un salto de pagina
	public void saltoDePagina(String url, String param, Object value) {
		Hashtable<String, Object> h = new Hashtable<String, Object>();
		h.put(param, value);
		saltoDePagina(url, h);
	}

	public void salirSistema(String url){
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
			System.out.println("************** error salto de pagina: "+url);
			e.printStackTrace();
			this.noAutorizado();

		}

	}

	public void poneCarita(boolean b) {

		try {
			main = Path.getComponent("/templateInicio");
			System.out.println("========================================================");
			System.out.println(main.getId() + "-"+  main.getClass().getName());
			Collection<Component> c = main.getFellows();
			for (Iterator iterator = c.iterator(); iterator.hasNext();) {
				Component component = (Component) iterator.next();
				System.out.println(component.getId() + " - " + component.getClass().getName());
			}
		
			System.out.println("========================================================");
			Image img = (Image) main.getFellow("carita", true);
			if (b == true) {
				img.setSrc(Archivo.caritaFeliz);
			} else {
				img.setSrc(Archivo.caritaEnojada);
			}
		} catch (Exception e) {
			System.out.println("error poniendo carita");
			e.printStackTrace();
			this.noAutorizado();

		}

	}

	public void noAutorizado() {
		System.out.println("==================================== no autorizado ============");
		
		try {
			Session s = Sessions.getCurrent();
			s.setAttribute(Config.LOGEADO, new Boolean(false));
			Executions.sendRedirect(Archivo.noAutorizado);
		} catch (Exception e1) {
			System.out.println("==================================== error no autorizado ============");

			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("==================================== Fin no autorizado ============");
	}

	public Assembler getAss() {
		return ass;
	}

	public void setAss(Assembler ass) {
		this.ass = ass;
	}


	/*************** Usados en el explorer generico que no funciona aun ********/
	private  ListModel<DTO> getAllModelx() {
		System.out.println("** Control.getAllModel:  No implementado en "
				+ this.getClass().getName());
		return null;
	}
	

	private  ListModel<DTO> getAllModelOriginalx() {
		System.out.println("** Control.getAllModel:  No implementado en "
				+ this.getClass().getName());
		return null;
	}
	/***********************************************************************/

	protected DTO saveDTO(DTO dto, boolean refreshDTO) throws Exception {
		Domain don = ass.dtoToDomain(dto);
		Register register = Register.getInstance();
		register.saveObject(don);
		if (refreshDTO == true){
			dto = ass.domainToDto(don);
		}
		return dto;
	}
	
	protected void deleteDTO(DTO dto) throws Exception {
		Domain don = ass.dtoToDomain(dto);
		Register register = Register.getInstance();
		register.deleteObject(don);
	}

	public DTO getDTOById(String entityName, String idObjeto) throws Exception{
		Register register = Register.getInstance();
		Domain dom = register.getObject(entityName, Long.parseLong(idObjeto));
		DTO dto = this.getAss().domainToDto(dom);
		return dto;
	}
	
	
	public List<DTO> getAllDTOs(String entityName) throws Exception{
		return getAllDTOs(entityName,  this.ass);
	}

	
	public List<DTO> getAllDTOs(String entityName, Assembler ass) throws Exception{
		List<DTO> ldto = new ArrayList<DTO>();
		Register register = Register.getInstance();
		List<Domain> ldom = (List<Domain>)register.getObjects(entityName);
		for (int i = 0; i < ldom.size(); i++) {
			Domain dom = ldom.get(i);
			DTO dto =  	ass.domainToDto(dom);
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
			System.out.println("");

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
	public ControlAgendaEvento getCtrAgenda() {
		return ctrAgenda;
	}

	public void setCtrAgenda(ControlAgendaEvento ctrAgenda) {
		this.ctrAgenda = ctrAgenda;
	}
	*/

	public LoginUsuarioDTO getUs() {
		return us;
	}

	public void setUs(LoginUsuarioDTO us) {
		this.us = us;
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
		Label lab = (Label)main.getFellow("nombreFormulario");
		lab.setValue(this.textoFormularioCorriente );
	}


	public synchronized  boolean operacionHabilitada(String aliasOperacion) throws Exception {
		String form = this.getAliasFormularioCorriente();
		if (form.compareTo(Control.aliasFormularioCorrienteTXT) == 0) {
			Exception ex = new Exception(
					"Nombre de formulario NO definido para la operacion: '"
							+ aliasOperacion + "' en la clase "
							+ this.getClass().getName());
			throw ex;
		}
		return this.getUs().opeHabilitada(form, aliasOperacion);

	}
	
	

	public boolean mensajeEliminar(String texto) {

		org.zkoss.zul.Messagebox.Button b = Messagebox.show(texto, "Eliminar",
				new Messagebox.Button[] { Messagebox.Button.YES,
						Messagebox.Button.NO }, Messagebox.QUESTION, null);
		if ((b != null) && (b.compareTo(Messagebox.Button.YES)) == 0) {
			return true;
		}
		return false;
	}

	public boolean mensajeAgregar(String texto) {
		org.zkoss.zul.Messagebox.Button b = Messagebox.show(texto, "Agregar",
				new Messagebox.Button[] { Messagebox.Button.YES,
						Messagebox.Button.NO }, Messagebox.QUESTION, null);
		if ((b != null) && (b.compareTo(Messagebox.Button.YES)) == 0) {
			return true;
		}
		return false;

	}

	public void mensajeInfo(String texto) {
		org.zkoss.zul.Messagebox.Button b = Messagebox.show(texto,
				"Informacion",
				new Messagebox.Button[] { Messagebox.Button.OK },
				Messagebox.INFORMATION, null);
	}

	public void mensajeError(String texto) {
		org.zkoss.zul.Messagebox.Button b = Messagebox.show(texto, "Error",
				new Messagebox.Button[] { Messagebox.Button.OK },
				Messagebox.ERROR, null);
	}

	public boolean mensajeSiNo(String texto) {

		org.zkoss.zul.Messagebox.Button b = Messagebox.show(texto, "Confirmar",
				new Messagebox.Button[] { Messagebox.Button.YES,
						Messagebox.Button.NO }, Messagebox.QUESTION, null);

		if ((b != null) && (b.compareTo(Messagebox.Button.YES)) == 0) {
			return true;
		}
		return false;
	}
	
	
	
	public boolean esGrupo(String grupo){
		boolean out = false;
		out = this.getUs().esGrupo(grupo);
		return out;
	}

	public static void main(String[] args) {
		String a = "";
		String b = "hola";
		String c = "xx";
		System.out.println("" + b + ":" + b.indexOf(a) + "   -   "
				+ b.indexOf(c));
	}

}
