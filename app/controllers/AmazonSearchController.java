package controllers;

import helpers.SignedRequestsHelper;
import models.Amazon;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import play.Logger;
import play.libs.ws.WSClient;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class AmazonSearchController{
    private final WSClient ws;

    @Inject
    public AmazonSearchController(WSClient ws) {
        this.ws = ws;
    }

//    public void searchAmazon(String title, String keywords){
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("Title", title);
//        params.put("Keywords", keywords);
//        String searchUrl = AmazonSearchController.getAmazonSearchUrl(params);
//        ws.url(searchUrl).get()
//            .thenApply(response -> {
//            Document searchResult = response.asXml();
//            NodeList returnedItems = searchResult.getElementsByTagName("Items");
//
//        });
//    }

    public static String getAmazonSearchUrl(Map<String, String> params){
        String requestUrl = null;
        String ACCESS_KEY_ID = "AKIAIX4GZ4HGMFXJHD3A";
        String SECRET_KEY = "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";
        String ENDPOINT = "webservices.amazon.com";

        SignedRequestsHelper helper;
        try {
            helper = SignedRequestsHelper.getInstance(ENDPOINT, ACCESS_KEY_ID, SECRET_KEY);
        } catch (Exception e) {
            Logger.error(e.getStackTrace().toString());
            return requestUrl;
        }

        params.put("Service", "AWSECommerceService");
        params.put("Operation", "ItemSearch");
        params.put("AWSAccessKeyId", ACCESS_KEY_ID);
        params.put("AssociateTag", "ASSOCIATE TAG");
        params.put("SearchIndex", "UnboxVideo");
        params.put("ResponseGroup", "BrowseNodes,Images,ItemAttributes,SearchBins");
        params.put("Availability", "Available");
        params.put("BrowseNode", "2858778011");

        requestUrl = helper.sign(params);


        return requestUrl;
    }
}