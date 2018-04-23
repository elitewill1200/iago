import java.util.ArrayList;

public class DecisionTree
{
	public static void main(String args[])
	{
		Boolean[][] input = new Boolean[4][4];
		input[0][0]=true;
		input[0][1]=false;
		OthelloBoard b = new OthelloBoard(4, input);
		Tree decisions = new Tree(b);
		decisions.branch();
		//decisions.printTree();
	}
	public static class Tree
	{
		OthelloBoard parent;
		ArrayList<Tree> children;
		public Tree(OthelloBoard p)
		{
			parent = p;
			children = new ArrayList<Tree>();
		}
		public void addChild(OthelloBoard c)
		{
			children.add(new Tree(c));
		}
		public ArrayList<Tree> getChildren()
		{
			return children;
		}
		public void branch()
		{
			parent.printBoard();
			OthelloBoard temp = null;
//			if(parent.getLegalMoves2().size()==0)
//			{
//				temp = parent.copy();
//				temp.pass();
//				addChild(temp);
//				children.get(children.size()-1).branch();
//			}
			System.out.println(parent.getLegalMoves2().size());
			for(int i = 0; i < parent.getLegalMoves2().size(); i++)
			{
				temp = parent.copy();
				temp.move(parent.getLegalMoves2().get(i)[0],parent.getLegalMoves2().get(i)[1], parent.p1Turn);
				System.out.println(parent.getLegalMoves2().size());
				addChild(temp);
				children.get(i).branch();
			}
		}
		public void printTree()
		{
			parent.printBoard();
			for(int i = 0; i < children.size(); i++)
			{
				children.get(i).printTree();
			}
		}
	}
}
