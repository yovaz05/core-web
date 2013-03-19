package com.coreweb.modulo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.coreweb.domain.Domain;
import com.coreweb.domain.Formulario;
import com.coreweb.domain.IiD;
import com.coreweb.domain.Modulo;
import com.coreweb.domain.Operacion;
import com.coreweb.domain.Ping;
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
		dto.setId(new Long(1));
		return dto;
	}

	// ==========================================================================

	@Override
	public Domain dtoToDomain(DTO dtoM) throws Exception {
		// TODO Auto-generated method stub
		ModuloDTO dto = (ModuloDTO) dtoM;

		List<MyArray> modulos = dto.getModulos();

		List<Domain> allModulos = new ArrayList<Domain>();
		List<Domain> allFormularios = new ArrayList<Domain>();
		List<Domain> allOperaciones = new ArrayList<Domain>();

		Register rr = Register.getInstance();

		// recorre cada modulo
		for (Iterator iterator = modulos.iterator(); iterator.hasNext();) {
			MyArray modArr = (MyArray) iterator.next();
			Modulo modDom = new Modulo();

			modDom.setId(modArr.getId());
			modDom.setNombre((String) modArr.getPos1());
			modDom.setDescripcion((String) modArr.getPos2());

			// recorre los formularios de cada modulo

			List<MyArray> formularios = (List<MyArray>) modArr.getPos3();
			Set<Formulario> setForm = new HashSet<Formulario>();
			for (Iterator iterator2 = formularios.iterator(); iterator2
					.hasNext();) {
				MyArray formArr = (MyArray) iterator2.next();
				Formulario formDom = new Formulario();

				formDom.setId(formArr.getId());
				formDom.setLabel((String) formArr.getPos1());
				formDom.setDescripcion((String) formArr.getPos2());
				formDom.setUrl((String) formArr.getPos3());
				formDom.setAlias((String) formArr.getPos4());
				if (((MyPair) formArr.getPos5()).getId() == 1) {
					formDom.setHabilitado(true);
				} else {
					formDom.setHabilitado(false);
				}

				List<MyArray> operaciones = (List<MyArray>) formArr.getPos6();
				Set<Operacion> setOper = new HashSet<Operacion>();
				for (Iterator iterator3 = operaciones.iterator(); iterator3
						.hasNext();) {
					MyArray operArr = (MyArray) iterator3.next();
					Operacion operDom = new Operacion();

					operDom.setId(operArr.getId());
					operDom.setAlias((String) operArr.getPos1());
					operDom.setNombre((String) operArr.getPos2());
					operDom.setDescripcion((String) operArr.getPos3());
					if (((MyPair) operArr.getPos4()).getId() == 1) {
						operDom.setHabilitado(true);
					} else {
						operDom.setHabilitado(false);
					}
					operDom.setIdTexto((String) operArr.getPos5());
					operDom.setFormulario(formDom);
					// rr.saveObject(operDom);
					setOper.add(operDom);
					allOperaciones.add(operDom);
				}
				formDom.setOperaciones(setOper);
				//rr.saveObject(formDom);
				setForm.add(formDom);
				allFormularios.add(formDom);
			}
			modDom.setFormularios(setForm);
			//rr.saveObject(modDom);
			allModulos.add(modDom);
		}
		
		rr.saveObjects(allModulos);
		rr.saveObjects(allFormularios);
		rr.saveObjects(allOperaciones);

		// controlar modulos
		List<Modulo> modulosDom = rr.getAllModulos();
		boolean existeM = false;
		for (Modulo moduloD : modulosDom) {
			for (Domain moduloNew : allModulos) {
				if (moduloD.getId() == moduloNew.getId()) {
					existeM = true;
				}
			}
			if (!existeM) {
				rr.deleteObject(moduloD);
			}
			existeM = false;
		}

		// controlar formularios
		List<Formulario> formulariosDom = rr.getAllFormulario();
		boolean existeF = false;
		for (Formulario formularioD : formulariosDom) {
			for (Domain formularioNew : allFormularios) {
				if (formularioD.getId() == formularioNew.getId()) {
					existeF= true;
				}
			}
			if (!existeF) {
				rr.deleteObject(formularioD);
			}
			existeF = false;
		}

		// controlar operaciones
		List<Operacion> operacionesDom = rr.getAllOperaciones();
		boolean existeO = false;
		for (Operacion operacionD : operacionesDom) {
			for (Domain operacionNew : allOperaciones) {
				if (operacionD.getId() == operacionNew.getId()) {
					existeO = true;
				}
			}
			if (!existeO) {
				rr.deleteObject(operacionD);
			}
			existeO = false;
		}

		Ping ping = new Ping();
		ping.setEcho("Configuracion modulo modificada: "
				+ System.currentTimeMillis());
		return ping;

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
					formHabilitado.setId(new Long(1));
					formHabilitado.setText("SI");
				} else {
					formHabilitado.setId(new Long(2));
					formHabilitado.setText("NO");
				}
				formArr.setPos5(formHabilitado);
				// formArr.setPos5(form.isHabilitado());

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
						operHabilitada.setId(new Long(1));
						operHabilitada.setText("SI");
					} else {
						operHabilitada.setId(new Long(2));
						operHabilitada.setText("NO");
					}
					operArr.setPos4(operHabilitada);
					// operArr.setPos4(oper.isHabilitado());
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

	public void listaMyArrayToListaDomain(List<MyArray> list, Class class1,
			String[] campos) throws Exception {
		Register rr = Register.getInstance();
		List<Domain> listDom = rr.getObjects(class1.getName());
		List<IiD> listIiD = new ArrayList<IiD>();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			MyArray mp = (MyArray) iterator.next();
			listIiD.add(mp);
		}

		listaDTOtoListaDomain(listIiD, listDom, campos, true, true, MY_ARRAY,
				null, class1);

	}

	public static void xmain(String[] args) {
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

	public static void main(String[] args) {
		try {

			ModuloDTO mdto = AssemblerModulo.getDTOModulo();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
