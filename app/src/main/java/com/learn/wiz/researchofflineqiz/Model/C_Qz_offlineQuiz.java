package com.learn.wiz.researchofflineqiz.Model;

import java.util.Date;

/**
 * Created by besam on 13/12/2016.
 */

public class C_Qz_offlineQuiz {

        int ID;
        String JSONData;
        Date Sync_Date;

        public C_Qz_offlineQuiz(String JSONData){
            this.JSONData = JSONData;
           // this.Sync_Date = Sync_Date;
        }

}
