package Bsw;

import java.util.ArrayList;

public class Policy_node {
    /* serialized */

    /* flag=1 if leaf, flag=0 if and/or gate */
    public int flag;

    /* flag=1 if labeled, flag=0 not */
    public int label_flag = 0;

    /*plaintext attribute name or and/or*/
    public String attr;

    /*policy matrix row vector*/
    public ArrayList<Integer> attr_vector = new ArrayList<Integer>();

    public Policy_node left_children;
    public Policy_node right_children;
    public Policy_node father;
}
