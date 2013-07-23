package com.example.habreader_testrss.messageparser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import android.util.Log;

import com.example.habreader_testrss.dto.Message;

import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

public class MessageParser {
	
	final static int MAX_RECURSION_DEEP_LEVEL = 4;
	
	private static MessageParser instance;
	private Map<String, Object> messageParameters = new HashMap<String, Object>();
	
	private MessageParser() {		
	}
	
	public static MessageParser getInstance(Map<String, Object> params) {
		if (instance == null) 
			instance = new MessageParser();
		instance.messageParameters.clear();
		instance.messageParameters.putAll(params);
		return instance;
	}

	public Document parsePostToDocument(Message oneFeedMessage) {
		XMLReader tagsoup;
		Element newRootElement = null;
		try {
			tagsoup = XMLReaderFactory.createXMLReader("org.ccil.cowan.tagsoup.Parser");
			Builder bob = new Builder(tagsoup);

			Document feedDocument = bob.build(oneFeedMessage.getLink().toString());
			Log.d("habry", "Document is parsed " + feedDocument.getChildCount());

			Element htmlElement = feedDocument.getRootElement();

			Element bodyElement = htmlElement.getChildElements().get(1);
			Integer currentRecursionLevel = Integer.valueOf(0);
			newRootElement = searchContent(bodyElement, currentRecursionLevel);

		} catch (SAXException e) {
			Log.e("habry", "MessageParser. Exception is " + e);
		} catch (ValidityException e) {
			Log.e("habry", "MessageParser. Exception is " + e);
		} catch (ParsingException e) {
			Log.e("habry", "MessageParser. Exception is " + e);
		} catch (IOException e) {
			Log.e("habry", "MessageParser. Exception is " + e);
		}
		
		if (newRootElement != null) {
			Element html = new Element("html");
			Element body = new Element("body");
			newRootElement.detach();
			
			Integer maxWidth = (Integer) messageParameters.get("MAX_DISPLAY_WIDTH");			
			//doPostProcessingForContent(newRootElement, maxWidth.intValue());
			
			body.appendChild(newRootElement);
			html.appendChild(body);		
			Document feedDetailsDocument = new Document(html);
			return feedDetailsDocument;
		}
		
		return null;
	}
	
	private Element searchContent(Element bodyElement, Integer currentRecursionLevel){
		Element returnElement = null;
		if (currentRecursionLevel.intValue() == MAX_RECURSION_DEEP_LEVEL) {
			currentRecursionLevel = Integer.valueOf(currentRecursionLevel.intValue() - 1);
			return returnElement;
		}
		Elements elements = bodyElement.getChildElements();
		
		if (elements != null && elements.size() > 0) {
			for (int i = 0; (i < elements.size()) && returnElement == null; i ++) {
				Element element = elements.get(i);
				if ("div".equalsIgnoreCase(element.getLocalName())) {
					Attribute divClassAttribute = element.getAttribute("class");
					if (divClassAttribute != null && "post shortcuts_item".equalsIgnoreCase(divClassAttribute.getValue())) {
						returnElement = element;
						break;
					} else {
						currentRecursionLevel = Integer.valueOf(currentRecursionLevel.intValue() + 1);
						returnElement = searchContent(element, currentRecursionLevel);
					}						
				}
			}
		}
		currentRecursionLevel = Integer.valueOf(currentRecursionLevel.intValue() - 1);
		return returnElement;
	}
	
	public void doPostProcessingForContent(Element documentRootElement, int imgWidth) {
		Elements elements = documentRootElement.getChildElements();		
		if (elements != null && elements.size() > 0) {
			for (int i = 0; i < elements.size(); i ++) {
				Element element = elements.get(i);
				if ("img".equalsIgnoreCase(element.getLocalName())) {
					Attribute widthAttribute = element.getAttribute("width");					
					if (widthAttribute != null) {
						int widthAttributeValue = 0;
						try { 
							widthAttributeValue = Integer.parseInt(widthAttribute.getValue());
						} catch (NumberFormatException nfe) {}						
						if (widthAttributeValue > imgWidth || widthAttributeValue == 0) {
							widthAttribute.setValue(Integer.toString(imgWidth));
						}
					} else {
						Attribute imageWidthAttr = new Attribute("width", Integer.toString(imgWidth));
						element.addAttribute(imageWidthAttr);
					}						
				}
				doPostProcessingForContent(element, imgWidth);
			}
		}
	}
}
