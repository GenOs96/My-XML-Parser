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

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class MyParser {

	public static void main(String[] args)
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, RowsExceededException, WriteException {

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
		// String type_exp = "XForm/Message-type";
		String func_exp = "XForm/Method";

		NodeList source_list = (NodeList) xPath.compile(source_exp).evaluate(source_doc, XPathConstants.NODESET);
		NodeList target_list = (NodeList) xPath.compile(target_exp).evaluate(target_doc, XPathConstants.NODESET);

		// NodeList type_list = (NodeList) xPath.compile(type_exp).evaluate(target_doc,
		// XPathConstants.NODESET);

		NodeList func_list = (NodeList) xPath.compile(func_exp).evaluate(target_doc, XPathConstants.NODESET);

		// System.out.println(type_list.getLength());

		ArrayList<CosmosBean> result = new ArrayList<CosmosBean>();

		for (int i = 0; i < target_list.getLength(); i++) {

			CosmosBean temp_bean = new CosmosBean();
			Node target_node = target_list.item(i);

			if (target_node.getNodeType() == Node.ELEMENT_NODE) {
				Element field_Element = (Element) target_node;

				String to = "0";
				String field_name;
				String from = null;
				String func = null;
				String logic = null;

				Element to_Element;

				try {
					to_Element = (Element) field_Element.getElementsByTagName("To").item(0);
					to = to_Element.getAttribute("Startindex");

				} catch (Exception e) {
					break;
				}

				try {
					func = to_Element.getAttribute("CallMethod");

				} catch (Exception e) {
				}

				try {
					Element from_Element = (Element) field_Element.getElementsByTagName("From").item(0);
					from = from_Element.getAttribute("XPath");
				} catch (Exception e) {

				}

				field_name = field_Element.getAttribute("Name");

				temp_bean.setField_name(field_name);
				temp_bean.setTo(to);

				for (int j = 0; j < func_list.getLength(); j++) {
					Element func_node = (Element) func_list.item(j);
					String func_name = func_node.getAttribute("CallMethod");

					if (func_name.equals(func)) {
						logic = func_node.getTextContent();
						temp_bean.setLogic(logic);
					}
				}

				if (from != null) {
					int j = 0;
					boolean flag = true;
					while (j < source_list.getLength() && flag) {

						Node search_node = source_list.item(j);
						String source = null;
						String target = null;

						if (search_node.getNodeType() == Node.ELEMENT_NODE) {

							Element search_Element = (Element) search_node;

							try {

								Element source_Element = (Element) search_Element.getElementsByTagName("source")
										.item(0);
								source = source_Element.getAttribute("xpath");

							} catch (Exception e) {
							}

							try {

								Element target_Element = (Element) search_Element.getElementsByTagName("target")
										.item(0);
								target = target_Element.getAttribute("xpath");

							} catch (Exception e) {
							}

							if (from.equals(target)) {

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

		for (int i = 0; i < result.size(); i++) {
			System.out.println(result.get(i).getField_name() + " " + result.get(i).getFrom() + " "
					+ result.get(i).getMid() + " " + result.get(i).getTo()+" "+result.get(i).getLogic());
		}
		
		PrintAll.XMLtoEXCEL(result);
		
	}
}
