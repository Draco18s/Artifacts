package draco18s.artifacts.plunder;

import com.mod.plunderrummage.api.RewardLister;
import draco18s.artifacts.item.ItemArtifact;
import draco18s.artifacts.item.ItemArtifactArmor;
import net.minecraft.item.Item;

public class PlunderList extends RewardLister
{
  public PlunderList()
  {
    addEasyReward("Artifact", ItemArtifact.instance.itemID, 1, 1, 2);
    addMedReward("Artifact", ItemArtifact.instance.itemID, 1, 1, 8);
    addHardReward("Artifact", ItemArtifact.instance.itemID, 1, 1, 16);

    addEasyReward("ArtifactArmorLH", ItemArtifactArmor.hcloth.itemID, 1, 1, 2);
    addEasyReward("ArtifactArmorLC", ItemArtifactArmor.ccloth.itemID, 1, 1, 2);
    addEasyReward("ArtifactArmorLL", ItemArtifactArmor.lcloth.itemID, 1, 1, 2);
    addEasyReward("ArtifactArmorLB", ItemArtifactArmor.bcloth.itemID, 1, 1, 2);
    
    addMedReward("ArtifactArmorIH", ItemArtifactArmor.hiron.itemID, 1, 1, 5);
    addMedReward("ArtifactArmorIC", ItemArtifactArmor.ciron.itemID, 1, 1, 5);
    addMedReward("ArtifactArmorIL", ItemArtifactArmor.liron.itemID, 1, 1, 5);
    addMedReward("ArtifactArmorIB", ItemArtifactArmor.biron.itemID, 1, 1, 5);
    
    addHardReward("ArtifactArmorDH", ItemArtifactArmor.hdiamond.itemID, 1, 1, 8);
    addHardReward("ArtifactArmorDC", ItemArtifactArmor.cdiamond.itemID, 1, 1, 8);
    addHardReward("ArtifactArmorDL", ItemArtifactArmor.ldiamond.itemID, 1, 1, 8);
    addHardReward("ArtifactArmorDB", ItemArtifactArmor.bdiamond.itemID, 1, 1, 8);
    //list();
  }

  public String getRewardListName()
  {
    return "AncientArtifacts";
  }
}