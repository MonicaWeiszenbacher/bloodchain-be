pragma solidity ^0.8.19;

import "@openzeppelin/contracts/token/ERC20/ERC20.sol";

contract BloodChainToken is ERC20 {
    address public admin;

    modifier onlyAdmin() {
        require(msg.sender == admin, "Only admin can mint");
        _;
    }

    constructor() ERC20("BloodChainToken", "BBT") {
        admin = msg.sender;
    }

    function mint(address recipient, uint256 amount) public onlyAdmin {
        _mint(recipient, amount);
    }

    function burn(address account, uint256 amount) public onlyAdmin {
        _burn(account, amount);
    }
}
