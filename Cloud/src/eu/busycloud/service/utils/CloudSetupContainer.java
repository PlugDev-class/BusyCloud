package eu.busycloud.service.utils;

import eu.busycloud.service.CloudInstance;

public class CloudSetupContainer {
	
	public String question;
	public String shortQuestion;
	public boolean optional;
	
	public Object answer;
	public AnswerType answerType;
	
	public enum AnswerType {
		
		STRING,
		INTEGER,
		BOOLEAN;
		
	}
	
	public CloudSetupContainer(String question, String shortQuestion, AnswerType answerType, boolean optional) {
		this.question = question;
		this.shortQuestion = shortQuestion;
		this.answerType = answerType;
		this.optional = optional;
	}
	
	public void setAnswer(Object answer) {
		this.answer = answer;
	}
	
	public Object getAnswer() {
		return answer;
	}

	public boolean validateAnswer(String input) {
		if(input.equalsIgnoreCase("skip") && optional) {
			CloudInstance.LOGGER.info("CloudSetup «~» You skipped that question.");
			answer = null;
			return true;
		} else if(input.equalsIgnoreCase("skip") && !optional) {
			CloudInstance.LOGGER.info("CloudSetup «~» This question isn't skippable.");
			return false;
		}
		switch (answerType) {
			case INTEGER:
				answer = ParseUtils.parseInt(input);
				break;
			case BOOLEAN:
				answer = ParseUtils.parseBool(input, false);
				break;
			default:
				answer = input;
				break;
		}
		return answer != null;
	}
	
}
