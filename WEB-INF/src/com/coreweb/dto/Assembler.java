package com.coreweb.dto;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import com.coreweb.domain.Domain;
import com.coreweb.domain.IiD;
import com.coreweb.domain.Register;
import com.coreweb.util.MyArray;
import com.coreweb.util.MyPair;

public abstract class Assembler {

	public static int MY_PAIR = 1;
	public static int MY_ARRAY = 2;
	public static int ASSEMBLER = 3;

	private List<Domain> subModel = new ArrayList<Domain>();

	public abstract Domain dtoToDomain(DTO dto) throws Exception;

	public abstract DTO domainToDto(Domain domain) throws Exception;

	public List<DTO> domainToDtoLista(List<Domain> list) throws Exception {

		List<DTO> out = new ArrayList<DTO>();
		Iterator<Domain> ite = list.iterator();
		while (ite.hasNext()) {
			Domain dom = ite.next();
			DTO dto = this.domainToDto(dom);
			out.add(dto);
		}

		return out;
	}

	// *************************************************************************************
	// *************************************************************************************
	
	public List<MyPair> listaSiNo() {
		List<MyPair> lImpo = new ArrayList<MyPair>();

		MyPair si = new MyPair();
		si.setId(new Long(1));
		si.setText("SI");
		MyPair no = new MyPair();
		no.setId(new Long(2));
		no.setText("NO");
		lImpo.add(si);
		lImpo.add(no);

		return lImpo;

	}

	// *************************************************************************************
	// *************************************************************************************

	public List<Domain> getSubModel() {
		return subModel;
	}

	public void setSubModel(List<Domain> subModel) {
		this.subModel = subModel;
	}

	// *************************************************************************************
	// *************************************************************************************

	/**
	 * obtiene de la DB un objeto según el ID del DTO
	 * 
	 * @param dto
	 * @param classDomain
	 * @return
	 * @throws Exception
	 */
	public Domain getDomain(DTO dto, Class classDomain) throws Exception {
		Domain dom = null;
		if (dto.esNuevo() == true) {
			// crea un nuevo objeto del dominio
			dom = (Domain) newInstance(classDomain);

		} else {
			// busca en la BD el objeto
			Register rr = Register.getInstance();
			dom = rr.getObject(classDomain.getName(), dto.getId());
		}
		return dom;
	}

	/**
	 * crea un DTO con la id del Domain
	 * 
	 * @param dom
	 * @param classDTO
	 * @return
	 * @throws Exception
	 */
	public DTO getDTO(Domain dom, Class classDTO) throws Exception {
		long id = -1;
		if (dom != null) {
			id = dom.getId();
		}
		DTO dto = (DTO) newInstance(classDTO);
		dto.setId(id);
		return dto;
	}

	// *************************************************************************************
	// *************************************************************************************

	/**
	 * Colpia los valores de los atributos iguales de un objeto a otro
	 * 
	 * @param desde
	 * @param hasta
	 * @param atributos
	 * @throws Exception
	 */
	public void copiarValoresAtributos(Object desde, Object hasta,
			String[] atributos) throws Exception {

		for (int i = 0; i < atributos.length; i++) {
			String att = atributos[i];

			// hacer el get
			Object value = getValue(desde, att);

			// hacer el set
			setValue(hasta, att, value);
		}

	}

	// *************************************************************************************
	// *************************************************************************************

	public void myPairToDomain(DTO dto, Domain dom, String atributo)
			throws Exception {

		MyPair mp = (MyPair) getValue(dto, atributo);
		if (mp == null) {
			// no tiene nada seteado, entonces retorna
			return;
		}

		myPairToDomain(mp, dom, atributo);
	}

	protected void myPairToDomain(IiD mp, Domain dom, String atributo)
			throws Exception, NoSuchFieldException {
		Object value = getValue(dom, atributo);
		
		System.out.println("Dom:" + dom + "" + dom.getClass().getName() +" atributo:" + atributo + " value" + value);

		// ver si el objeto del dominio ya tiene el mismo valor seteado
		if (value != null) {
			Domain asso = (Domain) value;
			if (asso.getId().longValue() == mp.getId().longValue()) {
				// es el mismo, no hace nada
				return;
			}
		}

		// recuperar el objeto de la base de la datos y hacer el set
		Register rr = Register.getInstance();
		String entidad = getEntidadAtributo(dom, atributo);
		Domain d = rr.getObject(entidad, mp.getId());
		setValue(dom, atributo, d);

		rr.saveObject(dom);
		mp.setId(d.getId());
	}

	public void domainToMyPair(Domain dom, DTO dto, String atributo)
			throws Exception {
		domainToMyPair(dom, dto, atributo, "descripcion");
	}

