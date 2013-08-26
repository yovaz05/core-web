package com.coreweb;

import org.zkoss.zk.ui.Executions;



public class Config {

	public static String ANCHO_APP = "1024px";
	
	public static String ALIAS_HABILITADO_SI_O_SI = "-- Alias habilitado si o si  --";
	public static String LOGEADO = "logeado";
	public static String USUARIO = "usuario";
	public static String LOGIN = "login";
	public static String CONTROL_INICIO = "ControlInicio";

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
	
	// buscar elementos
	public static int CUANTOS_BUSCAR_ELEMENTOS = 100;
	public static int TAMANIO_MASCARA = 50;
	
	
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
	

	public static String DATO_SOLO_VIEW_MODEL = "dato";
	public static String WINDOW_POPUP = "windowsPopUp";
	public static String MODO_SOLO_VIEW_MODEL = "modo";
	public static String MODO_DISABLE = "disable";
	public static String MODO_NO_DISABLE = "si-editable";
	public static String MODO_EDITABLE = "editar-si-tiene-permisos";
	
	public static String TIPO_STRING = "String";
	public static String TIPO_NUMERICO = "Numerico";
	public static String TIPO_BOOL = "Bool";
	public static String TIPO_DATE = "Date";
	
	public static String IMAGEN_OK = "/core/images/accept_.png";
	public static String IMAGEN_CANCEL = "/core/images/delete_.png";
	public static String IMAGEN_CHECK = "/core/images/tick.png";
}
