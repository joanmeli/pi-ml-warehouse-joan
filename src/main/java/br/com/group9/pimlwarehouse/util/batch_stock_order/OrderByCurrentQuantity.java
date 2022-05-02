package br.com.group9.pimlwarehouse.util.batch_stock_order;

import br.com.group9.pimlwarehouse.entity.BatchStock;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class OrderByCurrentQuantity implements IOrderBy<BatchStock> {

    public List<BatchStock> apply(List<BatchStock> batchStocks) {
        return batchStocks.stream()
                .sorted(Comparator.comparing(BatchStock::getCurrentQuantity))
                .collect(Collectors.toList());
    }
}
