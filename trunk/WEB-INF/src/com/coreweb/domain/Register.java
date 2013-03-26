package com.coreweb.domain;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;


import java.io.*;

public class Register {

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

	public void dropAllTables() {
		try {

			Random rand = new Random(System.currentTimeMillis());
			int v = 1000 + rand.nextInt(8999);
			String codigo = (" " + v).trim();

			InputStreamReader isr = new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(isr);

			System.out
					.println("NOTA: Está apunto de borrar toda la base de datos");
			System.out.print("Ingrese el código  [" + codigo + "] : ");
			String linea = br.readLine();
			if (codigo.compareTo(linea) != 0) {
				System.out.println("Código erroneo....");
				System.out
						.println("Base de Datos NO BORRADA, presione [ENTER] para continuar");
				linea = br.readLine();
				return;
			} else {
				System.out
						.print("Confirma que desea borrar la Base de datos ? [Y/N] : ");
				linea = br.readLine();
				if (linea.compareTo("Y") != 0) {
					System.out
							.println("Base de Datos NO BORRADA, presione [ENTER] para continuar");
					linea = br.readLine();
					return;
				}
			}

			System.out.println(".... borrando Base de datos ...");
			Configuration cfg = HibernateUtil.getConfiguration();
			cfg.setProperty("hibernate.hbm2ddl.auto", "create");
			HibernateUtil.forceRebuildSessionFactory(cfg);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void closeSession(Session session) {
		// System.out.println("Entra en close Session.");
		if (session != null) { // && session.isOpen()){
			// System.out.println("cierro la Session.");
			session.close();
		}

	}

	private Session getSession() throws Exception {
		Session session = HibernateUtil.getSession();

		if (session.isOpen() == false) {
			session = session.getSessionFactory().openSession();
		}
		return session;
	}

	public void saveObjects(List<Domain> ld) throws Exception {
		for (Iterator iterator = ld.iterator(); iterator.hasNext();) {
			Domain d = (Domain) iterator.next();
			this.saveObject(d);
		}
	}
	
	public synchronized void saveObject(Domain o) throws Exception {

		Session session = null;

		try {
			session = getSession();

			session.beginTransaction();

			if (o.esNuevo() == true) {
				session.save(o);
			} else {
				session.merge(o);
				// session.saveOrUpdate(o);
				// session.update(o);
			}

			session.getTransaction().commit();
		} catch (Exception e) {
			throw e;
		} finally {
			closeSession(session);
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
			throw e;
		} finally {
			closeSession(session);
		}
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

	protected synchronized List getObjects(String entityName, Vector rest,
			Vector orders) throws Exception {
		return getObjects(entityName, rest, orders, -1, -1);
		// return getObject_Real(entityName, rest, orders, -1, -1);
	}

	/* retorna la lista de objetos */
	protected synchronized List getObjects(String entityName, Vector rest,
			Vector orders, int ini, int max) throws Exception {
		// este metodo no debería hacer falta, lo pongo para hacer una prueba
		// nomas, deberñia borrar y usar el metodo que sigue.
		boolean ok = false;
		int vuelta = 0;
		List l = null;

		while (ok == false) {
			try {
				l = getObject_Real(entityName, rest, orders, ini, max);
				ok = true;
			} catch (Exception e) {
				e.printStackTrace();
				HibernateUtil.rebuildSessionFactory();
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
			throw e;
		} finally {
			closeSession(session);
		}
	}

	protected int getSizeObjects(String entityName, Vector rect)
			throws Exception {
		return getObjects(entityName, rect, new Vector()).size();
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

		List l = getObjects(
				AgendaEvento.class.getName(), v,
				new Vector());
		AgendaEvento a = (AgendaEvento) l.get(0);
		return a;
	}

	public Tecorei getTecorei() throws Exception {
		// retorna un chiste
		Tecorei te = null;
		// this.getObjects(entityName, rest, orders, ini, max)
		int n = this.getSizeObjects(
				Tecorei.class.getName(),
				new Vector());
		int v = 0;

		Random rand = new Random(System.currentTimeMillis());
		v = rand.nextInt(n);

		List l = this.getObjects(
				Tecorei.class.getName(),
				new Vector(), new Vector(), v, v);
		te = (Tecorei) l.get(0);
		return te;
	}

	public Operacion getOperacionFormulario(String aliasFormulario,
			String aliasOperacion) throws Exception {
		// cambiar este metodo, hay que usar una consulta Hql

		Vector v = new Vector();
		v.add(Restrictions.eq("alias", aliasFormulario));

		List l = getObjects(
				Formulario.class.getName(), v,
				new Vector());
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
		List l = getObjects(
				Usuario.class.getName(),
				new Vector(), new Vector());
		return l;
	}
	
	public List<Perfil> getAllPerfiles() throws Exception {
		List l = getObjects(
				com.coreweb.domain.Perfil.class.getName(),
				new Vector(), new Vector());
		return l;
	}
	
	public List<Permiso> getAllPermisos() throws Exception {
		List l = getObjects(
				com.coreweb.domain.Permiso.class.getName(),
				new Vector(), new Vector());
		return l;
	}


	public List<Modulo> getAllModulos() throws Exception {
		List l = getObjects(
				Modulo.class.getName(),
				new Vector(), new Vector());
		return l;
	}

	public List<Formulario> getAllFormulario() throws Exception {
		List l = getObjects(
				Formulario.class.getName(),
				new Vector(), new Vector());
		return l;
	}
	
	public List<Operacion> getAllOperaciones() throws Exception {
		List l = getObjects(
				com.coreweb.domain.Operacion.class.getName(),
				new Vector(), new Vector());
		return l;
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

		List l = getObjects(
				Usuario.class.getName(), v,
				new Vector());
		if (l.size() == 1) {
			u = (Usuario) l.get(0);
		}

		return u;
	}

	public Perfil getPerfil(String perfil) throws Exception {
		Perfil p = null;

		Vector v = new Vector();
		v.add(Restrictions.eq("nombre", perfil));

		List l = getObjects(
				Perfil.class.getName(), v,
				new Vector());
		if (l.size() == 1) {
			p = (Perfil) l.get(0);
		}

		return p;
	}

	public List<Domain> selectFrom(String entity, String where) throws Exception{
		List<Domain> list = new ArrayList<Domain>();
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();
	
			Query q = session.createQuery("from "+entity+" as t where t."+where);
			list = q.list();
			session.getTransaction().commit();

		} catch (Exception e) {
			throw e;
		} finally {
			closeSession(session);
		}
		
		return list;		
	}

	public Object hqlToObject(String query, Object o) throws Exception{
		Object out = null;
		List l = this.hql(query, new Object[]{o});
		if (l.size() > 0){
			out = l.get(0);
		}
		return out;
	}

	
	
	public List hql(String query) throws Exception{
		return this.hql(query, new Object[]{});
	}

	public List hql(String query, Object o) throws Exception{
		return this.hql(query, new Object[]{o});
	}
	
	public List hql(String query, Object[] param) throws Exception{
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
			throw e;
		} finally {
			closeSession(session);
		}
		
		return list;
		
	}

	
	public int hqlDelete(String query) throws Exception{
		return this.hqlDelete(query, new Object[]{});
	}

	public int hqlDelete(String query, Object o) throws Exception{
		return this.hqlDelete(query, new Object[]{o});
	}

	public int hqlDelete(String query, Object[] param) throws Exception{
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
			throw e;
		} finally {
			closeSession(session);
		}
		
		return out;
		
	}

	
	public int sql2(String sql) throws Exception{
		int out = 0;
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();

			SQLQuery q = session.createSQLQuery(sql);
			out = q.executeUpdate();
			
			session.getTransaction().commit();

		} catch (Exception e) {
			throw e;
		} finally {
			closeSession(session);
		}
		
		return out;
		
	}
	
	
	public static void main(String[] args) {
		try {
			String query = "" +
					" select ci.id, tipo.descripcion, cli.empresa.nombre" +
					" from  Cliente cli join cli.contactosInternos ci " +
					"               join ci.tipoContactoInterno tipo " +
					" where ci.funcionario.id = ? ";
						
			Register rr = Register.getInstance();
			//List<Domain> l = rr.selectFrom(ContactoInterno.class.getName(), "funcionario.id='2'");
			List l = rr.hql(query, new Object[]{(long)2});
			for (int i = 0; i < l.size(); i++) {
				Object[] o = (Object[])l.get(i);
				System.out.println(o[0] + " - " + o[1]);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
