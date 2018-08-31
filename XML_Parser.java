import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MyParser {

	public static void main(String[] args)
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

		File input = new File("src/source_xform.xml");
		File output = new File("src/target_xform.xml");

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		Document source_doc = builder.parse(input);
		source_doc.getDocumentElement().normalize();

		Document target_doc = builder.parse(output);
		target_doc.getDocumentElement().normalize();

		XPath xPath = XPathFactory.newInstance().newXPath();

		String source_exp = "/xform/field-set/fields/field";
		String target_exp = "/XForm/Message/Fields/Field";

		NodeList source_list = (NodeList) xPath.compile(source_exp).evaluate(source_doc, XPathConstants.NODESET);
		NodeList target_list = (NodeList) xPath.compile(target_exp).evaluate(target_doc, XPathConstants.NODESET);
		
		
		ArrayList<CosmosBean> result = new ArrayList<CosmosBean>();

		for (int i = 0; i < target_list.getLength(); i++) {
			
			CosmosBean temp_bean = new CosmosBean();
			Node target_node = target_list.item(i);

			if (target_node.getNodeType() == Node.ELEMENT_NODE) {
				Element field_Element = (Element) target_node;

				String field_name ;
				String from = "";
				String to = "";

				field_name = field_Element.getAttribute("Name");

				try {
					Element from_Element = (Element) field_Element.getElementsByTagName("From").item(0);
					from = from_Element.getAttribute("XPath");
				} catch (Exception e) {
					
				}

				try {
					Element to_Element = (Element) field_Element.getElementsByTagName("To").item(0);
					to = to_Element.getAttribute("Startindex");
				} catch (Exception e) {
					
				}
				
				temp_bean.setField_name(field_name);
				temp_bean.setTo(to);
				
				
				if(from != "") {
					int j = 0;
					boolean flag = true;
					while(j < source_list.getLength() && flag) {
						
						Node search_node = source_list.item(j);
						
						if (search_node.getNodeType() == Node.ELEMENT_NODE) {
							
							Element search_Element = (Element) search_node;
							
							Element source_Element = (Element) search_Element.getElementsByTagName("source").item(0);
							String source = source_Element.getAttribute("xpath");
							
							Element target_Element = (Element) search_Element.getElementsByTagName("target").item(0);
							String target = target_Element.getAttribute("xpath");
							
							
							if(from.equals(target)) {
								
								String mid = from;
								temp_bean.setMid(mid);
								
								from = source;
								temp_bean.setFrom(from);
								
								flag = false;
								}
							}
						j++;
						
					}
				}
			}
			
			result.add(temp_bean);
		}
		
		Collections.sort(result);
		
		for(int i = 0; i < result.size(); i++ ) {
			System.out.println(result.get(i).getField_name()+" "+result.get(i).getFrom()+" "+result.get(i).getDlxml()+" "+result.get(i).getTo());
		}
	}
}
