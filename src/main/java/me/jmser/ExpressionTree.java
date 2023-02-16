package me.jmser;

public class ExpressionTree {
    private Token token;

    int value;


    private ExpressionTree left;
    private ExpressionTree right;
    private ExpressionTree parent;

    public ExpressionTree(Token e){
        this.token = e;
        this.value = token.value;
    }

    public ExpressionTree(Token e, ExpressionTree left, ExpressionTree right){
        this.token = e;
        this.left = left;
        this.right = right;
    }

    public ExpressionTree(){
        this.token = null;
    }

    public void setToken(Token e){
        this.token = e;
        this.value = token.value;
    }

    public Token getToken(){
        return this.token;
    }

    public ExpressionTree getLeft(){
        return this.left;
    }

    public ExpressionTree getRight(){
        return this.right;
    }

    public ExpressionTree getParent(){
        return this.parent;
    }

    public ExpressionTree setLeft(ExpressionTree left){
        this.left = left;
        left.setParent(this);
        return left;
    }

    public ExpressionTree setRight(ExpressionTree right){
        this.right = right;
        right.setParent(this);
        return right;
    }

    public ExpressionTree setParent(ExpressionTree parent){
        this.parent = parent;
        return parent;
    }


    public int evaluate(){
        switch(this.token.type){
            case ADD:
                return this.left.evaluate() + this.right.evaluate();
            case SUBTRACT:
                return this.left.evaluate() - this.right.evaluate();
            case MULTIPLY:
                return this.left.evaluate() * this.right.evaluate();
            case DIVIDE:
                return this.left.evaluate() / this.right.evaluate();
            case NUMBER:
                return this.value;
            default:
                return 0;
        }
    }

    
}
