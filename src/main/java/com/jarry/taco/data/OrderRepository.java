package com.jarry.taco.data;

import com.jarry.taco.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {

}
