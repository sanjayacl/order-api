package com.loldigital.orderapi.enums;

public enum OrderStatus {
	WAITING("Waiting"), IN_QUEUE("In Queue"), PREPARING("Preparing"), READY_TO_DELIVER("Ready To Deliver"),
	DELIVERED("Delivered");

	private final String description;

	private OrderStatus(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}
}
