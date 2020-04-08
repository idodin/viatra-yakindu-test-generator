package mapper.transform.types;

import java.util.HashMap;
import java.util.Map;

public class GMFTypeStringFactory {
	private static Map<GMFType, String> map = new HashMap<GMFType, String>() {{
		put(GMFType.ROOT, "Root");
		put(GMFType.ENTRY, "Entry");
		put(GMFType.REGION, "Region");
		put(GMFType.REGION_COMPARTMENT, "RegionCompartment");
		put(GMFType.STATE_TEXT_COMPARTMENT, "StateTextCompartment");
		put(GMFType.TRANSITION, "Transition");
		put(GMFType.ROOT, "org.yakindu.sct.ui.editor.editor.StatechartDiagramEditor");
		put(GMFType.FINAL_STATE, "FinalState");
		put(GMFType.EXIT, "Exit");
		put(GMFType.SYNCHRONIZATION, "Synchronization");
		put(GMFType.CHOICE, "Choice");
	}};
	
	public static String forType(GMFType t) {
		return map.get(t);
	}
}