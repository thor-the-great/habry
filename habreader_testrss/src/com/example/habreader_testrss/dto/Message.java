package com.example.habreader_testrss.dto;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Message implements Comparable<Message>, Serializable {
	
	private URL link;
    private String description;
    private Date date;
    private String author;
    private List<String> categories = new ArrayList<String>();
    private MessageType type;
    
    public List<String> getCategories() {
		return categories;
	}

	//
	public Message() {
		
	}
	
	public Message(String title, String description, String author) {
		this.title = title;
		this.description = description;
		this.author = author;
	}
	
    static SimpleDateFormat FORMATTER = 
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
        private String title;
        public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public URL getLink() {
			return link;
		}

		public void setLink(URL link) {
			this.link = link;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
          // getters and setters omitted for brevity
        public void setLink(String link) {
            try {
                this.link = new URL(link);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        public String getDate() {
            return FORMATTER.format(this.date);
        }

        public void setDate(String date) {
            // pad the date if necessary
            while (!date.endsWith("00")){
                date += "0";
            }
            try {
                this.date = FORMATTER.parse(date.trim());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        
    	public String getAuthor() {
    		return author;
    	}

    	public void setAuthor(String author) {
    		this.author = author;
    	}
        
        /*@Override
        public String toString() {
                 // omitted for brevity
        }

        @Override
        public int hashCode() {
                // omitted for brevity
        }
        
        @Override
        public boolean equals(Object obj) {
                // omitted for brevity
        }*/
          // sort by date
        public int compareTo(Message another) {
            if (another == null) return 1;
            // sort descending, most recent first
            return another.date.compareTo(date);
        } 

		public MessageType getType() {
			return type;
		}

		public void setType(MessageType type) {
			this.type = type;
		}
    }
