package com.one.digitalapi.service;

import com.one.digitalapi.entity.PassengerRef;
import com.one.digitalapi.exception.PassengerException;
import com.one.digitalapi.exception.RouteException;
import com.one.digitalapi.logger.DefaultLogger;
import com.one.digitalapi.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PassengerServiceImpl implements PassengerService {

    private static final String CLASSNAME = "PassengerServiceImpl";
    private static final DefaultLogger LOGGER = new DefaultLogger(PassengerServiceImpl.class);

    @Autowired
    private PassengerRepository passengerRepository;

    @Override
    public PassengerRef savePassengerForUser(String userId, PassengerRef passenger) {
        String methodName = "savePassengerForUser";
        LOGGER.infoLog(CLASSNAME, methodName, "Saving passenger for userId: " + userId);
        passenger.setUserId(userId); // Set userId directly to the entity
        PassengerRef savedPassenger = passengerRepository.save(passenger); // Save using default JPA save
        LOGGER.infoLog(CLASSNAME, methodName, "Passenger saved successfully: " + savedPassenger);
        return savedPassenger;
    }

    @Override
    public List<PassengerRef> getPassengersByUserId(String userId) {
        String methodName = "getPassengersByUserId";
        LOGGER.infoLog(CLASSNAME, methodName, "Fetching passengers for userId: " + userId);
        List<PassengerRef> passengers = passengerRepository.findByUserId(userId);
        LOGGER.infoLog(CLASSNAME, methodName, "Found " + passengers.size() + " passengers.");
        return passengers;
    }

    @Override
    public PassengerRef updatePassenger(Integer passengerId, PassengerRef passenger) {
        String methodName = "updatePassenger";
        LOGGER.infoLog(CLASSNAME, methodName, "Updating passenger with ID: " + passengerId);
        PassengerRef existing = passengerRepository.findById(passengerId)
                .orElseThrow(() -> {
                    LOGGER.errorLog(CLASSNAME, methodName, "Passenger not found with id: " + passengerId);
                    return new PassengerException("Passenger not found with id: " + passengerId);
                });

        // Update fields
        existing.setName(passenger.getName());
        existing.setEmail(passenger.getEmail());
        existing.setAge(passenger.getAge());
        existing.setGender(passenger.getGender());
        existing.setContact(passenger.getContact());

        PassengerRef updatedPassenger = passengerRepository.save(existing);
        LOGGER.infoLog(CLASSNAME, methodName, "Passenger updated successfully: " + updatedPassenger);
        return updatedPassenger;
    }

    @Override
    public void deletePassenger(Integer passengerId) {
        String methodName = "deletePassenger";
        LOGGER.infoLog(CLASSNAME, methodName, "Deleting passenger with ID: " + passengerId);
        if (!passengerRepository.existsById(passengerId)) {
            LOGGER.errorLog(CLASSNAME, methodName, "Passenger not found with id: " + passengerId);
            throw new PassengerException("Passenger not found with id: " + passengerId);
        }
        passengerRepository.deleteById(passengerId);
        LOGGER.infoLog(CLASSNAME, methodName, "Passenger deleted successfully.");
    }

    @Override
    public Optional<PassengerRef> viewPassenger(int id) {
        return passengerRepository.findById(id);
    }

}