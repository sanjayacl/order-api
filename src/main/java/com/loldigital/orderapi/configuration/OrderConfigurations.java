package com.loldigital.orderapi.configuration;

import java.util.Locale;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import com.loldigital.orderapi.dto.OrderDTO;
import com.loldigital.orderapi.dto.OrderDetailDTO;
import com.loldigital.orderapi.model.Order;
import com.loldigital.orderapi.model.OrderDetail;

@Configuration
public class OrderConfigurations {

	@Bean
	public ModelMapper modelMapper() {
		PropertyMap<OrderDetailDTO, OrderDetail> oderDetailMap = new PropertyMap<OrderDetailDTO, OrderDetail>() {
		    @Override
		    protected void configure() {
		        skip(destination.getOrder());
		    }
		};
		
		PropertyMap<OrderDTO, Order> oderMap = new PropertyMap<OrderDTO, Order>() {
		    @Override
		    protected void configure() {
		        skip(destination.getOrderDetails());
		    }
		};
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(oderDetailMap);
		modelMapper.addMappings(oderMap);
	    return modelMapper;
	}
	
	// configuring ResourceBundle
	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages");
		return messageSource;
	}
	
    @Bean
    public AcceptHeaderLocaleResolver localeResolver() {
    	AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setDefaultLocale(Locale.US);
        return resolver;
    }
    	
}
