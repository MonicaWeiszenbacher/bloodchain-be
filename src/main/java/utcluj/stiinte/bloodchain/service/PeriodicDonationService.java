package utcluj.stiinte.bloodchain.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utcluj.stiinte.bloodchain.model.PeriodicDonation;
import utcluj.stiinte.bloodchain.repository.PeriodicDonationRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class PeriodicDonationService {
    
    private final PeriodicDonationRepository periodicDonationRepository;
    private final CalendarService calendarService;
    
    @Transactional
    public void sendNotifications() {
        List<PeriodicDonation> periodicDonations = periodicDonationRepository.findAllByNotificationSentIsFalse();
        
        periodicDonations.forEach(pd -> {
            calendarService.createPeriodicEvent(pd);
            pd.setNotificationSent(true);
            periodicDonationRepository.save(pd);
        });
    }
}
