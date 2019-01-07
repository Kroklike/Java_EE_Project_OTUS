package ru.otus.akn.statistics.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class StatisticHandler extends BodyTagSupport {

    private String pageName;

    @Override
    public int doEndTag() throws JspException {
        try {
            HttpServletRequest pageContextRequest = (HttpServletRequest) pageContext.getRequest();
            if (pageContextRequest.getCookies() != null) {
                pageContextRequest.setAttribute("pageNameInfo", pageName);
                pageContext.include("/gatherStatistic", false);
            }
        } catch (Exception e) {
            //ignore
        }
        return super.doEndTag();
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
}
