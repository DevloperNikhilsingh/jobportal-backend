package com.iptech.jobportal.repository;

import com.iptech.jobportal.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    void deleteByJobId(Long jobId);
}