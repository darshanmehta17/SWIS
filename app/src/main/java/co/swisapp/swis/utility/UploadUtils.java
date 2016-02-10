package co.swisapp.swis.utility;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import net.gotev.uploadservice.UploadServiceBroadcastReceiver;

public class UploadUtils extends UploadServiceBroadcastReceiver {
    private final UploadServiceBroadcastReceiver uploadServiceBroadcastReceiver = new
            UploadServiceBroadcastReceiver(){
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

                    Log.i("TEST", "onError of Upload", exception) ;
                }

                @Override
                public void onCompleted(String uploadId, int serverResponseCode, byte[] serverResponseBody) {
                    super.onCompleted(uploadId, serverResponseCode, serverResponseBody);

                    Log.i("TEST", "Upload with ID " + uploadId
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
