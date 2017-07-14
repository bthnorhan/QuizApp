package tr.bthnorhan.quizapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private TextView tv_login_notregistered;
    private Button btn_login_login;
    private EditText et_login_email,et_login_password;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private String email,password;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        listeners();
    }

    private void init()
    {
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        if (fUser != null)
        {
            Intent i = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }

        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setTitle("Lütfen Bekleyiniz.");
        pDialog.setMessage("Giriş Yapılıyor.");
        pDialog.setCancelable(false);

        tv_login_notregistered = (TextView) findViewById(R.id.tv_login_notregistered);
        btn_login_login = (Button) findViewById(R.id.btn_login_login);
        et_login_email = (EditText) findViewById(R.id.et_login_email);
        et_login_password = (EditText) findViewById(R.id.et_login_password);
    }

    private void listeners()
    {
        tv_login_notregistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        btn_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = et_login_email.getText().toString().trim();
                password = et_login_password.getText().toString().trim();
                pDialog.show();

                if (email.length() >= 7 && password.length() >= 6)
                {
                    fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                fUser = fAuth.getCurrentUser();
                                pDialog.dismiss();
                                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(i);
                                finish();
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            }
                            else
                            {
                                pDialog.dismiss();
                                Log.d(getLocalClassName(),task.getException().getMessage());
                                Toast.makeText(LoginActivity.this,getResources().getString(R.string.there_is_an_error),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else if (email.length() <= 0 || password.length() <= 0)
                {
                    pDialog.dismiss();
                    if (email.length() == 0) et_login_email.setError("Boş bırakmayınız.");
                    if (password.length() == 0) et_login_password.setError("Boş bırakmayınız.");
                }
                else
                {
                    pDialog.dismiss();
                    if (email.length() < 7) et_login_email.setError("Düzgün email giriniz.");
                    if (password.length() < 6) et_login_password.setError("Şifreniz 6 kara0kter olmalıdır.");
                }
            }
        });
    }
}
