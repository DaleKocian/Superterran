package io.gamejam.poundcode.superterran.cast;

import android.util.Log;

import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;

/**
 * Created by chris_pound on 8/8/15.
 */
public class SuperTerranChannel implements Cast.MessageReceivedCallback {
    private static final String TAG = SuperTerranChannel.class.getSimpleName();

    @Override
    public void onMessageReceived(CastDevice castDevice, String s, String s1) {
        Log.d(TAG, "onMessageReceived: " + s);
    }

    public String getNamespace() {
        return CastConstants.CAST_CHANNEL_NAMESPACE;
    }
}
