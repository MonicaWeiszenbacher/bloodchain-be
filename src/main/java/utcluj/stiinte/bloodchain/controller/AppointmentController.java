package utcluj.stiinte.bloodchain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utcluj.stiinte.bloodchain.data.appointment.AppointmentRequest;
import utcluj.stiinte.bloodchain.data.appointment.DonationHistory;
import utcluj.stiinte.bloodchain.model.Appointment;
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

    @Operation(description = "Deletes a blood donation appointment")
    @DeleteMapping("/{id}")
    public void deleteAppointment(@PathVariable long id) {
        appointmentService.deleteAppointment(id);
    }
   
    @Operation(description = "Returns the appointments in a transfusion center")
    @GetMapping("/transfusion-centers/{id}")
    public List<Appointment> getAppointments(@PathVariable long id) {
        return appointmentService.getAppointments(id);
    }

    @Operation(description = "Returns the donation history of a donor, with the most recent as the first")
    @GetMapping("/donors/{id}")
    public List<DonationHistory> getDonationHistory(@PathVariable long id) {
        return appointmentService.getCompletedAppointments(id);
    }
}
