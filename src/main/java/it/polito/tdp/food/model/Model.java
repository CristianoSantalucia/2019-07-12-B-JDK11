package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model
{
	private FoodDao dao;
	private Map<Integer, Food> foods;
	private Graph<Food, DefaultWeightedEdge> grafo;

	public Model()
	{
		this.dao = new FoodDao();
	}

	public void creaGrafo(int amount)
	{
		this.foods = new HashMap<>();
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		this.dao.getArchi(this.foods, amount);
		// vertici
		Graphs.addAllVertices(this.grafo, this.foods.values());
		// archi
		for (Adiacenza a : this.dao.getAdiacenza(foods, amount))
		{
			if (a.getPeso() < 0) Graphs.addEdgeWithVertices(this.grafo, a.getF2(), a.getF1(), a.getPeso());
			else if (a.getPeso() > 0) Graphs.addEdgeWithVertices(this.grafo, a.getF1(), a.getF2(), a.getPeso());
		}
	}

	public int getNumVertici()
	{
		return this.grafo.vertexSet().size();
	}

	public int getNumArchi()
	{
		return this.grafo.edgeSet().size();
	}

	public Set<Food> getVertex()
	{
		return this.grafo.vertexSet();
	}

	public Map<Food, Double> getSuccessiviMin(Food source)
	{
		ArrayList<Food> successivi = new ArrayList<>(); 
		for (DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(source))
			successivi.add(Graphs.getOppositeVertex(this.grafo, e, source)); 
		successivi.sort((f1,f2)->Double.compare(source.getFats()-f1.getFats(), source.getFats()-f2.getFats()));
		Map<Food, Double> result = new HashMap<>();
		if (successivi.size() >= 5) 
			for (int i = 0; i < 5; i++)
				result.put(successivi.get(i), source.getFats()-successivi.get(i).getFats());
//				result.put(successivi.get(i), successivi.get(i).getFats());
		else 
			for (Food f : successivi)
				result.put(f, source.getFats()-f.getFats()); 
//				result.put(f, f.getFats()); 
		return result; 
	}
}
