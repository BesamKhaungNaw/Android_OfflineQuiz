package com.learn.wiz.researchofflineqiz.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by besam on 8/11/2016.
 */

public class Answer {
    String answer_id;
    String answer_name;
    String value;
    Question question;

    // constructors
    public Answer() {    }

    public Answer(String answer_id) {
        this.answer_name =answer_name;
    }

    public Answer(String answer_id,String answer_name,String value, Question question) {

        this.answer_id = answer_id;
        this.answer_name = answer_name;
        this.value = value;
        setQuestion(question);
    }

    public Answer(String answer_name,String value,Question question) {
        this.answer_name = answer_name;
        this.value = value;
        setQuestion(question);
    }

    // setter
    public void setAnswer_id(String answer_id) {
        this.answer_id = answer_id;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setAnswer_name(String answer_name){this.answer_name = answer_name;}

    public void setQuestion(Question question){this.question = question; question.setAnswers(this);}

    // getter

    public String getAnswer_id() {
        return this.answer_id;
    }

    public String getAnswer_name() {
        return this.answer_name;
    }

    public String getValue(){return  this.value;}

    public Question getQuestion(){return  this.question;}
}
