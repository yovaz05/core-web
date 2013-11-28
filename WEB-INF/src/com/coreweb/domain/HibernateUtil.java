package com.coreweb.domain;

import org.hibernate.*;
import org.hibernate.cfg.*;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class HibernateUtil {

	private static Configuration configuration;
	private static SessionFactory sessionFactory;
	private static Session session;

	static {
		try {
			configuration = new Configuration();
					
			// Create the SessionFactory from hibernate.cfg.xml
			sessionFactory = configuration.configure().buildSessionFactory();
			
			/*
			System.out.println("====================================================");
			System.out.println("====================================================");
			System.out.println("====================================================");
			
			
			Properties p = configuration.getProperties();
			Set ks = p.keySet();
			for (Iterator iterator = ks.iterator(); iterator.hasNext();) {
				Object k = (String) iterator.next();
				Object o = configuration.getProperty((String)k);
				System.out.println("k: "+ k + " - " + o );
			}
			
			System.out.println("====================================================");
			System.out.println("====================================================");
			System.out.println("====================================================");
			 */
			
			session = sessionFactory.openSession();
			
		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			System.out.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	private static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static Configuration getConfiguration() {
		return configuration;
	}

	public static void forceRebuildSessionFactory(Configuration cfg) {
		configuration = cfg;
		sessionFactory = configuration.buildSessionFactory();
		System.out.println("---- rebuildSessionFactory----");
		synchronized (sessionFactory) {
			try {
				sessionFactory = configuration.buildSessionFactory();
				session = sessionFactory.openSession();
				System.out.println("---- ok rebuildSessionFactory----");
			} catch (Exception ex) {
				System.out.println("---- error !!! rebuildSessionFactory----");
				ex.printStackTrace();
			}
		}
	}

	public static void rebuildSessionFactory() {
		// si es null si o si hay que asignarle algo para sincronizar :(
		if (sessionFactory == null)
			sessionFactory = configuration.buildSessionFactory();
		System.out.println("---- rebuildSessionFactory----");
		synchronized (sessionFactory) {
			try {
				sessionFactory = configuration.buildSessionFactory();
				session = sessionFactory.openSession();
				System.out.println("---- ok rebuildSessionFactory----");
			} catch (Exception ex) {
				System.out.println("---- error !!! rebuildSessionFactory----");
				ex.printStackTrace();
			}
		}
	}

	public static Session getSession() throws Exception {
		checkSession();
		return session;
	}

	private static void checkSession() throws Exception {
		int v = 0;
		boolean ok = false;


		while (ok == false) {
			try {
				if (session.isOpen() == false) {
					// session = session.getSessionFactory().openSession();
					session = sessionFactory.openSession();
				}
				/*

				Criteria cri = session.createCriteria(Ping.class.getName());
				Order r = (Order) Order.asc("echo");
				cri.addOrder(r);

				List lo = cri.list();
				int size = lo.size();
				*/

				ok = true;

			} catch (Exception ex) {
				if (v >= 2) {
					ok = true;
					throw new Exception("No funciona la DB");
				}
				rebuildSessionFactory();
				v++;
			}
		}

	}

	public static void clearDB() {
		try {
			Register rr = Register.getInstance();
			rr.dropAllTables();

			Ping p = new Ping();
			p.setEcho("clearDB-" + System.currentTimeMillis());
			//rr.saveObject(p);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void pruebaPreparedStatement() {
		try {
			Class.forName("org.postgresql.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/yhaguydb", "postgres",
					"postgres");

			String sql = "SELECT * FROM usuariooperacion WHERE idformulario = ?";
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, 4);
			
			System.out.println("====================");
			ResultSet rd = stm.getResultSet();
			ParameterMetaData pmd = stm.getParameterMetaData();
			System.out.println("ParameterMetaData:"+pmd);
			System.out.println("pmd.getParameterCount():"+pmd.getParameterCount());
			System.out.println("pmd.getParameterTypeName(0):"+pmd.getParameterTypeName(1));
			
			
			
			//System.out.println("rd.getObject(1):"+rd.getObject(0));
			System.out.println("====================");
			
			
			ResultSet rs1 = stm.executeQuery();
			System.out.println("List");
			while (rs1.next()) {
				String alias = rs1.getString(2);
				Object idformulario = rs1.getObject(7);
				System.out.println(alias + " " + idformulario);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		//HibernateUtil.clearDB();

		pruebaPreparedStatement();
	}

}
