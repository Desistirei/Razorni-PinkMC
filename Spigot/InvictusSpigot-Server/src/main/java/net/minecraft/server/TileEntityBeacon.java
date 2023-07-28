package net.minecraft.server;

import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PotionEffectAddEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.github.paperspigot.event.block.BeaconEffectEvent;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TileEntityBeacon extends TileEntityContainer implements IUpdatePlayerListBox, IInventory {

    public static final MobEffectList[][] a = new MobEffectList[][] { { MobEffectList.FASTER_MOVEMENT, MobEffectList.FASTER_DIG}, { MobEffectList.RESISTANCE, MobEffectList.JUMP}, { MobEffectList.INCREASE_DAMAGE}, { MobEffectList.REGENERATION}};
    private boolean i;
    private int j = -1;
    private int k;
    private int l;
    private ItemStack inventorySlot;
    private String n;
    // CraftBukkit start - add fields and methods
    public final List<HumanEntity> transaction = new java.util.ArrayList<>();
    private int maxStack = MAX_STACK;

    public ItemStack[] getContents() {
        return new ItemStack[] { this.inventorySlot };
    }

    public void onOpen(CraftHumanEntity who) {
        transaction.add(who);
    }

    public void onClose(CraftHumanEntity who) {
        transaction.remove(who);
    }

    public List<HumanEntity> getViewers() {
        return transaction;
    }

    public void setMaxStackSize(int size) {
        maxStack = size;
    }
    // CraftBukkit end

    public TileEntityBeacon() {}

    public void c() {
        if (this.world.getTime() % 80L == 0L) {
            this.m();
        }

    }

    public void m() {
        this.B();
        this.A();
    }

    private void A() {
        if (this.i && this.j > 0 && this.k > 0) {
            double d0 = this.j * 10 + 10;
            byte b0 = 0;

            if (this.j >= 4 && this.k == this.l) {
                b0 = 1;
            }

            int i = this.position.getX();
            int j = this.position.getY();
            int k = this.position.getZ();
            AxisAlignedBB axisalignedbb = (new AxisAlignedBB(i, j, k, (i + 1), (j + 1), (k + 1))).grow(d0, d0, d0).a(0.0D, this.world.getHeight(), 0.0D);
            List<EntityHuman> list = this.world.a(EntityHuman.class, axisalignedbb);
            Iterator<EntityHuman> iterator = list.iterator();
            org.bukkit.block.Block block = this.world.getWorld().getBlockAt(this.position.getX(), this.position.getY(), this.position.getZ());
            PotionEffect primaryEffect = new PotionEffect(PotionEffectType.getById(this.k), 180, b0, true, true);
            while (iterator.hasNext()) {
                EntityHuman entityhuman = iterator.next();
                BeaconEffectEvent event = new BeaconEffectEvent(block, primaryEffect, (Player)entityhuman.getBukkitEntity(), true);
                if (CraftEventFactory.callEvent(event).isCancelled())
                    continue;
                PotionEffect effect = event.getEffect();
                entityhuman.addEffect(new MobEffect(effect.getType().getId(), effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.hasParticles()), PotionEffectAddEvent.EffectCause.BEACON);
            }
            if (this.j >= 4 && this.k != this.l && this.l > 0) {
                iterator = list.iterator();
                PotionEffect secondaryEffect = new PotionEffect(PotionEffectType.getById(this.l), 180, 0, true, true);
                while (iterator.hasNext()) {
                    EntityHuman entityhuman = iterator.next();
                    BeaconEffectEvent event = new BeaconEffectEvent(block, secondaryEffect, (Player)entityhuman.getBukkitEntity(), false);
                    if (CraftEventFactory.callEvent(event).isCancelled())
                        continue;
                    PotionEffect effect = event.getEffect();
                    entityhuman.addEffect(new MobEffect(effect.getType().getId(), effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.hasParticles()), PotionEffectAddEvent.EffectCause.BEACON);
                }
            }
        }

    }

    private void B() {

        int j = this.position.getX();
        int k = this.position.getY();
        int l = this.position.getZ();

        this.j = 0;
        this.i = true;
        BeaconColorTracker tileentitybeacon_beaconcolortracker = new BeaconColorTracker(EntitySheep.a(EnumColor.WHITE));

        boolean flag = true;

        int i1;

        for (i1 = k + 1; i1 < 256; ++i1) {
            IBlockData iblockdata = this.world.getType(j, i1, l);
            float[] afloat;

            Block block = iblockdata.getBlock();
            if (block == Blocks.STAINED_GLASS) {
                afloat = EntitySheep.a(iblockdata.get(BlockStainedGlass.COLOR));
            } else {
                if (block != Blocks.STAINED_GLASS_PANE) {
                    if (block.p() >= 15 && block != Blocks.BEDROCK) {
                        this.i = false;
                        break;
                    }

                    tileentitybeacon_beaconcolortracker.a();
                    continue;
                }

                afloat = EntitySheep.a(iblockdata.get(BlockStainedGlassPane.COLOR));
            }

            if (!flag) {
                afloat = new float[] { (tileentitybeacon_beaconcolortracker.b()[0] + afloat[0]) / 2.0F, (tileentitybeacon_beaconcolortracker.b()[1] + afloat[1]) / 2.0F, (tileentitybeacon_beaconcolortracker.b()[2] + afloat[2]) / 2.0F};
            }

            if (Arrays.equals(afloat, tileentitybeacon_beaconcolortracker.b())) {
                tileentitybeacon_beaconcolortracker.a();
            } else {
                tileentitybeacon_beaconcolortracker = new BeaconColorTracker(afloat);
            }

            flag = false;
        }

        if (this.i) {
            for (i1 = 1; i1 <= 4; this.j = i1++) {
                int j1 = k - i1;

                if (j1 < 0) {
                    break;
                }

                boolean flag1 = true;

                for (int k1 = j - i1; k1 <= j + i1 && flag1; ++k1) {
                    for (int l1 = l - i1; l1 <= l + i1; ++l1) {
                        Block block = this.world.getType(k1, j1, l1).getBlock();

                        if (block != Blocks.EMERALD_BLOCK && block != Blocks.GOLD_BLOCK && block != Blocks.DIAMOND_BLOCK && block != Blocks.IRON_BLOCK) {
                            flag1 = false;
                            break;
                        }
                    }
                }

                if (!flag1) {
                    break;
                }
            }

            if (this.j == 0) {
                this.i = false;
            }
        }

    }

    public Packet<?> getUpdatePacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        this.b(nbttagcompound);
        return new PacketPlayOutTileEntityData(this.position, 3, nbttagcompound);
    }

    private int h(int i) {
        if (i >= 0 && i < MobEffectList.byId.length && MobEffectList.byId[i] != null) {
            MobEffectList mobeffectlist = MobEffectList.byId[i];

            return mobeffectlist != MobEffectList.FASTER_MOVEMENT && mobeffectlist != MobEffectList.FASTER_DIG && mobeffectlist != MobEffectList.RESISTANCE && mobeffectlist != MobEffectList.JUMP && mobeffectlist != MobEffectList.INCREASE_DAMAGE && mobeffectlist != MobEffectList.REGENERATION ? 0 : i;
        } else {
            return 0;
        }
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.k = this.h(nbttagcompound.getInt("Primary"));
        this.l = this.h(nbttagcompound.getInt("Secondary"));
        this.j = nbttagcompound.getInt("Levels");
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInt("Primary", this.k);
        nbttagcompound.setInt("Secondary", this.l);
        nbttagcompound.setInt("Levels", this.j);
    }

    public int getSize() {
        return 1;
    }

    public ItemStack getItem(int i) {
        return i == 0 ? this.inventorySlot : null;
    }

    public ItemStack splitStack(int i, int j) {
        if (i == 0 && this.inventorySlot != null) {
            if (j >= this.inventorySlot.count) {
                ItemStack itemstack = this.inventorySlot;

                this.inventorySlot = null;
                return itemstack;
            } else {
                this.inventorySlot.count -= j;
                return new ItemStack(this.inventorySlot.getItem(), j, this.inventorySlot.getData());
            }
        } else {
            return null;
        }
    }

    public ItemStack splitWithoutUpdate(int i) {
        if (i == 0 && this.inventorySlot != null) {
            ItemStack itemstack = this.inventorySlot;

            this.inventorySlot = null;
            return itemstack;
        } else {
            return null;
        }
    }

    public void setItem(int i, ItemStack itemstack) {
        if (i == 0) {
            this.inventorySlot = itemstack;
        }

    }

    public String getName() {
        return this.hasCustomName() ? this.n : "container.beacon";
    }

    public boolean hasCustomName() {
        return this.n != null && this.n.length() > 0;
    }

    public void a(String s) {
        this.n = s;
    }

    public int getMaxStackSize() {
        return maxStack; // CraftBukkit
    }

    public boolean a(EntityHuman entityhuman) {
        return this.world.getTileEntity(this.position) == this && entityhuman.e((double) this.position.getX() + 0.5D, (double) this.position.getY() + 0.5D, (double) this.position.getZ() + 0.5D) <= 64.0D;
    }

    public void startOpen(EntityHuman entityhuman) {}

    public void closeContainer(EntityHuman entityhuman) {}

    public boolean b(int i, ItemStack itemstack) {
        return itemstack.getItem() == Items.EMERALD || itemstack.getItem() == Items.DIAMOND || itemstack.getItem() == Items.GOLD_INGOT || itemstack.getItem() == Items.IRON_INGOT;
    }

    public String getContainerName() {
        return "minecraft:beacon";
    }

    public Container createContainer(PlayerInventory playerinventory, EntityHuman entityhuman) {
        return new ContainerBeacon(playerinventory, this);
    }

    public int getProperty(int i) {
        switch (i) {
        case 0:
            return this.j;

        case 1:
            return this.k;

        case 2:
            return this.l;

        default:
            return 0;
        }
    }

    public void b(int i, int j) {
        switch (i) {
        case 0:
            this.j = j;
            break;

        case 1:
            this.k = this.h(j);
            break;

        case 2:
            this.l = this.h(j);
        }

    }

    public int g() {
        return 3;
    }

    public void l() {
        this.inventorySlot = null;
    }

    public boolean c(int i, int j) {
        if (i == 1) {
            this.m();
            return true;
        } else {
            return super.c(i, j);
        }
    }

    public static class BeaconColorTracker {

        private final float[] a;
        public BeaconColorTracker(float[] afloat) {
            this.a = afloat;
        }

        protected void a() {
        }

        public float[] b() {
            return this.a;
        }
    }
}