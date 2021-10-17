package com.tistory.starcue.songgainb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;

public class PlayActivity1 extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;

    DatabaseHandler databaseHandler;
    private SQLiteDatabase sqLiteDatabase;

    ListView listview;

    ImageButton provbtn;
    ImageButton nextbtn;
    ImageButton sandp;
    Button back;
    int index;
    int newindex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_activity);


        Dialog dialog = new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;

        //set dialog size
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = (int) (display.getWidth() * 1);
        int height = (int) (display.getHeight() * 0.2);
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        getWindow().getAttributes().width = width;
//        getWindow().getAttributes().height = height;


        //set UI


        //get intent data
//        Intent intent = getIntent();
//        url = intent.getStringExtra("urlField");


        //setToolbar
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        listview = (ListView) findViewById(R.id.play_activity_listview);
//        //set listview
        databaseHandler.setDB(this);
        databaseHandler = new DatabaseHandler(this);
        databaseHandler.openDatabase();
        sqLiteDatabase = databaseHandler.getWritableDatabase();
//        mList = databaseHandler.allSongList();
//        listAdapter = new ListAdapter(this, mList);
//        listview.setAdapter(listAdapter);
//        listview.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
////        listview.set(R.color.colorAccent);
//        listview.setItemsCanFocus(false);
//        listAdapter.notifyDataSetChanged();


