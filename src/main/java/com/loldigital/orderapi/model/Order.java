package com.loldigital.orderapi.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.loldigital.orderapi.audit.Auditable;
import com.loldigital.orderapi.enums.OrderStatus;

@Entity
@Table(name="orders")
public class Order extends Auditable<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;
		
	private Long customerId;	
    private Long storeId;
    private Boolean isActive;
    private String tenant;
    private Integer queueNumber;
    
	@Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

	@Temporal(TemporalType.TIMESTAMP)
    private Date orderTime;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderDetail> orderDetails = new ArrayList<>();
		
	public Integer getQueueNumber() {
		return queueNumber;
	}

	public void setQueueNumber(Integer queueNumber) {
		this.queueNumber = queueNumber;
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public List<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public Long getOrderId() {
		return orderId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public Long getStoreId() {
		return storeId;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	
}
