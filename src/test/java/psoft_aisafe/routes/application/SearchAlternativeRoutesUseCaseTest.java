package psoft_aisafe.routes.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import psoft_aisafe.routes.application.dtos.AlternativeRoutesResponse;
import psoft_aisafe.routes.domain.Route;
import psoft_aisafe.routes.domain.RouteRepository;
import psoft_aisafe.routes.domain.RouteRequirements;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchAlternativeRoutesUseCaseTest {

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private SearchAlternativeRoutesUseCase useCase;

    private Route createRoute(String origin, String dest) {
        RouteRequirements requirements = new RouteRequirements(400, 50, "B737");
        return new Route(origin, dest, requirements, 300.0, 50);
    }

    @Test
    void shouldFindBothDirectAndOneStopAlternativeRoutes() {
        // Arrange
        Route directRoute = createRoute("LIS", "OPO");
        Route lisToMad = createRoute("LIS", "MAD");
        Route madToOpo = createRoute("MAD", "OPO");
        Route lisToLhr = createRoute("LIS", "LHR");
        Route lhrToOpo = createRoute("LHR", "OPO");
        Route lisToCdg = createRoute("LIS", "CDG"); // Unrelated, no CDG -> OPO leg

        when(routeRepository.findByOriginIataCodeAndDestinationIataCodeAndActiveTrue("LIS", "OPO"))
                .thenReturn(List.of(directRoute));
        when(routeRepository.findByOriginIataCodeAndActiveTrue("LIS"))
                .thenReturn(List.of(directRoute, lisToMad, lisToLhr, lisToCdg));
        when(routeRepository.findByDestinationIataCodeAndActiveTrue("OPO"))
                .thenReturn(List.of(directRoute, madToOpo, lhrToOpo));

        // Act
        AlternativeRoutesResponse response = useCase.execute("LIS", "OPO");

        // Assert
        assertNotNull(response);
        assertEquals("LIS", response.originAirport());
        assertEquals("OPO", response.destinationAirport());

        // Direct routes validation
        assertEquals(1, response.directRoutes().size());
        assertEquals(directRoute.getRouteId().getRouteId(), response.directRoutes().get(0).getRouteId());

        // One stop routes validation: LIS->MAD->OPO and LIS->LHR->OPO
        assertEquals(2, response.oneStopRoutes().size());

        AlternativeRoutesResponse.AlternativeRouteChain chain1 = response.oneStopRoutes().stream()
                .filter(c -> "MAD".equals(c.connectionAirport()))
                .findFirst().orElseThrow();
        assertEquals(lisToMad.getRouteId().getRouteId(), chain1.firstLeg().getRouteId());
        assertEquals(madToOpo.getRouteId().getRouteId(), chain1.secondLeg().getRouteId());

        AlternativeRoutesResponse.AlternativeRouteChain chain2 = response.oneStopRoutes().stream()
                .filter(c -> "LHR".equals(c.connectionAirport()))
                .findFirst().orElseThrow();
        assertEquals(lisToLhr.getRouteId().getRouteId(), chain2.firstLeg().getRouteId());
        assertEquals(lhrToOpo.getRouteId().getRouteId(), chain2.secondLeg().getRouteId());

        verify(routeRepository, times(1)).findByOriginIataCodeAndDestinationIataCodeAndActiveTrue("LIS", "OPO");
        verify(routeRepository, times(1)).findByOriginIataCodeAndActiveTrue("LIS");
        verify(routeRepository, times(1)).findByDestinationIataCodeAndActiveTrue("OPO");
    }

    @Test
    void shouldFindOnlyOneStopRoutesWhenDirectRouteDoesNotExist() {
        // Arrange
        Route lisToMad = createRoute("LIS", "MAD");
        Route madToOpo = createRoute("MAD", "OPO");

        when(routeRepository.findByOriginIataCodeAndDestinationIataCodeAndActiveTrue("LIS", "OPO"))
                .thenReturn(Collections.emptyList());
        when(routeRepository.findByOriginIataCodeAndActiveTrue("LIS"))
                .thenReturn(List.of(lisToMad));
        when(routeRepository.findByDestinationIataCodeAndActiveTrue("OPO"))
                .thenReturn(List.of(madToOpo));

        // Act
        AlternativeRoutesResponse response = useCase.execute("LIS", "OPO");

        // Assert
        assertNotNull(response);
        assertTrue(response.directRoutes().isEmpty());
        assertEquals(1, response.oneStopRoutes().size());

        AlternativeRoutesResponse.AlternativeRouteChain chain = response.oneStopRoutes().get(0);
        assertEquals("MAD", chain.connectionAirport());
        assertEquals(lisToMad.getRouteId().getRouteId(), chain.firstLeg().getRouteId());
        assertEquals(madToOpo.getRouteId().getRouteId(), chain.secondLeg().getRouteId());

        verify(routeRepository, times(1)).findByOriginIataCodeAndDestinationIataCodeAndActiveTrue("LIS", "OPO");
        verify(routeRepository, times(1)).findByOriginIataCodeAndActiveTrue("LIS");
        verify(routeRepository, times(1)).findByDestinationIataCodeAndActiveTrue("OPO");
    }

    @Test
    void shouldReturnEmptyListsWhenNoRoutesExistAtAll() {
        // Arrange
        when(routeRepository.findByOriginIataCodeAndDestinationIataCodeAndActiveTrue("LIS", "OPO"))
                .thenReturn(Collections.emptyList());
        when(routeRepository.findByOriginIataCodeAndActiveTrue("LIS"))
                .thenReturn(Collections.emptyList());
        when(routeRepository.findByDestinationIataCodeAndActiveTrue("OPO"))
                .thenReturn(Collections.emptyList());

        // Act
        AlternativeRoutesResponse response = useCase.execute("LIS", "OPO");

        // Assert
        assertNotNull(response);
        assertTrue(response.directRoutes().isEmpty());
        assertTrue(response.oneStopRoutes().isEmpty());

        verify(routeRepository, times(1)).findByOriginIataCodeAndDestinationIataCodeAndActiveTrue("LIS", "OPO");
        verify(routeRepository, times(1)).findByOriginIataCodeAndActiveTrue("LIS");
        verify(routeRepository, times(1)).findByDestinationIataCodeAndActiveTrue("OPO");
    }
}
