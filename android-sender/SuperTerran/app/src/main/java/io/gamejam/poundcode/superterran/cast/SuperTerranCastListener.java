package io.gamejam.poundcode.superterran.cast;

import android.util.Log;

import com.google.android.gms.cast.Cast;

/**
 * Created by chris_pound on 8/8/15.
 */
public class SuperTerranCastListener extends Cast.Listener {
    @Override
    public void onApplicationDisconnected(int statusCode) {
        Log.d("CastListener", "Cast.Listener.onApplicationDisconnected: " + statusCode);

    }
}
