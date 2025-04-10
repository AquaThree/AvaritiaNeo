package net.byAqua3.avaritia.compat;

import net.byAqua3.avaritia.compat.projecte.AvaritiaEMC;
import net.byAqua3.avaritia.compat.rei.AvaritiaREI;

public enum AvaritiaCompat {

	EQUIVALENTEXCHANGE("equivalentexchange", AvaritiaEMC.class.getName()),
	ROUGHLYENOUGHITEMS("roughlyenoughitems", AvaritiaREI.class.getName());

	private final String modId;
	private final String className;

	private AvaritiaCompat(String modId, String className) {
		this.modId = modId;
		this.className = className;
	}

	public String getModId() {
		return this.modId;
	}

	@SuppressWarnings("deprecation")
	public ICompatInit getModInit() {
		try {
			Object instance = Class.forName(this.className).newInstance();
			if(instance instanceof ICompatInit) {
				return (ICompatInit) instance;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
