package tfc.verticala;

import net.minecraft.client.render.camera.ICamera;
import net.minecraft.core.HitResult;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.data.gamerule.GameRule;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.enums.EnumBlockSoundEffectType;
import net.minecraft.core.enums.LightLayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.sound.SoundType;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.util.phys.Vec3d;
import net.minecraft.core.world.*;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.provider.BiomeProvider;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.ChunkCoordinates;
import net.minecraft.core.world.chunk.provider.IChunkProvider;
import net.minecraft.core.world.pathfinder.Path;
import net.minecraft.core.world.save.LevelData;
import net.minecraft.core.world.save.LevelStorage;
import net.minecraft.core.world.saveddata.SavedData;
import net.minecraft.core.world.type.WorldType;
import net.minecraft.core.world.weather.Weather;

import java.util.Collection;
import java.util.List;

public class ClampedWorld extends World {
	World world;

	public ClampedWorld(LevelStorage saveHandler, String name, long seed, Dimension dimension, WorldType worldType) {
		super(saveHandler, name, seed, dimension, worldType);
	}

	public ClampedWorld(World world, Dimension dimension) {
		super(world, dimension);
		this.world = world;
	}

	public ClampedWorld(LevelStorage saveHandler, String name, Dimension dimension, WorldType worldType, long seed) {
		super(saveHandler, name, dimension, worldType, seed);
	}

	@Override
	public <T> T getGameRule(GameRule<T> gameRule) {
		return world.getGameRule(gameRule);
	}

	@Override
	public int getHeightBlocks() {
		return 256;
	}

	@Override
	public BiomeProvider getBiomeProvider() {
		return world.getBiomeProvider();
	}

	@Override
	protected IChunkProvider createChunkProvider() {
//		return world.createChunkProvider();
//		return super.createChunkProvider();
		return null;
	}

	@Override
	protected void getInitialSpawnLocation() {
//		world.getInitialSpawnLocation();
	}

	@Override
	public void getRespawnLocation() {
		world.getRespawnLocation();
	}

	@Override
	public int getTopBlock(int x, int z) {
		return world.getTopBlock(x, z);
	}

	@Override
	public void spawnPlayerWithLoadedChunks(EntityPlayer player) {
		world.spawnPlayerWithLoadedChunks(player);
	}

	@Override
	public void saveWorld(boolean saveImmediately, ProgressListener progressUpdate) {
		world.saveWorld(saveImmediately, progressUpdate);
	}

	@Override
	public boolean func_650_a(int i) {
		return world.func_650_a(i);
	}

	@Override
	public Weather getCurrentWeather() {
		return world.getCurrentWeather();
	}

	@Override
	public int getBlockId(int x, int y, int z) {
		return world.getBlockId(x, y, z);
	}

	@Override
	public Block getBlock(int x, int y, int z) {
		return world.getBlock(x, y, z);
	}

	@Override
	public double getBlockTemperature(int x, int z) {
		return world.getBlockTemperature(x, z);
	}

	@Override
	public double getBlockHumidity(int x, int z) {
		return world.getBlockHumidity(x, z);
	}

	@Override
	public double getBlockVariety(int x, int z) {
		return world.getBlockVariety(x, z);
	}

	@Override
	public Biome getBlockBiome(int x, int y, int z) {
		return world.getBlockBiome(x, y, z);
	}

	@Override
	public boolean getBlockLitInteriorSurface(int x, int y, int z) {
		return world.getBlockLitInteriorSurface(x, y, z);
	}

	@Override
	public boolean isAirBlock(int x, int y, int z) {
		return world.isAirBlock(x, y, z);
	}

	@Override
	public boolean isBlockLoaded(int x, int y, int z) {
		return world.isBlockLoaded(x, y, z);
	}

	@Override
	public boolean areBlocksLoaded(int x, int y, int z, int range) {
		return world.areBlocksLoaded(x, y, z, range);
	}

	@Override
	public boolean areBlocksLoaded(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		return world.areBlocksLoaded(minX, minY, minZ, maxX, maxY, maxZ);
	}

	@Override
	public boolean isChunkLoaded(int x, int z) {
		return world.isChunkLoaded(x, z);
	}

