package com.dragonwater.backend.Web.Shipment.repository;

import com.dragonwater.backend.Web.Shipment.domain.Shipments;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipments, Long> {
}
