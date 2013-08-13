package org.thor.habry.dao;

import java.util.List;

import org.thor.habry.dto.Message;

public interface HabryDAOInterface {

	public void saveOneMessage(Message message);

	public List<Message> findAllMessages();

	public String readMessageContentByRef(String messageRef);

	public void deleteMessage(String messageRef);
	
	public List<String> findSavedMessageRefByType(String type);

}
