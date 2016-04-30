package com.pj.service;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import utils.AnalyticType;

public class XMLService {
	public boolean init(String path) throws IOException {
		Document doc = new DocumentFactory().createDocument();
		Element elm = doc.addElement("analysitic");
		FileWriter write = new FileWriter(path);
		OutputFormat formt = OutputFormat.createPrettyPrint();
		formt.setEncoding("UTF-8");
		XMLWriter out = new XMLWriter(write, formt);
		out.write(doc);
		out.close();
		return true;
	}

	public boolean write(String analysiticType, String model, String data,
			String time, String id) {

		return true;
	}

	public boolean update() {
		return true;
	}

	public boolean read() {
		return true;
	}
}
