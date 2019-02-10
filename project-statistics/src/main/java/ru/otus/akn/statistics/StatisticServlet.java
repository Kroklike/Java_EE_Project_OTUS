package ru.otus.akn.statistics;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import org.codehaus.jettison.json.JSONObject;
import ru.otus.akn.project.db.entity.StatisticEntity;
import ru.otus.akn.project.ejb.api.stateless.StatisticService;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/gatherStatistic")
public class StatisticServlet extends HttpServlet {

    private static final Map<String, Long> ROAD_MAP_WITH_IP_ADDRESSES = new HashMap<>();
    private static boolean isInit = false;

    @EJB
    private StatisticService statisticService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        boolean isStatEnabled = Boolean.getBoolean("stat.enabled");

        if (isStatEnabled) {
            try {

                String statMark = System.getenv("STAT_MARK");
                String resultStatMark = statMark == null ? "unknown" : statMark;

                if (ROAD_MAP_WITH_IP_ADDRESSES.isEmpty() && !isInit) {
                    restoreRoadMapByStatisticMark(resultStatMark);
                }

                String pageName = (String) req.getAttribute("pageNameInfo");
                if (pageName == null) {
                    pageName = req.getParameter("pageName");
                }
                String finalPageName = pageName == null ? "unknown" : pageName;

                UserAgent userAgent = UserAgent.parseUserAgentString(req.getHeader("User-Agent"));
                Browser browser = userAgent.getBrowser();

                String clientIp = getClientIp(req);

                StatisticEntity statisticEntity = new StatisticEntity();
                statisticEntity.setStatisticMark(resultStatMark);
                statisticEntity.setPageName(finalPageName);
                statisticEntity.setIpAddress(!clientIp.isEmpty() ? clientIp : "unknown client ip");
                statisticEntity.setBrowserInfo(browser != null ? browser.getName() : "unknown browser");
                statisticEntity.setVisitDate(LocalDate.now());
                statisticEntity.setCookies(printCookiesInfo(req.getCookies()));
                statisticEntity.setRequestedParams(printReqParams(req.getParameterMap()));

                BigDecimal insertedId = statisticService.saveStatisticInfoUsingProcedure(statisticEntity,
                        !clientIp.isEmpty() ? ROAD_MAP_WITH_IP_ADDRESSES.get(clientIp) : null);

                if (!clientIp.isEmpty()) {
                    ROAD_MAP_WITH_IP_ADDRESSES.put(clientIp, insertedId.longValue());
                }

                JSONObject json = new JSONObject();
                json.put("insertedStatId", insertedId);
                resp.setContentType("application/json");
                try (PrintWriter out = resp.getWriter()) {
                    out.write(json.toString());
                }

            } catch (Exception e) {
                throw new ServletException(e);
            }
        }
    }

    private void restoreRoadMapByStatisticMark(String statisticMark) {
        ROAD_MAP_WITH_IP_ADDRESSES.putAll(statisticService.getRoadMapByStatMark(statisticMark));
        isInit = true;
    }

    private String printReqParams(Map<String, String[]> parameterMap) {
        StringBuilder builder = new StringBuilder();
        if (parameterMap == null || parameterMap.isEmpty()) {
            builder.append("Empty parameter map");
            return builder.toString();
        }
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            builder.append(" Key : " + entry.getKey() + " Value : " + Arrays.toString(entry.getValue()));
        }
        return builder.toString();
    }

    private String printCookiesInfo(Cookie[] cookies) {
        StringBuilder builder = new StringBuilder();
        if (cookies == null || cookies.length == 0) {
            builder.append("Empty cookies array");
            return builder.toString();
        }
        for (Cookie cookie : cookies) {
            builder.append(" Key : " + cookie.getName() + " Value : " + cookie.getValue());
        }
        return builder.toString();
    }

    private static String getClientIp(HttpServletRequest request) {
        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        return remoteAddr;
    }
}
