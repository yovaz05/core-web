package com.coreweb.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import org.hamcrest.core.IsInstanceOf;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.coreweb.Config;
import com.coreweb.IDCore;
import com.coreweb.extras.browser.Browser;

import java.io.*;

import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

public class Register {

	HibernateUtil hibernateUtil = new HibernateUtil();
	
	// El register tiene que ser un sigleton
	private static Register instance = new Register();
	
	protected Register() {
	}

	public synchronized static Register getInstance() {
		if (instance == null) {
			instance = new Register();
		}
		return instance;
	}
	
	
	/********************************/
	/***** Hibernate functions ******/
	/********************************/
	
	public void resetTables() throws Exception {
		Configuration cfg = this.hibernateUtil.getConfiguration();
		cfg.setProperty("hibernate.hbm2ddl.auto", "update");
		this.hibernateUtil.forceRebuildSessionFactory(cfg);
	}

	public void dropAllTables() throws Exception {

		Random rand = new Random(System.currentTimeMillis());
		int v = 1000 + rand.nextInt(8999);
		String codigo = (" " + v).trim();

		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);

		System.out.println("NOTA: Está apunto de borrar toda la base de datos");
		System.out.print("Ingrese el código  [" + codigo + "] : ");
		String linea = br.readLine();
		if (codigo.compareTo(linea) != 0) {
			System.out.println("Código erroneo....");
			System.out
					.println("Base de Datos NO BORRADA, presione [ENTER] para salir");
			linea = br.readLine();
			throw new Exception("Error de carga de código");
		} else {
			System.out
					.print("Confirma que desea borrar la Base de datos ? [Y/N] : ");
			linea = br.readLine();
			if (linea.compareTo("Y") != 0) {
				System.out
						.println("Base de Datos NO BORRADA, presione [ENTER] para continuar");
				linea = br.readLine();
				throw new Exception("No se confirmó el borrado");
			}
		}

