package javaProject;

import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class kuir {

	public static void main(String[] args) throws ParserConfigurationException, TransformerException, IOException, SAXException, ClassNotFoundException {
		// TODO Auto-generated method stub
	
		if(args[0].equals("-c"))
		{
		makeCollection maked = new makeCollection(args[1]);
		File[] contents = maked.openFile();
		Document doc=maked.collection(contents);
		maked.Makingxml(doc,"src/collection.xml");	// 가져온 doc파일을 xml파일로 전환시킨다
		}

		
		else if(args[0].equals("-k"))
		{
		makeKeyword maked = new makeKeyword();
		Document doc = maked.keywordgo(args[1]);
		maked.Makingxml(doc, "src/index.xml");
		}
		
		
		else if(args[0].equals("-i"))
		{
			indexer index = new indexer();
			index.makehashmapfile(args[1]);
			index.Hashmapreading("src/index.post",true);	
		}
		
		else if(args[0].equals("-s"))
		{
			searcher search =  new searcher();
			float[][] Calc = search.queryreading(args[3],args[1],5);	
			search.CompareSimtoXml("src/index.xml", Calc,3);
		}
		
		
		System.out.println("finish!");		
	}
}