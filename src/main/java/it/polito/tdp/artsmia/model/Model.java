package it.polito.tdp.artsmia.model;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.ArtsmiaController;
import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	private Graph<Artista,DefaultWeightedEdge> grafo;
	private ArtsmiaDAO dao;
	
	public Model() {
		this.dao = new ArtsmiaDAO();
	}
	
	public List<String> getRoles(){
		return dao.getRoles();
	}
	
	public void creaGrafo(String role) {
		this.grafo = new SimpleWeightedGraph<Artista,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		List<Artista> vertici = dao.getArtisti(role);
		Graphs.addAllVertices(this.grafo, vertici);
		
		
	}
}
