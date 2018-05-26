package com.odelan.track.ui.activity.intro;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import com.odelan.track.R;
import com.odelan.track.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IntroActivity extends BaseActivity {

    @BindView(R.id.videoView)
    VideoView videoView;

    Uri video;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_intro;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.hello);
        videoView.setVideoURI(video);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                videoView.start();
            }
        });

        videoView.start();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!videoView.isPlaying()) {
            videoView.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (videoView.isPlaying()) {
            videoView.pause();
        }
    }

    @OnClick (R.id.loginBtn) public void onLogin() {
        startActivity(new Intent(mContext, LoginActivity.class));
    }

    @OnClick (R.id.signupBtn) public void onSignup () {
        startActivity(new Intent(mContext, SignupActivity.class));
    }
}
