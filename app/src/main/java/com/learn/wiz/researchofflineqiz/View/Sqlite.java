package com.learn.wiz.researchofflineqiz.View;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.learn.wiz.researchofflineqiz.Controller.QuestionController;
import com.learn.wiz.researchofflineqiz.Model.Question;
import com.learn.wiz.researchofflineqiz.R;

import java.util.List;

public class Sqlite extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);
        button = (Button) findViewById(R.id.sync);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Question, Void,String>() {
                    @Override
                    protected String doInBackground(Question... params) {
                        return QuestionController.sync(params[0]);
                    }
                    @Override
                    protected void onPostExecute(String result) {
                        Log.i("This is result ", result);
                    }
                }.execute(QuestionController.getAll().get(0));
            }
        });


//        List<Question> questionList= QuestionController.getAll();
//        for(Question question: questionList){
//            question.getAnswers();
//            System.out.print("Question : " + question.getQuestion_id());
//            System.out.println("     Question status : " + question.getStatus());
//            for(Answer answer:question.getAnswers()){
//                System.out.println("Answer :" + answer.getValue());
//            }
//        }
//
//        System.out.println("Update Hidden1's Question's status to Sended ");
//        questionList.get(0).setStatus("Sended");
//        QuestionController.UpdateQuestion(questionList.get(0));
//
//        System.out.println("Update Hidden1's answer to 100 ");
//        questionList.get(0).getAnswers().get(0).setValue("100");
//        QuestionController.UpdateAnswer(questionList.get(0).getAnswers().get(0));
//
//        System.out.println("After update Hidden1's answer to 100 ");
//        List<Question> updateQuestions= QuestionController.getAll();
//        System.out.println("After update value is ");
//        for(Question question: updateQuestions){
//            question.getAnswers();
//            System.out.print("Question : " + question.getQuestion_id());
//            System.out.println("Question status : " + question.getStatus());
//            for(Answer answer:question.getAnswers()){
//                System.out.println("Answer :" + answer.getValue());
//            }
//        }

    }
}
