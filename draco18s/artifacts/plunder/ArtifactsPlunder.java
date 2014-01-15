package draco18s.artifacts.plunder;

import com.mod.plunderrummage.api.PlunderRummageAPI;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid="ArtifactsPlunder", name="Artifacts Plunder", version="0.3.0", dependencies="required-after:Artifacts;required-after:PlunderRummage")
@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class ArtifactsPlunder
{

  @Mod.Instance("ArtifactsPlunder")
  public static ArtifactsPlunder instance;

  @Mod.EventHandler
  public void postInit(FMLPostInitializationEvent event)
  {
    PlunderRummageAPI.registerRewardLister(new PlunderList());
  }
}