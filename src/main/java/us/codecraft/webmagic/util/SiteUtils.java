package us.codecraft.webmagic.util;

import us.codecraft.webmagic.Site;

public class SiteUtils {
    public static Site getSite() {
        return Site.me()
                .setUserAgent("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)")
                .setRetryTimes(3)                // 网络传输下载html失败会触发
                .setCycleRetryTimes(3)           // 我重写了onDownloadSuccess的httpCode非200的逻辑，非200的code时，重发请求
                .setRetrySleepTime(500)          // 网络传输下载html失败才会触发，httpStatus非200不能触发
                .setSleepTime(10)
                .setTimeOut(10000);

//        HashSet<Integer> codeSet = new HashSet();
//        codeSet.add(200);
//        codeSet.add(500);
//        site.setAcceptStatCode(codeSet);

    }
}
