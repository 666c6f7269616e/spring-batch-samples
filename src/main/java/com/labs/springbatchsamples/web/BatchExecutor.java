package com.labs.springbatchsamples.web;

import com.labs.springbatchsamples.job.JobEnum;
import com.labs.springbatchsamples.job.JobRunner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "Job Execution")
@RestController
@RequestMapping(value = "/execucao")
public class BatchExecutor {

    private static final Long NO_EXECUTION = null;
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final JobRunner jobRunner;

    public BatchExecutor(JobRunner jobRunner) {
        this.jobRunner = jobRunner;
    }

    @ApiOperation(value = "Run Job")
    @GetMapping(value = "/{jobName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity
    executarJob(@PathVariable("jobName") JobEnum jobName,
                @RequestParam(required = false) String jobParameter)
            throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException {

        JobParameters jobParameters;

        if (jobParameter != null){
            jobParameters = new JobParametersBuilder()
                    .addString("param", jobParameter)
                    .toJobParameters();
        } else {
            jobParameters = new JobParametersBuilder().toJobParameters();
        }

        JobExecution execution = jobRunner.run(jobName, jobParameters, NO_EXECUTION);

        return ResponseEntity.ok(execution.getStatus());
    }

    @ApiOperation(value = "Stop Job")
    @GetMapping(value = "/stop")
    public ResponseEntity
    stopJob(@RequestParam("jobId") Long jobId) throws NoSuchJobExecutionException, JobExecutionNotRunningException {

        jobRunner.stop(jobId);

        return ResponseEntity.ok().build();
    }

}
