package bb.chat.chat;

import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.chat.network.packet.chatting.ChatPacket;
import bb.net.enums.Side;
import bb.net.interfaces.IIOHandler;

import java.util.LinkedList;

/**
 * Created by BB20101997 on 26.03.2015.
 */
public class WorkingThread {

	final IChat iChat;
	WorkingRunnable workingRunnable;
	Thread          workingThread;

	public WorkingThread(IChat ic) {
		iChat = ic;
	}

	final synchronized void start() {
		if(workingRunnable == null) {
			workingRunnable = new WorkingRunnable();
		}
		if(workingThread != null) {
			workingRunnable.stop();
			while(workingThread.isAlive()) ;
		}
		workingThread = new Thread(workingRunnable);
		workingThread.setName("WorkingThread");
		workingThread.setDaemon(true);
		workingThread.start();
	}

	final synchronized void stop() {
		if(workingRunnable != null) {
			workingRunnable.stop();
			workingRunnable = null;
			workingThread = null;
		}
	}


	//adds the given string to the list of Strings to be processed
	//pass down
	void addLine(String s) {
		workingRunnable.addInput(s);
	}

	@SuppressWarnings("HardcodedFileSeparator")
	private class WorkingRunnable implements Runnable {


		private boolean keepGoing = true;

		public void stop() {
			keepGoing = false;
		}

		private final LinkedList<String> toProcess = new LinkedList<>();

		//adds the given string to the list of Strings to be processed
		public void addInput(String s) {
			toProcess.add(s);
		}

		public void run() {
			Side side = iChat.getIConnectionHandler().getSide();
			String s;
			do {
				if(!toProcess.isEmpty()) {

					s = toProcess.pollFirst();

					if(s.startsWith(ICommand.COMMAND_INIT_STRING)) {

						String[] strA = s.split(" ");
						strA[0] = strA[0].replace(ICommand.COMMAND_INIT_STRING, "");
						ICommand ic = iChat.getCommandRegistry().getCommand(strA[0]);

						if(ic != null) {
							if(side == Side.SERVER) {
								ic.runCommand(s, iChat);
							} else {
								ic.runCommand(s, iChat);
							}
						} else {
							iChat.getBasicChatPanel().println("[" + iChat.getLocalActor().getActorName() + "]Please enter a valid command!");
						}

					} else {

						String aName = iChat.getLocalActor().getActorName();
						IIOHandler iA = iChat.getIConnectionHandler().ALL();

						iChat.getIConnectionHandler().sendPackage(new ChatPacket(s, aName), iA);

						if(side == Side.SERVER) {
							iChat.getBasicChatPanel().println("[" + iChat.getLocalActor().getActorName() + "] " + s);
						}

					}
				}
			} while(keepGoing);
		}
	}

}