package io.gamejam.poundcode.superterran.cast;

import android.util.Log;

import com.google.android.gms.cast.Cast;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by chris_pound on 8/8/15.
 */
public class CastMessageService {

    public static void sendMessage(String message, SuperTerranChannel mChannel, GoogleApiClient apiClient) {
        if (apiClient != null && mChannel != null) {
            try {
                Cast.CastApi.sendMessage(apiClient, mChannel.getNamespace(), message)
                        .setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status result) {
                                        if (!result.isSuccess()) {
                                            Log.e("CastMessageService", "Sending message failed");
                                        }
                                    }
                                });
            } catch (Exception e) {
                Log.e("CastMessageService", "Exception while sending message", e);
            }
        }
    }
}
