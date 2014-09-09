package net.rush.model.misc;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import net.rush.model.ItemStack;
import net.rush.protocol.Packet;

public class TradeList extends ArrayList<Trade> {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("deprecation")
	public void writeRecipesToStream(DataOutputStream output, boolean prot18) throws IOException {
		output.writeByte(size() & 255);

		for (Trade trade : this) {
			
			Packet.writeItemstack(trade.getBuying(), output, prot18);
			Packet.writeItemstack(trade.getSelling(), output, prot18);
			
			ItemStack secondItem = trade.getSecondBuying();
			
			output.writeBoolean(secondItem != null);

			if (secondItem != null)
				Packet.writeItemstack(secondItem, output, prot18);

			output.writeBoolean(trade.isLocked());
		}
	}

}
