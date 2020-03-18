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
public class BoolOperandNode extends BoolExpNode{
    
    public BoolOperandNode(char symbol) throws IllegalArgumentException {
        super(symbol);
    }

    @Override
    public boolean evaluate() {
        if(symbol == 'T') return true;
        else return false;
    }
}
