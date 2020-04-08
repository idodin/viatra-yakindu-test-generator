package mapper.transform;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.gmf.runtime.notation.Bounds;
import org.eclipse.gmf.runtime.notation.Compartment;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.RelativeBendpoints;
import org.eclipse.gmf.runtime.notation.Shape;

import ca.mcgill.ecse.dp46.sgraph.Choice;
import ca.mcgill.ecse.dp46.sgraph.Entry;
import ca.mcgill.ecse.dp46.sgraph.Exit;
import ca.mcgill.ecse.dp46.sgraph.FinalState;
import ca.mcgill.ecse.dp46.sgraph.Region;
import ca.mcgill.ecse.dp46.sgraph.State;
import ca.mcgill.ecse.dp46.sgraph.Statechart;
import ca.mcgill.ecse.dp46.sgraph.Synchronization;
import ca.mcgill.ecse.dp46.sgraph.Transition;
import ca.mcgill.ecse.dp46.sgraph.Vertex;
import ca.mcgill.ecse.dp46.sgraph.YakindunamedPackage;
import mapper.transform.types.GMFType;
import mapper.transform.types.GMFTypeStringFactory;

public class Yakindu2GMFMapper {

	private static final String MEASUREMENT_UNIT = "Pixel";
	private static final int DEFAULT_X = 1;
	private static final int DEFAULT_Y = 1;
	private static final int DEFAULT_HEIGHT = 400;
	private static final int DEFAULT_WIDTH = 400;
	private static final String DEFAULT_POINTS = "[0, 0, 0, 0]$[0, 0, 0, 0]";

	private static NotationFactory nfactory;
	private static Map<Vertex, Node> vertexToNode = new HashMap<>();
	private static Set<Transition> transitions = new HashSet<>();
	
	/**
	 * Generate an concrete representation instance of {@link org.eclipse.gmf.runtime.notation.Diagram} from the specified
	 * {@link hu.bme.mit.inf.dslreasoner.domains.yakindu.sgraph.yakindumm.Statechart} instance
	 * 
	 * @param root the '<em>Statechart</em>' instance to generate a concrete representation off of.
	 * @return the '<em>Diagram</em>' concrete representation instance
	 */
	public static Diagram generateConcrete(Statechart root) {

		// Initialize Model
		NotationPackage.eINSTANCE.eClass();
		YakindunamedPackage.eINSTANCE.eClass();

		nfactory = NotationFactory.eINSTANCE;

		vertexToNode.clear();
		transitions.clear();
		
		// Create new Diagram Instance
		Diagram diagram = createDiagramFromStatechart(root);

		return diagram;

	}

	/**
	 * Generate a {@link org.eclipse.gmf.runtime.notation.Diagram} concrete representation instance from the input 
	 * {@link hu.bme.mit.inf.dslreasoner.domains.yakindu.sgraph.yakindumm.Statechart} instance.
	 * @param the '<em>Statechart</em>' instance to generate a concrete edge out of.
	 * @return the '<em>Diagram</em>' concrete representation instance generated.
	 */
	private static Diagram createDiagramFromStatechart(Statechart root) {

		// Initialize Concrete Root
		Diagram diagram = nfactory.createDiagram();
		diagram.setType(GMFTypeStringFactory.forType(GMFType.ROOT));
		diagram.setElement(root);
		diagram.setMeasurementUnit(MEASUREMENT_UNIT);
		
		// Create Nodes for Regions and attach to Diagram
		List<Node> regionNodes = root.getRegions().stream().map(x -> createNodeForRegion(x))
				.collect(Collectors.toList());
		diagram.getChildren().addAll(regionNodes);
		
		// Create Edges for Transitions and attach to Diagram
		List<Edge> transitionEdges = transitions.stream().map(x -> createEdgeFromTransition(x))
				.collect(Collectors.toList());
		diagram.getEdges().addAll(transitionEdges);
		
		return diagram;
	}

	/**
	 * Generate a {@link org.eclipse.gmf.runtime.notation.Node} concrete representation instance from the input 
	 * {@link hu.bme.mit.inf.dslreasoner.domains.yakindu.sgraph.yakindumm.Region} instance.
	 * @param the '<em>Region</em>' instance to generate a concrete edge out of.
	 * @return the '<em>Node</em>' concrete representation instance generated.
	 */
	private static Node createNodeForRegion(Region r) {

		// Create Node for Concrete Region
		Node concreteRegion = nfactory.createNode();
		concreteRegion.setType(GMFTypeStringFactory.forType(GMFType.REGION));
		concreteRegion.setElement(r);

		// Set Concrete Bounds for the Region
		Bounds regionConstraint = nfactory.createBounds();
		regionConstraint.setX(DEFAULT_X);
		regionConstraint.setY(DEFAULT_Y);
		regionConstraint.setHeight(DEFAULT_HEIGHT);
		regionConstraint.setWidth(DEFAULT_WIDTH);
		concreteRegion.setLayoutConstraint(regionConstraint);

		concreteRegion.getChildren().add(createCompartmenForRegion(r));

		return concreteRegion;
	}

