package Trees;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 *
 * @author sehall
 */
public class BETreeGUI extends JPanel implements ActionListener {

    private final JButton evaluateButton, buildTreeButton;

    private DrawPanel drawPanel;
    private BoolExpNode root;
    private int numberNodes = 0;

    private JTextField postFixField;
    public static int PANEL_H = 500;
    public static int PANEL_W = 700;
    private JLabel nodeCounterLabel;

    public BETreeGUI() {
        super(new BorderLayout());
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        root = null;
        super.setPreferredSize(new Dimension(PANEL_W, PANEL_H + 30));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(PANEL_W, 30));
        drawPanel = new DrawPanel();

        evaluateButton = new JButton("Evaluate to infix");
        buildTreeButton = new JButton("Build Expression Tree");

        evaluateButton.addActionListener((ActionListener) this);
        buildTreeButton.addActionListener((ActionListener) this);

        postFixField = new JTextField(40);

        buttonPanel.add(postFixField);
        buttonPanel.add(buildTreeButton);
        buttonPanel.add(evaluateButton);

        super.add(drawPanel, BorderLayout.CENTER);
        super.add(buttonPanel, BorderLayout.SOUTH);

        nodeCounterLabel = new JLabel("Number of Nodes: " + numberNodes);
        super.add(nodeCounterLabel, BorderLayout.NORTH);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();

        if (source == evaluateButton) {   //finish this button event to handle the evaluation and output to infix of the tree 
            if (root == null) {
                JOptionPane.showMessageDialog(this, "Tree is null, not built", "INFO",
                        JOptionPane.ERROR_MESSAGE);
            } else {

                String infixString = toInfixString(root);
                
                String result;
                if(root.evaluate()) result = "true";
                else result = "false";
                
                if (root != null) {
                    JOptionPane.showMessageDialog(this, infixString + " = " + result, "INFO" ,
                            JOptionPane.ERROR_MESSAGE);
                }
            }

        } else if (source == buildTreeButton && postFixField.getText() != null) {   //Finish this button event and below methods to handle the building of the tree
            try {
                root = buildExpressionTree(postFixField.getText().toCharArray());
                
                nodeCounterLabel.setText("Number of Nodes: " + numberNodes);
                
            } catch (ArithmeticException e) {
                JOptionPane.showMessageDialog(this, "Not a valid postfix expression", "INFO",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        drawPanel.repaint();
    }

    public BoolExpNode buildExpressionTree(char[] postfix) throws ArithmeticException {
        Stack<BoolExpNode> stack = new Stack();
        BoolExpNode topNode = null;
        numberNodes = 0;
        
        for(int i = 0; i < postfix.length; ++i) {
            if(postfix[i] == 'T' || postfix[i] == 'F') {
                stack.push(new BoolOperandNode(postfix[i]));
                ++numberNodes;
            }
            else if(postfix[i] =='!' || postfix[i] == '|' || postfix[i] == '&' || postfix[i] == '^'){
                BoolOperatorNode tempNode = new BoolOperatorNode(postfix[i]);
                topNode = tempNode;
                if(stack.isEmpty()) throw new ArithmeticException();
                tempNode.rightChild = stack.pop();
                if(postfix[i] != '!') {
                    if(stack.isEmpty()) throw new ArithmeticException();
                    tempNode.leftChild = stack.pop();
                }
                 
                stack.push(tempNode);
                ++numberNodes;
            }
            else throw new ArithmeticException();
            
        }
        return topNode;
    }

    public String toInfixString(BoolExpNode node) {
        String str = "";
        
        if(node!=null) {
            if(node instanceof BoolOperatorNode && node.symbol!='!') str+="(";
            
            if(node.leftChild instanceof BoolOperatorNode) {
                str+=toInfixString(node.leftChild);
            }
            else if(node.leftChild instanceof BoolOperandNode) str+=node.leftChild.symbol;
            
            str+=node.symbol;
            
            if(node.rightChild instanceof BoolOperatorNode) str+=toInfixString(node.rightChild);
            else if(node.rightChild instanceof BoolOperandNode) str+=node.rightChild.symbol;
            
            
            if(node instanceof BoolOperatorNode && node.symbol!='!') str+=")";
        }
        
        
        return str;
    }
    //DO NOT TOUCH THIS INNER CLASS!!!!!!!!!!!!!
    private class DrawPanel extends JPanel {

        public DrawPanel() {
            super();
            super.setBackground(Color.WHITE);
            super.setPreferredSize(new Dimension(PANEL_W, PANEL_H));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (root != null) {
                drawTree(g, getWidth());
            }
        }

        public void drawTree(Graphics g, int width) {
            int nodeCount = 0 - (numberNodes / 2);
            drawNode(g, root, width / 2, 0, nodeCount, new HashMap<BoolExpNode, Point>());
        }

        private int drawNode(Graphics g, BoolExpNode current,
                int x, int level, int nodeCount, Map<BoolExpNode, Point> map) {
            int RECT = 40;

            if (current.leftChild != null) {
                nodeCount = drawNode(g, current.leftChild, x, level + 1, nodeCount, map);
            }

            int currentX = x + nodeCount * RECT;
            int currentY = level * 2 * RECT + RECT;
            nodeCount++;
            map.put(current, new Point(currentX, currentY));

            if (current.rightChild != null) {
                nodeCount = drawNode(g, current.rightChild, x, level + 1, nodeCount, map);
            }

            g.setColor(Color.red);
            if (current.leftChild != null) {
                Point leftPoint = map.get(current.leftChild);
                g.drawLine(currentX, currentY, leftPoint.x, leftPoint.y - RECT / 2);
            }
            if (current.rightChild != null) {
                Point rightPoint = map.get(current.rightChild);
                g.drawLine(currentX, currentY, rightPoint.x, rightPoint.y - RECT / 2);

            }
            if (current instanceof BoolOperandNode) {
                g.setColor(Color.WHITE);
            } else {
                g.setColor(Color.YELLOW);
            }

            Point currentPoint = map.get(current);
            g.fillRect(currentPoint.x - RECT / 2, currentPoint.y - RECT / 2, RECT, RECT);
            g.setColor(Color.BLACK);
            g.drawRect(currentPoint.x - RECT / 2, currentPoint.y - RECT / 2, RECT, RECT);
            Font f = new Font("courier new", Font.BOLD, 16);
            g.setFont(f);
            g.drawString(current.toString(), currentPoint.x - 3, currentPoint.y);
            return nodeCount;

        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Boolean Expression Tree builder and evaluator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new BETreeGUI());
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        int screenHeight = dimension.height;
        int screenWidth = dimension.width;
        frame.pack();             //resize frame apropriately for its content
        //positions frame in center of screen
        frame.setLocation(new Point((screenWidth / 2) - (frame.getWidth() / 2),
                (screenHeight / 2) - (frame.getHeight() / 2)));
        frame.setVisible(true);
    }
}
