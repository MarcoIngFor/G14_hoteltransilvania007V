package com.hoteltransilvania.checkincheckout.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "checkin_checkout")
public class CheckinCheckout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El clienteId es obligatorio")
    @Column(nullable = false)
    private Long clienteId;

    @NotNull(message = "La reservaId es obligatoria")
    @Column(nullable = false)
    private Long reservaId;

    @NotNull(message = "La habitacionId es obligatoria")
    @Column(nullable = false)
    private Long habitacionId;

    private LocalDateTime fechaCheckin;

    private LocalDateTime fechaCheckout;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCheckinCheckout estado;

    public enum EstadoCheckinCheckout {
        CHECKIN_REALIZADO,
        CHECKOUT_REALIZADO
    }

    // GETTERS Y SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getReservaId() {
        return reservaId;
    }

    public void setReservaId(Long reservaId) {
        this.reservaId = reservaId;
    }

    public Long getHabitacionId() {
        return habitacionId;
    }

    public void setHabitacionId(Long habitacionId) {
        this.habitacionId = habitacionId;
    }

    public LocalDateTime getFechaCheckin() {
        return fechaCheckin;
    }

    public void setFechaCheckin(LocalDateTime fechaCheckin) {
        this.fechaCheckin = fechaCheckin;
    }

    public LocalDateTime getFechaCheckout() {
        return fechaCheckout;
    }

    public void setFechaCheckout(LocalDateTime fechaCheckout) {
        this.fechaCheckout = fechaCheckout;
    }

    public EstadoCheckinCheckout getEstado() {
        return estado;
    }

    public void setEstado(EstadoCheckinCheckout estado) {
        this.estado = estado;
    }
}