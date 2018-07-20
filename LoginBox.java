import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import com.sun.mail.imap.*;
import javax.swing.*;
import java.util.Properties;

public class LoginBox extends JDialog implements ActionListener
{
	JButton           loginBtn;
	JButton           cancelBtn;
	JButton			  saveBtn;
	JButton			  closeBtn;
	JTextField        smtpServerTF;
	JTextField        portTF;
	JTextField        userTF;
	JPasswordField    passTF;
	JTextField        timeTF;
	Checkbox          soundChBox;
	//Checkbox          rememberBox;
	IntInputVerifier  timeVerifier;
	Profile           profile;
	Container         cp;
	MailAlert         mailAlert;

	public LoginBox(Profile prof)
	{
		JPanel btnPanel;

		loginBtn = new JButton("Login");
		loginBtn.addActionListener(this);
		loginBtn.setActionCommand("LOGIN");

		cancelBtn = new JButton("Cancel");
		cancelBtn.addActionListener(this);
		cancelBtn.setActionCommand("CANCEL");

		btnPanel = new JPanel();

		btnPanel.add(loginBtn);
		btnPanel.add(cancelBtn);

		cp = getContentPane();

		cp.add(btnPanel, BorderLayout.SOUTH);

		setup();
		profile = prof;

		smtpServerTF.setText(profile.getSMTP());
		portTF.setText(profile.getPort());
		userTF.setText(profile.getUser());
		passTF.setText(profile.getPass());
		timeTF.setText("" + profile.getTime());
		soundChBox.setState(profile.wantSound());
		//rememberBox.setState(profile.wantRemember());

		setTitle("Login");

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	public LoginBox(MailAlert ma, Profile prof, boolean sound)
	{
		JPanel btnPanel;

		saveBtn = new JButton("Save");
		saveBtn.addActionListener(this);
		saveBtn.setActionCommand("SAVE");

		closeBtn = new JButton("Cancel");
		closeBtn.addActionListener(this);
		closeBtn.setActionCommand("CLOSE");

		btnPanel = new JPanel();

		btnPanel.add(saveBtn);
		btnPanel.add(closeBtn);

		cp = getContentPane();

		cp.add(btnPanel, BorderLayout.SOUTH);

		setup();
		profile = prof;
		mailAlert = ma;

		smtpServerTF.setText(profile.getSMTP());
		portTF.setText(profile.getPort());
		userTF.setText(profile.getUser());
		passTF.setText(profile.getPass());
		timeTF.setText("" + profile.getTime());
		soundChBox.setState(sound);

		setTitle("Settings");

		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	}

	public void setup()
	{
		JLabel      smtpServerLBL;
		JLabel      portLBL;
		JLabel      userLBL;
		JLabel      passLBL;
		JLabel      timeLBL;
		JLabel      blankLBL;
		GroupLayout groupLO;
		JPanel      mainPanel;
		JPanel      btnPanel;

		smtpServerTF = new JTextField ();

		smtpServerLBL = new JLabel("Server Domain");

		portTF = new JTextField();

		portLBL = new JLabel("Port");

		userTF = new JTextField();

		userLBL = new JLabel("Username");

		passTF = new JPasswordField();

		passLBL = new JLabel("Password");

		blankLBL = new JLabel();

		timeVerifier = new IntInputVerifier(20, 3600);

		timeLBL = new JLabel("Time Between Mail Checks: ");

		timeTF = new JTextField();
		timeTF.setInputVerifier(timeVerifier);

		soundChBox = new Checkbox("Play sound?");

		//rememberBox = new Checkbox("Remember me", false);

		mainPanel = new JPanel();

		groupLO = new GroupLayout(mainPanel);
		mainPanel.setLayout(groupLO);

		GroupLayout.SequentialGroup hGroup = groupLO.createSequentialGroup();

		hGroup.addGroup(groupLO.createParallelGroup().
			addComponent(smtpServerLBL).addComponent(portLBL).
			addComponent(userLBL).addComponent(passLBL).
			addComponent(timeLBL).addComponent(blankLBL));
		hGroup.addGroup(groupLO.createParallelGroup().
			addComponent(smtpServerTF).addComponent(portTF).
			addComponent(userTF).addComponent(passTF).
			addComponent(timeTF).addComponent(soundChBox));

		groupLO.setHorizontalGroup(hGroup);

		GroupLayout.SequentialGroup vGroup = groupLO.createSequentialGroup();

		vGroup.addGroup(groupLO.createParallelGroup(GroupLayout.Alignment.BASELINE).
			addComponent(smtpServerLBL).addComponent(smtpServerTF));
		vGroup.addGroup(groupLO.createParallelGroup(GroupLayout.Alignment.BASELINE).
			addComponent(portLBL).addComponent(portTF));
		vGroup.addGroup(groupLO.createParallelGroup(GroupLayout.Alignment.BASELINE).
			addComponent(userLBL).addComponent(userTF));
		vGroup.addGroup(groupLO.createParallelGroup(GroupLayout.Alignment.BASELINE).
			addComponent(passLBL).addComponent(passTF));
		vGroup.addGroup(groupLO.createParallelGroup(GroupLayout.Alignment.BASELINE).
			addComponent(timeLBL).addComponent(timeTF));
		vGroup.addGroup(groupLO.createParallelGroup(GroupLayout.Alignment.BASELINE).
			addComponent(blankLBL).addComponent(soundChBox));

		groupLO.setVerticalGroup(vGroup);

		mainPanel.add(smtpServerTF);
		mainPanel.add(smtpServerLBL);
		mainPanel.add(portLBL);
		mainPanel.add(portTF);
		mainPanel.add(userLBL);
		mainPanel.add(userTF);
		mainPanel.add(passTF);
		mainPanel.add(passLBL);
		mainPanel.add(timeLBL);
		mainPanel.add(timeTF);
		//mainPanel.add(rememberBox);
		mainPanel.add(soundChBox);

		cp.add(mainPanel, BorderLayout.CENTER);

		smtpServerTF.requestFocus();
		setUpDialogBox();
	}

	public boolean checkConnection()
	{
		Properties props;
		Session    session;
		Store      store;
		String     password;

		try
		{
			props = new Properties();
			session = Session.getDefaultInstance(props, null);
			store = session.getStore("imaps");

			password = new String(passTF.getPassword());

			if(smtpServerTF.getText().trim().startsWith("imap."))
				store.connect(smtpServerTF.getText().trim(), userTF.getText().trim(), password);
			else
				store.connect("imap." + smtpServerTF.getText().trim(), userTF.getText().trim(), password);

			return true;
		}
		catch(NoSuchProviderException nspe)
		{
			JOptionPane.showMessageDialog(null, "Incorrect username/password combination", "Error", JOptionPane.ERROR_MESSAGE);
			System.out.println("Error in update: No such provider");
			return false;
			//throw new CreationException(nspe);
		}
		catch(MessagingException me)
		{
			JOptionPane.showMessageDialog(null, "Incorrect username/password combination", "Error", JOptionPane.ERROR_MESSAGE);
			System.out.println("Error in update: Messaging exception");
			return false;
			//throw new CreationException(me);
		}
	}

	public void actionPerformed(ActionEvent ae)
	{
		//boolean             remember;
		boolean             sound;
		String              timeString;
		int                 time;
		Profile             profile;
		//MainFrame           mainFrame;
		File                outputFL;
		DataOutputStream    dos;
		System.out.println("Inside actionPerformed");

		if(ae.getActionCommand().equals("LOGIN"))
		{
			try
			{
				System.out.println("Pressed login");
				//remember = rememberBox.getState();
				sound = soundChBox.getState();
				timeString = timeTF.getText().trim();
				time = Integer.parseInt(timeString);

				profile = new Profile(userTF.getText().trim(), smtpServerTF.getText().trim(),
									  portTF.getText().trim(), passTF.getPassword(), time, sound);
				if(checkConnection())
				{
					try
					{
						outputFL = new File("config.bin");

						dos = new DataOutputStream(new FileOutputStream(outputFL));
						profile.store(dos);
					}
					catch(IOException ioe)
					{
						System.out.println("Error saving profile");
					}
					mailAlert = new MailAlert(profile);
					//mainFrame = new MainFrame(profile);
					this.dispose();
					//this.setVisible(false);
				}
			}
			catch(NumberFormatException nfe)
			{
				JOptionPane.showMessageDialog(null, "Need value for time", "Error", JOptionPane.ERROR_MESSAGE);
			}
			//catch(CreationException ce)
			//{
			//}
		}
		else if(ae.getActionCommand().equals("CANCEL"))
		{
			System.exit(1);
		}
		else if(ae.getActionCommand().equals("SAVE"))
		{
			try
			{
				System.out.println("Pressed login");
				//remember = rememberBox.getState();
				sound = soundChBox.getState();
				timeString = timeTF.getText().trim();
				time = Integer.parseInt(timeString);

				profile = new Profile(userTF.getText().trim(), smtpServerTF.getText().trim(),
									  portTF.getText().trim(), passTF.getPassword(), time, sound);

				if(checkConnection())
				{
					mailAlert.newProfile(profile);
					mailAlert.disposeSettingsBox();
					try
					{
						outputFL = new File("config.bin");

						dos = new DataOutputStream(new FileOutputStream(outputFL));
						profile.store(dos);
					}
					catch(IOException ioe)
					{
						System.out.println("Error saving profile");
					}
					//mainFrame = new MainFrame(profile);
					this.dispose();
				}
			}
			catch(NumberFormatException nfe)
			{
				JOptionPane.showMessageDialog(null, "Need value for time", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		else if(ae.getActionCommand().equals("CLOSE"))
		{
			mailAlert.disposeSettingsBox();
			this.dispose();
		}
	}

	private void setUpDialogBox()
	{
		Toolkit tk;
	    Dimension d;

	    tk = Toolkit.getDefaultToolkit();
	    d = tk.getScreenSize();
	    setSize(d.width/4, d.height/4);
	    setLocation(d.width/4, d.width/5);

	    //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    //setTitle("Login");

	    //this.pack();

	    setVisible(true);
    }
}