package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {

	private SimpleWeightedGraph<Airport, DefaultWeightedEdge> grafo;
	private Map<Integer, Airport> idMap;
	private ExtFlightDelaysDAO dao;
	
	private Map<Airport, Airport> visita = new HashMap<>();
	
	public Model() {
		idMap = new HashMap<Integer, Airport>();
		dao = new ExtFlightDelaysDAO();
		this.dao.loadAllAirports(idMap);
	}
	
	public void creaGrafo(int x) {
		
		this.grafo = new SimpleWeightedGraph<Airport, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		// Aggiungo i vertici 
		for(Airport a : idMap.values()) {
			if(dao.getAirlinesNumber(a) > x) {
				// Inserisco l'aeroporto come vertice
				this.grafo.addVertex(a);
			}
			
			
			for(Rotta r : dao.getRotte(idMap)) {
				if(this.grafo.containsVertex(r.getAp()) && this.grafo.containsVertex(r.getAd())) {
					DefaultWeightedEdge e = this.grafo.getEdge(r.ad, r.ap);
					
					if(e == null) {
						Graphs.addEdgeWithVertices(this.grafo, r.getAp(), r.getAd(), r.getPeso());
					} else {
						double pesoVecchio = this.grafo.getEdgeWeight(e);
						double pesoNuovo = pesoVecchio + r.getPeso();
						this.grafo.setEdgeWeight(e, pesoNuovo);
					}	
				}
				
				
			}
		}
	}
	
	public int vertexNumber() {
		return this.grafo.vertexSet().size();
	}
	
	public int edgeNumber() {
		return this.grafo.edgeSet().size();
	}
	
	public Collection<Airport> getAreoporti(){
		return this.grafo.vertexSet();
	}
	
	// Visita dell'albero 
	public List<Airport> trovaPercorso (Airport ap, Airport ad) { 
		List<Airport> percorso = new ArrayList<>();
		
		BreadthFirstIterator<Airport, DefaultWeightedEdge> it = new BreadthFirstIterator<>(this.grafo, ap);
		
		// Aggiungo la radice del mio albero di visita
		visita.put(ap, null);
		
		it.addTraversalListener(new TraversalListener<Airport, DefaultWeightedEdge>() {

			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {}

			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {}
			
			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultWeightedEdge> e) {
				// Ci interessa implementare solo questo metodo
				Airport sorgente = grafo.getEdgeSource(e.getEdge());
				Airport destinazione = grafo.getEdgeTarget(e.getEdge());
				
				if(!visita.containsKey(destinazione) && visita.containsKey(sorgente)) {
					visita.put(destinazione, sorgente);
				} else if (!visita.containsKey(sorgente) && visita.containsKey(destinazione)) {
					visita.put(sorgente, destinazione);
				}
			}

			@Override
			public void vertexTraversed(VertexTraversalEvent<Airport> e) {}

			@Override
			public void vertexFinished(VertexTraversalEvent<Airport> e) {}
			
		});
		
		// Visitiamo il grafo
		while(it.hasNext()) {
			it.next();
		}
		
		if(!visita.containsKey(ap) || !visita.containsKey(ad)) {
			// I due aeroporti non sono collegati
			return null;
		}
		
		Airport step = ad;
		
		while(!step.equals(ap)) {
			percorso.add(step);
			step = visita.get(step);
		}
		
		percorso.add(ap);
		
		return percorso;
		
	}
	
}
