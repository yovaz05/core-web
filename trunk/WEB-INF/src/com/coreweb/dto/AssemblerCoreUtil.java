package com.coreweb.dto;

import com.coreweb.domain.Domain;
import com.coreweb.domain.Register;

public abstract class AssemblerCoreUtil extends Assembler {
	
	public static UtilCoreDTO getDTOUtilCore(AssemblerCoreUtil as) {
		UtilCoreDTO dto = null;
		try {
			dto = (UtilCoreDTO) as.domainToDto(null);
			as.setUtilCoreDTO(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dto.setId(new Long(1));
		return dto;
	}

	public void setUtilCoreDTO(UtilCoreDTO dto) {
		// para gestion de usuarios (vere)
		dto.setHabilitado(this.listaSiNo());
	}

	
	@Override
	public DTO getDTObyId(String entityName, Long idObjeto) throws Exception {
		return this.getDTOUtilCore(this);
	}
	
}
