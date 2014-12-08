package zz23_jj26.server.cmd;

import gov.nasa.worldwind.render.Renderable;

import java.util.Map;
import java.util.UUID;

public interface IExtendedCmdAdapter {
	public Map<UUID, Renderable> getImageDictionary();
}
