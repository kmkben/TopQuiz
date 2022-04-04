package com.example.topquiz.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class QuestionBank implements Serializable {

    private List<Question> questions;
    private int questionIndex;

    public QuestionBank(List<Question> questions) {
        this.questions = questions;

        Collections.shuffle(this.questions);
    }

    public Question getCurrentQuestion() {
        return questions.get(questionIndex);
    }

    public Question getNextQuestion() {
        questionIndex++;
        return getCurrentQuestion();
    }
}