	@Override
	public Chunk getChunkFromBlockCoords(int x, int z) {
		return world.getChunkFromBlockCoords(x, z);
	}

	@Override
	public Chunk getChunkFromChunkCoords(int x, int z) {
		return world.getChunkFromChunkCoords(x, z);
	}

	@Override
	public boolean setBlockAndMetadata(int x, int y, int z, int id, int meta) {
		return world.setBlockAndMetadata(x, y, z, id, meta);
	}

	@Override
	public boolean setBlockRaw(int x, int y, int z, int id) {
		return world.setBlockRaw(x, y, z, id);
	}

	@Override
	public boolean setBlock(int x, int y, int z, int id) {
		return world.setBlock(x, y, z, id);
	}

	@Override
	public Material getBlockMaterial(int x, int y, int z) {
		return world.getBlockMaterial(x, y, z);
	}

	@Override
	public int getBlockMetadata(int x, int y, int z) {
		return world.getBlockMetadata(x, y, z);
	}

	@Override
	public void setBlockMetadataWithNotify(int x, int y, int z, int meta) {
		world.setBlockMetadataWithNotify(x, y, z, meta);
	}

	@Override
	public boolean setBlockMetadata(int x, int y, int z, int meta) {
		return world.setBlockMetadata(x, y, z, meta);
	}

	@Override
	public boolean setBlockRawWithNotify(int x, int y, int z, int id) {
		return world.setBlockRawWithNotify(x, y, z, id);
	}

	@Override
	public boolean setBlockWithNotify(int x, int y, int z, int id) {
		return world.setBlockWithNotify(x, y, z, id);
	}

	@Override
	public boolean setBlockAndMetadataWithNotify(int x, int y, int z, int id, int meta) {
		return world.setBlockAndMetadataWithNotify(x, y, z, id, meta);
	}

	@Override
	public void markBlockNeedsUpdate(int x, int y, int z) {
		world.markBlockNeedsUpdate(x, y, z);
	}

	@Override
	public void notifyBlockChange(int x, int y, int z, int id) {
		world.notifyBlockChange(x, y, z, id);
	}

	@Override
	public void markBlocksDirtyVertical(int x, int z, int y0, int y1) {
		world.markBlocksDirtyVertical(x, z, y0, y1);
	}

	@Override
	public void markBlockDirty(int x, int y, int z) {
		world.markBlockDirty(x, y, z);
	}

	@Override
	public void markBlocksDirty(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		world.markBlocksDirty(minX, minY, minZ, maxX, maxY, maxZ);
	}

	@Override
	public void notifyBlocksOfNeighborChange(int x, int y, int z, int blockId) {
		world.notifyBlocksOfNeighborChange(x, y, z, blockId);
	}

	@Override
	public boolean canBlockSeeTheSky(int x, int y, int z) {
		return world.canBlockSeeTheSky(x, y, z);
	}

	@Override
	public int getFullBlockLightValue(int x, int y, int z) {
		return world.getFullBlockLightValue(x, y, z);
	}

	@Override
	public int getBlockLightValue(int i, int j, int k) {
		return world.getBlockLightValue(i, j, k);
	}

	@Override
	public int getBlockLightValue_do(int i, int j, int k, boolean flag) {
		return world.getBlockLightValue_do(i, j, k, flag);
	}

	@Override
	public boolean canExistingBlockSeeTheSky(int i, int j, int k) {
		return world.canExistingBlockSeeTheSky(i, j, k);
	}

	@Override
	public int getHeightValue(int i, int j) {
		return world.getHeightValue(i, j);
	}

	@Override
	public void neighborLightPropagationChanged(LightLayer lightLayer, int x, int y, int z, int lightValue) {
		world.neighborLightPropagationChanged(lightLayer, x, y, z, lightValue);
	}

	@Override
	public int getSavedLightValue(LightLayer layer, int x, int y, int z) {
		return world.getSavedLightValue(layer, x, y, z);
	}

	@Override
	public void setLightValue(LightLayer layer, int x, int y, int z, int value) {
		world.setLightValue(layer, x, y, z, value);
	}

	@Override
	public float getBrightness(int x, int y, int z, int blockLightValue) {
		return world.getBrightness(x, y, z, blockLightValue);
	}

