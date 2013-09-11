package org.trendafilov.odesk.notifier.parser;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.jxpath.xml.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XPathEngine {
	public static List<Map<String, String>> query(String document, Map<String, String> rules) {
		try {
			Document doc = (Document) new DOMParser().parseXML(new ByteArrayInputStream(document.toString().getBytes("UTF-8")));
			XPath xpath = XPathFactory.newInstance().newXPath();
			Map<String, List<String>> results = new HashMap<>();
			for (Entry<String, String> rule : rules.entrySet()) {
				NodeList nodes = (NodeList) xpath.compile(rule.getValue()).evaluate(doc, XPathConstants.NODESET);
				for (int i = 0; i < nodes.getLength(); i++) {
					if (!results.containsKey(rule.getKey()))
						results.put(rule.getKey(), new ArrayList<String>());
					results.get(rule.getKey()).add(nodes.item(i).getNodeValue());
				}
			}
			List<Map<String, String>> output = new ArrayList<>();
			for (int i = 0; i < results.values().iterator().next().size(); i++) {
				Map<String, String> elements = new HashMap<>();
				for (Entry<String, List<String>> entry : results.entrySet())
					elements.put(entry.getKey(), entry.getValue().get(i));
				output.add(elements);
			}
			return output;
		} catch (XPathExpressionException | UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
