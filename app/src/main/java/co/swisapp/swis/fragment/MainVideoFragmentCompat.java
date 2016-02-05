package co.swisapp.swis.fragment;


import android.app.Fragment;
import android.content.res.Configuration;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

import co.swisapp.swis.R;
import co.swisapp.swis.customview.RecordButton;
import co.swisapp.swis.utility.FileHelper;

public class MainVideoFragmentCompat extends Fragment
        implements RecordButton.OnStartRecordListener, RecordButton.OnStopRecordListener, SurfaceHolder.Callback {

    public String MainfilePath ;
    public String MainfileName ;

    MediaRecorder recorder;
    SurfaceHolder holder;
    boolean mIsRecordingVideo = false;


    private void initRecorder() {
        recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

        CamcorderProfile cpHigh = CamcorderProfile
                .get(CamcorderProfile.QUALITY_HIGH);
        recorder.setProfile(cpHigh);


        MainfileName = FileHelper.generateVideoFileName() ;
        MainfilePath = FileHelper.createVideoFile(getActivity(), MainfileName, FileHelper.TYPE_INTERNAL ).getAbsolutePath() ;

        recorder.setOutputFile(MainfilePath);
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

    /*public void onClick(View v) {
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
    }*/

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
        RecordButton recordButton = (RecordButton) view.findViewById(R.id.video_record_button_compat) ;

        holder = cameraView.getHolder();
        holder.addCallback(this);
        //noinspection deprecation
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

       //TODO: Check code again
        int orientation = getResources().getConfiguration().orientation ;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            cameraView.getHolder().setSizeFromLayout();
        }

        recordButton.setOnStopRecordListener(this);
        recordButton.setOnStopRecordListener(this);

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

    @Override
    public void onStartRecord() {
        mIsRecordingVideo = true;
        recorder.start();
    }

    @Override
    public void onStopRecord() {
        recorder.stop();
        mIsRecordingVideo = false;

        initRecorder();
        prepareRecorder();
    }

/*
    private void configureTransform(int viewWidth, int viewHeight) {
        Activity activity = getActivity();
        if (null == cameraView || null == mPreviewSize || null == activity) {
            return;
        }
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        }
        cameraView.setTransform(matrix);
    }
*/




}
