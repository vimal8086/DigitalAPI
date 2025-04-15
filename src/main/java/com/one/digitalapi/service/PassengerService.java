package com.one.digitalapi.service;

import com.one.digitalapi.entity.PassengerRef;
import java.util.List;

public interface PassengerService {
    PassengerRef savePassengerForUser(String userId, PassengerRef passenger);
    List<PassengerRef> getPassengersByUserId(String userId);
    PassengerRef updatePassenger(Integer passengerId, PassengerRef passenger);
    void deletePassenger(Integer passengerId);
    PassengerRef viewPassenger(int id);
}

