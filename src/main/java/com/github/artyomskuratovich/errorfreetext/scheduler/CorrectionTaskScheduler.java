package com.github.artyomskuratovich.errorfreetext.scheduler;

import com.github.artyomskuratovich.errorfreetext.repository.CorrectionTaskRepository;
import com.github.artyomskuratovich.errorfreetext.service.TextCorrectionClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CorrectionTaskScheduler {
    private final CorrectionTaskRepository repository;
    private final TextCorrectionClient client;

    public void processPendingTasks() {

    }
}