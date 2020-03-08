package com.labs.neko.nekofxmusicplayer.Activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.support.v7.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.util.view.SearchInputView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;
import com.labs.neko.nekofxmusicplayer.Fragments.AlbumFragment;
import com.labs.neko.nekofxmusicplayer.Fragments.ArtistFragment;
import com.labs.neko.nekofxmusicplayer.Fragments.FXFragment;
import com.labs.neko.nekofxmusicplayer.Fragments.MusicFragment;
import com.labs.neko.nekofxmusicplayer.Fragments.PlaylistFragment;
import com.labs.neko.nekofxmusicplayer.Globals;
import com.labs.neko.nekofxmusicplayer.Lists.Album;
import com.labs.neko.nekofxmusicplayer.Lists.Artist;
import com.labs.neko.nekofxmusicplayer.MediaPlayerService;
import com.labs.neko.nekofxmusicplayer.Models.Music;
import com.labs.neko.nekofxmusicplayer.MuteCall;
import com.labs.neko.nekofxmusicplayer.Notification;
import com.labs.neko.nekofxmusicplayer.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import jp.wasabeef.fresco.processors.BlurPostprocessor;

public class MainActivity extends AppCompatActivity {
    private Globals globals = Globals.getInstance();

    private SlidingUpPanelLayout slidingUpPanelLayout;
    private TextView textViewHeader;
    private TextView tvArtistBar, tvTitleBar, tvArtistUp, tvTitleUp, timeStart, timeEnd;
    private SeekBar seekBar;
    private Fragment fragMusic, fragAlbum, fragArtist, fragPlaylist, fragFX;

    private BottomNavigationView navigation;
    private boolean tabClickable = true;
    /*
    private boolean loadFragment(Fragment fragment){
        if(fragment!=null){
            //fragment.setEnterTransition(new Slide(Gravity.TOP));
            //fragment.setExitTransition(new Slide(Gravity.TOP));
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.fade_out,R.anim.fade_in)
                    .replace(R.id.layout_topPanel,fragment)
                    .commit();
            return true;
        }
        return false;
    }
    */

