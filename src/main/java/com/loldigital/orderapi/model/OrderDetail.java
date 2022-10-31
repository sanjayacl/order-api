package com.loldigital.orderapi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.loldigital.orderapi.audit.Auditable;

@Entity
public class OrderDetail extends Auditable<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderDetailId;
		
	private Long productId;	
    private double quantity;
    private String notes;
    private String tenant;
    private Boolean isActive;
    
	@ManyToOne
	@JoinColumn(name = "orderId", referencedColumnName = "orderId")
    private Order order;
			
	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	public Long getOrderDetailId() {
		return orderDetailId;
	}

	public Long getProductId() {
		return productId;
	}

	public double getQuantity() {
		return quantity;
	}

	public String getNotes() {
		return notes;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrderDetailId(Long orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
}
