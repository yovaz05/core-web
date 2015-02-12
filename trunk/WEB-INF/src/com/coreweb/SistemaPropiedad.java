package com.coreweb;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

/**
 * Lee un archivo ini en donde se pondrá información propia del sistema
 * Puede ser extendido por cada aplicación si lo cree necesario, pero no es necesario
 * invocar nada raro, sólo hay que poner los métodos especiales de nombres de 
 * valores y listo.
 * @author daniel
 *
 */
public class SistemaPropiedad {

	static private Hashtable<String, String> sysPro = null;

	// por defecto busca este archivo
	static private String FILE = Config.DIRECTORIO_BASE_REAL
			+ "/WEB-INF/sistema-propiedad.ini";

	// carga las propiedades
	static {
		reloadSistemaPropiedad();
	}

	public static synchronized void reloadSistemaPropiedad() {
		reloadSistemaPropiedad(FILE);
	}

	public static synchronized void reloadSistemaPropiedad(String file) {
		sysPro = new Hashtable<>();

		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream(file);

			// load a properties file
			prop.load(input);
			Enumeration<Object> enu = prop.keys();
			for (; enu.hasMoreElements();) {
				String k = (String) enu.nextElement();
				String v = prop.getProperty(k);
				sysPro.put(k, v);
			}

		} catch (Exception ex) {
			System.err.println("==========Sistema Propiedad =====================");
			ex.printStackTrace();
			System.err.println("=================================================");
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * Lee una propiedad del sistema-propiedad.ini, retorna null si no la encuentra
	 * @param propiedad
	 * @return
	 */
	public String getPropiedad(String propiedad){
		String out = sysPro.get(propiedad);
		if (out != null){
			out = out.trim();
		}
		return out;
	}
	
	// =====================================================
	// Configuración del correo
	
	public String getSmtpHost(){
		return this.getPropiedad("SMTP_HOST_NAME");
	}

	public String getSmtpPort(){
		return this.getPropiedad("SMTP_PORT");
	}

	public String getSmtpStatTlsEnable(){
		return this.getPropiedad("SMTP_START_TLS_ENABLE");
	}
	
	public String getEmailDefault(){
		return this.getPropiedad("EMAIL_FROM");
	}
	
	public String getEmailPassDefault(){
		return this.getPropiedad("EMAIL_FROM_PASSWORD");
	}
	
	public boolean siEnviarCorreo(){
		boolean b = Boolean.parseBoolean(this.getPropiedad("EMAIL_ENVIAR_CORREO"));
		return b;
	}
	
	
	// ==============================================
	
}
