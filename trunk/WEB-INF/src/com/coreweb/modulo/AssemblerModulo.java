package com.coreweb.modulo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.coreweb.domain.Domain;
import com.coreweb.domain.Formulario;
import com.coreweb.domain.IiD;
import com.coreweb.domain.Modulo;
import com.coreweb.domain.Operacion;
import com.coreweb.domain.Register;
import com.coreweb.dto.Assembler;
import com.coreweb.dto.DTO;
import com.coreweb.util.MyArray;
import com.coreweb.util.MyPair;


public class AssemblerModulo extends Assembler {

	
	public static ModuloDTO getDTOModulo() {
		ModuloDTO dto = null;
		try {
			AssemblerModulo as = new AssemblerModulo();
			dto = (ModuloDTO) as.domainToDto(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dto.setId(1);
		return dto;
	}
	
	
	
	@Override
	public Domain dtoToDomain(DTO dtoM) throws Exception {
		// TODO Auto-generated method stub
		ModuloDTO dto = (ModuloDTO) dtoM;
		return null;

	}

	@Override
	public DTO domainToDto(Domain domain) throws Exception {
		// TODO Auto-generated method stub
		ModuloDTO dto = new ModuloDTO();

		List<MyArray> listModArr = getModulos();
		
		dto.setModulos(listModArr);

		return dto;
	}



	public static List<MyArray> getModulos() throws Exception {
		List<MyArray> listModArr = new ArrayList<MyArray>();

		Register rr = Register.getInstance();
		// recorre los modulos
		List<Modulo> listMod = rr.getAllModulos();
		
		for (Iterator iterator = listMod.iterator(); iterator.hasNext();) {
			Modulo mod = (Modulo) iterator.next();
			MyArray modArr = new MyArray();

			modArr.setId(mod.getId());
			modArr.setPos1(mod.getNombre());
			modArr.setPos2(mod.getDescripcion());

			// recorre los formularios de cada modulo
			Set<Formulario> setForm = mod.getFormularios();
			List<MyArray> listFormArr = new ArrayList<MyArray>();
			
			for (Iterator iterator2 = setForm.iterator(); iterator2.hasNext();) {
				Formulario form = (Formulario) iterator2.next();
				MyArray formArr = new MyArray();

				formArr.setId(form.getId());
				formArr.setPos1(form.getLabel());
				formArr.setPos2(form.getDescripcion());
				formArr.setPos3(form.getUrl());
				formArr.setPos4(form.getAlias());
				MyPair formHabilitado = new MyPair();
				if (form.isHabilitado()) {
					formHabilitado.setId(1);
					formHabilitado.setText("SI");
				} else {
					formHabilitado.setId(2);
					formHabilitado.setText("NO");
				}
				formArr.setPos5(formHabilitado);
				//formArr.setPos5(form.isHabilitado());

				Set<Operacion> setOper = form.getOperaciones();
				List<MyArray> listOperArr = new ArrayList<MyArray>();
				
				for (Iterator iterator3 = setOper.iterator(); iterator3
						.hasNext();) {
					Operacion oper = (Operacion) iterator3.next();
					MyArray operArr = new MyArray();
					operArr.setId(oper.getId());
					operArr.setPos1(oper.getAlias());
					operArr.setPos2(oper.getNombre());
					operArr.setPos3(oper.getDescripcion());
					MyPair operHabilitada = new MyPair();
					if (oper.isHabilitado()) {
						operHabilitada.setId(1);
						operHabilitada.setText("SI");
					} else {
						operHabilitada.setId(2);
						operHabilitada.setText("NO");
					}
					operArr.setPos4(operHabilitada);
					//operArr.setPos4(oper.isHabilitado());
					operArr.setPos5(oper.getIdTexto());
					operArr.setPos6(oper.getFormulario().getId());
					
					listOperArr.add(operArr);
				}
				formArr.setPos6(listOperArr);
				listFormArr.add(formArr);
			}
			modArr.setPos3(listFormArr);
			listModArr.add(modArr);
		}
		return listModArr;
	}

	

	
	public static void main(String[] args) {
		try {
			Register rr = Register.getInstance();
			Modulo m = (Modulo) rr.getObject(Modulo.class.getName(), 1);
			
			AssemblerModulo ass = new AssemblerModulo();
			ModuloDTO mdto = (ModuloDTO) ass.domainToDto(m);
			
			System.out.println(mdto.getModulos().size());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


}
