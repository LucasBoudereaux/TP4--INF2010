package RougeNoir;
import java.util.LinkedList;
import java.util.Queue;

public class RedBlackTree<T extends Comparable<? super T> > 
{
	private RBNode<T> root;  // Racine de l'arbre

	enum ChildType{ left, right }

	public RedBlackTree()
	{ 
		root = null;
	}

	public void printFancyTree()
	{
		printFancyTree( root, "", ChildType.right);
	}

	private void printFancyTree( RBNode<T> t, String prefix, ChildType myChildType)
	{
		System.out.print( prefix + "|__"); // un | et trois _

		if( t != null )
		{
			boolean isLeaf = (t.isNil()) || ( t.leftChild.isNil() && t.rightChild.isNil() );

			System.out.println( t );
			String _prefix = prefix;

			if( myChildType == ChildType.left )
				_prefix += "|   "; // un | et trois espaces
				else
					_prefix += "   " ; // trois espaces

			if( !isLeaf )
			{
				printFancyTree( t.leftChild, _prefix, ChildType.left );
				printFancyTree( t.rightChild, _prefix, ChildType.right );
			}
		}
		else
			System.out.print("null\n");
	}

	
	public T find(int key)
	{
		return find(root, key);
	}

	
	private T find(RBNode<T> current, int key)
	{
		if(current.value != null){
			if (current.value.hashCode() == key){
				return current.value;
			}
			else if (key > current.value.hashCode())
				return find(current.rightChild, key);	
			else
				return find(current.leftChild, key);
		}
		return null;
	}

	public int getHauteur() {
		// TODO Auto-generated method stub
		return getHauteur(this.root);
	}


	private int getHauteur(RBNode<T> tree) {
		if( tree.isNil())
			return 0;
		else
			return 1 + Math.max( getHauteur( tree.leftChild ), getHauteur( tree.rightChild ) );
	}
	

	public void insert(T val)
	{
		insertNode( new RBNode<T>( val ) );
	}

	private void insertNode( RBNode<T> newNode )
	{ 
		if (root == null)  // Si arbre vide
			root = newNode;
		else
		{
			RBNode<T> position = root; // On se place a la racine

			while( true ) // on insere le noeud (ABR standard)
			{
				int newKey = newNode.value.hashCode();
				int posKey = position.value.hashCode();

				if ( newKey < posKey ) 
				{
					if ( position.leftChild.isNil() ) 
					{
						position.leftChild = newNode;
						newNode.parent = position;
						break;
					} 
					else 
						position = position.leftChild;
				} 
				else if ( newKey > posKey ) 
				{
					if ( position.rightChild.isNil() )
					{
						position.rightChild = newNode;
						newNode.parent = position;
						break;
					}
					else 
						position = position.rightChild;
				}
				else // pas de doublons
				return;
			}
		}

		insertionCases( newNode );
	}

	private void insertionCases( RBNode<T> X )
	{
		insertionCase1( X );
	}

	private void insertionCase1 ( RBNode<T> X )
	{
		//Si c'est la racine, le noeud devient noir
		if(X.parent == null){ // Si le noeud n'a pas de parent, alors c'est forcement la racine
			X.setToBlack();
		}
		insertionCase2( X );
	}

	private void insertionCase2( RBNode<T> X )
	{
		if(X.parent != null && X.parent.isBlack()) //Si le parent du noeud est noir, alors le noeud est bien placé donc on SORT !
			return;

		insertionCase3( X ); //Sinon, on teste le cas 3
	}

	private void insertionCase3( RBNode<T> X )
	{
		
		if(X.parent != null && X.uncle() != null && X.parent.isRed() && X.uncle().isRed()){
				X.uncle().setToBlack();
				X.parent.setToBlack();
				X.grandParent().setToRed();
				insertionCases(X.grandParent()); //Le grand parent doit à son tour vérifier toutes les règles
		}
		else{
				insertionCase4( X );
		}
	}

