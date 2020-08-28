package com.example.churchlocation.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.churchlocation.Database.VerifiedDisciplesDB;
import com.example.churchlocation.Model.UserObject;
import com.example.churchlocation.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchUserRequestsAdapter extends RecyclerView.Adapter<SearchUserRequestsAdapter.ViewModel> {
    private Context context;
    private List<UserObject> userObjectList;
    private int title;
    private RecyclerView recyclerView;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();


    public SearchUserRequestsAdapter(Context context, List<UserObject> userObjectList, int title, RecyclerView recyclerView) {
        this.context = context;
        this.userObjectList = userObjectList;
        this.title = title;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public SearchUserRequestsAdapter.ViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_user_requests, null, false);
        context = view.getContext();
        return new ViewModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchUserRequestsAdapter.ViewModel holder, final int position) {
        final String fullname = userObjectList.get(position).getFullname();
        final String username = userObjectList.get(position).getUsername();
        final String uid = userObjectList.get(position).getId();
//        final String displayPic = userObjectList.get(position).getDisplayPic();

        holder.mUserName.setText(username);
        holder.mFullname.setText(fullname);

        // loading displayPic
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageReference = storage.getReference("users/" + uid + "/displayPic");

        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Picasso.get().load(task.getResult()).fit().centerCrop().into(holder.displayPic);
            }
        });


        holder.cardView.setOnClickListener(view -> {
            Toast.makeText(context, username, Toast.LENGTH_LONG).show();

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(view.getContext());
            final AlertDialog dialog;

            final View popUp = LayoutInflater.from(view.getContext()).inflate(R.layout.custom_user_request_alertdialog, null);

            TextView mUserName = popUp.findViewById(R.id.mUserName);
            mUserName.setText(fullname);

            if(title != 2){
                TextView mChurchDenomination = popUp.findViewById(R.id.mChurchDenomination);
                mChurchDenomination.setText(userObjectList.get(position).getChurch());
            }

            // load display image from firebase
            final ImageView displayPicView = popUp.findViewById(R.id.mDisplayPic);

            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Picasso.get().load(task.getResult()).fit().centerCrop().into(displayPicView);

                }
            });

            if (title == 0) {
                popUp.findViewById(R.id.discipleMessage).setVisibility(View.VISIBLE);
                alertBuilder.setView(popUp);
                dialog = alertBuilder.create();

                dialog.show();

            } else if (title == 1) {
                popUp.findViewById(R.id.leaderMessage).setVisibility(View.VISIBLE);
                alertBuilder.setView(popUp);
                dialog = alertBuilder.create();

                dialog.show();
            } else {
                dialog = null;
            }



            popUp.findViewById(R.id.mDeclineUser).setOnClickListener(view1 -> {
                final DatabaseReference db;
                if (title == 1) {
                    String country = userObjectList.get(position).getLeaderCountry();
                    db = firebaseDatabase.getReference("leaderVerification/" + country + "/" + uid + "/status");
                } else if(title == 0){
                    String church = userObjectList.get(position).getChurch();
                    db = firebaseDatabase.getReference("discipleVerification/" + church + "/" + uid + "/status");
                } else {
                    db = null;
                }

                if(db != null){
                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            db.setValue("declined");

                            assert dialog != null;
                            dialog.dismiss();

                            // Todo: Check this out
                            holder.cardView.removeAllViews();
                            recyclerView.refreshDrawableState();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            });

            popUp.findViewById(R.id.mVerifyUser).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference db = firebaseDatabase.getReference("users/" + uid + "/titleVerification");

                    db.setValue("true");

                    DatabaseReference removeReqRef;
                    if (title == 1) {
                        String country = userObjectList.get(position).getLeaderCountry();
                        removeReqRef = firebaseDatabase.getReference("leaderVerification/" + country + "/" + uid);
                    } else if (title == 0) {
                        String church = userObjectList.get(position).getChurch();
                        removeReqRef = firebaseDatabase.getReference("discipleVerification/" + church + "/" + uid);
                    } else {
                        removeReqRef = null;
                    }

                    VerifiedDisciplesDB verifiedDisciplesDB = new VerifiedDisciplesDB(context);
                    verifiedDisciplesDB.addUser(userObjectList.get(position));

                    if(removeReqRef != null)
                        removeReqRef.removeValue();

                    assert dialog != null;
                    dialog.dismiss();

                    holder.cardView.removeAllViews();
                    recyclerView.refreshDrawableState();
                }
            });
        });

    }

    @Override
    public int getItemCount() {
        return userObjectList.size();
    }

    class ViewModel extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView mUserName, mFullname;
        CircleImageView displayPic;

        ViewModel(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.mUserRequest);
            mUserName = itemView.findViewById(R.id.mUserName);
            mFullname = itemView.findViewById(R.id.mFullName);
            displayPic = itemView.findViewById(R.id.circle_profile_image);
        }
    }
}
