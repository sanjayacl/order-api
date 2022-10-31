package com.loldigital.orderapi.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.loldigital.orderapi.dto.OrderDTO;
import com.loldigital.orderapi.dto.OrderDetailDTO;
import com.loldigital.orderapi.enums.OrderStatus;
import com.loldigital.orderapi.exceptions.LolDigitalServerSideException;
import com.loldigital.orderapi.exceptions.ResourceNotFoundException;
import com.loldigital.orderapi.exceptions.TenantNotSpecifiedException;
import com.loldigital.orderapi.model.Order;
import com.loldigital.orderapi.model.OrderDetail;
import com.loldigital.orderapi.repository.IOrderDetailRepository;
import com.loldigital.orderapi.service.IOrderService;

@RestController
@RequestMapping("/loldigital")
public class OrderController {

	private static Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	IOrderService orderService;
	
	@Autowired
	IOrderDetailRepository orderDetailRepo;

	@GetMapping("/hello")
	public String test() {
		return "Hello ALT Health User";
	}

	@PostMapping("/orders")
	public OrderDTO createOrder(@RequestParam String tenant, @RequestBody OrderDTO orderDTO) {
		try {
			Order order = convertToModel(orderDTO, null, tenant, true);
			order.setOrderStatus(OrderStatus.WAITING);
			order.setOrderTime(Calendar.getInstance().getTime());
			
			List<OrderDetail> orderDetails = new ArrayList<>();
			for (OrderDetailDTO orderDetailDTO : orderDTO.getOrderDetails()) {
				OrderDetail orderDetail = convertToModel(orderDetailDTO, null, order, tenant, true);
				orderDetails.add(orderDetail);
			}
			
			order.setOrderDetails(orderDetails);
			return convertToDTO(orderService.saveOrder(order));
		} catch (TenantNotSpecifiedException tenantNotSpecifiedException) {
			logger.error(TenantNotSpecifiedException.ERROR_MESSAGE);
			throw tenantNotSpecifiedException;
		}
	}

	@PutMapping("/orders/{orderId}")
	public OrderDTO updateOrder(@RequestParam String tenant, @PathVariable Long orderId, @RequestBody OrderDTO orderDTO)
			throws ResourceNotFoundException {

		try {
			return handleUpdate(tenant, orderId, orderDTO);
		} catch (TenantNotSpecifiedException tenantNotSpecifiedException) {
			logger.error(TenantNotSpecifiedException.ERROR_MESSAGE);
			throw tenantNotSpecifiedException;
		} catch (ResourceNotFoundException resourceNotFoundException) {
			logger.error(ResourceNotFoundException.ERROR_MESSAGE);
			throw resourceNotFoundException;
		} catch (Exception exception) {

			if (exception instanceof TenantNotSpecifiedException || exception instanceof ResourceNotFoundException) {
				throw exception;
			} else {
				logger.error(exception.getMessage());
				throw new LolDigitalServerSideException();
			}
		}

	}

	private OrderDTO handleUpdate(String tenant, Long orderId, OrderDTO orderDTO) throws ResourceNotFoundException {
		Order order;
		order = orderService.findOrderByOrderIdAndTenant(orderId, tenant);
		List<OrderDetail> orderDetailPrevList = order.getOrderDetails();
		List<Long> prevIds = new ArrayList<>();
		
		for(OrderDetail OrderDetail : orderDetailPrevList) {
			prevIds.add(OrderDetail.getOrderDetailId());
		}
		
		order = convertToModel(orderDTO, order, tenant, true);
		
		List<OrderDetailDTO> orderDetailDTOList = orderDTO.getOrderDetails();
		List<OrderDetail> orderDetailList = new ArrayList<>();
		List<Long> orderDetailIds = new ArrayList<>();
		order.getOrderDetails().clear();
		
		handleNewAndModifiedItems(tenant, order, orderDetailDTOList, orderDetailList, orderDetailIds);	
		handleRemovedItems(prevIds, orderDetailIds);
		
		order.getOrderDetails().addAll(orderDetailList);
		return convertToDTO(orderService.saveOrder(order));
	}

	private void handleNewAndModifiedItems(String tenant, Order order, List<OrderDetailDTO> orderDetailDTOList,
			List<OrderDetail> orderDetailList, List<Long> orderDetailIds) throws ResourceNotFoundException {
		if(orderDetailDTOList != null && !orderDetailDTOList.isEmpty()) {
			
			// handle the new order details and updated details
			for(OrderDetailDTO orderDetailDTO : orderDetailDTOList) {
				OrderDetail orderDetail = null;					
				Long orderDetailId = orderDetailDTO.getOrderDetailId();
				
				if(orderDetailId == null) {
					orderDetail = convertToModel(orderDetailDTO, null, order,tenant, true);
				}else {
					Optional<OrderDetail> orderDetailOpt = orderDetailRepo.findById(orderDetailId);
					orderDetailIds.add(orderDetailId);
					
					if(orderDetailOpt.isPresent()) {
						orderDetail = convertToModel(orderDetailDTO, orderDetailOpt.get(), order, tenant, true);
					}else {
						logger.error("Order Detail is not available for given id {}", orderDetailId);
						throw new ResourceNotFoundException();
					}
				}
				
				orderDetail.setOrder(order);
				orderDetailList.add(orderDetail);
			}				
		}
	}

