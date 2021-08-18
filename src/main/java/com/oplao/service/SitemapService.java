package com.oplao.service;

import com.oplao.Application;
import com.oplao.model.GoogleSitemap;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@Service
public final class SitemapService {
    private static final String BASE_URL = "https://localhost:8080";
    private static final String FILE_LOC = System.getProperty("user.dir") + "\\src\\main\\webapp\\sitemap.xml";


    public String addToSitemap(String requestUrl) throws Exception {
        GoogleSitemap sitemap = new GoogleSitemap();
        sitemap.setPublicUrl(BASE_URL);
        GoogleSitemap.Url url = new GoogleSitemap.Url(requestUrl);
        url.setLastModified(new Date());

        File inputFile = new File(FILE_LOC);
        Document xmlDoc = parseXmlDoc(inputFile, sitemap);

        NodeList nodes = xmlDoc.getElementsByTagName("loc");
        if (nodes.getLength() > 0) {
            boolean exists = false;
            for (int i = 0; i < nodes.getLength(); i++) {
                if (nodes.item(i).getTextContent().equals(BASE_URL + requestUrl)) {
                    exists = true;
                    refreshLink(xmlDoc, i);
                    break;
                }
            }

            if (!exists) {
                addLink(xmlDoc, requestUrl, url);
            }else{

            }
        } else {
            sitemap.addUrl(url);
            sitemap.write(inputFile);
            Application.log.info("url written to sitemap");
        }
        return null;
    }

private void refreshLink(Document xmlDoc, int index){

    Node lastmodNode = xmlDoc.createElement("lastmod");
    lastmodNode.appendChild(xmlDoc.createTextNode("" + new Date()));
    Node node = xmlDoc.getElementsByTagName("url").item(index);
    int deleting = 7;
    for (int i = 0; i <node.getChildNodes().getLength() ; i++) {
        if(node.getChildNodes().item(i).getTextContent().contains(":")){
            deleting = i;
        }
    }

    node.removeChild(node.getChildNodes().item(deleting));
    node.appendChild(lastmodNode);

    refreshDoc(xmlDoc);
    Application.log.info("url refreshed in sitemap");
}
    private void addLink(Document xmlDoc, String requestUrl, GoogleSitemap.Url url) {

            Node urlNode = xmlDoc.createElement("url");
            Node locNode = xmlDoc.createElement("loc");
            locNode.appendChild(xmlDoc.createTextNode(BASE_URL + requestUrl));
            Node changefreqNode = xmlDoc.createElement("changefreq");
            changefreqNode.appendChild(xmlDoc.createTextNode(url.getChangefreq().name().toLowerCase()));
            Node priorityNode = xmlDoc.createElement("priority");
            priorityNode.appendChild(xmlDoc.createTextNode("" + url.getPriority()));
            Node lastmodNode = xmlDoc.createElement("lastmod");
            lastmodNode.appendChild(xmlDoc.createTextNode("" + url.getLastModified()));


            urlNode.appendChild(locNode);
            urlNode.appendChild(changefreqNode);
            urlNode.appendChild(priorityNode);
            urlNode.appendChild(lastmodNode);
            xmlDoc.getDocumentElement().appendChild(urlNode);
            refreshDoc(xmlDoc);
    }

    private void refreshDoc(Document xmlDoc){
        try {
            System.setProperty("javax.xml.transform.TransformerFactory",
                    "com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(xmlDoc);

            StreamResult result = new StreamResult(FILE_LOC);
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Document parseXmlDoc(File inputFile, GoogleSitemap sitemap) throws IOException, SAXException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = null;
        try {
            doc = dBuilder.parse(inputFile);
        } catch (SAXParseException spe) {
            sitemap.write(inputFile);
            doc = dBuilder.parse(inputFile);
        }
        doc.getDocumentElement().normalize();

        return doc;
    }
}
