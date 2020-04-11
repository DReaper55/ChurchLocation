package com.example.churchlocation.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.churchlocation.R;
import com.getbase.floatingactionbutton.AddFloatingActionButton;

public class Hymn extends AppCompatActivity implements View.OnClickListener {
    private TextView hymnSong;
    private AddFloatingActionButton zoomIn;
    private AddFloatingActionButton zoomOut;
    private int textSize = 20;
    private int text_diff = 3;
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;
    private static final String PREFS_NAME = "prefsFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hymn);

//        TextView hymnNumber = findViewById(R.id.hymnNumberID);
//         TextView hymnTitle = findViewById(R.id.hymnTitleID);
        hymnSong = findViewById(R.id.songID);

        zoomIn = findViewById(R.id.zoomInID);
        zoomOut = findViewById(R.id.zoomOutID);

        zoomIn.setOnClickListener(this);
        zoomOut.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            getSupportActionBar().setTitle(bundle.getString("number") + ". " + bundle.getString("title"));

//            hymnNumber.setText(bundle.getString("number"));
//            hymnTitle.setText(bundle.getString("title"));

            String number = bundle.getString("number");

            setUp(number);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // change text size
            case R.id.zoomInID:
                setZoomIn();

                break;
            case R.id.zoomOutID:
                setZoomOut();

