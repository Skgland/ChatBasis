package bb.chat.network.packet.Handshake;

import bb.chat.interfaces.IPacket;
import bb.chat.network.packet.DataIn;
import bb.chat.network.packet.DataOut;

import java.io.IOException;

/**
 * Created by BB20101997 on 23.11.2014.
 */
public class LoginPacket extends IPacket {

	private String userName;
	private String userPasswd;

	public void setUsername(String name) {
		userName = name;
	}

	public void setPassword(String Passwd) {
		userPasswd = Passwd;
	}

	public String getUsername() {
		return userName;
	}

	public String getPassword() {
		return userPasswd;
	}

	@Override
	public void writeToData(DataOut dataOut) throws IOException {
		dataOut.writeUTF(userName);
		dataOut.writeUTF(userPasswd);
	}

	@Override
	public void readFromData(DataIn dataIn) throws IOException {
		userName = dataIn.readUTF();
		userPasswd = dataIn.readUTF();
	}
}
