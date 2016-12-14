package com.learn.wiz.researchofflineqiz.Model;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by student on 8/9/16.
 */
public class StackTrace {
    public static String trace(Exception ex) {
        StringWriter outStream = new StringWriter();
        ex.printStackTrace(new PrintWriter(outStream));
        return outStream.toString();
    }
}
