package org.trendafilov.odesk.notifier.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pmw.tinylog.Logger;
import org.trendafilov.odesk.notifier.model.FixedJob;
import org.trendafilov.odesk.notifier.model.HourlyJob;
import org.trendafilov.odesk.notifier.model.Job;

public class OdeskUtils {
	private final static String TITLE = "op_title";
	private final static String CIPHERTEXT = "ciphertext";
	private final static String JOB_TYPE = "job_type";
	private final static String DESCRIPTION = "op_description";
	private final static String HOURS_PER_WEEK = "hours_per_week";
	private final static String DURATION = "op_est_duration";
	private final static String AMOUNT = "amount";
	private final static String FIXED = "fixed";
	private final static String HOURLY = "hourly";
	
	public static Map<String, String> getInitialRules() {
    	Map<String, String> rules = new HashMap<>();
    	rules.put(CIPHERTEXT, "//*/" + CIPHERTEXT + "/text()");
    	rules.put(JOB_TYPE, "//*/" + JOB_TYPE + "/text()");
    	return rules;
	}
	
	public static Map<String, String> getFixedJobRules(String ciphertext) {
    	Map<String, String> rules = getBasicRules(ciphertext);
    	rules.putAll(createJobSelectors(ciphertext, AMOUNT));
    	return rules;
	}
	
	public static Map<String, String> getHourlyJobRules(String ciphertext) {
    	Map<String, String> rules = getBasicRules(ciphertext);
    	rules.putAll(createJobSelectors(ciphertext, HOURS_PER_WEEK, DURATION));
    	return rules;
	}
	
	private static Map<String, String> getBasicRules(String ciphertext) {
		return createJobSelectors(ciphertext, TITLE, DESCRIPTION);
	}
	
	private static Map<String, String> createJobSelectors(String ciphertext, String... selectors) {
		Map<String, String> rules = new HashMap<>();
		for (String selector : selectors) {
	    	rules.put(selector, "//*/job[contains(ciphertext, \"" + ciphertext +"\")]/" + selector + "/text()");
		}
		return rules;
	}
	
	public static Collection<Job> parseJobs(String xmlInput) {
		if (xmlInput.isEmpty()) {
			return Collections.emptyList();
		}
		List<Map<String, String>> cipherPairs = XPathEngine.query(xmlInput, OdeskUtils.getInitialRules());
		List<Job> jobs = new ArrayList<>();
		for (Map<String, String> pair : cipherPairs) {
			String ciphertext = pair.get(CIPHERTEXT);
			String jobType = pair.get(JOB_TYPE);
			if (jobType.equalsIgnoreCase(FIXED)) {
				Map<String, String> data = XPathEngine.query(xmlInput, OdeskUtils.getFixedJobRules(ciphertext)).iterator().next();
				Job newJob = new FixedJob(data.get(TITLE), data.get(DESCRIPTION), ciphertext, data.get(AMOUNT));
				jobs.add(newJob);
			}
			else if (jobType.equalsIgnoreCase(HOURLY)) {
				Map<String, String> data = XPathEngine.query(xmlInput, OdeskUtils.getHourlyJobRules(ciphertext)).iterator().next();
				Job newJob = new HourlyJob(data.get(TITLE), data.get(DESCRIPTION), ciphertext, data.get(DURATION), data.get(HOURS_PER_WEEK));
				jobs.add(newJob);
			}
		}
		Logger.info(String.format("Fetched [%s] entries", jobs.size()));
		return jobs;
	}
	
	public static String getHumanDuration(String value) {
		if (value == null)
			return "Unknown";
		
		switch (value.trim()) {
			case "52":
				return "Ongoing";
			case "26":
				return "Ongoing";
			case "13":
				return "3 to 6 months";
			case "4":
				return "1 to 3 months";
			case "1":
				return "Less than 1 month";
			case "0":
				return "Less than 1 week";
			default:
				return "Unknown";
		}
	}
}
