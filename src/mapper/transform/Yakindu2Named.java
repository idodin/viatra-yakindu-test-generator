package mapper.transform;

import ca.mcgill.ecse.dp46.sgraph.Region;
import ca.mcgill.ecse.dp46.sgraph.State;
import ca.mcgill.ecse.dp46.sgraph.Statechart;
import ca.mcgill.ecse.dp46.sgraph.Vertex;
import ca.mcgill.ecse.dp46.sgraph.YakindunamedFactory;
import ca.mcgill.ecse.dp46.sgraph.YakindunamedPackage;
import hu.bme.mit.inf.dslreasoner.domains.yakindu.sgraph.yakindumm.YakindummPackage;

public class Yakindu2Named {
	
	private YakindunamedFactory yfactory = YakindunamedFactory.eINSTANCE;
	
	public Yakindu2Named() {
		// Initialize Model
		YakindunamedPackage.eINSTANCE.eClass();
		YakindummPackage.eINSTANCE.eClass();
	}
	
	public Statechart toNamed(hu.bme.mit.inf.dslreasoner.domains.yakindu.sgraph.yakindumm.Statechart sc) {
		Statechart namedStatechart = yfactory.createStatechart();
		for(hu.bme.mit.inf.dslreasoner.domains.yakindu.sgraph.yakindumm.Region r: sc.getRegions()) {
			namedStatechart.getRegions().add(toNamed(r));
		}
		return namedStatechart;
	}
	
	public Region toNamed(hu.bme.mit.inf.dslreasoner.domains.yakindu.sgraph.yakindumm.Region r) {
		Region namedRegion = yfactory.createRegion();
		for(hu.bme.mit.inf.dslreasoner.domains.yakindu.sgraph.yakindumm.Vertex v: r.getVertices()) {
			namedRegion.getVertices().add(toNamed(v));
		}
		return namedRegion;
	}
	
	public Vertex toNamed(hu.bme.mit.inf.dslreasoner.domains.yakindu.sgraph.yakindumm.Vertex v) {
		Vertex namedVertex;
		if(v instanceof hu.bme.mit.inf.dslreasoner.domains.yakindu.sgraph.yakindumm.State) {
			namedVertex = yfactory.createState();
			for(hu.bme.mit.inf.dslreasoner.domains.yakindu.sgraph.yakindumm.Region r: ((hu.bme.mit.inf.dslreasoner.domains.yakindu.sgraph.yakindumm.State) v).getRegions()) {
				((State) namedVertex).getRegions().add(toNamed(r));
			}
		} else if (v instanceof hu.bme.mit.inf.dslreasoner.domains.yakindu.sgraph.yakindumm.Entry) {
			namedVertex = yfactory.createEntry();
		} else if (v instanceof hu.bme.mit.inf.dslreasoner.domains.yakindu.sgraph.yakindumm.Exit) {
			namedVertex = yfactory.createExit();
		} else if (v instanceof hu.bme.mit.inf.dslreasoner.domains.yakindu.sgraph.yakindumm.Synchronization) {
			namedVertex = yfactory.createSynchronization();
		} else if (v instanceof hu.bme.mit.inf.dslreasoner.domains.yakindu.sgraph.yakindumm.Choice) {
			namedVertex = yfactory.createChoice();
		} else {
			namedVertex = yfactory.createFinalState();
		}
		
		return namedVertex;
	}
}
