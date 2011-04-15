package controllers;

import play.*;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.libs.WS.WSRequest;
import play.libs.XPath;
import play.mvc.*;

import java.util.*;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import models.*;

public class Application extends Controller {

    public static void index(int currentPage) {
    	currentPage = (currentPage < 1) ? 1 : currentPage;
    	
    	WSRequest flickrRequest = WS.url("http://api.flickr.com/services/rest/?method=flickr.interestingness.getList");
    	flickrRequest.setParameter("api_key", "c7ae09f0a5a1bc77cd78173d7e29460b");
    	flickrRequest.setParameter("per_page", 20);
    	flickrRequest.setParameter("page", currentPage);
    	
    	HttpResponse response = flickrRequest.get();
    	
    	Document doc = response.getXml();
    	int max  = Integer.parseInt(XPath.selectNode("//@pages", doc).getTextContent());

    	// Construction de la liste des pages
    	int[] pages = createPagesList(currentPage, max, 13);

    	List<Photo> photos = new ArrayList<Photo>();
    	for (Node node : XPath.selectNodes("//photo", doc)) {
    		Photo p = new Photo(
    				Long.valueOf(XPath.selectText("@id", node)), 
    				XPath.selectText("@title", node),
    				XPath.selectText("@server", node),
    				XPath.selectText("@secret", node));
    		photos.add(p);
    	}
    	
    	render(currentPage, max, pages, photos);
    }

	private static int[] createPagesList(int page, int max, int nbPages) {
		int[] pages;
		nbPages = nbPages + (1-nbPages%2);
		int middle = nbPages/2 + 1;
		
		int offset = 0; 
    	if((page-middle) < 0 && (page+middle) < max) {
    		offset = middle - page;
    	} else if ((page+middle) > max) {
    		offset = max - page - middle + 1;
    	}
    	
    	if (nbPages < max) {
	    	pages = new int[nbPages];
	    	for(int i = (1-middle) ; i < middle ; i++) {
	    		pages[i+(middle-1)] = page+i+offset;
	    	}
    	} else {
    		pages = new int[max];
    		for(int i = 0 ; i < max; i++) {
    			pages[i] = i+1;
    		}
    	}
    	
		return pages;
	}
}