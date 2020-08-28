package com.example.churchlocation.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.churchlocation.Adapter.ChurchHymnAdapter;
import com.example.churchlocation.Model.ListItem;
import com.example.churchlocation.R;

import java.util.ArrayList;
import java.util.List;

public class HymnFragment extends Fragment {
    private List<ListItem> listItems;
    private  AlertDialog.Builder alertBuilder;
    private RecyclerView.Adapter adapter;
    private Filterable filterable;

    private Context ctx;
    private View view;

    public HymnFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.toolbar_name, container, false);
        ctx = root.getContext();
        view = root.getRootView();

        setUp();
        recycleSetUp();

        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    public void recycleSetUp(){
        RecyclerView recyclerView = view.findViewById(R.id.myRecycleID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ctx));

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        adapter = new ChurchHymnAdapter(ctx, listItems);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.hymn_fragment_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.searchID);

        SearchView searchView;
        searchView = (SearchView) searchItem.getActionView();

        // change the keyboard search button
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                // to get the filter methods
                filterable = (Filterable) adapter;
                filterable.getFilter().filter(s);

                return false;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // calling a function when a menu item is clicked
        int menu_id = item.getItemId();

        switch (menu_id){
            case R.id.menuBarID:
                Toast.makeText(ctx, "No command yet", Toast.LENGTH_SHORT).show();
                break;
            case R.id.aboutUsID:
                alertBuilder = new AlertDialog.Builder(ctx);
                alertBuilder.setTitle(R.string.app_name);
                alertBuilder.setMessage("Your's truly: D.Reaper");
                alertBuilder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = alertBuilder.create();
                dialog.show();

                break;

        }


        return super.onOptionsItemSelected(item);
    }


    /*@Override
    public void onBackPressed() {
        // Recursive function to prevent the main activity from closing

        if(getSupportFragmentManager().getBackStackEntryCount() > 0){
            this.onBackPressed();
        } else{
            alertBuilder = new AlertDialog.Builder(HymnFragment.this);
            alertBuilder.setTitle(R.string.exit);
            alertBuilder.setMessage(R.string.decide_leave);
            alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    HymnFragment.this.finish();
                }
            });

            alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog dialog = alertBuilder.create();
            dialog.show();
        }
    }*/

    public void setUp(){
        listItems = new ArrayList<>();

        ListItem song2 = new ListItem("02", "Mine Eyes Have Seen"); listItems.add(song2);
        ListItem song3 = new ListItem("03", "Glory Be To Jesus"); listItems.add(song3);
        ListItem song6 = new ListItem("06", "O Sacred Head"); listItems.add(song6);
        ListItem song7 = new ListItem("07", "Our God He is Alive"); listItems.add(song7);
        ListItem song10 = new ListItem("10", "There's Not A Friend"); listItems.add(song10);
        ListItem song12 = new ListItem("12", "Hard Fighting Soldier"); listItems.add(song12);
        ListItem song17 = new ListItem("17", "Sanctuary"); listItems.add(song17);
        ListItem song18 = new ListItem("18", "Sing Hallelujah to the Lord"); listItems.add(song18);
        ListItem song19 = new ListItem("19", "We Praise Thee, O God"); listItems.add(song19);
        ListItem song20 = new ListItem("20", "What a Fellowship"); listItems.add(song20);
        ListItem song22 = new ListItem("22", "Just as I Am"); listItems.add(song22);
        ListItem song25 = new ListItem("25", "Lead Me To Calvary"); listItems.add(song25);
        ListItem song26 = new ListItem("26", "Thank You, Lord"); listItems.add(song26);
        ListItem song28 = new ListItem("28", "To God Be the Glory"); listItems.add(song28);
        ListItem song29 = new ListItem("29", "Rise Up O Men"); listItems.add(song29);
        ListItem song30 = new ListItem("30", "Humble Yourself"); listItems.add(song30);
        ListItem song31 = new ListItem("31", "We Shall Overcome"); listItems.add(song31);
        ListItem song32 = new ListItem("32", "Blue Skies and Rainbow"); listItems.add(song32);
        ListItem song34 = new ListItem("34", "My Hope is Built"); listItems.add(song34);
        ListItem song39 = new ListItem("39", "There's Power in the Blood"); listItems.add(song39);
        ListItem song43 = new ListItem("43", "It is Well With My Soul"); listItems.add(song43);
        ListItem song44 = new ListItem("44", "Trust and Obey"); listItems.add(song44);
        ListItem song45 = new ListItem("45", "My Redeemer Lives"); listItems.add(song45);
        ListItem song47 = new ListItem("47", "All Hail the Power"); listItems.add(song47);
        ListItem song50 = new ListItem("50", "Jesus is Lord"); listItems.add(song50);
        ListItem song51 = new ListItem("51", "Standing on the Promises"); listItems.add(song51);
        ListItem song53 = new ListItem("53", "Holy, Holy, Holy"); listItems.add(song53);
        ListItem song54 = new ListItem("54", "We Come Before Thee"); listItems.add(song54);
        ListItem song57 = new ListItem("57", "When I Survey"); listItems.add(song57);
        ListItem song58 = new ListItem("58", "We Will Glorify"); listItems.add(song58);
        ListItem song59 = new ListItem("59", "Amazing Grace Traditional"); listItems.add(song59);
        ListItem song60 = new ListItem("60", "Hallelujah"); listItems.add(song60);
        ListItem song63 = new ListItem("63", "Great is Thy Faithfulness"); listItems.add(song63);
        ListItem song64 = new ListItem("64", "El Shaddai"); listItems.add(song64);
        ListItem song66 = new ListItem("66", "To Canaan's Land"); listItems.add(song66);
        ListItem song71 = new ListItem("71", "Send the Light"); listItems.add(song71);
        ListItem song72 = new ListItem("72", "Soldier's of Christ"); listItems.add(song72);
        ListItem song73 = new ListItem("73", "Nearer My God to Thee"); listItems.add(song73);
        ListItem song75 = new ListItem("75", "Were You There"); listItems.add(song75);
        ListItem song76 = new ListItem("76", "Stand Up For Jesus"); listItems.add(song76);
        ListItem song77 = new ListItem("77", "Have You Been to Jesus"); listItems.add(song77);
        ListItem song79 = new ListItem("79", "Would You Be Poured"); listItems.add(song79);
        ListItem song80 = new ListItem("80", "This World is Not My Home"); listItems.add(song80);
        ListItem song81 = new ListItem("81", "Jesus Loves Me"); listItems.add(song81);
        ListItem song82 = new ListItem("82", "What Can Wash Away My Sins"); listItems.add(song82);
        ListItem song83 = new ListItem("83", "Hallelujah Praise Jehovah"); listItems.add(song83);
        ListItem song85 = new ListItem("85", "The Love of God"); listItems.add(song85);
        ListItem song86 = new ListItem("86", "Let Us Break Bread Together"); listItems.add(song86);
        ListItem song87 = new ListItem("87", "Purer in Heart"); listItems.add(song87);
        ListItem song88 = new ListItem("88", "In the Kingdom"); listItems.add(song88);
        ListItem song89 = new ListItem("89", "Lord of All"); listItems.add(song89);
        ListItem song91 = new ListItem("91", "There is Much to Do"); listItems.add(song91);
        ListItem song92 = new ListItem("92", "Rock of Ages"); listItems.add(song92);
        ListItem song93 = new ListItem("93", "My Faith Looks Up to Thee"); listItems.add(song93);
        ListItem song94 = new ListItem("94", "Blessed Assurance"); listItems.add(song94);
        ListItem song95 = new ListItem("95", "How Great Thou Art"); listItems.add(song95);
        ListItem song97 = new ListItem("97", "Onward Christian Soldiers"); listItems.add(song97);
        ListItem song98 = new ListItem("98", "There is a Habitation"); listItems.add(song98);
        ListItem song100 = new ListItem("100", "We're Marching on to Zion"); listItems.add(song100);
        ListItem song101 = new ListItem("101", "What A Friend We Have In Jesus"); listItems.add(song101);
        ListItem song103 = new ListItem("103", "Praise My Soul"); listItems.add(song103);
        ListItem song105 = new ListItem("105", "God of Mercy and Compassion"); listItems.add(song105);
        ListItem song106 = new ListItem("106", "Hallelujah What A Savior"); listItems.add(song106);
        ListItem song107 = new ListItem("107", "On A Hill Far Away"); listItems.add(song107);
        ListItem song108 = new ListItem("108", "Follow Me"); listItems.add(song108);
        ListItem song113 = new ListItem("113", "Breathe On Me"); listItems.add(song113);
        ListItem song115 = new ListItem("115", "Search Me"); listItems.add(song115);
        ListItem song118 = new ListItem("118", "Jesus, Keep Me Near The Cross"); listItems.add(song118);
        ListItem song140 = new ListItem("140", "We Are One In The Spirit"); listItems.add(song140);
        ListItem song142 = new ListItem("142", "Draw Me Nearer"); listItems.add(song142);
        ListItem song143 = new ListItem("143", "Heavenly Sunlight"); listItems.add(song143);
        ListItem song144 = new ListItem("144", "Take a Look on the Mountain"); listItems.add(song144);
        ListItem song145 = new ListItem("145", "I Know My Redeemer Lives"); listItems.add(song145);
        ListItem song147 = new ListItem("147", "All Things Bright and Beautiful"); listItems.add(song147);
        ListItem song149 = new ListItem("149", "Lord, Speak to Me"); listItems.add(song149);
        ListItem song158 = new ListItem("158", "O Master, Let Me Walk With Thee"); listItems.add(song158);
        ListItem song160 = new ListItem("160", "Stand in Awe"); listItems.add(song160);
        ListItem song161 = new ListItem("161", "The Lord has Laid His Hands on Me"); listItems.add(song161);
        ListItem song162 = new ListItem("162", "Christ, the Lord, is Risen Today"); listItems.add(song162);
        ListItem song164 = new ListItem("164", "Count Your Blessings"); listItems.add(song164);
        ListItem song165 = new ListItem("165", "As The Deer"); listItems.add(song165);
        ListItem song166 = new ListItem("166", "Praise to the Lord"); listItems.add(song166);
        ListItem song168 = new ListItem("168", "Joy to the World"); listItems.add(song168);
        ListItem song169 = new ListItem("169", "O Come All Ye Faithful"); listItems.add(song169);
        ListItem song170 = new ListItem("170", "While Shepherds Watched"); listItems.add(song170);
        ListItem song171 = new ListItem("171", "Angels in the Realms"); listItems.add(song171);
        ListItem song172 = new ListItem("172", "Once in Royal David's City"); listItems.add(song172);
        ListItem song174 = new ListItem("174", "Hark The Herald"); listItems.add(song174);
        ListItem song186 = new ListItem("186", "I Tried And I Tried"); listItems.add(song186);
        ListItem song187 = new ListItem("187", "Swing Low, Sweet Chariot"); listItems.add(song187);
        ListItem song188 = new ListItem("188", "Standing In The Need"); listItems.add(song188);
        ListItem song190 = new ListItem("190", "If I Don't"); listItems.add(song190);
        ListItem song191 = new ListItem("191", "I Want Jesus To Walk With Me"); listItems.add(song191);
        ListItem song193 = new ListItem("193", "The Steadfast Love of The Lord"); listItems.add(song193);
        ListItem song197 = new ListItem("197", "I Have Decided"); listItems.add(song197);
        ListItem song208 = new ListItem("208", "Remember Me"); listItems.add(song208);
        ListItem song209 = new ListItem("209", "Be With Me"); listItems.add(song209);
        ListItem song212 = new ListItem("212", "Thank You Lord"); listItems.add(song212);
        ListItem song215 = new ListItem("215", "Unto Thee O Lord"); listItems.add(song215);
        ListItem song220 = new ListItem("220", "I Will Call Upon The Lord"); listItems.add(song220);
        ListItem song221 = new ListItem("221", "Majesty"); listItems.add(song221);
        ListItem song225 = new ListItem("225", "Thine Be The Glory"); listItems.add(song225);
        ListItem song226 = new ListItem("226", "I Was Once In Darkness"); listItems.add(song226);
        ListItem song229 = new ListItem("229", "Showers Of Blessings"); listItems.add(song229);
        ListItem song231 = new ListItem("231", "Show Me The Way"); listItems.add(song231);
        ListItem song236 = new ListItem("236", "There Is A Green Hill"); listItems.add(song236);
        ListItem song242 = new ListItem("242", "Jesus Loves Me"); listItems.add(song242);
        ListItem song244 = new ListItem("244", "I Love To Tell The Story"); listItems.add(song244);
        ListItem song249 = new ListItem("249", "Deep Down In My Heart"); listItems.add(song249);
        ListItem song251 = new ListItem("251", "Take The Lord With You"); listItems.add(song251);
        ListItem song255 = new ListItem("255", "To Be Like Jesus"); listItems.add(song255);
        ListItem song257 = new ListItem("257", "You Fight On"); listItems.add(song257);
        ListItem song263 = new ListItem("263", "Lift Up Your Heads"); listItems.add(song263);
        ListItem song264 = new ListItem("264", "Sing For Joy"); listItems.add(song264);
        ListItem song265 = new ListItem("265", "Lord You Are Good"); listItems.add(song265);
        ListItem song266 = new ListItem("266", "At The Foot Of The Cross"); listItems.add(song266);
        ListItem song267 = new ListItem("267", "Mi Corazon"); listItems.add(song267);
        ListItem song268 = new ListItem("268", "God Is Good"); listItems.add(song268);
        ListItem song269 = new ListItem("269", "Yor Steadfast Love"); listItems.add(song269);
        ListItem song270 = new ListItem("270", "All The Earth"); listItems.add(song270);
        ListItem song272 = new ListItem("272", "Be It Unto Me"); listItems.add(song272);
        ListItem song273 = new ListItem("273", "Glory be to Jesus"); listItems.add(song273);
        ListItem song274 = new ListItem("274", "Thank you Lord"); listItems.add(song274);
        ListItem song275 = new ListItem("275", "I Just Want To Be"); listItems.add(song275);
        ListItem song276 = new ListItem("276", "Here We Are"); listItems.add(song276);
        ListItem song278 = new ListItem("278", "Kyrie Eleison, Lord Have Mercy"); listItems.add(song278);
    }
}
