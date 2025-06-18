package utcluj.stiinte.bloodchain.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utcluj.stiinte.bloodchain.exception.AppException;
import utcluj.stiinte.bloodchain.model.donation.Donation;
import utcluj.stiinte.bloodchain.model.enums.DonationStatus;
import utcluj.stiinte.bloodchain.repository.DonationRepository;
import utcluj.stiinte.bloodchain.service.blockchain.BlockchainService;

@Service
@AllArgsConstructor
public class DonationService {
    
    private final DonationRepository donationRepository;
    private final BlockchainService blockchainService;

    @Transactional
    public void completeDonation(long id) {
        Donation donation = getDonation(id);
        donation.setStatus(DonationStatus.COMPLETED);
        donationRepository.save(donation);
        blockchainService.createDonation(donation);
    }

    private Donation getDonation(long id) {
        return donationRepository
                .findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "donation_not_found"));
    }
}
