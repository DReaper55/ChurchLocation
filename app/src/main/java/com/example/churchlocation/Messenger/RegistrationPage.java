package com.example.churchlocation.Messenger;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.churchlocation.Activity.HomeActivity;
import com.example.churchlocation.Database.DatabaseHandler;
import com.example.churchlocation.Database.LinksDB;
import com.example.churchlocation.Model.SearchChurchModel;
import com.example.churchlocation.Model.UserObject;
import com.example.churchlocation.R;
import com.example.churchlocation.Utils.ConnectToChurchDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class RegistrationPage extends AppCompatActivity {
    private UserObject userObject = new UserObject();

    private TextInputEditText mFullname, mEmail, mPassword, mUsername;
    private TextInputLayout mLayFullname, mLayEmail, mLayPassword;
    private Button mRegisterAcc;
    private CheckedTextView mLeader, mDisciple;
    private RadioButton mGenderButton;
    private RadioGroup mRadioGroup;
    private Spinner churchDenominations, leaderCountrySelection;

    private DatabaseHandler db;
    private List<SearchChurchModel> searchChurchModelArrayList;
    private ConnectToChurchDB connect = new ConnectToChurchDB();

    private String[] churchesArray;
    private String[] leadersArray;

    private ArrayList<String> leaderCheck = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);
        getSupportActionBar().hide();

        FirebaseApp.initializeApp(this);

