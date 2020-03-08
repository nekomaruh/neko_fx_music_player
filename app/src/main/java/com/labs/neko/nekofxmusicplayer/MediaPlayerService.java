package com.labs.neko.nekofxmusicplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.os.IBinder;

public class MediaPlayerService extends Service {

    private MediaSession mediaSession;
    public static final String ACTION_PLAY = "action_play";
    public static final String ACTION_PAUSE = "action_pause";
    public static final String ACTION_REWIND = "action_rewind";
    public static final String ACTION_FAST_FORWARD = "action_fast_forward";
    public static final String ACTION_NEXT = "action_next";
    public static final String ACTION_PREVIOUS = "action_previous";
    public static final String ACTION_STOP = "acition_stop";

    private MediaPlayer mediaPlayer;
    private MediaSessionManager mediaManager;
    private MediaController mediaController;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mediaSession.release();
        return super.onUnbind(intent);
    }

    private void handleIntent(Intent intent){
        if(intent == null || intent.getAction()==null){
            return;
        }
        String action = intent.getAction();
        if(action.equalsIgnoreCase(ACTION_PLAY)){
            mediaController.getTransportControls().play();
        }else if(action.equalsIgnoreCase(ACTION_PAUSE)){
            mediaController.getTransportControls().pause();
        }else if(action.equalsIgnoreCase(ACTION_FAST_FORWARD)) {
            mediaController.getTransportControls().fastForward();
        }else if(action.equalsIgnoreCase(ACTION_REWIND)){
            mediaController.getTransportControls().rewind();
        }else if(action.equalsIgnoreCase(ACTION_PREVIOUS)){
            mediaController.getTransportControls().skipToPrevious();
        }else if(action.equalsIgnoreCase(ACTION_NEXT)){
            mediaController.getTransportControls().skipToNext();
        }else if(action.equalsIgnoreCase(ACTION_STOP)){
            mediaController.getTransportControls().stop();
        }
    }

    private Notification.Action generateAction(int icon, String title, String intentAction){
        Intent intent = new Intent(getApplicationContext(),MediaPlayerService.class);
        intent.setAction(intentAction);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(),1,intent,0);
        return new Notification.Action.Builder(icon,title,pendingIntent).build();
    }

    private void buildNotification(Notification.Action action){
        Notification.MediaStyle style = new Notification.MediaStyle();
        Intent intent = new Intent(getApplicationContext(),MediaPlayerService.class);
        intent.setAction(ACTION_STOP);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(),1,intent,0);
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Musica")
                .setContentText("Artista")
                .setDeleteIntent(pendingIntent)
                .setStyle(style);
        builder.addAction(generateAction(android.R.drawable.ic_media_previous,"Previous",ACTION_PREVIOUS));
        builder.addAction(generateAction(android.R.drawable.ic_media_rew,"Rewind",ACTION_REWIND));
        builder.addAction(action);
        builder.addAction(generateAction(android.R.drawable.ic_media_ff,"Fast forward",ACTION_FAST_FORWARD));
        builder.addAction(generateAction(android.R.drawable.ic_media_previous,"Next",ACTION_NEXT));
        style.setShowActionsInCompactView(0,1,2,3,4);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(mediaManager==null){
            initMediaSession();
        }
        handleIntent(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void initMediaSession(){
        mediaPlayer = new MediaPlayer();
        mediaSession = new MediaSession(getApplicationContext(),"Player Session");
        mediaController = new MediaController(getApplicationContext(),mediaSession.getSessionToken());

        mediaSession.setCallback(new MediaSession.Callback() {
            @Override
            public void onPlay() {
                super.onPlay();
                buildNotification(generateAction(android.R.drawable.ic_media_pause,"Pause",ACTION_PAUSE));
            }

            @Override
            public void onPause() {
                super.onPause();
                buildNotification(generateAction(android.R.drawable.ic_media_play,"Play",ACTION_PLAY));
            }

            @Override
            public void onFastForward() {
                super.onFastForward();
            }

            @Override
            public void onRewind() {
                super.onRewind();
            }

            @Override
            public void onStop() {
                super.onStop();
                NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(1);
                Intent intent = new Intent(getApplicationContext(),MediaPlayerService.class);
                stopService(intent);
            }
        });
    }


}
