pragma solidity ^0.8.0;

contract DonationTracking {

    enum Status { Collected, Transferred, Transfused }

    struct BloodBag {
        string bagId;
        string bloodType;
        string volumeMl;
        string donorId;
        uint256 collectionTimestamp;
        address currentOwner;
        Status currentStatus;
    }

    // Mapping from bagId to BloodBag
    mapping(string => BloodBag) public bloodBags;

    // Events to log state changes
    event BloodBagCollected(string indexed bagId, string donorId, uint256 timestamp, address collector);
    event BloodBagTransferred(string indexed bagId, address from, address to, uint256 timestamp);
    event BloodBagTransfused(string indexed bagId, uint256 timestamp, address hospital);

    // Only the owner of the contract can perform certain actions
    address public owner;

    // Whitelisted addresses for Transfusion Centers and Hospitals
    mapping(address => bool) public isTransfusionCenter;
    mapping(address => bool) public isHospital;

    modifier onlyTransfusionCenter() {
        require(isTransfusionCenter[msg.sender], "Only a registered Transfusion Center can call this function");
        _;
    }

    modifier onlyHospital() {
        require(isHospital[msg.sender], "Only a registered Hospital can call this function");
        _;
    }

    constructor() {
        owner = msg.sender;
    }

    /**
     * @dev Records a new blood donation. Callable only by a registered Transfusion Center.
     * @param _bagId Identifier for the blood bag.
     * @param _donorId Identifier of the donor.
     */
    function collectBlood(string memory _bagId, string memory _bloodType, string memory _volumeMl, string memory _donorId) public onlyTransfusionCenter {
        require(bytes(_bagId).length > 0, "Bag ID cannot be empty");
        require(bytes(_donorId).length > 0, "Donor ID cannot be empty");
        require(bytes(bloodBags[_bagId].bagId).length == 0, "Blood bag with this ID already exists"); // Check if bagId is already used

        bloodBags[_bagId] = BloodBag({
            bagId: _bagId,
            bloodType: _bloodType,
            volumeMl: _volumeMl,
            donorId: _donorId,
            collectionTimestamp: block.timestamp,
            currentOwner: msg.sender, // Transfusion Center is the initial owner
            currentStatus: Status.Collected
        });

        emit BloodBagCollected(_bagId, _donorId, block.timestamp, msg.sender);
    }

    /**
     * @dev Transfers a blood bag from a Transfusion Center to a Hospital.
     * Callable only by the current owner (Transfusion Center).
     * @param _bagId The ID of the blood bag to transfer.
     * @param _toHospitalAddress The address of the hospital receiving the bag.
     */
    function transferBlood(string memory _bagId, address _toHospitalAddress) public onlyTransfusionCenter {
        require(bytes(bloodBags[_bagId].bagId).length > 0, "Blood bag not found");
        require(bloodBags[_bagId].currentOwner == msg.sender, "You are not the current owner of this blood bag");
        require(bloodBags[_bagId].currentStatus == Status.Collected || bloodBags[_bagId].currentStatus == Status.Transferred, "Blood bag cannot be transferred in its current status");
        require(isHospital[_toHospitalAddress], "Recipient address is not a registered Hospital");
        require(_toHospitalAddress != address(0), "Invalid recipient address");
        require(_toHospitalAddress != msg.sender, "Cannot transfer to yourself");

        address previousOwner = bloodBags[_bagId].currentOwner;
        bloodBags[_bagId].currentOwner = _toHospitalAddress;
        bloodBags[_bagId].currentStatus = Status.Transferred;

        emit BloodBagTransferred(_bagId, previousOwner, _toHospitalAddress, block.timestamp);
    }

    /**
     * @dev Records a blood bag as transfused. Callable only by the current owner (Hospital).
     * @param _bagId The ID of the blood bag to mark as transfused.
     */
    function transfuseBlood(string memory _bagId) public onlyHospital {
        require(bytes(bloodBags[_bagId].bagId).length > 0, "Blood bag not found");
        require(bloodBags[_bagId].currentOwner == msg.sender, "You are not the current owner of this blood bag");
        require(bloodBags[_bagId].currentStatus == Status.Transferred, "Blood bag can only be transfused if its status is 'Transferred'");

        bloodBags[_bagId].currentStatus = Status.Transfused;

        emit BloodBagTransfused(_bagId, block.timestamp, msg.sender);
    }

    function getBloodBagDetails(string memory _bagId)
        public view returns (
            string memory bagId,
            string memory donorId,
            uint256 collectionTimestamp,
            address currentOwner,
            Status currentStatus
        )
    {
        BloodBag storage bag = bloodBags[_bagId];
        require(bytes(bag.bagId).length > 0, "Blood bag not found");

        return (
            bag.bagId,
            bag.donorId,
            bag.collectionTimestamp,
            bag.currentOwner,
            bag.currentStatus
        );
    }
}