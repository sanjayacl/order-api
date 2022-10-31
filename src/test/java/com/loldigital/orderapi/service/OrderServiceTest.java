package com.loldigital.orderapi.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.loldigital.orderapi.exceptions.TenantNotSpecifiedException;
import com.loldigital.orderapi.model.Order;
import com.loldigital.orderapi.repository.IOrderRepository;
import com.loldigital.orderapi.service.impl.OrderServiceImpl;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@Mock
	IOrderRepository orderRepo;
	
	@InjectMocks
    IOrderService orderService = new OrderServiceImpl();
	
	AutoCloseable closeable;
	
	@BeforeEach
    void init() {
    	closeable = MockitoAnnotations.openMocks(this);
    }
    
    @AfterEach
    void clear() throws Exception{
		closeable.close();
    }
	
	@Test
	void saveOrderTest() throws Exception{
		Order order = new Order();
        assertThrows(TenantNotSpecifiedException.class, () -> orderService.saveOrder(order));

        order.setTenant("cbtl");        
        Mockito.when(orderRepo.save(Mockito.isA(Order.class))).thenReturn(order);
        
        orderService.saveOrder(order);        
        Mockito.verify(orderRepo).save(order); 
	}
	
}
