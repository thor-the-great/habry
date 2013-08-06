package org.thor.habry.messageparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.thor.habry.dto.Comment;
import org.thor.habry.dto.Message;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import android.util.Log;


import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

public class MessageParser {
	
	final static int MAX_RECURSION_DEEP_LEVEL = 5;
	
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
			newRootElement = searchContent(bodyElement, currentRecursionLevel, "div", "class", "content html_format");

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
			
			//Integer maxWidth = (Integer) messageParameters.get("MAX_DISPLAY_WIDTH");			
			//doPostProcessingForContent(newRootElement, maxWidth.intValue());
			
			body.appendChild(newRootElement);
			html.appendChild(body);		
			Document feedDetailsDocument = new Document(html);
			return feedDetailsDocument;
		}
		
		return null;
	}
	
	public Document parseCommentsToDocument(Message oneFeedMessage) {
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
			newRootElement = searchContent(bodyElement, currentRecursionLevel, "div", "class", "comments_list");

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
			
			//Integer maxWidth = (Integer) messageParameters.get("MAX_DISPLAY_WIDTH");			
			//doPostProcessingForContent(newRootElement, maxWidth.intValue());
			
			body.appendChild(newRootElement);
			html.appendChild(body);		
			Document feedDetailsDocument = new Document(html);
			return feedDetailsDocument;
		}
		
		return null;
	}
	
	public List<Comment> parseToComments(Message oneFeedMessage) {
		List<Comment> listOfComments = new ArrayList<Comment>();
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
			newRootElement = searchContent(bodyElement, currentRecursionLevel, "div", "class", "comments_list");
			if (newRootElement != null) {
				List<Element> elementList = new ArrayList<Element>();
				currentRecursionLevel = Integer.valueOf(0);
				searchContentList(newRootElement, currentRecursionLevel, "div", "class", "comment_item", elementList, MAX_RECURSION_DEEP_LEVEL);
				if (elementList.size() > 0) {
					for (Element commentElement : elementList) {
						handleCommentElement(listOfComments, commentElement, null);							
					}
				}
			}

		} catch (SAXException e) {
			Log.e("habry", "MessageParser. Exception is " + e);
		} catch (ValidityException e) {
			Log.e("habry", "MessageParser. Exception is " + e);
		} catch (ParsingException e) {
			Log.e("habry", "MessageParser. Exception is " + e);
		} catch (IOException e) {
			Log.e("habry", "MessageParser. Exception is " + e);
		}		
		
		return listOfComments;
	}

	private void handleCommentElement(List<Comment> listOfComments, Element commentElement, Comment currentParent) {
		Integer currentRecursionLevel;
		Comment newComment = new Comment();
		currentRecursionLevel = Integer.valueOf(0);
		Element authorElement = searchContent(commentElement, currentRecursionLevel, "a", "class", "username", false, false);
		if (authorElement != null) {							
			newComment.setAuthor(authorElement.getValue());
		}
		Element messageText = searchContent(commentElement, currentRecursionLevel, "div", "class", "message html_format", false, false);
		if (messageText != null) {		
			StringBuilder sb = new StringBuilder();
			sb.append(messageText.getValue());
			if (sb.length() > 0 ) {
				sb.deleteCharAt(0);
			}
			newComment.setText(sb.toString());
		}
		Element messageId = searchContent(commentElement, currentRecursionLevel, "div", "class", "info", false, false);
		if (messageId != null) {
			Attribute relAttr = messageId.getAttribute("rel");
			newComment.setId(relAttr.getValue());
			Log.d("hanry", "comment id = " + newComment.getId());
		}		
		Element parentId = searchContent(commentElement, currentRecursionLevel, "span", "class", "parent_id", false, false);
		if (parentId != null) {
			Attribute relAttr = parentId.getAttribute("data-parent_id");
			if (relAttr != null) {
				//add to root collection if there is no parents
				if ("0".equalsIgnoreCase(relAttr.getValue())) {
					listOfComments.add(newComment);
				}
				//if no then parent has to handle child. Parent must be set to the method
				else {
					if (currentParent != null) {
						currentParent.getChildComments().add(newComment);
					}
				}
				
				Element replyCommentElement = searchContent(commentElement, currentRecursionLevel, "div", "class", "reply_comments", 2);
				if(replyCommentElement != null && replyCommentElement.getChildCount() > 0) {
					/*Elements childOfReply = replyCommentElement.getChildElements();
					for (int j =0; j < childOfReply.size(); j++) {
						Element commentItemFromReply = childOfReply.get(j);
						if (commentElement)
					}*/
					Integer currentRecursionLevel1 = Integer.valueOf(0);
					List<Element> elementList = new ArrayList<Element>();
					searchContentList(replyCommentElement, currentRecursionLevel1, "div", "class", "comment_item", elementList, 2);
					if (elementList.size() > 0) {
						for (Element element : elementList) {
							handleCommentElement(listOfComments, element, newComment);
						}
					}
				}								
			}							
		}
	}
	
	public Document parseQAToDocument(Message oneFeedMessage) {
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
			newRootElement = searchContent(bodyElement, currentRecursionLevel, "div", "class", "answers");

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
			
			//Integer maxWidth = (Integer) messageParameters.get("MAX_DISPLAY_WIDTH");			
			//doPostProcessingForContent(newRootElement, maxWidth.intValue());
			
			body.appendChild(newRootElement);
			html.appendChild(body);		
			Document feedDetailsDocument = new Document(html);
			return feedDetailsDocument;
		}
		
		return null;
	}
	
	private Element searchContent(Element bodyElement, Integer currentRecursionLevel, String tag, String attrName, String attrValue){		
		return searchContent(bodyElement, currentRecursionLevel, tag, attrName, attrValue, true, false);
	}
	
	private Element searchContent(Element bodyElement, Integer currentRecursionLevel, String tag, String attrName, String attrValue, int maxRecursionDeepLevel){		
		return searchContent(bodyElement, currentRecursionLevel, tag, attrName, attrValue, true, false, maxRecursionDeepLevel);
	}
	
	private Element searchContent(Element bodyElement, Integer currentRecursionLevel, String tag, String attrName, String attrValue, boolean simple, boolean contains){
		return searchContent(bodyElement, currentRecursionLevel, tag, attrName, attrValue, simple, contains, 0);
	}
	
	private Element searchContent(Element bodyElement, Integer currentRecursionLevel, String tag, String attrName, String attrValue, boolean simple, boolean contains, int maxRecursionDeepLevel){
		Element returnElement = null;
		if (maxRecursionDeepLevel == 0)
			maxRecursionDeepLevel = MAX_RECURSION_DEEP_LEVEL;
		if (currentRecursionLevel.intValue() == maxRecursionDeepLevel) {
			currentRecursionLevel = Integer.valueOf(currentRecursionLevel.intValue() - 1);
			return returnElement;
		}
		Elements elements = bodyElement.getChildElements();
		
		if (elements != null && elements.size() > 0) {
			for (int i = 0; (i < elements.size()) && returnElement == null; i ++) {
				Element element = elements.get(i);
				if (tag.equalsIgnoreCase(element.getLocalName())) {
					Attribute divClassAttribute = element.getAttribute(attrName);
					if (divClassAttribute != null &&
							((divClassAttribute.getValue().trim().equalsIgnoreCase(attrValue.trim()) && !contains ) ||
							(divClassAttribute.getValue().trim().contains(attrValue.trim()) && contains))) {
						returnElement = element;
						break;
					} else {
						currentRecursionLevel = Integer.valueOf(currentRecursionLevel.intValue() + 1);
						returnElement = searchContent(element, currentRecursionLevel, tag, attrName, attrValue, simple, contains, maxRecursionDeepLevel);
					}						
				} else if (!simple) {
					currentRecursionLevel = Integer.valueOf(currentRecursionLevel.intValue() + 1);
					returnElement = searchContent(element, currentRecursionLevel, tag, attrName, attrValue, simple, contains, maxRecursionDeepLevel);
				}
			}
		}
		currentRecursionLevel = Integer.valueOf(currentRecursionLevel.intValue() - 1);
		return returnElement;
	}
	
	private void searchContentList(Element bodyElement, Integer currentRecursionLevel, String tag, String attrName, String attrValue, List<Element> elementList, int maxRecursionDeepLevel){		
		if (maxRecursionDeepLevel == 0)
			maxRecursionDeepLevel = MAX_RECURSION_DEEP_LEVEL;
		if (currentRecursionLevel.intValue() == maxRecursionDeepLevel) {
			currentRecursionLevel = Integer.valueOf(currentRecursionLevel.intValue() - 1);
			//return returnElement;
			return;
		}
		Elements elements = bodyElement.getChildElements();
		
		if (elements != null && elements.size() > 0) {
			for (int i = 0; (i < elements.size()); i ++) {
				Element element = elements.get(i);
				if (tag.equalsIgnoreCase(element.getLocalName())) {
					Attribute divClassAttribute = element.getAttribute(attrName);
					if (divClassAttribute != null && attrValue.trim().equalsIgnoreCase(divClassAttribute.getValue().trim())) {
						elementList.add(element);
						//break;
					} else {
						currentRecursionLevel = Integer.valueOf(currentRecursionLevel.intValue() + 1);
						searchContentList(element, currentRecursionLevel, tag, attrName, attrValue, elementList , maxRecursionDeepLevel);
					}						
				} else {
					currentRecursionLevel = Integer.valueOf(currentRecursionLevel.intValue() + 1);
					searchContentList(element, currentRecursionLevel, tag, attrName, attrValue, elementList, maxRecursionDeepLevel);
				}
			}
		}
		currentRecursionLevel = Integer.valueOf(currentRecursionLevel.intValue() - 1);
		return;
	}
	
	private void doPostProcessingForContent(Element documentRootElement, int imgWidth) {
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
