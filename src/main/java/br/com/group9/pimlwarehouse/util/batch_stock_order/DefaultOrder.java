package br.com.group9.pimlwarehouse.util.batch_stock_order;

import br.com.group9.pimlwarehouse.entity.BatchStock;

import java.util.List;

public class DefaultOrder implements IOrderBy<BatchStock> {
    @Override
    public List<BatchStock> apply(List<BatchStock> batchStocks) {
        return batchStocks;
    }
}
