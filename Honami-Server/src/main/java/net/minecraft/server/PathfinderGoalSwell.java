package net.minecraft.server;

public class PathfinderGoalSwell extends PathfinderGoal {

	EntityCreeper a;

	public PathfinderGoalSwell(EntityCreeper entitycreeper) {
		this.a = entitycreeper;
		this.a(1);
	}

	@Override
	public boolean a() {
		EntityLiving entityliving = this.a.getGoalTarget();

		return this.a.cm() > 0 || entityliving != null && this.a.h(entityliving) < 9.0D;
	}

	@Override
	public void c() {
		this.a.getNavigation().n();
		
	}

	@Override
	public void d() {
		
	}

	@Override
	public void e() {
		
		EntityLiving target = this.a.getGoalTarget();
		if (target == null) {
			this.a.a(-1);
		} else if (this.a.h(target) > 49.0D) {
			this.a.a(-1);
		} else if (!this.a.getEntitySenses().a(target)) {
			this.a.a(-1);
		} else {
			this.a.a(1);
		}
	}
}
