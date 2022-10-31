package com.loldigital.orderapi.dto;

import java.util.Date;
import java.util.List;

import com.loldigital.orderapi.enums.OrderStatus;

public class OrderDTO {

	private Long orderId;		
	private Long customerId;	
    private Long storeId;   
    private OrderStatus orderStatus;
    private Date orderTime;
    private List<OrderDetailDTO> orderDetails;
    private Boolean isActive;
       
	public List<OrderDetailDTO> getOrderDetails() {
		return orderDetails;
	}
	public void setOrderDetails(List<OrderDetailDTO> orderDetails) {
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
	/**
	 * @return the isActive
	 */
	public Boolean getIsActive() {
		return isActive;
	}
	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
    
}
