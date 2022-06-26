package Bsw;

import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;

public class Police_node {
    /* serialized */

    /* k=1 if leaf, otherwise threshould */
    public int k;
    /* attribute string if leaf, otherwise null */
    public String attr;
    public Element c;			/* G_1 only for leaves */
    public Element cp;		/* G_1 only for leaves */
    /* array of BswabePolicy and length is 0 for leaves */
    public Police_node[] children;


    /* only used during encryption */
    public Polynomial q;

}
