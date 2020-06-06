package com.example.churchlocation.Messenger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.churchlocation.Database.SavedUserDB;
import com.example.churchlocation.Model.UserObject;
import com.example.churchlocation.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class LoginPage extends Fragment implements View.OnClickListener {
    private View view;
    private Context ctx;

    private EditText mEmail, mPassword;
    private TextView mForgotPassword, mRegisterAcc;
    private Button mLoginUser;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    private SavedUserDB savedUserDB;
    private UserObject userObject = new UserObject();

    public LoginPage() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = container.getRootView();
        ctx = inflater.getContext();

        View rootView = LayoutInflater.from(ctx).inflate(R.layout.activity_login_page, null, false);

        mEmail = rootView.findViewById(R.id.etEmail);
        mPassword = rootView.findViewById(R.id.etPassword);
        mForgotPassword = rootView.findViewById(R.id.etForgotPassword);
        mRegisterAcc = rootView.findViewById(R.id.etRegisterAcc);
        mLoginUser = rootView.findViewById(R.id.etLoginUser);

        savedUserDB = new SavedUserDB(rootView.getContext());

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mForgotPassword.setOnClickListener(this);
        mRegisterAcc.setOnClickListener(this);
        mLoginUser.setOnClickListener(this);

        String uid = null;
        if (getArguments() != null) {
            uid = getArguments().getString("UID");
        }
        if(uid != null){
            checkVerificationStatus(uid);

            Log.d("TAG", "From registration account "+uid);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.etForgotPassword:
                break;
            case R.id.etRegisterAcc:
                startActivity(new Intent(view.getContext(), RegistrationPage.class));
                break;
            case R.id.etLoginUser:
//                Toast.makeText(view.getContext(), "Not yet available", Toast.LENGTH_SHORT).show();

                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if(!email.isEmpty() && !password.isEmpty()){
                    /*firebaseAuth.fetchSignInMethodsForEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                    boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                                    if(isNewUser){
                                        Log.d("TAG", "Is new user");
                                    } else {
                                        Log.d("TAG", "Is not new user");
                                    }
                                }
                            });*/

                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            task.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Log.d("TAG", "Is not new user");

                                    String uid = authResult.getUser().getUid();

                                    if(authResult.getUser() != null){
                                        if(authResult.getUser().isEmailVerified()){
                                            DatabaseReference db = firebaseDatabase.getReference("users/"+uid+"/emailVerification");
                                            db.setValue(true);

                                            userObject.setId(uid);
                                            userObject.setEmail(authResult.getUser().getEmail());

                                            savedUserDB.addUser(userObject);

                                            Log.d("TAG", String.valueOf(savedUserDB.totalUsers()));

                                            FragmentManager manager = getActivity().getSupportFragmentManager();
                                            FragmentTransaction transaction = manager.beginTransaction();
                                            transaction.replace(R.id.fragment_container, new RecentChatsPage());
                                            transaction.commit();

                                        } else {
                                            sendVerificationEmail();
//                                            checkVerificationStatus(authResult.getUser().getUid());
                                        }
                                    }

//                                    Toast.makeText(LoginPage.this, authResult.getUser().getUid(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            task.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG", "Is new user");
                                }
                            });
                        }
                    });
                }
                break;
        }
    }

    private void checkVerificationStatus(String uid) {
        DatabaseReference db = firebaseDatabase.getReference("users/"+uid+"/emailVerification");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean value = dataSnapshot.getValue(Boolean.class);

                Log.d("TAG", String.valueOf(value));

//                The user is not verified
                if(!value){
                    Log.d("TAG", "User not verified");

                    sendVerificationEmail();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendVerificationEmail() {
        if(firebaseUser != null){
            firebaseUser.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.d("TAG", "Email sent");
                                Toast.makeText(view.getContext(), "Check email", Toast.LENGTH_LONG).show();
                            } else {
                                Log.d("TAG", task.getException().getMessage());
                            }
                        }
                    });
        }
    }

}
