package com.loldigital.orderapi.repository;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.loldigital.orderapi.enums.OrderStatus;
import com.loldigital.orderapi.model.Order;

@RunWith(SpringRunner.class)
@DataJpaTest
class OrderRepositoryTest {

	@Autowired
	private IOrderRepository orderRepository;

	Order order = new Order();
	Order order1 = new Order();
	
	@BeforeEach
	public void setUp() {
		
		order.setCustomerId(1l);
		order.setStoreId(2l);
		order.setTenant("cbtl");
		order.setOrderStatus(OrderStatus.IN_QUEUE);
		
		order1.setCustomerId(2l);
		order1.setStoreId(3l);
		order1.setTenant("cbtl");
		order1.setOrderStatus(OrderStatus.WAITING);
		
		// Insert order to db
		order = orderRepository.save(order);
		order1 = orderRepository.save(order1);
	}
	
	
	@Test
	void findByOrderIdAndTenantTest() {


        Long orderId = order.getOrderId();
        String tenant = order.getTenant();
		// fetching the order with given order id and tenant
        Optional<Order> ordersOpt = orderRepository.findByOrderIdAndTenant(orderId, tenant);

		// validating the results
        assertTrue(ordersOpt.isPresent());
        
        Order resultedOrder = ordersOpt.get();
		assertEquals(orderId, resultedOrder.getOrderId());
		assertEquals(tenant, resultedOrder.getTenant());
	}
	
	@Test
	void findByCustomerIdAndTenantTest() {

        Long orderId = order.getOrderId();
        String tenant = order.getTenant();
		// fetching the order with given order id and tenant
        Optional<Order> ordersOpt = orderRepository.findByOrderIdAndTenant(orderId, tenant);

		// validating the results
        assertTrue(ordersOpt.isPresent());
        
        Order resultedOrder = ordersOpt.get();
		assertEquals(orderId, resultedOrder.getOrderId());
		assertEquals(tenant, resultedOrder.getTenant());
	}
	
}
