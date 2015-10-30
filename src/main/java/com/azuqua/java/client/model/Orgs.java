package com.azuqua.java.client.model;
import java.util.ArrayList;

/**
 * Object representing the JSON object returned when the user logs on via email/password.
 * The Orgs object can contain zero or more Org objects.
 * @author quyle
 *
 */
public class Orgs {
	private ArrayList<Org> orgs;
	public Orgs() {}
	
	public ArrayList<Org> getOrgs() { return orgs; }
	public void setOrgs(ArrayList<Org> orgs) { this.orgs = orgs; }
}
