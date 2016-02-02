package co.swisapp.swis.fragment;


import android.app.Fragment;
import android.content.res.Configuration;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

import co.swisapp.swis.R;
import co.swisapp.swis.utility.CameraHelper;

public class MainVideoFragmentCompat extends Fragment implements View.OnClickListener, SurfaceHolder.Callback {

    MediaRecorder recorder;
    SurfaceHolder holder;
    boolean mIsRecordingVideo = false;


    private void initRecorder() {
        recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

        CamcorderProfile cpHigh = CamcorderProfile
                .get(CamcorderProfile.QUALITY_HIGH);
        recorder.setProfile(cpHigh);
        recorder.setOutputFile(CameraHelper.getVideoFileInternal(getActivity()).getAbsolutePath());
        recorder.setMaxDuration(24000);
        recorder.setMaxFileSize(5000000); // Approximately 5 megabytes


    }

    private void prepareRecorder() {
        recorder.setPreviewDisplay(holder.getSurface());
        try {
            recorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Log.d("prepareRecorder", "IllegalStateException" + e) ;
        } catch (IOException e) {
            Log.d("prepareRecorder", "IOException" + e) ;

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_capture_compat: {
                if (mIsRecordingVideo) {
                    recorder.stop();
                    mIsRecordingVideo = false;

                    initRecorder();
                    prepareRecorder();

                } else {
                    mIsRecordingVideo = true;
                    recorder.start();
                }
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_video_compat, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recorder = new MediaRecorder();
        initRecorder();

        SurfaceView cameraView = (SurfaceView) view.findViewById(R.id.surfaceview) ;
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.video_capture_compat) ;

        holder = cameraView.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

       //TODO: Check code again
        int orientation = getResources().getConfiguration().orientation ;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            cameraView.getHolder().setSizeFromLayout();
        }

        /*cameraView.setClickable(true);
        cameraView.setOnClickListener(this);*/

        floatingActionButton.setOnClickListener(this);

    }


    public void surfaceCreated(SurfaceHolder holder) {

        prepareRecorder();
    }



    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }


    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mIsRecordingVideo) {
            recorder.stop();
            mIsRecordingVideo = false;
        }
        recorder.release();
    }




}
