package ru.otus.akn.project.filters;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;

import javax.servlet.FilterChain;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebFilter(filterName = "BrowserVersionFilter", urlPatterns = "/")
public class BrowserVersionFilter extends HttpFilter {

    private static final String BROWSER_OK = "BROWSER_OK";
    private static final Logger LOGGER = Logger.getLogger(BrowserVersionFilter.class.getSimpleName());

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) {

        boolean foundCookie = false;
        boolean versionIsOld = false;

        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(BROWSER_OK)) {
                    foundCookie = true;
                    break;
                }
            }

            if (!foundCookie) {
                UserAgent userAgent = UserAgent.parseUserAgentString(req.getHeader("User-Agent"));
                Browser browser = userAgent.getBrowser();
                Version browserVersion = userAgent.getBrowserVersion();

                if (browser != null && browserVersion != null) {
                    browserVersion.getVersion();

                    if (browser.getGroup().equals(Browser.IE)) {
                        versionIsOld = CheckVersion(browserVersion, 10);
                    } else if (browser.getGroup().equals(Browser.CHROME)) {
                        versionIsOld = CheckVersion(browserVersion, 50);
                    } else if (browser.getGroup().equals(Browser.FIREFOX)) {
                        versionIsOld = CheckVersion(browserVersion, 45);
                    } else if (browser.getGroup().equals(Browser.OPERA)) {
                        versionIsOld = CheckVersion(browserVersion, 38);
                    }

                    if (!versionIsOld) {
                        Cookie cookie = new Cookie(BROWSER_OK, "true");
                        res.addCookie(cookie);
                    } else {
                        Cookie cookie = new Cookie(BROWSER_OK, "false");
                        res.addCookie(cookie);
                    }
                }
            }
        }

        try {
            super.doFilter(req, res, chain);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Filtering exception", e);
        }
    }

    private boolean CheckVersion(Version browserVersion, int i) {
        int ieVersion = Integer.parseInt(browserVersion.getMajorVersion());
        return ieVersion < i;
    }

    @Override
    public void destroy() {
    }
}
