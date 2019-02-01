package me.winterguardian.core.playerstats;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 *
 * Created by Alexander Winter on 2015-11-04.
 */
public interface UserDataLoader
{
	void init();
	MappedData load(UUID id);
	void save(UUID id, MappedData data);
	Set<UUID> listUsers();
	void merge(UserDataLoader loader);

	Map.Entry<UUID, MappedData> getFirstByValue(String path, Object value);
	Map<UUID, MappedData> getByValue(String path, Object value);
}
