package controllers;

import helpers.SignedRequestsHelper;
import models.Title;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import play.Logger;
import play.libs.XPath;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AmazonSearchController{
    private static final String USER_AGENT = "Mozilla/5.0";

    public Document searchAmazon(Map<String, String> params){
        Document results = null;
        BufferedReader br = null;

        String searchUrl = AmazonSearchController.getAmazonSearchUrl(params);
        try{
            URL obj = new URL(searchUrl);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            if (200 <= con.getResponseCode() && con.getResponseCode() <= 299) {
                results = db.parse(con.getInputStream());
            } else {
                results = db.parse(con.getErrorStream());
            }
            con.disconnect();
        }catch (Exception ex){
            Logger.error("Error searching amazon", ex);
        }
        return results;
    }


    public Boolean isEmptyResults(Document doc){
        Node topResult = XPath.selectNode("/ItemSearchResponse/Items/Item[1]", doc);
        return topResult==null;
    }

    public boolean isTitleAvailableOnAmazon(Title titleRecord){
        Logger.debug("Checking: " + titleRecord.originalTitle + ": " + titleRecord.castLead);
        Boolean answer = false; // pessimistic default
        Map<String, String> params = new HashMap<String, String>();
        params.put("Title", titleRecord.originalTitle);

        if(titleRecord.castLead != null){
            params.put("Keywords", titleRecord.castLead);
        }

        Document results = searchAmazon(params);
        if(isEmptyResults(results)){
            params.remove("Keywords");
            results = searchAmazon(params); // if it was empty try removing the keywords.
        }

        String availability = XPath.selectText("/ItemSearchResponse/Items/Item[1]/Offers/Offer/OfferListing/Availability", results); // Not yet released | Usually ships in 1-2 business days |
        String availabilityType = XPath.selectText("/ItemSearchResponse/Items/Item[1]/Offers/Offer/OfferListing/AvailabilityAttributes/AvailabilityType", results);
        String isPreorder = XPath.selectText("/ItemSearchResponse/Items/Item[1]/Offers/Offer/OfferListing/AvailabilityAttributes/IsPreorder", results);

        if(isEmptyResults(results)){ // the search result didn't have a first item so it didn't find our movie.
            answer = false;
            return answer;
        }

        Logger.debug("topResult wan't null");

        if(availability == null || availability.equals("") || availability.equals("Not yet released")){
            Logger.debug("Marking unavailable because availability response was: " + availability);
            answer = false;
            return answer;
        }

        Logger.debug("looks like we availability data");

        if(availabilityType.equals("now")){
            if(isPreorder.equals("1")){
                answer = false;
                Logger.debug("bummer, preorder");
            }else{
                answer = true;
                Logger.debug("looks like we have it. ");
            }
        }else{
            Logger.debug("weird, the type was|" + availabilityType + "|" );
        }
        return answer;
    }

    public static String getAmazonSearchUrl(Map<String, String> params){
        String requestUrl = null;
        SignedRequestsHelper helper;
        helper = new SignedRequestsHelper();
        params.put("Service", "AWSECommerceService");
        params.put("Operation", "ItemSearch");
        params.put("AssociateTag", "wfawcett");
        params.put("SearchIndex", "UnboxVideo");
        params.put("ResponseGroup", "OfferListings");
        params.put("Availability", "Available");
        params.put("BrowseNode", "2858905011");
        requestUrl = helper.sign(params);
        Logger.debug("requestURL: " + requestUrl);
        return requestUrl;
    }
}