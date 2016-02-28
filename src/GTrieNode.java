import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedList;


public class GTrieNode {
	private static final int MAX_BUF = 1024;
	private static final char BASE_FIRST = ' ';
	private static final int BASE_FORMAT = 95;
	private static final int BASE_BITS = 6;
	public static int[] mymap;
	public static boolean[] used;
	public static int numNodes;
	public static int[][] fastnei;
	public static boolean[][] adjM;
	public static int[] numnei;
	public static boolean isdir;
	public static int glk;
	public static int counterT = 0;
	int depth;
	int frequency ;
	//public static LinkedList<GTrieNode []> child = null;
	public LinkedList<GTrieNode> child;
	
	boolean show_occ = true;
	public static String occ_file = "D:\\res.txt";
	public static PrintWriter writer;
	Pair<Character, Character> p;
	Pair<Integer,Integer> p2;
	
	static LinkedList<LinkedList<Pair>> cond  = new LinkedList<LinkedList<Pair>>();
	 int[] conn = null;
     int nconn;
     boolean is_graph;
	boolean cond_ok;
	int ncond;
	 boolean[] out = null;
     boolean[] in = null;
     int total_in;       // Number of inward  edges
     int total_out;      // Number of outward edges
     int total_edges;    // Number of inward + outward edges
     
