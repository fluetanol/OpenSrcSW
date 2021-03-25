package javaProject;

import java.io.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.snu.ids.kkma.index.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class makeKeyword{

	makeKeyword(){};
	
	Document keywordgo(String filename) throws IOException, ParserConfigurationException, SAXException
	{
		File xml = new File(filename);
		String line = "";
		DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance(); //말 그대로 doc파일의 도구가 존나 많은 공장 하나 설립하는것
		DocumentBuilder facbuild = fac.newDocumentBuilder(); //도구집 하나 구해옴
		Document doc = facbuild.parse(xml); //특정 파일을 doc형태로 파싱하는 도구를 씀(doc형태로 읽어들이겠다는 것이다)
		
		Element root = doc.getDocumentElement();//맨 윗 노드 가져옴 (html하던것을 떠올려야함)
		Element thisid= (Element) root.getFirstChild(); 
		
		while(thisid!=null)
		{
			Element thisbody = (Element) thisid.getFirstChild().getNextSibling();; //동위의 다음 노드를 챙겨옴(sibling노드)
			String temp = thisbody.getTextContent();
		
			KeywordExtractor ke = new KeywordExtractor();
			KeywordList kl = ke.extractKeyword(temp, true);
			for(int i=0; i<kl.size(); i++)
			{
				Keyword kwd = kl.get(i);
				line = line + kwd.getString()+":"+kwd.getCnt()+"#";
			}
			thisbody.setTextContent(line);	
			line = "";
			thisid=(Element) thisid.getNextSibling();
		}
		return doc;
	}
	
	//		body.setTextContent("ad");

	
	void Makingxml(Document doc, String xmlfilename) throws TransformerException, FileNotFoundException
	{
	TransformerFactory transformerFactory = TransformerFactory.newInstance();
	Transformer transformer = transformerFactory.newTransformer();
	transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	DOMSource source = new DOMSource(doc);
	StreamResult result = new StreamResult(new FileOutputStream(new File(xmlfilename)));
	transformer.transform(source, result);
	}
	
}
