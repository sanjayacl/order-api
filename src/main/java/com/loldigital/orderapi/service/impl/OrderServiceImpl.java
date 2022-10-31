package com.loldigital.orderapi.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loldigital.orderapi.enums.OrderStatus;
import com.loldigital.orderapi.exceptions.ResourceNotFoundException;
import com.loldigital.orderapi.exceptions.TenantNotSpecifiedException;
import com.loldigital.orderapi.model.Order;
import com.loldigital.orderapi.repository.IOrderRepository;
import com.loldigital.orderapi.service.IOrderService;

@Service
public class OrderServiceImpl implements IOrderService {

	private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	IOrderRepository orderRepo;

	@Override
	@Transactional
	public Order saveOrder(Order order) throws TenantNotSpecifiedException{
		
		String tenant = order.getTenant();
		
		if (tenant == null || tenant.isEmpty()) {
			throw new TenantNotSpecifiedException();
		}
		
		// Invoke Queue service here to add the order as a queue entry and if that is success set the 
		// order status as IN_QUEUE, If Queue is Full then order status will be WAITING
		return orderRepo.save(order);
	}

	@Override
	@Transactional
	public Order findOrderByOrderIdAndTenant(Long orderId, String tenant)
			throws ResourceNotFoundException, TenantNotSpecifiedException {

		if (tenant == null || tenant.isEmpty()) {
			throw new TenantNotSpecifiedException();
		}

		Optional<Order> orderOpt = orderRepo.findByOrderIdAndTenant(orderId, tenant);

		if (orderOpt.isEmpty()) {
			logger.debug("Order not found by given order id {} and tenant {}", orderId, tenant);
			throw new ResourceNotFoundException();
		}

		return orderOpt.get();
	}
	
	@Override
	@Transactional
	public List<Order> searchOrders(String tenant,Long customerId,Long storeId)
			throws TenantNotSpecifiedException {

		if (tenant == null || tenant.isEmpty()) {
			throw new TenantNotSpecifiedException();
		}
		
		if(customerId != null && storeId != null) {
			return orderRepo.findByStoreIdAndCustomerIdAndTenant(storeId, customerId, tenant);
		}else if(customerId!= null) {
			return orderRepo.findByCustomerIdAndTenant(customerId, tenant);
		}else if(storeId != null) {
			return orderRepo.findByStoreIdAndTenant(storeId, tenant);
		}


        return Collections.emptyList();
	}
	
	@Override
	@Transactional
	public List<Order> getOrdersOfCustomersInQueue(String tenant, Long storeId, Integer queueNumber)
			throws TenantNotSpecifiedException {

		if (tenant == null || tenant.isEmpty()) {
			throw new TenantNotSpecifiedException();
		}

		return orderRepo.findByStoreIdAndTenantAndOrderStatusAndQueueNumber(storeId, tenant, OrderStatus.IN_QUEUE,
				queueNumber);
	}
	
	@Override
	@Transactional
	public Integer findWaitingCustomerCount(String tenant,Long storeId)
			throws TenantNotSpecifiedException {

		if (tenant == null || tenant.isEmpty()) {
			throw new TenantNotSpecifiedException();
		}
		
		return orderRepo.findWaitingCustomerCount(tenant, storeId);

	}

}