	@Override
	public float getLightBrightness(int x, int y, int z) {
		return world.getLightBrightness(x, y, z);
	}

	@Override
	public boolean isDaytime() {
		return world.isDaytime();
	}

	@Override
	public HitResult checkBlockCollisionBetweenPoints(Vec3d start, Vec3d end) {
		return world.checkBlockCollisionBetweenPoints(start, end);
	}

	@Override
	public HitResult checkBlockCollisionBetweenPoints(Vec3d start, Vec3d end, boolean shouldCollideWithFluids) {
		return world.checkBlockCollisionBetweenPoints(start, end, shouldCollideWithFluids);
	}

	@Override
	public HitResult checkBlockCollisionBetweenPoints(Vec3d start, Vec3d end, boolean shouldCollideWithFluids, boolean flag1) {
		return world.checkBlockCollisionBetweenPoints(start, end, shouldCollideWithFluids, flag1);
	}

	@Override
	public void playSoundAtEntity(Entity entity, String soundPath, float volume, float pitch) {
		world.playSoundAtEntity(entity, soundPath, volume, pitch);
	}

	@Override
	public void playSoundEffect(SoundType soundType, double x, double y, double z, String soundPath, float volume, float pitch) {
		world.playSoundEffect(soundType, x, y, z, soundPath, volume, pitch);
	}

	@Override
	public void playBlockSoundEffect(double x, double y, double z, Block block, EnumBlockSoundEffectType soundType) {
		world.playBlockSoundEffect(x, y, z, block, soundType);
	}

	@Override
	public void playRecord(String s, int i, int j, int k) {
		world.playRecord(s, i, j, k);
	}

	@Override
	public void spawnParticle(String particleKey, double x, double y, double z, double motionX, double motionY, double motionZ, double maxDistance) {
		world.spawnParticle(particleKey, x, y, z, motionX, motionY, motionZ, maxDistance);
	}

	@Override
	public void spawnParticle(String particleKey, double x, double y, double z, double motionX, double motionY, double motionZ) {
		world.spawnParticle(particleKey, x, y, z, motionX, motionY, motionZ);
	}

	@Override
	public boolean addWeatherEffect(Entity entity) {
		return world.addWeatherEffect(entity);
	}

	@Override
	public boolean entityJoinedWorld(Entity entity) {
		return world.entityJoinedWorld(entity);
	}

	@Override
	protected void obtainEntitySkin(Entity entity) {
//		world.obtainEntitySkin(entity);
	}

	@Override
	protected void releaseEntitySkin(Entity entity) {
//		world.releaseEntitySkin(entity);
	}

	@Override
	public void setEntityDead(Entity entity) {
		world.setEntityDead(entity);
	}

	@Override
	public void removePlayer(Entity entity) {
		world.removePlayer(entity);
	}

	@Override
	public void addListener(LevelListener iworldaccess) {
		world.addListener(iworldaccess);
	}

	@Override
	public void removeListener(LevelListener iworldaccess) {
		world.removeListener(iworldaccess);
	}

	@Override
	public List<AABB> getCubes(Entity entity, AABB axisalignedbb) {
		return world.getCubes(entity, axisalignedbb);
	}

	@Override
	public List<AABB> getCollidingSolidBlockBoundingBoxes(Entity entity, AABB axisalignedbb) {
		return world.getCollidingSolidBlockBoundingBoxes(entity, axisalignedbb);
	}

	@Override
	public Vec3d getSkyColor(ICamera camera, float partialTick) {
		return world.getSkyColor(camera, partialTick);
	}

	@Override
	public float getCelestialAngle(float partialTick) {
		return world.getCelestialAngle(partialTick);
	}

	@Override
	public Vec3d getDimensionColor(float partialTick) {
		return world.getDimensionColor(partialTick);
	}

	@Override
	public Vec3d getFogColor(float partialTick) {
		return world.getFogColor(partialTick);
	}

	@Override
	public int findTopSolidBlock(int x, int z) {
		return world.findTopSolidBlock(x, z);
	}

	@Override
	public int findTopSolidNonLiquidBlock(int i, int j) {
		return world.findTopSolidNonLiquidBlock(i, j);
	}

