/**
 * Copyright (c) 2011 Jim Heyne. All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 which is 
 * available at {@link http://www.eclipse.org/legal/epl-v10.html}
 */

package com.jwh.gwt.fasttable.sample.client;

import java.util.ArrayList;

import com.google.gwt.user.client.Random;

/**
 * @author jheyne
 * A simple model object which can be randomly configured
 */
public class SampleModel {

	static final String[] cities = new String[] { "Fresno", "Chicago",
			"Fort Wayne", "Wilmington", "Fort Meyers" };

	static final String[] names = new String[] { "Jim", "Katie", "Linda",
			"Rebecca", "John" };
	static final String[] states = new String[] { "CA", "IA", "FL", "LA", "NY" };
	static final String[] streets = new String[] { "360 Green St", "15 Oak St",
			"645 Cherry St", "421 30th St", "8907 Valley Rd" };
	static final String[] zips = new String[] { "67887", "34523", "89643",
			"07885", "5655" };

	public static ArrayList<SampleModel> getSamples(int count) {
		final ArrayList<SampleModel> answer = new ArrayList<SampleModel>();
		for (int i = 0; i < count; i++) {
			answer.add(new SampleModel());
		}
		return answer;
	}

	public static ArrayList<SampleModel> getTestSamples(int count) {
		final ArrayList<SampleModel> answer = new ArrayList<SampleModel>();
		for (int i = 0; i < count; i++) {
			answer.add(new SampleModel("Sally", "13 Cricket", "Bellevue", "NE", "45544"));
		}
		return answer;
	}

	public String city;
	public String name;
	public String state;
	public String street;
	public String zip;

	public SampleModel() {
		super();
		this.name = randomSample(names);
		this.street = randomSample(streets);
		this.city = randomSample(cities);
		this.state = randomSample(states);
		this.zip = randomSample(zips);
	}
	

	public SampleModel(String name, String street, String city, String state,
			String zip) {
		super();
		this.city = city;
		this.name = name;
		this.state = state;
		this.street = street;
		this.zip = zip;
	}


	/**
	 * @param samples Options available
	 * @return A random selection of one of the options
	 */
	private String randomSample(String[] samples) {
		return samples[Random.nextInt(samples.length - 1)];
	}

	@Override
	public String toString() {
		final StringBuilder b = new StringBuilder(name);
		b.append('\t');
		b.append(street);
		b.append('\t');
		b.append(city);
		b.append('\t');
		b.append(state);
		b.append('\t');
		b.append(zip);
		return b.toString();
	}

}
