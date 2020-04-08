package mapper.util;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.NotationPackage;

public class NotationUtil {

	/**
	 * Load a Instance of the Notation Metamodel from the specified relative path.
	 * 
	 * @param path The path of the instance model to load from
	 * @return the List of EObjects in the Instance Model
	 */
	public static List<EObject> loadModel(String path) {

		// Initialize Model
		NotationPackage.eINSTANCE.eClass();

		// Register resource factory
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		reg.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());

		// Create resource set and deserialize
		ResourceSet resSet = new ResourceSetImpl();
		Resource resource = resSet.getResource(URI.createURI(path), true);

		return resource.getContents();
	}

	/**
	 * Save a Instance of the Notation Metamodel to the specified relative path
	 * 
	 * @param list of EObject instances to serialize
	 * @param path to serialize to
	 * 
	 * @throws IOException if unable to serialize to file
	 */
	public static void saveModel(List<EObject> instances, String path) throws IOException {

		// Initialize Model
		NotationPackage.eINSTANCE.eClass();

		// Register resource factory
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		reg.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());

		// Create resource set and serialize
		ResourceSet resSet = new ResourceSetImpl();
		Resource resource = resSet.createResource(URI.createURI(path));

		resource.getContents().addAll(instances);

		resource.save(Collections.EMPTY_MAP);

	}

	/**
	 * Save a Instance of the Notation Metamodel to the specified relative path
	 * 
	 * @param root Root Diagram instance to serialize
	 * @param path to serialize to
	 * 
	 * @throws IOException if unable to serialize to file
	 */
	public static void saveModel(Diagram root, String path) throws IOException {

		// Initialize Model
		NotationPackage.eINSTANCE.eClass();

		// Register resource factory
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		reg.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());

		// Create resource set and serialize
		ResourceSet resSet = new ResourceSetImpl();
		Resource resource = resSet.createResource(URI.createURI(path));

		resource.getContents().add(root);

		resource.save(Collections.EMPTY_MAP);

	}
}
