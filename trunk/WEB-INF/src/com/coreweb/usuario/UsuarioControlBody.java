package com.coreweb.usuario;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import com.coreweb.domain.Usuario;
import com.coreweb.dto.Assembler;
import com.coreweb.dto.DTO;
import com.coreweb.templateABM.Body;
import com.coreweb.util.MyArray;


public class UsuarioControlBody extends Body {

	@Init(superclass = true)
	public void initModuloControlBody() {
	}

	@AfterCompose(superclass = true)
	public void afterComposeModuloControlBody() {
	}

	@Override
	public Assembler getAss() {
		// TODO Auto-generated method stub
		return new AssemblerUsuario();
	}

	@Override
	public DTO getDTOCorriente() {
		// TODO Auto-generated method stub
		return this.dto;
	}

	@Override
	public void setDTOCorriente(DTO dto) {
		// TODO Auto-generated method stub
		this.dto = (UsuarioDTO) dto;

	}

	@Override
	public DTO nuevoDTO() throws Exception {
		// TODO Auto-generated method stub
		return AssemblerUsuario.getDTOUsuario();
	}

	@Override
	public String getEntidadPrincipal() {
		// TODO Auto-generated method stub
		return Usuario.class.getName();
	}

	@Override
	public List<DTO> getAllModel() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	UsuarioDTO dto = AssemblerUsuario.getDTOUsuario();

	public UsuarioDTO getDto() {
		return dto;
	}

	public void setDto(UsuarioDTO dto) {
		this.dto = dto;
	}

	MyArray selectedUsuario = null;
	MyArray selectedPerfilUsuario = null;
	MyArray selectedPerfilUsr = null;
	MyArray selectedPerfil = null;
	MyArray selectedPermiso = null;
	MyArray selectedModulo = null;
	MyArray selectedFormulario = null;
	MyArray selectedOperacion = null;

	public MyArray getSelectedUsuario() {
		return selectedUsuario;
	}

	public void setSelectedUsuario(MyArray selectedUsuario) {
		this.selectedUsuario = selectedUsuario;
	}

	public MyArray getSelectedPerfilUsuario() {
		return selectedPerfilUsuario;
	}

	public void setSelectedPerfilUsuario(MyArray selectedPerfilUsuario) {
		this.selectedPerfilUsuario = selectedPerfilUsuario;
	}

	public MyArray getSelectedPerfil() {
		return selectedPerfil;
	}

	public void setSelectedPerfil(MyArray selectedPerfil) {
		this.selectedPerfil = selectedPerfil;
		/*
		 * MyArray aux = new MyArray(); aux.setPos2(new Operacion());
		 * aux.setPos3(new Perfil()); this.selectedPermiso = aux;
		 */
		this.selectedPermiso = null;
	}

	public MyArray getSelectedPermiso() {
		return selectedPermiso;
	}

	public void setSelectedPermiso(MyArray selectedPermiso) {
		this.selectedPermiso = selectedPermiso;
	}

	public MyArray getSelectedModulo() {
		return selectedModulo;
	}

	public void setSelectedModulo(MyArray selectedModulo) {
		this.selectedModulo = selectedModulo;
	}

	public MyArray getSelectedFormulario() {
		return selectedFormulario;
	}

	public void setSelectedFormulario(MyArray selectedFormulario) {
		this.selectedFormulario = selectedFormulario;
	}

	public MyArray getSelectedOperacion() {
		return selectedOperacion;
	}

	public void setSelectedOperacion(MyArray selectedOperacion) {
		this.selectedOperacion = selectedOperacion;
	}	

	public MyArray getSelectedPerfilUsr() {
		return selectedPerfilUsr;
	}

	public void setSelectedPerfilUsr(MyArray selectedPerfilUsr) {
		this.selectedPerfilUsr = selectedPerfilUsr;
	}

	@Command()
	@NotifyChange("*")
	public void agregarPerfilAlUsuario() {
		if (!((List) this.selectedUsuario.getPos4()).contains(selectedPerfilUsr))
			((List) this.selectedUsuario.getPos4()).add(this.selectedPerfilUsr);

		this.setSelectedPerfilUsr(null);
	}

	@Command()
	@NotifyChange("*")
	public void eliminarUsuario() {
		if (this.selectedUsuario != null) {

			if (mensajeEliminar("Está seguro que quiere eliminar el usuario junto con todos sus perfiles y permisos?")) {
				// verificar que no este asociado a ningun objeto

				this.getDto().getUsuarios().remove(this.selectedUsuario);
			}
			this.setSelectedUsuario(null);
		}
	}

	@Command()
	@NotifyChange("*")
	public void agregarUsuario() {

		if (mensajeAgregar("Agregar un nuevo usuario?")) {
			// verificar que no este asociado a ningun objeto
			MyArray nUsr = new MyArray();
			nUsr.setPos1("--editar--");
			nUsr.setPos2("--editar--");
			nUsr.setPos3("--editar--");
			nUsr.setPos4(new ArrayList<MyArray>());
			this.getDto().getUsuarios().add(nUsr);
			this.setSelectedUsuario(nUsr);

		}
	}

	@Command()
	@NotifyChange("*")
	public void eliminarPerfilUsuario() {
		if (this.selectedPerfilUsuario != null) {

			if (mensajeEliminar("Está seguro que quiere eliminar el perfil del usuario?")) {
				// verificar que no este asociado a ningun objeto

				((List) this.selectedUsuario.getPos4())
						.remove(this.selectedPerfilUsuario);
			}
			this.setSelectedPerfil(null);
		}
	}

	@Command()
	@NotifyChange("*")
	public void eliminarPerfil() {
		if (this.selectedPerfil != null) {

			if (mensajeEliminar("Está seguro que quiere eliminar el perfil y sus permisos?")) {
				// verificar que no este asociado a ningun objeto

				this.getDto().getPerfiles().remove(this.selectedPerfil);
			}
			this.setSelectedPerfil(null);
		}
	}

	@Command()
	@NotifyChange("*")
	public void agregarPerfil() {

		if (mensajeAgregar("Agregar un nuevo perfil?")) {
			// verificar que no este asociado a ningun objeto
			MyArray nPerf = new MyArray();
			nPerf.setPos1("--editar--");
			nPerf.setPos2("--editar--");
			List<MyArray> listAux = new ArrayList<MyArray>();
			MyArray aux = new MyArray();
			aux.setPos2(new ArrayList<MyArray>());
			nPerf.setPos3(listAux);
			this.getDto().getPerfiles().add(nPerf);
			this.setSelectedPerfil(nPerf);

		}
	}

	@Command()
	@NotifyChange("*")
	public void eliminarPermiso() {
		if (this.selectedPermiso != null) {

			if (mensajeEliminar("Está seguro que quiere eliminar el permiso?")) {
				// verificar que no este asociado a ningun objeto

				((List) this.selectedPerfil.getPos3()).remove(selectedPermiso);
			}
			this.setSelectedPermiso(null);
		}
	}

	@Command()
	@NotifyChange("*")
	public void agregarPermiso() {

		if (mensajeAgregar("Agregar un nuevo permiso?")) {
			// verificar que no este asociado a ningun objeto
			MyArray nPerm = new MyArray();
			nPerm.setPos1(false);
			nPerm.setPos2(new MyArray());
			nPerm.setPos3(this.selectedPerfil.getId());
			((List) this.selectedPerfil.getPos3()).add(nPerm);
			this.setSelectedPermiso(nPerm);

		}
	}

}
