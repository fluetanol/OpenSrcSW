package javaProject;

import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.xml.parsers.*;

import org.snu.ids.kkma.index.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;


public class searcher {

	String query;
	searcher(){}
	
	float[][] queryreading(String query, String hashmap, int docsize) throws ClassNotFoundException, IOException
	{
		System.out.println("확인할 문자열: "+query);
		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(query, true);		
		HashMap queryhash1 = new HashMap();
		indexer index= new indexer();
		HashMap hash = index.Hashmapreading(hashmap,false);//해쉬맵 가져옴

		for(int i=0; i<kl.size(); i++)
		{
			Keyword kwd = kl.get(i);
			queryhash1.put(kwd.getString(), kwd.getCnt());
		}	
		Set<String> set = queryhash1.keySet();
		float[][] Calc = CalcSim(set,queryhash1,hash, docsize); //  [CalcSimnum][docnum]
		return Calc;
	}
	
	void CompareSimtoXml(String filename,float[][] Calc,int printsize) throws ParserConfigurationException, SAXException, IOException
	{
		if(Calc.length<printsize)
		{
		System.out.println("잘못된 입력: printsize가 Calc배열크기보다 클수 없습니다.");
		return;
		}
		
		
		File xml = new File(filename);
		DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance(); 
		DocumentBuilder facbuild = fac.newDocumentBuilder(); 
		Document doc = facbuild.parse(xml);

		Element root = doc.getDocumentElement();
		Element[] thisid= new Element[Calc.length];
		thisid[0] = (Element) root.getFirstChild();
	
		for(int i=1; i<thisid.length; i++)
		{
		thisid[i] = (Element) thisid[i-1].getNextSibling();
		System.out.println(thisid[i].getFirstChild().getTextContent());
		}
	
		for(int i=0; i<printsize; i++)
		{
			float max=Calc[i][0];
			float maxpos=i;
			for(int j=i; j<Calc.length; j++)
			{
				if(Calc[j][0]>max)
				{
					max = Calc[j][0];
					maxpos=Calc[j][1];
				}
			}
			float[][] temp = new float[1][2];
			temp[0][0] = Calc[i][0];
			temp[0][1] = Calc[i][1];
			Calc[i][0] = max;
			Calc[i][1] = maxpos;
			Calc[(int)maxpos][0] = temp[0][0];
			Calc[(int)maxpos][1] = temp[0][1];
			
			System.out.println("순위 "+(i+1)+":"+thisid[(int) Calc[i][1]].getFirstChild().getTextContent().trim());
			System.out.println(" 값:"+Calc[i][0]);
		}
	}
		
	float[][] CalcSim(Set set,HashMap query,HashMap hash, int size)
	{
		float[] inner =	InnerProduct(set,query,hash, size);
		float[][] sim = new float[size][2];
		
		for(int i=0; i<size; i++)
		{
			Iterator<String> it = set.iterator();
			double addquery = 0;
			double addhash = 0;
			while(it.hasNext())
			{
				String key = it.next();
				ArrayList<Float> hashval = (ArrayList<Float>) hash.get(key);
				
				addquery =  addquery+Math.pow((Integer) query.get(key),2);
				try
				{
				addhash = addhash+Math.pow((double) hashval.get(i), 2);
				}
				catch(Exception e) {}
			}
			
			if((float) (Math.sqrt(addquery)*Math.sqrt(addhash))!=0)
			sim[i][0]=inner[i]/(float) (Math.sqrt(addquery)*Math.sqrt(addhash));
			
			sim[i][1]=i;
			System.out.println(sim[i][1]+": "+sim[i][0]);
		}

		return sim;
	}
	
	private float[] InnerProduct(Set set,HashMap query,HashMap hash, int size)
	{
		float[] inner = new float[size];
		Iterator<String> it = set.iterator();	
			
			for(int i=0; i<size; i++)
			{
				while(it.hasNext())
				{
					String key =it.next();
					ArrayList<Float> hashval = (ArrayList<Float>) hash.get(key);
					int queryval = (int) query.get(key);

					try
					{					
	inner[Math.round(hashval.get(i*2))] = inner[Math.round(hashval.get(i*2))]  + queryval * hashval.get(i*2+1);
					}
					catch(Exception e){}	
				}	
			}
			
		return inner;
	}
}
