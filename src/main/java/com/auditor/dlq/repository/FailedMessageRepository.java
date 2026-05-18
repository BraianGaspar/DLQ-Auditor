package com.auditor.dlq.repository;

import com.auditor.dlq.model.entity.FailedMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface FailedMessageRepository extends JpaRepository<FailedMessageEntity, UUID> {
}
