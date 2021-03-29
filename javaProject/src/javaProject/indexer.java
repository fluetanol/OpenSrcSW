package javaProject;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class indexer {
	
	ArrayList<Float> array;
	indexer()
	{};

	void makehashmapfile(String filename) throws ParserConfigurationException, SAXException, IOException
	{
		File xml = new File(filename);
		DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance(); 
		DocumentBuilder facbuild = fac.newDocumentBuilder(); 
		Document doc = facbuild.parse(xml); 
		
		Element root = doc.getDocumentElement();
		Element thisid= (Element) root.getFirstChild(); 
		
		FileOutputStream filestream = new FileOutputStream("src/index.post");
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(filestream);
		HashMap<String, ArrayList<Float>> hashmap = new HashMap<String, ArrayList<Float>>();
		float docnum=0;
		
		while(thisid!=null)
		{
			Element thisbody = (Element) thisid.getFirstChild().getNextSibling();
			String line = thisbody.getTextContent();
			String[] linesharpsplit = line.split("#");
			
			for(int i=0; i<linesharpsplit.length; i++)
			{
			String[] linesemicolonsplit = linesharpsplit[i].split(":");
			//Integer.valueOf();
			
			if(hashmap.get(linesemicolonsplit[0]) == null)
			{
				array = new ArrayList<Float>();
				array.add(docnum);
				array.add(Float.valueOf(linesemicolonsplit[1]));
				hashmap.put(linesemicolonsplit[0],array);
			}
			else
			{
				hashmap.get(linesemicolonsplit[0]).add(docnum);	
				hashmap.get(linesemicolonsplit[0]).add(Float.valueOf(linesemicolonsplit[1]));	
			}
			}			
			thisid=(Element) thisid.getNextSibling();
			docnum++;
		}	
		
		changehashweightfile(hashmap);
		objectOutputStream.writeObject(hashmap);
		objectOutputStream.close();
		
	}
	
	
	private void changehashweightfile(HashMap hashmap)
	{
		Iterator<String> it = hashmap.keySet().iterator();
		while(it.hasNext())
		{
			String key = it.next();
			ArrayList<Float> value = (ArrayList) hashmap.get(key);
				for(int i=1; i<value.size(); i=i+2)
				{
					float weight = weight(value.get(i),value.size()/2);
					value.set(i, weight);
				}
		}	
	}
	
	private float weight(float tfxy,float dfx)
	{
		float weight  = (float) (tfxy * Math.log(5/dfx));
		weight = (float)Math.round(weight*100)/100;

		//tfx,y * log(N/dfx)
		//x:단어, y:문서 , tfxy: 문서 y에서 단어 x가 등장한 빈도수
		//dfx: 단어x가 몇개의 문서에서 등장하는지
		//N:전체 문서의 수, 이번 실습에선 5개문서이므로 N=5
		return weight;
	}
	
	void Hashmapreading(String hashname) throws IOException, ClassNotFoundException	
	{
		FileInputStream filestream = new FileInputStream(hashname);
		ObjectInputStream objectinput = new ObjectInputStream(filestream);
		
		Object object = objectinput.readObject();
		objectinput.close();
		System.out.println("읽어온 객체 타입 -> " +object.getClass()+"\n");
		HashMap hashmap = (HashMap) object;
		Iterator<String> it = hashmap.keySet().iterator();
		
		while(it.hasNext())
		{
			String key = it.next();
			ArrayList value = (ArrayList) hashmap.get(key);
			System.out.println(key+"->>"+value+"\n");
		}
	}
	
}
	
	

