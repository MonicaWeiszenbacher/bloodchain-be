package utcluj.stiinte.bloodchain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utcluj.stiinte.bloodchain.data.appointment.AppointmentRequest;
import utcluj.stiinte.bloodchain.data.appointment.TransfusionCenterAppointmentData;
import utcluj.stiinte.bloodchain.model.enums.DonationStatus;
import utcluj.stiinte.bloodchain.data.TransfusionCenterData;
import utcluj.stiinte.bloodchain.service.TransfusionCenterService;

import java.util.List;

@RestController
@RequestMapping("v1/transfusion-center")
@AllArgsConstructor
@Tag(name = "Transfusion centers", description = "Center management")
public class TransfusionCenterController {
    
    private final TransfusionCenterService transfusionCenterService;
    
    @GetMapping
    List<TransfusionCenterData> getTransfusionCenterAppointments() {
        return transfusionCenterService.getTransfusionCenters(1);
    }

    @Operation(description = "Creates a blood donation appointment")
    @PostMapping
    public void saveAppointment(@RequestBody AppointmentRequest request) {
       transfusionCenterService.saveAppointment(request);
    }

    @Operation(description = "Updates a blood donation appointment")
    @PutMapping("/{id}")
    public void updateAppointment(@PathVariable long id,
                                  @RequestBody AppointmentRequest request) {
        transfusionCenterService.updateAppointment(id, request);
    }

    @Operation(description = "Deletes a blood donation appointment")
    @DeleteMapping("/{id}")
    public void deleteAppointment(@PathVariable long id) {
        transfusionCenterService.deleteAppointment(id);
    }
   
    @Operation(description = "Returns the appointments in a transfusion center")
    @GetMapping("/scheduled/transfusion-centers/{id}")
    public List<TransfusionCenterAppointmentData> getAppointments(@PathVariable long id) {
        return transfusionCenterService.getTransfusionCenterAppointments(id, DonationStatus.SCHEDULED);
    }

    @Operation(description = "Returns the donation history of a transfusion center")
    @GetMapping("/completed/transfusion-centers/{id}")
    public List<TransfusionCenterAppointmentData> getTransfusionCenterDonationHistory(@PathVariable long id) {
        return transfusionCenterService.getTransfusionCenterAppointments(id, DonationStatus.COMPLETED);
    }

    @Operation(description = "Marks a blood donation as completed")
    @PutMapping("/{id}/complete")
    public void completeDonation(@PathVariable long id) {
        //donationService.completeDonation(id);
    }
}
