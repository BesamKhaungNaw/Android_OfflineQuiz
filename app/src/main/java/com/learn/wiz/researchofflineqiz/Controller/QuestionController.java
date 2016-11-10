package com.learn.wiz.researchofflineqiz.Controller;
import com.learn.wiz.researchofflineqiz.Model.Answer;
import com.learn.wiz.researchofflineqiz.Model.Question;
import com.learn.wiz.researchofflineqiz.helper.Utilities;

import java.util.List;

/**
 * Created by besam on 9/11/2016.
 */

public class QuestionController {

    public static void createQuestions(List<Question> questions){

        for (Question question:
                questions) {
            Utilities.db.createQuestion(question);
            for (Answer answer:
                    question.getAnswers()){
                Utilities.db.createAnswer(answer);
            }
        }
    }

    //getAll
    public static List<Question> getAll(){
        List<Question> questionsList=Utilities.db.getAllToDos();
        for (Question question:questionsList) {
                Utilities.db.getAnswersByQuestion(question);
            }
        return questionsList;
    }

}
