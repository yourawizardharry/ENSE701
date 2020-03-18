/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Trees;

/**
 *
 * @author harry
 */
public abstract class BoolExpNode {
    protected char symbol;
    public BoolExpNode leftChild, rightChild;
    
    public BoolExpNode(char symbol) {
        this.symbol = symbol;
    }
    
    public abstract boolean evaluate();
    
    @Override
    public String toString() {
        return symbol +"";
    }
}
