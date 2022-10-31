package com.loldigital.orderapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loldigital.orderapi.model.OrderDetail;

@Repository
public interface IOrderDetailRepository extends JpaRepository<OrderDetail, Long> {

}
