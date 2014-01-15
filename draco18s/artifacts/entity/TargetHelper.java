package draco18s.artifacts.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityOwnable;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class TargetHelper
{
	private static final HashMap<String, TargetHelper> TARGET_HELPERS = new HashMap();
	public static File SAVE_DIRECTORY;
	public static final byte PERMISSION_TARGET = 1;
	public static final byte PERMISSION_USE = 2;
	public static final byte PERMISSION_OPEN = 4;
	public static final byte HIGHEST_PERMISSION = 4;
	//public static final boolean HOSTILE = false;//Properties.getBoolean("_general", "hostile");

	//public static final boolean ATTACK_UNKNOWN = false;//Properties.getBoolean("_general", "attack_unknown_players");
	public String owner;
	private boolean destroy;
	private HashMap<String, Byte> permissions = new HashMap();

	private HashSet<Class> mobBlacklist = new HashSet();

	private ArrayList<Class> mobWhitelist = new ArrayList();

	private TargetHelper(String username) {
		this.owner = username;
		TARGET_HELPERS.put(this.owner, this);
		if (this.owner != null)
			if (hasSave()) {
				load();
			} else {
				setPermissions(this.owner, 7);
				whitelist(EntitySilverfish.class);
				whitelist(EntitySkeleton.class);
				whitelist(EntitySlime.class);
				whitelist(EntitySpider.class);
				whitelist(EntityWitch.class);
				whitelist(EntityZombie.class);
			}
	}

	public static TargetHelper getTargetHelper(String owner)
	{
		if (owner == "")
			owner = null;
		TargetHelper targetHelper = (TargetHelper)TARGET_HELPERS.get(owner);
		if (targetHelper == null)
			targetHelper = new TargetHelper(owner);
		return targetHelper;
	}

	public boolean canDamagePlayer(String username)
	{
		/*if (HOSTILE)
			return true;
		if (!this.permissions.containsKey(username)) {
			return ATTACK_UNKNOWN;
		}
		return (((Byte)this.permissions.get(username)).byteValue() & 0x1) == 0;*/
		return true;
	}

	public byte getPermissions(String username)
	{
		if ((this.owner == null) || (!this.permissions.containsKey(username)))
			return 0;
		return ((Byte)this.permissions.get(username)).byteValue();
	}

	public boolean playerHasPermission(String username, int value)
	{
		return (this.owner == null) || ((this.permissions.containsKey(username)) && ((((Byte)this.permissions.get(username)).byteValue() & value) == value));
	}

	public boolean maintainTarget(Entity entity)
	{
		if ((!(entity instanceof EntityLivingBase)) || (!entity.isEntityAlive()))
			return false;
		return true;
	}

	public boolean isValidTarget(Entity entity)
	{
		if (!maintainTarget(entity))
			return false;
		if (this.owner == null && entity instanceof EntityOwnable)
			return ((EntityOwnable)entity).getOwner() != null;
		if (((entity instanceof EntityOwnable)) && (
				(this.owner.equals(((EntityOwnable)entity).getOwnerName())) || (!canDamagePlayer(((EntityOwnable)entity).getOwnerName()))))
			return false;
		if ((entity instanceof EntityPlayer))
			return canDamagePlayer(((EntityPlayer)entity).username);
		Class entityClass = entity.getClass();
		if ((!this.mobBlacklist.contains(entityClass)) && (isWhitelisted(entityClass)))
			return true;
		if(entity instanceof EntityCreature) {
			if(entity instanceof EntityAgeable)
				return false;
			return true;
		}
		return false;
	}

	public static void destroyAll()
	{
		for (Map.Entry entry : TARGET_HELPERS.entrySet())
			((TargetHelper)entry.getValue()).softDestroy();
		TARGET_HELPERS.clear();
	}
	private void softDestroy() {
		this.destroy = true;
	}
	public void destroy() {
		this.destroy = true;
		TARGET_HELPERS.remove(this);
	}
	public boolean destroyed() {
		return this.destroy;
	}

	public void setPermissions(String username, int value)
	{
		if (value > 0)
			this.permissions.put(username, Byte.valueOf((byte)value));
		else
			this.permissions.remove(username); 
	}

	public void addPermissions(String username, int value) { if (this.permissions.containsKey(username)) {
		byte prev = ((Byte)this.permissions.get(username)).byteValue();
		value |= prev;
	}
	this.permissions.put(username, Byte.valueOf((byte)value)); }

	public void remPermissions(String username, int value) {
		if (this.permissions.containsKey(username)) {
			byte prev = ((Byte)this.permissions.get(username)).byteValue();
			prev = (byte)(prev & (value ^ 0xFFFFFFFF));
			if (prev > 0)
				this.permissions.put(username, Byte.valueOf(prev));
			else
				this.permissions.remove(username);
		}
	}

	public void blacklist(Class entityClass)
	{
		if ((!this.mobBlacklist.contains(entityClass)) && (isWhitelisted(entityClass)))
			this.mobBlacklist.add(entityClass); 
	}

	public void unblacklist(Class entityClass) { this.mobBlacklist.remove(entityClass); }

	public void toggleBlacklist(Class entityClass) {
		if (this.mobBlacklist.contains(entityClass))
			unblacklist(entityClass);
		else
			blacklist(entityClass); 
	}

	public void whitelist(Class entityClass) { if ((!this.mobWhitelist.contains(entityClass)) && (EntityLivingBase.class.isAssignableFrom(entityClass)) && (!EntityPlayer.class.isAssignableFrom(entityClass)) && (clearWhitelistFor(entityClass)))
		this.mobWhitelist.add(entityClass); }

	public void unwhitelist(Class entityClass) {
		clearBlacklistFor(entityClass);
		this.mobWhitelist.remove(entityClass);
	}
	public void toggleWhitelist(Class entityClass) {
		if (this.mobWhitelist.contains(entityClass))
			unwhitelist(entityClass);
		else
			whitelist(entityClass);
	}

	public boolean isWhitelisted(Class entityClass)
	{
		for (Class allowedClass : this.mobWhitelist) if (allowedClass.isAssignableFrom(entityClass))
			return true;
		return false;
	}

	private boolean clearWhitelistFor(Class entityClass)
	{
		Class allowedClass;
		for (Iterator iterator = this.mobWhitelist.iterator(); (iterator.hasNext()) && ((allowedClass = (Class)iterator.next()) != null); ) {
			if (allowedClass.isAssignableFrom(entityClass))
				return false;
			if (entityClass.isAssignableFrom(allowedClass))
				iterator.remove();
		}
		return true;
	}

	private void clearBlacklistFor(Class entityClass)
	{
		Class disallowedClass;
		for (Iterator iterator = this.mobBlacklist.iterator(); (iterator.hasNext()) && ((disallowedClass = (Class)iterator.next()) != null); ) if (entityClass.isAssignableFrom(disallowedClass))
			iterator.remove();
	}

	public boolean hasSave()
	{
		if (this.owner == null)
			return false;
		try {
			return new File(SAVE_DIRECTORY, this.owner + ".txt").exists();
		}
		catch (Exception ex) {
			System.out.println("Failed to fetch target save data (" + this.owner + ".txt)!");
			ex.printStackTrace();
		}
		return false;
	}

	public void save()
	{
		if ((this.owner == null) || (destroyed()))
			return;
		try {
			File saveTmp = new File(SAVE_DIRECTORY, this.owner + ".txt.tmp");
			File save = new File(SAVE_DIRECTORY, this.owner + ".txt");
			SAVE_DIRECTORY.mkdirs();
			saveTmp.createNewFile();
			FileWriter out = new FileWriter(saveTmp);
			out.write("player_permissions");
			for (Map.Entry entry : this.permissions.entrySet())
				out.write("\n" + Integer.toBinaryString(((Byte)entry.getValue()).byteValue()) + " " + (String)entry.getKey());
			out.write("\n\nwhitelist");
			for (Class entityClass : this.mobWhitelist)
				out.write("\n" + entityClass.getName());
			out.write("\n\nblacklist");
			for (Class entityClass : this.mobBlacklist)
				out.write("\n" + entityClass.getName());
			out.close();
			save.delete();
			saveTmp.renameTo(save);
		}
		catch (Exception ex) {
			System.out.println("Failed to save target data (" + this.owner + ".txt)!");
			ex.printStackTrace();
		}
	}

	public void load()
	{
		if ((this.owner == null) || (destroyed()))
			return;
		try {
			this.permissions.clear();
			this.mobBlacklist.clear();
			this.mobWhitelist.clear();
			File save = new File(SAVE_DIRECTORY, this.owner + ".txt");
			if (!save.exists())
				return;
			FileInputStream in = new FileInputStream(save);

			byte status = 0;
			String key = "";
			String value = "";
			int dat;
			while ((dat = in.read()) >= 0)
				if (dat != 13)
				{
					if (dat == 10) {
						if (status == 0) {
							if (key != "")
							{
								if ((!key.equalsIgnoreCase("player_permissions")) && (!key.equalsIgnoreCase("whitelist")) && (!key.equalsIgnoreCase("blacklist"))) {
									System.out.println("Unrecognized value in player config: " + key + " (" + this.owner + ".txt)!");
									key = "";
								}
								else {
									status = 1;
								}
							} } else if (status == 1) {
								if (value == "") {
									key = "";
									status = 0;
								}
								else {
									if (key.equalsIgnoreCase("player_permissions")) {
										for (int i = 0; i < value.length(); i++) {
											if (value.charAt(i) == ' ') {
												try {
													setPermissions(value.substring(i + 1), Byte.valueOf((byte)Integer.parseInt(value.substring(0, i), 2)).byteValue());
												}
												catch (Exception ex) {
													System.out.println("Invalid player permissions entry: " + value + " (" + this.owner + ".txt)!");
												}
											}
										}
									}
									else if (key.equalsIgnoreCase("whitelist")) {
										try {
											whitelist(Class.forName(value));
										}
										catch (Exception ex) {
											System.out.println("Invalid whitelist entry: " + value + " (" + this.owner + ".txt)!");
										}
									}
									else if (key.equalsIgnoreCase("blacklist")) {
										try {
											blacklist(Class.forName(value));
										}
										catch (Exception ex) {
											System.out.println("Invalid blacklist entry: " + value + " (" + this.owner + ".txt)!");
										}
									}
									value = "";
								}
							}
					}
					else if (status == 0)
						key = key + Character.toString((char)dat);
					else if (status == 1)
						value = value + Character.toString((char)dat);
				}
			in.close();
		}
		catch (Exception ex) {
			System.out.println("Failed to load target data (" + this.owner + ".txt)!");
			ex.printStackTrace();
		}
	}

	/*public static ItemStack book(int id)
	{
		ItemStack book = new ItemStack(id == 0 ? Item.writableBook : Item.writtenBook);
		BookHelper.addPages(BookHelper.setTitle(book, "§b" + (id == 0 ? "Player Permissions" : "Mob Target List")), new String[] { "" }).stackTagCompound.setByte("umt", (byte)id);
		if (id == 0) {
			EffectHelper.setItemName(book, 11, "Player Permissions");
			EffectHelper.setItemGlowing(book);
		}
		return book;
	}*/

	public static ItemStack write(String username, int id)
	{
		return write(username, new ItemStack(id == 0 ? Item.writableBook : Item.writtenBook), id);
	}
	public static ItemStack write(String username, ItemStack book, int id) {
		return getTargetHelper(username).writeTo(username, book, id); } 
	private ItemStack writeTo(String username, ItemStack book, int id) {
		System.out.println("[Utility Mobs] Unable to write: Missing Bytecode.");
		return book;
	}
	//private ItemStack writeTo(String username, ItemStack book, int id) { // Byte code:
	//   0: aload_2
	//   1: ifnonnull +5 -> 6
	//   4: aconst_null
	//   5: areturn
	//   6: aload_2
	//   7: invokestatic 487	toast/utilityMobs/BookHelper:removePages	(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;
	//   10: pop
	//   11: iload_3
	//   12: ifne +304 -> 316
	//   15: aload_2
	//   16: getfield 490	net/minecraft/item/ItemStack:itemID	I
	//   19: getstatic 428	net/minecraft/item/Item:writableBook	Lnet/minecraft/item/Item;
	//   22: getfield 493	net/minecraft/item/Item:itemID	I
	//   25: if_icmpne +52 -> 77
	//   28: aload_2
	//   29: bipush 11
	//   31: ldc_w 438
	//   34: invokestatic 468	toast/utilityMobs/EffectHelper:setItemName	(Lnet/minecraft/item/ItemStack;ILjava/lang/String;)V
	//   37: aload_2
	//   38: bipush 7
	//   40: iconst_1
	//   41: anewarray 74	java/lang/String
	//   44: dup
	//   45: iconst_0
	//   46: new 271	java/lang/StringBuilder
	//   49: dup
	//   50: invokespecial 272	java/lang/StringBuilder:<init>	()V
	//   53: ldc_w 495
	//   56: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
	//   59: aload_1
	//   60: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
	//   63: invokevirtual 281	java/lang/StringBuilder:toString	()Ljava/lang/String;
	//   66: aastore
	//   67: invokestatic 499	toast/utilityMobs/EffectHelper:setItemText	(Lnet/minecraft/item/ItemStack;I[Ljava/lang/String;)V
	//   70: aload_2
	//   71: invokestatic 472	toast/utilityMobs/EffectHelper:setItemGlowing	(Lnet/minecraft/item/ItemStack;)V
	//   74: goto +16 -> 90
	//   77: aload_2
	//   78: ldc_w 501
	//   81: aload_1
	//   82: invokestatic 505	toast/utilityMobs/BookHelper:setTitleAndAuthor	(Lnet/minecraft/item/ItemStack;Ljava/lang/String;Ljava/lang/String;)Lnet/minecraft/item/ItemStack;
	//   85: pop
	//   86: aload_2
	//   87: invokestatic 508	toast/utilityMobs/EffectHelper:clearItemText	(Lnet/minecraft/item/ItemStack;)V
	//   90: aload_2
	//   91: iconst_4
	//   92: anewarray 74	java/lang/String
	//   95: dup
	//   96: iconst_0
	//   97: ldc_w 510
	//   100: aastore
	//   101: dup
	//   102: iconst_1
	//   103: ldc_w 512
	//   106: aastore
	//   107: dup
	//   108: iconst_2
	//   109: ldc_w 514
	//   112: aastore
	//   113: dup
	//   114: iconst_3
	//   115: ldc_w 516
	//   118: aastore
	//   119: invokestatic 450	toast/utilityMobs/BookHelper:addPages	(Lnet/minecraft/item/ItemStack;[Ljava/lang/String;)Lnet/minecraft/item/ItemStack;
	//   122: pop
	//   123: aload_0
	//   124: getfield 47	toast/utilityMobs/TargetHelper:permissions	Ljava/util/HashMap;
	//   127: invokevirtual 519	java/util/HashMap:size	()I
	//   130: ifgt +21 -> 151
	//   133: aload_2
	//   134: iconst_1
	//   135: anewarray 74	java/lang/String
	//   138: dup
	//   139: iconst_0
	//   140: ldc_w 521
	//   143: aastore
	//   144: invokestatic 450	toast/utilityMobs/BookHelper:addPages	(Lnet/minecraft/item/ItemStack;[Ljava/lang/String;)Lnet/minecraft/item/ItemStack;
	//   147: pop
	//   148: goto +577 -> 725
	//   151: aload_0
	//   152: getfield 47	toast/utilityMobs/TargetHelper:permissions	Ljava/util/HashMap;
	//   155: invokevirtual 519	java/util/HashMap:size	()I
	//   158: anewarray 74	java/lang/String
	//   161: astore 4
	//   163: iconst_0
	//   164: istore 5
	//   166: iconst_1
	//   167: istore 6
	//   169: aload 4
	//   171: iconst_0
	//   172: ldc_w 523
	//   175: aastore
	//   176: aload_0
	//   177: getfield 47	toast/utilityMobs/TargetHelper:permissions	Ljava/util/HashMap;
	//   180: invokevirtual 186	java/util/HashMap:entrySet	()Ljava/util/Set;
	//   183: invokeinterface 192 1 0
	//   188: astore 7
	//   190: aload 7
	//   192: invokeinterface 197 1 0
	//   197: ifeq +109 -> 306
	//   200: aload 7
	//   202: invokeinterface 201 1 0
	//   207: checkcast 7	java/util/Map$Entry
	//   210: astore 8
	//   212: iload 6
	//   214: iload 6
	//   216: iconst_1
	//   217: iadd
	//   218: i2b
	//   219: istore 6
	//   221: bipush 10
	//   223: if_icmpne +16 -> 239
	//   226: iconst_0
	//   227: istore 6
	//   229: aload 4
	//   231: iinc 5 1
	//   234: iload 5
	//   236: ldc 101
	//   238: aastore
	//   239: new 271	java/lang/StringBuilder
	//   242: dup
	//   243: invokespecial 272	java/lang/StringBuilder:<init>	()V
	//   246: aload 4
	//   248: iload 5
	//   250: dup2_x1
	//   251: aaload
	//   252: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
	//   255: aload 8
	//   257: invokeinterface 204 1 0
	//   262: checkcast 120	java/lang/Byte
	//   265: invokevirtual 124	java/lang/Byte:byteValue	()B
	//   268: invokestatic 331	java/lang/Integer:toBinaryString	(I)Ljava/lang/String;
	//   271: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
	//   274: ldc_w 333
	//   277: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
	//   280: aload 8
	//   282: invokeinterface 336 1 0
	//   287: checkcast 74	java/lang/String
	//   290: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
	//   293: ldc_w 325
	//   296: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
	//   299: invokevirtual 281	java/lang/StringBuilder:toString	()Ljava/lang/String;
	//   302: aastore
	//   303: goto -113 -> 190
	//   306: aload_2
	//   307: aload 4
	//   309: invokestatic 450	toast/utilityMobs/BookHelper:addPages	(Lnet/minecraft/item/ItemStack;[Ljava/lang/String;)Lnet/minecraft/item/ItemStack;
	//   312: pop
	//   313: goto +412 -> 725
	//   316: iload_3
	//   317: iconst_1
	//   318: if_icmpne +407 -> 725
	//   321: aload_2
	//   322: ldc_w 527
	//   325: aload_1
	//   326: invokestatic 505	toast/utilityMobs/BookHelper:setTitleAndAuthor	(Lnet/minecraft/item/ItemStack;Ljava/lang/String;Ljava/lang/String;)Lnet/minecraft/item/ItemStack;
	//   329: iconst_3
	//   330: anewarray 74	java/lang/String
	//   333: dup
	//   334: iconst_0
	//   335: ldc_w 529
	//   338: aastore
	//   339: dup
	//   340: iconst_1
	//   341: ldc_w 531
	//   344: aastore
	//   345: dup
	//   346: iconst_2
	//   347: ldc_w 533
	//   350: aastore
	//   351: invokestatic 450	toast/utilityMobs/BookHelper:addPages	(Lnet/minecraft/item/ItemStack;[Ljava/lang/String;)Lnet/minecraft/item/ItemStack;
	//   354: pop
	//   355: aload_0
	//   356: getfield 57	toast/utilityMobs/TargetHelper:mobWhitelist	Ljava/util/ArrayList;
	//   359: invokevirtual 534	java/util/ArrayList:size	()I
	//   362: ifgt +21 -> 383
	//   365: aload_2
	//   366: iconst_1
	//   367: anewarray 74	java/lang/String
	//   370: dup
	//   371: iconst_0
	//   372: ldc_w 536
	//   375: aastore
	//   376: invokestatic 450	toast/utilityMobs/BookHelper:addPages	(Lnet/minecraft/item/ItemStack;[Ljava/lang/String;)Lnet/minecraft/item/ItemStack;
	//   379: pop
	//   380: goto +160 -> 540
	//   383: aload_0
	//   384: getfield 57	toast/utilityMobs/TargetHelper:mobWhitelist	Ljava/util/ArrayList;
	//   387: invokevirtual 534	java/util/ArrayList:size	()I
	//   390: anewarray 74	java/lang/String
	//   393: astore 4
	//   395: iconst_0
	//   396: istore 5
	//   398: iconst_1
	//   399: istore 6
	//   401: aload 4
	//   403: iconst_0
	//   404: ldc_w 538
	//   407: aastore
	//   408: aload_0
	//   409: getfield 57	toast/utilityMobs/TargetHelper:mobWhitelist	Ljava/util/ArrayList;
	//   412: invokevirtual 257	java/util/ArrayList:iterator	()Ljava/util/Iterator;
	//   415: astore 7
	//   417: aload 7
	//   419: invokeinterface 197 1 0
	//   424: ifeq +109 -> 533
	//   427: aload 7
	//   429: invokeinterface 201 1 0
	//   434: checkcast 179	java/lang/Class
	//   437: astore 8
	//   439: iload 6
	//   441: iload 6
	//   443: iconst_1
	//   444: iadd
	//   445: i2b
	//   446: istore 6
	//   448: bipush 10
	//   450: if_icmpne +16 -> 466
	//   453: iconst_0
	//   454: istore 6
	//   456: aload 4
	//   458: iinc 5 1
	//   461: iload 5
	//   463: ldc 101
	//   465: aastore
	//   466: aconst_null
	//   467: astore 9
	//   469: getstatic 544	net/minecraft/entity/EntityList:field-75626_c	Ljava/util/Map;
	//   472: aload 8
	//   474: invokeinterface 545 2 0
	//   479: checkcast 74	java/lang/String
	//   482: astore 9
	//   484: goto +5 -> 489
	//   487: astore 10
	//   489: aload 9
	//   491: ifnonnull +8 -> 499
	//   494: ldc_w 547
	//   497: astore 9
	//   499: new 271	java/lang/StringBuilder
	//   502: dup
	//   503: invokespecial 272	java/lang/StringBuilder:<init>	()V
	//   506: aload 4
	//   508: iload 5
	//   510: dup2_x1
	//   511: aaload
	//   512: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
	//   515: aload 9
	//   517: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
	//   520: ldc_w 325
	//   523: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
	//   526: invokevirtual 281	java/lang/StringBuilder:toString	()Ljava/lang/String;
	//   529: aastore
	//   530: goto -113 -> 417
	//   533: aload_2
	//   534: aload 4
	//   536: invokestatic 450	toast/utilityMobs/BookHelper:addPages	(Lnet/minecraft/item/ItemStack;[Ljava/lang/String;)Lnet/minecraft/item/ItemStack;
	//   539: pop
	//   540: aload_0
	//   541: getfield 52	toast/utilityMobs/TargetHelper:mobBlacklist	Ljava/util/HashSet;
	//   544: invokevirtual 548	java/util/HashSet:size	()I
	//   547: ifgt +21 -> 568
	//   550: aload_2
	//   551: iconst_1
	//   552: anewarray 74	java/lang/String
	//   555: dup
	//   556: iconst_0
	//   557: ldc_w 550
	//   560: aastore
	//   561: invokestatic 450	toast/utilityMobs/BookHelper:addPages	(Lnet/minecraft/item/ItemStack;[Ljava/lang/String;)Lnet/minecraft/item/ItemStack;
	//   564: pop
	//   565: goto +160 -> 725
	//   568: aload_0
	//   569: getfield 52	toast/utilityMobs/TargetHelper:mobBlacklist	Ljava/util/HashSet;
	//   572: invokevirtual 548	java/util/HashSet:size	()I
	//   575: anewarray 74	java/lang/String
	//   578: astore 4
	//   580: iconst_0
	//   581: istore 5
	//   583: iconst_1
	//   584: istore 6
	//   586: aload 4
	//   588: iconst_0
	//   589: ldc_w 552
	//   592: aastore
	//   593: aload_0
	//   594: getfield 52	toast/utilityMobs/TargetHelper:mobBlacklist	Ljava/util/HashSet;
	//   597: invokevirtual 262	java/util/HashSet:iterator	()Ljava/util/Iterator;
	//   600: astore 7
	//   602: aload 7
	//   604: invokeinterface 197 1 0
	//   609: ifeq +109 -> 718
	//   612: aload 7
	//   614: invokeinterface 201 1 0
	//   619: checkcast 179	java/lang/Class
	//   622: astore 8
	//   624: iload 6
	//   626: iconst_1
	//   627: iadd
	//   628: i2b
	//   629: istore 6
	//   631: iload 6
	//   633: bipush 10
	//   635: if_icmpne +16 -> 651
	//   638: iconst_0
	//   639: istore 6
	//   641: aload 4
	//   643: iinc 5 1
	//   646: iload 5
	//   648: ldc 101
	//   650: aastore
	//   651: aconst_null
	//   652: astore 9
	//   654: getstatic 544	net/minecraft/entity/EntityList:field-75626_c	Ljava/util/Map;
	//   657: aload 8
	//   659: invokeinterface 545 2 0
	//   664: checkcast 74	java/lang/String
	//   667: astore 9
	//   669: goto +5 -> 674
	//   672: astore 10
	//   674: aload 9
	//   676: ifnonnull +8 -> 684
	//   679: ldc_w 547
	//   682: astore 9
	//   684: new 271	java/lang/StringBuilder
	//   687: dup
	//   688: invokespecial 272	java/lang/StringBuilder:<init>	()V
	//   691: aload 4
	//   693: iload 5
	//   695: dup2_x1
	//   696: aaload
	//   697: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
	//   700: aload 9
	//   702: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
	//   705: ldc_w 325
	//   708: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
	//   711: invokevirtual 281	java/lang/StringBuilder:toString	()Ljava/lang/String;
	//   714: aastore
	//   715: goto -113 -> 602
	//   718: aload_2
	//   719: aload 4
	//   721: invokestatic 450	toast/utilityMobs/BookHelper:addPages	(Lnet/minecraft/item/ItemStack;[Ljava/lang/String;)Lnet/minecraft/item/ItemStack;
	//   724: pop
	//   725: aload_2
	//   726: getfield 454	net/minecraft/item/ItemStack:stackTagCompound	Lnet/minecraft/nbt/NBTTagCompound;
	//   729: ldc_w 456
	//   732: iload_3
	//   733: i2b
	//   734: invokevirtual 462	net/minecraft/nbt/NBTTagCompound:setByte	(Ljava/lang/String;B)V
	//   737: aload_2
	//   738: areturn
	//
	// Exception table:
	//   from	to	target	type
	//   469	484	487	java/lang/Exception
	//   654	669	672	java/lang/Exception } 
	public static void read(String username, ItemStack book) { getTargetHelper(username).readFrom(username, book); }

	private void readFrom(String username, ItemStack book) {
		if ((book == null) || (book.stackTagCompound == null) || (!book.stackTagCompound.hasKey("pages")))
			return;
		this.permissions.clear();
		NBTTagList pages = book.stackTagCompound.getTagList("pages");

		for (int p = 0; p < pages.tagCount(); p++) {
			String page = ((NBTTagString)pages.tagAt(p)).data;
			int index;
			while ((index = page.indexOf("\n")) >= 0) {
				String line = page.substring(0, index);
				page = page.substring(index + 1);
				readLine(line);
			}
			readLine(page);
		}
		writeTo(username, book, 0);
		save();
	}
	private void readLine(String line) {
		int index = line.indexOf(" ");
		if (index <= 0)
			return;
		try {
			byte permission = (byte)Math.max(0, Integer.parseInt(line.substring(0, index), 2));
			String username = line.substring(index + 1);
			if (username.indexOf(" ") < 0)
				setPermissions(username, permission);
		}
		catch (Exception ex)
		{
		}
	}

	public static void interact(String username, ItemStack book, int id, EntityLivingBase entity, boolean sneaking) {
		getTargetHelper(username).interactWith(username, book, id, entity, sneaking);
	}
	private void interactWith(String username, ItemStack book, int id, EntityLivingBase entity, boolean sneaking) {
		if (id == 0) {
			if (!(entity instanceof EntityPlayer))
				return;
			byte playerPermissions = getPermissions(((EntityPlayer)entity).username);
			if (sneaking) {
				if (playerPermissions > 0) for (byte permission = 4; permission > 0; permission = (byte)(permission >> 1)) if ((permission & playerPermissions) > 0) {
					remPermissions(((EntityPlayer)entity).username, permission);
					save();
					break;
				}
			}
			else {
				for (byte permission = 1; permission <= 4; permission = (byte)(permission << 1)) if ((permission & playerPermissions) == 0) {
					addPermissions(((EntityPlayer)entity).username, permission);
					save();
					break;
				}
			}
		}
		else if (id == 1) {
			if ((entity instanceof EntityPlayer))
				return;
			if (sneaking)
				toggleBlacklist(entity.getClass());
			else
				toggleWhitelist(entity.getClass());
			save();
		}
		writeTo(username, book, id);
	}
}

/* Location:           C:\Users\Major\Downloads\UtilityMobs 2.1 for MC 1.6.2.zip
 * Qualified Name:     toast.utilityMobs.TargetHelper
 * JD-Core Version:    0.6.2
 */