	private void handleRemovedItems(List<Long> prevIds, List<Long> orderDetailIds) {
		for(Long orderDetailPrevId : prevIds) {
			// handle the removed order details
			
			if(! orderDetailIds.contains(orderDetailPrevId)) {
				orderDetailRepo.deleteById(orderDetailPrevId);
			}
		}
	}

	@GetMapping("/orders/{orderId}")
	public OrderDTO getOrder(@RequestParam String tenant, @PathVariable Long orderId) throws ResourceNotFoundException {
		try {
			Order order = orderService.findOrderByOrderIdAndTenant(orderId, tenant);
			return convertToDTO(order);
		} catch (TenantNotSpecifiedException tenantNotSpecifiedException) {
			logger.error(TenantNotSpecifiedException.ERROR_MESSAGE);
			throw tenantNotSpecifiedException;
		} catch (ResourceNotFoundException resourceNotFoundException) {
			logger.error(ResourceNotFoundException.ERROR_MESSAGE);
			throw resourceNotFoundException;
		} catch (Exception exception) {

			if (exception instanceof TenantNotSpecifiedException || exception instanceof ResourceNotFoundException) {
				throw exception;
			} else {
				logger.error(exception.getMessage());
				throw new LolDigitalServerSideException();
			}
		}

	}

	@DeleteMapping("/orders/{orderId}")
	public void inactivateOrder(@RequestParam String tenant, @PathVariable Long orderId) throws ResourceNotFoundException {
		try {
			Order order = orderService.findOrderByOrderIdAndTenant(orderId, tenant);
			order.setIsActive(false);
			orderService.saveOrder(order);
		} catch (TenantNotSpecifiedException tenantNotSpecifiedException) {
			logger.error(TenantNotSpecifiedException.ERROR_MESSAGE);
			throw tenantNotSpecifiedException;
		} catch (ResourceNotFoundException resourceNotFoundException) {
			logger.error(ResourceNotFoundException.ERROR_MESSAGE);
			throw resourceNotFoundException;
		}
	}

	@GetMapping("/orders")
	public List<OrderDTO> searchOrders(@RequestParam String tenant, @RequestParam(required = false) Long customerId,
			@RequestParam(required = false) Long storeId){
		try {
			List<Order> orders = orderService.searchOrders(tenant, customerId, storeId);
			return convertToDTOList(orders);
		} catch (TenantNotSpecifiedException tenantNotSpecifiedException) {
			logger.error(TenantNotSpecifiedException.ERROR_MESSAGE);
			throw tenantNotSpecifiedException;
		}
	}

	@GetMapping("/orders/getWaitingCustomers")
	public Integer findWaitingCustomerCount(@RequestParam String tenant, @RequestParam Long storeId){
		try {
			return orderService.findWaitingCustomerCount(tenant, storeId);
		} catch (TenantNotSpecifiedException tenantNotSpecifiedException) {
			logger.error(TenantNotSpecifiedException.ERROR_MESSAGE);
			throw tenantNotSpecifiedException;
		}

	}

	@GetMapping("/orders/getTheOrdersOfCustomersInTheQueue")
	public List<OrderDTO> getOrdersOfCustomersInQueue(@RequestParam String tenant, @RequestParam Long storeId,
			@RequestParam(required = false) Integer queueNumber){
		try {

			// If the queue number is not passed, we can assume that only one queue is there
			// in the store
			if (queueNumber == null) {
				queueNumber = 1;
			}

			List<Order> orders = orderService.getOrdersOfCustomersInQueue(tenant, storeId, queueNumber);
			return convertToDTOList(orders);
		} catch (TenantNotSpecifiedException tenantNotSpecifiedException) {
			logger.error(TenantNotSpecifiedException.ERROR_MESSAGE);
			throw tenantNotSpecifiedException;
		}
	}

	public OrderDTO convertToDTO(Order order) {
		return modelMapper.map(order, OrderDTO.class);
	}
	
	public OrderDetailDTO convertToDTO(OrderDetail orderDetail) {
		return modelMapper.map(orderDetail, OrderDetailDTO.class);
	}

	public List<OrderDTO> convertToDTOList(List<Order> orders) {
		return modelMapper.map(orders, new TypeToken<List<OrderDTO>>() {}.getType());
	}

	public Order convertToModel(OrderDTO orderDTO, Order order, String tenant, boolean isActive) {

		if (order == null) {
			order = modelMapper.map(orderDTO, Order.class);
		} else {
			modelMapper.map(orderDTO, order);
		}

		order.setTenant(tenant);
		order.setIsActive(isActive);

		return order;
	}
	
	public OrderDetail convertToModel(OrderDetailDTO orderDetailDTO, OrderDetail orderDetail, Order order, String tenant, boolean isActive) {
		
		if (orderDetail == null) {
			orderDetail = modelMapper.map(orderDetailDTO, OrderDetail.class);
		} else {
			modelMapper.map(orderDetailDTO, orderDetail);
		}

		orderDetail.setOrder(order);
		orderDetail.setTenant(tenant);
		orderDetail.setIsActive(isActive);

		return orderDetail;
	}
}