	private void insertionCase4( RBNode<T> X )
	{
		
		boolean rotation = false;
		
		if(X.parent != null && X.parent.isRed()){
			if(X.uncle().isBlack()){
				if(X.parent.leftChild.value != null && X.grandParent().rightChild.value != null){
					if((X.parent.leftChild.value.compareTo(X.value) == 0) && (X.grandParent().rightChild.value.compareTo(X.parent.value)) == 0){
						rotateRight(X.parent);
						insertionCase5( X.rightChild );
					}
				}
				
				//Pour l'insertion de 69 on devrait passer ici
				else if(X.parent.rightChild.value != null && X.grandParent().leftChild.value != null){
					if((X.parent.rightChild.value.compareTo(X.value) == 0) && (X.grandParent().leftChild.value.compareTo(X.parent.value)) == 0){
						rotateLeft(X.parent);
						insertionCase5( X.leftChild);
					}
				}
			}
		}
	}

	private void insertionCase5( RBNode<T> X )
	{
		
		if(X.parent != null  && X.parent.isRed() && X.uncle().isBlack()){
			if((X.parent.rightChild == X) && (X.grandParent().rightChild == X.parent)){
				if(X.grandParent().isBlack()){
					X.grandParent().setToRed();
				}else{
					X.grandParent().setToBlack();
				}
				
				if(X.parent.isBlack()){
					X.parent.setToRed();
				}else{
					X.parent.setToBlack();
				}
				
				rotateLeft(X.grandParent());
			}
			
			else if((X.parent.leftChild == X) && (X.grandParent().leftChild == X.parent)){
				if(X.grandParent().isBlack()){
					X.grandParent().setToRed();
				}else{
					X.grandParent().setToBlack();
				}
				
				if(X.parent.isBlack()){
					X.parent.setToRed();
				}else{
					X.parent.setToBlack();
				}
				
				rotateRight(X.grandParent());
			}
		}
		
		return; 
	}

	private void rotateLeft( RBNode<T> R )
	{
		
		RBNode<T> X = R.rightChild;
		RBNode<T> P = R;
		RBNode<T> temp;
		
		temp = X.leftChild;
		X.leftChild = P;
		P.rightChild = temp;
		P.rightChild.parent = P;
		
		temp = P.parent;
		P.parent = X;
		if(temp != null){
			X.parent = temp;
			if(temp.leftChild.value.compareTo(P.value) == 0)
				X.parent.leftChild = X;
			else
				X.parent.rightChild = X;
		}
		return; 
	}

	private void rotateRight( RBNode<T> R )
	{
		System.out.println("rotate right");
		RBNode<T> X = R.leftChild;
		RBNode<T> P = R;
		RBNode<T> temp;
		
		temp = X.rightChild;
		X.rightChild = P;
		P.leftChild = temp;
		P.leftChild.parent = P;
		
		temp = P.parent;
		P.parent = X;
		
		if(temp != null){
			X.parent = temp;
			if(temp.leftChild.value.compareTo(P.value) == 0)
				X.parent.leftChild = X;
			else
				X.parent.rightChild = X;
		}
		return; 
	}

	public void printTreePreOrder()
	{
		if(root == null)
			System.out.println( "Empty tree" );
		else
		{
			System.out.print( "PreOrdre ( ");
			//System.out.println(root);
			printTreePreOrder( root );
			System.out.println( " )");
		}
		return;
	}

	private void printTreePreOrder( RBNode<T> P ) //NGD
	{
		if(P == null){
			//
		}else{
			if(P.value != null){
				System.out.print("{" + P + "},");
			}
			printTreePreOrder(P.leftChild);
			printTreePreOrder(P.rightChild);
		}
	}

	public void printTreePostOrder()
	{
		if(root == null)
			System.out.println( "Empty tree" );
		else
		{
			System.out.print( "PostOrdre ( ");
			printTreePostOrder( root );
			System.out.println( ")");
		}
		return;
	}

	private void printTreePostOrder( RBNode<T> P ) //GDN
	{
		if(P == null){
			//
		}else{
			printTreePostOrder(P.leftChild);
			printTreePostOrder(P.rightChild);
			if(P.value != null){
				System.out.print("{" + P + "},");
			}
		}
		return; 
	}