	public void domainToMyPair(Domain dom, DTO dto, String atributo,
			String campo) throws Exception {

		domainToMyPair(dom, dto, atributo, new String[] { campo });
	}

	public void domainToMyPair(Domain dom, DTO dto, String atributo,
			String[] campos) throws Exception {
		// obtengo el valor del dominio
		Domain dato = (Domain) getValue(dom, atributo);
		if (dato == null) {
			// no tiene nada seteado, entonces termina
			return;
		}
		String texto = "";
		int cnt = campos.length;
		for (int i = 0; i < cnt; i++) {
			texto += getValue(dato, campos[i]);
			texto += (i < (cnt - 1)) ? " - " : "";
		}

		// setea los valores
		MyPair mp = new MyPair();
		mp.setId(dato.getId());
		mp.setText(texto);

		setValue(dto, atributo, mp);
	}

	public MyPair pasaDomainToMyPair(Domain dom) throws Exception {
		// obtengo el valor del dominio
		Object dato = getValue(dom, "descripcion");

		// setea los valores
		MyPair mp = new MyPair();
		mp.setId(dom.getId());
		mp.setText(dato.toString());

		return mp;
	}

	// NOTA: este método se puede juntar con el de arriba
	public void domainToMyArray(Domain dom, DTO dto, String atributo,
			String[] campos) throws Exception {
		// obtengo el valor del dominio
		Domain dato = (Domain) getValue(dom, atributo);
		if (dato == null) {
			// no tiene nada seteado, entonces termina
			return;
		}
		MyArray mp = new MyArray();
		mp.setId(dato.getId());
		for (int i = 0; i < campos.length; i++) {
			String campo = campos[i];
			Object valor = getValue(dato, campo);
			setValue(mp, "pos" + (i + 1), valor);
		}
		setValue(dto, atributo, mp);
	}

	public MyArray createMyArray(Domain dom, String[] campos) throws Exception {
		MyArray mp = new MyArray();
		mp.setId(dom.getId());
		for (int i = 0; i < campos.length; i++) {
			String campo = campos[i];
			Object valor = getValue(dom, campo);
			setValue(mp, "pos" + (i + 1), valor);
		}
		return mp;
	}

	public void myArrayToDomain(DTO dto, Domain dom, String atributo)
			throws Exception {

		MyArray mp = (MyArray) getValue(dto, atributo);
		if (mp == null) {
			// no tiene nada seteado, entonces retorna
			return;
		}

		Object value = getValue(dom, atributo);

		// ver si el objeto del dominio ya tiene el mismo valor seteado
		if (value != null) {
			Domain asso = (Domain) value;
			if (asso.getId().longValue() == mp.getId().longValue()) {
				// es el mismo, no hace nada
				return;
			}
		}

		// recuperar el objeto de la base de la datos y hacer el set
		Register rr = Register.getInstance();
		String entidad = getEntidadAtributo(dom, atributo);
		Domain d = rr.getObject(entidad, mp.getId());
		setValue(dom, atributo, d);

		rr.saveObject(dom);
		mp.setId(d.getId());
	}

	// *************************************************************************************
	// *************************************************************************************

	public void listaMyPairToListaDomain(DTO dto, Domain dom, String atributo)
			throws Exception {
		listaObjectToListaDomain(dto, dom, atributo,
				new String[] { "descripcion" }, false, false, MY_PAIR, null);

	}

	public void listaDomainToListaMyPair(Domain dom, DTO dto, String atributo)
			throws Exception {
		listaDomainToListaMyPair(dom, dto, atributo, "descripcion");
	}

	public void listaDomainToListaMyPair(Domain dom, DTO dto, String atributo,
			String campo) throws Exception {

		listaDomainToListaObject(dom, dto, atributo, new String[] { campo },
				MY_PAIR, null);
	}

	// *************************************************************************************
	// *************************************************************************************

	// este es el caso en donde la relación era con los ID, es decir, NO se
	// modificaba los
	// los campos de los objetos
	public void listaMyArrayToListaDomain(DTO dto, Domain dom, String atributo)
			throws Exception {
		String[] arr = {};
		listaMyArrayToListaDomain(dto, dom, atributo, arr, false, false);
	}

	public void listaMyArrayToListaDomain(DTO dto, Domain dom, String atributo,
			String[] campos, boolean add, boolean delete) throws Exception {

		listaObjectToListaDomain(dto, dom, atributo, campos, add, delete,
				MY_ARRAY, null);

	}

