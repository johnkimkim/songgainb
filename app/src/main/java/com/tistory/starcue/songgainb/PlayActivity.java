package com.tistory.starcue.songgainb;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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


import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity {

    private YouTubePlayerSupportFragment youTubePlayerFragment;
    private YouTubePlayer youTubePlayer;

    private YouTubePlayerView youTubePlayerView;

    DatabaseHandler databaseHandler;
    ListAdapter listAdapter;
    ArrayList<SongModel> mList;
    private SQLiteDatabase sqLiteDatabase;
    ListView listview;

    ImageButton provbtn;
    ImageButton nextbtn;
    ImageButton sandp;
    Button back;

    String url;

    TextView textView;

//    int autoindex;
    int index;
    int newindex;
//    int indexmn;

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
                Intent intent = new Intent(PlayActivity.this, MainActivity.class);
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
                DatabaseHandler databaseHandler = new DatabaseHandler(PlayActivity.this);
                databaseHandler.openDatabase();
                sqLiteDatabase = databaseHandler.getWritableDatabase();
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
                    sqLiteDatabase = databaseHandler.getWritableDatabase();

                    Cursor cursorcount = sqLiteDatabase.rawQuery("select * from songTable", null);
                    int cnt = cursorcount.getCount();

                    Intent intent = new Intent(getApplicationContext(), PlayActivity1.class);
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

//                if (state == PlayerConstants.PlayerState.PLAYING) {
//                    sandp.setEnabled(true);
//                    sandp.setImageResource(R.drawable.stop_true);
//                }
//                if (state == PlayerConstants.PlayerState.PAUSED) {
//                    sandp.setEnabled(true);
//                    sandp.setImageResource(R.drawable.play_true);
//                }

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

                        Intent intent = new Intent(getApplicationContext(), PlayActivity1.class);
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

                        Intent intent = new Intent(getApplicationContext(), PlayActivity1.class);
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

//        initializeYoutubePlayer();

    }

//    private void initializeYoutubePlayer() {
//
//        youTubePlayerFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.playView);
//
//        if (youTubePlayerFragment == null)
//            return;
//
//        youTubePlayerFragment.initialize(Constants.YOUTUBE_KEY, new YouTubePlayer.OnInitializedListener() {
//
//            @Override
//            public void onInitializationSuccess(final YouTubePlayer.Provider provider, YouTubePlayer player,
//                                                final boolean wasRestored) {
//
//                player.setPlayerStateChangeListener(playerStateChangeListener);
//                player.setPlaybackEventListener(playbackEventListener);
//                player.setPlaylistEventListener(playlistEventListener);
//
////                sqLiteDatabase = databaseHandler.getWritableDatabase();
//
//
//
//                if (!wasRestored) {
//                    youTubePlayer = player;
//                    Intent intent = getIntent();
//                    index = intent.getExtras().getInt("index");
////                    int indexmn = index - 1;
////                    String urlField = intent.getExtras().getString("urlField");
////                    SongModel model = (SongModel) listAdapter.getItem(indexmn);
////                    url = model.get_url();
////                    youTubePlayer.loadVideo(url);
//                    youTubePlayer.loadVideos(databaseHandler.getrandom(), index - 1, 0);
//                    //set the player style default
//                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
//                    //cue the 1st video by default
////                    youTubePlayer.cueVideo(youtubeVideoArrayList.get(0));
//
//                    provbtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if (youTubePlayer.hasPrevious()) {
//                                youTubePlayer.previous();
//                            }
//                            if (!youTubePlayer.hasPrevious()) {
//                                provbtn.setEnabled(false);
//                                provbtn.setImageResource(R.drawable.prev_enabled_false);
//                            }
//                        }
//                    });
//
//                    sandp.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if (youTubePlayer.isPlaying()) {
//                                youTubePlayer.pause();
//                                sandp.setImageResource(R.drawable.play_true);
//                            } else {
//                                youTubePlayer.play();
//                                sandp.setImageResource(R.drawable.stop_true);
//                            }
//                        }
//                    });
//
//
//                    nextbtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
////                            String urlmore;
////                            int indexmore = indexmn;
////                            indexmore++;
////                            int indexbtn = indexmore;
////                            SongModel model = (SongModel) listAdapter.getItem(indexbtn);
////                            urlmore = model.get_url();
////                            youTubePlayer.loadVideo(urlmore);
//
//                        if (youTubePlayer.hasNext()) {
//                            youTubePlayer.next();
//                        }
//                        if (!youTubePlayer.hasNext()) {
//                            nextbtn.setEnabled(false);
//                            nextbtn.setImageResource(R.drawable.next_enabled_false);
//                        }
//                        }
//                    });
//
//                }
//
//            }
//            @Override
//            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
//
//                //print or show error if initialization failed
////                Log.e(TAG, "Youtube Player View initialization failed");
//            }
//        });
//    }
//
//    public int autoindex(int index, int newindex) {
//        Intent intent = getIntent();
//        index = intent.getExtras().getInt("index");
//        newindex = intent.getExtras().getInt("newindex");
//        if (listview.isPressed()) {
//            return newindex;
//        } else {
//            return index;
//        }
//    }
//
//    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
//        @Override
//        public void onLoading() {
//            provbtn.setEnabled(false);
//            provbtn.setImageResource(R.drawable.prev_enabled_false);
//            sandp.setEnabled(false);
//            sandp.setImageResource(R.drawable.stop_false);
//            nextbtn.setEnabled(false);
//            nextbtn.setImageResource(R.drawable.next_enabled_false);
//        }
//
//        @Override
//        public void onLoaded(String s) {
//            provbtn.setEnabled(true);
//            provbtn.setImageResource(R.drawable.prev_enabled_true);
//            sandp.setEnabled(true);
//            sandp.setImageResource(R.drawable.stop_true);
//            nextbtn.setEnabled(true);
//            nextbtn.setImageResource(R.drawable.next_enabled_true);
//        }
//
//        @Override
//        public void onAdStarted() {
//
//        }
//
//        @Override
//        public void onVideoStarted() {
//
//        }
//
//        @Override
//        public void onVideoEnded() {
//
//        }
//
//        @Override
//        public void onError(YouTubePlayer.ErrorReason errorReason) {
//
//        }
//    };
//
//    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
//        @Override
//        public void onPlaying() {
//
//        }
//
//        @Override
//        public void onPaused() {
//
//        }
//
//        @Override
//        public void onStopped() {
//
//        }
//
//        @Override
//        public void onBuffering(boolean b) {
//
//        }
//
//        @Override
//        public void onSeekTo(int i) {
//
//        }
//    };
//
//    private YouTubePlayer.PlaylistEventListener playlistEventListener = new YouTubePlayer.PlaylistEventListener() {
//        @Override
//        public void onPrevious() {
//
//        }
//
//        @Override
//        public void onNext() {
//
//        }
//
//        @Override
//        public void onPlaylistEnded() {
//
//        }
//    };


    //out layer click block
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        sqLiteDatabase.execSQL("update songTable set position = 0");
        Intent intent = new Intent(PlayActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        super.onBackPressed();
    }

    //    //back button in toolbar
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == android.R.id.home) {
//            this.finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
