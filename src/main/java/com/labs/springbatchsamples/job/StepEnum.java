package com.labs.springbatchsamples.job;

import java.util.Arrays;

public enum StepEnum {
	HELLO_WORLD("helloStep");

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
				.get();
	}
}
