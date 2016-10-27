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
		// À COMPLÉTER
	}

	public int getHauteur() {
		// TODO Auto-generated method stub
		return getHauteur(this.root);
	}


	private int getHauteur(RBNode<T> tree) {
		if (tree == null) {
			return 0;
		}

		// À COMPLÉTER

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
		// A MODIFIER/COMPLÉTER
		System.out.println("On veut inserer le noeud : " + X.value);
		System.out.println("go ins1");
		insertionCase1( X );
	}

	private void insertionCase1 ( RBNode<T> X )
	{
		System.out.println("***********       INS1      ***************");
		//Si c'est la racine, le noeud devient noir
		if(X.parent == null){ // Si le noeud n'a pas de parent, alors c'est forcement la racine
			X.setToBlack();
			System.out.println("La couleur de la racine (" + X.value + ") est : " + X.color);
		}
		System.out.println("go ins2");
		insertionCase2( X );
	}

	private void insertionCase2( RBNode<T> X )
	{
		System.out.println("***********       INS2      ***************");
		if(X.parent != null && X.parent.isBlack()) //Si le parent du noeud est noir, alors le noeud est bien placé donc on SORT !
			return;
		else{
			if(X.parent != null)
				System.out.println("La couleur du parent (" + X.parent.value + ") du noeud (" + X.value + ") est : " + X.parent.color);
			else
				System.out.println("Le parent du noeud (" + X.value + ") est : " + X.parent);
			
			System.out.println("go ins3");
			insertionCase3( X ); //Sinon, on teste le cas 3
		}
	}

	private void insertionCase3( RBNode<T> X )
	{
		System.out.println("***********       INS3      ***************");
		
		if(X.parent != null && X.uncle() != null && X.parent.isRed() && X.uncle().isRed()){
				X.uncle().setToBlack();
				X.parent.setToBlack();
				X.grandParent().setToRed();
				System.out.println("Le grand parent du noeud " + X.value + " est : " + X.grandParent().value);
				System.out.println("On rapelle insertionCases avec le grand parent (" + X.grandParent().value + " du noeud (" + X.value + ") pour vérifier que les conditions d'un arbre rouge noir sont respectées");
				System.out.println("go ins0");
				insertionCases(X.grandParent()); //Le grand parent doit à son tour vérifier toutes les règles
		}
		else{
			if(X.parent == null)
				System.out.println("Le parent du noeud (" + X.value + ") est : null");
			else
				System.out.println("La couleur du parent (" + X.parent.value + ") du noeud (" + X.value + ") est : " + X.parent.color);
			
			if(X.uncle() == null){}
			else
				System.out.println("La couleur de l'oncle (" + X.uncle().value + ") du noeud (" + X.value + ") est : " + X.uncle().color);

			
			System.out.println("go ins4");
			insertionCase4( X );
		}
	}

	private void insertionCase4( RBNode<T> X )
	{
		boolean rotation = false;
		
		System.out.println("***********       INS4      ***************");
		System.out.println("Parent : " + X.parent + ", Oncle : " + X.uncle());
		System.out.println("X.grandParent() : " + X.grandParent());
		//System.out.println("X.parent.leftChild.value : " + X.parent.leftChild.value);
		if(X.parent != null && X.parent.isRed()){
			if(X.uncle() != null && X.uncle().isBlack()){
				if(X.parent.leftChild != null && X.grandParent().rightChild != null){
					System.out.println("ntmé");
					System.out.println(X.parent.leftChild);
					if((X.parent.leftChild.value.compareTo(X.value) == 0) && (X.grandParent().rightChild.value.compareTo(X.parent.value)) == 0){
						System.out.println("On tourne à droite");
						rotateRight(X.parent);
						System.out.println("go ins5");
						insertionCase5( X.rightChild );
					}
				}
				
				//Pour l'insertion de 69 on devrait passer ici
				else if(X.parent.rightChild != null && X.grandParent().leftChild != null){
					System.out.println("youhouuuuuu");
					if((X.parent.rightChild.value.compareTo(X.value) == 0) && (X.grandParent().leftChild.value.compareTo(X.parent.value)) == 0){
						rotateLeft(X.parent);
						System.out.println("go ins5");
						insertionCase5( X.leftChild );
					}
				}
			}
		}
		
		if(!rotation)
			insertionCase5(X);
	}

	private void insertionCase5( RBNode<T> X )
	{
		System.out.println("***********       INS5      ***************");
		if(X.parent != null && X.uncle() != null && X.parent.isRed() && X.uncle().isBlack()){
			if((X.parent.rightChild.value.compareTo(X.value) == 0) && (X.grandParent().rightChild.value.compareTo(X.parent.value)) == 0){
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
				
				System.out.println("rotate left");
				rotateLeft(X.grandParent());
			}
			
			else if((X.parent.leftChild.value.compareTo(X.value) == 0) && (X.grandParent().leftChild.value.compareTo(X.parent.value)) == 0){
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
				
				System.out.println("rotate right");
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
		if(P.rightChild != null)
			P.rightChild.parent = P;
		
		temp = P.parent;
		P.parent = X;
		if(temp != null)
			X.parent = temp;
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
		if(P.leftChild != null)
			P.leftChild.parent = P;
		
		temp = P.parent;
		P.parent = X;
		if(temp != null)
			X.parent = temp;
		
		return; 
	}

	public void printTreePreOrder()
	{
		if(root == null)
			System.out.println( "Empty tree" );
		else
		{
			System.out.print( "PreOrdre ( ");
			printTreePreOrder( root );
			System.out.println( " )");
		}
		return;
	}

	private void printTreePreOrder( RBNode<T> P )
	{
		// A MODIFIER/COMPLÉTER
		return; 
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

	private void printTreePostOrder( RBNode<T> P )
	{
		// A MODIFIER/COMPLÉTER
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

	private void printTreeAscendingOrder( RBNode<T> P )
	{
		// A COMPLÉTER

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
		// A COMPLÉTER

	}

	public void printTreeLevelOrder()
	{
		if(root == null)
			System.out.println( "Empty tree" );
		else
		{
			System.out.print( "LevelOrdre ( ");

			Queue<RBNode<T>> q = new LinkedList<RBNode<T>>();

			q.add(root);

			//  À COMPLÉTER

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
				if(this.grandParent().leftChild.value != null && (this.grandParent().leftChild.value.compareTo(parent.value) != 0)){
					return grandParent().leftChild;
				}
				
				else if(this.grandParent().rightChild.value != null && (this.grandParent().rightChild.value.compareTo(parent.value) != 0)){
					return this.grandParent().rightChild;
				}
				
				else{
					System.out.println("Le noeud " + this.value + " a un grand parent, mais celui-ci n'a qu'un enfant, donc " + this.value + " n'a pas d'oncle");
					return null;
				}
			}
			else{
				System.out.println("Le noeud " + this.value + " n'a pas de grand parent donc PAS d'oncle");
				return null;
			}
		}

		RBNode<T> sibling()
		{
			if(parent != null){
				if(parent.leftChild.value != null && (parent.leftChild.value.compareTo(this.value) != 0))
					return parent.leftChild;
				else
					return null;
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
