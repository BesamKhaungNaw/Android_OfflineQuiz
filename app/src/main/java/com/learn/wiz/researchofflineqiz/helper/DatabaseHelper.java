package com.learn.wiz.researchofflineqiz.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.learn.wiz.researchofflineqiz.Model.Answer;
import com.learn.wiz.researchofflineqiz.Model.Question;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

	// Logcat tag
	private static final String LOG = "DatabaseHelper";

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "QizDB";

	// Table Names
	private static final String TABLE_QUESTION = "Question";
	private static final String TABLE_ANSWER = "Answer";

	// Common column names for Question
	private static final String KEY_QUESTION_ID = "question_id";
	private static final String KEY_STATUS = "status";

	// Common column names for Answer
	private static final String KEY_ANSWER_ID = "answer_id";
	private static final String KEY_ANSWER_NAME = "answer_name";
	private static final String KEY_VALUE = "value";


	// Table Create Statements
	// Todo table create statement
	private static final String CREATE_TABLE_QUESTION = "CREATE TABLE "
			+ TABLE_QUESTION + "(" + KEY_QUESTION_ID + " TEXT PRIMARY KEY,"
			+ KEY_STATUS + " INTEGER"
			+")";

	private static final String CREATE_TABLE_ANSWER = "CREATE TABLE "
			+ TABLE_ANSWER + "(" + KEY_ANSWER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ KEY_ANSWER_NAME + " TEXT,"
			+ KEY_VALUE + " TEXT,"
			+ KEY_QUESTION_ID + " TEXT"+")";


	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// creating required tables
		db.execSQL(CREATE_TABLE_QUESTION);
		db.execSQL(CREATE_TABLE_ANSWER);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
		// create new tables
		onCreate(db);
	}

	// ------------------------ "Question" table methods ----------------//

	/*
	 * Creating a todo
	 */
	public long createQuestion(Question question) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_QUESTION_ID, question.getQuestion_id());
		values.put(KEY_STATUS, question.getStatus());
		// insert row
		long todo_id = db.insert(TABLE_QUESTION, null, values);
		return todo_id;
	}

	/**
	 * getting all todos
	 * */
	public List<Question> getAllToDos() {
		List<Question> todos = new ArrayList<>();
		String selectQuery = "SELECT  * FROM " + TABLE_QUESTION;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Question td = new Question();
				td.setQuestion_id(c.getString(c.getColumnIndex(KEY_QUESTION_ID)));
				td.setStatus(c.getString(c.getColumnIndex(KEY_STATUS)));
				// adding to todo list
				todos.add(td);
			} while (c.moveToNext());
		}
		return todos;
	}

	// ------------------------ "Answer" table methods ----------------//

	/*
	 * Creating a todo
	 */
	public long createAnswer(Answer answer) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_ANSWER_NAME, answer.getAnswer_name());
		values.put(KEY_VALUE,answer.getValue());
		values.put(KEY_QUESTION_ID,answer.getQuestion().getQuestion_id());
		// insert row
		long todo_id = db.insert(TABLE_ANSWER, null, values);
		return todo_id;
	}

	/**
	 * get all answers based on question
	 * */
	public List<Answer> getAnswersByQuestion(Question question) {
		List<Answer> answers = new ArrayList<>();
		String selectQuery = "SELECT  * FROM " + TABLE_ANSWER + " WHERE "
				+ KEY_QUESTION_ID + " = '" + question.getQuestion_id()+"'";

	//	String selectQuery = "SELECT  * FROM " + TABLE_ANSWER;
		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Answer td = new Answer();
				td.setAnswer_id(c.getString(c.getColumnIndex(KEY_ANSWER_ID)));
				td.setAnswer_name(c.getString(c.getColumnIndex(KEY_ANSWER_NAME)));
				td.setValue(c.getString(c.getColumnIndex(KEY_VALUE)));
				td.setQuestion(question);
				// adding to todo list
				answers.add(td);
			} while (c.moveToNext());
		}
		return answers;
	}




}
