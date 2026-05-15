package com.hoteltransilvania.checkincheckout.repository;

import com.hoteltransilvania.checkincheckout.entity.CheckinCheckout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckinCheckoutRepository extends JpaRepository<CheckinCheckout, Long> {

    List<CheckinCheckout> findByClienteId(Long clienteId);

    List<CheckinCheckout> findByHabitacionId(Long habitacionId);
}