	public void listaDomainToListaMyArray(Domain dom, DTO dto, String atributo,
			String[] campos) throws Exception {

		listaDomainToListaObject(dom, dto, atributo, campos, MY_ARRAY, null);
	}

	// *************************************************************************************
	// *************************************************************************************

	public void listaDTOToListaDomain(DTO dto, Domain dom, String atributo,
			boolean add, boolean delete, Assembler ass) throws Exception {

		listaObjectToListaDomain(dto, dom, atributo, new String[] {}, add,
				delete, ASSEMBLER, ass);

	}

	public void listaDomainToListaDTO(Domain dom, DTO dto, String atributo,
			Assembler ass) throws Exception {

		listaDomainToListaObject(dom, dto, atributo, new String[] {},
				ASSEMBLER, ass);
	}

	// *************************************************************************************
	// *************************************************************************************

	public void hijoDtoToHijoDomain(DTO dto, Domain dom, String atributo,
			Assembler ass, boolean siActualiza) throws Exception {

		// se llama al get empresa??
		DTO mp = (DTO) getValue(dto, atributo);
		if (mp == null) {
			// no tiene nada seteado, entonces retorna
			return;
		}
		Register rr = Register.getInstance();
		Domain dd = null;
		if ((mp.esNuevo() == true) || (siActualiza == true)) {
			dd = ass.dtoToDomain(mp);
			rr.saveObject(dd);
			mp.setId(dd.getId());
		} else {
			String entidad = getEntidadAtributo(dom, atributo);
			dd = rr.getObject(entidad, mp.getId());
		}

		setValue(dom, atributo, dd);
		rr.saveObject(dom);
		dto.setId(dom.getId());
	}

	public void hijoDomainToHijoDTO(Domain dom, DTO dto, String atributo,
			Assembler ass) throws Exception {

		Domain dd = (Domain) getValue(dom, atributo);
		if (dd == null) {
			// no tiene nada seteado, entonces retorna
			return;
		}

		DTO mp = ass.domainToDto(dd);
		setValue(dto, atributo, mp);

	}

	// *************************************************************************************
	// *************************************************************************************

	// permite agregar y borrar en la base de datos
	public void listaObjectToListaDomain(DTO dto, Object dom, String atributo,
			String[] campos, boolean add, boolean delete, int tipo,
			Assembler ass) throws Exception {

		Class tipoColeccion = getTipoColeccion(dom, atributo);

		// primero obtener las listas
		Collection<Domain> listaDom = (Collection<Domain>) getValue(dom,
				atributo);
		Collection<IiD> listIiD = (Collection<IiD>) getValue(dto, atributo);

		listaDTOtoListaDomain(listIiD, listaDom, campos, add, delete, tipo,
				ass, tipoColeccion);

	}

