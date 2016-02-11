package co.swisapp.swis.utility;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import net.gotev.uploadservice.UploadServiceBroadcastReceiver;

public class UploadUtils extends UploadServiceBroadcastReceiver {

    public UploadUtils() {
        super();
    }

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
    public void onProgress(String uploadId, int progress) {
        super.onProgress(uploadId, progress);
    }

    @Override
    public void onProgress(String uploadId, long uploadedBytes, long totalBytes) {
        super.onProgress(uploadId, uploadedBytes, totalBytes);
    }

    @Override
    public void onError(String uploadId, Exception exception) {
        super.onError(uploadId, exception);
    }

    @Override
    public void onCompleted(String uploadId, int serverResponseCode, byte[] serverResponseBody) {
        super.onCompleted(uploadId, serverResponseCode, serverResponseBody);
    }

    @Override
    public void onCancelled(String uploadId) {
        super.onCancelled(uploadId);
    }
}
