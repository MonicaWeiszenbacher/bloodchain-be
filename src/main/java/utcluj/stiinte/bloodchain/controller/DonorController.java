package utcluj.stiinte.bloodchain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import utcluj.stiinte.bloodchain.data.donor.*;
import utcluj.stiinte.bloodchain.data.transfusioncenter.TransfusionCenterData;
import utcluj.stiinte.bloodchain.service.DonorService;
import utcluj.stiinte.bloodchain.service.TransfusionCenterService;

import java.util.List;

@RestController
@RequestMapping("v1/donors")
@AllArgsConstructor
@Tag(name = "Donors", description = "Operations accessible to donors")
public class DonorController {
    
    private final DonorService donorService;
    private final TransfusionCenterService transfusionCenterService;
    
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

    @GetMapping("/{id}/closest-transfusion-centers")
    public List<TransfusionCenterData> getTransfusionCenters(@PathVariable long id) {
        return transfusionCenterService.getTransfusionCenters(id);
    }

    @Operation(description = "Creates a blood donation appointment")
    @PostMapping("/{id}/appointment")
    public void saveAppointment(@PathVariable long id, @RequestBody AppointmentRequest request) {
        donorService.saveAppointment(id, request);
    }

    @Operation(description = "Creates a periodic donation reminder")
    @PostMapping("/{id}/remind-and-reserve")
    public void savePeriodicDonation(@PathVariable long id, @RequestBody PeriodicDonationRequest request) {
        donorService.savePeriodicDonation(id, request);
    }
}
