package com.coreweb.util;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Messagebox;

import com.coreweb.Config;

//import com.yhaguy.Configuracion;
//import com.yhaguy.gestion.compras.importacion.ImportacionPedidoCompraDTO;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class Misc {

	private static long idUnique = 0;
	private String correosIncorrectos = "";
	private static String dir;

	private static String TEXTO_ROJO = "font-weight: bold; color:Red; text-align: right";
	private static String TEXTO_NARANJA = "font-weight: bold; color:Orange; text-align: right";
	private static String TEXTO_VERDE = "font-weight: bold; color:DarkGreen; text-align: right";
	private static String TEXTO_AZUL = "color: #003390; text-align: right";
	private static String LABEL_AZUL = "color:#003390; font-size:14px; text-align: center";
	private static String LABEL_ROJO = "color:red; font-size:14px; text-align: center";
	private static String BACKGROUND_NARANJA = "background:#f6d197";
	public static String COLOR_ROJO = "color:red";
	public static String SIMBOLO_ASTERISCO = "(*)";

	public static String YYYY_MM_DD = "yyyy-mm-dd";
	public static String YYYY_MM_DD_HORA_MIN_SEG = "yyyy-mm-dd h:m:s";
	public static String YYYY_MM_DD_HORA_MIN_SEG2 = "yyyy-mm-dd (h:m:s)";

	public static String LABEL_BORDER = "border:1px solid; border-color:#54afcb; padding:2px";

	public static String getDir() {
		return dir;
	}

	public static void setDir(String dir) {
		Misc.dir = dir;
	}

	public String getCorreosIncorrectos() {
		return correosIncorrectos;
	}

	public void setCorreosIncorrectos(String correosIncorrectos) {
		this.correosIncorrectos = correosIncorrectos;
	}

	public static synchronized long getIdUnique() {
		long tmp = System.currentTimeMillis();
		while (tmp == idUnique) {
			tmp = System.currentTimeMillis();
		}
		idUnique = tmp;
		return idUnique;
	}

	public String colorVariacion(double actual, double nuevo) {
		double d = nuevo - actual;
		double porc = d / actual * 100;
		String color = TEXTO_AZUL;

		if (porc >= 10) {
			color = TEXTO_ROJO;
		} else if (porc >= 0.01) {
			color = TEXTO_NARANJA;
		} else if (porc < -0.01) {
			color = TEXTO_VERDE;
		}

		return color;
	}

	// 1:color rojo, 2:color azul, 3:---
	public String colorVariacionByParam(int param) {

		String color = null;
		switch (param) {

		case 1:
			color = LABEL_ROJO;
			break;

		case 2:
			color = LABEL_AZUL;
			break;
		}

		return color;
	}

	public String rowColorDescuento(double valor) {
		String color = "";
		if (valor < 0) {
			color = BACKGROUND_NARANJA;
		}
		return color;
	}
	
	//Metodo para cambiar el color de las filas en el Grid del SubDiario Detalle
	public String rowColorCuentaHaber(int tipo){
		String color = "";
		if (tipo < 0.001) {
			color = BACKGROUND_NARANJA;
		}
		return color;
	}

	public java.util.Date stringToDate(String dd) {
		dd = dd + "0000000000000000000"; // son 19 caracteres, formato completo
		int anio = 0;
		int mes = 0;
		int dia = 0;
		int hora24 = 0;
		int minuto = 0;
		int segundo = 0;

		anio = Integer.parseInt(dd.substring(0, 4));
		mes = Integer.parseInt(dd.substring(5, 7)) - 1;
		dia = Integer.parseInt(dd.substring(8, 10));
		hora24 = Integer.parseInt(dd.substring(11, 13));
		minuto = Integer.parseInt(dd.substring(14, 16));
		segundo = Integer.parseInt(dd.substring(17, 19));

		Calendar calendar = Calendar.getInstance();
		calendar.set(anio, mes, dia, hora24, minuto, segundo);

		return calendar.getTime();

	}

	public String dateHoyToString() {
		return dateToString(new Date(), YYYY_MM_DD_HORA_MIN_SEG2);
	}

	
	public String dateToString(java.util.Date dd, String format) {
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String out2 = sdf.format(dd);
		if (true){
			return out2;
		}
		
		// de acá en adelante no se usa mas
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dd);
		

		int anio = calendar.get(Calendar.YEAR); // año
		int mes = calendar.get(Calendar.MONTH) + 1; // mes, de 0 a 11
		int dia = calendar.get(Calendar.DAY_OF_MONTH); // dia del mes

		int hora24 = calendar.get(Calendar.HOUR_OF_DAY); // hora en formato 24hs
		int minutos = calendar.get(Calendar.MINUTE);
		int segundos = calendar.get(Calendar.SECOND);

		String out = "";
		if (format.compareTo(YYYY_MM_DD) == 0) {
			out = (anio + "-" + lastNchars("00" + mes, 2) + "-" + lastNchars(
					"00" + dia, 2));
		}

		if ((format.compareTo(YYYY_MM_DD_HORA_MIN_SEG) == 0)
				|| (format.isEmpty())) {

			out = (anio + "-" + lastNchars("00" + mes, 2) + "-"
					+ lastNchars("00" + dia, 2) + " "
					+ lastNchars("00" + hora24, 2) + ":"
					+ lastNchars("00" + minutos, 2) + ":" + lastNchars("00"
					+ segundos, 2));
		}

		return out;

	}

	public Date getFechaManana() {

		Calendar dateCal = Calendar.getInstance();
		// make it now
		dateCal.setTime(new Date());
		// make it tomorrow
		dateCal.add(Calendar.DAY_OF_YEAR, 1);
		// Now set it to the time you want
		dateCal.set(Calendar.HOUR_OF_DAY, 0);
		dateCal.set(Calendar.MINUTE, 0);
		dateCal.set(Calendar.SECOND, 0);
		dateCal.set(Calendar.MILLISECOND, 0);
		return dateCal.getTime();
	}

	public Date getFechaHoy00() {

		Calendar dateCal = Calendar.getInstance();
		// make it now
		dateCal.setTime(new Date());
		// Now set it to the time you want
		dateCal.set(Calendar.HOUR_OF_DAY, 0);
		dateCal.set(Calendar.MINUTE, 0);
		dateCal.set(Calendar.SECOND, 0);
		dateCal.set(Calendar.MILLISECOND, 0);
		return dateCal.getTime();
	}

	public Date getFechaHoyDetalle() {

		Calendar dateCal = Calendar.getInstance();
		// make it now
		dateCal.setTime(new Date());
		return dateCal.getTime();
	}

	
	/**
	 * Obtiene los n ultimos caracteres de un string
	 */
	public String lastNchars(String tex, int n) {
		String blank = new String(new char[n]).replace("\0", " ");
		tex = blank + tex;
		int l = tex.length();
		String out = tex.substring(l - n, l);
		return out;
	}

	public String getFormatoPedidoCompra() {
		Calendar fecha = Calendar.getInstance();
		String dia = String.valueOf(fecha.get(Calendar.DATE));
		if (dia.length() == 1) {
			dia = "0" + dia;

		}

		String mes = String.valueOf(fecha.get(Calendar.MONTH) + 1);
		if (mes.length() == 1) {
			mes = "0" + mes;

		}

		String yy = String.valueOf(fecha.get(Calendar.YEAR));

		return yy + ":" + mes + ":" + dia;
	}

	public boolean esAproximado(double d1, double d2, double diferencia) {
		double diff = d1 - d2;
		double d = Math.sqrt(diff * diff);
		return d <= diferencia;
	}

	
	public boolean esIgual(double d1, double d2) {
		double diff = d1 - d2;
		return ((diff * diff) < 0.00001);
	}

	public double redondeo(double d) {
		double d2 = Math.rint(d * 1000) / 1000;
		return d2;
	}
	
	public double redondeoDosDecimales(double d){
		double d2 = Math.rint(d * 100) / 100;
		return d2;
	}

	/**
	 * Para poner numero en blanco si son cero
	 * 
	 * @param dato
	 * @param longitud
	 * @param izquierda
	 * @param blanco
	 * @return
	 */
	public String formato(Object dato, int longitud, boolean izquierda,
			boolean blanco) {
		if ((dato instanceof Double) && (blanco == true)) {
			double d = (double) dato;
			if ((d * d) < 0.0001) {
				dato = " ";
			}
		}

		return formato(dato, longitud, izquierda);
	}

	public String formato(Object dato, int longitud) {
		return formato(dato, longitud, true);
	}

	public String formato(Object dato, int longitud, boolean izquierda) {
		if (dato instanceof Boolean) {
			if ((boolean) dato) {
				dato = "Si";
			} else {
				dato = "No";
			}
		}

		if (dato instanceof Double) {
			NumberFormat formatter = new DecimalFormat("###,###,##0.00");
			String str = formatter.format(dato);
			dato = str;
		}

		return formato("" + dato, longitud, izquierda);
	}

	public String formatoGs(double dato, int longitud, boolean izquierda) {
		NumberFormat formatter = new DecimalFormat("###,###,##0");
		String str = formatter.format(dato);
		return formato(str, longitud, izquierda);
	}

	public String formatoDs(Object dato, int longitud, boolean izquierda) {
		NumberFormat formatter = new DecimalFormat("###,###,##0.00");
		String str = formatter.format(dato);
		return formato(str, longitud, izquierda);
	}

	public String formato(String dato, int longitud, boolean izquierda) {
		String bl = "                             ";
		String out = "";
		if (izquierda == true) {
			out = (dato + bl).substring(0, longitud);
		} else {
			out = bl + dato;
			int l = out.length();
			out = out.substring(l - longitud);
		}

		return out;
	}

	public double div(double d1, double d2) {
		double d = 0;
		if (d2 != 0) {
			d = d1 / d2;
		}
		return d;
	}

	/**
	 * Graba un string en un archivo. Si el archivo existe lo borra
	 * 
	 * @param pathFile
	 * @param data
	 * @throws Exception
	 */
	public void grabarStringToArchivo(String pathFile, String data)
			throws Exception {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(pathFile);
			out.write(data.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
		}

	}

	/**
	 * repite un string n veces
	 * 
	 * @param str
	 * @param numero
	 * @return
	 */
	public String repeatSrt(String str, int numero) {
		String out = "";
		for (int i = 0; i < numero; i++) {
			out += str;
		}
		return out;
	}

	public boolean chequearMultipleCorreos(String[] correos) {

		boolean mailCorrecto = true;
		String validador = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		if (correos[0].isEmpty() == false) {
			for (int i = 0; i < correos.length; i++) {
				boolean check = correos[i].matches(validador);
				if (check == false) {
					mailCorrecto = false;
					this.correosIncorrectos = correosIncorrectos + " , "
							+ correos[i];
				}
			}
		}
		return mailCorrecto;
	}

	public String encriptar(String cadena) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "** error encriptacion **";
		}
		md.reset();
		md.update(cadena.getBytes());
		byte[] b = md.digest();
		String out = toHexadecimal(b);
		return out;

	}

	private String toHexadecimal(byte[] digest) {
		String hash = "";
		for (byte aux : digest) {
			int b = aux & 0xff;
			if (Integer.toHexString(b).length() == 1)
				hash += "0";
			hash += Integer.toHexString(b);
		}
		return hash;
	}

	public String[] split(String dato, String regex) {
		String[] out = dato.split(regex);
		for (int i = 0; i < out.length; i++) {
			out[i] = out[i].trim();
		}
		return out;
	}

	public Map<String, String> getQueryParam(String query) {

		Map<String, String> map = new HashMap<String, String>();
		if (query.trim().length() > 0) {
			String[] params = query.split("&");

			for (int i = 0; i < params.length; i++) {
				String param = params[i];
				String name = param.split("=")[0];
				String value = param.split("=")[1];
				map.put(name, value);
			}
		}

		return map;
	}

	public String xxparserPalabrasAMayusculas(String pre, String str) {
		// pre = "F"
		// str = "palabraConString"
		// out = "F_PALABRA_CON_STRING"
		String out = "";

		String[] r = str.split("(?=[A-Z])");
		for (int i = 0; i < r.length; i++) {
			String p = (r[i]).trim();
			if (p.length() > 0) {
				out += "_" + p;
			}
		}
		out = pre + out.toUpperCase();
		return out;
	}

	public String parserPalabrasAMayusculas(String pre, String str) {
		// pre = "F"
		// str = "palabraConString"
		// out = "F_PALABRA_CON_STRING"
		String out = "";

		boolean era1 = false;
		String[] r = str.split("(?=[A-Z])");
		for (int i = 0; i < r.length; i++) {
			String p = (r[i]).trim();

			// elimina los blancos
			if (p.length() > 0) {
				if (era1 == true) {
					out += p;
				} else {
					out += "_" + p;
				}
				era1 = ((p.length() == 1) && (Character
						.isUpperCase(p.charAt(0))));
			}

		}
		out = pre + out.toUpperCase();
		return out;
	}

	public boolean testAliasInID(String tipo, String alias, Class aClass) {

		boolean out = false;
		try {

//			Class aClass = com.yhaguy.ID.class;

			String campo = this.parserPalabrasAMayusculas(tipo, alias);

			Field field = aClass.getField(campo);
			String valor = (String) field.get(null);
			out = (alias.compareTo(valor) == 0);
			if (out == false) {
				System.err.println("[Fa] Error atributo en clase ID: " + campo
						+ " = " + alias);
			}
		} catch (Exception e) {
			System.err.println("[Ex] Error atributo en clase ID: "
					+ e.getMessage() + " = " + alias);
		}

		return out;

	}

	public boolean testIdInAlias(Hashtable<String, String> aliasTipo, Class aClass) {

		System.out.println("-");
		boolean out = false;
		try {

			
//			Class aClass = com.yhaguy.ID.class;
			Field[] fields = aClass.getFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				String nombreVarianble = field.getName();
				String alias = (String) field.get(null);

//				System.out.println("nombreVarianble:"+nombreVarianble + "  alias:" + alias);
				String tipo = aliasTipo.get(alias);
				if (tipo == null) {
					System.err.println("[Fa] Error, la variable "
							+ nombreVarianble
							+ " no esta en el menu_config.ini");
					out = false;
				} else {
					String tAux = nombreVarianble.substring(0, 1);
					if (tipo.compareTo(tAux) != 0) {
						System.err.println("[Fa] Error, la variabnle "
								+ nombreVarianble + " es de tipo diferente a "
								+ tipo + "-" + tAux);
						out = false;
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("[Ex] Error " + e.getMessage());
		}

		return out;

	}

	public int numeroRandom(int maximo) {
		Random rand = new Random(System.currentTimeMillis());
		int v = rand.nextInt(maximo);
		return v;

	}
	
	public boolean containsOnlyNumbersAndPercent(String str) {

		// It can't contain only numbers if it's null or empty...
		if (str == null || str.length() == 0)
			return false;

		for (int i = 0; i < str.length(); i++) {

			// If we find a non-digit character we return false.
			if (!Character.isDigit(str.charAt(i)) && (Character.toString(str.charAt(i)).compareTo("%") != 0)){
				return false;
			}
			if ((Character.toString(str.charAt(i)).compareTo("%") == 0) && str.length() == 1) {
				return false;
			}
		}

		return true;
	}

	// Genera un PDF con JasperPrint y luego abre en una nueva pestanha
	public void redirectAndPrint(Map parameters, Collection dataSrc,
			String reportSrc, String destFileSrc, String link) {
		try {

			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(
					dataSrc);
			JasperPrint print = JasperFillManager.fillReport(reportSrc,
					parameters, ds);
			JasperExportManager.exportReportToPdfFile(print, destFileSrc);
			Executions.getCurrent().sendRedirect(link, "_blank");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Genera un archivo PDF con JasperPrint
	public void generarPDFconJasperPrint(Map parameters, Collection dataSrc,
			String reportSrc, String destFileSrc) {
		try {

			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(
					dataSrc);
			JasperPrint print = JasperFillManager.fillReport(reportSrc,
					parameters, ds);
			JasperExportManager.exportReportToPdfFile(print, destFileSrc);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void uploadFile(String path, String name, String ext,
			InputStream file) {
		try {
			OutputStream out = new java.io.FileOutputStream(path + name + ext);
			InputStream in = file;
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			in.close();
			out.flush();
			out.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public double obtenerPorcentaje(double valor, double porcentaje){
		return (valor * porcentaje) / 100;
	}

	// Para obtener un id unico =======================
	private static Object sycId = "para sincronizar";
	private static long idUnico = System.currentTimeMillis();

	public long getIdUnico() {

		synchronized (sycId) {
			long idAux = 0;
			while (idAux <= idUnique) {
				idAux = System.currentTimeMillis();
			}
			idUnico = idAux;
		}
		return idUnico;
	}

	// ======================================================

	
	
	public boolean mensajeEliminar(String texto) {

		org.zkoss.zul.Messagebox.Button b = Messagebox.show(texto, "Eliminar",
				new Messagebox.Button[] { Messagebox.Button.YES,
						Messagebox.Button.NO }, Messagebox.QUESTION, null);
		if ((b != null) && (b.compareTo(Messagebox.Button.YES)) == 0) {
			return true;
		}
		return false;
	}

	public boolean mensajeAgregar(String texto) {
		org.zkoss.zul.Messagebox.Button b = Messagebox.show(texto, "Agregar",
				new Messagebox.Button[] { Messagebox.Button.YES,
						Messagebox.Button.NO }, Messagebox.QUESTION, null);
		if ((b != null) && (b.compareTo(Messagebox.Button.YES)) == 0) {
			return true;
		}
		return false;

	}

	public void mensajeInfo(String texto) {
		org.zkoss.zul.Messagebox.Button b = Messagebox.show(texto,
				"Informacion",
				new Messagebox.Button[] { Messagebox.Button.OK },
				Messagebox.INFORMATION, null);
	}

	public void mensajeError(String texto) {
		org.zkoss.zul.Messagebox.Button b = Messagebox.show(texto, "Error",
				new Messagebox.Button[] { Messagebox.Button.OK },
				Messagebox.ERROR, null);
	}

	public boolean mensajeSiNo(String texto) {

		org.zkoss.zul.Messagebox.Button b = Messagebox.show(texto, "Confirmar",
				new Messagebox.Button[] { Messagebox.Button.YES,
						Messagebox.Button.NO }, Messagebox.QUESTION, null);

		if ((b != null) && (b.compareTo(Messagebox.Button.YES)) == 0) {
			return true;
		}
		return false;
	}
	
	//Metodo que retorna el valor del Iva a partir de un valor gravado..
	public double calcularIVA(double gravado, int porcentajeIva){		
		return (gravado / (100 + porcentajeIva)) * porcentajeIva;
	}
	
	//Metodo que retorna el valor Gravado sin Iva a partir de un valor Gravado iva incluido..
	public double calcularGravado(double gravado, int porcentajeIva){
		return (gravado / (100 + porcentajeIva)) * 100;
	}
	
	// ejecuta el método de una clase
	public void ejecutarMetoto(String clase, String metodo) throws Exception {
		
		Class cls = Class.forName(clase);
		Object obj = cls.newInstance();

		Class[] noparams = {};
		Method method = cls.getDeclaredMethod(metodo, noparams);
		method.invoke(obj, null);
		
	}
	
	
	
	

	/*  NO ESTA REVISADO SI FUNCIONA
	// ejecuta el método de una clase
	public void ejecutarMetoto(String clase, String metodo, Object[] params) throws Exception {
		
		Class cls = Class.forName(clase);
		Object obj = cls.newInstance();

		
		Class[] paramsT = new Class[params.length];
		for(int i=0; i<params.length; i++){
			paramsT[i] = params.getClass();
		}
		
		Method method = cls.getDeclaredMethod(metodo, paramsT);
		method.invoke(obj, params);
		
	}
	*/
	
	public String ceros(String str, int ancho){
		String out = "0000000000000000000000000000000000000000000000000000000000000000000000"+str.trim();
		out = out.substring(out.length()-ancho, out.length());
		return out;
	}
	
	
	public static void main(String[] args) {

		try {
			Misc m = new Misc();
			System.out.println(""+m.calcularIVA(0, 10));
			System.out.println(""+m.calcularGravado(0, 10));
			System.out.println(""+m.esAproximado(5000, 5100, 101));
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
