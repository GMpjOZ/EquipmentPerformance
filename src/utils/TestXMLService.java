package utils;

import junit.framework.TestCase;

import com.pj.service.XMLService;

public class TestXMLService extends TestCase {

//	 public void testinit(){
//	 XMLService xs=new XMLService();
//	 xs.initXml();
//	 }
//	public void testwrite() {
//		XMLService xs = new XMLService();
////		xs.writeOruodate("availity", "type", "{'ccc':23.3}",
////				"2016-09-12 12:30;30", "123");
//		xs.writeOrUpdateDept("心脏科", "12");
//	}
	public void testread(){
		XMLService xs = new XMLService();
		
		System.out.println(xs.readDept("心脏科"));
//		System.out.println(xs.readCalculate("availi", "type").getData());
	}
	
}
