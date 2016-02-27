import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class esu {
	public static GraphMatrix g;
	public static boolean dir = false;
	public static String graph_file = new String(new char[100]);
	public static int motif_size;
	public static int graph_size;
	public static int[] current;
	public static int[] ext;
	public static int nextv;
	public static GraphTree _sg;
	public static long total_occ = 0;
	
	
	public static void readArguments(int argc, String[] argv)
	{
	  if (argc != 3)
	  {
		System.out.print("Usage: ./esu <dir/undir> <size> <graph>\n");
		System.exit(1);
	  }

	  String arg_dir = argv[0];
	  if (arg_dir.charAt(0) == 'd')
	  {
		  dir = true;
	  }
	  motif_size = Integer.parseInt(argv[1]);
	  graph_file = argv[2];
	}
	
	
	
	
	public static GraphMatrix loadGraph(String graph_file) throws IOException
	{
	  g = new GraphMatrix();
		BufferedReader br = new BufferedReader(new FileReader(graph_file));
		int size;
		int max;
		int i;
		ArrayList<Integer> va = new ArrayList<Integer>();
		ArrayList<Integer> vb = new ArrayList<Integer>();

		try {
		    
		    String line = br.readLine();
		    if (line == null)
			  {
				  System.exit(1);
			  }
		   
		    GraphMatrix g = new GraphMatrix();

			  
			  int a;
			  int b;
			  int c;
			  
			  
			  size = max = 0;
		    String [] ints = new String [3];
		    while (line != null) {
		    	
		        ints = line.split(" ");
		        a = Integer.parseInt(ints[0]);
		        b = Integer.parseInt(ints[1]);
		        c = Integer.parseInt(ints[2]);
		    	va.add(a);
				vb.add(b);
				if (a > max)
				{
					max = a;
				}
				if (b > max)
				{
					max = b;
				}
				size++;
		    	
		        line = br.readLine();
		    }
		    
		} finally {
		    br.close();
		}


	  if (dir)
	  {
		  g.createGraph(max, GraphType.DIRECTED);
	  }
	  else
	  {
		  g.createGraph(max, GraphType.UNDIRECTED);
	  }

	  for (i = 0; i < size; i++)
	  {
		if (va.get(i) != vb.get(i))
		{
		  g.addEdge(va.get(i) - 1, vb.get(i) - 1);
		  //if(!dir)/// TESTTTT
			//  g.addEdge( vb.get(i) - 1, va.get(i) - 1);
		}
	  }

	  va.clear();
	  vb.clear();

	  g.sortNeighbours();
	  g.makeArrayNeighbours();

	  return g;
	}
	
	
	
	public static void countSubgraphs(GraphMatrix g, int k)
	{
	  int i;
	  int[] v = new int[1];

	  graph_size = g.numNodes();
	  current = new int[k];
	  ext = new int[graph_size];
	  nextv = 0;

	  for (i = 0; i < graph_size; i++)
	  {
		go(i, 0, 0, v);
	  }

	  current = null;
	  ext = null;
	}
	
	public static void countSubgraphs1(GraphMatrix g, int k)
	{
		//System.out.printf("In Count 1\n");
	  int i;
	  int[] v = new int[1];
	  
	  graph_size = g.numNodes();
	  current = new int[k];
	  ext = new int[graph_size];
	  nextv = 0;
	  
	  _sg.zeroFrequency();
	  
	  for (i = 0; i < graph_size; i++)
	  {
		go1(i, 0, 0, v);
	  }

	  current = null;
	  ext = null;
	  
	}
	
	
	public static void go1(int n, int size, int nextv, int[] ext)
	{
		//System.out.printf("In go1  \n");
	  current[size++] = n;

	  if (size == motif_size)
	  {
		  //System.out.printf("In IF  \n");
		String s = new String(new char[motif_size * motif_size+1]);
		s = Isomorphism.canonicalStrNauty(g, current, s);
		_sg.incrementString(s);
		total_occ++;
		//for (int i=0; i<size; i++)
			//System.out.println(s + " :  " + current[i]+1);
	  }
	  else
	  {
		int i;
		int j;
		int nextv2 = nextv;
		int[] ext2 = new int[graph_size];

		for (i = 0;i < nextv;i++)
		{
			ext2[i] = ext[i];
		}

		int[] v = g.arrayNeighbours(current[size-1]);
		int num = g.numNeighbours(current[size-1]);


		for (i = 0;i < num;i++)
		{
		  if (v[i] <= current[0])
			  continue;
		  for (j = 0;j + 1 < size;j++)
		  {
		if (g.isConnected(v[i], current[j]))
			break;
		  }
		  if (j + 1 == size)
		  {
			  ext2[nextv2++] = v[i];
		  }
		}

		while (nextv2 > 0)
		{
		  nextv2--;
		  go1(ext2[nextv2], size, nextv2, ext2);
		}
	  }
	}
	
	
	
	
	public static void go(int n, int size, int nextv, int[] ext)
	{
	  current[size++] = n;

	  if (size == motif_size)
	  {
		String s = new String(new char[motif_size * motif_size+1]);
		total_occ++;
	  }
	  else
	  {
		int i;
		int j;
		int nextv2 = nextv;
		int[] ext2 = new int[graph_size];

		for (i = 0;i < nextv;i++)
		{
			ext2[i] = ext[i];
		}

		int[] v = g.arrayNeighbours(current[size-1]);
		int num = g.numNeighbours(current[size-1]);


		for (i = 0;i < num;i++)
		{
		  if (v[i] <= current[0])
			  continue;
		  for (j = 0;j + 1 < size;j++)
		  {
		if (g.isConnected(v[i], current[j]))
			break;
		  }
		  if (j + 1 == size)
		  {
			  ext2[nextv2++] = v[i];
		  }
		}

		while (nextv2 > 0)
		{
		  nextv2--;
		  go(ext2[nextv2], size, nextv2, ext2);
		}
	  }
	}
	
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
	
		Scanner s = new Scanner(System.in);
		String input = s.nextLine();
		input = "u 3 D:\\TestG2.txt";//"u 3 D:\\s420_st.txt";
		String [] data = input.split(" ");
		 readArguments(data.length, data); 
		  g = loadGraph(graph_file);
		  boolean esu = false, gtries = !esu;
		  _sg = new GraphTree();
		  TreeMap< String, Integer> m = new TreeMap<String, Integer>();
			TreeMap< String, Integer> m_original;// = _sg.populateMap(m, motif_size);
			int i = 0, j;
		  if(esu)
		  {
			  Isomorphism.initNauty(motif_size, dir);
			  long startTime = System.nanoTime();
			  
			 
			  countSubgraphs1(g, motif_size);
			  //countSubgraphs(g, motif_size);
			  long stopTime = System.nanoTime();
			  
			  long x = total_occ;
			  System.out.printf("Found   " + total_occ +" occurrences,  Time(Nano Seconds): "+ (stopTime - startTime) +"  After convertion to mili:"+(stopTime - startTime)/1000000+ "\n" );
			  
			    int numGraphs = _sg.countGraphs();
				int occurences = (int) _sg.countOccurrences();
				
				//System.out.printf("hi\n");
					m_original = _sg.populateMap(m, motif_size);
				//result->populateMap(&m_original, motif_Size);
				//System.out.printf("hi222\n");
				ResultType[] res = new ResultType[m_original.size()];
				System.out.printf(m_original.size() + "\n");
				for(Map.Entry<String,Integer> entry : m_original.entrySet()) {
					
					  String key = entry.getKey();
					  Integer value = entry.getValue();
					  
					  res[i] = new ResultType();
					  res[i].s = key;//strdup((ii->first).c_str());
					  res[i].f_original = value;//ii->second;
					  res[i].z_score = res[i].avg_random = res[i].dev_random = 0;
					  i++;
					  System.out.printf(key + " :  "+value + "\n");
				}
		
		  }
		  if(gtries)
		  {
			  Gtrie gt_original = new Gtrie();
			  long startTime = System.nanoTime();
			  
			  
			  Isomorphism.initNauty(motif_size, dir);
			  //_sg = new GraphTree();
			  gt_original.readFromFile("D:\\undir3.gt");
			  
			  gt_original.census(g);
			  /*
			  _sg = gt_original.populateGraphTree(_sg, motif_size);
			  
			  long stopTime = System.nanoTime();
			  
			  long x = (int) _sg.countOccurrences();
			  System.out.printf("Found   " + x +" occurrences, Graphs:"+ _sg.countGraphs()+"  Time(Mili Seconds): "+(stopTime - startTime)/1000000+ "\n" );
			  
			  m_original = _sg.populateMap(m, motif_size);
			  ResultType[] res = new ResultType[m_original.size()];
				System.out.printf(m_original.size() + "\n");
				for(Map.Entry<String,Integer> entry : m_original.entrySet()) {
					
					  String key = entry.getKey();
					  Integer value = entry.getValue();
					  
					  res[i] = new ResultType();
					  res[i].s = key;//strdup((ii->first).c_str());
					  res[i].f_original = value;//ii->second;
					  res[i].z_score = res[i].avg_random = res[i].dev_random = 0;
					  i++;
					  System.out.printf(key + " :  "+value + "\n");
				}*/
				System.out.printf("Count: "+(int) _sg.countOccurrences()+"   NEW: " + GTrieNode.counterT);
		  }
		  		
			
			
		
	}

}
