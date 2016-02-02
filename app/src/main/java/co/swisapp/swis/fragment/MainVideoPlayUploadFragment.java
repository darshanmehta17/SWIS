package co.swisapp.swis.fragment;


import android.app.Fragment;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

import co.swisapp.swis.R;
import co.swisapp.swis.utility.CameraHelper;

public class MainVideoPlayUploadFragment extends Fragment implements
        TextureView.SurfaceTextureListener, MediaPlayer.OnCompletionListener, View.OnClickListener{

    private MediaPlayer mMediaPlayer;
    private TextureView mPreview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_video_play_upload, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPreview = (TextureView) view.findViewById(R.id.play_texture);
        mPreview.setSurfaceTextureListener(this);


    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Surface s = new Surface(surface);

        try {
            mMediaPlayer= new MediaPlayer();
            mMediaPlayer.setDataSource(CameraHelper.getVideoFile(getActivity()).getAbsolutePath());
            mMediaPlayer.setSurface(s);
            mMediaPlayer.prepare();
            /*mMediaPlayer.setOnBufferingUpdateListener(this);

            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnVideoSizeChangedListener(this);*/

            mMediaPlayer.setOnCompletionListener(this);

            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.start();
        } catch (IllegalArgumentException e) {

            e.printStackTrace();
        } catch (SecurityException e) {

            e.printStackTrace();
        } catch (IllegalStateException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.upload: uploadToServer() ;
                                break;
            case R.id.save:
                                break;
            case R.id.setLocation:
                                break;

            default:
                return;


        }
    }

    private void uploadToServer() {
        /*
        * UPLOAD TO SERVER
        * SHOW PROGRESS IN NOTIFICATION
        * SET UPLOADING ON UI
        * COMPRESS FILE
        * */



    }
}
