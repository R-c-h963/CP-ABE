package Main.Method;

import Bsw.Ciphertext;
import Bsw.Police_node;

import java.util.ArrayList;

public class Encrypt {
    public static Ciphertext encryption(String message,String policy){
        Ciphertext ciphertext = new Ciphertext();


        return ciphertext;
    }

    private static Police_node parsePolicyPostfix(String s) throws Exception {
        String[] toks;
        String tok;
        ArrayList<Police_node> stack = new ArrayList<Police_node>();
        Police_node root;

        toks = s.split(" ");

        int toks_cnt = toks.length;
        for (int index = 0; index < toks_cnt; index++) {
            int i, k, n;

            tok = toks[index];
            if (!tok.contains("of")) {
                stack.add(baseNode(1, tok));
            } else {
                Police_node node;

                /* parse kof n node */
                String[] k_n = tok.split("of");
                k = Integer.parseInt(k_n[0]);
                n = Integer.parseInt(k_n[1]);

                if (k < 1) {
                    System.out.println("error parsing " + s
                            + ": trivially satisfied operator " + tok);
                    return null;
                } else if (k > n) {
                    System.out.println("error parsing " + s
                            + ": unsatisfiable operator " + tok);
                    return null;
                } else if (n == 1) {
                    System.out.println("error parsing " + s
                            + ": indentity operator " + tok);
                    return null;
                } else if (n > stack.size()) {
                    System.out.println("error parsing " + s
                            + ": stack underflow at " + tok);
                    return null;
                }

                /* pop n things and fill in children */
                node = baseNode(k, null);
                node.children = new Police_node[n];

                for (i = n - 1; i >= 0; i--)
                    node.children[i] = stack.remove(stack.size() - 1);

                /* push result */
                stack.add(node);
            }
        }

        if (stack.size() > 1) {
            System.out.println("error parsing " + s
                    + ": extra node left on the stack");
            return null;
        } else if (stack.size() < 1) {
            System.out.println("error parsing " + s + ": empty policy");
            return null;
        }

        root = stack.get(0);
        return root;
    }

    private static Police_node baseNode(int k, String s) {
        Police_node p = new Police_node();

        p.k = k;
        if (!(s == null))
            p.attr = s;
        else
            p.attr = null;
        p.q = null;
        return p;
    }
}
