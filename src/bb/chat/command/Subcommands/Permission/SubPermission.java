package bb.chat.command.subcommands.permission;

import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.chat.network.packet.command.PermissionPacket;
import bb.net.enums.Side;
import bb.net.interfaces.APacket;
import bb.net.interfaces.IIOHandler;

/**
 * Created by BB20101997 on 15.12.2014.
 */
public abstract class SubPermission implements ICommand {

	private final String name;

	public SubPermission(String name) {
		this.name = "permission-" + name;
	}

	@Override
	public final void runCommand(String commandLine, IChat iChat) {
		if(iChat.getIConnectionManager().getSide() == Side.CLIENT) {
			runClient(commandLine, iChat);
		} else {
			runServer(commandLine, iChat);
		}
	}

	protected void runServer(String cL, IChat iChat) {
		String[] command = cL.split(" ", 2);
		iChat.getPermissionRegistry().executePermissionCommand(iChat, iChat.getIConnectionManager().SERVER(), command[0], command[1]);
	}

	protected void runClient(String cL, IChat iChat) {
		String[] command = cL.split(" ", 2);
		APacket p = new PermissionPacket(command[0], command[1]);
		iChat.getIConnectionManager().sendPackage(p, iChat.getIConnectionManager().SERVER());
	}

	@Override
	public String getName() {
		return name;
	}

	public abstract void executePermissionCommand(IChat iChat, IIOHandler executor, String cmd, String rest);
}
