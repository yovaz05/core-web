package com.coreweb;



public class Config {

	public static String LOGEADO = "logeado";
	public static String USUARIO = "usuario";

	public static String PREFIX = "prefix";
	
	
/*
	private static String iniFile = "./yhaguy.ini";

	private static String CLASS_PATH_KEY = "CLASS_PATH";
	public static String CLASS_PATH = "/home/daniel/datos/daniel/yhaguy/proyecto-documentacion/proyecto/classes";

	private static String IP_KEY = "IP";
	public static String IP = "127.0.1.1";

	public static void loadFile() {
		try {
			Properties p = new Properties();
			p.load(new FileInputStream("user.ini"));

			System.out.println("user = " + p.getProperty("DBuser"));
			System.out.println("password = " + p.getProperty("DBpassword"));
			System.out.println("location = " + p.getProperty("DBlocation"));
			p.list(System.out);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
*/
	public static String GRUPO_ADMINISTRACION = "Administracion";
	public static String GRUPO_VENTAS = "Ventas";
	public static String GRUPO_AUDITORIA = "Auditoria";
	public static String GRUPO_IMPORTACION = "Importacion";
	
	
	
	
	

}
