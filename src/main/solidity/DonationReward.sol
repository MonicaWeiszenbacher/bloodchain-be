pragma solidity ^0.8.19;

import "./BloodChainToken.sol";
import "./DonationTracking.sol";

contract DonationReward {
    BloodChainToken public bloodChainToken;
    DonationTracking public donationTracking;

    uint256 public rewardAmount; // Amount of tokens awarded per successful donation

    event DonationRewarded(address donor, string bagId, uint256 amount);

    constructor(address _BloodChainTokenAddress, address _donationTrackingAddress, uint256 _rewardAmount) {
        bloodChainToken = BloodChainToken(_BloodChainTokenAddress);
        donationTracking = DonationTracking(_donationTrackingAddress);
        rewardAmount = _rewardAmount;
    }

    function rewardDonor(string memory bagId, string memory donorId, address donorAddress) public {
        //DonationTracking.BloodBag memory bag = donationTracking.getBloodBagDetails(bagId);

        bloodChainToken.mint(donorAddress, rewardAmount);

        emit DonationRewarded(donorAddress, bagId, rewardAmount);
    }
}
