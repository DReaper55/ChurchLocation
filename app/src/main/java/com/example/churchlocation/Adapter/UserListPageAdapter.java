package com.example.churchlocation.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.churchlocation.Database.SavedUserDB;
import com.example.churchlocation.Messenger.UserInfoPage;
import com.example.churchlocation.Model.UserObject;
import com.example.churchlocation.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserListPageAdapter extends RecyclerView.Adapter<UserListPageAdapter.ViewModel>{
    private Context context;
    private ArrayList<UserObject> userObjectList;

    private SavedUserDB userDB;

    public UserListPageAdapter(Context context, ArrayList<UserObject> userObjectList) {
        this.context = context;
        this.userObjectList = userObjectList;
    }

    @NonNull
    @Override
    public UserListPageAdapter.ViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_chats_adapter_view, null);

        userDB = new SavedUserDB(view.getContext());

        context = view.getContext();
        return new UserListPageAdapter.ViewModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserListPageAdapter.ViewModel holder, final int position) {
//        for (int i = 0; i < userObjectList.size(); i++) {
//            boolean check = false;
//
//            if(userDB.totalUsers() >= 1){
//                for (int j = 0; j < userDB.getAllUsers().size(); j++) {
//                    if(userDB.getAllUsers().get(j).getId().equals(userObjectList.get(i).getId())){
//
//                        check = true;
//                    }
//
//                    if(j == userDB.totalUsers() - 1 && !check){
//                        userDB.addUser(userObjectList.get(i));
//                    }
//                }
//            }
//        }

        final String fullname = userObjectList.get(position).getFullname();
        final String username = userObjectList.get(position).getUsername();
        final String uid = userObjectList.get(position).getId();

        Log.d("AdapterUserTag", userObjectList.get(position).getUsername());
//        holder.username.setText(userObjectList.get(position).getUsername());

        holder.username.setText(username);
        holder.latestMessage.setText(fullname);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageReference = storage.getReference("users/" + uid + "/displayPic");

        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Picasso.get().load(task.getResult()).fit().centerCrop().into(holder.displayPic);
            }
        });

        holder.recentChatsView.setOnClickListener(view -> {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            String uid1 = firebaseAuth.getUid();
            UserObject userObject = userDB.getUser(uid1);

            Log.d("UserListTag", userObject.isTitleVerification());
            Log.d("UserListTag", uid1);


            if(userObject.isTitleVerification().equals("true")){
                Intent intent = new Intent(context, UserInfoPage.class);
                intent.putExtra("USER", userObjectList.get(position).getId());
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Please verify account", Toast.LENGTH_LONG).show();
            }

        });
    }

    @Override
    public int getItemCount() {
        // todo: replace with size of arraylist
        return userObjectList.size();
    }

    class ViewModel extends RecyclerView.ViewHolder {
        TextView username, latestMessage; // this is actually the bio
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

