package com.labs.springbatchsamples.web;

import com.labs.springbatchsamples.job.DecisionEnum;
import com.labs.springbatchsamples.job.JobDecisionEnum;
import com.labs.springbatchsamples.job.JobEnum;
import com.labs.springbatchsamples.job.SimpleJobEnum;
import com.labs.springbatchsamples.util.SpringContextUtil;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.BeansException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value = "/execucao")
@Api(value = "execucao")
public class BatchExecutor {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final JobLauncher launcher;

    public BatchExecutor(JobLauncher launcher) {
        this.launcher = launcher;
    }

    @GetMapping(value = "/{jobName}")
    public ResponseEntity<?> executarJob(@PathVariable("jobName") SimpleJobEnum jobName) {

        JobParameters jobParameters = new JobParametersBuilder().toJobParameters();

        return runJob(jobName, jobParameters);
    }

    @GetMapping(value = "/{jobName}/{decision}")
    public ResponseEntity<?> executarJobWithDecision(@PathVariable("jobName") JobDecisionEnum jobName, @PathVariable(name = "decision") DecisionEnum decision) {

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("decision", decision.getDecision())
                .addDate("date", new Date())
                .toJobParameters();

        return runJob(jobName, jobParameters);
    }

    private ResponseEntity<?> runJob(JobEnum jobName, JobParameters jobParameters) {

        try {
            Job job = SpringContextUtil.getBean(jobName.getJobName(), Job.class);
            LOG.info("\n==============================\n" +
                    "LAUNCHING JOB: " + jobName.getJobName() +
                    "\n==============================\n");
            JobExecution jobExecution = launcher.run(job, jobParameters);
            return ResponseEntity.ok("JobExecutionId: " + jobExecution.getId() +
                    " JobParameters: " + jobParameters.toString());

        } catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException | JobRestartException e) {
            LOG.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Job " + jobName.getJobName() + " não disponível pelo momento: " + e.getMessage());

        } catch (BeansException e) {
            LOG.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Job " + jobName.getJobName() + " não cadastrado. " + e.getMessage());

        } catch (Exception e) {
            LOG.warn("Erro não gerenciado: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
