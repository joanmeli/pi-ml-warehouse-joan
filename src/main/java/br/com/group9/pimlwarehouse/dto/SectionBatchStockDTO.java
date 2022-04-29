package br.com.group9.pimlwarehouse.dto;

import br.com.group9.pimlwarehouse.entity.BatchStock;
import br.com.group9.pimlwarehouse.entity.InboundOrder;
import br.com.group9.pimlwarehouse.entity.Section;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
public class SectionBatchStockDTO {

    private Long warehouseCode;

    private Long sectionCode;

    private Long productId;

    @JsonIgnoreProperties({"productId", "manufacturingDateTime", "initialQuantity"})
    private List<BatchStockDTO> batchStock;


    public static List<SectionBatchStockDTO> map(Map<Long, List<BatchStock>> batchStocksByProductId) {
        return batchStocksByProductId.entrySet().stream()
                .map(e -> {
                    Stream<Map.Entry<InboundOrder, BatchStock>> entryStream1 = e.getValue().stream()
                            .map(b -> Map.entry(b.getInboundOrder(), b));
                    Map<Section, List<BatchStock>> collect = entryStream1.collect(Collectors.toMap(a -> a.getKey().getSection(),
                                    b -> Arrays.asList(b.getValue()),
                                    (b, c) -> Stream.concat(b.stream(), c.stream()).collect(Collectors.toList()))
                            );
                    return Map.entry(e.getKey(), collect);
            })
            .map(c -> c.getValue().entrySet().stream()
                .map(d -> SectionBatchStockDTO.builder()
                        .warehouseCode(d.getKey().getWarehouse().getId())
                        .sectionCode(d.getKey().getId())
                        .productId(c.getKey())
                        .batchStock(BatchStockDTO.convert(d.getValue()))
                        .build()
                ).collect(Collectors.toList())
            )
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }
}
