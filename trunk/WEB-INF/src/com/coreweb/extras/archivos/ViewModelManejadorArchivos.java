package com.coreweb.extras.archivos;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zkmax.zul.Filedownload;

import com.coreweb.util.Misc;

public class ViewModelManejadorArchivos {

	Misc m = new Misc();
	
	boolean modoAdm = false;
	String direBase = "";
	
	String direCCWeb = "";

	File fileCC = null;

	// la lista de archivos corrientes
	List<TipoFileHtml> listaArchivos = new ArrayList<>();

	@Init(superclass = true)
	public void initViewModelManejadorArchivos(
			@ExecutionArgParam("direBase") String direBase, @ExecutionArgParam("modoAdm") boolean modoAdm) {
		this.direBase = direBase;
		this.fileCC = new File(this.direBase);
		this.modoAdm = modoAdm;
		this.actualizarArchivos();
	}

	@AfterCompose(superclass = true)
	public void afterComposeViewModelManejadorArchivos() {

	}

	@Command
	public void clickFile(@BindingParam("file") TipoFileHtml file)
			throws FileNotFoundException {
		if (file.esDirectorio() == true) {
			this.fileCC = file.getFile();
			this.actualizarArchivos();
			BindUtils.postNotifyChange(null, null, this, "*");

		} else {
			// es archivo
			Filedownload.save(file.getInputStream(), file.getMimetypesFile(),
					file.getNombre());
		}

	}
	
	@Command
	public void eliminarFile(@BindingParam("file") TipoFileHtml file){
		String msg = "Está seguro que desea eliminar el archivo/directorio\n"+file.getNombre();
		boolean siDel = this.m.mensajeEliminar(msg);
		if (siDel == true){
			this.deleteFolder(file.getFile());
			this.actualizarArchivos();
			BindUtils.postNotifyChange(null, null, this, "*");
		}
		
	}
	
	private void deleteFolder(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) { //some JVMs return null for empty dirs
	        for(File f: files) {
	            if(f.isDirectory()) {
	                deleteFolder(f);
	            } else {
	                f.delete();
	            }
	        }
	    }
	    folder.delete();
	}
    	               
	@Listen("onUpload=#uploadArchivo")
	public void agregarFile(UploadEvent event){
		
		String path =  this.fileCC.getPath()+"/"+event.getMedia().getName();
		System.out.println("\n\n\n\n\n\n path:"+ path);
		m.uploadFile(path, (InputStream) new ByteArrayInputStream(event
				.getMedia().getStringData().getBytes()));
		this.actualizarArchivos();
		BindUtils.postNotifyChange(null, null, this, "*");
	}

	public String getDireBase() {
		return direBase;
	}

	public void setDireBase(String direBase) {
		this.direBase = direBase;
	}

	private void actualizarArchivos() {
		this.listaArchivos = new ArrayList<>();

		File[] fList = this.fileCC.listFiles();

		for (int i = 0; i < fList.length; i++) {
			File ff = fList[i];
			this.listaArchivos.add(new TipoFileHtml(ff));
		}
	}

	public List<TipoFileHtml> getListaArchivos() {
		return listaArchivos;
	}

	public void setListaArchivos(List<TipoFileHtml> listaArchivos) {
		this.listaArchivos = listaArchivos;
	}

	public String getDireCCWeb() {
		this.direCCWeb = "./";
		try {
			String cc = this.fileCC.getPath();
			int pIni = this.direBase.length();
			System.out.println("\n\n\n\n");
			System.out.println("direBase:" + this.direBase);
			System.out.println("      cc:" + cc);
			this.direCCWeb = "./" + cc.substring(pIni);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return direCCWeb;
	}

	public void setDireCCWeb(String direCCWeb) {
		this.direCCWeb = direCCWeb;
	}

	public static void main(String[] args) throws Exception {
		String f1 = "/home/daniel/datos/varios/propuestas/scg33/proyecto-scg33/scg33/";
		String f2 = "/home/daniel/datos/varios/propuestas/scg33/proyecto-scg33/scg33";
		int pIni = f1.length();
		String out = "./" + f2.substring(pIni);

		System.out.println(out);
	}

	public boolean isModoAdm() {
		return modoAdm;
	}

	public void setModoAdm(boolean modoAdm) {
		this.modoAdm = modoAdm;
	}

}

// ===================================================
// ===================================================
// ===================================================

