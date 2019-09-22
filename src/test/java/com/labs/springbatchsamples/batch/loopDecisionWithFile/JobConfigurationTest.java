package com.labs.springbatchsamples.batch.loopDecisionWithFile;

import com.labs.springbatchsamples.batch.BatchTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.StepScopeTestExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@Import(JobConfiguration.class)
@ContextConfiguration(classes = {RepeatDecider.class, BatchTestConfiguration.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, StepScopeTestExecutionListener.class})
public class JobConfigurationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    
    @Test
    public void testInjections() {
        assertThat(jobLauncherTestUtils).isNotNull();
    }
    
}