package com.coreweb.usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.coreweb.domain.Domain;
import com.coreweb.domain.Perfil;
import com.coreweb.domain.Permiso;
import com.coreweb.domain.Register;
import com.coreweb.domain.Usuario;
import com.coreweb.dto.Assembler;
import com.coreweb.dto.DTO;
import com.coreweb.modulo.AssemblerModulo;
import com.coreweb.util.MyArray;
import com.coreweb.util.MyPair;


public class AssemblerUsuario extends Assembler {
	public static UsuarioDTO getDTOUsuario() {
		UsuarioDTO dto = null;
		try {
			AssemblerUsuario as = new AssemblerUsuario();
			dto = (UsuarioDTO) as.domainToDto(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dto.setId(new Long(1));
		return dto;
	}

	@Override
	public Domain dtoToDomain(DTO dto) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DTO domainToDto(Domain domain) throws Exception {
		// TODO Auto-generated method stub
		UsuarioDTO dto = new UsuarioDTO();
		
		//========================================================================
		// hash entre usuarios y perfiles
		HashMap <Long,List<String>> usrPorPerfil = new HashMap <Long,List<String>>();
		//========================================================================
		
		List<MyArray> listUsrArr = new ArrayList<MyArray>();
		Register rr = Register.getInstance();
		
		// recorre los usuarios
		List<Usuario> listUsr = rr.getAllUsuarios();

		for (Iterator iterator = listUsr.iterator(); iterator.hasNext();) {
			Usuario usuario = (Usuario) iterator.next();
			MyArray usrArr = new MyArray();

			usrArr.setId(usuario.getId());
			usrArr.setPos1(usuario.getNombre());
			usrArr.setPos2(usuario.getLogin());
			usrArr.setPos3(usuario.getClave());

			Set<Perfil> setPerf = usuario.getPerfiles();
			
			List<MyArray> listPerfArr = new ArrayList<MyArray>();
			for (Iterator iterator2 = setPerf.iterator(); iterator2.hasNext();) {
				Perfil perfil = (Perfil) iterator2.next();
				MyArray perfArr = new MyArray();

				//agrega la lista en la que se almacenarán los usuarios del perfil
				List lista = usrPorPerfil.get(perfil.getId());
				if(lista == null){
					lista = new ArrayList<String>();
					usrPorPerfil.put(perfil.getId(), lista);
				}
				lista.add(usuario.getNombre());
				
				perfArr.setId(perfil.getId());
				perfArr.setPos1(perfil.getNombre());
				perfArr.setPos2(perfil.getDescripcion());
				// depende de si al traer los usuarios se necesitan tb los
				// permisos, por ahora trae igual
				Set<Permiso> setPerm = perfil.getPermisos();
				List<MyArray> listPermArr = new ArrayList<MyArray>();
				for (Iterator iterator3 = setPerm.iterator(); iterator3
						.hasNext();) {
					Permiso permiso = (Permiso) iterator3.next();
					MyArray permArr = new MyArray();

					permArr.setId(permiso.getId());
					permArr.setPos1(permiso.isHabilitado());
					permArr.setPos2(permiso.getOperacion());
					permArr.setPos3(permiso.getPerfil());

					listPermArr.add(permArr);
				}
				perfArr.setPos3(listPermArr);
				listPerfArr.add(perfArr);
			}
			usrArr.setPos4(listPerfArr);
			listUsrArr.add(usrArr);
		}
		dto.setUsuarios(listUsrArr);

		//recorre los perfiles

		List<MyArray> listPerfArr = new ArrayList<MyArray>();
		List<Perfil> listPerfi = rr.getAllPerfiles();

		for (Iterator iterator = listPerfi.iterator(); iterator.hasNext();) {
			Perfil perfil = (Perfil) iterator.next();
			MyArray perfArr = new MyArray();

			perfArr.setId(perfil.getId());
			perfArr.setPos1(perfil.getNombre());
			perfArr.setPos2(perfil.getDescripcion());
			perfArr.setPos3(usrPorPerfil.get(perfil.getId()));

			Set<Permiso> setPerm = perfil.getPermisos();
			List<MyArray> listPermArr = new ArrayList<MyArray>();
			for (Iterator iterator3 = setPerm.iterator(); iterator3.hasNext();) {
				Permiso permiso = (Permiso) iterator3.next();
				MyArray permArr = new MyArray();

				permArr.setId(permiso.getId());
				MyPair habilitado = new MyPair();
				if (permiso.isHabilitado()) {
					habilitado.setId(new Long(1));
					habilitado.setText("SI");
				} else {
					habilitado.setId(new Long(2));
					habilitado.setText("NO");
				}
				permArr.setPos1(habilitado);

				MyArray operMA = new MyArray();
				operMA = createMyArray(permiso.getOperacion(), new String[] {
						"alias", "nombre", "descripcion", "habilitado",
						"idTexto"});
				permArr.setPos2(operMA);

				permArr.setPos3(permiso.getPerfil().getId());

				listPermArr.add(permArr);
			}
			perfArr.setPos4(listPermArr);
			listPerfArr.add(perfArr);
		}
		dto.setPerfiles(listPerfArr);
		dto.setModulos(AssemblerModulo.getModulos());

		return dto;
	}

}
