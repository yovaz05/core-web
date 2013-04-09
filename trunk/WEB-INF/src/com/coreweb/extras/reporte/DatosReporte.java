package com.coreweb.extras.reporte;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;



public class DatosReporte {

	public static final String TIPO_STRING = "STRING";
	public static final String TIPO_INTEGER = "INTEGER";
	public static final String TIPO_BIGDECIMAL = "BIGDECIMAL";
	public static final String TIPO_DATE = "java.util.Date";
	public static final String TIPO_DATEYEAR = "DATEYEAR";
	public static final String TIPO_DATEMONTH = "DATEMONTH";
	public static final String TIPO_DATEDAY = "DATEDAY";
	
	private CabeceraReporte cr; 
	private List<Object[]> data;
	private String titulo;
	private String usuario;
	private String archivo;
	
	public DatosReporte() {
		
		

		
/*
		List<Object[]> data = new ArrayList<>();
		
		Object[] objeto = new Object[3];
		objeto[0] = new Date(System.currentTimeMillis());
		objeto[1] = new String("Notebook");
		objeto[2] = 1;
		data.add(objeto);
		
		objeto = new Object[3];
		objeto[0] = new Date(System.currentTimeMillis()-86400000);
		objeto[1] = new String("PDA");
		objeto[2] = 4;
		data.add(objeto);
	*/	
		
	}

	public void setTitulosCabecera(List<Object[]> titulos){
		cr = new CabeceraReporte();
		for (Object[] objects : titulos) {
			//for (int i = 0; i < objects.length; i++) {
			if(objects.length == 2)
				cr.addColumna((String)objects[0], (String)objects[1]);
			else if(objects.length == 3)
				cr.addColumna((String)objects[0], (String)objects[1],(Integer)objects[2]);
			//}
		}
		//cr.addColumna();
		//cr.addColumna("Cantidad", CabeceraReporte.TIPO_INTEGER);
	}
	public void setDatosReporte(List<Object[]> datos){
		data = datos; 		
	}
	public void setDatosReporte(String titulo){
		this.titulo = titulo; 		
	}
	public void setArchivo(String archivo){
		this.archivo = archivo; 		
	}
	
	public void setUsuario(String usuario){
		this.usuario = usuario;
	}
	
	
	public void ejecutar (){
		ejecutar(false);
	}
	public void ejecutar (boolean mostrar){
		MyReport reporte = new MyReport(cr, data, titulo, usuario, archivo);
		reporte.show(mostrar);
	}

	public static void main(String[] args) {
		
		DatosReporte dr = new DatosReporte();
		
		List<Object[]> cabecera = new ArrayList<Object[]>();
		
		Object[] objeto = new Object[]{"Fecha", TIPO_DATE};
		cabecera.add(objeto);
		objeto = new Object[]{"Item", TIPO_STRING};
		cabecera.add(objeto);

		objeto = new Object[]{"Cantidad", TIPO_INTEGER};
		cabecera.add(objeto);
		
		objeto = new Object[]{"Cantidad", TIPO_INTEGER};
		cabecera.add(objeto);
		objeto = new Object[]{"Cantidad", TIPO_INTEGER};
		cabecera.add(objeto);
		objeto = new Object[]{"Cantidad", TIPO_INTEGER};
		cabecera.add(objeto);
		
		
		
		dr.setTitulosCabecera(cabecera);
		
		List<Object[]> data = new ArrayList<>();

		Object[] obj = new Object[6];

		for (int i = 0; i < 200; i++) {
			obj = new Object[6];
			obj[0] = new Date(System.currentTimeMillis());
			obj[1] = new String("Dato "+i);
			obj[2] = 2 + i;
			obj[3] = 3 + i;
			obj[4] = 4 + i;
			obj[5] = 5 + i;
			data.add(obj);
			
		}

		
		dr.setDatosReporte(data);
		dr.setArchivo("./pepe.pdf");
		dr.setTitulo("El titulo del repooooooooorrrrrrte ");
		dr.setUsuario("Daniel Romero");
	
		dr.ejecutar(true);

	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
}
