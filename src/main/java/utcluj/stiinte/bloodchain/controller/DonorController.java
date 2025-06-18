package utcluj.stiinte.bloodchain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import utcluj.stiinte.bloodchain.data.appointment.DonorDonation;
import utcluj.stiinte.bloodchain.data.donor.BloodRequestData;
import utcluj.stiinte.bloodchain.data.donor.DonorDetailsResponse;
import utcluj.stiinte.bloodchain.service.DonorService;
import utcluj.stiinte.bloodchain.service.StorageService;

import java.util.List;

@RestController
@RequestMapping("v1/donors")
@AllArgsConstructor
@Tag(name = "Donors", description = "Operations accessible to donors")
public class DonorController {
    
    private final DonorService donorService;
    private final StorageService storageService;
    
    @GetMapping("/{id}")
    public DonorDetailsResponse getUserDetails(@PathVariable("id") long id) {
        return donorService.getUserDetails(id);
    }

    @Operation(description = "Returns the donation history of a donor, with the most recent as the first")
    @GetMapping("/{id}/donations")
    public List<DonorDonation> getDonorDonationHistory(@PathVariable long id) {
        return donorService.getDonationHistory(id);
    }

    @PostMapping("/{id}/medical-file-upload")
    public void handleFileUpload(@PathVariable long id, @RequestPart("file") MultipartFile file) {
        donorService.saveMedicalFile(id, file);
    }

    @PostMapping("/{id}/request-blood")
    public void requestBlood(@PathVariable long id, @RequestBody BloodRequestData request) {
        donorService.requestBlood(id, request);
    }
}