    public void setFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_out,R.anim.fade_in)
                .replace(R.id.layout_topPanel,fragment)
                .commit();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_music:
                    //fragment = new MusicFragment();
                    if(tabClickable){
                        setFragment(fragMusic);
                        //textViewHeader.setText(R.string.title_music);
                        return true;
                    }
                case R.id.navigation_album:
                    //fragment = new AlbumFragment();
                    if(tabClickable){
                        setFragment(fragAlbum);
                        //textViewHeader.setText(R.string.title_album);
                        return true;
                    }
                case R.id.navigation_artist:
                    //fragment = new ArtistFragment();
                    if(tabClickable){
                        setFragment(fragArtist);
                        //textViewHeader.setText(R.string.title_artist);
                        return true;
                    }
                case R.id.navigation_playlist:
                    //fragment = new PlaylistFragment();
                    if(tabClickable){
                        setFragment(fragPlaylist);
                        //textViewHeader.setText(R.string.title_playlist);
                        return true;
                    }
                case R.id.navigation_fx:
                    //fragment = new FXFragment();
                    if(tabClickable){
                        setFragment(fragFX);
                        //textViewHeader.setText(R.string.title_fx);
                        return true;
                    }
            }
            //return loadFragment(fragment);
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        if(slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED){
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);

        setContentView(R.layout.activity_main);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorBlack));

        Random rand = new Random();
        final short random_track = (short) rand.nextInt(globals.getMusicList().size());

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        slidingUpPanelLayout = findViewById(R.id.slidingPanelLayout);
        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        //final CardView cardViewHeader = findViewById(R.id.cardViewHeader);
        //textViewHeader = findViewById(R.id.textView_header);
        tvArtistBar = findViewById(R.id.textView_artistCardView);
        tvTitleBar = findViewById(R.id.textView_musicCardView);
        final ImageView ivArtBar = findViewById(R.id.imageView_artCardView);
        timeStart = findViewById(R.id.textView_startTime);
        timeEnd = findViewById(R.id.textView_endTime);

        final SimpleDraweeView ivBg = findViewById(R.id.imageView_generalBg);
        final SimpleDraweeView ivArtBgUp = findViewById(R.id.imageView_bgPlayerView);
        final ImageView ivArtUp = findViewById(R.id.imageView_artPlayerView);
        tvArtistUp = findViewById(R.id.textView_artistPlayerView);
        tvTitleUp = findViewById(R.id.textView_titlePlayerView);

        final Button btnPrev = findViewById(R.id.buttonPreviousCardView);
        final Button btnPlay = findViewById(R.id.buttonPlayCardView);
        final Button btnNext = findViewById(R.id.buttonNextCardView);
        //Button btnMenu = findViewById(R.id.buttonMenu);
        Button btnUpPrev = findViewById(R.id.buttonPreviousPlayerView);
        final Button btnUpPlay = findViewById(R.id.buttonPlayPlayerView);
        Button btnUpNext = findViewById(R.id.buttonNextPlayerView);

        //Button btnSearch = findViewById(R.id.buttonSearch);
        final FloatingSearchView floatingSearchView = findViewById(R.id.floating_search_view);

        seekBar = findViewById(R.id.seekBar);

        final ImageView bgBar = findViewById(R.id.imageView_bgBar);

        tvTitleBar.setSelected(true);
        tvTitleUp.setSelected(true);



        fragAlbum = new AlbumFragment();
        fragArtist = new ArtistFragment();
        fragFX = new FXFragment();
        fragMusic = new MusicFragment();
        fragPlaylist = new PlaylistFragment();

        if(globals.getMediaPlayer().isPlaying()){
            setBarMusic(globals.getMusic(),ivArtBar,ivArtBgUp,ivArtUp,ivBg);
            btnPlay.setBackgroundResource(R.drawable.ic_pause_circle_outline_black_24dp);
            btnUpPlay.setBackgroundResource(R.drawable.ic_pause_circle_outline_black_24dp);
        }
        if(!globals.isHasTrack()){
            setBarMusic(globals.getMusicList().get(random_track),ivArtBar,ivArtBgUp,ivArtUp,ivBg);
            globals.setTrack(random_track);
        }

        final float NO_ALPHA = 1f, ALPHA= 0.7f, ALPHA_95 = 0.95f;

        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

                navigation.setAlpha(NO_ALPHA-slideOffset);
                ivArtBar.setAlpha(NO_ALPHA-slideOffset);
                tvArtistBar.setAlpha(NO_ALPHA-slideOffset);
                tvTitleBar.setAlpha(NO_ALPHA-slideOffset);
                bgBar.setAlpha(ALPHA_95-slideOffset);
                floatingSearchView.setAlpha(NO_ALPHA-slideOffset);

                btnNext.setAlpha(ALPHA-slideOffset);
                btnPlay.setAlpha(ALPHA-slideOffset);
                btnPrev.setAlpha(ALPHA-slideOffset);


                if(slideOffset>0.5){
                    tabClickable = false;
                }
                if(slideOffset<=0.5){
                    tabClickable = true;
                }
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

            }
        });


        globals.getMediaPlayer().setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                setBarMusic(globals.getMusic(),ivArtBar,ivArtBgUp,ivArtUp,ivBg);
                globals.getMediaPlayer().start();
                int total = globals.getMediaPlayer().getDuration();
                String end = milliSecondsToTimer(total);
                timeEnd.setText(end);
                btnPlay.setBackgroundResource(R.drawable.ic_pause_circle_outline_black_24dp);
                btnUpPlay.setBackgroundResource(R.drawable.ic_pause_circle_outline_black_24dp);
                seekBar.setMax(globals.getMediaPlayer().getDuration());
                updateSeekBar();
                Notification notification = new Notification(MainActivity.this);
                notification.showNotification();
            }
        });

        globals.getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(globals.getQueue().isEmpty()){
                    int listSize = globals.getMusicList().size();
                    int trackPosition = globals.getTrack()+1;
                    if(trackPosition<listSize){
                        globals.play(trackPosition);
                    }else{
                        trackPosition = 0;
                        globals.play(trackPosition);
                    }
                }else{
                    globals.playQueue();
                }
                globals.setHasTrack(true);
            }
        });

        View.OnTouchListener nextListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521,PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        int track = globals.getTrack()+1;
                        if(track== globals.getMusicList().size()){
                            track = 0;
                        }
                        globals.getMediaPlayer().pause();
                        globals.play(track);
                        break;
                    }
                }
                return false;
            }
        };

        View.OnTouchListener playListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521,PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        if(globals.getMediaPlayer().isPlaying()){
                            globals.getMediaPlayer().pause();
                            btnUpPlay.setBackgroundResource(R.drawable.ic_play_circle_outline_black_24dp);
                            btnPlay.setBackgroundResource(R.drawable.ic_play_circle_outline_black_24dp);
                        }else{
                            btnPlay.setBackgroundResource(R.drawable.ic_pause_circle_outline_black_24dp);
                            btnUpPlay.setBackgroundResource(R.drawable.ic_pause_circle_outline_black_24dp);
                            globals.getMediaPlayer().start();
                            if(globals.isHasTrack()){
                                globals.getMediaPlayer().start();
                            }else{
                                globals.play(random_track);
                                globals.setHasTrack(true);
                            }
                        }
                        break;
                    }
                }
                return false;
            }
        };

        View.OnTouchListener prevListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521,PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();

                        int track = globals.getTrack()-1;
                        if(track<0){
                            track = globals.getMusicList().size()-1;
                        }
                        globals.getMediaPlayer().pause();
                        globals.play(track);
                        break;
                    }
                }
                return false;
            }
        };

        btnNext.setOnTouchListener(nextListener);
        btnPlay.setOnTouchListener(playListener);
        btnPrev.setOnTouchListener(prevListener);

        btnUpPrev.setOnTouchListener(prevListener);
        btnUpPlay.setOnTouchListener(playListener);
        btnUpNext.setOnTouchListener(nextListener);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    globals.getMediaPlayer().seekTo(progress);
                if(seekBar.getMax()==progress)
                    btnPlay.setBackgroundResource(R.drawable.ic_play_circle_outline_black_24dp);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        if(savedInstanceState==null){
            //loadFragment(new MusicFragment());
            setFragment(fragMusic);
        }




        /*

        MuteCall muteCall = new MuteCall();
        PhoneStateListener mPhoneStateListener = new PhoneStateListener();
        TelephonyManager mgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        mgr.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);


        Intent intent = new Intent(getApplicationContext(), MediaPlayerService.class);
        intent.setAction(MediaPlayerService.ACTION_PLAY);
        startService(intent);

        // Notification panel

        NotificationCompat.Builder builder;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int notificationID = (int) System.currentTimeMillis();
        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.notification_panel);

        remoteViews.setImageViewResource(R.id.imageView_panel,ivArtUp.getId());
        remoteViews.setTextViewText(R.id.textView_musicPanel,tvTitleUp.getText());
        remoteViews.setTextViewText(R.id.textView_artistPanel,tvArtistUp.getText());

        Intent intentPlayPanel = new Intent("play_panel");
        intentPlayPanel.putExtra("id_play",notificationID);

        PendingIntent pIntentPlayPanel = PendingIntent.getBroadcast(MainActivity.this,123,intentPlayPanel,0);
        remoteViews.setOnClickPendingIntent(R.id.button_playPanel,pIntentPlayPanel);

        // Build notification

        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,0,notificationIntent,0);

        builder = new NotificationCompat.Builder(MainActivity.this);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setCustomBigContentView(remoteViews)
                .setContentIntent(pendingIntent);
        notificationManager.notify(notificationID,builder.build());

        */






        floatingSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                newQuery = newQuery.toLowerCase();

                final ArrayList<Music> filterList = new ArrayList<>();
                final ArrayList<Music> filterListArtist = new ArrayList<>();

                for (Music music : globals.getMusicList()) {
                    String title = music.getTitle().toLowerCase();
                    String artist = music.getArtist().toLowerCase();
                    if (title.contains(newQuery) || artist.contains(newQuery)) {
                        filterList.add(music);
                    }
                }
                globals.getAdapter().updateList(filterList);
            }
        });

        floatingSearchView.attachNavigationDrawerToMenuButton(drawerLayout);

        floatingSearchView.setOnLeftMenuClickListener(new FloatingSearchView.OnLeftMenuClickListener() {
            @Override
            public void onMenuOpened() {
                drawerLayout.openDrawer(Gravity.START);
            }
            @Override
            public void onMenuClosed() {

            }
        });

        floatingSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                floatingSearchView.setSearchFocused(true);
            }
        });





    }


    public void updateSeekBar(){
        handler.postDelayed(seekbarRunnable,500);
    }

    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString;

        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        return finalTimerString;
    }

    @Override
    public void onStateNotSaved() {
        super.onStateNotSaved();
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }


    private Handler handler = new Handler();
    private Runnable seekbarRunnable = new Runnable() {
        @Override
        public void run() {
            int position = globals.getMediaPlayer().getCurrentPosition();
            timeStart.setText(String.valueOf(position));
            seekBar.setProgress(position);
            timeStart.setText(milliSecondsToTimer(position));
            handler.postDelayed(this, 500);
        }
    };




    private void setBarMusic(Music music, ImageView aBar, SimpleDraweeView aBgUp, ImageView aUp, SimpleDraweeView bg){
        int transparent = R.drawable.ic_album_black_24dp;

        tvTitleBar.setText(music.getTitle());
        tvArtistBar.setText(music.getArtist());

        tvTitleUp.setText(music.getTitle());
        tvArtistUp.setText(music.getArtist());

        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

        String artID = music.getCover();
        long parseLong = Long.parseLong(artID);
        Uri uri = ContentUris.withAppendedId(sArtworkUri, parseLong);

        loadImages(aBar,uri,transparent,128,128);
        loadImages(aUp,uri,transparent,512,512);
        //globals.applyBlur(MainActivity.this,bg,findViewById(R.id.layout_main));
        loadBlurImage(bg,uri,80);
        loadBlurImage(aBgUp, uri,30);
    }

    private void loadBlurImage(SimpleDraweeView ivArtBgUp, Uri uri, int radius){
        Postprocessor postprocessor = new BlurPostprocessor(this,radius);
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setPostprocessor(postprocessor)
                .build();

        DraweeController controller =
                Fresco.newDraweeControllerBuilder()
                        .setImageRequest(imageRequest)
                        .setOldController(ivArtBgUp.getController())
                        .build();

        ivArtBgUp.setController(controller);
    }

    private void loadImages(ImageView imageView, Uri uri, int errorColor,int resizeX, int resizeY){
        Picasso.get().load(uri)
                .resize(resizeX, resizeY)
                .noFade().centerCrop()
                .error(errorColor)
                .placeholder(errorColor)
                .into(imageView);
    }

}
