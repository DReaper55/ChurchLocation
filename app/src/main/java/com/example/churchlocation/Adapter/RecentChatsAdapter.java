package com.example.churchlocation.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.churchlocation.Messenger.ChatScreen;
import com.example.churchlocation.Messenger.UserInfoPage;
import com.example.churchlocation.Model.ChatMessage;
import com.example.churchlocation.Model.UserObject;
import com.example.churchlocation.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecentChatsAdapter extends RecyclerView.Adapter<RecentChatsAdapter.ViewModel>{
    private Context context;
    private ArrayList<ChatMessage> chatMessageArrayList; //todo: change this model to a chat model

    public RecentChatsAdapter(Context context, ArrayList<ChatMessage> chatMessageArrayList) {
        this.context = context;
        this.chatMessageArrayList = chatMessageArrayList;
    }

    @NonNull
    @Override
    public RecentChatsAdapter.ViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_chats_adapter_view, null);

        context = view.getContext();
        return new ViewModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentChatsAdapter.ViewModel holder, int position) {
        String toId = chatMessageArrayList.get(position).getToId();
        String message = chatMessageArrayList.get(position).getText();

        if(toId.equals(FirebaseAuth.getInstance().getUid())){
            toId = chatMessageArrayList.get(position).getFromId();
        }

        String finalToId = toId;
        FirebaseDatabase.getInstance().getReference("users/" + finalToId + "/username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username = dataSnapshot.getValue(String.class);

                holder.username.setText(username);
                holder.latestMessage.setText(message);

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReference("users/" + finalToId + "/displayPic");
                storageReference.getDownloadUrl().addOnCompleteListener(task -> Picasso.get().load(task.getResult()).fit().centerCrop().into(holder.displayPic));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.recentChatsView.setOnClickListener(view -> {
            FirebaseDatabase.getInstance().getReference("users/" + finalToId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserObject userObject = dataSnapshot.getValue(UserObject.class);
                    Intent intent = new Intent(context, ChatScreen.class);
                    intent.putExtra("USER", userObject);
                    context.startActivity(intent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return chatMessageArrayList.size();
    }

    static class ViewModel extends RecyclerView.ViewHolder {
        TextView username, latestMessage;
        CircleImageView displayPic;
        RelativeLayout recentChatsView;

        ViewModel(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.mUserName);
            latestMessage = itemView.findViewById(R.id.mTextMessage);
            displayPic = itemView.findViewById(R.id.circle_profile_image);
            recentChatsView = itemView.findViewById(R.id.recentChatsView);
        }
    }
}
