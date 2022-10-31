package com.loldigital.orderapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.loldigital.orderapi.enums.OrderStatus;
import com.loldigital.orderapi.exceptions.TenantNotSpecifiedException;
import com.loldigital.orderapi.model.Order;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {
	Optional<Order> findByOrderIdAndTenant(Long orderId, String tenant);

	List<Order> findByCustomerIdAndTenant(Long customerId, String tenant);

	List<Order> findByStoreIdAndTenant(Long storeId, String tenant);

	List<Order> findByStoreIdAndCustomerIdAndTenant(Long storeId, Long customerId, String tenant);

	@Query(value = "SELECT count(*) FROM orders WHERE tenant = ?1 AND store_id = ?2 AND order_status = 'WAITING' ", nativeQuery = true)
	Integer findWaitingCustomerCount(String tenant, Long storeId) throws TenantNotSpecifiedException;

	List<Order> findByStoreIdAndTenantAndOrderStatusAndQueueNumber(Long storeId, String tenant, OrderStatus orderStatus,
			Integer queueNumber);

}
