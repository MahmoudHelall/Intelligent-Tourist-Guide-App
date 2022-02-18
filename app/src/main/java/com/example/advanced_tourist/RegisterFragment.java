package com.example.advanced_tourist;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {
    EditText email,pass,repass,name,phone;
    TextView a;
    Button register;
    CheckBox c1,c2;
    FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root=(ViewGroup) inflater.inflate(R.layout.registerfragment,container,false);
        mAuth = FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        email=root.findViewById(R.id.your_email);
        name=root.findViewById(R.id.name);
        a=root.findViewById(R.id.al);
        pass=root.findViewById(R.id.Password);
        repass=root.findViewById(R.id.repass);
        register=root.findViewById(R.id.signup);
        phone=root.findViewById(R.id.phone);
        c1=root.findViewById(R.id.checkBox);
        c2=root.findViewById(R.id.checkBox2);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),Login.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n=name.getText().toString();
                String e=email.getText().toString();
                String p=pass.getText().toString();
                String rp=repass.getText().toString();
                String ph=phone.getText().toString();
                c1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if((buttonView.isChecked()) ){
                            c2.setChecked(false);
                        }
                    }
                });
                c2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if((buttonView.isChecked()) ){
                            c1.setChecked(false);
                        }
                    }
                });
                if(n.isEmpty()) {
                    name.setError("Please enter email!!");
                    return;
                }
                if (p.isEmpty()) {
                    pass.setError("please Enter password!!");
                    return;
                }
                if(!TextUtils.equals(p,rp))
                {
                    repass.setError("Password Not Matching!!");
                    return;
                }
                if (TextUtils.isEmpty(n)||TextUtils.isEmpty(rp)||TextUtils.isEmpty(ph)) {
                    Toast.makeText(getContext(), "Please Fill All Data!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!(c1.isChecked()|| c2.isChecked())){
                    Toast.makeText(getContext(),"select the account type ",Toast.LENGTH_LONG).show();
                    return;

                }
                mAuth.createUserWithEmailAndPassword(e,p).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(getContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(),"Verification Email Hass been Sent ",Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Log.d("TAG","onFailure :Email not sent"+e.getMessage());
                            }
                        });
                        DocumentReference df = fstore.collection("Users").document(user.getUid());
                        Map<String, Object> userinfo = new HashMap<>();
                        userinfo.put("Name", name.getText().toString());
                        userinfo.put("Email", email.getText().toString());
                        userinfo.put("Password", pass.getText().toString());
                        userinfo.put("Phone Number", phone.getText().toString());
                        if(c2.isChecked()){
                            userinfo.put("isAgency","1");
                        }
                        if(c1.isChecked()){
                            userinfo.put("isUser","1");
                        }
                        df.set(userinfo);
                        if(c2.isChecked()){
                            startActivity(new Intent(getContext(),Agency.class));
                        }
                        if(c1.isChecked()){
                            startActivity(new Intent(getContext(),User.class));
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getContext(), "Registration failed!!" + " Please try again later", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        return root;
    }
}
