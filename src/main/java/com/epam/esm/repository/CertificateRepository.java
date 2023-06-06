package com.epam.esm.repository;

import com.epam.esm.domain.Certificate;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CertificateRepository extends JpaRepository<Certificate, Long>, CertificateCustomRepository {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Certificate c where c.id = :id")
    Optional<Certificate> findForUpdateById(long id);
}