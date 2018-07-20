import java.io.*;

public class Profile
{
	String  userName;
	String  smtpServer;
	String  port;
	String  password;
	boolean sound;
	int     timeToCheck;
	//boolean remember;

	public Profile(DataInputStream dis)
	{
		try
		{
			smtpServer = dis.readUTF();
			userName = dis.readUTF();
			password = dis.readUTF();
			port = dis.readUTF();
			timeToCheck = dis.readInt();
			//remember = dis.readBoolean();
			sound = dis.readBoolean();
		}
		catch(Exception e)
		{
			System.out.println("Error trying to read from file");
		}
	}

	public Profile(String user, String smtp, String port, char[] pass, int time, boolean soundCheck)
	{
		sound = soundCheck;
		//remember = rememberCheck;
		timeToCheck = time;
		userName = user;
		smtpServer = smtp;
		this.port = port;
		password = new String(pass);
		for(int i = 0; i < pass.length; i++)
			pass[i] = 0;
		System.out.println("Created profile");
	}

	public void store(DataOutputStream dos)
	{
		try
		{
			dos.writeUTF(smtpServer);
			dos.writeUTF(userName);
			dos.writeUTF(password);
			dos.writeUTF(port);
			dos.writeInt(timeToCheck);
			//dos.writeBoolean(remember);
			dos.writeBoolean(sound);
		}
		catch(Exception e)
		{
			System.out.println("Error when trying to store profile");
		}
	}

	public String getSMTP()
	{
		//String[] splitString;
		//String   tempString;

		//tempString = userName;
		//splitString = tempString.split("@");

		//return splitString[1];
		return smtpServer;
	}

	public String getUser()
	{
		return userName;
	}

	public String getPort()
	{
		return port;
	}

	public String getPass()
	{
		return password;
	}

	public int getTime()
	{
		return timeToCheck;
	}

	public boolean wantSound()
	{
		return sound;
	}

	//public boolean wantRemember()
	//{
	//	return remember;
	//}
}