	public void printTreeAscendingOrder()
	{
		if(root == null)
			System.out.println( "Empty tree" );
		else
		{
			System.out.print( "AscendingOrdre ( ");
			printTreeAscendingOrder( root );
			System.out.println( " )");
		}
		return;
	}

	private void printTreeAscendingOrder( RBNode<T> P )//GND
	{
		if(P == null){
			//
		}else{
			printTreeAscendingOrder(P.leftChild);
			if(P.value != null){
				System.out.print("{" + P + "},");
			}
			printTreeAscendingOrder(P.rightChild);
		}
		return;

	}


	public void printTreeDescendingOrder()
	{
		if(root == null)
			System.out.println( "Empty tree" );
		else
		{
			System.out.print( "DescendingOrdre ( ");
			printTreeDescendingOrder( root );
			System.out.println( " )");
		}
		return;
	}

	private void printTreeDescendingOrder( RBNode<T> P )
	{
		if(P == null){
			//
		}else{
			printTreeDescendingOrder(P.rightChild);
			if(P.value != null){
				System.out.print("{" + P + "},");
			}
			printTreeDescendingOrder(P.leftChild);
		}
		return;

	}

	@SuppressWarnings("unchecked")
	public void printTreeLevelOrder()
	{
		if(root == null)
			System.out.println( "Empty tree" );
		else
		{
			System.out.print( "LevelOrdre ( ");

			Queue<RBNode<T>> q = new LinkedList<RBNode<T>>();

			q.add(root);
			
			while (!q.isEmpty()) 
	        {
				RBNode tempNode = q.poll(); // poll() permet de retirer le noeud courant dans la liste et renvoie ce noeud
	            System.out.print("{" + tempNode+ " },");
	 
	            if (tempNode.leftChild.value != null) { //On vérifie que l'on a pas un NIL pour pas l'afficher !
	                q.add(tempNode.leftChild);
	            }
	 
	            if (tempNode.rightChild.value != null) {
	                q.add(tempNode.rightChild);
	            }
	        }

			System.out.println( " )");
		}
		return;
	}

	private static class RBNode<T extends Comparable<? super T> > 
	{
		enum RB_COLOR { BLACK, RED }  // Couleur possible

		RBNode<T>  parent;      // Noeud parent
		RBNode<T>  leftChild;   // Feuille gauche
		RBNode<T>  rightChild;  // Feuille droite
		RB_COLOR   color;       // Couleur du noeud
		T          value;       // Valeur du noeud

		// Constructeur (NIL)   
		RBNode() { setToBlack(); }

		// Constructeur (feuille)   
		RBNode(T val)
		{
			setToRed();
			value = val;
			leftChild = new RBNode<T>();
			leftChild.parent = this;
			rightChild = new RBNode<T>();
			rightChild.parent = this;
		}

		RBNode<T> grandParent()
		{
			if(parent != null && parent.parent != null)
				return parent.parent;
			else
				return null;
		}

		RBNode<T> uncle()
		{
			
			if(this.grandParent() != null ){
				return parent.sibling();
			}
			else{
				//System.out.println("Le noeud " + this.value + " n'a pas de grand parent donc PAS d'oncle");
				return null;
			}
		}

		RBNode<T> sibling()
		{
			if(parent != null){
				if(parent.leftChild.value == null )
					return parent.leftChild;
				else if(parent.leftChild.value.compareTo(this.value) != 0)
					return parent.leftChild;
				else
					return parent.rightChild;
			}
			else
				return null;
		}

		public String toString()
		{
			return value + " (" + (color == RB_COLOR.BLACK ? "black" : "red") + ")"; 
		}

		boolean isBlack(){ return (color == RB_COLOR.BLACK); }
		boolean isRed(){ return (color == RB_COLOR.RED); }
		boolean isNil(){ return (leftChild == null) && (rightChild == null); }

		void setToBlack(){ color = RB_COLOR.BLACK; }
		void setToRed(){ color = RB_COLOR.RED; }
	}
}
