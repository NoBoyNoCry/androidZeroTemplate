package com.video.utils;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.video.listen.MediaPlayListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.media.MediaPlayer.MEDIA_ERROR_IO;
import static android.media.MediaPlayer.MEDIA_ERROR_MALFORMED;
import static android.media.MediaPlayer.MEDIA_ERROR_TIMED_OUT;
import static android.media.MediaPlayer.MEDIA_ERROR_UNSUPPORTED;

/**
 * @author : yzf
 * time   : 2019/08/13
 * description：
 */
public class MediaPlayUtils {
    @SuppressLint("StaticFieldLeak")
    private static MediaPlayUtils ourInstance;
    /**
     * 负责播放进入视频播放界面后的第一段视频
     */
    private MediaPlayer mediaPlayer;
    /**
     * 负责一段视频播放结束后，播放下一段视频
     */
    private MediaPlayer nextMediaPlayer;
    /**
     * 负责setNextMediaPlayer的player缓存对象
     */
    private MediaPlayer cachePlayer;
    /**
     * 负责当前播放视频段落的player对象
     */
    private MediaPlayer currentPlayer;
    //================================================================
    /**
     * 播放列表
     */
    private List<String> urls;
    /**
     * 播放监听
     */
    private MediaPlayListener listener;
    /**
     * 所有player对象的缓存
     */
    private HashMap<String, MediaPlayer> playersCache = new HashMap<String, MediaPlayer>();
    /**
     * 当前播放到的视频段落数
     */
    private int currentVideoIndex;

    public static MediaPlayUtils getInstance() {
        if (ourInstance == null) {
            ourInstance = new MediaPlayUtils();
        }
        return ourInstance;
    }

    private MediaPlayUtils() {
    }

    /**
     * 添加url
     *
     * @param urls 播放的路径
     * @return this
     */
    public MediaPlayUtils updateUrls(List<String> urls) {
        this.urls = urls;
        return ourInstance;
    }

    public MediaPlayUtils updateUrls(String[] urlsArray) {
        urls = new ArrayList<>();
        if (urlsArray != null) {
            urls.addAll(Arrays.asList(urlsArray));
        }
        return ourInstance;
    }

    public MediaPlayUtils addUrls(String url) {
        if (urls == null) {
            urls = new ArrayList<>();
        }
        urls.add(url);
        return ourInstance;
    }

    public MediaPlayUtils setListener(MediaPlayListener listener) {
        this.listener = listener;
        return ourInstance;
    }

    public void init() {
        try {
            onDestroy();
            currentVideoIndex = 0;
            initFirstPlayer();
            //设置cachePlayer为该player对象
            cachePlayer = mediaPlayer;
            initNextPlayer();
            startFirstPlayer();
        } catch (IOException e) {
            e.printStackTrace();
            if (listener != null) {
                listener.onError(e.toString());
            }
        }
    }

    private void startFirstPlayer() throws IOException {
        mediaPlayer.setDataSource(urls.get(currentVideoIndex));
        // 通过异步的方式装载媒体资源
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 装载完毕回调
                mediaPlayer.start();
            }
        });
        mediaPlayer.setOnErrorListener(returnOnErrorListener());
    }

    private void initFirstPlayer() throws IOException {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer
                .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        onVideoPlayCompleted(mp);
                    }
                });
    }

    /**
     * 新开线程负责初始化负责播放剩余视频分段的player对象,避免UI线程做过多耗时操作
     */
    private void initNextPlayer() {
        new Thread(() -> {
            for (int i = 1; i < urls.size(); i++) {
                nextMediaPlayer = new MediaPlayer();
                nextMediaPlayer
                        .setAudioStreamType(AudioManager.STREAM_MUSIC);
                nextMediaPlayer
                        .setOnCompletionListener(mp -> onVideoPlayCompleted(mp));
                nextMediaPlayer.setOnErrorListener(returnOnErrorListener());
                try {
                    nextMediaPlayer.setDataSource(urls.get(i));
                    nextMediaPlayer.prepare();
                } catch (IOException e) {
                    // TODO 自动生成的 catch 块
                    e.printStackTrace();
                }
                //set next mediaplayer
                cachePlayer.setNextMediaPlayer(nextMediaPlayer);
                //set new cachePlayer
                cachePlayer = nextMediaPlayer;
                //put nextMediaPlayer in cache
                playersCache.put(String.valueOf(i), nextMediaPlayer);
            }

        }).start();
    }

    /**
     * 负责处理一段播放过后，切换player播放下一段视频
     */
    private void onVideoPlayCompleted(MediaPlayer mp) {
        mp.setDisplay(null);
        //get next player
        currentPlayer = playersCache.get(String.valueOf(++currentVideoIndex));
        if (listener != null) {
            listener.onComplete(urls.get(currentVideoIndex - 1));
        }
        if (currentPlayer == null) {
            if (listener != null) {
                urls.clear();
                listener.onAllComplete();
            }
        }
    }

    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (nextMediaPlayer != null) {
            nextMediaPlayer.stop();
            nextMediaPlayer.release();
            nextMediaPlayer = null;
        }
        if (currentPlayer != null) {
            currentPlayer.stop();
            currentPlayer.release();
            currentPlayer = null;
        }
    }

    private MediaPlayer.OnErrorListener returnOnErrorListener() {
        return new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                switch (extra) {
                    case MEDIA_ERROR_IO:
                        if (listener != null) {
                            listener.onError("what:" + what + "extra:" + "MEDIA_ERROR_IO");
                        }
                        break;
                    case MEDIA_ERROR_MALFORMED:
                        if (listener != null) {
                            listener.onError("what:" + what + "extra:" + "MEDIA_ERROR_MALFORMED");
                        }
                        break;
                    case MEDIA_ERROR_UNSUPPORTED:
                        if (listener != null) {
                            listener.onError("what:" + what + "extra:" + "MEDIA_ERROR_UNSUPPORTED");
                        }
                        break;
                    case MEDIA_ERROR_TIMED_OUT:
                        if (listener != null) {
                            listener.onError("what:" + what + "extra:" + "MEDIA_ERROR_TIMED_OUT");
                        }
                        break;
                    default:
                        break;
                }

                return false;
            }
        };
    }
}
