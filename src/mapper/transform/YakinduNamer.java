package mapper.transform;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.eclipse.emf.ecore.EObject;

import ca.mcgill.ecse.dp46.sgraph.Region;
import ca.mcgill.ecse.dp46.sgraph.State;
import ca.mcgill.ecse.dp46.sgraph.Statechart;
import ca.mcgill.ecse.dp46.sgraph.Vertex;
import ca.mcgill.ecse.dp46.sgraph.YakindunamedPackage;
import hu.bme.mit.inf.dslreasoner.domains.yakindu.sgraph.yakindumm.YakindummPackage;
import hu.bme.mit.inf.dslreasoner.domains.yakindu.sgraph.yakindumm.impl.YakindummPackageImpl;

public class YakinduNamer {
	private static Stack<String> nameStack = new Stack<>();

	/**
	 * Nests Yakindu Simplified Metamodel elements into the Statechart root element
	 * 
	 * @param e List of EObjects (must be instance of the Simplified Yakindu Metamodel) to nest
	 * @return Statechart root element of the newly nested model
	 * 
	 * @throws IllegalArgumentException if the list of EObjects does not conform to the Simplified Yakindu Metamodel
	 * 									or if more than one Statechart root already exists.  
	 */
	public static Statechart nameElements(List<EObject> e) throws IllegalArgumentException {
		if (e.stream().anyMatch(x -> !(x.eClass().eContainer() instanceof YakindummPackageImpl))) {
			throw new IllegalArgumentException("Objects must conform to Yakindu Simplified Metamodel");
		}
		
		// Initialize Model
		YakindunamedPackage.eINSTANCE.eClass();
		YakindummPackage.eINSTANCE.eClass();
		
		// Populate Vertex Names
		populateVertexNames();
		
		// Statechart will be single element in model (given Initial Model provided)
		hu.bme.mit.inf.dslreasoner.domains.yakindu.sgraph.yakindumm.Statechart unnamedStatechart = 
				(hu.bme.mit.inf.dslreasoner.domains.yakindu.sgraph.yakindumm.Statechart) e.get(0);
		
		// Name Statechart
		Statechart statechart = (new Yakindu2Named()).toNamed(unnamedStatechart);
		
		return nameStatechart(statechart);
		
	}
	
	
	private static Statechart nameStatechart(Statechart sc) {
		sc.setName(nameStack.pop());
		for(Region r : sc.getRegions()) {
			nameRegion(r);
		}
		return sc;
	}
	
	private static Region nameRegion(Region r) {
		r.setName(nameStack.pop());
		r.getVertices().stream()
			.filter(x->x instanceof State)
			.forEach(x->nameState((State) x)); 
		return r;
	}
	
	private static Vertex nameState(State s) {
		s.setName(nameStack.pop());
		for(Region r : s.getRegions()) {
			nameRegion(r);
		}
		return s;
	}
	
	
	private static void populateVertexNames() {
		for(int i = 0; i<500; i++) nameStack.push(Integer.toString(i));
		
		Set<String> values = new HashSet<>(Arrays.asList("on", "off", "flickering", "dim", "red", "blue", "green", "pink", "purple", "slow", "fast",
				"happy", "sad", "angry", "ticking", "paused", "reversing", "forwarding", "playing", "producing", "loading",
				"initializing", "buffering", "complete", "spinning", "blocked", "resting", "playing", "studying", "dancing",
				"singing", "cooking", "cooling", "blending", "baking", "cutting", "packaging", "delivering", "delivered", "cooked",
				"loaded", "yellow", "white", "black", "mauve", "cyan", "high", "low", "medium", "preparing", "active", "inactive",
				"printing", "printed", "scanning", "scanned", "stapling", "stapled", "cooling", "heating", "coding", "working",
				"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
				"X", "Y", "Z"));
		for(String s : values) nameStack.push(s);
	}
}
