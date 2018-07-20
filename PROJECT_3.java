import java.io.*;

public class PROJECT_3
{
	public static void main (String args[])
	{
		DataInputStream dis;
		File            inputFL;
		Profile         profile;

		try
		{
			inputFL = new File("config.bin");
			dis = new DataInputStream(new FileInputStream(inputFL));

			profile = new Profile(dis);

			new LoginBox(profile);
		}
		catch(FileNotFoundException fnfe)
		{
			//new LoginBox();
			System.out.println("File not found");
		}
	}
}//end main class





