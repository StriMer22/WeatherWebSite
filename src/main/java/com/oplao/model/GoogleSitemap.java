package com.oplao.model;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GoogleSitemap {
    private String publicUrl;
    private List<Url> urls = new ArrayList<Url>();

    public String getPublicUrl() { return publicUrl; }
    public void setPublicUrl(String publicUrl) { this.publicUrl = publicUrl; }

    public List<Url> getUrls() { return new ArrayList<Url>(urls); }
    public Url addUrl(Url url) { urls.add(url); return url; }
    public void removeUrl(Url url) { urls.remove(url); }

    private String w3cDateTime(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String formated = df.format(date);
        return formated.substring(0, 22) + ":" + formated.substring(22);
    }

    public void write(File outputFile) {
        try {
            PrintWriter printWriter = new PrintWriter (outputFile);
            printWriter.println("<?xml version='1.0' encoding='UTF-8'?>");
            printWriter.println("<urlset xmlns='http://www.sitemaps.org/schemas/sitemap/0.9'>");

            for (Url url : urls) {
                printWriter.println("<url>");
                printWriter.println("<loc>" + (publicUrl == null ? "" : publicUrl) + url.loc + "</loc>");
                printWriter.println("<changefreq>" + url.changefreq.name().toLowerCase() + "</changefreq>");
                printWriter.println("<priority>" + url.priority + "</priority>");
                if (url.lastModified != null)
                    printWriter.println("<lastmod>" + w3cDateTime(url.lastModified) + "</lastmod>");
                    printWriter.println("</url>");
            }

            printWriter.println("</urlset>");
            printWriter.flush();
            printWriter.close ();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static class Url {
        private String loc;
        private float priority = 0.5f;
        private Changefreq changefreq = Changefreq.WEEKLY;
        private Date lastModified = null;

        public Url(String loc) {
            this.loc = loc;
        }

        public Url(String loc, Changefreq changefreq) {
            this.loc = loc;
            this.changefreq = changefreq;
        }

        public Url(String loc, Changefreq changefreq, float priority) {
            this.loc = loc;
            this.changefreq = changefreq;
            this.priority = priority;
        }

        public String getLoc() { return loc; }
        public void setLoc(String loc) { this.loc = loc; }

        public float getPriority() { return priority; }
        public void setPriority(float priority) { this.priority = priority; }

        public Changefreq getChangefreq() { return changefreq; }
        public void setChangefreq(Changefreq changefreq) { this.changefreq = changefreq; }

        public Date getLastModified() { return lastModified; }
        public void setLastModified(Date lastModified) { this.lastModified = lastModified; }
    }

    public static enum Changefreq {
        ALWAYS, HOURLY, DAILY, WEEKLY, MONTHLY, YEARLY, NEVER
    }

    public static void main(String[] args) throws IOException {

    }
}