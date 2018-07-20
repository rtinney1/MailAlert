/*import java.awt.HeadlessException;import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.plaf.metal.MetalIconFactory;*/
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;
import javax.sound.sampled.*;
import java.net.*;
import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;

public class MailAlert implements ActionListener
{
	TrayIcon     trayIcon;
	MenuItem     settingsMI;
	MenuItem     showMI;
	MenuItem     closeMI;
	MenuItem     toggleMI;
	//MainFrame mainFrame;
	Timer        mailTimer;
	int       	 timeToCheck;
	boolean   	 playSound;
	Profile   	 profile;
	UpdateThread updateThread;
	LoginBox     settings;
	//TimerThread timerThread;

	public MailAlert(Profile prof)
	{
		//try
		//{
			System.out.println("Inside MailAlert");
			PopupMenu menu;

			profile = prof;

			settings = null;

			System.out.println("Thread created and about to run");
			updateThread = new UpdateThread(profile, this);
			//updateThread.start();
			updateThread.run();

			//timerThread = new TimerThread(this, profile.getTime() * 1000);
			//timerThread.start();

			mailTimer = new Timer(profile.getTime() * 1000, this);
			mailTimer.start();
			mailTimer.setRepeats(true);
			mailTimer.setActionCommand("TIME");

			playSound = profile.wantSound();

			try
			{
				settingsMI = new MenuItem("Settings");
				settingsMI.addActionListener(this);
				settingsMI.setActionCommand("SETTINGS");

				//showMI = new MenuItem("Show Main Frame");
				//showMI.addActionListener(this);
				//showMI.setActionCommand("SHOW");

				closeMI = new MenuItem("Close Program");
				closeMI.addActionListener(this);
				closeMI.setActionCommand("CLOSE");

				toggleMI = new MenuItem("Toggle Sound");
				toggleMI.addActionListener(this);
				toggleMI.setActionCommand("TOGGLE");

				menu = new PopupMenu();
				menu.add(toggleMI);
				menu.add(settingsMI);
				//menu.add(showMI);
				menu.add(closeMI);

				trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage("small_smiley_face.jpg"), "Email Alert", menu);
				trayIcon.setImageAutoSize(true);

				SystemTray.getSystemTray().add(trayIcon);

				System.out.println("Created TrayIcon");
			}
			catch(Exception e)
			{
				System.out.println("Whoops");
			}
		//}
		/*catch(InterruptedException ie)
		{
			System.out.println("Thread interrupted in constructor");
		}
		catch(InvocationTargetException ite)
		{
			System.out.println("Some sort of weird error that je ne sais pas in MailAlert");
		}*/
	}

	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getActionCommand().equals("TIME"))
		{
			System.out.println("About to run thread");
			updateThread.run();
			/*try
			{
				SwingUtilities.invokeAndWait(updateThread);
			}
			catch(InterruptedException ie)
			{
				System.out.println("Thread interrupted in constructor");
			}
			catch(InvocationTargetException ite)
			{
				System.out.println("Some sort of weird error that je ne sais pas in MailAlert");
			}*/
			/*try
			{
				System.out.println("Timer went off");
				mainFrame.inbox.update();
				System.out.println("Done with actionPerformed for timer in mailalert");
			}
			catch(CreationException ce)
			{
				System.out.println("Something screwed up when checking inbox after timer went off");
			}*/
		}
		else if(ae.getActionCommand().equals("TOGGLE"))
		{
			toggleSound();
		}
		else if(ae.getActionCommand().equals("SETTINGS"))
		{
			if(settings == null)
			{
				System.out.println("Inside Settings ActionPerformed");
				//settingsMI.setEnabled(false);
				settings = new LoginBox(this, profile, playSound);
				//new SettingsBox(this, playSound);
				System.out.println("Finished creating settings box");
			}
		}
		//else if(ae.getActionCommand().equals("SHOW"))
		//{
		//	mainFrame.setVisible(true);
		//}
		else if(ae.getActionCommand().equals("CLOSE"))
		{
			System.exit(1);
		}
	}

	public void newProfile(Profile prof)
	{
		profile = prof;
		playSound = profile.wantSound();
		updateThread = null;
		updateThread = new UpdateThread(profile, this);
		//updateThread.changeProfile(profile);
		updateThread.run();
	}

	public void disposeSettingsBox()
	{
		settings = null;
	}

	public void stopTimer()
	{
		mailTimer.stop();
		//timerThread.stopTimer();
	}

	public void setTimer(int time)
	{
		timeToCheck = time * 1000;

		//timerThread.setTimer(timeToCheck);

		mailTimer.setDelay(time);
		mailTimer.start();
		mailTimer.setRepeats(true);
	}

	public void setSound(boolean play)
	{
		playSound = play;
	}

	public void toggleSound()
	{
		if(playSound)
			playSound = false;
		else
			playSound = true;
	}

	public void makeSound()
	{
		Clip             audioClip;
		AudioInputStream audioInputStream;
		URL              url;

		try
		{
			url = this.getClass().getClassLoader().getResource("beep-04.wav");
			audioInputStream = AudioSystem.getAudioInputStream(url);
			audioClip = AudioSystem.getClip();
			audioClip.open(audioInputStream);
			audioClip.start();
		}
		catch(UnsupportedAudioFileException uafe)
		{
			System.out.println("Unsupported File");
		}
		catch(IOException ioe)
		{
			System.out.println("IO Exception");
		}
		catch(LineUnavailableException lue)
		{
			System.out.println("Line Unavailable");
		}
	}

	/*public void timerWentOff()
	{
		try
		{
			System.out.println("Timer went off");
			mainFrame.inbox.update();
		}
		catch(CreationException ce)
		{
			System.out.println("Something screwed up when checking inbox after timer went off");
		}
	}*/
}