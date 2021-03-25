package javaProject;
import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class makeCollection {

	private String filepath;
	
	makeCollection(){};
	makeCollection(String filename){
		filepath = filename;	
	};
	
	File[] openFile(){
		File temp = new File(filepath);
		File[] contents = temp.listFiles();	
		return contents;
	}
	
	void setFilename(String filename){
		filepath = filename;
	}
	
	
	Document collection(File[] contents) throws ParserConfigurationException, DOMException, IOException{
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		Element docs = doc.createElement("docs");
		doc.appendChild(docs);
		parsing(doc,docs,contents);
		return doc;
		
	}
	
	private void parsing(Document doc, Element docs,File[] contents) throws DOMException, IOException
	{
		//doc는 문서 element는 문서에 들어가는 태그들
		for(int i=0; i<contents.length; i++)
		{
		Element docid= doc.createElement("doc");
		docid.setAttribute("id",Integer.toString(i));
		Element title =  doc.createElement("title");
		Element body = doc.createElement("body");	
		
		docs.appendChild(docid);
		docid.appendChild(title);
		docid.appendChild(body);
		
		FileReader fr = new FileReader(contents[i]);
		BufferedReader bufr = new BufferedReader(fr);
		int linenum=0;
		String line = "";
		
		//title태그와 p태그를 쳐내는 부분
		while((line = bufr.readLine())!=null)
		{

		if(line.indexOf("<title>")!=-1)  //title이 있다면
		{
			line = line.replace("<title>", "");	//그것을 공백으로 교체
			line = line.replace("</title>", "");//닫는 title태그도 없앰
			title.appendChild(doc.createTextNode(line));
			System.out.println(line);
		}
			
		if(line.indexOf("<p>")!=-1)
		{
		line = line.replace("<p>", "");
		line = line.replace("</p>", "");
		body.appendChild(doc.createTextNode(line));
		System.out.println(line);
		}	
		}
		System.out.println();
		fr.close();
		bufr.close();
		}
	}
	
	//UTF-8형식으로 XML화 시켜주는 코드
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
