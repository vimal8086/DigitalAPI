package com.one.digitalapi.service;
import com.one.digitalapi.entity.Bus;
import java.util.List;

public interface BusService {

    public Bus addBus(Bus bus);
    public Bus updateBus(Long busId, Bus busDetails);
    public Bus deleteBus(int busId);
    public Bus viewBus(int busId);
    public List<Bus> viewBusByType(String BusType);
    public List<Bus> viewAllBuses();
    List<Bus> searchBuses(String from, String to, String departureTime);
}
