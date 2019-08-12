package com.video.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.DefaultMediaSourceEventListener;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author : yzf
 * time   : 2019/08/12
 * description：
 */
public class VideoUtils {
    @SuppressLint("StaticFieldLeak")
    private static VideoUtils ourInstance;
    private List<String> urls;
    private ExoPlayer player;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private Context context;
    private PlayerView view;

    private long lastPlayedPosition = 0L;

    private boolean pausedFromPlayer = false;

    public static VideoUtils getInstance() {
        if (ourInstance == null) {
            ourInstance = new VideoUtils();
        }
        return ourInstance;
    }

    private VideoUtils() {

    }

    public VideoUtils with(Context context) {
        this.context = context;
        urls = new ArrayList<>();
        return ourInstance;
    }

    public VideoUtils updateUrls(List<String> urls) {
        this.urls = urls;
        return ourInstance;
    }

    public VideoUtils updateUrls(String[] urlsArray) {
        urls = new ArrayList<>();
        if (urlsArray != null) {
            urls.addAll(Arrays.asList(urlsArray));
        }
        return ourInstance;
    }

    public VideoUtils addUrls(String url) {
        if (urls == null) {
            urls = new ArrayList<>();
        }
        urls.add(url);
        return ourInstance;
    }

    public VideoUtils setView(PlayerView view) {
        this.view = view;
        return ourInstance;
    }

    public void play() {
        if (urls == null || urls.size() == 0) {
            return;
        }
        shouldAutoPlay = true;
        //传入工厂对象，以便创建选择磁道对象
        trackSelector = new DefaultTrackSelector();
        //根据选择磁道创建播放器对象
        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        //将player和view绑定
        if (view != null) {
            view.requestFocus();
            view.setPlayer(player);
        }
        player.setPlayWhenReady(shouldAutoPlay);
        //定义数据源工厂对象
        DefaultBandwidthMeter mDefaultBandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory mediaDataSourceFactory = new DefaultDataSourceFactory(context,
                mDefaultBandwidthMeter, new DefaultHttpDataSourceFactory("exoplayer-codelab", null, 15000, 15000, true));
        //创建Extractor工厂对象，用于提取多媒体文件
        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        //创建数据源
        MediaSource[] mediaSources = new MediaSource[urls.size()];
        for (int i = 0; i < urls.size(); i++) {
            mediaSources[i] =
                    new ExtractorMediaSource.Factory(mediaDataSourceFactory).
                            setExtractorsFactory(extractorsFactory).
                            createMediaSource(Uri.parse(urls.get(i)));
        }
        // 播放第一个视频两次，然后再播放第二个视频
        ConcatenatingMediaSource concatenatedSource =
                new ConcatenatingMediaSource(mediaSources);
        //添加监听
        player.addListener(new Player.EventListener() {

            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {
                Log.i("loggerVideo", "-------------------------");
                Log.d("loggerVideo", "onTimelineChanged() called with: timeline = [" + timeline + "], " +
                        "manifest = [" + manifest + "]");
                Log.i("loggerVideo", "-------------------------");
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                Log.i("loggerVideo", "-------------------------");
                Log.d("loggerVideo", "onTracksChanged() called with: trackGroups = [" + trackGroups + "], " +
                        "trackSelections = [" + trackSelections + "]");
                Log.i("loggerVideo", "-------------------------");
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Log.i("loggerVideo", "-------------------------");
                String stateString;
                // actually playing media
                if (playWhenReady && playbackState == Player.STATE_READY) {
                    Log.d("loggerVideo", "onPlayerStateChanged: actually playing media");
                }
                switch (playbackState) {
                    case Player.STATE_IDLE:
                        stateString = "ExoPlayer.STATE_IDLE      -";
                        break;
                    case Player.STATE_BUFFERING:
                        stateString = "ExoPlayer.STATE_BUFFERING -";
                        break;
                    case Player.STATE_READY:
                        stateString = "ExoPlayer.STATE_READY     -";
                        break;
                    case Player.STATE_ENDED:
                        stateString = "ExoPlayer.STATE_ENDED     -";
                        break;
                    default:
                        stateString = "UNKNOWN_STATE             -";
                        break;
                }
                Log.d("loggerVideo", "changed state to " + stateString + " playWhenReady: " + playWhenReady);
                Log.i("loggerVideo", "-------------------------");
            }

            @Override
            public void onPositionDiscontinuity(int reason) {
                Log.i("loggerVideo", "-------------------------");
                Log.i("loggerVideo", "onPositionDiscontinuity:" + reason);
                Log.i("loggerVideo", "-------------------------");
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.i("loggerVideo", "-------------------------");
                Log.i("loggerVideo", "ExoPlaybackException:" + error.toString());
                Log.i("loggerVideo", "-------------------------");
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                Log.i("loggerVideo", "-------------------------");
                Log.i("loggerVideo", "playbackParameters:" + playbackParameters.toString());
                Log.i("loggerVideo", "-------------------------");
            }
        });
        //添加数据源到播放器中
        player.prepare(concatenatedSource);
    }

    /**
     * 释放当前播放的类
     */
    public void releasePlayer() {
        if (player != null) {
            shouldAutoPlay = player.getPlayWhenReady();
            //释放播放器对象
            player.release();
            player = null;
            trackSelector = null;
        }
    }

    /**
     * 判断是否播放
     */
    public Boolean isNeedInstall() {
        return player == null;
    }

    /**
     * 暂停
     */
    public void pause() {
        if (player == null) {
            return;
        }
        if (player.getPlayWhenReady()) {
            lastPlayedPosition = player.getCurrentPosition();
            player.setPlayWhenReady(false);
            pausedFromPlayer = false;
        } else {
            pausedFromPlayer = true;
        }
    }

    /**
     * 开始
     */
    public void resume() {
        if (player == null) {
            return;
        }

        if (player.getPlayWhenReady()) {
            return;
        }

        if (!pausedFromPlayer) {
            player.seekTo(lastPlayedPosition - 500 < 0 ? 0 : lastPlayedPosition - 500);
            player.setPlayWhenReady(true);
        }
    }
}
