package com.loldigital.orderapi.controller;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loldigital.orderapi.dto.OrderDTO;
import com.loldigital.orderapi.exceptions.LolDigitalServerSideException;
import com.loldigital.orderapi.exceptions.TenantNotSpecifiedException;
import com.loldigital.orderapi.model.Order;
import com.loldigital.orderapi.repository.IOrderDetailRepository;
import com.loldigital.orderapi.security.AuthEntryPointJwt;
import com.loldigital.orderapi.service.IOrderService;

@RunWith(SpringRunner.class)
@WithMockUser
@WebMvcTest(controllers = OrderController.class)
@AutoConfigureMockMvc
class OrderControllerTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ModelMapper modelMapper;
	
	@MockBean
	private IOrderService orderService;
	
	@MockBean
	private IOrderDetailRepository orderDetailRepository;
	
	@MockBean
	private AuthEntryPointJwt authEntryPointJwt;
	
	public String tenant = "cbtl";
	
	ObjectMapper mapper = new ObjectMapper();
	OrderDTO orderDTO = new OrderDTO();
	
	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@WithMockUser
	void createOrderTest() throws Exception {
		Order order = new Order();
		orderDTO.setOrderDetails(Collections.EMPTY_LIST);
		when(orderService.saveOrder(isA(Order.class))).thenThrow(new TenantNotSpecifiedException());
		when(modelMapper.map(isA(OrderDTO.class), isA(Class.class))).thenReturn(order);

		mockMvc.perform(post("/loldigital/orders").param("tenant", tenant).accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(orderDTO)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());


		orderDTO.setOrderId(1l);

		when(orderService.saveOrder(isA(Order.class))).thenReturn(order);
		when(modelMapper.map(order, OrderDTO.class)).thenReturn(orderDTO);
		mockMvc.perform(post("/loldigital/orders").param("tenant", tenant).accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(orderDTO)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.orderId").value(1));
		
	}
		
}
