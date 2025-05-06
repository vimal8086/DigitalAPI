package com.one.digitalapi.repository;

import java.util.List;

import com.one.digitalapi.entity.Bus;
import com.one.digitalapi.validation.DefaultValidation;
import com.one.digitalapi.validation.FullValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BusRepository extends JpaRepository<Bus, Integer> {

    @Query("select b from Bus b where b.routeFrom =?1 and b.routeTo = ?2")
    public List<Bus> getBusByRoute(String routeFrom, String routeTo);

    public List<Bus> findByBusType(String busType);

    // Find buses by route (from and to)
    List<Bus> findByRouteFromAndRouteToAndIsActiveTrue(String routeFrom, String routeTo);

    // Find buses by route and departure time greater than or equal to a given time
    List<Bus> findByRouteFromAndRouteToAndDepartureTimeAfterAndIsActiveTrue(@NotNull(message = "Route From can not be null.",
            groups = FullValidation.class) @NotBlank(message = "Route From can not be blank.", groups = FullValidation.class)

                                                              @NotEmpty(message = "Route From can not be empty.", groups = FullValidation.class) String routeFrom, @NotNull(message = "Route To can not be null.", groups = FullValidation.class) @NotBlank(message = "Route To can not be blank.", groups = FullValidation.class) @NotEmpty(message = "Route To can not be empty.", groups = FullValidation.class) String routeTo, @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$", message = "Invalid time format. Expected HH:mm:ss") @NotNull(message = "departure time can not be null.", groups = FullValidation.class) @NotBlank(message = "departure time can not be blank.", groups = DefaultValidation.class) @NotEmpty(message = "departure time can not be empty.", groups = DefaultValidation.class) String departureTime);

}
