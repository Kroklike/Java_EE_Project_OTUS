package ru.otus.akn.project.gwt.client.widget;

import com.google.gwt.cell.client.AbstractInputCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import java.util.ArrayList;
import java.util.List;

public class DynamicSelectionCell extends AbstractInputCell<String, String> {

    interface Template extends SafeHtmlTemplates {
        @Template("<option value=\"{0}\">{0}</option>")
        SafeHtml deselected(String option);

        @Template("<option value=\"{0}\" selected=\"selected\">{0}</option>")
        SafeHtml selected(String option);
    }

    private static Template template;
    private List<String> cachedValues = new ArrayList<>();

    public DynamicSelectionCell() {
        super("change");
        if (template == null) {
            template = GWT.create(Template.class);
        }
    }

    public void addOptions(List<String> cached) {
        cachedValues = cached;
    }

    public void removeOptions(List<String> toRemove) {
        cachedValues.removeAll(toRemove);
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, String value,
                               NativeEvent event, ValueUpdater<String> valueUpdater) {
        super.onBrowserEvent(context, parent, value, event, valueUpdater);
        String type = event.getType();
        if ("change".equals(type)) {
            Object key = context.getKey();
            SelectElement select = parent.getFirstChild().cast();
            String newValue = cachedValues.get(select.getSelectedIndex());
            setViewData(key, newValue);
            finishEditing(parent, newValue, key, valueUpdater);
            if (valueUpdater != null) {
                valueUpdater.update(newValue);
            }
        }
    }

    @Override
    public void render(Context context, String value, SafeHtmlBuilder sb) {
        // Get the view data.
        Object key = context.getKey();
        String viewData = getViewData(key);
        if (viewData != null && viewData.equals(value)) {
            clearViewData(key);
            viewData = null;
        }

        int selectedIndex = getSelectedIndex(viewData == null ? value : viewData);
        sb.appendHtmlConstant("<select tabindex=\"-1\">");
        int index = 0;
        try {
            for (String option : cachedValues) {
                if (index++ == selectedIndex) {
                    sb.append(template.selected(option));
                } else {
                    sb.append(template.deselected(option));
                }
            }
        } catch (Exception e) {
            System.out.println("error");
        }
        sb.appendHtmlConstant("</select>");
    }

    private int getSelectedIndex(String value) {
        if (cachedValues == null) {
            return -1;
        }
        return cachedValues.indexOf(value);
    }
}
