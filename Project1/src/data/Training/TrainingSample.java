package data.Training;

import java.io.File;

import data.Structures.Entry;
import data.Structures.Graph;
import data.Structures.Heap;
import data.Structures.Node;
import data.Structures.RoadList;
import feature.Extraction.FeatureExtracter;

public class TrainingSample {
	private Graph databaseGraph;

	public Graph getDatabaseGraph() {
		return databaseGraph;
	}

	public TrainingSample() {
		databaseGraph = new Graph();
	}

	/**
	 * Loads PDF files from a specified dataset directory.
	 * Each valid file path is stored in a list for training.
	 * 
	 * @param path directory containing dataset files
	 * @return list of PDF files
	 */
	public RoadList<File> loadReferenceDataSet(String path) {

		File folder = new File(path);
		RoadList<File> list = new RoadList<>();

		if (folder.exists() && folder.isDirectory()) {

			File[] files = folder.listFiles((dir, name) -> name.endsWith(".pdf"));

			if (files != null) {
				for (File file : files) {
					list.add(file);
				}
			}
		}

		return list;
	}

	/**
	 * Executes training on both valid and invalid CV datasets.
	 * Dataset files are processed into feature vectors and stored in a graph.
	 */
	public void train() {

		RoadList<File> invalid = loadReferenceDataSet("dataset/InvalidCv");
		trainOnDataset(invalid, "Invalid", 500);

		RoadList<File> valid = loadReferenceDataSet("dataset/ValidCv");
		trainOnDataset(valid, "Valid", 500);

		addEdges(databaseGraph, 5);
	}

	/**
	 * Processes dataset files into feature vectors and converts them into graph nodes.
	 * Each PDF is converted into a feature representation before insertion into the graph.
	 * 
	 * @param list dataset file list
	 * @param label classification label assigned to each node
	 * @param maxFiles maximum number of files to process
	 */
	private void trainOnDataset(RoadList<File> list, String label, int maxFiles) {

		int filesAdded = 0;

		for (int i = 0; i < list.size() && filesAdded < maxFiles; i++) {

			File file = list.get(i);

			// Extract feature representation from PDF document
			FeatureExtracter analyzer = new FeatureExtracter();
			double[] features = analyzer.extractFeatures(file);

			// Convert feature vector into graph node
			Node node = new Node(file.getAbsolutePath(), features, label);

			databaseGraph.addNode(node);

			filesAdded++;
		}
	}

	/**
	 * Builds k-nearest-neighbour relationships between nodes in the graph.
	 * Each node is connected to its closest feature-based neighbors.
	 * 
	 * @param g graph containing all training nodes
	 * @param k number of nearest neighbors to connect
	 */
	private void addEdges(Graph g, int k) {

		int n = g.getNodes().size();

		for (int i = 0; i < n; i++) {

			Node current = g.getNodes().get(i);

			Heap<Double, Node> heap = new Heap<>();

			// insert all other nodes into heap
			for (int j = 0; j < n; j++) {

				if (i == j)
					continue;

				Node other = g.getNodes().get(j);

				double d = distance(current.getFeatures(), other.getFeatures());

				heap.insert(d, other);
			}

			// extract k nearest and create edges
			for (int t = 0; t < k && heap.size() > 0; t++) {

				Entry<Double, Node> e = heap.removeMin();

				g.addEdge(current, e.getValue());
			}
		}
	}

	/**
	 * Computes Euclidean distance between two feature vectors.
	 * 
	 * @param a first feature vector
	 * @param b second feature vector
	 * @return calculated distance value
	 */
	private double distance(double[] a, double[] b) {

		double sum = 0;

		for (int i = 0; i < a.length; i++) {
			double diff = a[i] - b[i];
			sum += diff * diff;
		}

		return Math.sqrt(sum);
	}


}
