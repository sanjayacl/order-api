package com.loldigital.orderapi.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loldigital.orderapi.dto.OrderDTO;
import com.loldigital.orderapi.dto.OrderDetailDTO;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderControllerIT {

	@Autowired
	private MockMvc mockMvc;

	OrderDTO orderDTO = new OrderDTO();

	ObjectMapper mapper = new ObjectMapper();

	@BeforeEach
	public void init() {

		orderDTO.setCustomerId(1l);
		orderDTO.setStoreId(2l);
		
		List<OrderDetailDTO> orderDetailsDTOs = new ArrayList<OrderDetailDTO>();
		OrderDetailDTO orderDetail1 = new OrderDetailDTO();
		orderDetail1.setProductId(34l);
		orderDetail1.setQuantity(2);
		OrderDetailDTO orderDetail2 = new OrderDetailDTO();
		orderDetail2.setProductId(44l);
		orderDetail2.setQuantity(4);
		
		orderDetailsDTOs.add(orderDetail1);
		orderDetailsDTOs.add(orderDetail2);

		orderDTO.setOrderDetails(orderDetailsDTOs);
	}

	@Test
	void createOrderTest() throws Exception {

		// Converting the Object to JSONString
		String jsonStringForOrder = mapper.writeValueAsString(orderDTO);

		// Testing for missing tenant request
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/loldigital/orders")
				.content(jsonStringForOrder).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

		// Testing for successful request
		requestBuilder = MockMvcRequestBuilders.post("/loldigital/orders").param("tenant", "cbtl")
				.content(jsonStringForOrder).contentType(MediaType.APPLICATION_JSON);

		result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
	}

	
}
