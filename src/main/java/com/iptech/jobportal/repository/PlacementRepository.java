package com.iptech.jobportal.repository;

import com.iptech.jobportal.model.Placement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlacementRepository extends JpaRepository<Placement, Long> {
}