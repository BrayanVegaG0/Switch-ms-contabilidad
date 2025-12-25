package com.switchbank.mscontabilidad.mapper;

import com.switchbank.mscontabilidad.dto.CuentaDTO;
import com.switchbank.mscontabilidad.modelo.Cuenta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CuentaMapper {
    CuentaDTO aDTO(Cuenta cuenta);

    @Mapping(target = "version", ignore = true)
    Cuenta aEntidad(CuentaDTO cuentaDTO);
}