	@Override
	public float getStarBrightness(float partialTick) {
		return world.getStarBrightness(partialTick);
	}

	@Override
	public void scheduleBlockUpdate(int x, int y, int z, int id, int delay) {
		world.scheduleBlockUpdate(x, y, z, id, delay);
	}

	@Override
	public void updateEntities() {
		world.updateEntities();
	}

	@Override
	public void func_31054_a(Collection collection) {
		world.func_31054_a(collection);
	}

	@Override
	public void updateEntity(Entity entity) {
		world.updateEntity(entity);
	}

	@Override
	public void updateEntityWithOptionalForce(Entity entity, boolean flag) {
		world.updateEntityWithOptionalForce(entity, flag);
	}

	@Override
	public boolean checkIfAABBIsClear(AABB axisalignedbb) {
		return world.checkIfAABBIsClear(axisalignedbb);
	}

	@Override
	public boolean getIsAnySolidGround(AABB boundingBox) {
		return world.getIsAnySolidGround(boundingBox);
	}

	@Override
	public boolean getIsAnyLiquid(AABB boundingBox) {
		return world.getIsAnyLiquid(boundingBox);
	}

	@Override
	public boolean isBoundingBoxBurning(AABB axisalignedbb) {
		return world.isBoundingBoxBurning(axisalignedbb);
	}

	@Override
	public boolean handleMaterialAcceleration(AABB axisalignedbb, Material material, Entity entity) {
		return world.handleMaterialAcceleration(axisalignedbb, material, entity);
	}

	@Override
	public boolean isMaterialInBB(AABB axisalignedbb, Material material) {
		return world.isMaterialInBB(axisalignedbb, material);
	}

	@Override
	public boolean isAABBInMaterial(AABB axisalignedbb, Material material) {
		return world.isAABBInMaterial(axisalignedbb, material);
	}

	@Override
	public Explosion createExplosion(Entity entity, double d, double d1, double d2, float f) {
		return world.createExplosion(entity, d, d1, d2, f);
	}

	@Override
	public Explosion newExplosion(Entity entity, double d, double d1, double d2, float f, boolean flag, boolean isCannonBall) {
		return world.newExplosion(entity, d, d1, d2, f, flag, isCannonBall);
	}

	@Override
	public float func_675_a(Vec3d vec3d, AABB axisalignedbb) {
		return world.func_675_a(vec3d, axisalignedbb);
	}

	@Override
	public void onBlockHit(EntityPlayer entityplayer, int x, int y, int z, Side side) {
		world.onBlockHit(entityplayer, x, y, z, side);
	}

	@Override
	public Entity func_4085_a(Class class1) {
		return world.func_4085_a(class1);
	}

	@Override
	public String getNumLoadedEntitiesString() {
		return world.getNumLoadedEntitiesString();
	}

	@Override
	public String getChunkProviderInfoString() {
		return world.getChunkProviderInfoString();
	}

	@Override
	public TileEntity getBlockTileEntity(int x, int y, int z) {
		return world.getBlockTileEntity(x, y, z);
	}

	@Override
	public void setBlockTileEntity(int i, int j, int k, TileEntity tileentity) {
		world.setBlockTileEntity(i, j, k, tileentity);
	}

	@Override
	public void removeBlockTileEntity(int i, int j, int k) {
		world.removeBlockTileEntity(i, j, k);
	}

	@Override
	public boolean isBlockOpaqueCube(int x, int y, int z) {
		return world.isBlockOpaqueCube(x, y, z);
	}

	@Override
	public boolean canPlaceOnSurfaceOfBlock(int x, int y, int z) {
		return world.canPlaceOnSurfaceOfBlock(x, y, z);
	}

	@Override
	public boolean isBlockNormalCube(int x, int y, int z) {
		return world.isBlockNormalCube(x, y, z);
	}

	@Override
	public boolean canPlaceInsideBlock(int x, int y, int z) {
		return world.canPlaceInsideBlock(x, y, z);
	}

	@Override
	public void onUnload() {
		world.onUnload();
	}

	@Override
	public void saveWorldIndirectly(ProgressListener iprogressupdate) {
		world.saveWorldIndirectly(iprogressupdate);
	}

	@Override
	public boolean updatingLighting() {
		return world.updatingLighting();
	}

