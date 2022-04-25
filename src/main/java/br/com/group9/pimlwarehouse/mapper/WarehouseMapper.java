package br.com.group9.pimlwarehouse.mapper;

import br.com.group9.pimlwarehouse.dto.WarehouseDTO;
import br.com.group9.pimlwarehouse.entity.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WarehouseMapper {
    WarehouseMapper INSTANCE = Mappers.getMapper( WarehouseMapper.class );

    // MAPPING IMPLEMENTATION EXAMPLE
    //    @Mapping(source = "numberOfSeats", target = "seatCount")
    WarehouseDTO warehouseToWarehouseDTO(Warehouse car);
}