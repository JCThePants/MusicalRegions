/* This file is part of MusicalRegions for Bukkit, licensed under the MIT License (MIT).
 *
 * Copyright (c) JCThePants (www.jcwhatever.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */


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
