package com.coreweb.dto;

import com.coreweb.Config;
import com.coreweb.domain.Domain;
import com.coreweb.domain.Register;
import com.coreweb.domain.Tipo;

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

	public void setUtilCoreDTO(UtilCoreDTO dto) throws Exception {
		// para gestion de usuarios (vere)
		dto.setHabilitado(this.listaSiNo());
		// para cargar los diferentes tipos y niveles de alertas
		Register rr = Register.getInstance();
		Tipo nivelAlertaInformativa = (Tipo) rr
				.getTipoPorSigla(Config.SIGLA_NIVEL_ALERTA_INFORMATIVA);
		Tipo nivelAlertaError = (Tipo) rr
				.getTipoPorSigla(Config.SIGLA_NIVEL_ALERTA_ERROR);
		Tipo tipoAlertaUno = (Tipo) rr
				.getTipoPorSigla(Config.SIGLA_TIPO_ALERTA_UNO);
		Tipo tipoAlertaMuchos = (Tipo) rr
				.getTipoPorSigla(Config.SIGLA_TIPO_ALERTA_MUCHOS);
		Tipo tipoAlertaComunitaria = (Tipo) rr
				.getTipoPorSigla(Config.SIGLA_TIPO_ALERTA_COMUNITARIA);
		dto.setNivelAlertaInformativa(this.tipoToMyPair(nivelAlertaInformativa));
		dto.setNivelAlertaError(this.tipoToMyPair(nivelAlertaError));
		dto.setTipoAlertaUno(this.tipoToMyPair(tipoAlertaUno));
		dto.setTipoAlertaMuchos(this.tipoToMyPair(tipoAlertaMuchos));
		dto.setTipoAlertaComunitaria(this.tipoToMyPair(tipoAlertaComunitaria));
	}

	@Override
	public DTO getDTObyId(String entityName, Long idObjeto) throws Exception {
		return this.getDTOUtilCore(this);
	}

}
