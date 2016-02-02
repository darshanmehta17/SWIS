package co.swisapp.swis.fragment;


import android.app.Fragment;
import android.app.VoiceInteractor;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
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

            case R.id.upload: new uploadToServer() ;
                                break;
            case R.id.save:
                                break;
            case R.id.setLocation:
                                break;

            default:
                return;


        }
    }

    private class uploadToServer extends AsyncTask<Void, Integer, String>{
        public uploadToServer() {
        /*
        * UPLOAD TO SERVER
        * SHOW PROGRESS IN NOTIFICATION
        * SET UPLOADING ON UI
        * COMPRESS FILE
        * */


        }

        @Override
        protected String doInBackground(Void... params) {
            String responseString = null;

            //HttpClient httpclient = new DefaultHttpClient();
            //HttpPost httppost = new HttpPost(Config.FILE_UPLOAD_URL);

            try {
               /* AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new RecoverySystem.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });*/


            /* TODO: Set File Path properly */
                File sourceFile = new File(CameraHelper.getVideoFile(getActivity()).getAbsolutePath());

                /*// Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server
                entity.addPart("website",
                        new StringBody("www.androidhive.info"));
                entity.addPart("email", new StringBody("abc@gmail.com"));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }*/

            /*} catch(ClientProtocolException e) {
                responseString = e.toString();*/
            /*} catch (IOException e) {
                responseString = e.toString();*/
            }catch (Exception e){
                e.printStackTrace();
            }

            return responseString;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

}
