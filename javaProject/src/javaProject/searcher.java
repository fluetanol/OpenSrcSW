package javaProject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.snu.ids.kkma.index.*;


public class searcher {

	String query;
	
	searcher(){}
	


	void queryreading(String query, String hashmap, int size) throws ClassNotFoundException, IOException
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

		float[] inner = InnerProduct(set,queryhash1,hash, size);
		for(int i=0; i<inner.length; i++)
		System.out.println("doc"+i+"의 내적결과: "+inner[i]);
	}
	
	private float[] CalcSim()
	{
		return null;
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
>>>>>>> feature
