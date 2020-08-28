package com.example.churchlocation.Messenger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;


import com.example.churchlocation.Model.ChatMessage;
import com.example.churchlocation.Model.UserObject;
import com.example.churchlocation.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

public class ChatScreen extends AppCompatActivity {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private String uid = firebaseAuth.getUid();

    private GroupAdapter<ViewHolder> adapter = new GroupAdapter<>();
    private RecyclerView recyclerView;

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);

        editText = findViewById(R.id.editTextChatLogId);

        recyclerView = findViewById(R.id.chatLogRecyclerId);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);

        Intent intent = getIntent();
        final UserObject userObject = intent.getParcelableExtra("USER");

        if(getSupportActionBar()!=null)
            getSupportActionBar().setTitle(userObject.getUsername());

//        getSupportActionBar().setTitle(userObject.getUsername());

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setElevation(0.0f);
        toolbar.setTitle(userObject.getUsername());
        toolbar.setOnClickListener(view -> {
            Intent userInfoIntent = new Intent(ChatScreen.this, UserInfoPage.class);
            userInfoIntent.putExtra("USER", userObject.getId());
            startActivity(userInfoIntent);
        });

        listenForMessages(userObject);

        findViewById(R.id.sendChatLogBtn).setOnClickListener(view -> {
            sendMessage(userObject);
            editText.getText().clear();
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);

        });

        findViewById(R.id.mCloseUserInfo).setOnClickListener(view -> finish());

    }

    private void listenForMessages(final UserObject userObject) {
        String toId = userObject.getId();
        DatabaseReference db = firebaseDatabase.getReference("messages/" + uid + "/" + toId);
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

//                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);

//                Log.d("ChatScreenTAG", chatMessage.getText());

                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);

                if (chatMessage != null) {
                    Log.d("ChatScreenTAG", chatMessage.getText());

                    if (uid.equals(chatMessage.getFromId())) {
                        adapter.add(new ChatRightItem(chatMessage.getText()));
                    } else {
                        adapter.add(new ChatLeftItem(chatMessage.getText(), userObject));
                    }
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);

                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(UserObject userObject) {
        String text = editText.getText().toString();



        DatabaseReference fromRef = FirebaseDatabase.getInstance().getReference("messages/" + uid + "/" + userObject.getId()).push();
        DatabaseReference toRef = FirebaseDatabase.getInstance().getReference("messages/" + userObject.getId() + "/" + uid).push();

//        ChatMessage chatMessage = new ChatMessage(fromRef.getKey(), text, uid, userObject.getId());
//        ChatMessage chatMessage = new ChatMessage(fromRef.getKey(), text, uid, userObject.getId());

        ChatMessage chatMessage = new ChatMessage(fromRef.getKey(), text, uid, userObject.getId(), System.currentTimeMillis() / 1000);

        if (!editText.getText().toString().isEmpty()) {
            fromRef.setValue(chatMessage).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("ChatScreenTag1", "Saved Successfully");
                } else {
                    Log.d("ChatScreenTag1", task.getException().toString());
                }
            });

            toRef.setValue(chatMessage);

            DatabaseReference latestMessageRef = FirebaseDatabase.getInstance().getReference("latestMessage/" + userObject.getId() + "/" + uid);
            DatabaseReference latestMessageToRef = FirebaseDatabase.getInstance().getReference("latestMessage/" + uid + "/" + userObject.getId());

            latestMessageRef.setValue(chatMessage);
            latestMessageToRef.setValue(chatMessage);
        }
    }
}

class ChatLeftItem extends Item<ViewHolder> {
    String text;
    UserObject userObject;

    ChatLeftItem(String text, UserObject userObject) {
        this.text = text;
        this.userObject = userObject;
    }

    @Override
    public void bind(@NonNull ViewHolder viewHolder, int position) {
        TextView textView = viewHolder.itemView.findViewById(R.id.textViewLeftChatID);
        textView.setText(text);

        final ImageView imageView = viewHolder.itemView.findViewById(R.id.imageViewLeftChatID);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageReference = storage.getReference("users/" + userObject.getId() + "/displayPic");
        storageReference.getDownloadUrl().addOnCompleteListener(task -> Picasso.get().load(task.getResult()).fit().centerCrop().into(imageView));
    }

    @Override
    public int getLayout() {
        return R.layout.chat_log_left;
    }
}

class ChatRightItem extends Item<ViewHolder> {
    String text;

    ChatRightItem(String text) {
        this.text = text;
    }

    @Override
    public void bind(@NonNull ViewHolder viewHolder, int position) {
        TextView textView = viewHolder.itemView.findViewById(R.id.textViewRightChatID);
        textView.setText(text);

        final ImageView imageView = viewHolder.itemView.findViewById(R.id.imageViewRightChatID);

        String uid = FirebaseAuth.getInstance().getUid();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageReference = storage.getReference("users/" + uid + "/displayPic");
        storageReference.getDownloadUrl().addOnCompleteListener(task -> Picasso.get().load(task.getResult()).fit().centerCrop().into(imageView));
    }

    @Override
    public int getLayout() {
        return R.layout.chat_log_right;
    }
}

