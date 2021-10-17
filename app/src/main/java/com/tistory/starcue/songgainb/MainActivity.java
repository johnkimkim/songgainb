package com.tistory.starcue.songgainb;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AdView adView;

    private Toolbar toolbar;
    ListView listview;
    DatabaseHandler databaseHandler;
    private SQLiteDatabase sqLiteDatabase;
    ArrayList<SongModel> mList;
    ListAdapter listAdapter;

    Button allplay;

    private long time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FCM Log", "getInstanceId failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();
                        Log.d("FCM Log", "FCM 토큰: " + token);
//                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_LONG).show();
                    }
                });

//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x;


        //admob ads
        MobileAds.initialize(this, getString(R.string.ads_id));
        adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("7AF578A575ED8BAF31AD452E5DDA9609")
                .addTestDevice("7FFE3DAE12F890AAC183AC7B92E9AFAC")
                .addTestDevice("D6B81045BE65B144F3717CAD87F0E43E")
                .addTestDevice("587B0E954BC444EFCEBDEBFE6C587952").build();
        adView.loadAd(adRequest);

//        AdSize adSize = new AdSize(AdSize.FULL_WIDTH, AdSize.AUTO_HEIGHT);
//        adView.setAdSize(adSize);

        //set toolbar
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name_main);
        toolbar.setTitleTextColor(getResources().getColor(R.color.title));
        //set navi_icon, menu
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //set listview
        listview = (ListView) findViewById(R.id.list_view);
        databaseHandler.setDB(this);
        databaseHandler = new DatabaseHandler(this);
        mList = databaseHandler.allSongList();
        listAdapter = new ListAdapter(this, mList);
        listview.setAdapter(listAdapter);
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//        listview.set(R.color.colorAccent);
        listview.setItemsCanFocus(false);
        listAdapter.notifyDataSetChanged();


        allplay = (Button) findViewById(R.id.all_play);

        sqLiteDatabase = databaseHandler.getWritableDatabase();

        sqLiteDatabase.execSQL("update songTable set position = 0");

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, PlayActivity.class);

                DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
                databaseHandler.openDatabase();
                databaseHandler.getWritableDatabase();
                SongModel model = (SongModel) parent.getItemAtPosition(position);

                int index = model.get_index();
                int pos = index + 1;
                sqLiteDatabase.execSQL("update songTable set position = 1 where indexField = " + index);

                startActivity(intent);
            }
        });

//        sqLiteDatabase = databaseHandler.getWritableDatabase();
//
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(MainActivity.this, PlayActivity.class);
//
//                SongModel model = (SongModel) parent.getItemAtPosition(position);
//                String urlField = model.get_url();
//                int index = model.get_index();
//
//                intent.putExtra("urlField", urlField);
//                intent.putExtra("index", index);
//
//                startActivity(intent);
//            }
//        });

        allplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PlayAllActivity.class);
                DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
                databaseHandler.openDatabase();
                sqLiteDatabase = databaseHandler.getWritableDatabase();
                sqLiteDatabase.execSQL("update songTable set position = 1 where _rowId_ = 1");
                startActivity(intent);

            }
        });

    }


    //activity 시작할때 한번만 호출되는 함수. menu 관련 초기설정 작업
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        if (id == R.id.nav_1) {
//            Intent intent = new Intent(MainActivity.this, Request.class);
//            startActivity(intent);
            Intent intent = new Intent(Intent.ACTION_SEND);
            String[] to = {"kmj654649@gmail.com"};
            String subject = "신청곡 요청합니다!";
            String message = "여기에 신청곡과 사연 또는 건의사항을 적어서 보내주세요^^";
            intent.putExtra(Intent.EXTRA_EMAIL, to);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, message);
            intent.setType("message/rfc822");
//            intent.setType("plaine/text");
            startActivity(Intent.createChooser(intent, "Gmail을 선택해주세요"));
        } else if (id == R.id.nav_2) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://play.google.com/store/apps/details?id=com.tistory.starcue.songgainb"));
            intent.setPackage("com.android.vending");
            startActivity(intent);
        }
//        else if (id == R.id.nav_3) {
//            Toast.makeText(this.getApplicationContext(), "준비중입니다", Toast.LENGTH_LONG).show();
//        }
        else if (id == R.id.nav_4) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.tistory.starcue.songgainb");
            startActivity(Intent.createChooser(intent, "메신저를 선택해주세요"));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (System.currentTimeMillis() - time >= 2000) {
            time = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "뒤로 버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() - time < 2000) {
            finishAffinity();
        }
    }

    //menu 눌렀을때 함수
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent1 = new Intent(this, SearchActivity.class);
        Toast toast = Toast.makeText(getApplicationContext(),"노래를 검색하세요!", Toast.LENGTH_LONG);
        toast.show();
        startActivity(intent1);

//        switch (item.getItemId()) {
//            case android.R.id.home:{
//                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                drawer.openDrawer(GravityCompat.START);
//            }
//        }

        return super.onOptionsItemSelected(item);
    }
}
