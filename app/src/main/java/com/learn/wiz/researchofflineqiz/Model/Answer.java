package com.learn.wiz.researchofflineqiz.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by besam on 8/11/2016.
 */

public class Answer {
    String answer_id;
    String value;
    Question question;

    // constructors
    public Answer() {    }

    public Answer(String answer_id) {
        this.answer_id = answer_id;
    }

    public Answer(String answer_id, String value, Question question) {
        this.answer_id = answer_id;
        this.value = value;
        this.question = question;
        question.setAnswers(this);
    }
    // setter
    public void setAnswer_id(String answer_id) {
        this.answer_id = answer_id;
    }

    public void setValue(String value) {
        this.value = value;
    }

    // getter

}
