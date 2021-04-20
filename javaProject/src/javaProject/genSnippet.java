package javaProject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class genSnippet {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		if(args[0].equals("-f"))
		{
			File test = new File("C:\\Users\\mike4\\OneDrive\\Desktop\\코딩\\SimpleIR\\javaProject\\src\\input.txt");
			FileReader read = new FileReader(test);
			
			String [] keyword = args[3].split(" ");
			int count []  = new int[keyword.length];

			
			char buf[] = new char[80];
			read.read(buf);
			String[] bufstr = buf.toString().split(" ");
			
			for(int i=0; i<keyword.length; i++)
			if(bufstr[i].equals(keyword[i]))
				count[i]++;
			
			int max=count[0];
			int maxindex =0;
			
			for(int i=0; i<keyword.length; i++)
			{
				for(int j=0; j<keyword.length; j++)
				{
					if(count[i]>max)
					{
						maxindex = i;
					}	
				}
				System.out.println(keyword[maxindex]);
				
			}
			
		}
		
			
		
	
	
			
		}
	}

