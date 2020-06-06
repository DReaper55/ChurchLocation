package com.example.churchlocation.Activity;


import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.churchlocation.Database.DatabaseHandler;
import com.example.churchlocation.Model.SearchChurchModel;
import com.example.churchlocation.R;
import com.example.churchlocation.Utils.ConnectToChurchDB;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class ChurchDetailsBottomSheet extends BottomSheetDialogFragment {
    private DatabaseHandler db;
    private ConnectToChurchDB connect = new ConnectToChurchDB();

    List<SearchChurchModel> churchModelArrayList;

    private AppBarLayout appBarLayout;
    private ScrollView scrollView;
    private ImageView imageView;

    private TextView name_of_church, church_leader_name, church_members_number,
            church_address, church_country_and_state, church_lat_long, church_details;

    ImageView call_church_leader, church_leader_dm;


    public ChurchDetailsBottomSheet() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog sheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        final View view = View.inflate(getContext(), R.layout.fragment_church_details_bottom_sheet, null);
        sheetDialog.setContentView(view);

        String title = this.getArguments().getString("church_title");
        Log.d("Actionss", title);

        db = connect.getChurches(getContext());
        churchModelArrayList = db.getAllContacts();

        BottomSheetBehavior sheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        sheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);

        TextView sheetTitle = view.findViewById(R.id.bottom_sheet_title);
        sheetTitle.setText(title);

        appBarLayout = view.findViewById(R.id.bottom_sheet_details);
        scrollView = view.findViewById(R.id.bottom_scrollview_sheet);
        imageView = view.findViewById(R.id.bottom_sheet_close);

        name_of_church = view.findViewById(R.id.bottom_name_of_church);
        church_leader_name = view.findViewById(R.id.bottom_church_leader_name);
        church_members_number = view.findViewById(R.id.bottom_church_members_number);
        church_address = view.findViewById(R.id.bottom_church_address);
        church_country_and_state = view.findViewById(R.id.bottom_church_country);
        church_lat_long = view.findViewById(R.id.bottom_church_lat_long);
        church_details = view.findViewById(R.id.bottom_church_details);

        call_church_leader = view.findViewById(R.id.bottom_call_church_leader);

        for (int i = 0; i < churchModelArrayList.size(); i++) {
            if (churchModelArrayList.get(i).getChurchName().equals(title)) {
                name_of_church.setText(churchModelArrayList.get(i).getChurchName());
                church_leader_name.setText(churchModelArrayList.get(i).getPastorName());
                church_members_number.setText(churchModelArrayList.get(i).getDisciples());
                church_address.setText(churchModelArrayList.get(i).getAddress());
//                church_details.setText(churchModelArrayList.get(i).getAbout());

                String churchCountry = churchModelArrayList.get(i).getCountry() + ", "
                        + churchModelArrayList.get(i).getState();
                church_country_and_state.setText(churchCountry);

                String churchLatandLong = churchModelArrayList.get(i).getChurchLat() + ", "
                        + churchModelArrayList.get(i).getChurchLng();
                church_lat_long.setText(churchLatandLong);

                final int finalI = i;
                call_church_leader.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + churchModelArrayList.get(finalI).getNumber()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (view.getContext().checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.CALL_PHONE}, 1);

                            } else {
                                getActivity().startActivity(intent);
                            }
                        }
                    }
                });
            }
        }


        hideView(appBarLayout);

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if(BottomSheetBehavior.STATE_EXPANDED == i){
                    showView(appBarLayout, getActionBarSize());
                    imageView.setVisibility(View.VISIBLE);
//                    hideView(scrollView);
                }

                if(BottomSheetBehavior.STATE_COLLAPSED == i){
                    showView(appBarLayout, getActionBarSize());
                    hideView(scrollView);
                    imageView.setVisibility(View.INVISIBLE);
                }

                if(BottomSheetBehavior.STATE_HIDDEN == i){
                    dismiss();
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return sheetDialog;
    }

    private void hideView(View appBarLayout) {
        ViewGroup.LayoutParams params = appBarLayout.getLayoutParams();
        params.height = 0;
        appBarLayout.setLayoutParams(params);
    }

    private void showView(View appBarLayout, int actionBarSize) {
        ViewGroup.LayoutParams params = appBarLayout.getLayoutParams();
        params.height = actionBarSize;
        appBarLayout.setLayoutParams(params);
    }

    private int getActionBarSize() {
        final TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(new int[]{
           android.R.attr.actionBarSize
        });
        return (int) typedArray.getDimension(0,0);
    }

}
