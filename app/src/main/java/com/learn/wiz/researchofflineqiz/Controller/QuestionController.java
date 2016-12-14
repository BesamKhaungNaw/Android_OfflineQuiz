package com.learn.wiz.researchofflineqiz.Controller;
import android.util.Log;
import com.learn.wiz.researchofflineqiz.Model.Answer;
import com.learn.wiz.researchofflineqiz.Model.JSONParser;
import com.learn.wiz.researchofflineqiz.Model.Question;
import com.learn.wiz.researchofflineqiz.helper.Utilities;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by besam on 9/11/2016.
 */

public class QuestionController {

    private static String host = "http://192.168.7.81/OfflineQuizApp/api/quiz";
    public static void createQuestions(List<Question> questions){

        try{

            for (Question question:
                    questions) {
                Utilities.db.createQuestion(question);
                for (Answer answer:
                        question.getAnswers()){
                    Utilities.db.createAnswer(answer);
                }
            }

        } catch (Exception e){
            Log.i(e.toString(), "Duplicate Items ");
        }
        System.out.println("Finished Created Questions and answers ");
    }

    //getAll
    public static List<Question> getAll(){
        List<Question> questionsList=Utilities.db.getAllToDos();
        for (Question question:questionsList) {
                Utilities.db.getAnswersByQuestion(question);
            }
        return questionsList;
    }

    //Update Answer
    public static void UpdateAnswer(Answer answer){
        Log.i(Long.toString( Utilities.db.updateAnswer(answer)), "Updated Answer Rows ");
    }

    //Update Question
    public static void UpdateQuestion(Question question){
        Log.i(Long.toString( Utilities.db.updateQueston(question)), "Updated Question Rows ");
    }

    public static String sync(Question question) {
        JSONObject user_object = new JSONObject();

        System.out.println("converted json value is "+ JSONParser.fromPojoToJson(question));
      //  JSONObject ne = new JSONObject();
        try {
            user_object.put("ID", "");
            user_object.put("JSONData",JSONParser.fromPojoToJson(question));
            user_object.put("Sync_Date","");
            System.out.println(user_object.toString());
          //  ne.put("model",user_object.toString());
        } catch (Exception e) {
        }
        return JSONParser.postStream(host, user_object.toString());

    }
}