	@Override
	public void scheduleLightingUpdate(LightLayer layer, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		world.scheduleLightingUpdate(layer, minX, minY, minZ, maxX, maxY, maxZ);
	}

	@Override
	public void updateSkyBrightness() {
		if (world == null) return;
		world.updateSkyBrightness();
	}

	@Override
	public void setAllowedMobSpawns(boolean flag, boolean flag1) {
		world.setAllowedMobSpawns(flag, flag1);
	}

	@Override
	public void doLightingUpdate() {
		world.doLightingUpdate();
	}

	@Override
	public void doSeasonUpdate() {
		world.doSeasonUpdate();
	}

	@Override
	protected void updateSleepingPlayers() {
//		world.updateSleepingPlayers();
	}

	@Override
	public void tick() {
		world.tick();
	}

	@Override
	protected void updateBlocksAndPlayCaveSounds() {
//		world.updateBlocksAndPlayCaveSounds();
	}

	@Override
	public boolean tickUpdates(boolean all) {
		return world.tickUpdates(all);
	}

	@Override
	public void randomDisplayUpdates(int x, int y, int z) {
		world.randomDisplayUpdates(x, y, z);
	}

	@Override
	public List<Entity> getEntitiesWithinAABBExcludingEntity(Entity entity, AABB aabb) {
		return world.getEntitiesWithinAABBExcludingEntity(entity, aabb);
	}

	@Override
	public List<Entity> getEntitiesWithinAABB(Class class1, AABB aabb) {
		return world.getEntitiesWithinAABB(class1, aabb);
	}

	@Override
	public List<Entity> getLoadedEntityList() {
		return world.getLoadedEntityList();
	}

	@Override
	public void updateTileEntityChunkAndSendToPlayer(int x, int y, int z, TileEntity tileEntity) {
		world.updateTileEntityChunkAndSendToPlayer(x, y, z, tileEntity);
	}

	@Override
	public int countEntities(Class class1) {
		return world.countEntities(class1);
	}

	@Override
	public void addLoadedEntities(List list) {
		world.addLoadedEntities(list);
	}

	@Override
	public void unloadEntities(List list) {
		world.unloadEntities(list);
	}

	@Override
	public void dropOldChunks() {
		world.dropOldChunks();
	}

	@Override
	public boolean canBlockBePlacedAt(int blockId, int x, int y, int z, boolean flag, Side side) {
		return world.canBlockBePlacedAt(blockId, x, y, z, flag, side);
	}

	@Override
	public Path getPathToEntity(Entity entity, Entity entityToTravelTo, float distance) {
		return world.getPathToEntity(entity, entityToTravelTo, distance);
	}

	@Override
	public Path getEntityPathToXYZ(Entity entity, int i, int j, int k, float f) {
		return world.getEntityPathToXYZ(entity, i, j, k, f);
	}

	@Override
	public boolean isBlockProvidingPowerTo(int i, int j, int k, int l) {
		return world.isBlockProvidingPowerTo(i, j, k, l);
	}

	@Override
	public boolean isBlockGettingPowered(int i, int j, int k) {
		return world.isBlockGettingPowered(i, j, k);
	}

	@Override
	public boolean isBlockIndirectlyProvidingPowerTo(int i, int j, int k, int l) {
		return world.isBlockIndirectlyProvidingPowerTo(i, j, k, l);
	}

	@Override
	public boolean isBlockIndirectlyGettingPowered(int i, int j, int k) {
		return world.isBlockIndirectlyGettingPowered(i, j, k);
	}

	@Override
	public EntityPlayer getClosestPlayerToEntity(Entity entity, double radius) {
		return world.getClosestPlayerToEntity(entity, radius);
	}

	@Override
	public EntityPlayer getClosestPlayer(double x, double y, double z, double radius) {
		return world.getClosestPlayer(x, y, z, radius);
	}

	@Override
	public EntityPlayer getPlayerEntityByName(String s) {
		return world.getPlayerEntityByName(s);
	}

	@Override
	public void setChunkData(int x, int y, int z, int width, int height, int length, byte[] data) {
		world.setChunkData(x, y, z, width, height, length, data);
	}

