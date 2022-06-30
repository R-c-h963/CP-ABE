package Bsw;

import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;

public class Ciphertext {
    public String map_size;//矩阵行数
    public String attr_vector_size;//矩阵列数

    public Element c_tilde;//c_tilde
    public Element c_hat;//c_hat

    public ArrayList<A_map_to_P> map;//用于说明属性名与行向量的映射关系
    public ArrayList<Element> c_x;//c_x

    public byte[] ciphertext;//AES加密的明文内容
}
