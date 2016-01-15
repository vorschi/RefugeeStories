package at.ac.tuwien.inso.refugeestories.utils.components;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import at.ac.tuwien.inso.refugeestories.MainActivity;
import at.ac.tuwien.inso.refugeestories.domain.Prediction;

/**
 * Created by Amer Salkovic on 14.1.2016.
 */
public class CustomAutoCompleteTextView extends AutoCompleteTextView {

    private static final int MESSAGE_TEXT_CHANGED = 100;
    private static final int DEFAULT_AUTOCOMPLETE_DELAY = 500;

    private final Handler autoCompleteHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            CustomAutoCompleteTextView.super.performFiltering((CharSequence) msg.obj, msg.arg1);
        }
    };

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        autoCompleteHandler.removeMessages(MESSAGE_TEXT_CHANGED);
        autoCompleteHandler.sendMessageDelayed(autoCompleteHandler.obtainMessage(MESSAGE_TEXT_CHANGED, text), DEFAULT_AUTOCOMPLETE_DELAY);
    }

    @Override
    protected CharSequence convertSelectionToString(Object selectedItem) {
        Prediction prediction = (Prediction) selectedItem;
        return prediction.getDescription();
    }
}
