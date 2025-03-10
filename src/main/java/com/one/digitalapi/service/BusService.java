package com.one.digitalapi.service;

import com.one.digitalapi.entity.Bus;
import com.one.digitalapi.exception.BusException;
import com.one.digitalapi.exception.LoginException;

import java.util.List;

public interface BusService {

    public Bus addBus(Bus bus);
    public Bus updateBus(Bus bus);
    public Bus deleteBus(int busId);
    public Bus viewBus(int busId);
    public List<Bus> viewBusByType(String BusType);
    public List<Bus> viewAllBuses();
}
