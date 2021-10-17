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

import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class PlayAllActivity extends AppCompatActivity{

    private YouTubePlayerSupportFragment youTubePlayerFragment;
    private YouTubePlayer youTubePlayer;
    private DatabaseHandler databaseHandler;
    private SQLiteDatabase sqLiteDatabase;

    private YouTubePlayerView youTubePlayerView;

    String url;

    TextView titleText;

    ImageButton provbtn;
    ImageButton nextbtn;
    ImageButton sandp;
    Button back;

    private int mVideosIndex = 0;
    private boolean mLoop = false;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_all_activity);

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

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.playView);

        provbtn = (ImageButton) findViewById(R.id.provbtn);
        nextbtn = (ImageButton) findViewById(R.id.nextbtn);
        sandp = (ImageButton) findViewById(R.id.stop_and_play);
        back = (Button) findViewById(R.id.backbtn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
                DatabaseHandler databaseHandler = new DatabaseHandler(PlayAllActivity.this);
                databaseHandler.openDatabase();
                sqLiteDatabase = databaseHandler.getWritableDatabase();

                Cursor cursor = sqLiteDatabase.rawQuery("select urlField from songTable order by random() limit 1", null);
                cursor.moveToFirst();
                String url = cursor.getString(0);
                youTubePlayer.loadVideo(url, 0);
                super.onReady(youTubePlayer);
            }

            @Override
            public void onStateChange(@NotNull final com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, @NotNull final PlayerConstants.PlayerState state) {
                if (state == PlayerConstants.PlayerState.ENDED) {
                    Intent intent = new Intent(PlayAllActivity.this, PlayAllActivity1.class);
                    startActivity(intent);
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
                        Intent intent = new Intent(PlayAllActivity.this, PlayAllActivity1.class);
                        startActivity(intent);
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
                        Intent intent = new Intent(PlayAllActivity.this, PlayAllActivity1.class);
                        startActivity(intent);
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

//        youTubePlayerFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.playView);
//        youTubePlayerFragment.initialize(Constants.YOUTUBE_KEY, PlayAllActivity.this);
//
//        initializeYoutubePlayer();

    }

    private void initializeYoutubePlayer() {

        if (youTubePlayerFragment == null)
            return;

        databaseHandler = new DatabaseHandler(PlayAllActivity.this);
        sqLiteDatabase = databaseHandler.getReadableDatabase();
    }

//    @Override
//    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
//        player.setPlayerStateChangeListener(playerStateChangeListener);
//        player.setPlaybackEventListener(playbackEventListener);
//        player.setPlaylistEventListener(playlistEventListener);
//
//        sqLiteDatabase = databaseHandler.getWritableDatabase();
//
//        Random random = new Random();
//        int i = random.nextInt(94);
//
//        if (!wasRestored) {
//            youTubePlayer = player;
//            youTubePlayer.loadVideos(databaseHandler.getrandom(), i, 0);
////            youTubePlayer.loadPlaylist("PL6FJ8jMWLUS6eWrP7_pUFxBArPeDUQDWh", i, 0);
//
//            //set the player style default
//            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
//
//            //cue the 1st video by default
////                    youTubePlayer.cueVideo(youtubeVideoArrayList.get(0));
//
//        }
//
//
////        titleText = (TextView) findViewById(R.id.title_text);
////        String sql = "SELECT * FROM songTable WHERE indexField =" + i;
////        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
////        cursor.moveToFirst();
////        while (!cursor.isAfterLast()) {
////            String song_name = cursor.getString(2);
////            titleText.setText(song_name);
////            cursor.moveToNext();
////        }
////        cursor.close();
//
//
//        //control button
//        provbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (youTubePlayer.hasPrevious()) {
//                    youTubePlayer.previous();
//                }
//                if (!youTubePlayer.hasPrevious()) {
//                    provbtn.setEnabled(false);
//                    provbtn.setImageResource(R.drawable.prev_enabled_false);
//                }
//            }
//        });
//
//        sandp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (youTubePlayer.isPlaying()) {
//                    youTubePlayer.pause();
//                    sandp.setImageResource(R.drawable.play_true);
//                } else {
//                    youTubePlayer.play();
//                    sandp.setImageResource(R.drawable.stop_true);
//                }
//            }
//        });
//
//
//        nextbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (youTubePlayer.hasNext()) {
//                    youTubePlayer.next();
//                }
//                if (!youTubePlayer.hasNext()) {
//                    nextbtn.setEnabled(false);
//                    nextbtn.setImageResource(R.drawable.next_enabled_false);
//                }
//            }
//        });
//    }
//
//    @Override
//    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
////                //print or show error if initialization failed
////                Log.e(TAG, "Youtube Player View initialization failed");
//    }
//
//
////    private void initializeYoutubePlayer() {
////
////        if (youTubePlayerFragment == null)
////            return;
////
////        databaseHandler = new DatabaseHandler(PlayAllActivity.this);
////        sqLiteDatabase = databaseHandler.getReadableDatabase();
////
////        youTubePlayerFragment.initialize(Constants.YOUTUBE_KEY, new YouTubePlayer.OnInitializedListener() {
////
////            @Override
////            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
////                                                boolean wasRestored) {
////
//////                youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
//////                youTubePlayer.setPlaybackEventListener(playbackEventListener);
////
////
////
////                if (!wasRestored) {
////                    youTubePlayer = player;
////                    youTubePlayer.loadVideos(databaseHandler.getrandom());
////
////                    //set the player style default
////                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
////
////                    //cue the 1st video by default
//////                    youTubePlayer.cueVideo(youtubeVideoArrayList.get(0));
////
////                }
////
////                //control button
////                Button before = (Button) findViewById(R.id.before);
////                before.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View view) {
////                        if (youTubePlayer.hasPrevious()) {
////                            youTubePlayer.previous();
////                        }
////                        if (!youTubePlayer.hasPrevious()) {
////                            Toast.makeText(getApplicationContext(), "이전곡 없음", Toast.LENGTH_LONG).show();
////                        }
////                    }
////                });
////
////                final Button next = (Button) findViewById(R.id.next);
////                next.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View view) {
////                        if (youTubePlayer.hasNext()) {
////                            youTubePlayer.next();
////                        }
////                        if (!youTubePlayer.hasNext()) {
////                            Toast.makeText(getApplicationContext(), "다음곡 없음", Toast.LENGTH_LONG).show();
////                        }
////                    }
////                });
////
////            }
////
////
////
////
////
////            @Override
////            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
////
////                //print or show error if initialization failed
//////                Log.e(TAG, "Youtube Player View initialization failed");
////            }
////
////        });
////
////
////
////    }
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
        Intent intent = new Intent(PlayAllActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        super.onBackPressed();
    }

    public int getVideosIndex() {
        return mVideosIndex;
    }

}
