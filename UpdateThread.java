import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import java.io.*;
import com.sun.mail.imap.*;
import javax.swing.*;

public class UpdateThread implements Runnable//extends Thread
{
	Profile   profile;
	Folder    inboxFolder;
	MailAlert mailAlert;
	//MainFrame mainFrame;
	//Inbox     inbox;

	public UpdateThread(Profile prof, MailAlert ma)
	{
		profile = prof;
		mailAlert = ma;
		//this.inbox = inbox;
		System.out.println("Created thread");
	}

	public void run()
	{
		System.out.println("Inside run in thread");
		update();
		System.out.println("Finished update");
	}

	public void changeProfile(Profile prof)
	{
		profile = prof;
	}

	public void update()
	{
		Properties props;
		Session    session;
		Store      store;
		Message[]  messageList;
		String     host;
		String     username;
		String     password;
		int        newMessages;

		try
		{
			//inbox.clear();
			if(!profile.getSMTP().startsWith("imap."))
				host = "imap." + profile.getSMTP();
			else
				host = profile.getSMTP();
			username = profile.getUser();
			password = profile.getPass();

			System.out.println("host: " + host + " username " + username + " password " + password);

			System.out.println("Before making properties");
			props = new Properties();
			session = Session.getDefaultInstance(props, null);
			store = session.getStore("imaps");
			store.connect(host, username, password);

			System.out.println("Getting Inbox");
			inboxFolder = store.getFolder("INBOX");
			inboxFolder.open(Folder.READ_WRITE);

			System.out.println("Right before checkNewMessages");
			newMessages = this.checkNewMessages();

			System.out.println("new messages " + newMessages);

			if(newMessages > 0)
			{
				if(mailAlert.playSound)
					mailAlert.makeSound();

				JOptionPane.showMessageDialog(null, "You have " + newMessages + " new messages!", "Hello!", JOptionPane.PLAIN_MESSAGE);
			}

			messageList = inboxFolder.getMessages();
			System.out.println("Number of messages: " + inboxFolder.getMessageCount());

			//for(Message message: messageList)
			//	processMessages(message);

			inboxFolder.close(false);
			store.close();
			System.out.println("Finished update in thread");
		}
		catch(NoSuchProviderException nspe)
		{
			System.out.println("Error in update: No such provider");
			//throw new CreationException(nspe);
		}
		catch(MessagingException me)
		{
			System.out.println("Error in update: Messaging exception");
			//throw new CreationException(me);
		}
	}

	/*public void processMessages(Message message)
	{
		String from;
		String subject;

		try
		{
			from = message.getFrom()[0].toString();
			subject = message.getSubject();

			inbox.addElement(new MyMessage(from, subject));
		}
		catch(MessagingException me)
		{
			System.out.println("Error in Inbox: MessagingException");
		}
	}*/

	public int checkNewMessages()
	{
		try
		{
			System.out.println("Number of messages inside checkNewMessages: " + inboxFolder.getMessageCount());

			if(inboxFolder.hasNewMessages())
			{
				System.out.println("We have new messages");
				return inboxFolder.getNewMessageCount();
			}
			else
				return 0;
		}
		catch(MessagingException me)
		{
			System.out.println("Error in checkNewMessages: MessagingException");
			return 0;
		}
	}
}