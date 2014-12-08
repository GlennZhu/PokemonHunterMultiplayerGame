package zz23_jj26.server.Util;

import gov.nasa.worldwind.render.Renderable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import provided.mixedData.MixedDataKey;
import zz23_jj26.server.cmd.IExtendedCmdAdapter;

public class Util {
	public static Map<UUID, Renderable> idToImage = new ConcurrentHashMap<>();
	public static MixedDataKey<IExtendedCmdAdapter> exdCmdAdptKey = new MixedDataKey<IExtendedCmdAdapter>(UUID.randomUUID(), "exdCmdAdptKey Desp", IExtendedCmdAdapter.class);
}
