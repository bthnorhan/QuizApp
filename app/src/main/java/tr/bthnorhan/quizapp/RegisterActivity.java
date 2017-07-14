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

public class RegisterActivity extends AppCompatActivity {

    private TextView tv_register_registered;
    private EditText et_register_email,et_register_password,et_register_repassword;
    private Button btn_register_register;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private String email,password,repassword;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
        listeners();
    }

    private void init() {
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        if (fUser != null)
        {
            Intent i = new Intent(RegisterActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }

        pDialog = new ProgressDialog(RegisterActivity.this);
        pDialog.setTitle("Lütfen Bekleyiniz.");
        pDialog.setMessage("Kayıt Olunuyor.");
        pDialog.setCancelable(false);

        tv_register_registered = (TextView) findViewById(R.id.tv_register_registered);
        et_register_email = (EditText) findViewById(R.id.et_register_email);
        et_register_password = (EditText) findViewById(R.id.et_register_password);
        et_register_repassword = (EditText) findViewById(R.id.et_register_repassword);
        btn_register_register = (Button) findViewById(R.id.btn_register_register);
    }

    private void listeners() {
        tv_register_registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        btn_register_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = et_register_email.getText().toString().trim();
                password = et_register_password.getText().toString().trim();
                repassword = et_register_repassword.getText().toString().trim();
                pDialog.show();

                if (password.equals(repassword))
                {
                    if (email.length() >= 7 && password.length() >= 6)
                    {
                        fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful())
                                {
                                    fUser = fAuth.getCurrentUser();
                                    pDialog.dismiss();
                                    Intent i = new Intent(RegisterActivity.this,MainActivity.class);
                                    startActivity(i);
                                    finish();
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                }
                                else
                                {
                                    pDialog.dismiss();
                                    Log.d(getLocalClassName(),task.getException().getMessage());
                                    Toast.makeText(RegisterActivity.this,getResources().getString(R.string.there_is_an_error),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else if (email.length() <= 0 || password.length() <= 0)
                    {
                        if (email.length() == 0) et_register_email.setError("Boş bırakmayınız.");
                        if (password.length() == 0) et_register_password.setError("Boş bırakmayınız.");
                        if (repassword.length() == 0) et_register_repassword.setError("Boş bırakmayınız.");
                    }
                    else
                    {
                        pDialog.dismiss();
                        if (email.length() < 7) et_register_email.setError("Düzgün email giriniz.");
                        if (password.length() < 6) et_register_password.setError("Şifreniz 6 karakter olmalıdır.");
                        if (repassword.length() < 6) et_register_repassword.setError("Şifreniz 6 karakter olmalıdır.");
                    }
                }
                else
                {
                    et_register_password.setError("Şifreler aynı değil.");
                    et_register_repassword.setError("Şifreler aynı değil.");
                }
            }
        });
    }
}
