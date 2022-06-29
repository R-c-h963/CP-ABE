package Main.Method;

import Bsw.*;
import Main.KeyAndParameters.PK_AA;
import Main.KeyAndParameters.PK_CTA;
import it.unisa.dia.gas.jpbc.Element;

import java.io.IOException;
import java.util.ArrayList;

public class Encrypt {

    static public int c = 1;
    static public ArrayList<Integer> v = new ArrayList<Integer>();
    static public ArrayList<Policy_node> labled_node_list = new ArrayList<Policy_node>();

    public static void main(String[] args) {

//        Policy_node root = gen_policy_tree("( ( A and B ) or ( C and D ) )");
//        Policy_node root = gen_policy_tree("( A and ( D or ( B and C ) ) )");

        String policy =  policy_pre_treatment("(A and (D or (B and C)))");
        Policy_node root = gen_policy_tree(policy);
        gen_policy_matrix(root);

        try {
            Ciphertext ciphertext=encryption("test","");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Ciphertext encryption(String message, String policy) throws Exception{
//    public static Ciphertext encryption() throws IOException{
        Ciphertext ciphertext = new Ciphertext();

        /*构建(A,ρ)*/
        ArrayList<A_map_to_P> map = new ArrayList<A_map_to_P>();
        for(int i=0;i<labled_node_list.size();i++)
        {
            A_map_to_P map_i = new A_map_to_P();
            map_i.attribute_name=labled_node_list.get(i).attr;
            map_i.attr_vector = (ArrayList<Integer>)labled_node_list.get(i).attr_vector.clone();
            map.add(map_i);
        }

        PK_CTA pk_cta = KeyLoad.load_PK_CTA("Parameters/PK_CTA");

        /*构建v*/
        ArrayList<Element> v_element = new ArrayList<Element>();
        for(int i=0;i<c;i++)
        {
            v_element.add(pk_cta.P.getZr().newRandomElement().getImmutable());

        }
        Element s = v_element.get(0).getImmutable();


        /* 构建Ax*v */
        Element omega_x = pk_cta.P.getZr().newRandomElement();
        ArrayList<Element> Omega_X = new ArrayList<Element>();
        for(int i=0;i<map.size();i++)
        {
            omega_x.setToZero();
            for(int j=0;j<map.get(i).attr_vector.size();j++)
            {
                omega_x = omega_x.add(v_element.get(j).mul(map.get(i).attr_vector.get(j)));
            }
            Omega_X.add(omega_x.getImmutable());
        }

        /*构建c_tilde*/
        Element seed = (pk_cta.Y.powZn(pk_cta.P.getZr().newRandomElement())).getImmutable();
        Element c_tilde = seed.mul(pk_cta.Y.powZn(s)).getImmutable();

        /*使用AES加密明文，秘钥种子为seed*/
        byte[] m = message.getBytes();

        byte[] cipher_text_aes = AESCoder.encrypt(seed.toBytes(), m);
//        println(cipher_text_aes);
//        byte[] plain_text_aes = AESCoder.decrypt(seed.toBytes(), cipher_text_aes);
//        String text_string = new String(plain_text_aes);



        /*构建c_hat*/
        Element Z = (pk_cta.X4.powZn(pk_cta.P.getZr().newRandomElement())).getImmutable();
        Element c_hat = ((pk_cta.g.powZn(s)).mul(Z)).getImmutable();

        /*构建c_x*/
        ArrayList<Element> C_x = new ArrayList<Element>();
          /*此处仅使用了一个PK_AA的公钥，未自动匹配与属性对应的AA,后续做测试时可能需要修改*/
        PK_AA pk_aa = KeyLoad.load_PK_AA("Parameters/SK_AA-1",pk_cta);
        Element H_j = pk_aa.H_j.getImmutable();
          /*此处仅未实现从策略中自动获取属性值，因此进行手动赋值处理*/
        ArrayList<String> attr_value = new ArrayList<String>();
        attr_value.add("1");//A=1
        attr_value.add("4");//D=4
        attr_value.add("2");//B=2
        attr_value.add("3");//C=3

        for(int i=0;i<map.size();i++)
        {
            Element Z_x = (pk_cta.X4.powZn(pk_cta.P.getZr().newRandomElement())).getImmutable();

            byte[] b = attr_value.get(i).getBytes();
            Element hash_attr_value = pk_cta.P.getZr().newElement().setFromHash(b, 0, b.length).getImmutable();
            Element g_tpx = pk_cta.g.powZn(s.mul(-1)).getImmutable();

            Element c_x = (pk_cta.g_a.powZn(Omega_X.get(i))).mul(g_tpx.mul(H_j).powZn(s.mul(-1))).mul(Z_x);
            C_x.add(c_x.getImmutable());
        }

        ciphertext.map_size = String.valueOf(map.size());
        ciphertext.attr_vector_size = String.valueOf(map.get(0).attr_vector.size());
        ciphertext.c_tilde = c_tilde;
        ciphertext.c_hat = c_hat;
        ciphertext.c_x = C_x;
        ciphertext.map = map;
        ciphertext.ciphertext = cipher_text_aes;

        byte[] ciphertext_byte;
        ciphertext_byte = SerializeUtils.serialize_Ciphertext(ciphertext);
        Common.spitFile("Parameters/User1/Ciphertext", ciphertext_byte);

        return ciphertext;
    }

    /*对访问策略进行预处理（主要是保证括号左右有空格）*/
    public static String policy_pre_treatment(String s){
        s = s.replace("(","( ");
        s = s.replace(")"," )");
        return s;
    }

    /*基于策略树构建策略矩阵*/
    public static void gen_policy_matrix(Policy_node root){
        v.add(1);
        root.attr_vector =v;
        root.label_flag=1;

        /*对策略树进行标记*/
        policy_tree_label(root);

        /*将不同长度的向量补齐至相同长度*/
        for(int i=0;i<labled_node_list.size();i++)
        {
            if(labled_node_list.get(i).attr_vector.size()<(c+1))
            {
                for(int j =0;j < c-labled_node_list.get(i).attr_vector.size();j++)
                {
                    labled_node_list.get(i).attr_vector.add(0);
                }
            }
        }

        for(int k=0;k<labled_node_list.size();k++)
        {
            println(labled_node_list.get(k).attr);
            println(labled_node_list.get(k).attr_vector);
        }
    }

    /*将全括号表达式转化为树形结构,具体实现参考https://light-of-d.blog.csdn.net/article/details/121699643*/
    public static Policy_node gen_policy_tree(String policy) {
        Policy_node root = new Policy_node();
        String[] token = policy.split(" ");

        Policy_node current = root;

        for (int i = 0; i < token.length; i++) {
            if (token[i].equals(" ")){
                continue;
            }
            if (token[i].equals("("))
            {
                current.left_children = new Policy_node();
                current.left_children.father = current;
                current = current.left_children;
            }

            else if(token[i].equals("and")||token[i].equals("or"))
            {
                current.attr = token[i];
                current.flag = 0;
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

    /*遍历整个树，并对每个节点进行操作*/
    public static void policy_tree_label(Policy_node current){
        if(current == null){
            return;
        }

        gen_policy_vector(current);

        policy_tree_label(current.left_children);
        policy_tree_label(current.right_children);
    }

    /*根据节点的类型（and/or/属性名）,选择不同的操作*/
    public static void gen_policy_vector(Policy_node current) {
        if(current.flag==0)
        {
            if(current.attr.equals("or"))
            {
                current.left_children.attr_vector = (ArrayList<Integer>)current.attr_vector.clone();
                current.left_children.label_flag = 1;
                current.right_children.attr_vector = (ArrayList<Integer>)current.attr_vector.clone();
                current.right_children.label_flag = 1;
            }

            else if(current.attr.equals("and"))
            {
                ArrayList<Integer> v_left = (ArrayList<Integer>)current.attr_vector.clone();
                v_left.add(1);

                ArrayList<Integer> v_right = new ArrayList<Integer>();
                for(int i=0;i<current.attr_vector.size();i++)
                {
                    v_right.add(0);
                }
                v_right.add(-1);

                current.left_children.attr_vector = (ArrayList<Integer>)v_left.clone();
                current.left_children.label_flag = 1;
                current.right_children.attr_vector = (ArrayList<Integer>)v_right.clone();
                current.right_children.label_flag = 1;

                c=c+1;
            }
        }

        else if(current.flag==1)
        {
            labled_node_list.add(current);
        }
    }

    private static void println(Object o) {
        System.out.println(o);
    }
}
