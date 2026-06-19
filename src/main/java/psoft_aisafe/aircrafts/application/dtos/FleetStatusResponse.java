package psoft_aisafe.aircrafts.application.dtos;

import java.util.List;
import java.util.Map;

public record FleetStatusResponse(
        Map<String, Long> statusCounts,
        List<AircraftStatusDetail> aircrafts
) {
    // Este sub-record é o que garante que a tua lista fica com a estrutura que pediste!
    public record AircraftStatusDetail(String registrationNumber, String status) {}
}