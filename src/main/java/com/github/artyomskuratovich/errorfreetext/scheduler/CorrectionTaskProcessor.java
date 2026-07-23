package com.github.artyomskuratovich.errorfreetext.scheduler;

import com.github.artyomskuratovich.errorfreetext.model.CorrectionTask;
import com.github.artyomskuratovich.errorfreetext.model.TaskStatus;
import com.github.artyomskuratovich.errorfreetext.repository.CorrectionTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
class CorrectionTaskProcessor {
    private final CorrectionTaskRepository repository;

    public void startProcessing(CorrectionTask task) {
        task.setStatus(TaskStatus.PROCESSING);
        repository.saveAndFlush(task);
    }

    public void complete(CorrectionTask task, String correctedText) {
        task.setText(correctedText);
        task.setStatus(TaskStatus.COMPLETED);
        repository.saveAndFlush(task);
    }

    public void fail(CorrectionTask task, String errorMsg) {
        task.setErrorMessage(errorMsg);
        task.setStatus(TaskStatus.FAILED);
        repository.saveAndFlush(task);
    }
}