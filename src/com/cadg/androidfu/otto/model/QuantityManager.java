package com.cadg.androidfu.otto.model;

import java.util.Locale;

import android.util.Log;

import com.squareup.otto.Bus;
import com.squareup.otto.BusProvider;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

/**
 * QuantityManager is our singleton to manage quantity.
 * 
 * <pre>
 *      20130114 -- Code Review
 * </pre>
 * 
 * @author bill.mote
 * 
 */
public class QuantityManager {

    private static final String TAG = QuantityManager.class.getSimpleName();
    private static final int INITIAL_QUANTITY_VALUE = 0;

    private static QuantityManager _instance;
    private static Bus _bus;

    private int _quantity;

    /**
     * getInstance()
     * 
     * @return the only instance of QuantityManager
     */
    public static QuantityManager getInstance() {
        Log.v(TAG, "getInstance()");
        if (_instance == null) {
            _instance = new QuantityManager();
        }
        return _instance;
    }

    /**
     * Hidden empty constructor so you have to use getInstance().
     */
    private QuantityManager() {
        Log.v(TAG, "QuantityManager()");
        _quantity = INITIAL_QUANTITY_VALUE;
        _bus = BusProvider.getInstance();
    }

    /**
     * Register QualityManager on the Otto bus.
     */
    public void register() {
        Log.v(TAG, "register()");
        _bus.register(_instance);
    }

    /**
     * Unregister QualityManager on the Otto bus.
     */
    public void unregister() {
        Log.v(TAG, "unregister()");
        _bus.unregister(_instance);
    }

    /**
     * addQuantity accepts an int and publishes an event to the bus.
     * 
     * @param qty
     *            the int to add to the existing value of _quantity.
     */
    @Subscribe
    public void addQuantity(Integer qty) {
        Log.v(TAG, "addQuantity()");
        String errorMsg = "";
        int quantity = _quantity;
        //FIXME No MAGIC values, replace 0 with a constant -- DEFAULT_VALUE_TO_ADD = 0
        _quantity += ((_quantity + qty <= Integer.MAX_VALUE && _quantity + qty > 0) ? qty : 0);
        if (_quantity == quantity && qty != 0) {
            //FIXME Extract Android String
            errorMsg = String.format(Locale.ENGLISH, "Value %d + %d was outside the bounds of an INT32.", quantity, qty);
            Log.e(TAG, errorMsg);
            post(errorMsg);
            return;
        }
        post(changeQuantityEvent());
    }

    /**
     * Our setter so _quantity can be private.
     * 
     * @param qty
     *            the new value of _quantity.
     */
    @Subscribe
    public void setQuantity(Boolean resetValue) {
        if (resetValue) {
            this._quantity = INITIAL_QUANTITY_VALUE;
            post(changeQuantityEvent());
        }
    }

    /**
     * Create a new QuantityChangedEvent for the bus.
     * 
     * @return a QuantityChangedEvent with our new value of _quantity.
     */
    @Produce
    public QuantityChangedEvent changeQuantityEvent() {
        Log.v(TAG, "changeQuantityEvent()");
        return new QuantityChangedEvent(this._quantity);
    }

    /**
     * Our Bus Post helper method.
     * 
     * @param event
     *            to publish on the bus.
     */
    private void post(Object event) {
        Log.v(TAG, "post()");
        BusProvider.getInstance().post(event);
    }

}
