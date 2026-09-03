package net.minecraft.server;

public class SecondaryWorldServer extends WorldServer {

	private WorldServer a;

	public SecondaryWorldServer(MinecraftServer minecraftserver, IDataManager idatamanager, int i,
			WorldServer worldserver, MethodProfiler methodprofiler, WorldData worldData,
			org.bukkit.World.Environment env, org.bukkit.generator.ChunkGenerator gen) {
		super(minecraftserver, idatamanager, worldData, i, methodprofiler, env, gen);
		
		this.a = worldserver;

	}

	@Override
	public World b() {
		this.worldMaps = this.a.T();
		
		String s = PersistentVillage.a(this.worldProvider);
		PersistentVillage persistentvillage = (PersistentVillage) this.worldMaps.get(PersistentVillage.class, s);

		if (persistentvillage == null) {
			this.villages = new PersistentVillage(this);
			this.worldMaps.a(s, this.villages);
		} else {
			this.villages = persistentvillage;
			this.villages.a(this);
		}

		return super.b(); 
	}
}
