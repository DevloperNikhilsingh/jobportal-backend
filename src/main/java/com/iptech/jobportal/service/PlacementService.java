package com.iptech.jobportal.service;

import com.iptech.jobportal.model.Placement;
import com.iptech.jobportal.repository.PlacementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class PlacementService {

    @Autowired
    private PlacementRepository placementRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public Placement addPlacement(String studentName, String companyName, String jobRole,
                                   String packageCtc, String batchYear, String location,
                                   String testimonial, MultipartFile image) throws IOException {

        Placement placement = new Placement();
        placement.setStudentName(studentName);
        placement.setCompanyName(companyName);
        placement.setJobRole(jobRole);
        placement.setPackageCtc(packageCtc);
        placement.setBatchYear(batchYear);
        placement.setLocation(location);
        placement.setTestimonial(testimonial);

        if (image != null && !image.isEmpty()) {
            placement.setImageUrl(saveImage(image));
        }

        return placementRepository.save(placement);
    }

    private String saveImage(MultipartFile image) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = image.getOriginalFilename();
        String extension = (originalFilename != null && originalFilename.contains("."))
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
        String filename = UUID.randomUUID().toString() + extension;

        Path filePath = uploadPath.resolve(filename);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/" + filename;
    }

    public List<Placement> getAllPlacements() {
        return placementRepository.findAll();
    }

    public void deletePlacement(Long id) {
        placementRepository.deleteById(id);
    }
}