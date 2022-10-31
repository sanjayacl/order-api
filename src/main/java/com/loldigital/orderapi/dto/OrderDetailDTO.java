package com.loldigital.orderapi.dto;

public class OrderDetailDTO{

	private Long orderDetailId;		
	private Long productId;	
    private double quantity;
    private String notes;
    
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
	
}
