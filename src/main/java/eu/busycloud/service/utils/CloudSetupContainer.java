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
			CloudInstance.LOGGER.info("You skipped that question.");
			answer = null;
			return true;
		} else if(input.equalsIgnoreCase("skip") && !optional) {
			CloudInstance.LOGGER.info("This question isn't skippable.");
			return false;
		}
		switch (answerType) {
			case INTEGER:
				if(input != null)
					answer = ParseUtils.parseInt(input);
				break;
			case BOOLEAN:
				if(input != null)
					answer = ParseUtils.parseBool(input, false);
				break;
			case STRING:
				if(input != null)
					answer = input;
				break;
			default:
				answer = input;
				break;
		}
		return answer != null;
	}
	
}
