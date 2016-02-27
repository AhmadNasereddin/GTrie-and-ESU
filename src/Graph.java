import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

public class Graph
{
	private GraphType _type;

	  private int _num_nodes;
	  private int _num_edges;

	  private int[] _in;
	  private int[] _out;
	  private int[] _num_neighbours;

	  private int[][] _adjM;
	  private int[][] _array_neighbours;
	  private ArrayList<Integer>[] _adjOut;
	  //private ArrayList<Integer>[] _adjIn;
	  //ArrayList<ArrayList<Integer>> _adjIn;
	  private ArrayList<Integer>[] _adjIn;
	  private ArrayList<Integer>[] _neighbours;
	  
	  private ArrayList<Integer>[] _adjL ;
	  private ArrayList<Integer>[] _adjLI ;
	 
	  private int[] _in_ds ;
	  private int[] _out_ds;
	  private Pair _io_ds ;
	
	public Graph()
	{
	  _init();
	}

	public Graph(int n)
	{
	  _init();
	  _createGraph(n);
	}

	public void dispose()
	{
	  _delete();
	}
	
	public final int numNodes()
	  {
		  return _num_nodes;
	  }
	  
	public final int numEdges()
	  {
		  return _num_edges;
	  }
	  
	public Graph(boolean a, Graph g)
	{
	  int i;
	  int j;
    
	  _init();
	  _createGraph(g.numNodes());
    
	  for (i = 0; i < g.numNodes(); i++)
	  {
		for (j = 0; j < g.numNodes(); j++)
		{
		  addEdge(i, j, g.getEdge(i,j));
		}
	  }
	}



private int getEdge(int i, int j) {
		
		return _adjM[i][j];
	}

	//C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
	public void _init()
	{
	  _adjM = null;
	  _adjL = null;
	  _adjLI = null;
	  _neighbours = null;
	  _in_ds = null;
	  _out_ds = null;
	  _io_ds = null;
	  _in = null;
	  _out = null;
	  _num_nodes = _num_edges = 0;
	}

//C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
	public void _createGraph(int num_nodes)
	{
    
	  _num_nodes = num_nodes;
	  _num_edges = 0;
    
	  _delete();
    
	  _adjM = new int[num_nodes][];
	  for (int i = 0; i < num_nodes; i++)
	  {
		  _adjM[i] = new int[num_nodes];
	  }
	  _adjL = (ArrayList<Integer>[])new ArrayList[num_nodes];
	  _adjLI = (ArrayList<Integer>[])new ArrayList[num_nodes];
	  _neighbours = (ArrayList<Integer>[])new ArrayList[num_nodes];//tangible.Arrays.initializeWithDefaultClassicLinkedListInstances(num_nodes);
	  _in_ds = new int[num_nodes];
	  _out_ds = new int[num_nodes];
	  //_io_ds = (ArrayList<Integer>[])new ArrayList[num_nodes];//tangible.Arrays.initializeWithDefaultiPairInstances(num_nodes);
    
	  _in = new int[num_nodes];
	  _out = new int[num_nodes];
    
	  zero();
	}

//C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
	public void _delete()
	{
	  if (_adjM != null)
	  {
		for (int i = 0; i < _num_nodes; i++)
		{
		  if (_adjM[i] != null)
		  {
			  _adjM[i] = null;
		  }
		}
		_adjM = null;
	  }
	  if (_adjL != null)
	  {
		  _adjL = null;
	  }
	  if (_adjLI != null)
	  {
		  _adjLI = null;
	  }
	  if (_neighbours != null)
	  {
		  _neighbours = null;
	  }
	  if (_in_ds != null)
	  {
		  _in_ds = null;
	  }
	  if (_out_ds != null)
	  {
		  _out_ds = null;
	  }
	  if (_io_ds != null)
	  {
		  _io_ds = null;
	  }
	}

//C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
	public void zero()
	{
	  int i;
	  int j;
    
	  _num_edges = 0;
	  for (i = 0; i < _num_nodes;i++)
	  {
		_in[i] = 0;
		_out[i] = 0;
		_adjL[i].clear();
		_adjLI[i].clear();
		_neighbours[i].clear();
		for (j = 0; j < _num_nodes;j++)
		{
		  _adjM[i][j] = 0;
		}
	  }
    
	}

