# AISafe - Glossary

| Concept | Description |
|---------|-------------|
| **Aircraft** | Physical aircraft belonging to the fleet. Records the specific registration number, manufacturing date, actual seating capacity, total flight hours, and operational status. |
| **AircraftAirportCertification** | Certification that authorises a specific AircraftModel to operate at (fly to/from) a specific Airport. Includes a certification code, description, validity period (certifiedFrom, certifiedUntil), and optional operational notes. |
| **AircraftCertification** | Technical authorisation details (code, description, validity period) embedded within an AircraftModel, indicating that the model meets specific operational requirements. |
| **AircraftModel** | Factory technical specifications of an aircraft type. Includes model name, fuel capacity, maximum range, cruising speed, optional technical diagram/image, and manufacturer. Does not include final seating configuration, which is set per Aircraft instance. |
| **AircraftStatus** | Operational state of an aircraft at a given moment. Values: AVAILABLE, IN_FLIGHT, UNDER_MAINTENANCE, INACTIVE. |
| **Airport** | Airport infrastructure. Records name, city, country, region, timezone, operational hours, optional photos, contact information, runway data, and facility details. |
| **AirportStatus** | Current operational state of an airport. Values: OPERATIONAL, CLOSED, UNDER_MAINTENANCE. |
| **Checklist** | Ordered list of verification tasks embedded within a MaintenanceTemplate. Its meaning is derived from its content and has no identity of its own. |
| **ComponentType** | Categorises the aircraft component targeted by a MaintenanceRecord. Values: ENGINE, AIRFRAME, AVIONICS, INTERIOR, EXTERIOR. |
| **ContactInfo** | A single contact detail for an Airport, including the ContactType channel, the contact value (e.g., phone number or email address), and an optional department or description. |
| **ContactType** | Communication channel for a ContactInfo entry. Values: PHONE, EMAIL, FAX, RADIO. |
| **Coordinates** | Exact geographical position of an Airport expressed as latitude and longitude. Has no identity beyond its values. |
| **FacilityInfo** | Groups information about passenger infrastructure within an Airport: number of terminals, number of gates, and a list of available services. |
| **FlightNumber** | Unique alphanumeric identifier assigned to a specific ScheduledFlight (e.g., TP101). Has no identity beyond its value. |
| **FlightStatus** | Lifecycle states of a ScheduledFlight. Values: SCHEDULED, DELAYED, CANCELED, IN_FLIGHT, COMPLETED. |
| **IATACode** | Three-letter IATA code that uniquely identifies an Airport (e.g., OPO for Porto). Validated against the standard format. |
| **MaintenanceRecord** | Historical and financial record of maintenance performed on an Aircraft. Includes description, start date, expected duration, completion date, completion notes, cost, ComponentType, and MaintenanceStatus. Based on a MaintenanceTemplate. |
| **MaintenanceStatus** | Lifecycle state of a maintenance operation. Values: SCHEDULED, IN_PROGRESS, WAITING_PARTS, COMPLETED, CANCELED. |
| **MaintenanceTemplate** | Reusable blueprint defining a type of maintenance activity. Contains a template name, TemplateType, Checklist, and a list of applicable AircraftModel entries. Used as the basis for creating MaintenanceRecord instances. |
| **Manufacturer** | Company that manufactures aircraft models (e.g., Boeing, Airbus). Pre-loaded during system bootstrapping. Referenced by one or more AircraftModel entries. |
| **Password** | Encapsulates the hashed credential of a User, preventing direct exposure of security-sensitive data. |
| **RegistrationNumber** | Unique alphanumeric identifier of a physical Aircraft instance, formatted according to aviation standards (e.g., CS-TUA). Used as the natural key to look up a specific aircraft. |
| **Role** | Authorisation role that defines the access level of a User. Values: ADMIN, BACKOFFICE_OPERATOR, ATCC, MAINTENANCE_TECHNICIAN, MAINTENANCE_SUPERVISOR. |
| **Route** | Planned flight path linking an origin Airport to a destination Airport. Has a unique RouteID, a set of RouteRequirements, a history of RouteVersion entries, and an isActive flag. |
| **RouteID** | Unique business identifier for a specific Route, generated as a UUID. Has no identity beyond its value. |
| **RouteRequirements** | Minimum technical demands (range, capacity, optional required certification code) that an Aircraft must meet to safely operate a Route. |
| **RouteVersion** | Historical snapshot of a Route's operational parameters at a point in time: distance, estimated flight time, validity period (validFrom, validUntil), and change reason. A new RouteVersion is created each time a Route is updated, preserving full history. |
| **RunwayInfo** | Structural data of a single runway at an Airport: name, length (metres), and orientation (e.g., magnetic heading). An Airport may have multiple RunwayInfo entries. |
| **ScheduledFlight** | The temporal and physical allocation of an Aircraft to a Route. Records scheduled and actual departure/arrival times, a FlightNumber, and a FlightStatus. Subject to range, capacity, certification, and availability constraints. |
| **TemplateType** | Category of a MaintenanceTemplate. Values: INSPECTION, SCHEDULED_MAINTENANCE, OVERHAUL, MODIFICATION. |
| **User** | Person with access to the AISafe system. Has a username, a hashed Password, and one or more Roles that determine their permissions. Authentication is handled via JWT tokens. |

---

