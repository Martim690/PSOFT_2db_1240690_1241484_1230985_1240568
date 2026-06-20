package psoft_aisafe.aircrafts.application;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import psoft_aisafe.aircrafts.application.dtos.AircraftModelResponse;
import psoft_aisafe.aircrafts.application.dtos.RegisterAircraftModelRequest;
import psoft_aisafe.aircrafts.domain.AircraftModel;
import psoft_aisafe.aircrafts.domain.AircraftModelRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class RegisterAircraftModelUseCase {

    private final AircraftModelRepository repository;
    private final String UPLOAD_DIR = "uploads/diagrams/";

    public RegisterAircraftModelUseCase(AircraftModelRepository repository) {
        this.repository = repository;
    }

    public AircraftModelResponse execute(RegisterAircraftModelRequest request, MultipartFile file) {
        if (repository.findByModelName(request.modelName()).isPresent()) {
            throw new IllegalArgumentException("Model name already exists.");
        }

        String diagramFileName = "empty.png";

        if (file != null && !file.isEmpty()) {
            try {
                Files.createDirectories(Paths.get(UPLOAD_DIR));

                String originalName = file.getOriginalFilename();
                String extension = "";
                if (originalName != null && originalName.contains(".")) {
                    extension = originalName.substring(originalName.lastIndexOf("."));
                }

                diagramFileName = request.modelName().replaceAll("\\s+", "-").toLowerCase() + extension;
                Path targetLocation = Paths.get(UPLOAD_DIR + diagramFileName);

                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            } catch (IOException e) {
                throw new RuntimeException("Could not store file. Please try again!", e);
            }
        }

        AircraftModel model = new AircraftModel(
                request.modelName(),
                request.fuelCapacity(),
                request.maximumRange(),
                request.cruisingSpeed(),
                request.manufacturer(),
                diagramFileName
        );

        AircraftModel savedModel = repository.save(model);

        String finalUrl = savedModel.getTechnicalDiagramUrl().equals("empty.png") ? "" : "http://localhost:8080/diagrams/" + savedModel.getTechnicalDiagramUrl();

        return new AircraftModelResponse(
                savedModel.getModelName(),
                savedModel.getManufacturer().name(),
                savedModel.getFuelCapacity(),
                savedModel.getMaximumRange(),
                savedModel.getCruisingSpeed(),
                finalUrl
        );
    }
}