     LinkedList< LinkedList<Integer> >   this_node_cond; // This node must be bigger than all these nodes
     boolean cond_this_ok;
     
       
	public GTrieNode(int d) throws FileNotFoundException, UnsupportedEncodingException {
		//writer = new PrintWriter(occ_file, "UTF-8");
		depth = d;

		is_graph = false;
		frequency = 0;
		nconn = 0;

		if (d != 0) {
			in = new boolean[d];
			out = new boolean[d];
			conn = new int[d];
		}
		else {
			in = out = null;
			conn = null;
		}

		total_in = total_out = total_edges = 0;

		//child = new LinkedList<GTrieNode []>();
		child = new LinkedList<GTrieNode>();
		child.clear();
		
		//cond= new LinkedList<LinkedList<Pair>>();
		//cond.clear();
		this_node_cond = new LinkedList<LinkedList<Integer>>();
		this_node_cond.clear();

		cond_ok = false;
		cond_this_ok = false;
	}
	public final void readFromFile(FileInputStream inputStream ) throws IOException
	//public final void readFromFile(BufferedReader buffReader) throws IOException
	{
		int nchilds;
		
	  int i;
	  int j;
	  int pos;
	  int bits;
	  
	  //Pair p = new Pair(p, p);
	  char aux;
	  char [] buff =  new char[MAX_BUF];
	  String buf = new String(new char[MAX_BUF]);
	  byte[] buffer = new byte[MAX_BUF];
	  byte[] buffer2 = new byte[MAX_BUF];
	  
	  //GTrieNode[] c;
	  GTrieNode c;
	  //BufferedReader reader = new BufferedReader(new FileReader(f2));
	 // FileInputStream inputStream = new FileInputStream(inputStream);
	  
	  int total = 0;
      int nRead = 0;
     
      boolean cond_ok ;
      int countAux = 0;
      String line = " A";
      while(true)
      {
    	  int nRead1 = inputStream.read(buffer2, 0, 1);
    	  buffer[countAux] = buffer2[0];
    	  
    	  if(buffer[countAux] == 10)
    		  break;
    	  countAux++;
    	  if(nRead1 == -1)
    		  line = null;
      }
      
   	  
      //while((nRead = inputStream.read(buffer, 0, MAX_BUF)) != -1) {
      //while((nRead = inputStream.read(buffer, 0, numChars)) != -1) {
      //String line = buffReader.readLine();
      if(line  != null){
    	//  buffer = line.getBytes(StandardCharsets.UTF_8);;
    	  
    	for (int totot = 0; totot < MAX_BUF; totot++)
  		{
    		  System.out.println("---> buf[t]: "+  buffer[totot]+ " , t: "+ totot);
  			if (buffer[totot] == 0)
  				break;
  		}
   		  int totot = (int)buffer[0];
    	  aux =  (char) ((int)buffer[0] - (int)BASE_FIRST);//(char) buff[0];
    	  if(BIT_VALUE(aux, 0))//(aux == BASE_FIRST)
    	  { is_graph = true;
    	  	System.out.println("GRAPH");
    	  }
    	  else
    		  is_graph = false;
    	  
    	  int nbytes = aux >> 1;

  		pos = 1;
  		System.out.println("Starting11: POS: "+ pos + " nbytes: " + nbytes + "   AUX:" +(int)aux );
  		
  		for (i = 0, j = 1, nchilds = 0; i < nbytes; i++, j *= BASE_FORMAT)
  		{
  			//if(buff[pos++] == BASE_FIRST)
  			//{
  				
  			//}
  		  aux = (char) (((int)buffer[pos++]) - ((int)BASE_FIRST));
  		  nchilds += (int)aux * j;
  		}
  		
  		System.out.println("Starting: POS: "+ pos + " NChilds: " + nchilds);
  		
  	// Connections: outgoing
  			aux = (char) (buffer[pos++] - BASE_FIRST);
  			for (i = 0, bits = 0; i < depth; i++, bits++)
  			{
  			  if (bits == BASE_BITS)
  			  {
  				  aux = (char) (buffer[pos++] - BASE_FIRST);
  				  bits = 0;
  			  }
  			  if (BIT_VALUE(aux, bits))
  			  {
  				  out[i] = true;
  			  }
  			  else
  			  {
  				  out[i] = false;
  			  }
  			}

  			System.out.println("After OUTGoing: POS: "+ pos + " Depth: " + depth);

  			// Connections: ingoing
  			aux = (char) (buffer[pos++] - BASE_FIRST);
  			for (i = 0, bits = 0; i < depth; i++, bits++)
  			{
  			  if (bits == BASE_BITS)
  			  {
  				  aux = (char) (buffer[pos++] - BASE_FIRST);
  				  bits = 0;
  			  }
  			  if (BIT_VALUE(aux, bits))
  			  {
  				  in[i] = true;
  			  }
  			  else
  			  {
  				  in[i] = false;
  			  }
  			}

  			
  			
  			System.out.println("After INGoing: POS: "+ pos + " Depth: " + depth);

  			// Previous Conditions
  			int t = buffer[pos];
  			byte tt = buffer[pos];
  			char ttt = (char) buffer[pos];
  			int test = (buffer[pos] - BASE_FIRST);
  			
  			aux = (char) (buffer[pos++] - BASE_FIRST);
  			int tttre = (int) aux;
			if (aux == 0)
  			{
  				this.cond_ok = true;
  				System.out.println("cond_ok = true");
  			}
  			else
  			{
  				this.cond_ok = false;
  				System.out.println("cond_ok = false");
  			}

			System.out.println("Before aux"+ (int)aux + "  POS:"+ pos);
			if(buffer[pos] == 10 || buffer[pos] ==0 ) aux = 0;
  			if (aux > 0)
  			{
  			  ncond = aux;
  			  for (i = 0; i < ncond; i++)
  			  {
  				  LinkedList<Pair> newcond = new LinkedList<Pair>();
	  			while (true)
	  			{
	  				//if(pos < line.length())
	  				int LOLO = buffer[pos];
	  			  aux = (char) ((buffer[pos++] - BASE_FIRST) - 1);
	  			  test = (int)aux;
	  			  System.out.println("Inside aux "+ (int)aux + "  POS:"+ pos + "Ncond: " + ncond + "Buffer" + LOLO);
	  			  if ((int)aux < 0 || (int)aux > 6000)
	  				  {
	  				  	aux = (char) -1;
	  				  	break;
	  				  
	  				  }
	  			  
	  			  p = new Pair<Character, Character>(' ', ' ');
	  			  p2 = new Pair<Integer, Integer>(0, 0);
	  			  p2.setFirst((int)aux);
	  			  p.setFirst(aux); //= aux;
	  			  //char aux2;
	  			  aux = (char) (buffer[pos++] - BASE_FIRST - 1);
	  			  p.setSecond(aux);
	  			  p2.setSecond((int)aux);
	  			  test = (int)aux;
	  			  //Pair<Character, Character> p = new Pair<Character, Character>(aux, aux2);
	  			System.out.println("Values being added to P,  f:"+ p2.getFirst() + "   S:"+p2.getSecond());
	  			  //newcond.addLast(p);
	  			//newcond.addLast(p2);
	  			newcond.add(p2);
	  			}
  			
	  			//cond.addLast(newcond);
	  			cond.add(newcond);
	  			System.out.println("COND: "+ cond.size());
  			  }
  			}

  			//if(pos < line.length())
  			if (buffer[pos] != '\n')
  			{
  				System.out.println("ERROR");
  			  /*fprintf(stderr, "ERROR: [%s] !%d!%c!\n", buf, pos, buf.charAt(pos));
  			  fprintf(stderr,"[%d](%s) |", depth, is_graph?"X":" ");
  			  for (i = 0, bits = 0; i < depth; i++, bits++)
  			  {
  			fprintf(stderr,"%s", out[i]?"1":"0");
  			  }
  			  fprintf(stderr,"|\n");
  			  */
  			}

  			
  			
  		// Conn and nconn variables (was missing)
  			for (i = 0; i < depth; i++)
  			{
  			  if (out[i] || in[i])
  			  {
  			  
  				  conn[(nconn)++] = i;
  			  }
  			}


  			for (i = 0; i < nchilds; i++)
  			{
  			  c = new GTrieNode(depth + 1);
  			  //c[i] = new GTrieNode(depth + 1);
  			 c.readFromFile(inputStream);
  			 
  			 //c.readFromFile(buffReader);
  			  this.child.add(c);
  			  //this.child.addLast(c);
  			}

  		  
  			
  			
  			
  			
          // Convert to String so we can display it.
          // Of course you wouldn't want to do this with
          // a 'real' binary file.
          //System.out.println(new String(buffer));
          total += nRead;
      }   

      
     // inputStream.close();        

	  
	
	}
	private boolean BIT_VALUE(char aux, int bits) {
		// TODO Auto-generated method stub
		int r = (((aux)>>(bits))&1); 
		
		if(r ==0)return false;
		
		return true;
		
	}
	public void zeroFrequency() {
		// TODO Auto-generated method stub
		frequency = 0;
		if(this.child.size() > 0)
		for (GTrieNode gTrieNodes : this.child) {
			gTrieNodes.zeroFrequency();
			//gTrieNodes.frequency = 0;
		}
		/*
		for (GTrieNode gTrieNodes : child) {
			gTrieNodes.zeroFrequency(); ////[0] ???
		}
		
		*/
	}
	public void goCondDir() {
		// TODO Auto-generated method stub
		
	}
	public void goCondUndir() {
		
		int i;
		  int j;
		  int ci;
		  int mylim;
		  int glaux;
		  int ncand;
		
		  int p;
		  char ft,st;
			int fft, sst;
			
		  mylim =Integer.MAX_VALUE;
		  
		if (!this.cond_ok)
		  {
			i = 1;
			
			//counterT++;
			//System.out.println("FAAAAT  " + counterT);
			for(int jN=0; jN < cond.size() -1 ; jN ++)
			{
				glaux = -1;
				
				LinkedList<Pair> jj2N = cond.get(jN);
				Pair kk3 = jj2N.get(jj2N.size()-1);
				int kN = 0;
				for(kN = 0 ; kN < jj2N.size()-1 ; kN++){
					Pair kk2N = jj2N.get(kN);
					//ft = (char)kk2N.getFirst();
					//st = (char)kk2N.getSecond();
					fft =  (int) kk2N.getFirst();//(int) ft;
					sst = (int) kk2N.getSecond();//(int) st;
					int tat = mymap[fft];
					int tat2 = mymap[sst];
					if ((sst < glk) && (mymap[fft] > mymap[sst]))
						  break;
					else if (sst == glk && mymap[fft] > glaux)
					{
							System.out.println("ENTER, sst = glk: "+ glk + "  mymap[fft]: "+ mymap[fft] + "  GLAUX:" + glaux);
							
							glaux = mymap[fft];
					}
					
				}
				
				if (kk3 == jj2N.get(kN))
				  {
					i = 0;
					if (glaux < mylim)
					{
						mylim = glaux;
						System.out.println("kk == kkend,  mylim = glaux : " + mylim);
						System.out.println("mymap[0]: "+mymap[0]+",   mymap[1]: "+mymap[1]+" , mymap[2]:"+mymap[2]);
						
					}
				  }
			}
			
			if (i != 0)
				return;
		  }
		
		  System.out.println("VALUES mylim: "+ mylim);
		
		  if (mylim == Integer.MAX_VALUE)
		  {
			  mylim = 0;
		  }
	    
		  ncand = 0;
		  j = ci = Integer.MAX_VALUE;
		  
		  for (i = 0; i < nconn; i++)
		  {
			glaux = numnei[mymap[conn[i]]];
			if (glaux < j)
			{
			  ci = mymap[conn[i]];
			  j = glaux;
			  System.out.println("VALUES_INSIDE CI: "+ ci+ "  J:" + j);
			}
		  }
	    
		  glaux = j;
		  ncand = ci;
		  System.out.println("VALUES CI: " + glaux + "  J:" + j);
		  
		  for (p = fastnei[ncand][j - 1], ci = glaux - 1; ci >= 0; ci--, p--)
		  {
			i = p;
			if (i < mylim)
				break;
			if (used[i])
				continue;
			mymap[glk] = i;
	    
			
			boolean b = adjM[i][0];
			boolean test;
			for (j = 0; j < glk; j++)
			{
				//System.out.println("FOR2");
				test = adjM[i][mymap[j]];//b && ((mymap[j] == 0) ? false : true);
			  if (out[j] != test)//b)//(b + mymap[j])) ????????????????????????????????????????????????
				  break;
			}
			
			//counterT++;
			//System.out.println("GLK:  "+ glk+ "   COUNTER: "+ counterT);
			if (j < glk)
				continue;
			//System.out.println("FOR");
			if (is_graph)
			{
			  frequency++;
			  counterT ++;
			  System.out.println("PLUS: " + counterT);
			  
			if (show_occ)
			  {
				String toWrit="";
			for (int k = 0; k <= glk; k++)
			{
			  for (int l = 0; l <= glk; l++)
			  {
				  toWrit+=(adjM[mymap[k]][mymap[l]]?'1':'0');
				//fputc(adjM[mymap[k]][mymap[l]]?'1':'0', occ_file);
			  }
			}
			//fputc(':', occ_file);
			toWrit+=':';
			for (int k = 0; k <= glk; k++)
			{
			  //fprintf(occ_file, " %d", mymap[k] + 1);
				toWrit+=  (mymap[k] + 1);
			}
			//fputc('\n', occ_file);
				toWrit+= '\n';
				//writer.println(toWrit);
				writer.append(toWrit);
	    		
				//
			  }
			}
	    
			used[i] = true;
			glk++;
			
			
			//for (GTrieNode[] gTrieNodes : child) {
			for (GTrieNode gTrieNodes : child) {
				gTrieNodes.goCondUndir(); ////[0] ???
			}
			
			glk--;
			used[i] = false;
		  }
		
		
		
		
		
	}
	public int maxDepth() {
		int aux = 3;
		
		/*
		for (GTrieNode gTrieNodes : child) {
			aux = Math.max(aux, 1 + gTrieNodes.maxDepth()); ////[0] ???
		}*/
		
		return aux;
	}
	public GraphTree populateGraphTree(GraphTree tree, char[] s, int size) {
		/*
		int i, pos = depth - 1;

		for (i = 0; i<depth; i++) {
			s[pos*size + i] = out[i] ? '1' : '0';
			s[i*size + pos] = in[i] ? '1' : '0';
		}
		String ss = String.valueOf(s);
		
		if (is_graph)
			tree.setString(ss, frequency);

		LinkedList<GTrieNode >::const_iterator ii, iiend;
		for (ii = child.begin(), iiend = child.end(); ii != iiend; ++ii)
			(*ii)->populateGraphTree(tree, s, size);
			*/
		for(GTrieNode gN1 : child)
			for(GTrieNode gN : child)
			{
				int i, pos = gN.depth - 1;

				for (i = 0; i< gN.depth; i++) {
					s[pos*size + i] = gN.out[i] ? '1' : '0';
					s[i*size + pos] = gN.in[i] ? '1' : '0';
				}
				
				String ss = String.valueOf(s);
				if (gN.is_graph)
					tree.setString(ss, gN.frequency);
				
				
				//gN.populateGraphTree(tree, s, size);
			}
				
		return tree;
		
	}

}
