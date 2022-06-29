package Bsw;

import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;

public class Ciphertext {
    public String map_size;//矩阵行数
    public String attr_vector_size;//矩阵列数
    public Element c_tilde;
    public Element c_hat;
    public ArrayList<A_map_to_P> map;
    public ArrayList<Element> c_x;

    public byte[] ciphertext;
}
