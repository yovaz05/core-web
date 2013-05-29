package com.coreweb.templateABM;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.bind.Binder;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;

import com.coreweb.control.GenericViewModel;
import com.coreweb.dto.DTO;

public class Finder extends GenericViewModel {

	Window windowFinder;

	List<DTO> model = null;
	DTO selectedDTO = null;
	Execution execution = null;
	Body body = null;

	@Init(superclass = true)
	public void initFinder(@ContextParam(ContextType.VIEW) Window view,
			@ContextParam(ContextType.EXECUTION) Execution execution) {
		this.windowFinder = view;
		this.body = (Body) execution.getArg().get("body");

	}

	public DTO getSelectedDTO() {
		return selectedDTO;
	}

	public void setSelectedDTO(DTO selectedDTO) {
		this.selectedDTO = selectedDTO;
		System.out.println("----------------->" + selectedDTO);
	}

	@Command
	public void doTask() {
		if (this.selectedDTO != null) {
			this.body.setDTOCorriente(this.selectedDTO);
			this.windowFinder.detach();
		}
	}

	@Command
	public void discard() {
		this.windowFinder.detach();
	}

	@Override
	public String getAliasFormularioCorriente() {
		// TODO Auto-generated method stub
		return "Finder.java";
	}

}
