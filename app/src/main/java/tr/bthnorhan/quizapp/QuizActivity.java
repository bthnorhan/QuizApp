package tr.bthnorhan.quizapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseDatabase fData;
    private DatabaseReference dRef;

    private TextView tv_quiz_question;
    private RadioButton rb_quiz_answer1,rb_quiz_answer2,rb_quiz_answer3,rb_quiz_answer4;
    private RadioGroup rg_quiz_answers;
    private Button btn_quiz_nextquestion,btn_quiz_leaderboard;

    private ArrayList<Question> quiz;
    private int qIndex = 0, score = 0;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        init();
        listeners();
    }

    private void init() {
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fData = FirebaseDatabase.getInstance();
        dRef = fData.getReference("questions");

        quiz = new ArrayList<>();

        tv_quiz_question = (TextView) findViewById(R.id.tv_quiz_question);
        rb_quiz_answer1 = (RadioButton) findViewById(R.id.rb_quiz_answer1);
        rb_quiz_answer2 = (RadioButton) findViewById(R.id.rb_quiz_answer2);
        rb_quiz_answer3 = (RadioButton) findViewById(R.id.rb_quiz_answer3);
        rb_quiz_answer4 = (RadioButton) findViewById(R.id.rb_quiz_answer4);
        rg_quiz_answers = (RadioGroup) findViewById(R.id.rg_quiz_answers);
        btn_quiz_nextquestion = (Button) findViewById(R.id.btn_quiz_nextquestion);
        btn_quiz_leaderboard = (Button) findViewById(R.id.btn_quiz_leaderboard);

        pDialog = new ProgressDialog(QuizActivity.this);
        pDialog.setMessage("Lütfen bekleyiniz.");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void listeners() {
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    quiz.add(ds.getValue(Question.class));
                }
                for (Question q: quiz)
                {
                    Log.d(getLocalClassName(), String.valueOf(q.getT_id()));
                }
                initQuestion();
                pDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        btn_quiz_nextquestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qIndex < quiz.size())
                {
                    Log.d(getLocalClassName(), String.valueOf(qIndex + 1));
                    switch (rg_quiz_answers.getCheckedRadioButtonId())
                    {
                        case R.id.rb_quiz_answer1:
                            if (quiz.get(qIndex).getT_id() == 1)
                            {
                                score += 5;
                                Log.d(getLocalClassName(),"Doğru");
                            }
                            else Log.d(getLocalClassName(),"Yanlış");
                            break;
                        case R.id.rb_quiz_answer2:
                            if (quiz.get(qIndex).getT_id() == 2)
                            {
                                score += 5;
                                Log.d(getLocalClassName(),"Doğru");
                            }
                            else Log.d(getLocalClassName(),"Yanlış");
                            break;
                        case R.id.rb_quiz_answer3:
                            if (quiz.get(qIndex).getT_id() == 3)
                            {
                                score += 5;
                                Log.d(getLocalClassName(),"Doğru");
                            }
                            else Log.d(getLocalClassName(),"Yanlış");
                            break;
                        case R.id.rb_quiz_answer4:
                            if (quiz.get(qIndex).getT_id() == 4)
                            {
                                score += 5;
                                Log.d(getLocalClassName(),"Doğru");
                            }
                            else Log.d(getLocalClassName(),"Yanlış");
                            break;
                        default:
                            Log.d(getLocalClassName(),"Boş");
                            break;
                    }
                    qIndex++;
                    if (qIndex < quiz.size()) initQuestion();
                    else if (qIndex == quiz.size())
                    {
                        btn_quiz_nextquestion.setText(getResources().getString(R.string.btn_quiz_finish));
                        rb_quiz_answer1.setEnabled(false);
                        rb_quiz_answer2.setEnabled(false);
                        rb_quiz_answer3.setEnabled(false);
                        rb_quiz_answer4.setEnabled(false);
                        qIndex++;
                    }
                }
                else if (qIndex >= quiz.size())
                {
                    btn_quiz_nextquestion.setEnabled(false);
                    dRef = fData.getReference("scores").push();
                    dRef.child("user").setValue(fUser.getEmail());
                    dRef.child("score").setValue(score);
                }
            }
        });

        btn_quiz_leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QuizActivity.this,LeaderboardActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    private void initQuestion() {
        rg_quiz_answers.clearCheck();
        tv_quiz_question.setText((qIndex+1) + "-) " + quiz.get(qIndex).getQ_text());
        rb_quiz_answer1.setText(quiz.get(qIndex).getA1());
        rb_quiz_answer2.setText(quiz.get(qIndex).getA2());
        rb_quiz_answer3.setText(quiz.get(qIndex).getA3());
        rb_quiz_answer4.setText(quiz.get(qIndex).getA4());
    }
}
