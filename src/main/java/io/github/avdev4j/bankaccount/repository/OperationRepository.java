package io.github.avdev4j.bankaccount.repository;

import io.github.avdev4j.bankaccount.domain.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

    List<Operation> findAllByAccountId(Long accountId);

}
