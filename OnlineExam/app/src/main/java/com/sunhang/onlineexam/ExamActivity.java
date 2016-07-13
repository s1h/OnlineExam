package com.sunhang.onlineexam;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class ExamActivity extends Activity implements RadioGroup.OnCheckedChangeListener,View.OnClickListener {

    private int count;
    private int current;
    private boolean wrongMode;
    private TextView tv_question;
    private RadioButton[] radioButtons;
    private Button btn_next;
    private Button btn_previous;
    private TextView tv_explanation;
    private RadioGroup radioGroup;
    private List<Question> list;
    private String multiSelect;
    private static final String TAG = "ExamActivity";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        multiSelect = intent.getStringExtra("MultiSelect");
        setContentView(getLayoutResID());
        initView();
        initListener();
        initData();
        showToast(multiSelect);
        Log.d(TAG, "onCreate: ------------------------------------------------------"+multiSelect);
    }

    /**
     * 查找view, 这个方法可以避免强转,让我们省去强转操作
     * @param id    ViewID
     * @param <T>   泛型
     * @return      返回这个View
     */
    public <T> T findView(int id) {
        return (T) super.findViewById(id);
    }

    public void showToast(String text) {
        Toast.makeText(ExamActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 初始化数据
     */
    public void initData() {
        DBService dbService = new DBService();
        list = dbService.getSingleSelectQuestions();
        count = list.size();
        current = 0;
        wrongMode = false;
    }

    /**
     * 初始化监听器
     */
    public void initListener() {
        btn_next.setOnClickListener(this);
        btn_previous.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);
    }

    /**
     * RadioGroup监听器
     * @param group         RadioGroup
     * @param checkedId     选中的ID
     */
    public void onCheckedChanged(RadioGroup group, int checkedId){
        //做过的题选项记录
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for(int i = 0; i < 4; i++)
                {
                    if(radioButtons[i].isChecked())
                    {
                        list.get(current).selectedAnswer = i;
                        break;
                    }
                }
            }
        });
    }

    /**
     * 初始化View
     */
    public void initView() {
        tv_question = findView(R.id.question);
        radioButtons = new RadioButton[4];
        radioButtons[0] = findView(R.id.answerA);
        radioButtons[1] = findView(R.id.answerB);
        radioButtons[2] = findView(R.id.answerC);
        radioButtons[3] = findView(R.id.answerD);
        btn_next = findView(R.id.btn_next);
        btn_previous = findView(R.id.btn_previous);
        tv_explanation = findView(R.id.explanation);
        radioGroup = findView(R.id.radioGroup);
        showToast(multiSelect);
        if (multiSelect.equals("singleSelect")) {
            Question q = new DBService().getSingleSelectQuestions().get(0);
            showQuestion(tv_question, radioButtons, tv_explanation, q);
            showToast("您选择的是多选题");
            Log.d(TAG, "initView: dddddddddddddddddddddddddddddddddddddddddddddddddddd");
        } else if (multiSelect.equals("multiSelect")){
            Question q = new DBService().getMultiSelectQuestions().get(0);
            showQuestion(tv_question, radioButtons, tv_explanation, q);
            showToast("您选择的是单选题");
            Log.d(TAG, "initView: xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        }
    }

    /**
     * 获取当前布局ID
     * @return 布局ID
     */
    public int getLayoutResID() {
        return R.layout.activity_exam;
    }


    /**
     * 显示一道题
     * @param tv_question       问题
     * @param radioButtons      选项
     * @param tv_explanation   解析
     * @param q                 试题
     */
    private void showQuestion(TextView tv_question, RadioButton[] radioButtons, TextView tv_explanation, Question q) {
        tv_question.setText(q.question);
        tv_explanation.setText(q.explanation);
        radioButtons[0].setText(q.answerA);
        radioButtons[1].setText(q.answerB);
        radioButtons[2].setText(q.answerC);
        radioButtons[3].setText(q.answerD);
    }

    /**
     * 刷新一道题显示，并保留选中的
     * @param list              所有试题
     * @param tv_question       问题
     * @param radioButtons      选项
     * @param tv_explaination   解析
     * @param radioGroup        选项集合
     */
    private void refreshQuestion(List<Question> list, TextView tv_question, RadioButton[] radioButtons, TextView tv_explaination, RadioGroup radioGroup) {
        Question q = list.get(current);
        showQuestion(tv_question, radioButtons, tv_explaination, q);

        radioGroup.clearCheck();
        if (q.selectedAnswer != -1) {
            radioButtons[q.selectedAnswer].setChecked(true);
        }
    }

    /**
     * 将做错的题的下标加入到错题集合中
     * @param list  试题集合
     * @return wrongList 错题集合
     */
    private List<Integer> checkAnswer(List<Question> list)
    {
        List<Integer> wrongList = new ArrayList<>();
        for(int i = 0; i < list.size(); i++)
        {
            if(list.get(i).answer != list.get(i).selectedAnswer)
            {
                wrongList.add(i);
            }
        }
        return wrongList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exam, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * onClick方法
     * @param view  view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                if (current < count - 1) {
                    current++;
                    refreshQuestion(list, tv_question, radioButtons, tv_explanation, radioGroup);
                }
                else if(current == count - 1 && wrongMode)
                {
                    new AlertDialog.Builder(ExamActivity.this)
                            .setTitle("提示")
                            .setMessage("已经到达最后一题，是否退出？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ExamActivity.this.finish();
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }
                else
                {
                    final List<Integer> wrongList = checkAnswer(list);
                    if (wrongList.size() == 0) {
                        new AlertDialog.Builder(ExamActivity.this)
                                .setTitle("提示")
                                .setMessage("恭喜你全部回答正确！")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ExamActivity.this.finish();
                                    }
                                })
                                .show();
                    } else {
                        new AlertDialog.Builder(ExamActivity.this)
                                .setTitle("提示")
                                .setMessage("您答对了" + (list.size() - wrongList.size()) +
                                        "道题目，答错了" + wrongList.size() + "道题目。是否查看错题？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        wrongMode = true;
                                        List<Question> newList = new ArrayList<>();
                                        for (int i = 0; i < wrongList.size(); i++) {
                                            newList.add(list.get(wrongList.get(i)));
                                        }
                                        list.clear();
                                        for (int i = 0; i < newList.size(); i++) {
                                            list.add(newList.get(i));
                                        }
                                        if (wrongList.size() != 0) {

                                            current = 0;
                                            count = list.size();

                                            //获取当前位置一道试题
                                            Question q = list.get(current);
                                            showQuestion(tv_question, radioButtons, tv_explanation, q);
                                            tv_explanation.setVisibility(View.VISIBLE);//设置解析为可见
                                        }
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ExamActivity.this.finish();
                                    }
                                })
                                .show();
                    }
                }
                break;
            case R.id.btn_previous:
                if (current > 0) {
                    current--;
                    refreshQuestion(list, tv_question, radioButtons, tv_explanation, radioGroup);
                }
                break;

            default:

                break;
        }
    }
}
