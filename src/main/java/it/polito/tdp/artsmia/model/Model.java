package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	private Graph<Artista,DefaultWeightedEdge> grafo;
	private ArtsmiaDAO dao;
	private Map<Integer,Artista> mapId;
	private List<Artista> best;
	
	public Model() {
		this.dao = new ArtsmiaDAO();
		this.mapId = new HashMap<>();
		this.best = new LinkedList<>();
	}
	
	public List<String> getRoles(){
		return dao.getRoles();
	}
	
	public String creaGrafo(String role) {
		this.grafo = new SimpleWeightedGraph<Artista,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		List<Artista> vertici = dao.getArtisti(role,this.mapId);
		Graphs.addAllVertices(this.grafo,vertici);
		
		List<Coppia> coppie = dao.getCoppie(role);
		for(Coppia c : coppie) {
			Graphs.addEdgeWithVertices(this.grafo, this.mapId.get(c.getA1()), this.mapId.get(c.getA2()), c.getPeso());
		}
		return this.grafo.vertexSet().size()+" "+this.grafo.edgeSet().size();
		
	}
	
	
	public List<Artista> calcolaPercorso(Artista a){
		
		List<Artista> parziale = new LinkedList<>();
		parziale.add(a);
		
		for(Artista aa : Graphs.neighborListOf(this.grafo, a)) {
			parziale.add(aa);
			ricorsione(parziale, this.grafo.getEdgeWeight(this.grafo.getEdge(a, aa)));
			parziale.remove(parziale.size()-1);
		}
		
		return best;
		
	}

	private void ricorsione(List<Artista> parziale, double peso) {
		if(possibili(parziale, peso).size()==0) {
			if(best.size()==0) {
				best = new LinkedList<>(parziale);
			}else {
				if(peso(parziale) > peso(best)) {
					best = new LinkedList<>(parziale);
				}
			}
			return;
		}
		
		for(Artista a : possibili(parziale, peso)) {
			parziale.add(a);
			ricorsione(parziale, peso);
			parziale.remove(parziale.size()-1);
		}
		
	}
	
	private List<Artista> possibili(List<Artista> parziale,double peso){
		Artista ultimo = parziale.get(parziale.size()-1);
		List<Artista> possibili = new LinkedList<>();
		
		for(DefaultWeightedEdge d : this.grafo.edgesOf(ultimo)) {
			if(this.grafo.getEdgeWeight(d)==peso && !parziale.contains(Graphs.getOppositeVertex(this.grafo, d, ultimo))) {
				possibili.add(Graphs.getOppositeVertex(this.grafo, d, ultimo));
			}
		}
		return possibili;
		
	}
	
	public int peso(List<Artista> parziale) {
		int peso = 0;
		
		for(int i=0; i<parziale.size()-1; i++) {
			peso += this.grafo.getEdgeWeight(this.grafo.getEdge(parziale.get(i), parziale.get(i+1)));
		}
		return peso;
	}
	
	public Artista getArt(int id) {
		return this.mapId.get(id);
	}
	
}
