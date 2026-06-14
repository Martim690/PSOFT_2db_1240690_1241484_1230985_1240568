package psoft_aisafe.aircrafts.application.dtos;

public record TopUtilizedModelResponse(
        String modelName,
        String manufacturer,
        int totalFlightHours,
        int totalAssignments
) {}