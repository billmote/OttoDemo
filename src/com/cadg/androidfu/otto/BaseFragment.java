package com.cadg.androidfu.otto;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.squareup.otto.BusProvider;

/**
 * BaseFragment registers fragments that extend this on the Bus and provides them a post() method.
 * 
 * <pre>
 *      20130115 -- Code Review
 * </pre>
 * 
 * @author Trey Robinson
 * 
 */
public class BaseFragment extends Fragment {

    private static final String TAG = BaseFragment.class.getSimpleName();

    @Override
    public void onResume() {
        Log.v(TAG, "onResume()");
        BusProvider.getInstance().register(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.v(TAG, "onPause()");
        BusProvider.getInstance().unregister(this);
        super.onPause();
    }

    protected void post(Object event) {
        Log.v(TAG, "post()");
        BusProvider.getInstance().post(event);
    }

}
