// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "@openzeppelin/contracts/access/Ownable.sol";
import "@openzeppelin/contracts/access/AccessControl.sol";

contract GuideRegistry is Ownable, AccessControl {
    bytes32 public constant GUIDE_ROLE = keccak256("GUIDE_ROLE");

    struct Guide {
        address guideAddress;
        string guideId; // RDB와 연동할 오프체인 식별자
        string metadata; // IPFS 등 외부 링크, 간략 공개 정보
        bool active;
    }

    mapping(address => Guide) public guides;
    address[] public guideList;

    event GuideRegistered(address indexed guide, string guideId);
    event GuideUpdated(address indexed guide, string guideId);
    event GuideActivated(address indexed guide, bool active);

    constructor() Ownable(msg.sender) {
        _grantRole(DEFAULT_ADMIN_ROLE, msg.sender);
    }

    function registerGuide(address _guide, string memory _guideId, string memory _metadata) external onlyOwner {
        require(guides[_guide].guideAddress == address(0), "Already registered");
        guides[_guide] = Guide(_guide, _guideId, _metadata, true);
        guideList.push(_guide);
        _grantRole(GUIDE_ROLE, _guide);
        emit GuideRegistered(_guide, _guideId);
    }

    function updateGuide(address _guide, string memory _metadata) external onlyOwner {
        require(guides[_guide].guideAddress != address(0), "Not registered");
        guides[_guide].metadata = _metadata;
        emit GuideUpdated(_guide, guides[_guide].guideId);
    }

    function activateGuide(address _guide, bool _active) external onlyOwner {
        require(guides[_guide].guideAddress != address(0), "Not registered");
        guides[_guide].active = _active;
        emit GuideActivated(_guide, _active);
    }

    function getGuide(address _guide) external view returns (Guide memory) {
        return guides[_guide];
    }

    function getAllGuides() external view returns (Guide[] memory) {
        Guide[] memory all = new Guide[](guideList.length);
        for (uint i = 0; i < guideList.length; i++) {
            all[i] = guides[guideList[i]];
        }
        return all;
    }
}