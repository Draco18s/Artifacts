package draco18s.artifacts.client;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class TESwordRenderer extends TileEntitySpecialRenderer {
	private SwordModel sword = new SwordModel();
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
		sword.render(tileentity, d0, d1, d2);
	}
}