//        Toast.makeText(this, title, Toast.LENGTH_SHORT).show();

        setUserInfoDetails();

        getUserTitle();

        setChurchDenomination();

        saveUserToDB();

        findViewById(R.id.etLoginUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setUserInfoDetails() {
        mFullname = findViewById(R.id.etFullname);
        mEmail = findViewById(R.id.etEmail);
        mPassword = findViewById(R.id.etPassword);
        mUsername = findViewById(R.id.etUsername);

        mLayFullname = findViewById(R.id.lay_fullname);
        mLayEmail = findViewById(R.id.lay_email);
        mLayPassword = findViewById(R.id.lay_password);
        leaderCountrySelection = findViewById(R.id.leaderCountrySelection);

// TODO: Change the option to set fields of class after validation
        userObject.setFullname(mFullname.getText().toString());
        userObject.setEmail(mEmail.getText().toString());
        userObject.setPassword(mPassword.getText().toString());

//        mEmail.addTextChangedListener(new ValidationTextWatcher(mEmail));
        mPassword.addTextChangedListener(new ValidationTextWatcher(mPassword));

    }

    private void saveUserToDB() {
        mRegisterAcc = findViewById(R.id.etRegisterUser);

        mRegisterAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUserInfoDetails();

                getUserTitle();

                getUserGender();

//                setChurchDenomination();

                if(validateEmail() && validatePassword()){
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(userObject.getEmail(), userObject.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            task.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("RegistrationPage", e.toString());
                                }
                            });

                            task.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Toast.makeText(RegistrationPage.this, "Successfully created account", Toast.LENGTH_SHORT).show();

                                    String uid = FirebaseAuth.getInstance().getUid();

//                                    set ID
                                    userObject.setId(uid);

                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference mRef = database.getReference("users/"+uid);
                                    mRef.setValue(userObject);

//                                    Code to create admins after accepting leader status

//                                    if(userObject.getTitle().equals("leader")){
//                                        DatabaseReference mLeader = database.getReference("admins/"+uid);
//                                        mLeader.setValue(userObject);
//                                    }

                                    FragmentManager manager;
                                    manager = getSupportFragmentManager();

                                    Bundle bundle = new Bundle();
                                    bundle.putString("UID", uid);

                                    LoginPage loginPage = new LoginPage();
                                    loginPage.setArguments(bundle);
                                    manager.beginTransaction().replace(R.id.fragment_container, loginPage).commit();

                                }
                            });
                        }
                    });

                }

                Log.d("UserObject", userObject.getFullname()
                        + " " + userObject.getEmail()
                        + " " + userObject.getTitle()
                        + " " + userObject.getGender()
                        + " " + userObject.getChurch()
                );
            }
        });
    }

    private void setChurchDenomination() {
        db = connect.getChurches(this);
        searchChurchModelArrayList = db.getAllContacts();

        churchesArray = new String[searchChurchModelArrayList.size()];

        for(int i =0; i<searchChurchModelArrayList.size(); i++){
            churchesArray[i] = searchChurchModelArrayList.get(i).getChurchName();
        }

        churchDenominations = findViewById(R.id.churchDenominations);

        ArrayAdapter churchDenomSpinner = new ArrayAdapter(this,android.R.layout.simple_spinner_item,churchesArray);
        churchDenomSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        churchDenominations.setAdapter(churchDenomSpinner);

        churchDenominations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userObject.setChurch(churchesArray[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setLeaderCountryInfo(){
        db = connect.getChurches(this);
        searchChurchModelArrayList = db.getAllContacts();

        for(int i =0; i<searchChurchModelArrayList.size(); i++){
            String country = searchChurchModelArrayList.get(i).getCountry();

            boolean check = false;

            if(leaderCheck.size() >= 1 && leaderCheck.get(0) != null){
                for(int j=0; j<leaderCheck.size(); j++){
                    if(leaderCheck.get(j).equals(country)){
//                        leaderCheck.add(j,country);

                        check = true;
                    }

                    if(j==leaderCheck.size()-1 && !check){
                        leaderCheck.add(j+1,country);
                    }
                }
            } else {
                leaderCheck.add(i,country);
            }
        }

        leadersArray = new String[leaderCheck.size()];

        for(int i =0; i<leaderCheck.size(); i++){
            leadersArray[i] = leaderCheck.get(i);
        }

        ArrayAdapter churchDenomSpinner = new ArrayAdapter(this,android.R.layout.simple_spinner_item,leadersArray);
        churchDenomSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leaderCountrySelection.setAdapter(churchDenomSpinner);

        leaderCountrySelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userObject.setLeaderCountry(leadersArray[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getUserGender() {
        mRadioGroup = findViewById(R.id.radioGroup);

        int selectedId = mRadioGroup.getCheckedRadioButtonId();
        mGenderButton = findViewById(selectedId);

//        set Gender
        userObject.setGender(mGenderButton.getText().toString());
    }

    private void getUserTitle() {
        mLeader = findViewById(R.id.etConfirmLeader);
        mDisciple = findViewById(R.id.etConfirmDisciple);

        mLeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLeader.toggle();
                if(mLeader.isChecked()){
                    mLeader.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    mLeader.setTextColor(Color.WHITE);
                    mDisciple.setEnabled(false);

                    leaderCountrySelection.setVisibility(View.VISIBLE);
                    setLeaderCountryInfo();

                    userObject.setTitle("leader");

                } else {
                    mLeader.setBackgroundColor(Color.WHITE);
                    mLeader.setTextColor(Color.BLACK);

                    leaderCountrySelection.setVisibility(View.GONE);
//                    mLeader.setChecked(false);
                    mDisciple.setEnabled(true);
                }
            }
        });

        mDisciple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDisciple.toggle();
                if(mDisciple.isChecked()){
                    mDisciple.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    mDisciple.setTextColor(Color.WHITE);
                    mLeader.setEnabled(false);

                    userObject.setTitle("disciple");
                } else {
                    mDisciple.setBackgroundColor(Color.WHITE);
                    mDisciple.setTextColor(Color.BLACK);
//                    mDisciple.setChecked(false);
                    mLeader.setEnabled(true);
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        getSupportActionBar().hide();
        leaderCheck.clear();
    }





    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validatePassword(){
        if(mPassword.getText().toString().trim().isEmpty()){
            mLayPassword.setError("Input password");
            requestFocus(mPassword);
            return false;
        } else if(mPassword.getText().toString().length() < 6){
            mLayPassword.setError("Password can't be less than 6 digit");
            requestFocus(mPassword);
            return false;
        } else {
            mLayPassword.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail(){
        if(mEmail.getText().toString().trim().isEmpty()){
            mLayEmail.setErrorEnabled(false);
        } else {
            String emailId = mEmail.getText().toString();
            boolean isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches();

            if(!isValid){
                mLayEmail.setError("Invalid email address, ex: abc@example.com");
                requestFocus(mEmail);
                return false;
            }
        }

        return true;
    }


    private class ValidationTextWatcher implements TextWatcher {

        private View view;

        private ValidationTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.etPassword:
                    validatePassword();
                    break;
                case R.id.etEmail:
                    validateEmail();
                    break;
            }
        }
    }
}
