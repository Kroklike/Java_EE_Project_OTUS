package ru.otus.akn.project.gwt.client.model;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import lombok.Data;
import lombok.NonNull;

@Data
public class PartnerItemCreator {

    private final FlowPanel item = new FlowPanel();

    public PartnerItemCreator(@NonNull String imageURL,
                              @NonNull String partnerName,
                              @NonNull String partnerDescription,
                              @NonNull String siteURL,
                              @NonNull String knowMore) {

        item.addStyleName("material-style-gwt");
        item.addStyleName("a-style");

        Image partnerLogo = new Image(imageURL);
        item.add(partnerLogo);
        FlowPanel partnerInfo = new FlowPanel();
        partnerInfo.addStyleName("material-text");

        Label name = new Label();
        name.getElement().setInnerHTML("<h2>" + partnerName + "</h2>");

        Label description = new Label(partnerDescription);

        Anchor link = new Anchor();
        link.setHref(siteURL);
        link.setText(knowMore);

        partnerInfo.add(name);
        partnerInfo.add(description);
        partnerInfo.add(link);

        item.add(partnerInfo);
    }
}
