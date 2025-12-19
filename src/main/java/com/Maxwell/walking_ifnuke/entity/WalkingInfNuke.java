package com.Maxwell.walking_ifnuke.entity;

import com.Maxwell.walking_ifnuke.ModEntity;
import com.buuz135.industrial.item.infinity.item.ItemInfinityNuke;
import com.buuz135.industrial.module.ModuleTool;
import com.buuz135.industrial.proxy.client.sound.TickeableSound;
import com.buuz135.industrial.utils.explosion.ExplosionTickHandler;
import com.buuz135.industrial.utils.explosion.ProcessExplosion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public class WalkingInfNuke extends PathfinderMob {

    private static final EntityDataAccessor<Integer> RADIUS = SynchedEntityData.defineId(WalkingInfNuke.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> EXPLODING = SynchedEntityData.defineId(WalkingInfNuke.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ARMED = SynchedEntityData.defineId(WalkingInfNuke.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> TICKS = SynchedEntityData.defineId(WalkingInfNuke.class, EntityDataSerializers.INT);

    @Nullable
    private LivingEntity placedBy;
    private ItemStack original;
    private boolean exploding = false;
    private boolean armed = false;
    private int radius = 1;
    private int ticksExploding = 1;
    private ProcessExplosion explosionHelper;
    @OnlyIn(Dist.CLIENT)
    private TickeableSound chargingSound;
    @OnlyIn(Dist.CLIENT)
    private TickeableSound explodingSound;

    public WalkingInfNuke(EntityType<? extends PathfinderMob> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
        this.original = new ItemStack(ModuleTool.INFINITY_NUKE.get());
        this.blocksBuilding = true;
        this.xpReward = 0;
    }

    public WalkingInfNuke(Level worldIn, LivingEntity owner, ItemStack original) {
        this(ModEntity.WALKING_INF_NUKE.get(), worldIn);
        this.placedBy = owner;
        this.original = original;
        this.radius = ItemInfinityNuke.getRadius(original);
        this.entityData.set(RADIUS, this.radius);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.ATTACK_DAMAGE, 1.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.5D, false));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    @Override
    public void thunderHit(ServerLevel level, LightningBolt lightning) {
        super.thunderHit(level, lightning);
        if (!this.isExploding()) {
            this.setArmed(true);
            this.setExploding(true);
            this.setHealth(this.getMaxHealth());
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            ServerLevel serverLevel = (ServerLevel) this.level();
            if (serverLevel.isThundering()) {
                if (serverLevel.canSeeSky(this.blockPosition().above())) {
                    if (this.random.nextFloat() < 0.005F) {
                        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(serverLevel);
                        if (lightning != null) {
                            lightning.moveTo(this.getX(), this.getY(), this.getZ());
                            serverLevel.addFreshEntity(lightning);
                        }
                    }
                }
            }
        }
        if (!this.level().isClientSide && this.isExploding()) {
            if (this.getTarget() == null || !this.getTarget().isAlive()) {
                Player nearestPlayer = this.level().getNearestPlayer(this, 64.0D);
                if (nearestPlayer != null) {
                    this.setTarget(nearestPlayer);
                }
            }
        }
        if (exploding) {
            if (level() instanceof ServerLevel && explosionHelper == null) {
                explosionHelper = new ProcessExplosion(this.blockPosition(), getRadius(), (ServerLevel) this.level(), 39, placedBy != null ? placedBy.getDisplayName().getString() : "");
                ExplosionTickHandler.processExplosionList.add(explosionHelper);
            }
            setTicksExploding(this.getTicksExploding() + 1);
            this.updateInWaterStateAndDoFluidPushing();
        }
        if (this.level().isClientSide && this.getEntityData().get(EXPLODING)) {
            if (this.level().isClientSide) {
                this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 1.1D, this.getZ(), 0.0D, 0.0D, 0.0D);
                this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, this.level().getBlockState(this.blockPosition().below())), this.getX() + this.level().getRandom().nextDouble() - 0.5, this.getY(), this.getZ() + this.level().getRandom().nextDouble() - 0.5, 0.0D, 0.0D, 0.0D);
            }
        }
        if (explosionHelper != null && explosionHelper.isDead) {
            this.remove(RemovalReason.KILLED);
            this.onClientRemoval();
        }
        if (level().isClientSide) {
            tickClient();
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isExploding()) {
            if (!source.isCreativePlayer() && !source.is(net.minecraft.tags.DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                return false;
            }
        } else {
            if (!this.level().isClientSide && this.random.nextFloat() < 0.05F) {
                this.setArmed(true);
                this.setExploding(true);
                ((ServerLevel) this.level()).sendParticles(
                        ParticleTypes.ANGRY_VILLAGER,
                        this.getX(),
                        this.getEyeY() + 0.5D,
                        this.getZ(),
                        3,
                        0.3D, 0.3D, 0.3D,
                        0.0D
                );
            }
        }
        return super.hurt(source, amount);
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHitIn) {
        super.dropCustomDeathLoot(source, looting, recentlyHitIn);
        if (!this.level().isClientSide) {
            this.spawnAtLocation(this.getOriginal());
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void tickClient() {
        if (chargingSound == null && this.getEntityData().get(EXPLODING)) {
            Minecraft.getInstance().getSoundManager().play(chargingSound = new TickeableSound(this.level(), this.blockPosition(), ModuleTool.NUKE_CHARGING.get(), getRadius(), 10, this.level().random));
        }
        if (chargingSound != null) {
            chargingSound.setDistance(getRadius());
            chargingSound.increase();
            if (!Minecraft.getInstance().getSoundManager().isActive(chargingSound) && explodingSound == null) {
                explodingSound = new TickeableSound(this.level(), this.blockPosition(), ModuleTool.NUKE_EXPLOSION.get(), getRadius(), 10, this.level().random);
                explodingSound.setPitch(1);
                Minecraft.getInstance().getSoundManager().play(explodingSound);
            }
        }
    }

    @Override
    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(RADIUS, 1);
        this.entityData.define(EXPLODING, false);
        this.entityData.define(ARMED, false);
        this.entityData.define(TICKS, 1);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("Original", this.getOriginal().serializeNBT());
        compound.putBoolean("Exploding", this.isExploding());
        compound.putBoolean("Armed", this.isArmed());
        compound.putInt("TicksExploding", this.getTicksExploding());
        compound.putInt("Radius", this.getRadius());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setArmed(compound.getBoolean("Armed"));
        this.setExploding(compound.getBoolean("Exploding"));
        if (compound.contains("Original")) {
            this.setOriginal(ItemStack.of(compound.getCompound("Original")));
        }
        this.setTicksExploding(compound.getInt("TicksExploding"));
        this.setRadius(compound.getInt("Radius"));
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (this.isExploding()) return InteractionResult.SUCCESS;
        if (player.level().isClientSide && hand == InteractionHand.MAIN_HAND && player.getItemInHand(hand).isEmpty()) {
            arm();
        }
        if (!player.level().isClientSide && hand == InteractionHand.MAIN_HAND) {
            ItemStack itemStack = player.getItemInHand(hand);
            if (itemStack.isEmpty()) {
                if (player.isShiftKeyDown()) {
                    ItemHandlerHelper.giveItemToPlayer(player, this.original);
                    this.remove(RemovalReason.KILLED);
                    this.onClientRemoval();
                    return InteractionResult.SUCCESS;
                } else {
                    this.setArmed(!isArmed());
                    return InteractionResult.SUCCESS;
                }
            }
            if (this.isArmed() && itemStack.getItem().equals(Items.FLINT_AND_STEEL)) {
                this.setExploding(true);
                itemStack.hurtAndBreak(1, player, (playerEntity) -> {
                    playerEntity.broadcastBreakEvent(hand);
                });
                return InteractionResult.SUCCESS;
            }
            if (this.isArmed() && itemStack.getItem().equals(Items.NETHER_STAR)) {
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
                WalkingInfNuke clone = new WalkingInfNuke(ModEntity.WALKING_INF_NUKE.get(), this.level());
                clone.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
                clone.setRadius(this.getRadius());
                clone.setOriginal(this.getOriginal().copy());
                clone.setArmed(true);
                this.level().addFreshEntity(clone);
                ((ServerLevel) this.level()).sendParticles(ParticleTypes.POOF, this.getX(), this.getY() + 1.0, this.getZ(), 10, 0.5, 0.5, 0.5, 0.0);
                return InteractionResult.SUCCESS;
            }
        }
        return super.mobInteract(player, hand);
    }

    @OnlyIn(Dist.CLIENT)
    private void arm() {
        Minecraft.getInstance().getSoundManager().play(new SimpleSoundInstance(ModuleTool.NUKE_ARMING.get(), SoundSource.BLOCKS, 1, 1, Minecraft.getInstance().level.random, this.blockPosition()));
    }

    @Override
    protected MovementEmission getMovementEmission() {
        return MovementEmission.EVENTS;
    }

    @Override
    public boolean isPickable() {
        return this.isAlive();
    }

    public ItemStack getOriginal() {
        return original;
    }

    public void setOriginal(ItemStack original) {
        this.original = original;
    }

    public boolean isExploding() {
        return exploding;
    }

    public void setExploding(boolean exploding) {
        this.exploding = exploding;
        this.getEntityData().set(EXPLODING, exploding);
    }

    public boolean isArmed() {
        return armed;
    }

    public void setArmed(boolean armed) {
        this.armed = armed;
        this.getEntityData().set(ARMED, armed);
    }

    public int getRadius() {
        return this.getEntityData().get(RADIUS);
    }

    public void setRadius(int radius) {
        this.radius = radius;
        this.getEntityData().set(RADIUS, radius);
    }

    public boolean isDataArmed() {
        return this.getEntityData().get(ARMED);
    }

    public boolean isDataExploding() {
        return this.getEntityData().get(EXPLODING);
    }

    public int getTicksExploding() {
        return ticksExploding;
    }

    public void setTicksExploding(int ticksExploding) {
        this.ticksExploding = ticksExploding;
        this.getEntityData().set(TICKS, ticksExploding);
    }

    public int getDataTicksExploding() {
        return this.getEntityData().get(TICKS);
    }
}