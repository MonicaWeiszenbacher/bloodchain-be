package utcluj.stiinte.bloodchain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utcluj.stiinte.bloodchain.data.transfusioncenter.AppointmentRequest;
import utcluj.stiinte.bloodchain.data.transfusioncenter.BloodRequestData;
import utcluj.stiinte.bloodchain.data.transfusioncenter.DonorData;
import utcluj.stiinte.bloodchain.data.transfusioncenter.TransfusionCenterDonation;
import utcluj.stiinte.bloodchain.model.enums.BloodRequestStatus;
import utcluj.stiinte.bloodchain.service.TransfusionCenterService;

import java.util.List;

@RestController
@RequestMapping("v1/transfusion-center")
@AllArgsConstructor
@Tag(name = "Transfusion centers", description = "Center management")
public class TransfusionCenterController {
    
    private final TransfusionCenterService transfusionCenterService;

    @Operation(description = "Returns the donation history of a transfusion center")
    @GetMapping("/{id}/donations")
    public List<TransfusionCenterDonation> getDonationHistory(@PathVariable long id) {
        return transfusionCenterService.getDonations(id);
    }

    @Operation(description = "Returns the details of donors who have donations at this transfusion center")
    @GetMapping("/{id}/blood-requests")
    public List<BloodRequestData> getBloodRequests(@PathVariable long id) {
        return transfusionCenterService.getBloodRequests(id);
    }

    @PatchMapping("{id}/blood-requests/{bloodRequestId}")
    public void updateBloodRequest(@PathVariable long id, @PathVariable long bloodRequestId,
                                   @RequestBody BloodRequestStatus newStatus) {
        transfusionCenterService.updateBloodRequest(bloodRequestId, newStatus);
    }

    @Operation(description = "Creates a blood donation appointment")
    @PostMapping("/{id}/appointment")
    public void saveAppointment(@PathVariable long id, @RequestBody AppointmentRequest request) {
        transfusionCenterService.saveAppointment(id, request);
    }

    @Operation(description = "Returns the details of donors who have donations at this transfusion center")
    @GetMapping("/{id}/donors")
    public List<DonorData> getDonors(@PathVariable long id) {
        return transfusionCenterService.getDonors(id);
    }
    
    @PatchMapping("/{donationId}/complete")
    public void sendTokenToDonor(@PathVariable long donationId) {
        transfusionCenterService.sendTokenToDonor(donationId);
    }
}