	/**
	 * Generate a {@link org.eclipse.gmf.runtime.notation.Shape} concrete representation instance from the input 
	 * {@link hu.bme.mit.inf.dslreasoner.domains.yakindu.sgraph.yakindumm.Transition} instance, to represent the Region Compartment.
	 * @param the '<em>Region</em>' instance to generate a concrete edge out of.
	 * @return the '<em>Shape</em>' concrete representation instance generated.
	 */
	private static Shape createCompartmenForRegion(Region r) {
		
		// Create Shape for Region Compartment
		Shape regionCompart = nfactory.createShape();
		regionCompart.setType(GMFTypeStringFactory.forType(GMFType.REGION_COMPARTMENT));

		// Create Nodes for Vertices
		List<Node> vertexNodes = r.getVertices().stream().map(x -> createNodeForVertex(x)).collect(Collectors.toList());

		regionCompart.getChildren().addAll(vertexNodes);

		return regionCompart;
	}

	/**
	 * Generate a {@link org.eclipse.gmf.runtime.notation.Node} concrete representation instance from the input 
	 * {@link hu.bme.mit.inf.dslreasoner.domains.yakindu.sgraph.yakindumm.Vertex} instance.
	 * @param the '<em>Vertex</em>' instance to generate a concrete edge out of.
	 * @return the '<em>Node</em>' concrete representation instance generated.
	 */
	private static Node createNodeForVertex(Vertex v) {

		Node vertexNode = nfactory.createNode();
		
		// Set GMF Types depending on State Types
		if (v instanceof Entry) {
			vertexNode.setType(GMFTypeStringFactory.forType(GMFType.ENTRY));
		} else if (v instanceof FinalState) {
			vertexNode.setType(GMFTypeStringFactory.forType(GMFType.FINAL_STATE));
		} else if(v instanceof Exit) {
			vertexNode.setType(GMFTypeStringFactory.forType(GMFType.EXIT));
		} else if(v instanceof Synchronization) {
			vertexNode.setType(GMFTypeStringFactory.forType(GMFType.SYNCHRONIZATION));
		} else if(v instanceof Choice) {
			vertexNode.setType(GMFTypeStringFactory.forType(GMFType.CHOICE));
		} else { 
			vertexNode.setType(GMFTypeStringFactory.forType(GMFType.STATE));
			Compartment stateTextCompart = nfactory.createCompartment();

			stateTextCompart.setType(GMFTypeStringFactory.forType(GMFType.STATE_TEXT_COMPARTMENT));
			vertexNode.getChildren().add(stateTextCompart);
			
			// Create Nodes for Regions and attach to Diagram
			List<Node> regionNodes = ((State) v).getRegions().stream().map(x -> createNodeForRegion(x))
					.collect(Collectors.toList());
			vertexNode.getChildren().addAll(regionNodes);
		}
		
		// Set Layout Constraint (Coordinates)
		Bounds layoutConstraint = nfactory.createBounds();
		layoutConstraint.setX(DEFAULT_X);
		layoutConstraint.setY(DEFAULT_Y);

		vertexNode.setLayoutConstraint(layoutConstraint);

		transitions.addAll(v.getIncomingTransitions());
		transitions.addAll(v.getOutgoingTransitions());
		vertexToNode.put(v, vertexNode);

		return vertexNode;
	}
	
	/**
	 * Generate an {@link org.eclipse.gmf.runtime.notation.Edge} concrete representation instance from the input 
	 * {@link hu.bme.mit.inf.dslreasoner.domains.yakindu.sgraph.yakindumm.Transition} instance.
	 * @param the '<em>Transition</em>' instance to generate a concrete edge out of.
	 * @return the '<em>Edge</em>' concrete representation instance generated.
	 */
	private static Edge createEdgeFromTransition(Transition t) {
		
		Edge e = nfactory.createEdge();
		e.setElement(t);
		e.setSource(vertexToNode.get(t.getSource()));
		e.setTarget(vertexToNode.get(t.getTarget()));

		RelativeBendpoints bp = nfactory.createRelativeBendpoints();
		bp.setPoints(DEFAULT_POINTS);

		e.setBendpoints(bp);

		return e;
	}

}
