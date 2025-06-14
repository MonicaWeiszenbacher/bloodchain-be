package utcluj.stiinte.bloodchain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utcluj.stiinte.bloodchain.data.appointment.AppointmentRequest;
import utcluj.stiinte.bloodchain.data.appointment.DonorAppointmentData;
import utcluj.stiinte.bloodchain.data.appointment.TransfusionCenterAppointmentData;
import utcluj.stiinte.bloodchain.model.enums.AppointmentStatus;
import utcluj.stiinte.bloodchain.service.AppointmentService;

import java.util.List;

@RestController
@RequestMapping("v1/appointments")
@AllArgsConstructor
@Tag(name = "Appointments", description = "Donor appointment management")
public class AppointmentController {
    
    private final AppointmentService appointmentService;

    @Operation(description = "Creates a blood donation appointment")
    @PostMapping
    public void saveAppointment(@RequestBody AppointmentRequest request) {
       appointmentService.saveAppointment(request);
    }

    @Operation(description = "Updates a blood donation appointment")
    @PutMapping("/{id}")
    public void updateAppointment(@PathVariable long id,
                                  @RequestBody AppointmentRequest request) {
        appointmentService.updateAppointment(id, request);
    }

    @Operation(description = "Marks a blood donation appointment as completed")
    @PutMapping("/{id}/complete")
    public void completeAppointment(@PathVariable long id) {
        appointmentService.completeAppointment(id);
    }

    @Operation(description = "Deletes a blood donation appointment")
    @DeleteMapping("/{id}")
    public void deleteAppointment(@PathVariable long id) {
        appointmentService.deleteAppointment(id);
    }
   
    @Operation(description = "Returns the appointments in a transfusion center")
    @GetMapping("/scheduled/transfusion-centers/{id}")
    public List<TransfusionCenterAppointmentData> getAppointments(@PathVariable long id) {
        return appointmentService.getTransfusionCenterAppointments(id, AppointmentStatus.SCHEDULED);
    }

    @Operation(description = "Returns the donation history of a transfusion center")
    @GetMapping("/completed/transfusion-centers/{id}")
    public List<TransfusionCenterAppointmentData> getTransfusionCenterDonationHistory(@PathVariable long id) {
        return appointmentService.getTransfusionCenterAppointments(id, AppointmentStatus.COMPLETED);
    }

    @Operation(description = "Returns the scheduled donations of a donor")
    @GetMapping("/scheduled/donors/{id}")
    public List<DonorAppointmentData> getScheduledDonations(@PathVariable long id) {
        return appointmentService.getDonorAppointmentsByStatus(id, AppointmentStatus.SCHEDULED);
    }

    @Operation(description = "Returns the donation history of a donor, with the most recent as the first")
    @GetMapping("/completed/donors/{id}")
    public List<DonorAppointmentData> getDonorDonationHistory(@PathVariable long id) {
        return appointmentService.getDonorAppointmentsByStatus(id, AppointmentStatus.COMPLETED);
    }
}
