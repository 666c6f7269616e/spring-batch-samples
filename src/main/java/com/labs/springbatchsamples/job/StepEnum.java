package com.labs.springbatchsamples.job;

import java.util.Arrays;

public enum StepEnum {
	HELLO_WORLD_STEP("helloWorldStep"),
	SEQUENTIAL_STEP_1("sequentialStep1"),
	SEQUENTIAL_STEP_2("sequentialStep2"),
	SEQUENTIAL_STEP_3("sequentialStep3"),
	ASYNC_STEP("asyncStep"),
	DECISION_STEP_ODD("decisionStepOdd"),
	DECISION_STEP_EVEN("decisionStepEven"),
	LOOP_DECISION_PREPARE_STEP("loopDecisionPrepareStep"),
	LOOP_DECISION_PRINT_STEP("loopDecisionPrintStep"),
	LOOP_DECISION_REMOVE_STEP("loopDecisionRemoveStep");

	private String stepName;

	StepEnum(String stepName) {
		this.stepName = stepName;
	}

	public String getStepName() {
		return stepName;
	}

	public static StepEnum fromStepName(String stepName) {
		return Arrays.stream(values()).
				filter(step -> step.getStepName().equals(stepName))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("No step for this name"));
	}
}
