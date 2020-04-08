package mapper.util;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.NotationPackage;

import ca.mcgill.ecse.dp46.sgraph.Statechart;
import hu.bme.mit.inf.dslreasoner.domains.yakindu.sgraph.yakindumm.YakindummPackage;

public class CombinedUtil {
	/**
	 * Saves instances of both the abstract (Yakindu Simplified) and concrete (GMF)
	 * models to the specified path
	 * 
	 * @param abstractRoot Statechart Abstract Root Element
	 * @param concreteRoot Diagram Concrete Root Element
	 * @param path         Path to serialize to
	 * @throws IOException if there is an error on serialization.
	 */
	public static void saveModel(Statechart abstractRoot, Diagram concreteRoot, String path) throws IOException {

		// Register resource factory
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;

		// Configure to use UUIDS
		reg.getExtensionToFactoryMap().put("sct", new XMIResourceFactoryImpl() {
			@Override
			public Resource createResource(URI uri) {
				return new XMIResourceImpl(uri) {
					@Override
					protected boolean useUUIDs() {
						return true;
					}

				};
			}
		});

		NotationPackage.eINSTANCE.eClass();
		YakindummPackage.eINSTANCE.eClass();

		ResourceSet resSet = new ResourceSetImpl();
		Resource resource = resSet.createResource(URI.createURI(path));

		resource.getContents().add(abstractRoot);
		resource.getContents().add(concreteRoot);

		resource.save(Collections.EMPTY_MAP);
	}
}