//        sqLiteDatabase = databaseHandler.getWritableDatabase();

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.playView);
        getLifecycle().addObserver(youTubePlayerView);

        provbtn = (ImageButton) findViewById(R.id.provbtn);
        nextbtn = (ImageButton) findViewById(R.id.nextbtn);
        sandp = (ImageButton) findViewById(R.id.stop_and_play);
        back = (Button) findViewById(R.id.backbtn);

        provbtn.setEnabled(false);
        nextbtn.setEnabled(false);
        sandp.setEnabled(false);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqLiteDatabase.execSQL("update songTable set position = 0");
                Intent intent = new Intent(PlayActivity1.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onApiChange(@NotNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                super.onApiChange(youTubePlayer);
            }

            @Override
            public void onCurrentSecond(@NotNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, float second) {
                super.onCurrentSecond(youTubePlayer, second);
            }

            @Override
            public void onError(@NotNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlayerError error) {
                super.onError(youTubePlayer, error);
            }

            @Override
            public void onPlaybackQualityChange(@NotNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlaybackQuality playbackQuality) {
                super.onPlaybackQualityChange(youTubePlayer, playbackQuality);
            }

            @Override
            public void onPlaybackRateChange(@NotNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlaybackRate playbackRate) {
                super.onPlaybackRateChange(youTubePlayer, playbackRate);
            }

            @Override
            public void onReady(@NotNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                DatabaseHandler databaseHandler = new DatabaseHandler(PlayActivity1.this);
                databaseHandler.openDatabase();
                sqLiteDatabase = databaseHandler.getWritableDatabase();

                sandp.setImageResource(R.drawable.stop_true);
                Cursor cursor = sqLiteDatabase.rawQuery("select urlField from songTable where position = 1", null);
                cursor.moveToFirst();
                String url = cursor.getString(0);
                youTubePlayer.loadVideo(url, 0);

//                provbtn.setEnabled(true);
//                provbtn.setImageResource(R.drawable.prev_enabled_true);
//                nextbtn.setEnabled(true);
//                nextbtn.setImageResource(R.drawable.next_enabled_true);
//                sandp.setEnabled(true);
//                sandp.setImageResource(R.drawable.stop_true);
                Log.d("테스트테스스테스트테스트", url);
                super.onReady(youTubePlayer);
            }

            @Override
            public void onStateChange(@NotNull final com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, @NotNull final PlayerConstants.PlayerState state) {
                if (state == PlayerConstants.PlayerState.ENDED) {
                    DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
                    databaseHandler.openDatabase();
                    databaseHandler.getWritableDatabase();

                    Cursor cursorcount = sqLiteDatabase.rawQuery("select * from songTable", null);
                    int cnt = cursorcount.getCount();

                    Cursor cursor = sqLiteDatabase.rawQuery("select indexField from songTable where position = 1", null);
                    cursor.moveToFirst();
                    int i = cursor.getInt(0);
                    if (i < cnt && i > 0) {
                        sqLiteDatabase.execSQL("update songTable set position = 0 where indexField = " + i);
                        int nextpos = i + 1;
                        sqLiteDatabase.execSQL("update songTable set position = 1 where indexField = " + nextpos);
                        Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                    }
                    if (i >= cnt) {
                        sqLiteDatabase.execSQL("update songTable set position = 0");
                        Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent1);
                        overridePendingTransition(0,0);
                    }
                }

                if (state == PlayerConstants.PlayerState.PLAYING) {
                    sandp.setEnabled(true);
                    sandp.setImageResource(R.drawable.stop_true);
                } else if (state == PlayerConstants.PlayerState.PAUSED) {
                    sandp.setImageResource(R.drawable.play_true);
                    sandp.setEnabled(true);
                } else {
                    sandp.setEnabled(false);
                    sandp.setImageResource(R.drawable.stop_false);
                }

                sandp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (state == PlayerConstants.PlayerState.PLAYING) {
                            youTubePlayer.pause();
                        }
                        if (state == PlayerConstants.PlayerState.PAUSED) {
                            youTubePlayer.play();
                        }
                    }
                });

                if (state == PlayerConstants.PlayerState.PAUSED || state == PlayerConstants.PlayerState.PLAYING) {
                    nextbtn.setEnabled(true);
                    nextbtn.setImageResource(R.drawable.next_enabled_true);
                } else {
                    nextbtn.setEnabled(false);
                    nextbtn.setImageResource(R.drawable.next_enabled_false);
                }

                nextbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
                        databaseHandler.openDatabase();
                        sqLiteDatabase = databaseHandler.getWritableDatabase();

                        Cursor cursorcount = sqLiteDatabase.rawQuery("select * from songTable", null);
                        int cnt = cursorcount.getCount();

                        Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
                        Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                        Cursor cursor = sqLiteDatabase.rawQuery("select indexField from songTable where position = 1", null);
                        cursor.moveToFirst();
                        int i = cursor.getInt(0);
                        if (i < cnt && i > 0) {
                            sqLiteDatabase.execSQL("update songTable set position = 0 where indexField = " + i);
                            int nextpos = i + 1;
                            sqLiteDatabase.execSQL("update songTable set position = 1 where indexField = " + nextpos);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                        }
                        if (i >= cnt) {
                            sqLiteDatabase.execSQL("update songTable set position = 0");
                            startActivity(intent1);
                            overridePendingTransition(0,0);
                        }
                    }
                });

                if (state == PlayerConstants.PlayerState.PAUSED || state == PlayerConstants.PlayerState.PLAYING) {
                    provbtn.setEnabled(true);
                    provbtn.setImageResource(R.drawable.prev_enabled_true);
                } else {
                    provbtn.setEnabled(false);
                    provbtn.setImageResource(R.drawable.prev_enabled_false);
                }

                provbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
                        databaseHandler.openDatabase();
                        sqLiteDatabase = databaseHandler.getWritableDatabase();

                        Cursor cursorcount = sqLiteDatabase.rawQuery("select * from songTable", null);
                        int cnt = cursorcount.getCount();

                        Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
                        Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                        Cursor cursor = sqLiteDatabase.rawQuery("select indexField from songTable where position = 1", null);
                        cursor.moveToFirst();
                        int i = cursor.getInt(0);
                        if (i < cnt && i > 0) {
                            sqLiteDatabase.execSQL("update songTable set position = 0 where indexField = " + i);
                            int nextpos = i - 1;
                            sqLiteDatabase.execSQL("update songTable set position = 1 where indexField = " + nextpos);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                        }
                        if (i >= cnt) {
                            sqLiteDatabase.execSQL("update songTable set position = 0");
                            startActivity(intent1);
                            overridePendingTransition(0,0);
                        }
                    }
                });
                super.onStateChange(youTubePlayer, state);
            }

            @Override
            public void onVideoDuration(@NotNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, float duration) {
                super.onVideoDuration(youTubePlayer, duration);
            }

            @Override
            public void onVideoId(@NotNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, @NotNull String videoId) {
                super.onVideoId(youTubePlayer, videoId);
            }

            @Override
            public void onVideoLoadedFraction(@NotNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, float loadedFraction) {
                super.onVideoLoadedFraction(youTubePlayer, loadedFraction);
            }
        });


    }



    @Override
    public void onBackPressed() {
        sqLiteDatabase.execSQL("update songTable set position = 0");
        Intent intent = new Intent(PlayActivity1.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        super.onBackPressed();
    }

    //out layer click block
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }
}