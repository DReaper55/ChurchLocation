package com.example.churchlocation.Messenger;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.churchlocation.Database.ChurchesDB;
import com.example.churchlocation.Model.SearchChurchModel;
import com.example.churchlocation.Model.UserObject;
import com.example.churchlocation.R;
import com.example.churchlocation.Utils.ConnectToChurchDB;
import com.example.churchlocation.Utils.UriToBitmap;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationPage extends AppCompatActivity {
    private UserObject userObject = new UserObject();

    private TextInputEditText mFullname, mEmail, mPassword, mUsername;
    private TextInputLayout mLayFullname, mLayEmail, mLayPassword;
    private Button mRegisterAcc;
    private CheckedTextView mLeader, mDisciple;
    private RadioButton mGenderButton;
    private RadioGroup mRadioGroup;
    private Spinner churchDenominations, churchCountrySelection, churchStateSelection;
    private Button displayPic;

    private ChurchesDB db;
    private List<SearchChurchModel> searchChurchModelArrayList;
    private ConnectToChurchDB connect = new ConnectToChurchDB();

    private String[] churchesArray;
    private String[] userCountryArray;
    private String[] userStateArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);
        getSupportActionBar().hide();

        FirebaseApp.initializeApp(this);

//        Toast.makeText(this, title, Toast.LENGTH_SHORT).show();

        setUserInfoDetails();

        getUserTitle();

        setUserCountry();
//        setChurchDenomination();

//        saveUserToDB();

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
        churchCountrySelection = findViewById(R.id.churchCountrySelection);
        churchStateSelection = findViewById(R.id.churchStateSelection);
        churchDenominations = findViewById(R.id.churchDenominations);


// TODO: Change the option to set fields of class after validation
        userObject.setFullname(mFullname.getText().toString());
        userObject.setEmail(mEmail.getText().toString());
        userObject.setPassword(mPassword.getText().toString());
        userObject.setUsername(mUsername.getText().toString());