	protected void listaDTOtoListaDomain(Collection<IiD> listIiD,
			Collection<Domain> listaDom, String[] campos, boolean add,
			boolean delete, int tipo, Assembler ass, Class tipoColeccion)
			throws Exception {

		Register rr = Register.getInstance();

		// hacer una copia de la lista de id que ya estan en el dominio
		// (oldLista)
		List<Domain> oldListaId = new ArrayList<Domain>();
		Iterator<Domain> iteOld = listaDom.iterator();
		while (iteOld.hasNext()) {
			Domain dAux = iteOld.next();
			oldListaId.add(dAux);
		}

		// recorrer la nueva lista de los id del dto y ver si están o no en el
		// dominio
		Iterator<IiD> iteIiD = listIiD.iterator();
		while (iteIiD.hasNext()) {
			Domain dAux = null;
			IiD mp = iteIiD.next();
			boolean esta = false;
			// recorrer la lista del dominio para ver si ya está
			for (int i = 0; i < oldListaId.size(); i++) {
				Domain dIte = oldListaId.get(i);
				if (dIte.getId().longValue() == mp.getId().longValue()) {
					esta = true;
					dAux = dIte; // guarda la referencia
				}
			}

			if (esta == false) {
				// recuperarlo de la BD
				dAux = rr.getObject(tipoColeccion.getName(), mp.getId());
			}

			if ((dAux == null) && (add == false)) {
				throw new Exception("No existe en " + tipoColeccion.getName()
						+ " este id:" + mp.getId() + ".");
			}

			// si no esta en la BD pero hay que agregarlo, entonces primero lo
			// creamos
			if ((dAux == null) && (add == true)) {
				dAux = (Domain) newInstance(tipoColeccion);
			}

			if (add == true) {
				// editar los valores del objeto, tratar segun el tipo
				if (tipo == MY_PAIR) {
					// MyPair
					Object vAux = getValue(mp, "text");
					String campo = campos[0];
					setValue(dAux, campo, vAux);

				} else if (tipo == MY_ARRAY) {
					// MyArray
					for (int i = 0; i < campos.length; i++) {
						Object vAux = getValue(mp, "pos" + (i + 1));
						// el myarray empieza de 1
						String campo = campos[i];

						if (vAux instanceof IiD) {
							// MyPair o MyArray
							myPairToDomain((IiD) vAux, dAux, campo);

						} else if (vAux instanceof Collection) {
							// Tratar la coleccion (Lista seguramente)

							Class tipoColeccionAux = getTipoColeccion(dAux,
									campo);

							// primero obtener las listas
							Collection<Domain> listaDomAux = (Collection<Domain>) getValue(
									dAux, campo);
							Collection<IiD> listIiDAux = (Collection<IiD>) vAux;

							listaDTOtoListaDomain(listIiDAux, listaDomAux,
									new String[] { "descripcion" }, add,
									delete, MY_PAIR, null, tipoColeccionAux);

						} else {
							// Tipo primitivo (String, long, int... )
							setValue(dAux, campo, vAux);
						}
					}

				} else if (tipo == ASSEMBLER) {
					// Otro DTO, usar parametro Assembler
					DTO dtoAux = (DTO) mp;
					listaDom.remove(dAux);
					dAux = ass.dtoToDomain(dtoAux);
					listaDom.add(dAux);

				} else {
					throw new Exception(
							"ERROR, no se definió el tipo correctamente, tipo: ("
									+ tipo + ") en " + tipoColeccion.getName()
									+ " con id:" + mp.getId() + "");
				}
				
				rr.saveObject(dAux);
				mp.setId(dAux.getId());
			}

			// si no estaba lo agregamos a la lista
			if (esta == false) {
				listaDom.add(dAux);
			}
		}

		// recorrer la lista original (oldLista) y quitar los que NO estan el
		// dto
		for (int i = 0; i < oldListaId.size(); i++) {
			Domain dAux = oldListaId.get(i);

			boolean esta = false;
			Iterator<IiD> iteAux = listIiD.iterator();
			while (iteAux.hasNext()) {
				IiD mp = iteAux.next();
				if (mp.getId().longValue() == dAux.getId().longValue()) {
					esta = true;
				}
			}
			if (esta == false) {
				listaDom.remove(dAux);
				if (delete == true) {
					// borrarlo de la DB
					rr.deleteObject(dAux);
				}
			}

		}
	}

	public void listaDomainToListaObject(Domain dom, DTO dto, String atributo,
			String[] campos, int tipo, Assembler ass) throws Exception {

		// primero obtener las listas
		Collection<Domain> listaDom = (Collection<Domain>) getValue(dom,
				atributo);
		Collection<IiD> listIiD = (Collection<IiD>) getValue(dto, atributo);
		// la lista del DTO debería estar vacia
		if (listIiD.size() > 0) {
			listIiD.removeAll(listIiD);
		}

		Iterator<Domain> iteD = listaDom.iterator();
		while (iteD.hasNext()) {
			Domain d = iteD.next();
			IiD auxIiD = null;

			if (tipo == MY_PAIR) {
				MyPair aux = new MyPair();
				String texto = getValue(d, campos[0]).toString();
				aux.setId(d.getId());
				aux.setText(texto);
				auxIiD = aux;

			} else if (tipo == MY_ARRAY) {
				MyArray aux = new MyArray();
				aux.setId(d.getId());
				for (int i = 0; i < campos.length; i++) {
					String campo = campos[i];
					Object value = getValue(d, campo);
					// los campos del MyArray empiezan del 1
					setValue(aux, "pos" + (i + 1), this.pasaDomainToDto(value));

				}
				auxIiD = aux;

			} else if (tipo == ASSEMBLER) {
				DTO aux = ass.domainToDto(d);
				auxIiD = aux;

			} else {
				throw new Exception(
						"ERROR, no se definió el tipo correctamente, valor: ("
								+ tipo + ")");
			}

			listIiD.add(auxIiD);
		}
	}

	// *************************************************************************************
	// *************************************************************************************

	public void domainToListaMyPair(DTO dto, String atributo, String entidad)
			throws Exception {
		domainToLista(dto, atributo, entidad, new String[] { "descripcion" },
				MY_PAIR);
	}

	public void domainToListaMyArray(DTO dto, String atributo, String entidad,
			String[] campos) throws Exception {
		domainToLista(dto, atributo, entidad, campos, MY_ARRAY);
	}

