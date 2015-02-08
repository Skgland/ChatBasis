package bb.chat.network.packet;

import bb.chat.interfaces.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BB20101997 on 31.08.2014.
 */
public class PacketDistributor implements IPacketDistributor<APacket> {

	private final IConnectionHandler IMH;

	public PacketDistributor(IConnectionHandler imh) {
		IMH = imh;
	}

	private final List<IPacketHandler> PHList = new ArrayList<>();

	@Override
	public int registerPacketHandler(IPacketHandler iph) {
		if(!PHList.contains(iph)) {
			PHList.add(iph);
		}
		return PHList.indexOf(iph);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void distributePacket(int id, byte[] data, IIOHandler sender) {

		APacket p = IMH.getIChatInstance().getPacketRegistrie().getNewPacketOfID(id);
		try {
			p.readFromData(DataIn.newInstance(data.clone()));
		} catch(IOException e) {
			e.printStackTrace();
		}

		main:
		for(IPacketHandler iph : PHList) {
			for(Class c : iph.getAssociatedPackets()) {
				if(c.equals(IMH.getIChatInstance().getPacketRegistrie().getPacketClassByID(id))) {
					iph.HandlePacket(p.copy(), sender);
					continue main;

				}
			}
		}


	}


}