	public void addEdge(int a, int b, int c)
	{
	  Pair aux = new Pair(b,c);
	  Pair aux2 = new Pair(a,c);
    
	  if (c == 0)
		  return;
	  if (_adjM[a][b] != 0)
		  return;
    
	  _adjM[a][b] = c;
    
	  
	  //_adjL[a].push_back(aux);
    
	  
	  //_adjLI[b].push_back(aux2);
    
	  if (_adjM[b][a] == 0)
	  {
		//_neighbours[a].push_back(b);
		//_neighbours[b].push_back(a);
	  }
    
	  _out[a]++;
	  _in[b]++;
    
	  _num_edges++;
	}

//C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
	public void rmEdge(int a, int b, int c)
	{
	  Pair aux = new Pair(b,c);
	  Pair aux2 = new Pair(a,c);
    
	  _adjM[a][b] = 0;
    
	  
	  _adjL[a].remove(aux);
    
	 
	  _adjLI[b].remove(aux);
    
	  if (_adjM[b][a] == 0)
	  {
		_neighbours[a].remove(b);
		_neighbours[b].remove(a);
	  }
    
	  _out[a]--;
	  _in[b]++;
    
	  _num_edges--;
	}

	public void rmEdgesNode(int v)
	{
	  int i;
    
	  for (i = 0; i < numNodes(); i++)
	  {
		if (getEdge(i,v)!= 0)
		{
		  rmEdge(i,v,getEdge(i,v));
		}
	  }
		if (getEdge(v,i) != 0)
		{
		  rmEdge(v,i,getEdge(v,i));
		}
	}
/*
	public int[] makeOutDegreeSequence()
	{
	  int i;
	  for (i = 0; i < numNodes(); i++)
	  {
		_out_ds[i] = nodeEdges(i);
	  }
	  //sort(_out_ds, _out_ds + numNodes());
	  Arrays.sort(_out_ds);
	  return _out_ds;
	}

//C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
	public int[]  makeInDegreeSequence()
	{
	  int i;
	  for (i = 0; i < numNodes(); i++)
	  {
		_in_ds[i] = nodeInEdges(i);
	  }
	  //sort(_in_ds, _in_ds + numNodes());
	  Arrays.sort(_in_ds);
	  return _in_ds;
	}

//C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
	public Pair  makeIODegreeSequence()
	{
	  int i;
	  for (i = 0; i < numNodes(); i++)
	  {
		_io_ds[i].first = nodeInEdges(i);
		_io_ds[i].second = nodeEdges(i);
	  }
	  //sort(_io_ds, _io_ds+numNodes());
	  return _io_ds;
	}

//C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
	public int readFile(String file_name, boolean undir)
	{
	  int i;
	  FILE f = fopen(file_name.argValue, "r");
    
	  if (f == null)
	  {
		fprintf(stderr,"Could not open \"%s\"!\n",file_name.argValue);
		return 0;
	  }
    
	  int size = 0;
	  int a;
	  int b;
	  int c;
	  int max = 0;
	  ArrayList<Integer> va = new ArrayList<Integer>();
	  ArrayList<Integer> vb = new ArrayList<Integer>();
	  ArrayList<Integer> vc = new ArrayList<Integer>();
    
	  while (fscanf(f, "%d %d %d", a, b, c) == 3)
	  {
		  va.add(a);
		  vb.add(b);
		  vc.add(c);
		  if (a > max)
		  {
			  max = a;
		  }
		  if (b > max)
		  {
			  max = b;
		  }
		  size++;
	  }
    
	  fclose(f);
    
	  _createGraph(max);
    
	  System.out.printf("!! %d %d\n", max, size);
    
	  if (undir)
	  {
		for (i = 0; i < size; i++)
		{
		  if (!getEdge(va.get(i) - 1,vb.get(i) - 1))
		  {
		addEdge(va.get(i) - 1,vb.get(i) - 1,vc.get(i));
		  }
		  if (!getEdge(vb.get(i) - 1,va.get(i) - 1))
		  {
		addEdge(vb.get(i) - 1,va.get(i) - 1,vc.get(i));
		  }
		}
	  }
	  else
	  {
		for (i = 0; i < size; i++)
		{
		  if (!getEdge(va.get(i) - 1,vb.get(i) - 1))
		  {
		addEdge(va.get(i) - 1,vb.get(i) - 1,vc.get(i));
		  }
		}
	  }
    
    
	  return 1;
	}

//C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
	public void vecEdges(vEdges v)
	{
	  int i;
	  int count = 0;
	  Iterator<iPair> ii;
    
	  v.resize(numEdges());
    
	  for (i = 0; i < numNodes(); i++)
	  {
		for (ii = _adjL[i].begin(); ii.hasNext();)
		{
		  v[count].first = i;
		  v[count++].second = (ii.next()).first;
		}
	  }
    
	}

//C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
	public LinkedList<iPair> adjL(int v)
	{
	  return _adjL[v];
	}
	
	*/
}