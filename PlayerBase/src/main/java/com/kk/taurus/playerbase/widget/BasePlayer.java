package com.kk.taurus.playerbase.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.setting.AspectRatio;
import com.kk.taurus.playerbase.setting.DecodeMode;
import com.kk.taurus.playerbase.setting.Rate;
import com.kk.taurus.playerbase.setting.VideoData;

import java.util.List;

/**
 * Created by Taurus on 2017/3/28.
 */

public abstract class BasePlayer extends BaseAdPlayer {

    private final String TAG = "BasePlayer";
    protected BaseSinglePlayer mInternalPlayer;
    protected VideoData dataSource;

    public BasePlayer(@NonNull Context context) {
        super(context);
    }

    public BasePlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BasePlayer(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onCoversHasInit(Context context) {
        super.onCoversHasInit(context);
        onBindPlayer(this,this);
    }

    private boolean available(){
        return mInternalPlayer !=null;
    }

    @Override
    public void setDataSource(VideoData data) {
        if(available() && data!=null && data.getData()!=null){
            this.dataSource = data;
            mInternalPlayer.setDataSource(data);
            Bundle bundle = new Bundle();
            bundle.putSerializable(OnPlayerEventListener.BUNDLE_KEY_VIDEO_DATA,data);
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_ON_SET_DATA_SOURCE,bundle);
        }
    }

    @Override
    public void start() {
        if(available()){
            startPos = 0;
            mInternalPlayer.start();
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_INTENT_TO_START,null);
        }
    }

    @Override
    public void start(int msc) {
        if(available()){
            startPos = msc;
            mInternalPlayer.start(msc);
            Bundle bundle = new Bundle();
            bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_POSITION,msc);
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_INTENT_TO_START,bundle);
        }
    }

    @Override
    public void pause() {
        if(available()){
            mInternalPlayer.pause();
            Bundle bundle = new Bundle();
            bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_POSITION,getCurrentPosition());
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAY_PAUSE,bundle);
        }
    }

    @Override
    public void resume() {
        if(available() && mStatus == STATUS_PAUSED){
            mInternalPlayer.resume();
            Bundle bundle = new Bundle();
            bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_POSITION,getCurrentPosition());
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAY_RESUME,bundle);
        }
    }

    @Override
    public void seekTo(int msc) {
        if(available()){
            mInternalPlayer.seekTo(msc);
            Bundle bundle = new Bundle();
            bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_POSITION,msc);
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_SEEK_TO,bundle);
        }
    }

    @Override
    public void stop() {
        if(available()){
            mInternalPlayer.stop();
            reset();
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_ON_STOP,null);
        }
    }

    @Override
    public void reset() {
        if(available()){
            mInternalPlayer.reset();
        }
    }

    @Override
    public void rePlay(int msc) {
        if(available()){
            if(dataSource!=null && available()){
                stop();
                setDataSource(dataSource);
                start(msc);
            }
        }
    }

    @Override
    public boolean isPlaying() {
        if(available()){
            return mInternalPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public int getCurrentPosition() {
        if(available()){
            return mInternalPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public int getDuration() {
        if(available()){
            return mInternalPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public int getBufferPercentage() {
        if(available()){
            return mInternalPlayer.getBufferPercentage();
        }
        return 0;
    }

    @Override
    public Rate getCurrentDefinition() {
        if(available()){
            return mInternalPlayer.getCurrentDefinition();
        }
        return null;
    }

    @Override
    public List<Rate> getVideoDefinitions() {
        if(available()){
            return mInternalPlayer.getVideoDefinitions();
        }
        return null;
    }

    @Override
    public void changeVideoDefinition(Rate rate) {
        if(available() && rate!=null){
            mInternalPlayer.changeVideoDefinition(rate);
            Bundle bundle = new Bundle();
            bundle.putSerializable(OnPlayerEventListener.BUNDLE_KEY_RATE_DATA,rate);
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_CHANGE_DEFINITION,bundle);
        }
    }

    @Override
    public void setDecodeMode(DecodeMode mDecodeMode) {
        super.setDecodeMode(mDecodeMode);
        if(available()){
            mInternalPlayer.setDecodeMode(mDecodeMode);
        }
    }

    @Override
    public void setAspectRatio(AspectRatio aspectRatio) {
        if(available()){
            mInternalPlayer.setAspectRatio(aspectRatio);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        destroyInternalPlayer();
    }

    protected void destroyInternalPlayer() {
        if(available()){
            mInternalPlayer.setOnErrorListener(null);
            mInternalPlayer.setOnPlayerEventListener(null);
            mInternalPlayer.destroy();
        }
    }
}