	@Override
	public byte[] getChunkData(int x, int y, int z, int xSize, int ySize, int zSize) {
		return world.getChunkData(x, y, z, xSize, ySize, zSize);
	}

	@Override
	public void sendQuittingDisconnectingPacket() {
		world.sendQuittingDisconnectingPacket();
	}

	@Override
	public void checkSessionLock() {
		world.checkSessionLock();
	}

	@Override
	public void setWorldTime(long l) {
		world.setWorldTime(l);
	}

	@Override
	public void setWorldTimeUpdateTicks(long l) {
		world.setWorldTimeUpdateTicks(l);
	}

	@Override
	public long getRandomSeed() {
		if (world == null) return 0;
		return world.getRandomSeed();
	}

	@Override
	public long getWorldTime() {
		return world.getWorldTime();
	}

	@Override
	public ChunkCoordinates getSpawnPoint() {
		return world.getSpawnPoint();
	}

	@Override
	public void setSpawnPoint(ChunkCoordinates chunkcoordinates) {
		world.setSpawnPoint(chunkcoordinates);
	}

	@Override
	public void joinEntityInSurroundings(Entity entity) {
		world.joinEntityInSurroundings(entity);
	}

	@Override
	public boolean canMineBlock(EntityPlayer entityplayer, int i, int j, int k) {
		return world.canMineBlock(entityplayer, i, j, k);
	}

	@Override
	public void sendTrackedEntityStatusUpdatePacket(Entity entityId, byte entityStatus) {
		world.sendTrackedEntityStatusUpdatePacket(entityId, entityStatus);
	}

	@Override
	public void sendTrackedEntityStatusUpdatePacket(Entity entityId, byte entityStatus, float attackedAtYaw) {
		world.sendTrackedEntityStatusUpdatePacket(entityId, entityStatus, attackedAtYaw);
	}

	@Override
	public void sendTrackedEntityDataPacket(Entity entity) {
		world.sendTrackedEntityDataPacket(entity);
	}

	@Override
	public void updateEntityList() {
		world.updateEntityList();
	}

	@Override
	public IChunkProvider getChunkProvider() {
		return world.getChunkProvider();
	}

	@Override
	public void triggerEvent(int x, int y, int z, int index, int data) {
		world.triggerEvent(x, y, z, index, data);
	}

	@Override
	public LevelStorage getSaveHandler() {
		return world.getSaveHandler();
	}

	@Override
	public LevelData getLevelData() {
		return world.getLevelData();
	}

	@Override
	public void updateEnoughPlayersSleepingFlag(EntityPlayer player) {
		world.updateEnoughPlayersSleepingFlag(player);
	}

	@Override
	public int getPlayersRequiredToSkipNight() {
		return world.getPlayersRequiredToSkipNight();
	}

	@Override
	protected void wakeUpAllPlayers() {
//		world.wakeUpAllPlayers();
	}

	@Override
	public boolean areEnoughPlayersFullyAsleep() {
		return world.areEnoughPlayersFullyAsleep();
	}

	@Override
	public boolean canBlockBeRainedOn(int x, int y, int z) {
		return world.canBlockBeRainedOn(x, y, z);
	}

	@Override
	public void setSavedData(String s, SavedData mapdatabase) {
		world.setSavedData(s, mapdatabase);
	}

	@Override
	public SavedData getSavedData(Class class1, String s) {
		return world.getSavedData(class1, s);
	}

	@Override
	public int getUniqueDataId(String s) {
		return world.getUniqueDataId(s);
	}

	@Override
	public void playSoundEffect(int id, int x, int y, int z, int data) {
		world.playSoundEffect(id, x, y, z, data);
	}

	@Override
	public void playSoundEffectForPlayer(EntityPlayer entityplayer, int i, int j, int k, int l, int i1) {
		world.playSoundEffectForPlayer(entityplayer, i, j, k, l, i1);
	}

	@Override
	public WorldType getWorldType() {
		return world.getWorldType();
	}

	@Override
	public EntityItem dropItem(int x, int y, int z, ItemStack itemStack) {
		return world.dropItem(x, y, z, itemStack);
	}

	@Override
	public int hashCode() {
		return world.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return world.equals(obj);
	}

	@Override
	public String toString() {
		return world.toString();
	}
}
