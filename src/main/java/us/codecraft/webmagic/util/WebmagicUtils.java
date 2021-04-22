package us.codecraft.webmagic.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebmagicUtils {

    public static void addLinkTargetRequest(Page page, List<Pattern> targetUrlPatterns) {
        Set<String> aLinks = new HashSet(); // 把a链接全部获取出来，page.getHtml().links()只能获取到url，获取不了a链接的名称
        if (targetUrlPatterns==null || targetUrlPatterns.size()==0) return;
        Elements elements = page.getHtml().getDocument().getElementsByTag("a");
        for (Element element:elements) {
            String url = element.attr("abs:href");
            for (Pattern targetUrlPattern : targetUrlPatterns) {
                Matcher matcher = targetUrlPattern.matcher(url);
                if (matcher.find()) {
                    aLinks.add(url);
                    Request request = new Request(url);
                    request.putExtra("aLinkName", element.text());
                    page.addTargetRequest(request);
                }
            }
        }
        List<String> links = page.getHtml().links().all();
        for (String link : links) {
            if (aLinks.contains(link)==false) {
                for (Pattern targetUrlPattern : targetUrlPatterns) {
                    Matcher matcher = targetUrlPattern.matcher(link);
                    if (matcher.find()) {
                        page.addTargetRequest(link);
                    }
                }
            }
        }
    }

    /** 要爬取的URL有一些正则表达式通配符，要转义 */
    public static List<Pattern> convertUrlPattern(String... urls) {
        List<Pattern> patterns = new ArrayList();
        if (urls==null) return patterns;
        for (String url:urls) {
            if (url!=null && url.length()>0) {
                patterns.add(Pattern.compile(url.replace(".", "\\.").replace("*", "[^\"'#]*")));
            }
        }
        return patterns;
    }

    public static void setLoggerLevel() {
        LoggerContext logContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        logContext.getLogger("org.apache.http").setLevel(Level.INFO);
        logContext.getLogger("us.codecraft.webmagic.scheduler.RedisScheduler").setLevel(Level.INFO);
        logContext.getLogger("com.zaxxer.hikari").setLevel(Level.INFO);
        logContext.getLogger("us.codecraft.webmagic.utils.CharsetUtils").setLevel(Level.INFO);
//        logContext.getLogger("org.springframework.jdbc").setLevel(Level.INFO);
//        logContext.getLogger("us.codecraft.webmagic.downloader.HttpClientDownloader").setLevel(Level.WARN);
    }
}
