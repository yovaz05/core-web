package com.coreweb;

import org.zkoss.zk.ui.Executions;



public class Config {

	public static String ALIAS_HABILITADO_SI_O_SI = "-- Alias habilitado si o si  --";
	public static String LOGEADO = "logeado";
	public static String USUARIO = "usuario";

	public static String PREFIX = "prefix";
	
	public static String INIT_CLASE = "com.?.inicio.Inicio";
	public static String INIT_METODO = "init";
	public static String INIT_AFTER_LOGIN = "afterLogin";
	
	public static String GRUPO_ADMINISTRACION = "Administracion";
	public static String GRUPO_VENTAS = "Ventas";
	public static String GRUPO_AUDITORIA = "Auditoria";
	public static String GRUPO_IMPORTACION = "Importacion";
	
	
	// Para acceder a los archivos por la Web o por el disco
	public static String DIRECTORIO_BASE_WEB = "";
	public static String DIRECTORIO_BASE_REAL = "";

	
	// directorios para reportes
	private static String REPORTES = "reportes";
	public static String DIRECTORIO_WEB_REPORTES;
	public static String DIRECTORIO_REAL_REPORTES;
	
	
	static {
		try {
			DIRECTORIO_BASE_WEB = Executions.getCurrent().getDesktop().getCurrentDirectory();
			DIRECTORIO_BASE_REAL = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
		} catch (Exception e) {
			System.err.println("");
			DIRECTORIO_BASE_WEB = "./";
			DIRECTORIO_BASE_REAL = "./";
		}
		DIRECTORIO_WEB_REPORTES = DIRECTORIO_BASE_WEB + REPORTES;
		DIRECTORIO_REAL_REPORTES = DIRECTORIO_BASE_REAL +  REPORTES;
		
	
	}
	
	

}
