package ru.otus.akn.project.gwt.client.constants;

import com.google.gwt.i18n.client.Messages;

public interface ApplicationMessages extends Messages {

    @DefaultMessage("Copyright © {0} mfr-group.com. Все права защищены.")
    String footerLabelText(String currentYear);
}
