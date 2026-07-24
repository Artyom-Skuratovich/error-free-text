package com.github.artyomskuratovich.errorfreetext.repository;

import com.github.artyomskuratovich.errorfreetext.model.CorrectionTask;
import com.github.artyomskuratovich.errorfreetext.model.TaskStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CorrectionTaskRepository extends JpaRepository<CorrectionTask, UUID> {
    List<CorrectionTask> findByStatus(TaskStatus status, Pageable pageable);
}