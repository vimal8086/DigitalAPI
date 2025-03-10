package com.one.digitalapi.repository;

import java.util.List;

import com.one.digitalapi.entity.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BusRepository extends JpaRepository<Bus, Integer> {

    @Query("select b from Bus b where b.routeFrom =?1 and b.routeTo = ?2")
    public List<Bus> getBusByRoute(String routeFrom, String routeTo);

    public List<Bus> findByBusType(String busType);


}
