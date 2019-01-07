package ru.otus.akn.statistics;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import org.codehaus.jettison.json.JSONObject;
import ru.otus.akn.project.util.EntityManagerControlGeneric;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
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
import java.util.List;
import java.util.Map;

import static ru.otus.akn.project.util.PersistenceUtil.MANAGER_FACTORY;

@WebServlet("/gatherStatistic")
public class StatisticServlet extends HttpServlet {

    private static final Map<String, Long> ROAD_MAP_WITH_IP_ADDRESSES = new HashMap<>();
    private static boolean isInit = false;

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

                UserAgent userAgent = UserAgent.parseUserAgentString(req.getHeader("User-Agent"));
                Browser browser = userAgent.getBrowser();

                String clientIp = getClientIp(req);

                BigDecimal insertedId = new EntityManagerControlGeneric<BigDecimal>(MANAGER_FACTORY) {
                    @Override
                    public BigDecimal requestMethod(EntityManager manager) {
                        StoredProcedureQuery query = manager
                                .createStoredProcedureQuery("save_statistic_info")
                                .registerStoredProcedureParameter("p_stat_mark", String.class, ParameterMode.IN)
                                .setParameter("p_stat_mark", resultStatMark)
                                .registerStoredProcedureParameter("p_page_name", String.class, ParameterMode.IN)
                                .setParameter("p_page_name", pageName == null ? "unknown" : pageName)
                                .registerStoredProcedureParameter("p_ip_address", String.class, ParameterMode.IN)
                                .setParameter("p_ip_address", !clientIp.isEmpty() ? clientIp : "unknown client ip")
                                .registerStoredProcedureParameter("p_browser_info", String.class, ParameterMode.IN)
                                .setParameter("p_browser_info", browser != null ? browser.getName() : "unknown browser")
                                .registerStoredProcedureParameter("p_visit_date", LocalDate.class, ParameterMode.IN)
                                .setParameter("p_visit_date", LocalDate.now())
                                .registerStoredProcedureParameter("p_cookies", String.class, ParameterMode.IN)
                                .setParameter("p_cookies", printCookiesInfo(req.getCookies()))
                                .registerStoredProcedureParameter("p_req_params", String.class, ParameterMode.IN)
                                .setParameter("p_req_params", printReqParams(req.getParameterMap()))
                                .registerStoredProcedureParameter("p_prev_stat_id", Long.class, ParameterMode.IN)
                                .setParameter("p_prev_stat_id", !clientIp.isEmpty() ? ROAD_MAP_WITH_IP_ADDRESSES.get(clientIp) : null)
                                .registerStoredProcedureParameter("ref_result", Class.class, ParameterMode.REF_CURSOR);
                        query.execute();
                        return (BigDecimal) query.getSingleResult();
                    }
                }.processRequest();

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

    private void restoreRoadMapByStatisticMark(String statisticMark) throws Exception {
        List<Object[]> roadMap = new EntityManagerControlGeneric<List<Object[]>>(MANAGER_FACTORY) {
            @Override
            public List<Object[]> requestMethod(EntityManager manager) {
                StoredProcedureQuery query = manager
                        .createStoredProcedureQuery("get_statistic_road_map")
                        .registerStoredProcedureParameter("p_stat_mark", String.class, ParameterMode.IN)
                        .setParameter("p_stat_mark", statisticMark)
                        .registerStoredProcedureParameter("stat_road_map", Class.class,
                                ParameterMode.REF_CURSOR);
                query.execute();
                return query.getResultList();
            }
        }.processRequest();

        for (Object[] map : roadMap) {
            ROAD_MAP_WITH_IP_ADDRESSES.put(String.valueOf(map[0]), ((BigDecimal) map[1]).longValue());
        }

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