		System.out.println(".... borrando Base de datos ...");
		Configuration cfg = this.hibernateUtil.getConfiguration();
		cfg.setProperty("hibernate.hbm2ddl.auto", "create");
		this.hibernateUtil.forceRebuildSessionFactory(cfg);

	}

	public void SESSIONcloseSession(Session session) {
		this.closeSession(session);
	}

	private void closeSession(Session session) {
		// System.out.println("Entra en close Session.");
		if (session != null) { // && session.isOpen()){
			// System.out.println("cierro la Session.");
			session.close();
		}

	}

	/**
	 * Obtener una session, para grabar muchos objetos juntos
	 */
	public Session SESSIONgetSession() throws Exception {
		return this.getSession();
	}

	private Session getSession() throws Exception {
		Session session = this.hibernateUtil.getSession();

		if (session.isOpen() == false) {
			session = session.getSessionFactory().openSession();
		}
		return session;
	}

	public synchronized void saveObjects(List<Domain> ld, String user)
			throws Exception {

		Session session = null;

		try {
			session = getSession();
			session.beginTransaction();

			for (Iterator iterator = ld.iterator(); iterator.hasNext();) {
				Domain d = (Domain) iterator.next();
				saveObjectDomain(d, session, user);
			}

			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			throw e;
		} finally {
			closeSession(session);
		}

	}



	public synchronized void saveObject(Domain o, String user) throws Exception {

		Session session = null;
		Transaction tx = null;
		try {

			session = getSession();
			tx = session.beginTransaction();

			saveObjectDomain(o, session, user);

			tx.commit();

			// session.getTransaction().commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		} finally {
			closeSession(session);
		}

	}

	/**
	 * Para grabar más rápido, se obtiene la session y se usa esa.
	 */
	public void SESSIONsaveObjectDomain(Domain o, Session session, String user)
			throws Exception {
		this.saveObjectDomain(o, session, user);
	}

	static long kk = 0;
	
	// este es el que realmente graba
	private void saveObjectDomain(Domain o, Session session, String user)
			throws Exception {
		//System.out.println("   ("+(kk++)+")saveObjectDomain "+o.getClass().getName());
		o.setModificado(new Date());
		o.setUsuarioMod(user);
		o.definirOrden();
		if (o.esNuevo() == true) {
			session.save(o);
		} else {
			session.merge(o);
			// session.saveOrUpdate(o);
			// session.update(o);
		}
	}

	public synchronized Domain getObject(String entityName, long id)
			throws Exception {

		Session session = null;

		try {
			session = getSession();
			session.beginTransaction();

			// Object o = session.load(News.class, new Long(id));
			Object o = session.get(entityName, new Long(id));
			session.getTransaction().commit();

			return (Domain) o;

		} catch (Exception e) {
			session.getTransaction().rollback();
			throw e;
		} finally {
			closeSession(session);
		}
	}

	public synchronized Object getObject(String entityName, String campo,
			Object value) throws Exception {

		Vector v = new Vector();
		v.add(Restrictions.eq(campo, value));

		List l = getObjects(entityName, v, new Vector(), -1, -1);
		if (l.size() == 1) {
			return l.get(0);
		}
		return null;
	}

	public synchronized Object SESSIONgetObject(String entityName,
			String campo, Object value, Session session) throws Exception {

		Vector v = new Vector();
		v.add(Restrictions.eq(campo, value));

		List l = SESSIONgetObject_Real(entityName, v, new Vector(), -1, -1,
				session);
		if (l.size() == 1) {
			return l.get(0);
		}
		return null;
	}

	public synchronized void deleteObject(String entityName, long id)
			throws Exception {

		Session session = null;

		try {
			session = getSession();

			session.beginTransaction();

			// Object o = session.load(News.class, new Long(id));
			Object o = session.get(entityName, new Long(id));
			session.delete(o);
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			throw e;
		} finally {
			closeSession(session);
		}

	}

	public void deleteObject(Domain obj) throws Exception {
		deleteObject(obj.getClass().getName(), obj.getId().longValue());
	}

	public void deleteObjects(Set<Domain> setDomain) throws Exception {
		Iterator ite = setDomain.iterator();
		while (ite.hasNext()) {
			Domain d = (Domain) ite.next();
			deleteObject(d);
		}
	}

	public synchronized List getObjects(String entityName) throws Exception {
		return getObjects(entityName, new Vector(), new Vector(), -1, -1);
		// return getObject_Real(entityName, rest, orders, -1, -1);
	}

	public synchronized List getObjects(String entityName, String campo,
			Object value) throws Exception {

		Vector v = new Vector();
		v.add(Restrictions.eq(campo, value));

		return getObjects(entityName, v, new Vector(), -1, -1);
		// return getObject_Real(entityName, rest, orders, -1, -1);
	}

	protected synchronized List getObjects(String entityName, Vector rest,
			Vector orders) throws Exception {
		return getObjects(entityName, rest, orders, -1, -1);
		// return getObject_Real(entityName, rest, orders, -1, -1);
	}

	/* retorna la lista de objetos */
	protected synchronized List getObjects(String entityName, Vector rest,
			Vector orders, int ini, int max) throws Exception {
		// este metodo no debería hacer falta, lo pongo para hacer una prueba
		// nomas, deberia borrar y usar el metodo que sigue.
		boolean ok = false;
		int vuelta = 0;
		List l = null;

		while (ok == false) {
			try {
				l = getObject_Real(entityName, rest, orders, ini, max);
				ok = true;
			} catch (Exception e) {
				e.printStackTrace();
				this.hibernateUtil.rebuildSessionFactory();
				vuelta++;
				if (vuelta == 2) {
					System.out
							.println("******* que lo pario no deberia dar error *********");
					throw e;
				}
			}
		}
		return l;
	}

	protected synchronized List getObject_Real(String entityName, Vector rest,
			Vector orders, int ini, int max) throws Exception {

		Session session = null;

		try {
			session = getSession();
			session.beginTransaction();
			Criteria cri = session.createCriteria(entityName);
			cri.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			for (int i = 0; i < rest.size(); i++) {
				Criterion r = (Criterion) rest.elementAt(i);
				cri.add(r);
			}

			for (int i = 0; i < orders.size(); i++) {
				Order o = (Order) orders.get(i);
				cri.addOrder(o);
			}

			if ((ini >= 0) && (max >= 0)) {
				cri.setFirstResult(ini);
				cri.setMaxResults(max);
			}

			List list = cri.list();
			session.getTransaction().commit();

			return list;
		} catch (Exception e) {
			session.getTransaction().rollback();
			throw e;
		} finally {
			closeSession(session);
		}
	}

	protected synchronized List SESSIONgetObject_Real(String entityName,
			Vector rest, Vector orders, int ini, int max, Session session)
			throws Exception {

		try {
			Criteria cri = session.createCriteria(entityName);
			cri.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			for (int i = 0; i < rest.size(); i++) {
				Criterion r = (Criterion) rest.elementAt(i);
				cri.add(r);
			}

			for (int i = 0; i < orders.size(); i++) {
				Order o = (Order) orders.get(i);
				cri.addOrder(o);
			}

			if ((ini >= 0) && (max >= 0)) {
				cri.setFirstResult(ini);
				cri.setMaxResults(max);
			}

			List list = cri.list();

			return list;
		} catch (Exception e) {
			throw e;
		}
	}

	protected int getSizeObjects(String entityName, Vector rest)
			throws Exception {

		Session session = null;

		try {

			session = getSession();
			session.beginTransaction();
			Criteria cri = session.createCriteria(entityName);
			cri.setProjection(Projections.rowCount());

			if (rest != null) {
				for (int i = 0; i < rest.size(); i++) {
					Criterion r = (Criterion) rest.elementAt(i);
					cri.add(r);
				}
			}

			// String resultado = cri.uniqueResult().toString();
			// return Integer.parseInt(resultado);
			return Integer.parseInt(cri.uniqueResult().toString());

		} catch (Exception e) {
			session.getTransaction().rollback();
			throw e;
		} finally {
			closeSession(session);
		}

	}

	protected int getCountPage(String entityName, Vector rest, int pageSize)
			throws Exception {
		int countPage = 0;
		int size = getSizeObjects(entityName, rest);

		countPage = size / pageSize;

		if ((countPage * pageSize) < size) {
			countPage++;
		}
		return countPage;
	}

	protected List getPageObject(String entityName, Vector rest, Vector orders,
			int nPage, int pageSize) throws Exception {
		return getObjects(entityName, rest, orders, ((nPage - 1) * pageSize),
				pageSize);
	}

	// *******************************************

	public AgendaEvento getAgenda(int tipo, String clave) throws Exception {
		Vector v = new Vector();
		v.add(Restrictions.eq("tipo", tipo));
		v.add(Restrictions.eq("key", clave));

		List l = getObjects(AgendaEvento.class.getName(), v, new Vector());
		AgendaEvento a = (AgendaEvento) l.get(0);
		return a;
	}

	public Tecorei getTecorei() throws Exception {
		// retorna un chiste
		Tecorei te = null;
		// this.getObjects(entityName, rest, orders, ini, max)
		int n = this.getSizeObjects(Tecorei.class.getName(), new Vector());
		int v = 0;

		Random rand = new Random(System.currentTimeMillis());
		v = rand.nextInt(n);

		List l = this.getObjects(Tecorei.class.getName(), new Vector(),
				new Vector(), v, v);
		te = (Tecorei) l.get(0);
		return te;
	}

	public Operacion getOperacionFormulario(String aliasFormulario,
			String aliasOperacion) throws Exception {
		// cambiar este metodo, hay que usar una consulta Hql

		Vector v = new Vector();
		v.add(Restrictions.eq("alias", aliasFormulario));

		List l = getObjects(Formulario.class.getName(), v, new Vector());
		if (l.size() > 0) {
			Formulario f = (Formulario) l.get(0);
			Set<Operacion> ops = f.getOperaciones();
			Iterator<Operacion> ite = ops.iterator();
			while (ite.hasNext()) {
				Operacion o = ite.next();
				if (o.getAlias().compareTo(aliasOperacion) == 0) {
					return o;
				}
			}
		}

		return null;
	}

	public List<Usuario> getAllUsuarios() throws Exception {
		List l = getObjects(Usuario.class.getName(), new Vector(), new Vector());
		return l;
	}

	public List<Perfil> getAllPerfiles() throws Exception {
		List l = getObjects(com.coreweb.domain.Perfil.class.getName(),
				new Vector(), new Vector());
		return l;
	}

	public List<Permiso> getAllPermisos() throws Exception {
		List l = getObjects(com.coreweb.domain.Permiso.class.getName(),
				new Vector(), new Vector());
		return l;
	}

	public List<Modulo> getAllModulos() throws Exception {
		List l = getObjects(Modulo.class.getName(), new Vector(), new Vector());
		return l;
	}

	public List<Formulario> getAllFormulario() throws Exception {
		List l = getObjects(Formulario.class.getName(), new Vector(),
				new Vector());
		return l;
	}

	public List<Operacion> getAllOperaciones() throws Exception {
		List l = getObjects(com.coreweb.domain.Operacion.class.getName(),
				new Vector(), new Vector());
		return l;
	}

	public void deleteAllObjects(String entityName, String att, String valor)
			throws Exception {
		Vector vr = new Vector();
		vr.add(Restrictions.eq(att, valor).ignoreCase());

		List<Domain> l = getObjects(entityName, vr, new Vector());
		for (int i = 0; i < l.size(); i++) {
			this.deleteObject(l.get(i));
		}
	}

	public void deleteAllObjects(String entityName) throws Exception {

		List<Domain> l = getObjects(entityName, new Vector(), new Vector());
		for (int i = 0; i < l.size(); i++) {
			this.deleteObject(l.get(i));
		}
	}

	public Usuario getUsuario(String login, String clave) throws Exception {
		Usuario u = null;

		Vector v = new Vector();
		v.add(Restrictions.eq("login", login));
		v.add(Restrictions.eq("clave", clave));

		List l = getObjects(Usuario.class.getName(), v, new Vector());
		if (l.size() == 1) {
			u = (Usuario) l.get(0);
		}

		return u;
	}

	public Perfil getPerfil(String perfil) throws Exception {
		Perfil p = null;

		Vector v = new Vector();
		v.add(Restrictions.eq("nombre", perfil));

		List l = getObjects(Perfil.class.getName(), v, new Vector());
		if (l.size() == 1) {
			p = (Perfil) l.get(0);
		}

		return p;
	}

	public List<Domain> selectFrom(String entity, String where)
			throws Exception {
		List<Domain> list = new ArrayList<Domain>();
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();

			Query q = session.createQuery("from " + entity + " as t where t."
					+ where);
			list = q.list();
			session.getTransaction().commit();

		} catch (Exception e) {
			session.getTransaction().rollback();
			throw e;
		} finally {
			closeSession(session);
		}

		return list;
	}

	public Object hqlToObject(String query) throws Exception {
		Object out = null;
		List l = this.hql(query, new Object[] {});
		if (l.size() == 1) {
			out = l.get(0);
		} else {
			throw new Exception("Más de un objeto (" + l.size()
					+ ") para el query \n" + query);
		}
		return out;
	}

	public Object hqlToObject(String query, Object o) throws Exception {
		Object out = null;
		List l = this.hql(query, new Object[] { o });
		if (l.size() == 1) {
			out = l.get(0);
		} else {
			throw new Exception("Más de un objeto (" + l.size()
					+ ") para el query \n" + query + "  param:" + o);
		}
		return out;
	}

	public synchronized List hql(String query) throws Exception {
		return this.hql(query, new Object[] {});
	}

	public synchronized List hql(String query, Object o) throws Exception {
		return this.hql(query, new Object[] { o });
	}

	public synchronized List hql(String query, Object[] param) throws Exception {

		//System.out.println("\n\n" + query + "\n\n");

		List list = new ArrayList<Domain>();
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();

			Query q = session.createQuery(query);
			for (int i = 0; i < param.length; i++) {
				Object o = param[i];
				q.setParameter(i, o);

			}
			list = q.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			throw e;
		} finally {
			closeSession(session);
		}

		return list;

	}

	/**
	 * Ejecuta una consulta hql
	 * 
	 * @param query
	 *            : consuta
	 * @param params
	 *            : Hash con los nombres de los parámetros y los valores
	 * @return
	 * @throws Exception
	 */
	public synchronized List hql(String query, Hashtable<String, Object> params)
			throws Exception {
		List list = new ArrayList<Domain>();
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();

			Query q = session.createQuery(query);
			Enumeration<String> keys = params.keys();
			while (keys.hasMoreElements()) {
				String k = keys.nextElement();
				Object v = params.get(k);
				// para quitar el ":" del parámetro
				if (k.toCharArray()[0] == ':'){
					k = k.substring(1);
				}
				q.setParameter(k, v);
			}
			list = q.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			throw e;
		} finally {
			closeSession(session);
		}

		return list;

	}

	public List SESSIONhql(String query, Object[] param, Session session)
			throws Exception {

		List list = new ArrayList<Domain>();
		try {

			Query q = session.createQuery(query);
			for (int i = 0; i < param.length; i++) {
				Object o = param[i];
				q.setParameter(i, o);
			}
			list = q.list();
		} catch (Exception e) {
			throw e;
		}

		return list;

	}

	public int hqlDelete(String query) throws Exception {
		return this.hqlDelete(query, new Object[] {});
	}

	public int hqlDelete(String query, Object o) throws Exception {
		return this.hqlDelete(query, new Object[] { o });
	}

	public int hqlDelete(String query, Object[] param) throws Exception {
		int out = 0;
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();

			Query q = session.createQuery(query);
			for (int i = 0; i < param.length; i++) {
				Object o = param[i];
				q.setParameter(i, o);
			}
			out = q.executeUpdate();
			session.getTransaction().commit();

		} catch (Exception e) {
			session.getTransaction().rollback();
			throw e;
		} finally {
			closeSession(session);
		}

		return out;

	}

	public int sql2(String sql) throws Exception {
		int out = 0;
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();

			SQLQuery q = session.createSQLQuery(sql);
			out = q.executeUpdate();

			session.getTransaction().commit();

		} catch (Exception e) {
			session.getTransaction().rollback();
			throw e;
		} finally {
			closeSession(session);
		}

		return out;

	}

	public AutoNumero getAutoNumero(String key) throws Exception {
		AutoNumero d = null;

		Vector v = new Vector();
		v.add(Restrictions.eq("key", key));

		List l = getObjects(AutoNumero.class.getName(), v, new Vector());
		if (l.size() == 1) {
			d = (AutoNumero) l.get(0);
		}
		return d;
	}

	// este usa el buscar elemento
	public List buscarElemento(Class clase, String[] atts, String[] values,
			String[] tipos, List<String> where, String join, String attOrden)
			throws Exception {
		return buscarElemento(clase, atts, values, tipos, false, true,
				Config.CUANTOS_BUSCAR_ELEMENTOS, true, where, join, attOrden);
	}

	public List buscarElemento(Class clase, String[] atts, String[] values,
			String[] tipos, String attOrden) throws Exception {
		return buscarElemento(clase, atts, values, tipos, false, true,
				Config.CUANTOS_BUSCAR_ELEMENTOS, true, new ArrayList(), "",
				attOrden);
	}

	// este usa el browser
	public List buscarElemento(Class clase, String[] atts, String[] values,
			String[] wheres, String[] tipos, boolean permiteFiltroVacio,
			String join, String attOrden) throws Exception {
		// armar la lista de wheres
		List<String> whereCl = new ArrayList();
		for (int i = 0; i < wheres.length; i++) {
			String w = wheres[i];
			if (w.trim().length() > 1) {
				whereCl.add(wheres[i]);
			}
		}

		return buscarElemento(clase, atts, values, tipos, permiteFiltroVacio,
				true, Config.CUANTOS_BUSCAR_ELEMENTOS, true, whereCl, join,
				attOrden);
	}

	public List buscarElemento(Class clase, String[] atts, String[] values,
			String[] tipos, boolean permiteFiltroVacio, boolean permiteLimite,
			int limite, boolean permiteLike, List<String> whereCl, String join,
			String attOrden) throws Exception {
		List l = new ArrayList<Object[]>();
		;

		// verificar que tenga algo que buscar
		if (permiteFiltroVacio == false) {

			String aux = "";
			for (int i = 0; i < values.length; i++) {
				aux += values[i].trim();
			}
			if (aux.length() < 1) {
				throw new Exception("Debe ingresar un criterio de filtro");
			}
		}

		String select = " ";
		// String where = " 1 = 1 and ";
		String where = " c.dbEstado != 'D' and ";

		// estos son los wheres que fueron agregados por el usuario al crear el
		// browser
		for (int i = 0; i < whereCl.size(); i++) {
			String w = whereCl.get(i).trim();
			where += w + " and ";
		}

		boolean like = permiteLike;
		int cnt = atts.length;
		for (int i = 0; i < cnt; i++) {
			String at = "c." + atts[i];
			String va = values[i].trim();

			int siLike = va.indexOf("%");
			permiteLike = like || (siLike >= 0);
			String strLike = this.likeStr(va, siLike >= 0);

			// va armando el select
			// prueba para las fechas
			select += at + " ,";

			// para armar el where que vienen del filtro de los textbox
			if (va.length() > 0) {
				String ww = "";

				if (tipos[i].compareTo(Config.TIPO_NUMERICO) == 0) {
					if (permiteLike == true) {
						ww = " cast(" + at + " as string) like " + strLike; // '%"
																			// +
																			// va.toLowerCase()+
																			// "%' ";
					} else {
						ww = at + " = " + va.toLowerCase();
					}

				} else if (tipos[i].compareTo(Config.TIPO_BOOL) == 0) {
					if (permiteLike == true) {
						// ww = " lower(str(" + at + ")) like  "+strLike; // '%"
						// + va.toLowerCase()+ "%' ";
					} else {
						// ww = at + " = " + va.toLowerCase();
					}
					ww = " lower(str(" + at + ")) like  " + strLike; // '%" +
																		// va.toLowerCase()+
																		// "%' ";

				} else if (tipos[i].compareTo(Config.TIPO_DATE) == 0) {
					if (permiteLike == true) {
						// ww = " lower(str(" + at + ")) like  "+strLike; // '%"
						// + va.toLowerCase()+ "%' ";
					} else {
						// ww = at + " = " + va.toLowerCase();
					}
					ww = " cast(" + at + " as string) like  " + strLike; // '%"
																			// +
																			// va.toLowerCase()+
																			// "%' ";

				} else {
					// por defecto es String
					if (permiteLike == true) {
						ww = " lower(" + at + ") like  " + strLike; // '%" +
																	// va.toLowerCase()+
																	// "%' ";
					} else {
						ww = " lower(" + at + ") = '" + va.toLowerCase() + "' ";
					}
				}

				where += " " + ww + " and ";
			}
		}

		select = "select " + select.substring(0, select.length() - 1);
		// quita el ultimo and
		where = "where " + where.substring(0, where.length() - 4);

		if (join.trim().length() > 0) {
			join = "join c." + join.trim();
		}

		String orden = "";
		if ((attOrden != null) && (attOrden.trim().length() > 0)) {
			orden = " order by " + attOrden + " asc ";
		}

		String hql = select + " from " + clase.getName() + " c " + join + " "
				+ where + orden;
		//System.out.println("\n\n\n" + hql + "\n\n\n");

		l = this.hql(hql);

		if ((permiteLimite == true) && (l.size() > limite)) {
			throw new Exception("más de '" + limite + "' elementos ("
					+ l.size() + ")...");
		}

		/*
		 * if (l.size() == 0) { throw new
		 * Exception("No se encontraron elementos ..."); }
		 */

		return l;
	}

	private String likeStr(String va, boolean siPorce) {
		String out = "";
		if (siPorce == true) {
			out = "'" + va.toLowerCase() + "'";
		} else {
			out = "'%" + va.toLowerCase() + "%'";
		}
		return out;
	}

	public boolean existe(Class clase, String atributo, String tipo,
			Object valor, IiD id) throws Exception {
		return existe(clase, new String[] { atributo }, new String[] { tipo },
				new String[] { (valor + "") }, id);
	}

	public boolean existe(Class clase, String[] atts, String[] tipos,
			String[] values, IiD id) throws Exception {
		boolean out = false;
		String where = "c.id != " + id.getId();
		List<String> lw = new ArrayList<String>();
		lw.add(where);

		List l = buscarElemento(clase, atts, values, tipos, false, false, 0,
				false, lw, "", "");

		return (l.size() > 0);
	}

	public Tipo getTipoPorSigla(String sigla) throws Exception {
		String queryTipo = "select t from Tipo t where t.sigla='" + sigla + "'";
		Tipo out = (Tipo) this.hqlToObject(queryTipo);
		return out;
	}

	
	public Tipo getTipoPorSiglaAuxi(String sigla, String auxi) throws Exception {
		Tipo t = null;
		try {
			String queryTipo = "select t from Tipo t where t.sigla='" + sigla + "' and  t.auxi='" + auxi + "'";
			t = (Tipo) this.hqlToObject(queryTipo);
		} catch (Exception e) {
		}
		return t;
	}
	
	
	public Tipo getTipoPorSigla_Index(String sigla, int id) throws Exception {
		String queryTipo = "select t from Tipo t where t.sigla='" + sigla
				+ "' order by t.id";
		List<Tipo> out = (List<Tipo>) this.hql(queryTipo);
		return out.get(id - 1);
	}

	public Tipo getTipoPorDescripcion(String descripcion) throws Exception {
		String queryTipo = "select t from Tipo t where t.descripcion='"
				+ descripcion + "'";
		Tipo out = (Tipo) this.hqlToObject(queryTipo);
		return out;
	}

	// retorna la lista de tipos segun el id tipoTipo..
	public List<Tipo> getTipos(String tipoTipoDescripcion) throws Exception {
		List<Tipo> list = null;
		String query = "select t from Tipo t where t.tipoTipo.descripcion = '"
				+ tipoTipoDescripcion + "'";
		list = this.hql(query);
		//System.out.println("query ("+list.size()+"):"+query);
		return list;
	}

	// retorna una alerta segun su id
	public Alerta getAlerta(long id) throws Exception {
		List<Alerta> list = null;
		String query = "select a from Alerta a where a.id = " + id;
		list = this.hql(query);
		return (Alerta) list.get(0);
	}

	// retorna todas las alertas
	public List<Alerta> getAllAlertas(int desde, int cantidad, String usuario)
			throws Exception {
		List<Alerta> list = null;
		Vector v = new Vector();
		v.add(Restrictions.like("destino", usuario, MatchMode.ANYWHERE));
		Vector v2 = new Vector();
		v2.add(Order.asc("cancelada"));
		v2.add(Order.desc("fechaCreacion"));
		list = this.getObjects(Alerta.class.getName(), v, v2, desde, cantidad);
		return list;
	}

	// retorna todas las alertas
	public synchronized int getCantidadAlertasNoCanceladas(String usuario)
			throws Exception {
		List<Alerta> list = null;
		String query = "select a from Alerta a where a.cancelada = 'false' and a.destino like '%"
				+ usuario + "%'";
		//System.out.println("\n\n\n" + query + "\n\n");
		list = this.hql(query);
		return list.size();
	}

	public static void main(String[] args) {
		try {
			String query = ""
					+ " select  c.id ,c.nroCuenta ,c.banco.descripcion  from com.yhaguy.domain.BancoCta c  where  c.dbEstado != 'D' and   lower(c.banco.descripcion) like  '%15%' ";

			Register rr = Register.getInstance();
			// List<Domain> l = rr.selectFrom(ContactoInterno.class.getName(),
			// "funcionario.id='2'");
			List l = rr.hql(query);
			for (int i = 0; i < l.size(); i++) {
				Object[] o = (Object[]) l.get(i);
				System.out.println(o[0] + " - " + o[1]);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void xxmain(String[] args) {
		try {
			String query = "select  c.id ,c.numeroPedidoCompra ,c.proveedor.empresa.razonSocial ,	c.importacionEstadoPedido.descripcion ,c.pedidoConfirmado ,	c.confirmadoVentas ,c.cambio  from com.yhaguy.domain.ImportacionPedidoCompra c where  1 = 1 and   lower(c.importacionEstadoPedido.descripcion) like '%env%'  and  	c.pedidoConfirmado = true  order by c.id asc";

			Register rr = Register.getInstance();
			// List<Domain> l = rr.selectFrom(ContactoInterno.class.getName(),
			// "funcionario.id='2'");
			List l = rr.hql(query);
			for (int i = 0; i < l.size(); i++) {
				Object[] o = (Object[]) l.get(i);
				System.out.println(o[0] + " - " + o[1]);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void printHql(String hql){
		try {
			int n = 0;
			List l = this.hql(hql);
			for (int i = 0; i < l.size(); i++) {
				
				String linea = "";
				Object fila = l.get(i);
				
				if (fila instanceof Object[]){
					Object[] ff = (Object[]) fila;
					for (int j = 0; j < ff.length; j++) {
						linea += ff[j]+" , ";
					}
				}else{
					linea = fila + "";
				}
				System.out.println((n++)+") "+linea);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
