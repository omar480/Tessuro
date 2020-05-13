package com.csulb.tessuro.models;

import java.util.ArrayList;

public class QuestionModel {
    private int questionNumber;
    private String question;
    private ArrayList<String> answerChoices;
    private String answer;

    public QuestionModel() {

    }

    public QuestionModel(int questionNumber, String question, ArrayList<String> answerChoices, String answer) {
        this.questionNumber = questionNumber;
        this.question = question;
        this.answerChoices = answerChoices;
        this.answer = answer;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<String> getAnswerChoices() {
        return answerChoices;
    }

    public String getAnswer() {
        return answer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswerChoices(ArrayList<String> answerChoices) {
        this.answerChoices = answerChoices;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
