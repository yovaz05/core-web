package com.coreweb.extras.alerta;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;

import com.coreweb.componente.WindowPopup;
import com.coreweb.control.SoloViewModel;
import com.coreweb.domain.Register;
import com.coreweb.domain.Tecorei;

public class Alertas extends SelectorComposer<Component> {

	@Wire
	Window winAlertas;

	@Listen("onClick = #cerrarAlertas")
	public void cerrarAlertas(Event e) {
		winAlertas.detach();
	}

	
	
	public void mostrarAlertas() {

		try {

			Register rr = Register.getInstance();
			//Tecorei te = rr.getTecorei();
			
			//Map<String, Object> map = new HashMap<String, Object>();
			//map.put("chiste", te.getTexto());

			
			Window window = (Window) Executions.createComponents(
					"/core/misc/alertas.zul", null, null);
			window.doModal();

		} catch (Exception e) {
			// mostrar mensaje de error
			e.printStackTrace();
		}
	}
	
}
