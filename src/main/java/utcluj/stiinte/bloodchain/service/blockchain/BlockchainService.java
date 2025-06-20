package utcluj.stiinte.bloodchain.service.blockchain;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.ContractGasProvider;
import utcluj.stiinte.blockchain.contract.BloodChainToken;
import utcluj.stiinte.blockchain.contract.DonationReward;
import utcluj.stiinte.blockchain.contract.DonationTracking;
import utcluj.stiinte.bloodchain.model.user.Donor;

import java.math.BigInteger;

@Service
@Log4j2
public class BlockchainService {

    private DonationTracking donationTracking;
    private DonationReward donationReward;
    private BloodChainToken token;
    
    public BlockchainService(Web3j web3j,
                             ContractGasProvider gasProvider,
                             Credentials adminCredentials,
                             @Value("${ethereum.contract.donationTrackingAddress}") String trackingAddress,
                             @Value("${ethereum.contract.donationTrackingAddress}") String rewardAddress,
                             @Value("${ethereum.contract.bloodChainTokenAddress}") String tokenAddress) throws Exception {

        if (StringUtils.isEmpty(trackingAddress)) {
            this.donationTracking = DonationTracking.deploy(web3j, adminCredentials, gasProvider).send();
            this.token = BloodChainToken.deploy(web3j, adminCredentials, gasProvider).send();
            this.donationReward = DonationReward.deploy(web3j, adminCredentials, gasProvider,
                    token.getContractAddress(), donationTracking.getContractAddress(), BigInteger.ONE).send();
        } else {
            this.donationTracking = DonationTracking.load(trackingAddress, web3j, adminCredentials, gasProvider);
            this.token = BloodChainToken.load(tokenAddress, web3j, adminCredentials, gasProvider);
            this.donationReward = DonationReward.load(rewardAddress, web3j, adminCredentials, gasProvider);
        }
    }
    
    public void sendTokenToDonor(Donor donor) {
        try {
            TransactionReceipt receipt = token.mint(donor.getBlockchainAddress(), BigInteger.ONE).send();
            
            if (receipt.isStatusOK()) {
                log.info("Successfully made donation");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
