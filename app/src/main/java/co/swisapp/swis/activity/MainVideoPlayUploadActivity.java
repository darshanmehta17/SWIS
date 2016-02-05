package co.swisapp.swis.activity;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.channels.FileChannel;

import co.swisapp.swis.BuildConfig;
import co.swisapp.swis.R;
import co.swisapp.swis.utility.CameraHelper;
import co.swisapp.swis.utility.FileHelper;

public class MainVideoPlayUploadActivity extends AppCompatActivity implements
        TextureView.SurfaceTextureListener, MediaPlayer.OnCompletionListener, View.OnClickListener{

    private MediaPlayer mMediaPlayer;
    private TextureView mPreview;
    private ImageButton saveFileButton ;
    private ImageButton uploadFile ;
    private static final String TAG = "UploadServiceDemo";
    private static final String USER_AGENT = "UploadServiceDemo/" + BuildConfig.VERSION_NAME;
    private File FilePathIntent ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;

        setContentView(R.layout.fragment_main_video_play_upload);
        mPreview = (TextureView)findViewById(R.id.play_texture);
        mPreview.setSurfaceTextureListener(this);

        saveFileButton = (ImageButton)findViewById(R.id.save_button) ;
        uploadFile = (ImageButton)findViewById(R.id.upload) ;

        Intent i = getIntent() ;
        FilePathIntent = (File)i.getExtras().get("filePath");

    }


    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Surface s = new Surface(surface);

        try {
            mMediaPlayer= new MediaPlayer();
            mMediaPlayer.setDataSource(FilePathIntent.getAbsolutePath());

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

            case R.id.upload: MultiPartUpload();
                                break;
            case R.id.save_button: SaveExternal();
                                break;
            case R.id.setLocation:
                                break;
        }
    }

    void MultiPartUpload(){
        final String serverUrlString = " " ;
        final String paramNameString = " ";

        final String filesToUploadString = FilePathIntent.getAbsolutePath() ;
        try {
            final String filename = getFilename(filesToUploadString);

            String uploadID = new MultipartUploadRequest(this, serverUrlString)
                    .addFileToUpload(filesToUploadString, paramNameString)
                    .setNotificationConfig(getNotificationConfig(filename))
                    .setCustomUserAgent(USER_AGENT)
                    .setAutoDeleteFilesAfterSuccessfulUpload(true)
                    .setUsesFixedLengthStreamingMode(true)
                    .setMaxRetries(3)
                    .startUpload();



            // these are the different exceptions that may be thrown
        } catch (FileNotFoundException exc) {
            exc.printStackTrace();
        } catch (IllegalArgumentException exc) {
            Log.d("ERR", exc.getMessage() + " ") ;

        } catch (MalformedURLException exc) {
            Log.d("ERR", exc.getMessage() ) ;
        }
    }

    private UploadNotificationConfig getNotificationConfig(String filename) {

        return new UploadNotificationConfig()
                .setIcon(R.drawable.ic_cast_on_light)
                .setTitle(filename)
                .setInProgressMessage(getString(R.string.uploading))
                .setCompletedMessage(getString(R.string.upload_success))
                .setErrorMessage(getString(R.string.upload_error))
                .setAutoClearOnSuccess(true)
                .setClickIntent(new Intent(this, MainVideoPlayUploadActivity.class))
                .setClearOnAction(true)
                .setRingToneEnabled(true);
    }




        /*
        * UPLOAD TO SERVER
        * SHOW PROGRESS IN NOTIFICATION
        * SET UPLOADING ON UI
        * COMPRESS FILE
        * */



    private void SaveExternal(){
        File source = FilePathIntent ;
        File destination = FileHelper.createVideoFile(getApplicationContext(), FileHelper.generateVideoFileName(), FileHelper.TYPE_EXTERNAL);

        try {
            copyVideo(source, destination);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void copyVideo(File src, File dst) throws IOException{

        FileInputStream in = new FileInputStream(src) ;
        FileOutputStream out = new FileOutputStream(dst) ;

        FileChannel inChannel = in.getChannel() ;
        FileChannel outChannel = out.getChannel() ;
        inChannel.transferTo(0, inChannel.size(), outChannel) ;
        in.close();
        out.close();
        out.close();
    }

    private String getFilename(String filepath) {
        if (filepath == null)
            return null;

        final String[] filepathParts = filepath.split("/");

        return filepathParts[filepathParts.length - 1];
    }


    /*//If we need to implement
    void onCancelUploadClick(String uploadId) {
        UploadService.stopUpload(uploadId);
    }*/

}
