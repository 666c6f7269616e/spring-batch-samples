//package com.labs.springbatchsamples.batch.loopDecision;
//
//import com.labs.springbatchsamples.batch.AbstractJobConfigutation;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.job.builder.FlowBuilder;
//import org.springframework.batch.core.job.flow.Flow;
//import org.springframework.batch.core.job.flow.JobExecutionDecider;
//import org.springframework.batch.repeat.RepeatStatus;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration("loopDecisionConfiguration")
//public class JobConfiguration extends AbstractJobConfigutation {
//
//    public JobConfiguration(JobBuilderFactory jobBuilderFactory,
//                            StepBuilderFactory stepBuilderFactory) {
//        super(jobBuilderFactory, stepBuilderFactory);
//    }
//
//    /* ------------------------------ JOB ------------------------------  */
//    @Bean
//    public Job loopDecisionJob(Flow loopDeciderFlow) {
//        return jobBuilderFactory.get("LOOP_DECISION_JOB")
//		        .start(loopDeciderFlow)
//                .build()
//                .build();
//    }
//
//    /* ------------------------------ FLOW ------------------------------  */
//
//    @Bean("loopDeciderFlow")
//    public Flow loopDecisionFlow(@Qualifier("repeatDecider") JobExecutionDecider repeatDecider,
//                                 @Qualifier("peekStep") Step peekStep) {
//
//        FlowBuilder<Flow> decisionFlow = new FlowBuilder<>("loopDecisionFlow");
//
//        decisionFlow.start(peekStep)
//                .next(repeatDecider)
//                .on("CONTINUE")
//                .to(peekStep)
//                .from(repeatDecider)
//                .on("COMPLETED")
//                .end();
//        return decisionFlow.build();
//    }
//
//    @Bean
//    public Step peekStep() {
//        return stepBuilderFactory.get("LOOP_DECISION_PRINT_STEP")
//                .tasklet((contribution, chunkContext) -> {
//                    System.out.println("ok");
//                    return RepeatStatus.FINISHED;
//                }).build();
//    }
//}
