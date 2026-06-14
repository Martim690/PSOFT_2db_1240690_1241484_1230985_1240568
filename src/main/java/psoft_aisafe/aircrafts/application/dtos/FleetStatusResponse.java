package psoft_aisafe.aircrafts.application.dtos;

import java.util.Map;

public record FleetStatusResponse(
        long totalAircraft,
        Map<String, Long> statusCounts
) {}