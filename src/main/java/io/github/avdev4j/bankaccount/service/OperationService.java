package io.github.avdev4j.bankaccount.service;

import io.github.avdev4j.bankaccount.repository.OperationRepository;
import org.springframework.stereotype.Service;

@Service
public class OperationService {

    private final OperationRepository operationRepository;


    public OperationService(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }
}
