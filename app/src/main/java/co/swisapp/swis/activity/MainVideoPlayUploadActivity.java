package co.swisapp.swis.activity;


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
import co.swisapp.swis.utility.FileHelper;

public class MainVideoPlayUploadActivity extends AppCompatActivity implements
        TextureView.SurfaceTextureListener, MediaPlayer.OnCompletionListener, View.OnClickListener{

    private MediaPlayer mMediaPlayer;
    private TextureView mPreview;
    private ImageButton saveFileButton;
    private ImageButton uploadFile;
    private static final String USER_AGENT = "device-android-" + BuildConfig.VERSION_NAME;
    private File filePathIntent;
    private boolean isPlaying = true ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_video_play_upload);
        initialize();

        mPreview.setSurfaceTextureListener(this);
        uploadFile.setOnClickListener(this);
        saveFileButton.setOnClickListener(this);
        mPreview.setOnClickListener(this);

        Intent i = getIntent();
        filePathIntent = (File) i.getExtras().get("filePath");


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
    protected void onPause() {

        mMediaPlayer.pause();
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
            mMediaPlayer.setDataSource(filePathIntent.getAbsolutePath());

            mMediaPlayer.setSurface(s);
            mMediaPlayer.prepare();
            mMediaPlayer.setLooping(true);
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
        switch (v.getId()) {
            case R.id.play_upload_ibtn_upload:
                multiPartUpload();
                break;
            case R.id.play_upload_ibtn_save:
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        saveExternal();

                    }
                };
                thread.start();
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
        final String filesToUploadString = filePathIntent.getAbsolutePath();
        final String filename = getFilename(filesToUploadString);

        final String serverUrlString = "https://swis-vuln-1.c9users.io/api/photo";
        final String paramNameString = filename;

        try {
            String uploadID = new MultipartUploadRequest(this, serverUrlString)
                    .addFileToUpload(filesToUploadString, paramNameString)
                    .setNotificationConfig(getNotificationConfig(filename))
                    .setCustomUserAgent(USER_AGENT)
                    .setAutoDeleteFilesAfterSuccessfulUpload(true)
                    .setUsesFixedLengthStreamingMode(true)
                    .setMaxRetries(3)
                    .startUpload();
        } catch (FileNotFoundException exc) {
            exc.printStackTrace();
        } catch (IllegalArgumentException exc) {
            Log.d("ERR", exc.getMessage() + " ");
        } catch (MalformedURLException exc) {
            Log.d("ERR", exc.getMessage());
        }
    }

    /**
     * Setting the persistent notification when the upload takes place.
     *
     * @param filename header name
     * @return Config
     */
    private UploadNotificationConfig getNotificationConfig(String filename) {
        return new UploadNotificationConfig()
                .setIcon(R.drawable.ic_cast_on_light) //TODO: Replace with swisapp logo
                .setTitle(filename)
                .setInProgressMessage(getString(R.string.uploading))
                .setCompletedMessage(getString(R.string.upload_success))
                .setErrorMessage(getString(R.string.upload_error))
                .setAutoClearOnSuccess(true)
                .setClickIntent(new Intent(this, MainVideoPlayUploadActivity.class))
                .setClearOnAction(true)
                .setRingToneEnabled(true);
    }

    private void saveExternal() {
        File source = filePathIntent;
        File destination = FileHelper.createVideoFile(getApplicationContext(), FileHelper.generateVideoFileName(), FileHelper.TYPE_EXTERNAL);

        try {
            copyVideo(source, destination);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Copies file from the exisiting internal directory and saves it to the external app directory
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

    private String getFilename(String filepath) {
        if (filepath == null)
            return null;
        final String[] filepathParts = filepath.split("/");
        return filepathParts[filepathParts.length - 1];
    }

}
