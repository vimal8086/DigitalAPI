package com.one.digitalapi.service;

import com.one.digitalapi.entity.PassengerRef;
import java.util.List;
import java.util.Optional;

public interface PassengerService {
    PassengerRef savePassengerForUser(String userId, PassengerRef passenger);
    List<PassengerRef> getPassengersByUserId(String userId);
    PassengerRef updatePassenger(Integer passengerId, PassengerRef passenger);
    void deletePassenger(Integer passengerId);
    Optional<PassengerRef> viewPassenger(int id);
}


