package com.github.artyomskuratovich.errorfreetext.scheduler;

import com.github.artyomskuratovich.errorfreetext.model.CorrectionTask;
import com.github.artyomskuratovich.errorfreetext.model.TaskStatus;
import com.github.artyomskuratovich.errorfreetext.repository.CorrectionTaskRepository;
import com.github.artyomskuratovich.errorfreetext.service.TextCorrectionClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CorrectionTaskScheduler {
    private final CorrectionTaskRepository repository;
    private final CorrectionTaskProcessor processor;
    private final TextCorrectionClient client;

    @Value("${app.scheduler.batch-size:10}")
    private int batchSize;

    @Scheduled(fixedDelayString = "${app.scheduler.fixed-delay:2000}")
    public void processPendingTasks() {
        Pageable limit = PageRequest.of(0, batchSize);
        List<CorrectionTask> tasks = repository.findByStatus(TaskStatus.CREATED, limit);

        if (tasks.isEmpty()) {
            return;
        }

        for (CorrectionTask task : tasks) {
            try {
                processor.startProcessing(task);
                String correctedText = client.correct(task.getText(), task.getLanguage());
                processor.complete(task, correctedText);
            } catch (Exception e) {
                log.error("Failed to process task ID: {}", task.getId(), e);
                processor.fail(task, e.getMessage());
            }
        }
    }
}