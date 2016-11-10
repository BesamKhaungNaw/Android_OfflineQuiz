package com.learn.wiz.researchofflineqiz.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.learn.wiz.researchofflineqiz.Controller.QuestionController;
import com.learn.wiz.researchofflineqiz.Model.Answer;
import com.learn.wiz.researchofflineqiz.Model.Question;
import com.learn.wiz.researchofflineqiz.R;

import java.util.List;

public class Sqlite extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);

        List<Question> questionList= QuestionController.getAll();
        for(Question question: questionList){
            question.getAnswers();
            System.out.println(" Retrive questions from sqlite : " + question.getQuestion_id());

            for(Answer answer:question.getAnswers()){
                System.out.println(" Retrive answer of each from sqlite " + answer.getValue());
            }

        }


    }
}
