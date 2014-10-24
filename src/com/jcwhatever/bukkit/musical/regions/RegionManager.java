package com.jcwhatever.bukkit.musical.regions;

import com.jcwhatever.bukkit.generic.sounds.ResourceSound;
import com.jcwhatever.bukkit.generic.storage.BatchOperation;
import com.jcwhatever.bukkit.generic.storage.IDataNode;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RegionManager {

	private IDataNode _settings;
	
	private Map<String, MusicRegion> _regions = new HashMap<String, MusicRegion>();
	
	public RegionManager(IDataNode regionSettings) {
		_settings = regionSettings;
		loadRegions();
	}
	
	public MusicRegion getRegion(String regionName) {
		if (regionName == null)
			return null;
		
		return _regions.get(regionName.toLowerCase());
	}
	
	public List<MusicRegion> getRegions() {
		return new ArrayList<MusicRegion>(_regions.values());
	}
	
	public MusicRegion create(String regionName, final ResourceSound sound, final Location p1, final Location p2) {
		
		final MusicRegion region = new MusicRegion(regionName, _settings.getNode(regionName));
		
		_settings.runBatchOperation(new BatchOperation() {

			@Override
			public void run(IDataNode config) {
				region.setCoords(p1, p2);
				region.setSound(sound);
			}
		});
		
		_regions.put(region.getSearchName(), region);
		
		return region;
	}
	
	public boolean delete(String regionName) {
		if (regionName == null)
			return false;
		
		MusicRegion region = _regions.remove(regionName.toLowerCase());
		if (region == null)
			return false;
		
		
		_settings.set(region.getName(), null);
		region.dispose();
		
		return true;
	}
	
	private void loadRegions() {
		
		Set<String> regionNames = _settings.getSubNodeNames();
		if (regionNames == null)
			return;
		
		for (String regionName : regionNames) {
			
			MusicRegion region = new MusicRegion(regionName, _settings.getNode(regionName));
			
			_regions.put(region.getSearchName(), region);
		}
	}
	
}
