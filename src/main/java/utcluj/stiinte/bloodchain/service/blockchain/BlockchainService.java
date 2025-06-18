package utcluj.stiinte.bloodchain.service.blockchain;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.ContractGasProvider;
import utcluj.stiinte.blockchain.contract.DonationReward;
import utcluj.stiinte.blockchain.contract.DonationTracking;
import utcluj.stiinte.bloodchain.model.donation.Donation;

@Service
@Log4j2
public class BlockchainService {
    
    private final Web3j web3j;
    private final ContractGasProvider gasProvider;
    
    private DonationTracking donationTracking;
    private DonationReward donationReward;
    
    private String trackingAddress;
    private String rewardAddress;

    public BlockchainService(Web3j web3j,
                             ContractGasProvider gasProvider,
                             Credentials adminCredentials,
                             @Value("${ethereum.contract.donationTrackingAddress}") String trackingAddress) throws Exception {
        this.web3j = web3j;
        this.gasProvider = gasProvider;
        
        /*if (StringUtils.isEmpty(trackingAddress)) {
            this.donationTracking = DonationTracking.deploy(web3j, adminCredentials, gasProvider).send();
        } else {
            this.donationTracking = DonationTracking.load(trackingAddress, web3j, adminCredentials, gasProvider);
        }*/
    }
    
    public void createDonation(Donation donation) {
        try {
            // TODO fix code
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
