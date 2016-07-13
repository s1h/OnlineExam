package com.sunhang.onlineexam;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 孙航 on 2016/07/11
 */
public class DBService {
    private SQLiteDatabase db;

    /**
     * 构造函数，获取db对象
     */
    public DBService()
    {
        db = SQLiteDatabase.openDatabase("/data/data/com.sunhang.onlineexam/databases/question.db", null, SQLiteDatabase.OPEN_READWRITE);

    }

    /**
     * 方法，返回单选试题集合
     * @return
     */
    public List<Question> getSingleSelectQuestions()
    {
        List<Question> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from question where MultiSelect=1", null);
        if(cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            int count = cursor.getCount();
            for(int i = 0; i < count; i++)
            {
                cursor.moveToPosition(i);
                Question question = new Question();
                question.question = cursor.getString(cursor.getColumnIndex("question"));
                question.answerA = cursor.getString(cursor.getColumnIndex("answerA"));
                question.answerB = cursor.getString(cursor.getColumnIndex("answerB"));
                question.answerC = cursor.getString(cursor.getColumnIndex("answerC"));
                question.answerD = cursor.getString(cursor.getColumnIndex("answerD"));
                question.answer = cursor.getInt(cursor.getColumnIndex("answer"));
                question.ID = cursor.getInt(cursor.getColumnIndex("ID"));
                question.explanation = cursor.getString(cursor.getColumnIndex("explanation"));
                question.MultiSelect = cursor.getInt(cursor.getColumnIndex("MultiSelect"));

                question.selectedAnswer = -1;
                list.add(question);
            }
        }
        return list;
    }
    /**
     * 方法，返回单选试题集合
     * @return
     */
    public List<Question> getMultiSelectQuestions()
    {
        List<Question> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from question where MultiSelect=0", null);
        if(cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            int count = cursor.getCount();
            for(int i = 0; i < count; i++)
            {
                cursor.moveToPosition(i);
                Question question = new Question();
                question.question = cursor.getString(cursor.getColumnIndex("question"));
                question.answerA = cursor.getString(cursor.getColumnIndex("answerA"));
                question.answerB = cursor.getString(cursor.getColumnIndex("answerB"));
                question.answerC = cursor.getString(cursor.getColumnIndex("answerC"));
                question.answerD = cursor.getString(cursor.getColumnIndex("answerD"));
                question.answer = cursor.getInt(cursor.getColumnIndex("answer"));
                question.ID = cursor.getInt(cursor.getColumnIndex("ID"));
                question.explanation = cursor.getString(cursor.getColumnIndex("explanation"));
                question.MultiSelect = cursor.getInt(cursor.getColumnIndex("MultiSelect"));

                question.selectedAnswer = -1;
                list.add(question);
            }
        }
        return list;
    }

}
