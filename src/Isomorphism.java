import java.util.Collections;
import java.util.Random;
import java.util.Set;

import org.omg.CORBA.FREE_MEM;


public class Isomorphism {
	
	private static final int WORDSIZE = 32;//WORKSPACE_SIZE MAXN*160;
	private static final int WORKSPACE_SIZE = 0;
	public static int n = 3;
	public static int m;
	
	public static Set [] gv;
	
	public static boolean dir;
	
	
	// Static variables
		static int MAXN = 10 , MAXM = 10;
		public static int []  workspace = new int[WORKSPACE_SIZE];
		//public static setword[] workspace = new setword[WORKSPACE_SIZE];
		public static GraphMatrix[] g = new GraphMatrix[MAXN * MAXM];
		public static int[] lab = new int[MAXN];
		public static int[] ptn = new int[MAXN];
		public static int[] orbits = new int[MAXN];
		
	
	
	
	public static void initNauty(int size, boolean directed)
	{
	  n = size;
	  m = (n + WORDSIZE - 1) / WORDSIZE;
	  //nauty_check(WORDSIZE,m,n,NAUTYVERSIONID);
    
	  dir = directed;
    /*
	  options.getcanon = 1;
	  options.writeautoms = 0;
	  if (dir)
	  {
		  options.digraph = 1;
	  }
	  else
	  {
		  options.digraph = 0;
	  }*/
	}

	public static char[] canonicalBasedNauty(char[] in, char[] best, int size) {
		// TODO Auto-generated method stub
		
		int i;
		  int j;
		  int k;
		  int min_i;
		  int ss;
		  
		  int[] mymap = new int[size];
		  int[] degree = new int[size];
		  int[] last_degree = new int[size];
		  int[] total_degree = new int[size];
		  //String used = new String(new char[size]);
		  char [] used = new char[size];
		  char [] articulation = new char[size];
		  int nbfs;
		  int[] bfs = new int[size];
	    
		  for (i = 0; i < size; i++)
		  {
			degree[i] = 0;
			used[i] = 0;
			for (j = 0; j < size; j++)
			{
			  if (in[i * size + j] == '1')
			  {
				  degree[i]++;
			  }
			  if (in[i + size * j] == '1')
			  {
				  degree[i]++;
			  }
			}
			total_degree[i] = last_degree[i] = degree[i];
		  }
	    
		  // Find the node with the smaller number of connections
		  for (ss = size-1; ss >= 0; ss--)
		  {
	    
			if (ss > 3)
			{
			  // Find articulation points (cannot be removed)
			  for (i = 0; i < size; i++)
			  {
				if (used[i] != 0)
					continue;
				used[i] = 2;
				j = 0;
				while (used[j] != 0)
				{
					j++;
				}
				nbfs = 1;
				bfs[0] = j;
				used[j] = 2;
				j = 0;
				while (j < nbfs)
				{
				  for (k = 0; k < size; k++)  // (used[k] == 0) ???
				  { //(used[k] == 0)
					if ((used[k] == ' ') && (in[k * size + bfs[j]] == '1' || in[k + size * bfs[j]] == '1'))
					{
					  used[k] = 2;
					  bfs[nbfs++] = k;
					}
				  }
				  j++;
				}
				if (nbfs != ss)
				{
					articulation[i] = 1;
				}
				else
				{
					articulation[i]  = 0;
				}
				for (j = 0; j < size; j++)
				{
				  if (used[j] == 2)
				  {
					  used[j] = 0;
				  }
				}
				  }
				}
				else
				{
					for (i = 0; i < size; i++)
					{
						articulation[i]  = 0;
					}
				}
		    
				min_i = -1;
				for (i = 0; i < size; i++)
				{
				  if ((used[i] != 0) || (articulation[i] != 0))
					  continue;
				  if (min_i < 0 || degree[i] < degree[min_i])
				  {
					  min_i = i;
				  }
				  else if (degree[i] == degree[min_i])
				  {
				if (last_degree[i] < last_degree[min_i])
				{
					min_i = i;
				}
				else if (last_degree[i] == last_degree[min_i])
				{
				  if (total_degree[i] < total_degree[min_i])
				  {
					  min_i = i;
				  }
				  else if (total_degree[i] == total_degree[min_i])
				  {
					for (j = ss + 1; j < size; j++)
					{
					  if ((in[i * size + mymap[j]] != in[min_i * size + mymap[j]] )&& (in[i * size + mymap[j]] != 0)&&(in[min_i * size + mymap[j]] != 0))
					  {
					if (in[i * size + mymap[j]] == '0')
					{
						min_i = i;
					}
					break;
					  }
					}
					if (j == size)
					{
					  for (j = ss + 1; j < size; j++)
					  {
					if ((in[i + size * mymap[j]] != in[min_i + size * mymap[j]])&&( in[i + size * mymap[j]] != 0)&&( in[min_i + size * mymap[j]] != 0) )
					{
					  if (in[i + size * mymap[j]] == '0')
					  {
						  min_i = i;
					  }
					  break;
					}
					  }
					}
				  }
				}
			  }
			}
	    
			if (ss != 0 && degree[min_i] == 0)
			{
				/*
			  fprintf(stderr, "[%s]\n", in);
			  fprintf(stderr, "!!! OH NO %d [canonicalBasedNauty]\n", ss);
			  fflush(stdout);
			  System.exit(1);*/
			}
	    
			for (i = 0; i < size; i++)
			{
			  last_degree[i] = degree[i];
			  if (in[i * size + min_i] == '1')
			  {
				  degree[i]--;
			  }
			  if (in[i + size * min_i] == '1')
			  {
				  degree[i]--;
			  }
			}
			mymap[ss] = min_i;
			used[min_i] = 1;
		  }
	    
		  best[size * size] = 0;
		  for (i = 0; i < n; i++)
		  {
			for (j = 0; j < n; j++)
			{
			  best[i * size + j] = in[mymap[i] * size + mymap[j]];
			}
		  }
		  return best;
		}

	public static String canonicalStrNauty(GraphMatrix myg, int[] v, String s) {
		String out="";
		
		boolean adjM [][] = myg.adjacencyMatrix();//new boolean [10][10];
		String test = "";
		java.util.Arrays.sort(v);
		for (int i = 0; i < n ; i++) {
			for (int j = 0; j<n; j++)
			{
				if(adjM [v[i]][v[j]]) test += "1";
				else test += "0";
			}
			 
		}
		
		
		 Object options;
		Object stats;
		Object workspace;
		Object mm;
		//nauty(g, lab, ptn, null, orbits, options, stats, workspace, WORKSPACE_SIZE, m, n, mm);
		    
		
		
		test += " ";
		System.out.println(test);
			out = "011100100" + " ";
		
		if((esu.total_occ %5) == 0)
			out = "011101110" + " ";
		//for (int i = 0; i < v.length; i++) {
		//	System.out.printf(v[i] + "*");
		//}
		//System.out.println();
		//return out;
		return test;
		
		
	}


}
