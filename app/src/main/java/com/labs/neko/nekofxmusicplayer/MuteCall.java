package com.labs.neko.nekofxmusicplayer;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class MuteCall extends PhoneStateListener {

    private Globals globals = Globals.getInstance();
    private boolean mWasPlayingWhenCalled = false;

    public MuteCall(){

    }

    @Override
    public void onCallStateChanged(int state, String phoneNumber) {
        super.onCallStateChanged(state, phoneNumber);
        switch (state){
            case TelephonyManager.CALL_STATE_RINGING:
                if( globals.getMediaPlayer().isPlaying() ) {
                    globals.getMediaPlayer().pause();
                    mWasPlayingWhenCalled = true;
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                if( mWasPlayingWhenCalled ){
                    globals.getMediaPlayer().start();
                    mWasPlayingWhenCalled = false;
                }
                break;
                default:
                    break;
        }
        super.onCallStateChanged(state,phoneNumber);
    }

}
