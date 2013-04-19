package com.coreweb.control;

import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hamcrest.core.IsInstanceOf;
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
import org.zkoss.zk.ui.util.Template;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.SimpleConstraint;
import org.zkoss.zul.Window;
import org.zkoss.zul.ext.Constrainted;

import com.coreweb.dto.Assembler;
import com.coreweb.util.MyConverter;

public abstract class GenericViewModel extends Control {

	private static String DISABLED = "Disabled";
	private static String READONLY = "Readonly";
	private static String BUTTON_VISIBLE = "ButtonVisible";

	private boolean deshabilitado = true;

	/*
	 * esto es para darle un tratamiento especial a algunos que se tienen que
	 * deshabilitar/habilitar de forma especial
	 */

	private static Object[][] listaClasePropiedad = {
			{ Button.class, DISABLED, true },
			{ Bandbox.class, DISABLED, true }, { Radio.class, DISABLED, true },
			{ Combobox.class, BUTTON_VISIBLE, false },
			{ Datebox.class, BUTTON_VISIBLE, false } };

	private static Object[][] listaInstancePropiedad = new Object[listaClasePropiedad.length][3];

	static {
		for (int i = 0; i < listaInstancePropiedad.length; i++) {
			try {
				listaInstancePropiedad[i][0] = ((Class) listaClasePropiedad[i][0])
						.newInstance();
				listaInstancePropiedad[i][1] = listaClasePropiedad[i][1];
				listaInstancePropiedad[i][2] = listaClasePropiedad[i][2];
			} catch (Exception e) {
			}
		}
	}

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
	public abstract String getAliasFormularioCorriente();

	// para guardar el estado de los componentes, esto es porque segun los
	// permisos puede que haya algunos que ya estan desabilitados, y cuando se
	// restaure queremos que tenga su estado original
	private Set<Component> tmpComponentesDeshabilitados = new HashSet<Component>();

	@Init(superclass = true)
	public void initGenericViewModel(
			@ContextParam(ContextType.VIEW) Component view) {
		this.mainComponent = view;

	}

	public void readonlyAllComponents() {
		this.disableComponents((AbstractComponent) this.mainComponent, READONLY);
	}

	public void disableAllComponents() {
		this.disableComponents((AbstractComponent) this.mainComponent, DISABLED);
	}

	private void disableComponents(AbstractComponent ac, String property) {
		//System.out.println("-----paso: "+ac.getId() + " - " + ac.getClass().getName());
		this.deshabilitado = true;
		try {

			for (int i = 0; i < listaInstancePropiedad.length; i++) {

				if (listaInstancePropiedad[i][0].getClass().isInstance(ac)) {

					String propertyAux = (String) listaInstancePropiedad[i][1];
					boolean valueDisable = (boolean) listaInstancePropiedad[i][2];

					// si se va a aplicar la propiedad luego, entonces no es
					// necesario hacerlo
					// ahora. Además, si se hace ahora, luego da como que ya
					// tenia la propiedad de
					// antes y entonce luego no lo vuelve a habilitar
					if (propertyAux.compareTo(property) != 0) {
						this.disableComponente(ac, propertyAux, valueDisable);
					}

				}
			}

			// obtener su estado y guardar los deshabilitados
			this.disableComponente(ac, property, true);

		} catch (Exception e) {
		}

		/*
		 * if (ac instanceof Radiogroup){
		 * 
		 * Radiogroup rg = (Radiogroup)ac; List<Component> lr =
		 * rg.getChildren(); //rg.getModel() for (int i = 0; i < lr.size(); i++)
		 * { System.out.println("------------- Radio "+property);
		 * AbstractComponent r = (AbstractComponent)lr.get(i);
		 * disableComponents(r, property); } }
		 */

		/*
		Set<String> tm = ac.getTemplateNames();
		for (Iterator iterator = tm.iterator(); iterator.hasNext();) {
			String tname = (String) iterator.next();
			Template t = ac.getTemplate(tname);
			System.out.println("-----paso: template "+tname+" - "+ t.getClass().getName());
			Map<String, Object> m =  t.getParameters();
			Set<String> ks = m.keySet();
			for (Iterator iterator2 = ks.iterator(); iterator2.hasNext();) {
				String k = (String) iterator2.next();
				Object o = m.get(k);
				System.out.println("------------------------"+k+" - "+ o.getClass().getName());	
			}
			//disableComponents((AbstractComponent)t, property);
		}
		*/

		
		List children = ac.getChildren();
		if (children != null) {
			for (int i = 0; i < children.size(); i++) {
				AbstractComponent co = (AbstractComponent) children.get(i);
				disableComponents(co, property);
			}
		}

	}

	private void disableComponente(AbstractComponent ac, String property,
			boolean valueDiabled) throws Exception {

		// obtener su estado y guardar los deshabilitados
		Method mget = ac.getClass().getMethod("is" + property);
		boolean v = (Boolean) mget.invoke(ac);
		if (v == valueDiabled) {
			this.tmpComponentesDeshabilitados.add(ac);
		}

		// deshabilita el componente
		Method mset = ac.getClass().getMethod("set" + property, Boolean.TYPE);
		mset.invoke(ac, valueDiabled);
	}

	public void restoreAllReadonlyComponents() {
		this.restoreComponents((AbstractComponent) this.mainComponent, READONLY);
	}

	public void restoreAllDisabledComponents() {
		this.restoreComponents((AbstractComponent) this.mainComponent, DISABLED);
	}

	private void restoreComponents(AbstractComponent ac, String property) {
		this.deshabilitado = false;

		if (this.tmpComponentesDeshabilitados.contains(ac) == false) {
			try {

				for (int i = 0; i < listaInstancePropiedad.length; i++) {

					if (listaInstancePropiedad[i][0].getClass().isInstance(ac)) {
						String propertyAux = (String) listaInstancePropiedad[i][1];
						boolean valueDisable = (boolean) listaInstancePropiedad[i][2];
						this.restoreComponente(ac, propertyAux, !valueDisable);

					}
				}

				// habilita el componente
				this.restoreComponente(ac, property, false);

			} catch (Exception e) {
			}
		}

		/*
		 * if (ac instanceof Radiogroup){
		 * 
		 * Radiogroup rg = (Radiogroup)ac;
		 * 
		 * List<Component> lr = rg.getChildren(); //rg.getModel() for (int i =
		 * 0; i < lr.size(); i++) {
		 * System.out.println("------------- (restore) Radio "+property);
		 * AbstractComponent r = (AbstractComponent)lr.get(i);
		 * //disableComponents(r, property); } }
		 */

		List children = ac.getChildren();
		if (children != null) {
			for (int i = 0; i < children.size(); i++) {
				AbstractComponent co = (AbstractComponent) children.get(i);
				restoreComponents(co, property);
			}
		}
	}

	private void restoreComponente(AbstractComponent ac, String property,
			boolean valueEnable) throws Exception {
		// habilita el componente
		Method mset = ac.getClass().getMethod("set" + property, Boolean.TYPE);
		mset.invoke(ac, valueEnable);

	}

	public boolean isDeshabilitado() {
		return deshabilitado;
	}

	@GlobalCommand
	@NotifyChange("*")
	public void refreshComponentes() {

	}

	// Pone el (*) en los campos con constraint, si tiene constraint es obligatorio
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
	public Window createWindow(Window window, String zulPath){
		
		window = (Window) Executions.createComponents(zulPath, this.mainComponent, null);
		this.addCamposObligotorios(window);
		
		return window;
	}
	
	
	public MyConverter getCnv(){
		return new MyConverter();
	}
	

}
