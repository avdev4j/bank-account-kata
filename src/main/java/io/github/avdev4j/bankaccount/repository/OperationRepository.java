package io.github.avdev4j.bankaccount.repository;

import io.github.avdev4j.bankaccount.domain.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

}
