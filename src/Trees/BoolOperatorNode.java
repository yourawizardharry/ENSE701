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
public class BoolOperatorNode extends BoolExpNode{
    
    public BoolOperatorNode(char symbol) {
        super(symbol);
    }

    @Override
    public boolean evaluate() {
        boolean leftoperator;
        boolean rightoperator;
        boolean result;

        if(this.symbol == '|') {
            leftoperator = this.leftChild.evaluate();
            rightoperator = this.rightChild.evaluate();
            result = rightoperator | leftoperator ;
        }
        else if(symbol == '&') {
            leftoperator = this.leftChild.evaluate();
            rightoperator = this.rightChild.evaluate();
            result = rightoperator & leftoperator ;
        }
        else if(symbol == '^') {
            leftoperator = this.leftChild.evaluate();
            rightoperator = this.rightChild.evaluate();
            result = rightoperator ^ leftoperator;
        }
        else if(symbol == '!') {
            rightoperator = this.rightChild.evaluate();
            result = !rightoperator;
        }
        else throw new IllegalArgumentException();
        
        return result;
    }
    
}
