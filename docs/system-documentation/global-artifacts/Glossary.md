# AISafe - Glossary

| Concept | DDD Stereotype | Description |
|---------|---------------|-------------|
| **Aircraft** | Aggregate Root / Entity | Physical aircraft belonging to the fleet. Records the specific registration number, manufacturing date, actual seating capacity, total flight hours, and operational status. |
| **AircraftModel** | Entity | Factory technical specifications of an aircraft type. Includes model name, fuel capacity, maximum range, cruising speed, optional technical diagram/image, and manufacturer. Does not include final seating configuration, which is set per Aircraft instance. |
| **AircraftStatus** | Enum | Operational state of an aircraft at a given moment. Values: AVAILABLE, IN_FLIGHT, UNDER_MAINTENANCE, INACTIVE. |
| **Airport** | Aggregate Root / Entity | Airport infrastructure. Records name, city, country, region, timezone, operational hours, optional photos, contact information, runway data, and facility details. |
| **AirportAircraftCertification** | Entity | Certification that authorises a specific AircraftModel to operate at (fly to/from) a specific Airport. Includes a certification code, description, validity period (certifiedFrom, certifiedUntil), and optional operational notes. |
| **AirportStatus** | Enum | Current operational state of an airport. Values: OPERATIONAL, CLOSED, UNDER_MAINTENANCE. |
| **Checklist** | Value Object | Ordered list of verification tasks embedded within a MaintenanceTemplate. Its meaning is derived from its content and has no identity of its own. |
| **ComponentType** | Enum | Categorises the aircraft component targeted by a MaintenanceRecord. Values: ENGINE, AIRFRAME, AVIONICS, INTERIOR, EXTERIOR. |
| **ContactInfo** | Value Object | A single contact detail for an Airport, including the ContactType channel, the contact value (e.g., phone number or email address), and an optional department or description. |
| **ContactType** | Enum | Communication channel for a ContactInfo entry. Values: PHONE, EMAIL, FAX, RADIO. |
| **Coordinates** | Value Object | Exact geographical position of an Airport expressed as latitude and longitude. Has no identity beyond its values. |
| **FacilityInfo** | Value Object | Groups information about passenger infrastructure within an Airport: number of terminals, number of gates, and a list of available services. |
| **FlightNumber** | Value Object | Unique alphanumeric identifier assigned to a specific ScheduledFlight (e.g., TP101). Has no identity beyond its value. |
| **FlightStatus** | Enum | Lifecycle states of a ScheduledFlight. Values: SCHEDULED, DELAYED, CANCELED, IN_FLIGHT, COMPLETED. |
| **IATACode** | Value Object | Three-letter IATA code that uniquely identifies an Airport (e.g., OPO for Porto). Validated against the standard format. |
| **MaintenanceRecord** | Aggregate Root / Entity | Historical and financial record of maintenance performed on an Aircraft. Includes description, start date, expected duration, completion date, completion notes, cost, ComponentType, and MaintenanceStatus. Based on a MaintenanceTemplate. |
| **MaintenanceStatus** | Enum | Lifecycle state of a maintenance operation. Values: SCHEDULED, IN_PROGRESS, WAITING_PARTS, COMPLETED, CANCELED. |
| **MaintenanceTemplate** | Entity | Reusable blueprint defining a type of maintenance activity. Contains a template name, TemplateType, Checklist, and a list of applicable AircraftModel entries. Used as the basis for creating MaintenanceRecord instances. |
| **Manufacturer** | Entity | Company that manufactures aircraft models (e.g., Boeing, Airbus). Pre-loaded during system bootstrapping. Referenced by one or more AircraftModel entries. |
| **RegistrationNumber** | Value Object | Unique alphanumeric identifier of a physical Aircraft instance, formatted according to aviation standards (e.g., CS-TUA). Used as the natural key to look up a specific aircraft. |
| **Role** | Enum | Authorisation role that defines the access level of a User. Values: ADMIN, BACKOFFICE_OPERATOR, ATCC, MAINTENANCE_TECHNICIAN, MAINTENANCE_SUPERVISOR. |
| **Route** | Aggregate Root / Entity | Planned flight path linking an origin Airport to a destination Airport. Has a unique RouteID, a set of RouteRequirements, a history of RouteVersion entries, and an isActive flag. |
| **RouteID** | Value Object | Unique business identifier for a specific Route, generated as a UUID. Has no identity beyond its value. |
| **RouteRequirements** | Value Object | Minimum technical demands (range, capacity, optional required certification code) that an Aircraft must meet to safely operate a Route. |
| **RouteVersion** | Entity | Historical snapshot of a Route's operational parameters at a point in time: distance, estimated flight time, validity period (validFrom, validUntil), and change reason. A new RouteVersion is created each time a Route is updated, preserving full history. |
| **RunwayInfo** | Value Object | Structural data of a single runway at an Airport: name, length (metres), and orientation (e.g., magnetic heading). An Airport may have multiple RunwayInfo entries. |
| **ScheduledFlight** | Aggregate Root / Entity | The temporal and physical allocation of an Aircraft to a Route. Records scheduled and actual departure/arrival times, a FlightNumber, and a FlightStatus. Subject to range, capacity, certification, and availability constraints. |
| **TemplateType** | Enum | Category of a MaintenanceTemplate. Values: INSPECTION, SCHEDULED_MAINTENANCE, OVERHAUL, MODIFICATION. |
| **User** | Aggregate Root / Entity | Person with access to the AISafe system. Has a username, a hashed Password, and one or more Roles that determine their permissions. Authentication is handled via JWT tokens. |

---
*Total concepts: 32 | Stereotypes used: Aggregate Root / Entity, Entity, Value Object, Enum*
---

