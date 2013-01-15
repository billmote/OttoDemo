package com.cadg.androidfu.otto;

import android.app.Application;
import android.util.Log;

import com.cadg.androidfu.otto.model.QuantityManager;

/**
 * This application was stubbed together as a proof of concept by Trey Robinson. I leveraged his foundation for this
 * hardened demonstration. Error handling has been added. More extensive use of the bus to get/put/cache values has been
 * implemented.
 * 
 * <pre>
 *      20130115 -- Code Review
 * </pre>
 * 
 * @author Bill Mote & Trey Robinson
 * 
 */
public class OttoApplication extends Application {

    private static final String TAG = OttoApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        Log.v(TAG, "onCreate()");
        // Our QuantityManager is just a POJO so it has no lifecycle events.  Register it when the application is created.
        QuantityManager.getInstance().register();
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        // Our QuantityManager is just a POJO so it has no lifecycle events.  Unregister it when the application is terminated.
        Log.v(TAG, "onTerminate()");
        QuantityManager.getInstance().unregister();
        super.onTerminate();
    }

    public OttoApplication() {
        Log.v(TAG, "OttoApplication()");
    }

}
