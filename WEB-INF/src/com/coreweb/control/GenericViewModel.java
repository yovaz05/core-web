package com.coreweb.control;

import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hamcrest.core.IsInstanceOf;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Default;
import org.zkoss.bind.annotation.ExecutionParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.ext.Disable;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.Template;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Rows;
import org.zkoss.zul.SimpleConstraint;
import org.zkoss.zul.Space;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.ZulEvents;
import org.zkoss.zul.ext.Constrainted;

import com.coreweb.Config;
import com.coreweb.componente.BodyPopupAceptarCancelar;
import com.coreweb.dto.Assembler;
import com.coreweb.login.ControlInicio;
import com.coreweb.util.Check;
import com.coreweb.util.MyConverter;

public abstract class GenericViewModel extends Control {


	private boolean deshabilitado = true;
	private boolean siempreHabilitado = true;
	public Check check = new Check(this);

	private DisableEnableComponent disableEnableComponent = new DisableEnableComponent(this);
	
	
	/*
	 * esto es para darle un tratamiento especial a algunos que se tienen que
	 * deshabilitar/habilitar de forma especial
	 */


	public GenericViewModel(Assembler ass) throws RemoteException {
		super(ass);
		// TODO Auto-generated constructor stub
	}

	public GenericViewModel() {
		super(null);
	}

	public Component mainComponent = null;

	// creo que este método debería estar en control
	// *ver* Para que se usaba?
	// public abstract String getAliasFormularioCorriente();



	@Init(superclass = true)
	public void initGenericViewModel(
			@ContextParam(ContextType.VIEW) Component view) {

		this.mainComponent = view;

	}

	@AfterCompose(superclass = true)
	public void afterComposeGenericViewModel() {
		this.addCamposObligotorios(this.mainComponent);
	}

	public void readonlyAllComponents(AbstractComponent cmp){
		this.disableEnableComponent.disableComponents(cmp);
	}

	public void restoreAllReadonlyComponents(AbstractComponent cmp) {
		this.disableEnableComponent.restoreComponents(cmp);
	}

	
	public void readonlyAllComponents() {
		this.deshabilitado = true;
		this.disableEnableComponent.disableComponents((AbstractComponent) this.mainComponent);
	}

	public void disableAllComponents() {
		this.deshabilitado = true;
		this.disableEnableComponent.disableComponents((AbstractComponent) this.mainComponent);
	}


	public void restoreAllReadonlyComponents() {
		this.deshabilitado = false;
		this.disableEnableComponent.restoreComponents((AbstractComponent) this.mainComponent);
	}

	public void restoreAllDisabledComponents() {
		this.deshabilitado = false;
		this.disableEnableComponent.restoreComponents((AbstractComponent) this.mainComponent);
	}

	public void setDeshabilitado(boolean deshabilitado){
		this.deshabilitado = deshabilitado;
	}
	
	public boolean isDeshabilitado() {
		return deshabilitado;
	}

	@GlobalCommand
	@NotifyChange("*")
	public void refreshComponentes() {
	}

	// Pone el (*) en los campos con constraint, si tiene constraint es
	// obligatorio
	public void addCamposObligotorios(Component ac) {
		this.addCamposObligotorios((AbstractComponent) ac);
	}

	private AbstractComponent addCamposObligotorios(AbstractComponent ac) {

		List<AbstractComponent> children2 = new ArrayList<AbstractComponent>();

		boolean nuevaLista = false;
		List children = ac.getChildren();

		if (children != null) {
			int len = children.size();
			for (int i = (len - 1); i >= 0; i--) {
				AbstractComponent co = (AbstractComponent) children.get(i);
				AbstractComponent co2 = addCamposObligotorios(co);
				if (co != co2) {
					children.remove(co);
					children.add(i, co2);
				}
			}
		}

		if (ac instanceof Constrainted) {

			Constrainted c = (Constrainted) ac;
			Constraint cn = c.getConstraint();

			if (cn != null) {

				Label l = new Label();
				l.setValue("(*)");
				l.setStyle("color:red");

				Hlayout hl = new Hlayout();
				hl.getChildren().add(ac);
				hl.getChildren().add(l);

				ac = hl;

			}
		}

		return ac;

	}

	// Crea un nuevo Window recibiendo como parametro el Path del zul..
	public Window createWindow(Window window, String zulPath) {

		window = (Window) Executions.createComponents(zulPath,
				this.mainComponent, null);
		this.addCamposObligotorios(window);

		return window;
	}

	public MyConverter getCnv() {
		return new MyConverter();
	}

	public boolean isSiempreHabilitado() {
		return siempreHabilitado
				&& this.getCondicionComponenteSiempreHabilitado();
	}

	public void setSiempreHabilitado(boolean siempreHabilitado) {
		this.siempreHabilitado = siempreHabilitado;
	}

	public abstract boolean getCondicionComponenteSiempreHabilitado();

	public Check getCheck() {
		return check;
	}

	public void setCheck(Check check) {
		this.check = check;
	}

	/*
	 * public Object getAtributoSession(String arg) { Session s =
	 * Sessions.getCurrent(); Object atributo = s.getAttribute(arg); return
	 * atributo; }
	 */

	public void xhabilitarMenu() {
		Session s = Sessions.getCurrent();
		ControlInicio ctr = (ControlInicio) s
				.getAttribute(Config.CONTROL_INICIO);
		ctr.setMenuVisible(true);
	}

	public void xdeshabilitarMenu() {
		Session s = Sessions.getCurrent();
		ControlInicio ctr = (ControlInicio) s
				.getAttribute(Config.CONTROL_INICIO);
		ctr.setMenuVisible(false);
	}

	
	
	public void enmascarar(Component comp, String texto, int textoSize) {	
		// Pone un texto en formato de mascara de agua
		Vbox v = new Vbox();		
		v.setHeight("100%");
		v.setWidth("100%");
		v.setPack("center");
		v.setAlign("center");

		Label l = new Label();
		l.setValue(texto);
		l.setStyle("font-weight:bold;font-size:"+textoSize+"pt");		
					
		Div mask = new Div();
		mask.setZclass("z-modal-mask");
		mask.setStyle("z-index: 2000; display: block; background:#ffcc99; opacity:0.2; filter:alpha(opacity=30); ");
		
		l.setParent(v);
		v.setParent(mask);
		
		comp.getParent().appendChild(mask);	
	}

	// para poner una imagen los iconoss de la tilde y el contra mano
	public String getImgSiNo(boolean b){
		String out = Config.IMAGEN_CANCEL;
		if (b == true){
			out = Config.IMAGEN_OK;
		}
		return out;
	}
	

	/**
	 * Retorna un String que es el motivo de anulacion..
	 * Retorna vacio en caso de que se dio click en Cancelar..
	 * */
	public String getMotivoAnulacion(){
		
		Hbox hb = new Hbox();
		hb.setPack("center");
		hb.setWidth("100%");
		
		Vlayout vl = new Vlayout();
		vl.setParent(hb);
		
		Div dv = new Div();
		dv.setHeight("5px");
		dv.setParent(vl);
		
		Textbox tx = new Textbox();
		tx.setRows(5);
		tx.setWidth("380px");
		tx.setPlaceholder("Especifique el Motivo de Anulación..");
		tx.setParent(vl);
		
		BodyPopupAceptarCancelar b = new BodyPopupAceptarCancelar();	
		b.addComponente("", hb);
		b.setHighWindows("200px");
		b.showPopupUnaColumna("");
		if (b.isClickAceptar()) {
			return tx.getValue();
		}		
		return "";
	}
	
}

