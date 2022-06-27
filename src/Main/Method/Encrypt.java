package Main.Method;

import Bsw.Ciphertext;
import Bsw.Policy_node;

import java.io.IOException;

public class Encrypt {

    public static void main(String[] args) {

        Policy_node root = gen_policy_tree("( ( A and B ) or C )");
        println(root.attr);
        println(root.left_children.attr);
        println(root.right_children.attr);
        println(root.left_children.left_children.attr);
        println(root.left_children.right_children.attr);
    }

    public static Ciphertext encryption(String message, String policy) {
        Ciphertext ciphertext = new Ciphertext();


        return ciphertext;
    }

    public static Policy_node gen_policy_tree(String policy) {
        Policy_node root = new Policy_node();
        String[] token = policy.split(" ");

        root.attr = "a";

        Policy_node current = root;

        for (int i = 0; i < token.length; i++) {
            if (token[i].equals("("))
            {
                current.left_children = new Policy_node();
                current.left_children.father = current;
                current = current.left_children;
            }

            else if(token[i].equals("and")||token[i].equals("or"))
            {
                current.attr = token[i];
                current.right_children = new Policy_node();
                current.right_children.father = current;
                current = current.right_children;
            }

            else if(token[i].equals(")"))
            {
                current = current.father;
            }

            else
            {
                current.attr = token[i];
                current.flag = 1;
                current = current.father;
            }
        }
        return root;
    }


    private static void println(Object o) {
        System.out.println(o);
    }
}
