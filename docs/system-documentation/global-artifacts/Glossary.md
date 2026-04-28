# AlSafe - Glossary
# AISafe - Glossary

| Concept | DDD Stereotype | Description |
|--------|----------------|-------------|
| **User** | Aggregate Root / Entity | System user containing secure access credentials. |
| **Role** | Enum | Authorization role (e.g., Backoffice Operator, ATCC) defining permitted user access levels. |

## Aircraft Domain

| Concept | DDD Stereotype | Description |
|--------|----------------|-------------|
| **Manufacturer** | Entity | Aircraft manufacturing company, pre-loaded during system bootstrapping. |
| **AircraftModel** | Entity | Factory technical specifications of an aircraft. Includes range, cruising speed, technical diagrams, and manufacturer, but **not** the final seating configuration. |
| **Aircraft** | Aggregate Root / Entity | Physical aircraft belonging to the fleet. Records the specific registration number, manufacturing date, actual seating capacity, accumulated flight hours, and operational status. |
| **RegistrationNumber** | Value Object | Unique alphanumeric identifier of a physical aircraft. |
| **AircraftStatus** | Enum | Operational state of an aircraft at a given moment (Available, In-Flight, Under Maintenance, Inactive). |
| **AircraftCertification** | Value Object | Certification held by an aircraft model, defining compliance with aviation regulations or operational capabilities. Includes validity period. |
| **Fleet** | Domain Concept | The complete collection of all physical airplanes (`Aircraft`) managed and operated by the Air Transport Company. |
| **AircraftAvailability** | Domain Concept | Derived concept representing whether an aircraft can be assigned to a flight, based on its status and maintenance state. |

## Airport Domain

| Concept | DDD Stereotype | Description |
|--------|----------------|-------------|
| **Airport** | Aggregate Root / Entity | Airport infrastructure. Records geographical data, region, country, operational hours, contact info, and certifies compatible aircraft models for its runways. |
| **IATAcode** | Value Object | Unique standard identifier for an airport. |
| **AirportStatus** | Enum | Current operational state of an airport (Operational, Closed, Under Maintenance). |
| **Coordinates** | Value Object | Exact geographical location (latitude/longitude) of an airport. |
| **RunwayInfo** | Value Object | Structural data of an airport's runway (name, length, orientation). |
| **FacilityInfo** | Value Object | Groups detailed information about passenger infrastructures within an airport (terminals, gates, and services). |
| **ContactInfo** | Value Object | Detailed contact information for an airport, including the contact value and an optional department/description. |
| **ContactType** | Enum | Defines the specific communication channel for a contact (e.g., Phone, Email, Fax, Radio). |
| **AirportAircraftCertification** | Entity | Association indicating that an airport allows a specific aircraft model to operate. May include validity period and operational notes. |

## Route Domain

| Concept | DDD Stereotype | Description |
|--------|----------------|-------------|
| **Route** | Aggregate Root / Entity | Planned flight path linking an origin airport to a destination airport. Maintains a history of route versions to track changes over time. |
| **RouteID** | Value Object | Unique business identifier for a specific route. |
| **RouteRequirements** | Value Object | Minimum technical demands (range, capacity, and optionally certifications) required for an aircraft to safely operate a route. |
| **RouteVersion** | Entity | Historical version of a route containing distance, estimated flight time, validity period, and change reason. |
| **RouteHistory** | Domain Concept | Collection of all RouteVersion instances associated with a Route. |
| **Network** | Domain Concept | The subset of all active routes in the system (`isActive = true`). |

## Flight Domain

| Concept | DDD Stereotype | Description |
|--------|----------------|-------------|
| **ScheduledFlight** | Aggregate Root / Entity | The temporal and physical allocation of an aircraft to a route to perform a flight with defined departure and arrival times (scheduled and actual). |
| **FlightNumber** | Value Object | Unique alphanumeric identifier for a scheduled flight. |
| **FlightStatus** | Enum | Lifecycle states of a scheduled flight (Scheduled, Delayed, Canceled, In-Flight, Completed). |
| **ScheduledDeparture** | Value Object | Planned departure date and time of a flight. |
| **ScheduledArrival** | Value Object | Planned arrival date and time of a flight. |
| **ActualDeparture** | Value Object | Actual departure date and time of a flight. |
| **ActualArrival** | Value Object | Actual arrival date and time of a flight. |
| **FlightAssignment** | Domain Concept | The act of assigning an aircraft to a route at a specific time, materialized by ScheduledFlight. |

## Maintenance Domain

| Concept | DDD Stereotype | Description |
|--------|----------------|-------------|
| **MaintenanceTemplate** | Entity | Baseline maintenance blueprint containing a type and an immutable checklist of tasks. |
| **TemplateType** | Enum | Category of maintenance template (Inspection, Scheduled Maintenance, Overhaul, Modification). |
| **Checklist** | Value Object | List of verification tasks embedded within a maintenance template. |
| **MaintenanceRecord** | Aggregate Root / Entity | Historical and financial record of maintenance performed on an aircraft, including dates, costs, notes, and component type. |
| **MaintenanceStatus** | Enum | Lifecycle state of a maintenance operation (Scheduled, In Progress, Waiting Parts, Completed, Canceled). |
| **ComponentType** | Enum | Categorizes the aircraft component affected (Engine, Airframe, Avionics, Interior, Exterior). |
| **MaintenanceActivity** | Domain Concept | General abstraction grouping maintenance templates and records within the maintenance lifecycle. |

## Constraints & Invariants

| Concept | Description |
|--------|-------------|
| **Route Invariant** | A route must have different origin and destination airports. |
| **Aircraft Invariant** | Registration number must be unique. |
| **Flight Assignment Constraint** | Aircraft must satisfy route requirements (range, capacity, certification). |
| **Maintenance Constraint** | Aircraft under maintenance cannot be assigned to flights. |
| **Airport Constraint** | Aircraft model must be certified by the airport to operate there. |