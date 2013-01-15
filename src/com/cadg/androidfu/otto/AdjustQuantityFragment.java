package com.cadg.androidfu.otto;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.squareup.otto.Subscribe;

/**
 * AdjustQuantityFragment is the portion of our screen where the user enters data.
 * 
 * <pre>
 *      20130115 -- Code Review
 * </pre>
 * 
 * @author bill.mote
 * 
 */
public class AdjustQuantityFragment extends BaseFragment implements OnClickListener {

    private static final String TAG = AdjustQuantityFragment.class.getSimpleName();
    private EditText _quantityEditText;
    private Button _submitQuantityButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView()");
        View fragment = inflater.inflate(R.layout.fragment_adjust_qty, container, false);
        _quantityEditText = (EditText) fragment.findViewById(R.id.quantityEditText);
        _submitQuantityButton = (Button) fragment.findViewById(R.id.submitButton);
        _submitQuantityButton.setOnClickListener(this);

        /*
         * This TextWatcher() along with the type set to "number" in the layout acts as crude client side validation.
         * Note that we're NOT validating the number entered, but we know that we're never going to submit anything
         * other than 0..n to the bus.
         */
        _quantityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Only enable the button if the field contains data.
                if (s.toString().length() > 0) {
                    enableSubmitButton(true);
                } else {
                    enableSubmitButton(false);
                }
            }
        });
        return fragment;
    }

    /*
     * Having the class implement onClickListener() is an alternative to creating anonymous inner classes for
     * _submitQuantityButton.setOnClickListener(new onClickListener() { ...
     */
    @Override
    public void onClick(View v) {
        Log.v(TAG, "onClick()");
        switch (v.getId()) {
            case R.id.submitButton:
                addQuantity();
                closeSoftKeyboard(getActivity());
                break;
            default:
                break;
        }
    }

    /**
     * Set the state of the submit button.
     * 
     * @param enableButton
     *            should the button be enabled? true/false
     */
    private void enableSubmitButton(boolean enableButton) {
        Log.v(TAG, "enableSubmitButton()");
        _submitQuantityButton.setEnabled(enableButton);
    }

    /**
     * Add the value from our to the singleton manager class.
     */
    private void addQuantity() {
        Log.v(TAG, "addQuantity()");
        String quantityString = _quantityEditText.getText().toString();
        int quantity = 0;
        try {
            quantity = Integer.parseInt((quantityString == null || quantityString.equals("")) ? "0" : quantityString);
        } catch (NumberFormatException nfe) {
            //FIXME Extract Android String and explain why.
            _quantityEditText.setError(String.format("Enter a number between 0 and %d", Integer.MAX_VALUE));
            _quantityEditText.requestFocus();
            // Different from the message above; the user will never see this text so it's okay to be in-line.
            Log.e(TAG, "Value not a number or outside the bounds of an INT32.", nfe);
        }
        post(Integer.valueOf(quantity));
        clearQuantityInputField();
    }

    /**
     * Clear the edit text so the hint is displayed.
     */
    private void clearQuantityInputField() {
        Log.v(TAG, "clearQuantityInputField()");
        _quantityEditText.setText("");
    }

    /**
     * A helper method to make sure the keyboard gets closed when the value is submitted.
     * 
     * @param fragmentActivity
     *            the context of the parent activity.
     */
    private static void closeSoftKeyboard(FragmentActivity fragmentActivity) {
        Log.v(TAG, "closeSoftKeyboard()");
        InputMethodManager inputManager = (InputMethodManager) fragmentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow((null == fragmentActivity.getCurrentFocus()) ? null : fragmentActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * Just to illustrate that our method is completely decoupled from anything and it's the annotation + the single
     * method argument that determine whether or not it should respond to data on the bus.
     * 
     * This event WILL NOT get called as it is subscribed to the bus listening for a String().
     * 
     * @param errorMessage
     */
    @Subscribe
    public void handleErrorEvent(String errorMessage) {
        Log.v(TAG, "processErrorEvent()");
        Log.d(TAG, String.format("The value published to the bus was: %s", errorMessage));
        if (_quantityEditText != null && errorMessage != null) {
            _quantityEditText.setError((errorMessage.equals("")) ? null : errorMessage);
        }
    }
}