	private void domainToLista(DTO dto, String atributo, String entidad,
			String[] campos, int tipo) throws Exception {

		List<IiD> datos = new ArrayList<IiD>();
		Register rr = Register.getInstance();
		List<Domain> lDom = (List<Domain>) rr.getObjects(entidad);
		for (int i = 0; i < lDom.size(); i++) {
			Domain dom = lDom.get(i);
			IiD dato = null;
			if (tipo == MY_PAIR) {
				MyPair mp = new MyPair();
				mp.setId(dom.getId());
				mp.setText(getValue(dom, campos[0]) + "");

				dato = mp;
			} else if (tipo == MY_ARRAY) {
				MyArray ma = new MyArray();
				ma.setId(dom.getId());
				for (int j = 0; j < campos.length; j++) {
					String campo = campos[j];
					// setValue(ma, "pos"+(j+1), getValue(dom, campo));
					Object valor = getValue(dom, campo);

					setValue(ma, "pos" + (j + 1), this.pasaDomainToDto(valor));

				}
				dato = ma;
			}

			datos.add(dato);
		}
		// setea el DTO
		setValue(dto, atributo, datos);

	}

	private Object pasaDomainToDto(Object valor) throws Exception {
		Object out = null;

		if (valor instanceof Domain) {
			Domain d = (Domain) valor;
			MyPair mp = this.pasaDomainToMyPair(d);
			out = mp;

		} else if (valor instanceof Collection) {
			List<MyPair> lMp = new ArrayList<MyPair>();

			Collection<Domain> setDom = (Collection<Domain>) valor;
			Iterator<Domain> ite = setDom.iterator();
			while (ite.hasNext()) {
				Domain d = ite.next();
				MyPair mp = this.pasaDomainToMyPair(d);
				lMp.add(mp);
			}
			out = lMp;

		} else {
			out = valor;
		}
		return out;

	}

	
	
	// **********************************************************************

	// Para que un error en algun maping de atributos no haga que deje de
	// funcionar otras partes...
	public void utilDomainToListaMyPair(DTO dto, String atributo,
			String entidad) {
		try {
			this.domainToListaMyPair(dto, atributo, entidad);
		} catch (Exception e) {
			System.out.println("[Error] utilDomainToListaMyPair: atributo "
					+ atributo + " " + entidad);
			e.printStackTrace();
		}
	}

	public void utilDomainToListaMyArray(DTO dto, String atributo,
			String entidad, String[] campos) {
		try {
			domainToListaMyArray(dto, atributo, entidad, campos);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("[Error] utilDomainToListaMyArray: atributo "
					+ atributo + " " + entidad);
			e.printStackTrace();
		}
	}

	public void listaMyPairToListaDomain(List<MyPair> list, Class class1)
			throws Exception {
		Register rr = Register.getInstance();
		List<Domain> listDom = rr.getObjects(class1.getName());
		List<IiD> listIiD = new ArrayList<IiD>();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			MyPair mp = (MyPair) iterator.next();
			listIiD.add(mp);
		}

		listaDTOtoListaDomain(listIiD, listDom, new String[] { "descripcion" },
				true, true, MY_PAIR, null, class1);

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


	
	public DTO getDto(String entityName, IiD id) throws Exception{
		Register r = Register.getInstance();
		Domain d =  r.getObject(entityName, id.getId());
		DTO dto = this.domainToDto(d);
		return dto;
	}
	
	
	// *************************************************************************************
	// *************************************************************************************
	// *************************************************************************************
	// *************************************************************************************

	// obtiene la entidad de un atributo
	private String getEntidadAtributo(Object dom, String atributo)
			throws NoSuchFieldException {
		Field fd = dom.getClass().getDeclaredField(atributo);
		String entidad = fd.getType().getName();
		return entidad;
	}

	// hace un get
	private Object getValue(Object obj, String att) throws Exception {
		Field fd = obj.getClass().getDeclaredField(att);
		fd.setAccessible(true);
		return fd.get(obj);
	}

	// hace un set
	private void setValue(Object obj, String att, Object value)
			throws Exception {
		Field fd = obj.getClass().getDeclaredField(att);
		fd.setAccessible(true);
		fd.set(obj, value);
	}

	// obtiene el tipo generico de una coleccion
	private Class getTipoColeccion(Object obj, String att) throws Exception {
		Field fd = obj.getClass().getDeclaredField(att);
		fd.setAccessible(true);
		ParameterizedType tipoGenerico = (ParameterizedType) fd
				.getGenericType();
		Class<?> classLista = (Class<?>) tipoGenerico.getActualTypeArguments()[0];

		return classLista;
	}

	private Object newInstance(Class classObject) throws Exception {
		Constructor ctor = classObject.getConstructor();
		Object obj = ctor.newInstance();
		return obj;
	}

}
