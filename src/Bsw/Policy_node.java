package Bsw;

public class Policy_node {
    /* serialized */

    /* flag=1 if leaf, otherwise threshould */
    public int flag;
    /* attribute string if leaf, otherwise  and ' or */
    public String attr;

    public Policy_node left_children;
    public Policy_node right_children;
    public Policy_node father;
}
