package com.labs.springbatchsamples.batch.decisionJob;

import com.labs.springbatchsamples.batch.BatchTestConfiguration;
import com.labs.springbatchsamples.batch.listenerJob.SimpleJobListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.StepScopeTestExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(SpringRunner.class)
@Import({JobConfiguration.class, SimpleJobListener.class})
@ContextConfiguration(classes = {BatchTestConfiguration.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, StepScopeTestExecutionListener.class})
public class JobConfigurationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    public void testInjections() {
        assertThat(jobLauncherTestUtils).isNotNull();
    }


    @Test
    public void decisionJobOdd() throws Exception {
        
        // Given
        JobParameters param = new JobParametersBuilder().addString("param", "abc").toJobParameters();
        
        // When
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(param);
        Collection<StepExecution> stepExecutions = jobExecution.getStepExecutions();

        // Then
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        assertThat(stepExecutions).hasSize(1);
        assertThat(stepExecutions)
                .extracting("exitStatus", "stepName")
                .contains(tuple(ExitStatus.COMPLETED, "DECISION_STEP_ODD"));
    }

    @Test
    public void decisionJobEven() throws Exception {

        // Given
        JobParameters param = new JobParametersBuilder().addString("param", "ab").toJobParameters();

        // When
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(param);
        Collection<StepExecution> stepExecutions = jobExecution.getStepExecutions();

        // Then
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        assertThat(stepExecutions).hasSize(1);
        assertThat(stepExecutions)
                .extracting("exitStatus", "stepName")
                .contains(tuple(ExitStatus.COMPLETED, "DECISION_STEP_EVEN"));
    }
    
    @Test
    public void decisionStepOdd() {

        // Given and When
        Collection<StepExecution> stepExecutions = jobLauncherTestUtils
                .launchStep("DECISION_STEP_ODD")
                .getStepExecutions();
        
        // Then
        assertThat(stepExecutions).hasSize(1);
        assertThat(stepExecutions)
                .extracting(StepExecution::getExitStatus)
                .containsOnly(ExitStatus.COMPLETED);
    }

    @Test
    public void decisionStepEven() {

        // Given and When
        Collection<StepExecution> stepExecutions = jobLauncherTestUtils
                .launchStep("DECISION_STEP_EVEN")
                .getStepExecutions();

        // Then
        assertThat(stepExecutions).hasSize(1);
        assertThat(stepExecutions)
                .extracting(StepExecution::getExitStatus)
                .containsOnly(ExitStatus.COMPLETED);
    }
}