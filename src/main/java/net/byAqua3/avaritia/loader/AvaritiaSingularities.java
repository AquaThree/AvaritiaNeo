package net.byAqua3.avaritia.loader;

import java.util.ArrayList;
import java.util.List;

import net.byAqua3.avaritia.singularity.Singularity;

public class AvaritiaSingularities {
	
	private static final AvaritiaSingularities INSTANCE = new AvaritiaSingularities();

	private final List<Singularity> singularities = new ArrayList<>();
	
    public static AvaritiaSingularities getInstance() {
		return INSTANCE;
	}
	
	public List<Singularity> getSingularities() {
		return this.singularities;
	}
	
	public Singularity getSingularity(String id) {
		for (Singularity singularity : this.singularities) {
			if(singularity.getId().equalsIgnoreCase(id)) {
				return singularity;
			}
		}
		return null;
	}}
