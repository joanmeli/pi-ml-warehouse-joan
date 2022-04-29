package br.com.group9.pimlwarehouse.util.batch_stock_order;

import java.util.List;

public interface IOrderBy <T>{
    List<T> apply(List<T> t);
}
