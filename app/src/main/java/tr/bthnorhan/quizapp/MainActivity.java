package tr.bthnorhan.quizapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button btn_main_exit,btn_main_quiz,btn_main_leaderboard;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseDatabase fData;
    private DatabaseReference dRef;
    private Boolean quizAvailable = true;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        listeners();
    }

    private void init() {
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fData = FirebaseDatabase.getInstance();
        dRef = fData.getReference("scores");
        btn_main_exit = (Button) findViewById(R.id.btn_main_exit);
        btn_main_quiz = (Button) findViewById(R.id.btn_main_quiz);
        btn_main_leaderboard = (Button) findViewById(R.id.btn_main_leaderboard);

        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Lütfen bekleyiniz.");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void listeners() {

        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot ds: dataSnapshot.getChildren())
                {
                    if (!ds.getKey().isEmpty())
                    {
                        if (ds.child("user").getValue().equals(fUser.getEmail()))
                        {
                            i++;
                        }
                    }
                    else
                    {
                        Log.d(getLocalClassName(),"Boş skor tablosu.");
                    }
                }
                if (i != 0)
                {
                    quizAvailable = false;
                }
                else
                {
                    quizAvailable = true;
                }
                pDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pDialog.dismiss();
            }
        });

        btn_main_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.signOut();
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        btn_main_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quizAvailable)
                {
                    Intent i = new Intent(MainActivity.this,QuizActivity.class);
                    startActivity(i);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Daha önce testi çözdünüz.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_main_leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,LeaderboardActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }
}
