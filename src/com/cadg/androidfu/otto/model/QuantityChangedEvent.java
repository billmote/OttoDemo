package com.cadg.androidfu.otto.model;

import android.util.Log;

/**
 * QuantityChangedEvent to be published on the bus.
 * 
 * <pre>
 *      20130115 -- Code Review
 * </pre>
 * 
 * @author bill.mote
 * 
 */
public class QuantityChangedEvent {

    private static final String TAG = QuantityChangedEvent.class.getSimpleName();
    private int _quantity;

    /**
     * Public constructor.
     * 
     * @param quantity
     *            the value of _quantity.
     */
    public QuantityChangedEvent(int quantity) {
        Log.v(TAG, "QuantityChangedEvent()");
        this._quantity = quantity;
    }

    /**
     * Public getter so we can keep _quantity private.
     * 
     * @return the value of _quantity.
     */
    public int getQuantity() {
        Log.v(TAG, "getQuantity()");
        return _quantity;
    }
}
