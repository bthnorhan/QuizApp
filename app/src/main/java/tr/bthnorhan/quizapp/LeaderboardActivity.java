package tr.bthnorhan.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LeaderboardActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseDatabase fData;
    private DatabaseReference dRef;

    private ListView lv_leaderboard_scorelist;
    private Button btn_leaderboard_main;
    private ArrayList<String> scoreList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        init();
        listeners();

    }

    private void init() {
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fData = FirebaseDatabase.getInstance();
        dRef = fData.getReference("scores");

        lv_leaderboard_scorelist = (ListView) findViewById(R.id.lv_leaderboard_scorelist);
        btn_leaderboard_main = (Button) findViewById(R.id.btn_leadderboard_main);

        scoreList = new ArrayList<>();
    }


    private void listeners() {
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                scoreList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren())
                {

                    scoreList.add(ds.getValue(Score.class).getUser() + " \t " + String.valueOf(ds.getValue(Score.class).getScore()));
                }
                if (!scoreList.isEmpty())
                {
                    initList();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btn_leaderboard_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LeaderboardActivity.this,MainActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    private void initList()
    {
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,android.R.id.text1,scoreList);
        lv_leaderboard_scorelist.setAdapter(adapter);
    }
}
