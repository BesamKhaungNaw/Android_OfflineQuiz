package com.learn.wiz.researchofflineqiz.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by besam on 8/11/2016.
 */

public class Question {
    String question_id;
    String status;
    List<Answer> answers;

    // constructors
    public Question() {

    }

    public Question(String tag_name) {
        this.status = status;
    }

    public Question(String question_id, String status) {
        this.question_id = question_id;
        this.status = status;
        answers = new ArrayList<>();
    }
    // setter
    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAnswers(Answer answer){this.answers.add(answer);}


    // getter
    public String getQuestion_id() {
        return this.question_id;
    }

    public String getStatus() {
        return this.status;
    }

    public List<Answer> getAnswers(){return  this.answers;}
}