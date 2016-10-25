package BinaryTree;

public class Tree<AnyType> {

	private Node<AnyType> root = null; // Racine de l'arbre

	public void insert (AnyType elem) {
		Node<AnyType> newElem = new Node<AnyType>(elem);
		root = insert(root, newElem);
	}

	@SuppressWarnings("unchecked")
	private Node<AnyType> insert (Node<AnyType> root, Node<AnyType> newelement) {
		
		if(root == null){
			return new Node(newelement.val);
		}
		else{
			if(newelement.val.hashCode() > root.val.hashCode()){ //Le nouvel élément est dans le sous arbre de droite de la racine
				root.right = insert(root.right, newelement);
			}else if(newelement.val.hashCode() < root.val.hashCode()){ //Le nouvel élément est dans le sous arbre de gauche de la racine
				root.left = insert(root.left, newelement);
			}else{
				; //On ne fait rien, deux noeuds ne peuvent pas avoir la même valeur dans un arbre binaire de recherche
			}
		}
		
		return root;
	}

	public int getHauteur () {
		return this.getHauteur(root);
	}

	private int getHauteur(Node<AnyType> tree) {
		if( tree == null)
			return -1;
		else
			return 1 + Math.max( getHauteur( tree.left ), getHauteur( tree.right ) );
	}

	public String printTreePreOrder() {
		return "{" + this.printPreOrder(root) + "}";

	}

	private String printPreOrder(Node root) {
		String results = "";
		//Preorder = EN ORDRE : GND
		if(root == null){
			//
		}else{
			results += printPreOrder(root.left);
			results += ", (" + root.val + ")" ;
			results += printPreOrder(root.right);
		}
		return results;
	}

	private class Node<AnyType> {
		AnyType val; // Valeur du noeud
		Node right; // fils droite
		Node left; // fils gauche

		public Node (AnyType val) {
			this.val = val;
			right = null;
			left = null;
		}

	}


}

