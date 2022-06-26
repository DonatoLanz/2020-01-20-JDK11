package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Artista;
import it.polito.tdp.artsmia.model.Coppia;
import it.polito.tdp.artsmia.model.Exhibition;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Exhibition> listExhibitions() {
		
		String sql = "SELECT * from exhibitions";
		List<Exhibition> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Exhibition exObj = new Exhibition(res.getInt("exhibition_id"), res.getString("exhibition_department"), res.getString("exhibition_title"), 
						res.getInt("begin"), res.getInt("end"));
				
				result.add(exObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getRoles(){
		String sql = "SELECT DISTINCT(a.role) "
				+ "FROM authorship a "
				+ "ORDER BY a.role ";
		
		List<String> result = new LinkedList<>();
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				result.add(res.getString("role"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public List<Artista> getArtisti(String role,Map<Integer,Artista> map){
		String sql = "SELECT a.* "
				+ "FROM authorship au, artists a "
				+ "WHERE au.artist_id = a.artist_id AND au.role = ? "
				+ "GROUP BY a.artist_id,a.name ";
		List<Artista> result = new LinkedList<>();
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, role);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Artista a = new Artista(res.getInt("artist_id"), res.getString("name"));
				result.add(a);
				map.put(a.getId(), a);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public List<Coppia> getCoppie(String role){
		String sql = "SELECT a1.artist_id AS a1,a2.artist_id AS a2,COUNT(DISTINCT e1.exhibition_id) AS peso "
				+ "FROM exhibition_objects e1, exhibition_objects e2, authorship a1, authorship a2 "
				+ "WHERE e1.object_id = a1.object_id AND e2.object_id = a2.object_id AND e1.exhibition_id = e2.exhibition_id AND a1.artist_id > a2.artist_id AND a1.role = ? AND a2.role = ? "
				+ "GROUP BY a1.artist_id,a2.artist_id ";
		List<Coppia> result = new LinkedList<>();
		

		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, role);
			st.setString(2, role);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Coppia c = new Coppia(res.getInt("a1"), res.getInt("a2"), res.getInt("peso"));
				result.add(c);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		
		
		
		
		
	}
	
}
