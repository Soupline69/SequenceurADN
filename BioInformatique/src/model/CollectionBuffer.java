package model;

import java.util.List;

public class CollectionBuffer {
	private List<StringBuffer> fragments;
	
	public CollectionBuffer(List<StringBuffer> fragments) {
		this.fragments = fragments;
	}
	
	public List<StringBuffer> getFragments() {
		return fragments;
	}
	
	// Faire un alignement semi global entre toutes les paires de fragments
	
	// Imaginons que nous ayons f1, f2 faire l'aligment semi global entre
	
	// f1/f2, f1/f2ci, f1ci/f2, f1ci/f2ci
	
}