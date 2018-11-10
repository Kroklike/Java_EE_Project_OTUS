package ru.otus.akn.project.gwt.client.widget;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.ui.TextBox;

public class DefaultTextBox extends TextBox implements FocusHandler, BlurHandler {
    private final String defaultText;

    public DefaultTextBox(String defText) {
        defaultText = defText;
        setValue(defaultText);
        addFocusHandler(this);
        addBlurHandler(this);
    }

    public String getDefaultText() {
        return defaultText;
    }

    @Override
    public void onFocus(FocusEvent event) {
        if (this.getValue().equals(getDefaultText())) {
            this.setValue("");
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (this.getValue().isEmpty()) {
            this.setValue(getDefaultText());
        }
    }
}
