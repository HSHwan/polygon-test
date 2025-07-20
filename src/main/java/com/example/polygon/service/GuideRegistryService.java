package com.example.polygon.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.guideregistry.GuideRegistry;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import java.util.List;

@Service
@Slf4j
public class GuideRegistryService {

    private final Web3j web3j;
    private final Credentials credentials;
    private GuideRegistry guideRegistry;

    @Value("${polygon.contract.address}")
    private String contractAddress;

    public GuideRegistryService(Web3j web3j, Credentials credentials) {
        this.web3j = web3j;
        this.credentials = credentials;
    }

    @PostConstruct
    public void init() {
        ContractGasProvider gasProvider = new DefaultGasProvider();
        this.guideRegistry = GuideRegistry.load(contractAddress, web3j, credentials, gasProvider);
    }

    public String registerGuide(String guideAddress, String guideId, String metadata) throws Exception {
        var tx = guideRegistry.registerGuide(guideAddress, guideId, metadata).send();
        log.info("RegisterGuide Tx: {}", tx.getTransactionHash());
        return tx.getTransactionHash();
    }

    public String updateMetadata(String guideAddress, String updatedMetadata) throws Exception {
        var tx = guideRegistry.updateGuide(guideAddress, updatedMetadata).send();
        log.info("UpdateGuide Tx: {}", tx.getTransactionHash());
        return tx.getTransactionHash();
    }

    public GuideRegistry.Guide getGuide(String guideAddress) throws Exception {
        return guideRegistry.getGuide(guideAddress).send();
    }

    public List<GuideRegistry.Guide> getAllGuides() throws Exception {
        return guideRegistry.getAllGuides().send();
    }
}
