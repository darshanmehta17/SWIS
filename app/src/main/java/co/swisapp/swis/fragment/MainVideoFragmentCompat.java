package co.swisapp.swis.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import co.swisapp.swis.R;
import co.swisapp.swis.activity.MainVideoPlayUploadActivity;
import co.swisapp.swis.customview.RecordButton;
import co.swisapp.swis.utility.FileHelper;

@SuppressWarnings("deprecation")
public class MainVideoFragmentCompat extends Fragment
        implements TextureView.SurfaceTextureListener,
        RecordButton.OnStartRecordListener, RecordButton.OnStopRecordListener{

    private TextureView textureView ;
    private RecordButton recordButton ;
    private Camera camera ;
    private MediaRecorder mediaRecorder ;
    /*private Thread thread1 ;*/
    private String MainfileName ;
    private File MainfilePath ;
    private SurfaceTexture surfaceTexture ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_video_compat, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textureView = (TextureView) view.findViewById(R.id.textureview_compat) ;
        recordButton = (RecordButton) view.findViewById(R.id.video_record_button_compat) ;

        recordButton.setOnStartRecordListener(this);
        recordButton.setOnStopRecordListener(this);

        textureView.setSurfaceTextureListener(this);


    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        surfaceTexture = surface ;

        setupPreview();

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        camera.stopPreview();
        camera.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void onStartRecord() {
        Log.d("CHECK", "function onStartRecording") ;

        MainfileName = FileHelper.generateVideoFileName() ;
        MainfilePath = FileHelper.createVideoFile(getActivity(), MainfileName, FileHelper.TYPE_INTERNAL);

        mediaRecorder = new MediaRecorder() ;
        try {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

            mediaRecorder.setOutputFile(MainfilePath.getAbsolutePath());
            mediaRecorder.setVideoEncodingBitRate(10000000);
            mediaRecorder.setVideoFrameRate(15);

            camera.unlock();
            mediaRecorder.setCamera(camera);

            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);


            mediaRecorder.prepare();
        } catch (IOException e) {
            Log.d("CHECK", "Error message" + e.getMessage()) ;
        } catch (IllegalStateException ise){
            Log.d("CHECK", "Error message:" + ise.getMessage()) ;
        }
        mediaRecorder.start();


    }

    @Override
    public void onStopRecord() {
        mediaRecorder.stop();
        mediaRecorder.reset();
        Activity activity = getActivity();
        if (null != activity) {
            Toast.makeText(activity, "Video saved: " + MainfileName,
                    Toast.LENGTH_SHORT).show();
            Log.d("FILE NAME", " " + MainfileName) ;
        }
        try{
            Intent previewFragment = new Intent(getActivity(), MainVideoPlayUploadActivity.class);
            previewFragment.putExtra("filePath", MainfilePath);
            startActivity(previewFragment);
        }catch (Throwable th){
            setupPreview();
        }
    }

    public void setupPreview(){
        Log.d("CHECK", "function setupPreview") ;

        camera = Camera.open() ;

        try {
            camera.setPreviewTexture(surfaceTexture);
            camera.setDisplayOrientation(90);

            camera.startPreview();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (RuntimeException re){
            Log.d("CHECK", "Runtime: " + re.getMessage()) ;
        }

    }


}
