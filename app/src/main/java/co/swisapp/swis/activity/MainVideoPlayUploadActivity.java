package co.swisapp.swis.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageButton;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.channels.FileChannel;

import co.swisapp.swis.BuildConfig;
import co.swisapp.swis.R;
import co.swisapp.swis.utility.Constants;
import co.swisapp.swis.utility.FileHelper;
import co.swisapp.swis.utility.UploadUtils;

public class MainVideoPlayUploadActivity extends AppCompatActivity implements
        TextureView.SurfaceTextureListener, MediaPlayer.OnCompletionListener, View.OnClickListener{

    private MediaPlayer mMediaPlayer;
    private TextureView mPreview;
    private ImageButton saveFileButton;
    private ImageButton uploadFile;
    private String filePathIntent;
    private Integer intentId ;
    private boolean isPlaying = true ;
    private static String TAG = "Err_Play_Upload_Log" ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_video_play_upload);
        initialize();

        mPreview.setSurfaceTextureListener(this);
        uploadFile.setOnClickListener(this);
        saveFileButton.setOnClickListener(this);
        mPreview.setOnClickListener(this);

        handleIntent() ;
    }

    private void handleIntent(){
        Intent intent = getIntent();
        intentId = (int)intent.getExtras().get(Constants.INTENT_ID) ;
        if(intent.getData() == null && intentId == 1) {
            filePathIntent = (String) intent.getExtras().get(Constants.INTENT_FILEPATH_PARAM);
        }
    }

    private void initialize() {
        /**
         * Sets the namespace used to broadcast events. As given in the library documentation.
         */
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;

        mPreview = (TextureView) findViewById(R.id.play_upload_tv_texture);
        saveFileButton = (ImageButton) findViewById(R.id.play_upload_ibtn_save);
        uploadFile = (ImageButton) findViewById(R.id.play_upload_ibtn_upload);
    }

    @Override
    protected void onResume() {
        super.onResume();
        uploadServiceBroadcastReceiver.register(getApplicationContext());
        //TODO: Register broadcast in Manifest file
    }

    @Override
    protected void onPause() {
        mMediaPlayer.stop();
        uploadServiceBroadcastReceiver.unregister(getApplicationContext());
        super.onPause();
    }

    /**
     * Setting up mediaPlayer on the current surface to play the recorded video.
     *
     * @param surface SurfaceTexture on which rendering will take place
     * @param width   obtained width
     * @param height  obtained height
     */
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Surface s = new Surface(surface);

        try {
            mMediaPlayer = new MediaPlayer();
            if(filePathIntent != null){

                mMediaPlayer.setDataSource(filePathIntent);

                mMediaPlayer.setSurface(s);
                mMediaPlayer.prepare();
                mMediaPlayer.setLooping(true);
            /*mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnVideoSizeChangedListener(this);*/
                mMediaPlayer.setOnCompletionListener(this);

                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.start();
            }

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
        switch (v.getId()) {
            case R.id.play_upload_ibtn_upload:
                multiPartUpload();
                break;
            case R.id.play_upload_ibtn_save:
                new Thread() {
                    @Override
                    public void run() {
                        saveExternal();
                    }
                }.start();
                break;
            case R.id.play_upload_ibtn_location:
                break;
            case R.id.play_upload_tv_texture:
                if(isPlaying){
                    mMediaPlayer.pause();
                    isPlaying = false ;
                }
                else{
                    mMediaPlayer.start();
                    isPlaying = true ;
                }
                break;
        }
    }

    /**
     * Method handling the upload to server functionality, as part of the library - 'gotev'
     * Upload service.
     */
    void multiPartUpload() {


        try {
            String uploadID = new MultipartUploadRequest(this, Constants.URL_VIDEO_UPLOAD)
                    .addFileToUpload(filePathIntent, Constants.KEY_UPLOAD_VIDEO_PARAMETER)
                    .setNotificationConfig(getNotificationConfig(Constants.TEXT_UPLOAD_NOTIFICATION_TITLE))
                    .setCustomUserAgent(Constants.USER_AGENT)
                    .setAutoDeleteFilesAfterSuccessfulUpload(true)
                    .setUsesFixedLengthStreamingMode(true)
                    .setMaxRetries(3)
                    .startUpload();
        } catch (FileNotFoundException exc) {
            exc.printStackTrace();
        } catch (IllegalArgumentException exc) {
            Log.d(TAG, exc.getMessage() + " ");
        } catch (MalformedURLException exc) {
            Log.d(TAG, exc.getMessage());
        }
    }

    /**
     * Setting the persistent notification when the upload takes place.
     *
     * @param title header name
     * @return Config
     */
    private UploadNotificationConfig getNotificationConfig(String title) {

        Intent here = new Intent(this, MainVideoPlayUploadActivity.class) ;
        here.putExtra(Constants.INTENT_ID, 0) ;

        return new UploadNotificationConfig()
                .setIcon(R.drawable.ic_cast_on_light) //TODO: Replace with swisapp logo
                .setTitle(title)
                .setInProgressMessage(getString(R.string.uploading))
                .setCompletedMessage(getString(R.string.upload_success))
                .setErrorMessage(getString(R.string.upload_error))
                .setAutoClearOnSuccess(true)
                .setClickIntent(here)
                .setClearOnAction(true)
                .setRingToneEnabled(true);
    }

    private void saveExternal() {
        File source = new File(filePathIntent) ;
        File destination = FileHelper.createVideoFile(getApplicationContext(), FileHelper.generateVideoFileName(), FileHelper.TYPE_EXTERNAL);

        try {
            copyVideo(source, destination);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Copies file from the existing internal directory and saves it to the external app directory
     *
     * @param src Original file in root directory
     * @param dst New file in storage directory
     * @throws IOException
     */
    private void copyVideo(File src, File dst)  throws IOException {

        FileInputStream in = new FileInputStream(src);
        FileOutputStream out = new FileOutputStream(dst);

        FileChannel inChannel = in.getChannel();
        FileChannel outChannel = out.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        in.close();
        out.close();
        out.close();
    }

    private final UploadUtils uploadServiceBroadcastReceiver = new
            UploadUtils(){
                @Override
                public void onReceive(Context context, Intent intent) {
                    super.onReceive(context, intent);
                }

                @Override
                public void register(Context context) {
                    super.register(context);
                }

                @Override
                public void unregister(Context context) {
                    super.unregister(context);
                }

                @Override
                public void onError(String uploadId, Exception exception) {
                    super.onError(uploadId, exception);
                }

                @Override
                public void onCompleted(String uploadId, int serverResponseCode, byte[] serverResponseBody) {
                    super.onCompleted(uploadId, serverResponseCode, serverResponseBody);

                    Log.i(TAG, "Upload with ID " + uploadId
                            + " has been completed with HTTP " + serverResponseCode
                            + ". Response from server: "
                            + new String(serverResponseBody));

                }

                @Override
                public void onCancelled(String uploadId) {
                    super.onCancelled(uploadId);
                }
            };
}
