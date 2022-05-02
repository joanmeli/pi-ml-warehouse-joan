package br.com.group9.pimlwarehouse.util.batch_stock_order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderBatchStockEnum {
    L(new OrderByBatchNumber()),  // Ordenar por número de lote (crescente)
    C(new OrderByCurrentQuantity()),  // Ordenar por quantidade atual (menor para o maior)
    F(new OrderByDueDate()),   // Ordenar por data de validade (mais antigo para o mais novo)
    DEFAULT(new DefaultOrder());

    IOrderBy orderByStrategy;
}
