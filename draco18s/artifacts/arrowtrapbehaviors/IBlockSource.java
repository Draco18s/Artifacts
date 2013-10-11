package draco18s.artifacts.arrowtrapbehaviors;

import net.minecraft.tileentity.TileEntity;

public interface IBlockSource extends ILocatableSource
{
    double getX();

    double getY();

    double getZ();

    int getXInt();

    int getYInt();

    int getZInt();

    int getBlockMetadata();

    TileEntity getBlockTileEntity();
}
