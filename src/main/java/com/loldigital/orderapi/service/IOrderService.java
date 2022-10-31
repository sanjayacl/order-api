package com.loldigital.orderapi.service;

import java.util.List;

import com.loldigital.orderapi.exceptions.ResourceNotFoundException;
import com.loldigital.orderapi.exceptions.TenantNotSpecifiedException;
import com.loldigital.orderapi.model.Order;

public interface IOrderService {
	Order saveOrder(Order order) throws TenantNotSpecifiedException;

	Order findOrderByOrderIdAndTenant(Long orderId, String tenant)
			throws ResourceNotFoundException, TenantNotSpecifiedException;

	List<Order> searchOrders(String tenant, Long custoemrId, Long storeId) throws TenantNotSpecifiedException;

	Integer findWaitingCustomerCount(String tenant, Long storeId) throws TenantNotSpecifiedException;

	List<Order> getOrdersOfCustomersInQueue(String tenant, Long storeId, Integer queueNumber)
			throws TenantNotSpecifiedException;
}
