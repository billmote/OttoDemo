package com.cadg.androidfu.otto;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cadg.androidfu.otto.model.QuantityChangedEvent;
import com.squareup.otto.Subscribe;

/**
 * TotalFragment is the portion of the screen where the value is displayed.
 * 
 * <pre>
 *      20130115 -- Code Review
 * </pre>
 * 
 * @author bill.mote
 * 
 */
public class TotalFragment extends BaseFragment {

    private static final String TAG = TotalFragment.class.getSimpleName();
    private static final String KEY_STATE_QUANTITY_EDIT_TEXT = "com.cadg.androidfu.otto.TotalFragment.mTextView";

    private TextView _textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView()");
        View fragment = inflater.inflate(R.layout.fragment_display_qty, container, false);
        if (_textView == null) {
            _textView = (TextView) fragment.findViewById(R.id.quantityTV);
        } else {
            _textView.setText(_textView.getText().toString());
        }
        /*
         * We can dig the state of this field out of the bundle we stored --OR-- we can use a producer to get the last
         * cached value via Otto! Live coding time!
         */
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_STATE_QUANTITY_EDIT_TEXT)) {
                _textView.setText(savedInstanceState.getString(KEY_STATE_QUANTITY_EDIT_TEXT));
            }
        }
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState()");
        outState.putString(KEY_STATE_QUANTITY_EDIT_TEXT, _textView.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        //FIXME Get our value from an Otto Producer
    }

    /**
     * This is our method annotated to listen for QuantityChangedEvent.
     * 
     * @param quantityChangedBusEvent
     *            is the QuantityChangedEvent published to our Otto bus.
     */
    @Subscribe
    public void changeQuantity(QuantityChangedEvent quantityChangedBusEvent) {
        Log.v(TAG, "changeQuantity()");
        if (_textView != null) {
            _textView.setText(Integer.toString(quantityChangedBusEvent.getQuantity()));
        }
    }

    /**
     * Just to illustrate that our method is completely decoupled from anything and it's the annotation + the single
     * method argument that determine whether or not it should respond to data on the bus.
     * 
     * This event WILL get called and you'll see the entries in the LogCat.
     * 
     * @param ourChangeEvent
     */
    @Subscribe
    public void justAnotherMethod(QuantityChangedEvent ourChangeEvent) {
        Log.w(TAG, "justAnotherMethod(QuantityChangedEvent ourChangeEvent)");
        Log.d(TAG, String.format("The quantity published to the bus was: %d", ourChangeEvent.getQuantity()));
    }

    /**
     * Handle an error event from the bus.
     * 
     * @param errorMessage
     */
    @Subscribe
    public void handleErrorMessage(String errorMessage) {
        Log.e(TAG, "handleErrorMessage(String errorMessage)");
        Log.d(TAG, String.format("The value published to the bus was: %s", errorMessage));
        if (getActivity() != null && errorMessage != null && !errorMessage.equals("")) {
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}
