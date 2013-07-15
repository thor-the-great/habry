package com.example.habreader_testrss.tasks;

import java.io.IOException;

import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Comment;
import nu.xom.DocType;
import nu.xom.Document;
import nu.xom.Elements;
import nu.xom.Node;
import nu.xom.ParsingException;
import nu.xom.ProcessingInstruction;
import nu.xom.Text;
import nu.xom.ValidityException;

import nu.xom.Element;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.habreader_testrss.dto.Message;

public class GetFeedMainDetailsAsyncTask extends AsyncTask<Message, Integer, Document> {
	
	ViewGroup mainLayout;
	Activity activity;
	Exception error;
	
	public GetFeedMainDetailsAsyncTask(ViewGroup mainLayout, Activity activity) {
		this.mainLayout = mainLayout;
		this.activity = activity;
	}
	@Override
	protected void onPostExecute(Document result) {
		if (result != null) {
			WebView webview = new WebView(activity);
			activity.setContentView(webview);
			webview.loadData(result.toXML(), "text/html", null);
		}
	}
	
	@Override
	protected Document doInBackground(Message... feeds) {
		Message feedMessage = feeds[0];
		XMLReader tagsoup;
		Element newRootElement = null;
		try {
			tagsoup = XMLReaderFactory.createXMLReader("org.ccil.cowan.tagsoup.Parser");
			Builder bob = new Builder(tagsoup);
			Document feedDocument = bob.build(feedMessage.getLink().toString());
			Log.d("habry", "Document is parsed " + feedDocument.getChildCount());

			Element htmlElement = feedDocument.getRootElement();

			Element bodyElement = htmlElement.getChildElements().get(1);

			//Element bodyElement = htmlElement.getFirstChildElement("body");
			Elements divCollection = bodyElement.getChildElements();
			for (int i = 0; i < divCollection.size(); i++) {
				Element divElement = divCollection.get(i);
				if ("div".equalsIgnoreCase(divElement.getLocalName())) {
					Attribute divClassAttribute = divElement.getAttribute("class");
					if (divClassAttribute != null 
							//&& "content html_format".equalsIgnoreCase(divClassAttribute.getValue())
							) {
						Log.d("habry", "Found usefull content");
						newRootElement = divElement;
						for (int j = 0; j < divElement.getChildCount(); j++) {
							Node current = divElement.getChild(j);
							String data = "";
							if (current instanceof Element) {
								Element temp = (Element) current;
								data = ": " + temp.getQualifiedName();   
							}
							else if (current instanceof ProcessingInstruction) {
								ProcessingInstruction temp = (ProcessingInstruction) current;
								data = ": " + temp.getTarget();   
							}
							else if (current instanceof DocType) {
								DocType temp = (DocType) current;
								data = ": " + temp.getRootElementName();   
							}
							else if (current instanceof Text || current instanceof Comment) {
								String value = current.getValue();
								value = value.replace('\n', ' ').trim();
								if (value.length() <= 20) data = ": " + value;
								else data = ": " + current.getValue().substring(0, 17) + "...";   
							}
							// Attributes are never returned by getChild()
							Log.d("habry", current.getClass().getName() + data);				
						}
						break;
					}
				}
			}

		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ValidityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		
		if (newRootElement != null) {
			Element html = new Element("html");
			Element body = new Element("body");
			newRootElement.detach();
			body.appendChild(newRootElement);
			html.appendChild(body);		
			Document feedDetailsDocument = new Document(html);
			return feedDetailsDocument;
		}
		
		return null;
	}

}
