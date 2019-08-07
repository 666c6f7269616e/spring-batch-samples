package com.labs.springbatchsamples.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class SimpleStepListener implements StepExecutionListener {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass().getName());

    @Override
    public void beforeStep(StepExecution stepExecution) {
        LOG.info("Before Step");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        LOG.info("After Step");
        return stepExecution.getExitStatus();
    }
}
