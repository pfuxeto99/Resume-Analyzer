package image.Classify;

import java.awt.image.BufferedImage;
import java.io.File;

import data.Structures.Graph;
import data.Structures.Node;
import data.Structures.RoadList;
import feature.Extraction.FeatureExtracter;
import ui.Design.UserInterface;

public class ClassificationTask implements Runnable {

	private File chosenFile;
	private UserInterface design;
	private Graph graph;
	private Classifyer knnClassifier;

	/**
	 * a constuctor for initialisation
	 * 
	 * @param image         - the uploaded image
	 * @param design        - the gui design class
	 * @param trainingGraph - The graph from the training data
	 * @param knnClassifier - instance of the class that deals with the
	 *                      classification
	 * 
	 */
	public ClassificationTask(File file, UserInterface design, Graph trainingGraph, Classifyer knnClassifier) {

		this.chosenFile = file;
		this.design = design;
		this.knnClassifier = knnClassifier;
		this.graph = trainingGraph;
	}

	/**
	 * 
	 */
	@Override
	public void run() {

		FeatureExtracter extractor = new FeatureExtracter();

		// Extract feature vector from uploaded CV (PDF)
		double[] queryFeatures = extractor.extractFeatures(chosenFile);

		
		// Perform KNN classification on extracted CV features
		String knnLabel = knnClassifier.classify(queryFeatures, 5);

		// Retrieve nearest CV nodes from feature space
		RoadList<Node> neighbours = knnClassifier.similarityCheck(queryFeatures, 5);

		// Create graph node representing the uploaded CV
		Node node = new Node("", queryFeatures, knnLabel);

		// Insert node into graph database
		graph.addNode(node);

		// Connect node to most similar CV nodes
		for (int i = 0; i < neighbours.size(); i++) {
			graph.addEdge(node, neighbours.get(i));
		}

		// Perform BFS over similarity graph
		int newIndex = graph.getNodeIndex(node);
		int[] result = bfs(newIndex);

		int clusterCount = result[0];

		String finalStatus;
		String recommendation;

		if (knnLabel.equalsIgnoreCase("invalid") && clusterCount >= 5) {
			finalStatus = "LOW QUALITY CV";
			recommendation = "CV is similar to lower-quality profiles.";
		} else {
			finalStatus = "HIGH QUALITY CV";
			recommendation = "CV matches strong profiles.";
		}

		design.setClassificationResult(
		        finalStatus,
		        recommendation,
		        knnLabel,
		        clusterCount
		);
		
		
	}

	/**
	 * Performs Breadth-First Search on the CV similarity graph starting from a node.
	 * Counts the number of connected nodes in the similarity cluster.
	 * 
	 * @param startIndex - starting node index in graph
	 * @return array containing cluster size statistics
	 */
	private int[] bfs(int startIndex) {

		boolean[] visited = new boolean[graph.getNumNodes()];

		RoadList<Integer> queue = new RoadList<>();
		queue.add(startIndex);

		int clusterCount = 0;

		while (queue.size() > 0) {

			int currentIndex = queue.get(0);
			queue.removeAt(0);

			if (visited[currentIndex])
				continue;

			visited[currentIndex] = true;

			Node current = graph.getNode(currentIndex);

			// skip root node
			if (currentIndex != startIndex) {
				clusterCount++;
			}

			RoadList<Node> neighbors = current.getNeighbors();

			for (int i = 0; i < neighbors.size(); i++) {

				int ni = graph.getNodeIndex(neighbors.get(i));

				if (!visited[ni]) {
					queue.add(ni);
				}
			}
		}

		return new int[] { clusterCount };
	}
}
