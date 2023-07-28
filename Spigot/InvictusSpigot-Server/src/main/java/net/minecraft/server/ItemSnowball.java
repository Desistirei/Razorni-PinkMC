package net.minecraft.server;

public class ItemSnowball extends Item {

    public ItemSnowball() {
        this.maxStackSize = 16;
        this.a(CreativeModeTab.f);
    }

    public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman) {
        if (!entityhuman.abilities.canInstantlyBuild) {
            --itemstack.count;
        }

        world.makeSound(entityhuman, "random.bow", 0.5F, 0.4F / (ItemSnowball.g.nextFloat() * 0.4F + 0.8F));
        world.addEntity(new EntitySnowball(world, entityhuman));
        return itemstack;
    }
}