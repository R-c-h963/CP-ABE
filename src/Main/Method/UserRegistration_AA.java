package Main.Method;

import Bsw.Ciphertext_Attribute_Set;
import Bsw.Common;
import Bsw.Plaintext_Attribute_Set;
import Bsw.SerializeUtils;
import Main.KeyAndParameters.*;
import it.unisa.dia.gas.jpbc.Element;


import java.io.IOException;
import java.util.ArrayList;

import static Bsw.SerializeUtils.serializeString;

public class UserRegistration_AA {

    public static void main(String[] args) {

        Plaintext_Attribute_Set test1 = new Plaintext_Attribute_Set();
        Plaintext_Attribute_Set test2 = new Plaintext_Attribute_Set();
        test1.attribute_name="Name";
        test2.attribute_name="Sex";
        test1.attribute_value="Rch";
        test2.attribute_value="male";
        ArrayList<Plaintext_Attribute_Set> p_attr_list=new ArrayList<Plaintext_Attribute_Set>();
        p_attr_list.add(test1);
        p_attr_list.add(test2);
        try {
            PK_CTA cta_pk = KeyLoad.load_PK_CTA("Parameters/PK_CTA");
            SK_AA sk_aa = KeyLoad.load_SK_AA("Parameters/SK_AA-1",cta_pk);
            gen_sk_gid_aa(p_attr_list,sk_aa,cta_pk);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static SK_GID_AA gen_sk_gid_aa(ArrayList<Plaintext_Attribute_Set> p_attr_list, SK_AA sk_aa,PK_CTA cta_pk) throws IOException {
        SK_GID_AA sk_gid_aa = new SK_GID_AA();
        sk_gid_aa.attr_list = new ArrayList<Ciphertext_Attribute_Set>();
        /*get user's random number t*/
        Element t = load_User_t("Parameters/User1-t",cta_pk);
//        println(t);

        /*this loop is for each attribute */
        for(int i=0;i<p_attr_list.size();i++)
        {
            /*creat sk_gid_aaj_i*/
            Ciphertext_Attribute_Set sk_gid_aaj_i = new Ciphertext_Attribute_Set();
            /*get plaintext attribute name*/
            sk_gid_aaj_i.attribute_name = p_attr_list.get(i).attribute_name;
            /*map plaintext attribute value to Zn*/
            byte[] b = p_attr_list.get(i).attribute_value.getBytes();
            Element hash_attr_value = cta_pk.P.getZr().newElement().setFromHash(b, 0, b.length).getImmutable();

//            println("hash:"+hash_attr_value);

            /*randomly select R based on X2*/
            Element R = (cta_pk.X2.powZn(cta_pk.P.getZr().newRandomElement())).getImmutable();
            /*calculate ciphertext attribute value*/
            Element sk_gid_aaj_val_i = (((cta_pk.g.powZn(hash_attr_value).mul(sk_aa.h_j)).powZn(t)).mul(R)).getImmutable();
//            println("g:"+cta_pk.g);
//            Element one = (cta_pk.g.powZn(hash_attr_value));
//            println("one:"+one);
//            Element two = (one.mul(sk_aa.h_j));
//            println("two:"+two);
//            Element three = two.powZn(t);
//            println("three:"+three);
//            Element four = three.mul(R);
//            println("four:"+four);
//            println("sk_gid_aaj_i:"+sk_gid_aaj_i);
            /*creat sk_gid_aaj_i and save it to sk_gid_aaj*/
            sk_gid_aaj_i.attribute_value = sk_gid_aaj_val_i;
            sk_gid_aa.attr_list.add(sk_gid_aaj_i);
        }

        byte[] sk_gid_aa_byte;
        sk_gid_aa_byte = SerializeUtils.serialize_SK_GID_AA(sk_gid_aa);
        Common.spitFile("Parameters/User1/SK_GID_AA1", sk_gid_aa_byte);

        SK_GID_AA sk_gid_aa_test = KeyLoad.load_SK_GID_AA("Parameters/User1/SK_GID_AA1",cta_pk);

        for(int i=0;i<p_attr_list.size();i++)
        {
            println(sk_gid_aa.attr_list.get(i).attribute_name);
            println(sk_gid_aa_test.attr_list.get(i).attribute_name);
            println(sk_gid_aa.attr_list.get(i).attribute_value);
            println(sk_gid_aa_test.attr_list.get(i).attribute_value);
        }

        return sk_gid_aa;
    }

    /*get user's random number t from file*/
    public static Element load_User_t(String user_t_path,PK_CTA pk_cta) throws IOException {

        int offset = 0;
        byte[]  t_byte;
        Element t = pk_cta.P.getZr().newElement();

        t_byte = Common.suckFile(user_t_path);
        offset = SerializeUtils.unserializeElement(t_byte, offset, t);

        return t;
    };

    private static void println(Object o) {
        System.out.println(o);
    }
}