//        mEmail.addTextChangedListener(new ValidationTextWatcher(mEmail));
        mPassword.addTextChangedListener(new ValidationTextWatcher(mPassword));

        displayPic = findViewById(R.id.profile_image);
        displayPic.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 0);
        });

        mRegisterAcc = findViewById(R.id.etRegisterUser);

        mRegisterAcc.setOnClickListener(view -> saveUserToDB());
    }

    private void saveUserToDB() {

        setUserInfoDetails();

        getUserTitle();

        getUserGender();

//                setChurchDenomination();

        if (validateEmail() && validatePassword()) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(userObject.getEmail(), userObject.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    task.addOnFailureListener(e -> Log.d("RegistrationPage", e.toString()));

                    task.addOnSuccessListener(authResult -> {
                        Toast.makeText(RegistrationPage.this, "Successfully created account", Toast.LENGTH_SHORT).show();

                        final String uid = FirebaseAuth.getInstance().getUid();

                        // set ID
                        userObject.setId(uid);

                        // set bio
                        userObject.setBio("N/A");

                        // set date of birth
                        userObject.setDateOfBirth("N/A");

                        // set date of baptism
                        userObject.setDateOfBaptism("N/A");

                        // set hobby
                        userObject.setHobby("N/A");

                        // set title verification
                        userObject.setTitleVerification("false");

                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        DatabaseReference mRefs = db.getReference("users/" + uid);
                        mRefs.setValue(new UserObject(userObject.getFullname(), userObject.getEmail(), userObject.getPassword(), userObject.getTitle(), userObject.getGender(),
                                userObject.getChurch(), userObject.getDisplayPic(), userObject.getId(), userObject.getLeaderCountry(), userObject.getUsername(),
                                userObject.getState(), userObject.getDateOfBirth(), userObject.getDateOfBaptism(), userObject.getBio(),
                                userObject.getHobby(), userObject.isTitleVerification()));

                        // Save display picture in db
                        if (userObject.getDisplayPic() != null) {
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            final StorageReference storageReference = storage.getReference("users/" + uid + "/displayPic");

                            storageReference.putFile(Uri.parse(userObject.getDisplayPic())).addOnCompleteListener(task1 -> {
                                if(task1.isSuccessful()){
                                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                        userObject.setDisplayPic(String.valueOf(uri));

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference mRef = database.getReference("users/" + uid);
                                        mRef.setValue(new UserObject(userObject.getFullname(), userObject.getEmail(), userObject.getPassword(), userObject.getTitle(), userObject.getGender(),
                                                userObject.getChurch(), userObject.getDisplayPic(), userObject.getId(), userObject.getLeaderCountry(), userObject.getUsername(),
                                                userObject.getState(), userObject.getDateOfBirth(), userObject.getDateOfBaptism(), userObject.getBio(),
                                                userObject.getHobby(), userObject.isTitleVerification()));
                                    });
                                }
                            });
                        }


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


    private void setUserCountry() {
        ArrayList<String> leaderCheck = new ArrayList<>();

        db = connect.getChurches(this);
        searchChurchModelArrayList = db.getAllContacts();

        // To avoid repetition of countries in the spinner, filter them out
        for (int i = 0; i < searchChurchModelArrayList.size(); i++) {
            String country = searchChurchModelArrayList.get(i).getCountry();

            boolean check = false;

            if (leaderCheck.size() >= 1 && leaderCheck.get(0) != null) {
                for (int j = 0; j < leaderCheck.size(); j++) {
                    if (leaderCheck.get(j).equals(country)) {
//                        leaderCheck.add(j,country);

                        check = true;
                    }

                    if (j == leaderCheck.size() - 1 && !check) {
                        leaderCheck.add(j + 1, country);
                    }
                }
            } else {
                leaderCheck.add(i, country);
            }
        }

        userCountryArray = new String[leaderCheck.size()];

        for (int i = 0; i < leaderCheck.size(); i++) {
            userCountryArray[i] = leaderCheck.get(i);
        }

        // Todo: Change the spinners to narrow down the churches by location
        ArrayAdapter churchDenomSpinner = new ArrayAdapter(this, android.R.layout.simple_spinner_item, userCountryArray);
        churchDenomSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        churchCountrySelection.setAdapter(churchDenomSpinner);

        churchCountrySelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userObject.setLeaderCountry(userCountryArray[i]);

                setUserState(userCountryArray[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setUserState(String country){
        db = connect.getChurches(this);
        searchChurchModelArrayList = db.getAllContacts();
        ArrayList<String> statesList = new ArrayList<>();
        ArrayList<String> statesCheck = new ArrayList<>();

        for(SearchChurchModel countryIterator : searchChurchModelArrayList){
            if(country.equals(countryIterator.getCountry())){
                statesList.add(countryIterator.getState());
            }
        }

        for (int i = 0; i < statesList.size(); i++) {
            String state = statesList.get(i);

            boolean check = false;

            if (statesCheck.size() >= 1 && statesCheck.get(0) != null) {
                for (int j = 0; j < statesCheck.size(); j++) {
                    if (statesCheck.get(j).equals(state)) {
//                        leaderCheck.add(j,country);

                        check = true;
                    }

                    if (j == statesCheck.size() - 1 && !check) {
                        statesCheck.add(j + 1, state);
                    }
                }
            } else {
                statesCheck.add(i, state);
            }
        }


        userStateArray = new String[statesCheck.size()];

        for (int i = 0; i < statesCheck.size(); i++) {
            userStateArray[i] = statesCheck.get(i);
        }

        ArrayAdapter churchDenomSpinner = new ArrayAdapter(this, android.R.layout.simple_spinner_item, userStateArray);
        churchDenomSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        churchStateSelection.setAdapter(churchDenomSpinner);

        churchStateSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userObject.setState(userStateArray[i]);

                Toast.makeText(getApplicationContext(), userStateArray[i], Toast.LENGTH_LONG).show();

                setChurchDenomination(userStateArray[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void setChurchDenomination(String state) {
        db = connect.getChurches(this);
        searchChurchModelArrayList = db.getAllContacts();
        ArrayList<String> churchList = new ArrayList<>();


//        for (int i = 0; i < searchChurchModelArrayList.size(); i++) {
//            churchesArray[i] = searchChurchModelArrayList.get(i).getChurchName();
//        }

        for(SearchChurchModel stateIterator : searchChurchModelArrayList){
            if(stateIterator.getState().equals(state)){
                churchList.add(stateIterator.getChurchName());
            }
        }

        churchesArray = new String[churchList.size()];

        for (int i = 0; i < churchList.size(); i++) {
            churchesArray[i] = churchList.get(i);
        }

        ArrayAdapter churchDenomSpinner = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, churchesArray);
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

        mLeader.setOnClickListener(view -> {
            mLeader.toggle();
            if (mLeader.isChecked()) {
                mLeader.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                mLeader.setTextColor(Color.WHITE);
                mDisciple.setEnabled(false);

                userObject.setTitle("leader");

            } else {
                mLeader.setBackgroundColor(Color.WHITE);
                mLeader.setTextColor(Color.BLACK);

//                    mLeader.setChecked(false);
                mDisciple.setEnabled(true);
            }
        });

        mDisciple.setOnClickListener(view -> {
            mDisciple.toggle();
            if (mDisciple.isChecked()) {
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

        });
    }

    @Override
    public void onResume() {
        super.onResume();

        getSupportActionBar().hide();
//        leaderCheck.clear();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {

            Uri selectsImageUri = data.getData();
//            Picasso.get().load(data.getData()).into(displayPic);

            Bitmap bitmap = null;
            UriToBitmap uriToBitmap = new UriToBitmap();
            try {
                bitmap = uriToBitmap.getThumbnail(selectsImageUri, getApplicationContext());
            } catch (IOException e) {
                e.printStackTrace();
            }

            CircleImageView circleImageView = findViewById(R.id.circle_profile_image);
            circleImageView.setImageBitmap(bitmap);
            displayPic.setAlpha(0f);

            userObject.setDisplayPic(String.valueOf(selectsImageUri));
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validatePassword() {
        if (mPassword.getText().toString().trim().isEmpty()) {
            mLayPassword.setError("Input password");
            requestFocus(mPassword);
            return false;
        } else if (mPassword.getText().toString().length() < 6) {
            mLayPassword.setError("Password can't be less than 6 digit");
            requestFocus(mPassword);
            return false;
        } else {
            mLayPassword.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        if (mEmail.getText().toString().trim().isEmpty()) {
            mLayEmail.setErrorEnabled(false);
        } else {
            String emailId = mEmail.getText().toString();
            boolean isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches();

            if (!isValid) {
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
