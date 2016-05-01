package com.pj.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Service;

import com.pj.model.AnalysData;

import utils.API;

@Service
public class XMLService {
	public boolean initXml() {
		Document doc = new DocumentFactory().createDocument();
		Element elm = doc.addElement("root");
		try {
			out(doc, API.XMLCalculatePath);
			out(doc, API.XMLDeptPath);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public boolean out(Document doc, String path) throws IOException {
		FileWriter write = new FileWriter(path);
		
		OutputFormat formt = OutputFormat.createPrettyPrint();
		formt.setEncoding("UTF-8");
		XMLWriter out = new XMLWriter(write, formt);
		out.write(doc);
		out.close();
		return true;
	}

	public Document in(String path) throws Exception {
		InputStream is = new FileInputStream(new File(path));
		SAXReader reader = new SAXReader();
		Document doc = reader.read(new InputStreamReader(is, "UTF-8"));
		is.close();
		return doc;
	}

	public boolean writeOruodateCalculate(String analysiticType, String model,
			String data, String count) {
		Document doc;
		try {
			doc = in(API.XMLCalculatePath);
			List<Element> list = doc.selectNodes("//root/analysistic");
			for (Element elem : list) {
				String type = elem.valueOf("@type");
				if (type.equals(analysiticType)) {
					List<Element> temp = elem.selectNodes("model");
					for (Element e_temp : temp) {
						String model_type = e_temp.valueOf("@type");
						if (model_type.equals(model)) {
							// 有数据进行更新
							e_temp.element("count").setText(count);
							e_temp.element("data").setText(data);
							out(doc, API.XMLCalculatePath);
							return true;
						}
					}
					{
						// 没数据，更新父节点
						Element e_type = elem.addElement("model");
						e_type.addAttribute("type", model);
						e_type.addElement("count").addText(count);
						e_type.addElement("data").addText(data);

						out(doc, API.XMLCalculatePath);
						return true;
					}
				}
			}
			// 创建父节点
			Element elem = doc.getRootElement().addElement("analysistic")
					.addAttribute("type", analysiticType);
			Element e_type = elem.addElement("model");
			e_type.addAttribute("type", model);
			e_type.addElement("count").addText(count);
			e_type.addElement("data").addText(data);
			out(doc, API.XMLCalculatePath);
			return true;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public AnalysData readCalculate(String analysiticType, String model) {

		try {
			Document doc = in(API.XMLCalculatePath);
			AnalysData analysData = new AnalysData();
			List<Element> list = doc.selectNodes("//root/analysistic");
			for (Element elem : list) {
				String type = elem.valueOf("@type");
				if (type.equals(analysiticType)) {
					List<Element> temp = elem.selectNodes("model");
					for (Element e_temp : temp) {
						String model_type = e_temp.valueOf("@type");
						if (model_type.equals(model)) {

							analysData.setCount(e_temp.element("count")
									.getText());
							analysData
									.setData(e_temp.element("data").getText());

						}
					}
				}
			}
			return analysData;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public boolean writeOrUpdateDept(String dept, String time) {
		try {
			Document doc = in(API.XMLDeptPath);
			List<Element> list = doc.selectNodes("//root/dept");
			for (Element elem : list) {
				String type = elem.valueOf("@type");
				if (type.equals(dept)) {
					elem.setText(time);
					out(doc, API.XMLDeptPath);
					return true;
				}
			}

			// 创建父节点
			Element elem = doc.getRootElement().addElement("dept")
					.addAttribute("type", dept);
			elem.setText(time);
			out(doc, API.XMLDeptPath);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}
	
	public String readDept(String dept){
		Document doc;
		try {
			doc = in(API.XMLDeptPath);
			List<Element> list = doc.selectNodes("//root/dept");
			for (Element elem : list) {
				String type = elem.valueOf("@type");
				if (type.equals(dept)) {
					
					return elem.getText().toString();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		return "";
	}
}
