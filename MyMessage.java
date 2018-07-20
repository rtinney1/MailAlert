public class MyMessage
{
	String from;
	String subject;

	public MyMessage(String from, String subject)
	{
		this.from = from;
		this.subject = subject;
	}

	@Override
	public String toString()
	{
		return String.format("From: " + from + " Subject: " + subject);
	}
}