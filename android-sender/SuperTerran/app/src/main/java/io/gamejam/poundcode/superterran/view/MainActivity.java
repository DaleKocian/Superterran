package io.gamejam.poundcode.superterran.view;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.cast.ApplicationMetadata;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.CastMediaControlIntent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.IOException;

import io.gamejam.poundcode.superterran.R;
import io.gamejam.poundcode.superterran.cast.CastMessageService;
import io.gamejam.poundcode.superterran.cast.DeviceSelectedCallback;
import io.gamejam.poundcode.superterran.cast.SuperTerranCastListener;
import io.gamejam.poundcode.superterran.cast.SuperTerranMediaRouterCallback;
import io.gamejam.poundcode.superterran.cast.SuperTerranChannel;

import static io.gamejam.poundcode.superterran.cast.CastConstants.CAST_APP_ID;

/**
 * Created by chris_pound on 8/8/15.
 */
public class MainActivity extends AppCompatActivity implements DeviceSelectedCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private MediaRouter mMediaRouter;
    private MediaRouteSelector mMediaRouteSelector;
    private Object mSelectedDevice;
    private SuperTerranMediaRouterCallback mMediaRouterCallback;
    private GoogleApiClient mApiClient;
    private Cast.Listener mCastClientListener;
    private boolean mApplicationStarted;
    private SuperTerranChannel mSuperTerranChannel;

    @Override
    protected void onStart() {
        super.onStart();
        mMediaRouter.addCallback(mMediaRouteSelector, mMediaRouterCallback,
                MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY);
    }

    @Override
    protected void onStop() {
        mMediaRouter.removeCallback(mMediaRouterCallback);
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMediaRouter = MediaRouter.getInstance(getApplicationContext());
        mMediaRouterCallback = new SuperTerranMediaRouterCallback(this);
        mMediaRouteSelector = new MediaRouteSelector.Builder()
                .addControlCategory(CastMediaControlIntent.categoryForCast(CAST_APP_ID))
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cast, menu);
        MenuItem mediaRouteMenuItem = menu.findItem(R.id.media_route_menu_item);
        MediaRouteActionProvider mediaRouteActionProvider =
                (MediaRouteActionProvider) MenuItemCompat.getActionProvider(mediaRouteMenuItem);
        mediaRouteActionProvider.setRouteSelector(mMediaRouteSelector);
        return true;
    }

    @Override
    public void onDeviceSelected(Object selectedDevice, String routeId) {
        this.mSelectedDevice = selectedDevice;
        mCastClientListener = new SuperTerranCastListener();
        Cast.CastOptions.Builder apiOptionsBuilder = Cast.CastOptions
                .builder((CastDevice) mSelectedDevice, mCastClientListener);

        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(Cast.API, apiOptionsBuilder.build())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mApiClient.connect();

    }

    @Override
    public void onConnected(Bundle bundle) {
        // TODO: 8/8/15
        connectToChannel();
        CastMessageService.sendMessage("COME ON AND SLAM AND WALK INTO THE JAM",mSuperTerranChannel ,mApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Could not connect to cast: " + connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();
    }

    public void connectToChannel() {
        Cast.CastApi.launchApplication(mApiClient, CAST_APP_ID, false)
                .setResultCallback(
                        new ResultCallback<Cast.ApplicationConnectionResult>() {
                            @Override
                            public void onResult(Cast.ApplicationConnectionResult result) {
                                Status status = result.getStatus();
                                if (status.isSuccess()) {
                                    ApplicationMetadata applicationMetadata =
                                            result.getApplicationMetadata();
                                    String sessionId = result.getSessionId();
                                    String applicationStatus = result.getApplicationStatus();
                                    boolean wasLaunched = result.getWasLaunched();

                                    mApplicationStarted = true;

                                    mSuperTerranChannel = new SuperTerranChannel();
                                    try {
                                        Cast.CastApi.setMessageReceivedCallbacks(mApiClient,
                                                mSuperTerranChannel.getNamespace(),
                                                mSuperTerranChannel);
                                    } catch (IOException e) {
                                        Log.e(TAG, "Exception while creating channel", e);
                                    }
                                }
                            }
                        });
    }
}
