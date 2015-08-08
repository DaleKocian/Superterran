package io.gamejam.poundcode.superterran.cast;

import android.support.v7.media.MediaRouter;

import com.google.android.gms.cast.CastDevice;

/**
 * Created by chris_pound on 8/8/15.
 */
public class SuperTerranMediaRouterCallback extends MediaRouter.Callback {

    private DeviceSelectedCallback mCallback;

    public SuperTerranMediaRouterCallback(DeviceSelectedCallback mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo info) {
        Object selectedDevice = CastDevice.getFromBundle(info.getExtras());
        String routeId = info.getId();
        mCallback.onDeviceSelected(selectedDevice, routeId);

    }

    @Override
    public void onRouteUnselected(MediaRouter router, MediaRouter.RouteInfo info) {
        //mSelectedDevice = null;
        // TODO: 8/8/15 implement unselected
    }
}
