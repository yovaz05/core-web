package com.coreweb.control;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Init;

/**
 * Para ser usada en ventanas que no pertenecen a un template, por ejemplo, VindowsPopup
 *
 */

public abstract class SimpleViewModel extends GenericViewModel {

	
	@Init(superclass = true)
	public void initSimpleViewModel(){
		
	}
	
	@AfterCompose(superclass = true)
	public void afterComposeSimpleViewModel(){
		
	}
	
	

}
