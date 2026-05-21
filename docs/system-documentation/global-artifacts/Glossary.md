# AlSafe - Glossary
# AISafe - Glossary

| Concept | DDD Stereotype | Description |
|---------|----------------|-------------|
| **ActualArrival** | Value Object | Actual arrival date and time of a flight, recorded once the aircraft lands. May differ from ScheduledArrival. |
| **ActualDeparture** | Value Object | Actual departure date and time of a flight, recorded once the aircraft takes off. May differ from ScheduledDeparture. |
| **Aircraft** | Aggregate Root / Entity | Physical aircraft belonging to the fleet. Records the specific registration number, manufacturing date, actual seating capacity, accumulated flight hours, and operational status. |
| **AircraftAirportCertification** | Aggregate Root / Entity | Certification that authorises a specific AircraftModel to operate at (fly to/from) a specific Airport. Consolidates what was previously split between AircraftCertification (on AircraftModel) and AirportAircraftCertification (on Airport) into a single concept. Includes a certification code, description, validity period (certifiedFrom, certifiedUntil), and optional operational notes. |
| **AircraftAvailability** | Domain Concept | Derived concept representing whether an aircraft can be assigned to a flight, based on its current AircraftStatus and active MaintenanceRecord entries. |
| **AircraftModel** | Entity | Factory technical specifications of an aircraft type. Includes model name, fuel capacity, maximum range, cruising speed, optional technical diagram/image, and manufacturer. Does not include final seating configuration, which is set per Aircraft instance. |
| **AircraftStatus** | Enum | Operational state of an aircraft at a given moment. Values: AVAILABLE, IN_FLIGHT, UNDER_MAINTENANCE, INACTIVE. |
| **Airport** | Aggregate Root / Entity | Airport infrastructure. Records name, city, country, region, timezone, operational hours, optional photos, contact information, runway data, and facility details. |
| **AirportStatus** | Enum | Current operational state of an airport. Values: OPERATIONAL, CLOSED, UNDER_MAINTENANCE. |
| **Checklist** | Value Object | Ordered list of verification tasks embedded within a MaintenanceTemplate. Has no identity of its own; its meaning is derived from its content. |
| **ComponentType** | Enum | Categorises the aircraft component targeted by a MaintenanceRecord. Values: ENGINE, AIRFRAME, AVIONICS, INTERIOR, EXTERIOR. |
| **ContactInfo** | Value Object | A single contact detail for an Airport, including the ContactType channel, the contact value (e.g., phone number or email address), and an optional department or description. |
| **ContactType** | Enum | Communication channel for a ContactInfo entry. Values: PHONE, EMAIL, FAX, RADIO. |
| **Coordinates** | Value Object | Exact geographical position of an Airport expressed as latitude and longitude. Has no identity beyond its values. |
| **FacilityInfo** | Value Object | Groups information about passenger infrastructure within an Airport: number of terminals, number of gates, and a list of available services. |
| **Fleet** | Domain Concept | The complete collection of all physical Aircraft managed and operated by the Air Transport Company. |
| **FlightAssignment** | Domain Concept | The act of assigning an Aircraft to a Route at a specific date and time, materialised as a ScheduledFlight. Subject to compatibility constraints (range, capacity, certification, availability). |
| **FlightNumber** | Value Object | Unique alphanumeric identifier for a ScheduledFlight (e.g., TP101). Has no identity beyond its value. |
| **FlightStatus** | Enum | Lifecycle states of a ScheduledFlight. Values: SCHEDULED, DELAYED, CANCELED, IN_FLIGHT, COMPLETED. |
| **IATACode** | Value Object | Three-letter IATA code that uniquely identifies an Airport (e.g., OPO for Porto). Validated against the standard format. |
| **MaintenanceActivity** | Domain Concept | General abstraction grouping maintenance templates and records within the maintenance lifecycle. |
| **MaintenanceRecord** | Aggregate Root / Entity | Historical and financial record of maintenance performed on an Aircraft. Includes description, start date, expected duration, completion date, completion notes, cost, ComponentType, and MaintenanceStatus. |
| **MaintenanceStatus** | Enum | Lifecycle state of a maintenance operation. Values: SCHEDULED, IN_PROGRESS, WAITING_PARTS, COMPLETED, CANCELED. |
| **MaintenanceTemplate** | Entity | Reusable blueprint defining a type of maintenance activity. Contains a template name, TemplateType, Checklist, and a list of applicable AircraftModel entries. Used as the basis for creating MaintenanceRecord instances. |
| **Manufacturer** | Entity | Company that manufactures aircraft models (e.g., Boeing, Airbus). Pre-loaded during system bootstrapping. Referenced by one or more AircraftModel entries. |
| **Network** | Domain Concept | The subset of all active Routes in the system (isActive = true). Represents the operational route map of the Air Transport Company. |
| **Password** | Value Object | Encapsulates the hashed credential of a User. Treated as a Value Object to enforce proper encapsulation and to prevent direct exposure of security-sensitive data. |
| **RegistrationNumber** | Value Object | Unique alphanumeric identifier of a physical Aircraft instance, formatted according to aviation standards (e.g., CS-TUA). Used as the natural key to look up a specific aircraft. |
| **Role** | Enum | Authorisation role that defines the access level of a User. Values: ADMIN, BACKOFFICE_OPERATOR, ATCC, MAINTENANCE_TECHNICIAN, MAINTENANCE_SUPERVISOR. |
| **Route** | Aggregate Root / Entity | Planned flight path linking an origin Airport to a destination Airport. Has a unique RouteID, a set of RouteRequirements, a history of RouteVersion entries, and an active flag. |
| **RouteHistory** | Domain Concept | The collection of all RouteVersion instances associated with a Route, enabling full traceability of changes over time. |
| **RouteID** | Value Object | Unique business identifier for a specific Route. Has no identity beyond its value. |
| **RouteRequirements** | Value Object | Minimum technical demands (range, capacity, optional required certification code) that an Aircraft must meet to safely operate a Route. |
| **RouteVersion** | Entity | Historical snapshot of a Route's operational parameters at a point in time: distance, estimated flight time, validity period (validFrom, validUntil), and change reason. |
| **RunwayInfo** | Value Object | Structural data of a single runway at an Airport: name, length (metres), and orientation (e.g., magnetic heading). An Airport may have multiple RunwayInfo entries. |
| **ScheduledArrival** | Value Object | Planned arrival date and time of a ScheduledFlight. |
| **ScheduledDeparture** | Value Object | Planned departure date and time of a ScheduledFlight. |
| **ScheduledFlight** | Aggregate Root / Entity | The temporal and physical allocation of an Aircraft to a Route. Records scheduled and actual departure/arrival times, a FlightNumber, and a FlightStatus. Subject to range, capacity, certification, and availability constraints. |
| **TemplateType** | Enum | Category of a MaintenanceTemplate. Values: INSPECTION, SCHEDULED_MAINTENANCE, OVERHAUL, MODIFICATION. |
| **User** | Aggregate Root / Entity | Person with access to the AISafe system. Has a username, a hashed Password, and one or more Roles that determine their permissions. Authentication is handled via JWT tokens. |

---
*Total concepts: 40 | Stereotypes used: Aggregate Root / Entity, Entity, Value Object, Enum, Domain Concept*
 





