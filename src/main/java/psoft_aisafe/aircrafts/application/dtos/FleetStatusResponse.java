package psoft_aisafe.aircrafts.application.dtos;

import java.util.List;
import java.util.Map;

public record FleetStatusResponse(
        Map<String, Long> statusCounts,
        List<AircraftStatusDetail> aircrafts
) {
    public record AircraftStatusDetail(String registrationNumber, String status) {}
}