                break;
        }
    }

    public void setZoomIn() {
        textSize = textSize + text_diff;
        hymnSong.setTextSize(textSize);

        sharedPrefs = getSharedPreferences(PREFS_NAME, 0);
        editor = sharedPrefs.edit();

        editor.putInt("textSize", textSize);
        editor.apply();
    }

    public void setZoomOut() {
        textSize = textSize - text_diff;
        hymnSong.setTextSize(textSize);

        sharedPrefs = getSharedPreferences(PREFS_NAME, 0);
        editor = sharedPrefs.edit();
        editor.putInt("textSize", textSize);
        editor.apply();
    }

    public void setUp(String song) {
        switch (song) {
            case "06":
                hymnSong.setText(R.string.o_sacred_head_06);
                break;
            case "03":
                hymnSong.setText(R.string.glory_be_to_jesus_03);
                break;
            case "02":
                hymnSong.setText(R.string.mine_eyes_02);
                break;
            case "07":
                hymnSong.setText(R.string.our_god_07);
                break;
            case "10":
                hymnSong.setText(R.string.not_a_friend_10);
                break;

            case "12":
                hymnSong.setText(R.string.hard_fighting_12);
                break;
            case "18":
                hymnSong.setText(R.string.sing_hallelujah_18);
                break;
            case "19":
                hymnSong.setText(R.string.we_praise_thee_19);
                break;
            case "20":
                hymnSong.setText(R.string.what_a_fellowship_20);
                break;
            case "17":
                hymnSong.setText(R.string.sanctuary_17);
                break;
            case "22":
                hymnSong.setText(R.string.sanctuary_17);
                break;
            case "25":
                hymnSong.setText(R.string.lead_me_to_calvary_25);
                break;
            case "26":
                hymnSong.setText(R.string.thank_you_lord_26);
                break;
            case "28":
                hymnSong.setText(R.string.to_be_the_glory_28);
                break;
            case "29":
                hymnSong.setText(R.string.rise_up_men_29);
                break;
            case "30":
                hymnSong.setText(R.string.humble_yourself_30);
                break;
            case "31":
                hymnSong.setText(R.string.we_shall_overcome_31);
                break;
            case "32":
                hymnSong.setText(R.string.blue_skies_32);
                break;
            case "34":
                hymnSong.setText(R.string.my_hope_34);
                break;
            case "39":
                hymnSong.setText(R.string.power_in_blood_39);
                break;
            case "43":
                hymnSong.setText(R.string.well_with_my_soul_43);
                break;
            case "44":
                hymnSong.setText(R.string.trust_and_obey_44);
                break;
            case "45":
                hymnSong.setText(R.string.my_redeemer_lives_45);
                break;
            case "47":
                hymnSong.setText(R.string.hail_the_power_47);
                break;
            case "50":
                hymnSong.setText(R.string.jesus_is_lord_50);
                break;
            case "51":
                hymnSong.setText(R.string.standing_on_the_promises_51);
                break;
            case "53":
                hymnSong.setText(R.string.holyx3_53);
                break;
            case "54":
                hymnSong.setText(R.string.we_come_before_thee_54);
                break;
            case "57":
                hymnSong.setText(R.string.when_i_survey_57);
                break;
            case "58":
                hymnSong.setText(R.string.we_will_glorify_58);
                break;
            case "59":
                hymnSong.setText(R.string.amazing_grace_59);
                break;
            case "60":
                hymnSong.setText(R.string.hallelujah_60);
                break;
            case "63":
                hymnSong.setText(R.string.great_is_thy_faithfulness_63);
                break;
            case "64":
                hymnSong.setText(R.string.el_shaddai_64);
                break;
            case "66":
                hymnSong.setText(R.string.to_cannan_land_66);
                break;
            case "71":
                hymnSong.setText(R.string.send_the_light_71);
                break;
            case "72":
                hymnSong.setText(R.string.soldiers_of_christ_72);
                break;
            case "73":
                hymnSong.setText(R.string.nearer_to_thee_73);
                break;
            case "75":
                hymnSong.setText(R.string.were_you_there_75);
                break;
            case "76":
                hymnSong.setText(R.string.stand_up_for_76);
                break;
            case "77":
                hymnSong.setText(R.string.have_you_been_to_jesus_77);
                break;
            case "79":
                hymnSong.setText(R.string.would_u_be_poured_79);
                break;
            case "80":
                hymnSong.setText(R.string.this_world_is_not_my_home_80);
                break;
            case "81":
                hymnSong.setText(R.string.jesus_loves_me_81);
                break;
            case "82":
                hymnSong.setText(R.string.what_can_wash_away_82);
                break;
            case "83":
                hymnSong.setText(R.string.hallelujah_praise_jehovah_83);
                break;
            case "85":
                hymnSong.setText(R.string.the_love_of_85);
                break;
            case "86":
                hymnSong.setText(R.string.let_us_break_bread_86);
                break;
            case "87":
                hymnSong.setText(R.string.purer_in_heart_87);
                break;
            case "88":
                hymnSong.setText(R.string.in_the_kingdom_88);
                break;
            case "89":
                hymnSong.setText(R.string.lord_of_all_89);
                break;
            case "91":
                hymnSong.setText(R.string.there_is_much_to_do_91);
                break;
            case "92":
                hymnSong.setText(R.string.rock_of_ages_92);
                break;
            case "93":
                hymnSong.setText(R.string.my_faith_looks_up_93);
                break;
            case "94":
                hymnSong.setText(R.string.blessed_assurance_94);
                break;
            case "95":
                hymnSong.setText(R.string.how_great_thou_art_95);
                break;
            case "97":
                hymnSong.setText(R.string.onward_christian_soldiers_97);
                break;
            case "98":
                hymnSong.setText(R.string.there_is_a_habitation_98);
                break;
            case "100":
                hymnSong.setText(R.string.were_marching_on_100);
                break;
            case "101":
                hymnSong.setText(R.string.what_a_friend_we_have_in_101);
                break;
            case "103":
                hymnSong.setText(R.string.praise_my_soul_103);
                break;
            case "105":
                hymnSong.setText(R.string.God_of_mercy_and_compassion_105);
                break;
            case "106":
                hymnSong.setText(R.string.hallelujah_what_a_savior_106);
                break;
            case "107":
                hymnSong.setText(R.string.on_a_hill_far_away_107);
                break;
            case "108":
                hymnSong.setText(R.string.follow_me_108);
                break;
            case "113":
                hymnSong.setText(R.string.breathe_on_me_113);
                break;
            case "115":
                hymnSong.setText(R.string.search_me_115);
                break;
            case "118":
                hymnSong.setText(R.string.jesus_keep_me_118);
                break;
            case "140":
                hymnSong.setText(R.string.we_are_one_140);
                break;
            case "142":
                hymnSong.setText(R.string.draw_me_nearer_142);
                break;
            case "143":
                hymnSong.setText(R.string.heavenly_sunlight_143);
                break;
            case "144":
                hymnSong.setText(R.string.take_a_look_144);
                break;
            case "145":
                hymnSong.setText(R.string.i_know_my_redeemer_145);
                break;
            case "147":
                hymnSong.setText(R.string.all_things_bright_147);
                break;
            case "149":
                hymnSong.setText(R.string.lord_speak_149);
                break;
            case "158":
                hymnSong.setText(R.string.o_master_let_me_158);
                break;
            case "160":
                hymnSong.setText(R.string.stand_in_awe_160);
                break;
            case "161":
                hymnSong.setText(R.string.lord_laid_hands_161);
                break;
            case "162":
                hymnSong.setText(R.string.lord_is_risen_2day_162);
                break;
            case "164":
                hymnSong.setText(R.string.count_your_blessings_164);
                break;
            case "165":
                hymnSong.setText(R.string.as_the_deer_165);
                break;
            case "166":
                hymnSong.setText(R.string.as_the_deer_165);
                break;
            case "168":
                hymnSong.setText(R.string.joy_to_d_world_168);
                break;
            case "169":
                hymnSong.setText(R.string.o_come_all_ye_faithful_169);
                break;
            case "170":
                hymnSong.setText(R.string.while_d_shepherds_watched_170);
                break;
            case "171":
                hymnSong.setText(R.string.angels_in_d_realms_171);
                break;
            case "172":
                hymnSong.setText(R.string.once_in_royal_dav_city_172);
                break;
            case "174":
                hymnSong.setText(R.string.hark_the_herald_174);
                break;
            case "186":
                hymnSong.setText(R.string.i_tried_and_i_tried_186);
                break;
            case "187":
                hymnSong.setText(R.string.swing_low_187);
                break;
            case "188":
                hymnSong.setText(R.string.standing_in_d_need_188);
                break;
            case "190":
                hymnSong.setText(R.string.if_i_dont_190);
                break;
            case "191":
                hymnSong.setText(R.string.i_want_jesus_191);
                break;
            case "193":
                hymnSong.setText(R.string.the_steadfast_love_193);
                break;
            case "197":
                hymnSong.setText(R.string.i_have_decided_197);
                break;
            case "208":
                hymnSong.setText(R.string.remember_me_208);
                break;
            case "209":
                hymnSong.setText(R.string.be_with_me_209);
                break;
            case "212":
                hymnSong.setText(R.string.thank_you_lord_212);
                break;
            case "215":
                hymnSong.setText(R.string.unto_thee_o_lord_215);
                break;
            case "220":
                hymnSong.setText(R.string.i_will_call_upon_220);
                break;
            case "221":
                hymnSong.setText(R.string.majesty_221);
                break;
            case "225":
                hymnSong.setText(R.string.thine_be_the_glory_225);
                break;
            case "226":
                hymnSong.setText(R.string.i_was_once_dark_226);
                break;
            case "229":
                hymnSong.setText(R.string.showers_of_blessing_229);
                break;
            case "231":
                hymnSong.setText(R.string.show_me_the_way_231);
                break;
            case "236":
                hymnSong.setText(R.string.there_a_green_hill_236);
                break;
            case "242":
                hymnSong.setText(R.string.jesus_loves_me_242);
                break;
            case "244":
                hymnSong.setText(R.string.i_love_to_tell_244);
                break;
            case "249":
                hymnSong.setText(R.string.deep_down_in_my_249);
                break;
            case "251":
                hymnSong.setText(R.string.take_the_lord_w_u_251);
                break;
            case "255":
                hymnSong.setText(R.string.to_be_like_jesus_255);
                break;
            case "257":
                hymnSong.setText(R.string.you_fight_on_257);
                break;
        }

        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, 0);
        if (preferences.contains("textSize")) {
            int message = preferences.getInt("textSize", 20);

            hymnSong.setTextSize(message);
        }
    }
}
