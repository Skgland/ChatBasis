package bb.chat.command;

import bb.chat.enums.Side;
import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.IIOHandler;
import bb.chat.interfaces.IMessageHandler;
import bb.chat.network.packet.Command.RenamePacket;

/**
 * @author BB20101997
 */
public class Rename implements ICommand {


	@Override
	public int maxParameterCount() {
		return 2;
	}

	@Override
	public int minParameterCount() {
		return 2;
	}

	@Override
	public String[] getAlias() {
		return new String[0];
	}

	@Override
	public String getName() {

		return "rename";
	}

	@Override
	public boolean runCommand(String commandLine, IMessageHandler imh) {

		if(imh.getSide() == Side.CLIENT) {
			String[] dS = commandLine.split(" ");
			if(dS.length <= 2) {
				return false;
			}
			if("Client".equals(dS[2]) || "SERVER".equals(dS[2])) {
				return false;
			}
			imh.setEmpfaenger(IMessageHandler.ALL);
			imh.sendPackage(new RenamePacket(dS[1], dS[2]));
			return true;
		} else {
			String[] dS = commandLine.split(" ");
			if(dS.length > 2) {
				IIOHandler ica = imh.getUserByName(dS[1]);
				if(ica != null) {
					imh.getActor().setActorName(dS[2]);
					imh.println("[" + imh.getActor().getActorName() + "] " + dS[1] + " is now known as " + dS[2]);
					imh.setEmpfaenger(IMessageHandler.ALL);
					imh.sendPackage(new RenamePacket(dS[1], dS[2]));
					return true;
				}

			}
			return false;
		}
	}

	@Override
	public String[] helpCommand() {

		return new String[]{"/rename <new Name>", "Used to rename you in Chat!"};
	}

	@Override
	public boolean debugModeOnly() {
		return false;
	}

}
