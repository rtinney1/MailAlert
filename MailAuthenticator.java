import javax.mail.*;

public class MailAuthenticator extends javax.mail.Authenticator
{
	String senderUsername;
	String senderPassword;

	public MailAuthenticator(String username, String password)
	{
		senderUsername = username;
		senderPassword = new String(password);
		//senderPassword = password;
	}

	protected PasswordAuthentication getPasswordAuthentication()
	{
		return new PasswordAuthentication(senderUsername, senderPassword);
	}
}