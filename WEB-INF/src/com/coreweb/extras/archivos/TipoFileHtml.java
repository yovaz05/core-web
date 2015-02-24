package com.coreweb.extras.archivos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.activation.MimetypesFileTypeMap;

public class TipoFileHtml {
	
	static String IMG_DIRECTORIO = "/core/images/folder.png";
	static String IMG_ARCHIVO = "/core/images/file.png";
	
	String image = "";
	
	File f;
	
	public TipoFileHtml(File f){
		this.f = f;
	}
	
	public String getNombre(){
		String out = this.f.getName();
		return out;
	}
	
	public boolean esDirectorio(){
		return this.f.isDirectory();
	}
	
	public String getImage(){
		String out = IMG_ARCHIVO;
		if (this.f.isDirectory() == true){
			out = IMG_DIRECTORIO;
		}
		return out;
	}
	
	public FileInputStream getInputStream() throws FileNotFoundException{
		return new FileInputStream(this.f);
	}
	
	
	public String getMimetypesFile(){
		return new MimetypesFileTypeMap().getContentType(this.f);
	}
	
	public File getFile(){
		return this.f;
	}
	
}
