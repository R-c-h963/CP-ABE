package Bsw;

import Main.KeyAndParameters.*;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.parameters.PropertiesParameters;

import java.util.ArrayList;

public class SerializeUtils {
    /*	序列化Element*/
    public static void serializeElement(ArrayList<Byte> arrlist, Element e) {
        byte[] arr_e = e.toBytes();
        serializeUint32(arrlist, arr_e.length);
        byteArrListAppend(arrlist, arr_e);
    }

    /*	反序列化Element*/
    public static int unserializeElement(byte[] arr, int offset, Element e) {
        int len;
        int i;
        byte[] e_byte;

        len = unserializeUint32(arr, offset);
        e_byte = new byte[(int) len];
        offset += 4;
        for (i = 0; i < len; i++)
            e_byte[i] = arr[offset + i];
        e.setFromBytes(e_byte);

        return (int) (offset + len);
    }

    /*	序列化String*/
    public static void serializeString(ArrayList<Byte> arrlist, String s) {
        byte[] b = s.getBytes();
        serializeUint32(arrlist, b.length);
        byteArrListAppend(arrlist, b);
    }

    /*	反序列化String*/
    public static int unserializeString(byte[] arr, int offset, StringBuffer sb) {
        int i;
        int len;
        byte[] str_byte;

        len = unserializeUint32(arr, offset);
        offset += 4;
        str_byte = new byte[len];
        for (i = 0; i < len; i++)
            str_byte[i] = arr[offset + i];

        sb.append(new String(str_byte));
        return offset + len;
    }

    /*	序列化 PK_CTA*/
    public static byte[] serialize_PK_CTA(PK_CTA pk) {
        ArrayList<Byte> arrlist = new ArrayList<Byte>();

        serializeString(arrlist, pk.N);
        serializeElement(arrlist, pk.g);
        serializeElement(arrlist, pk.g_a);
        serializeElement(arrlist, pk.X2);
        serializeElement(arrlist, pk.X4);
        serializeElement(arrlist, pk.Y);

        return Byte_arr2byte_arr(arrlist);
    }

    /*	反序列化 PK_CTA*/
    public static PK_CTA unserialize_PK_CTA(byte[] b) {
        int offset = 0;
        PK_CTA pk = new PK_CTA();

        StringBuffer sb = new StringBuffer("");

        PropertiesParameters curveParams = new PropertiesParameters();
        PairingParameters typeA1Params = curveParams.load("Parameters/a1.properties");
        Pairing pairing = PairingFactory.getPairing(typeA1Params);

        pk.P = pairing;
        pk.g = pairing.getG1().newElement();
        pk.g_a = pairing.getG1().newElement();
        pk.X2 = pairing.getG1().newElement();
        pk.X4 = pairing.getG1().newElement();
        pk.Y = pairing.getGT().newElement();

        offset = unserializeString(b, offset, sb);
        pk.N = sb.substring(0);

        offset = unserializeElement(b, offset, pk.g);
        offset = unserializeElement(b, offset, pk.g_a);
        offset = unserializeElement(b, offset, pk.X2);
        offset = unserializeElement(b, offset, pk.X4);
        offset = unserializeElement(b, offset, pk.Y);

        return pk;
    }

    /*	序列化 SK_CTA*/
    public static byte[] serialize_SK_CTA(SK_CTA sk) {
        ArrayList<Byte> arrlist = new ArrayList<Byte>();

        serializeElement(arrlist, sk.a);
        serializeElement(arrlist, sk.alpha);
        serializeElement(arrlist, sk.X3);

        return Byte_arr2byte_arr(arrlist);
    }

    /*	反序列化 SK_CTA*/
    public static SK_CTA unserialize_SK_CTA(PK_CTA pk_cta, byte[] b) {
        int offset = 0;
        SK_CTA sk = new SK_CTA();

        sk.a = pk_cta.P.getZr().newElement();
        sk.alpha = pk_cta.P.getZr().newElement();
        sk.X3 = pk_cta.P.getG1().newElement();

        offset = unserializeElement(b, offset, sk.a);
        offset = unserializeElement(b, offset, sk.alpha);
        offset = unserializeElement(b, offset, sk.X3);

        return sk;
    }

    /*序列化 PK_AA*/
    public static byte[] serialize_PK_AA(PK_AA aa_pk) {
        ArrayList<Byte> arrlist = new ArrayList<Byte>();

        serializeElement(arrlist, aa_pk.H_j);
        return Byte_arr2byte_arr(arrlist);
    }

    /*反序列化 PK_AA*/
    public static PK_AA unserialize_PK_AA(PK_CTA pk_cta, byte[] b) {
        int offset = 0;
        PK_AA aa_pk = new PK_AA();

        aa_pk.H_j = pk_cta.P.getG1().newElement();
        offset = unserializeElement(b, offset, aa_pk.H_j);

        return aa_pk;
    }

    /*序列化 SK_AA*/
    public static byte[] serialize_SK_AA(SK_AA sk) {
        ArrayList<Byte> arrlist = new ArrayList<Byte>();

        serializeElement(arrlist, sk.h_j);
        serializeElement(arrlist, sk.Z_j);

        return Byte_arr2byte_arr(arrlist);
    }

    /*反序列化 SK_AA*/
    public static SK_AA unserialize_SK_AA(PK_CTA pk_cta, byte[] b) {
        int offset = 0;
        SK_AA sk = new SK_AA();

        sk.h_j = pk_cta.P.getG1().newElement();
        sk.Z_j = pk_cta.P.getG1().newElement();


        offset = unserializeElement(b, offset, sk.h_j);
        offset = unserializeElement(b, offset, sk.Z_j);

        return sk;
    }

    /*序列化SK_GID_CTA*/
    public static byte[] serialize_SK_GID_CTA(SK_GID_CTA sk_gid_cta) {
        ArrayList<Byte> arrlist = new ArrayList<Byte>();

        serializeString(arrlist, sk_gid_cta.Gid);
        serializeElement(arrlist, sk_gid_cta.K);
        serializeElement(arrlist, sk_gid_cta.K_);

        return Byte_arr2byte_arr(arrlist);
    }

    /*反序列化 SK_GID_CTA*/
    public static SK_GID_CTA unserialize_SK_GID_CTA(PK_CTA pk_cta, byte[] b) {
        int offset = 0;
        SK_GID_CTA sk = new SK_GID_CTA();

        sk.K = pk_cta.P.getG1().newElement();
        sk.K_ = pk_cta.P.getG1().newElement();

        StringBuffer sb = new StringBuffer("");
        offset = unserializeString(b, offset, sb);
        sk.Gid = sb.substring(0);

        offset = unserializeElement(b, offset, sk.K);
        offset = unserializeElement(b, offset, sk.K_);

        return sk;
    }

    /*序列化SK_GID_AA*/
    public static byte[] serialize_SK_GID_AA(SK_GID_AA sk_gid_aa) {
        ArrayList<Byte> arrlist = new ArrayList<Byte>();

        for(int i=0;i<sk_gid_aa.attr_list.size();i++)
        {
            serializeString(arrlist, sk_gid_aa.attr_list.get(i).attribute_name);
            serializeElement(arrlist, sk_gid_aa.attr_list.get(i).attribute_value);
        }

        return Byte_arr2byte_arr(arrlist);
    }

    /*反序列化 SK_GID_AA*/
    public static SK_GID_AA unserialize_SK_GID_AA(PK_CTA pk_cta, byte[] b) {
        int offset = 0;
        SK_GID_AA sk = new SK_GID_AA();
        sk.attr_list = new ArrayList<Ciphertext_Attribute_Set>();


        while(offset<b.length)
        {
            Ciphertext_Attribute_Set sk_gid_aaj_i = new Ciphertext_Attribute_Set();
            String attr_name= new String();
            Element attr_val = pk_cta.P.getG1().newElement();

            StringBuffer sb = new StringBuffer("");
            offset = unserializeString(b, offset, sb);
            attr_name = sb.substring(0);

            offset = unserializeElement(b, offset, attr_val);
            sk_gid_aaj_i.attribute_name=attr_name;
            sk_gid_aaj_i.attribute_value=attr_val.getImmutable();
            sk.attr_list.add(sk_gid_aaj_i);
        }

        return sk;
    }

    /*序列化SK_GID*/
    public static byte[] serialize_SK_GID(SK_GID sk_gid) {
        ArrayList<Byte> arrlist = new ArrayList<Byte>();


        serializeElement(arrlist, sk_gid.K);
        serializeElement(arrlist, sk_gid.K_);
        serializeElement(arrlist, sk_gid.z);

        for(int i=0;i<sk_gid.attr_list.size();i++)
        {
            serializeString(arrlist, sk_gid.attr_list.get(i).attribute_name);
            serializeElement(arrlist, sk_gid.attr_list.get(i).attribute_value);
        }

        return Byte_arr2byte_arr(arrlist);
    }

    /*反序列化 SK_GID*/
    public static SK_GID unserialize_SK_GID(PK_CTA pk_cta, byte[] b) {
        int offset = 0;
        SK_GID sk = new SK_GID();
        sk.K = pk_cta.P.getG1().newElement();
        sk.K_ = pk_cta.P.getG1().newElement();
        sk.z = pk_cta.P.getZr().newElement();

        sk.attr_list = new ArrayList<Ciphertext_Attribute_Set>();
        offset = unserializeElement(b, offset, sk.K);
        offset = unserializeElement(b, offset, sk.K_);
        offset = unserializeElement(b, offset, sk.z);
        while(offset<b.length)
        {
            Ciphertext_Attribute_Set sk_gid_aaj_i = new Ciphertext_Attribute_Set();
            String attr_name= new String();
            Element attr_val = pk_cta.P.getG1().newElement();

            StringBuffer sb = new StringBuffer("");
            offset = unserializeString(b, offset, sb);
            attr_name = sb.substring(0);

            offset = unserializeElement(b, offset, attr_val);
            sk_gid_aaj_i.attribute_name=attr_name;
            sk_gid_aaj_i.attribute_value=attr_val.getImmutable();
            sk.attr_list.add(sk_gid_aaj_i);
        }

        return sk;
    }

    /*序列化TK_GID*/
    public static byte[] serialize_TK_GID(TK_GID tk_gid) {
        ArrayList<Byte> arrlist = new ArrayList<Byte>();


        serializeElement(arrlist, tk_gid.K);
        serializeElement(arrlist, tk_gid.K_);

        for(int i=0;i<tk_gid.attr_list.size();i++)
        {
            serializeString(arrlist, tk_gid.attr_list.get(i).attribute_name);
            serializeElement(arrlist, tk_gid.attr_list.get(i).attribute_value);
        }

        return Byte_arr2byte_arr(arrlist);
    }

    /*反序列化 TK_GID*/
    public static TK_GID unserialize_TK_GID(PK_CTA pk_cta, byte[] b) {
        int offset = 0;
        TK_GID tk = new TK_GID();
        tk .K = pk_cta.P.getG1().newElement();
        tk.K_ = pk_cta.P.getG1().newElement();

        tk.attr_list = new ArrayList<Ciphertext_Attribute_Set>();
        offset = unserializeElement(b, offset, tk.K);
        offset = unserializeElement(b, offset, tk.K_);

        while(offset<b.length)
        {
            Ciphertext_Attribute_Set tk_gid_aaj_i = new Ciphertext_Attribute_Set();
            String attr_name= new String();
            Element attr_val = pk_cta.P.getG1().newElement();

            StringBuffer sb = new StringBuffer("");
            offset = unserializeString(b, offset, sb);
            attr_name = sb.substring(0);

            offset = unserializeElement(b, offset, attr_val);
            tk_gid_aaj_i.attribute_name=attr_name;
            tk_gid_aaj_i.attribute_value=attr_val.getImmutable();
            tk.attr_list.add(tk_gid_aaj_i);
        }

        return tk;
    }

    /*序列化 Ciphertext*/
    public static byte[] serialize_Ciphertext(Ciphertext ciphertext) {
        ArrayList<Byte> arrlist = new ArrayList<Byte>();

        serializeString(arrlist, ciphertext.map_size);
        serializeString(arrlist, ciphertext.attr_vector_size);
        serializeElement(arrlist, ciphertext.c_tilde);
        serializeElement(arrlist, ciphertext.c_hat);

        /*通过循环依次序列化map数组中内容*/
        /*因为A_map_to_P类中存在数组，因此要嵌套循环*/
        for(int i=0;i<Integer.valueOf(ciphertext.map_size);i++)
        {
            serializeString(arrlist, ciphertext.map.get(i).attribute_name);
            for (int j=0;j<Integer.valueOf(ciphertext.attr_vector_size);j++)
            {
                serializeString(arrlist, ciphertext.map.get(i).attr_vector.get(j).toString());
            }
        }

        /*通过循环依次序列化c_x数组中内容*/
        for(int i=0;i<Integer.valueOf(ciphertext.map_size);i++)
        {
            serializeElement(arrlist, ciphertext.c_x.get(i));
        }

        serializeUint32(arrlist, ciphertext.ciphertext.length);
        byteArrListAppend(arrlist, ciphertext.ciphertext);

        return Byte_arr2byte_arr(arrlist);
    }

    /*反序列化 Ciphertext*/
    public static Ciphertext unserialize_Ciphertext(PK_CTA pk_cta, byte[] b) {
        int offset = 0;
        Ciphertext ciphertext = new Ciphertext();

        /* 反序列化两个String，用于对后续数组的反序列化 */
        StringBuffer map_size_sb = new StringBuffer("");
        StringBuffer attr_vector_sb = new StringBuffer("");
        offset = unserializeString(b, offset, map_size_sb);
        offset = unserializeString(b, offset, attr_vector_sb);
        ciphertext.map_size = map_size_sb.substring(0);
        ciphertext.attr_vector_size = attr_vector_sb.substring(0);

        /* 反序列化两个Element */
        ciphertext.c_tilde= pk_cta.P.getGT().newElement();
        ciphertext.c_hat= pk_cta.P.getG1().newElement();
        offset = unserializeElement(b, offset, ciphertext.c_tilde);
        offset = unserializeElement(b, offset, ciphertext.c_hat);

        /* 反序列化两个数组 */
        ciphertext.map = new ArrayList<A_map_to_P>();
        ciphertext.c_x = new ArrayList<Element>();

        for(int i=0;i<Integer.valueOf(ciphertext.map_size);i++)
        {
            A_map_to_P a_map_to_p =new A_map_to_P();

            StringBuffer attribute_name_i_sb = new StringBuffer("");
            offset = unserializeString(b, offset, attribute_name_i_sb);
            a_map_to_p.attribute_name = attribute_name_i_sb.substring(0);
            for (int j=0;j<Integer.valueOf(ciphertext.attr_vector_size);j++)
            {
                StringBuffer attr_vector_i_sb = new StringBuffer("");
                offset = unserializeString(b, offset, attr_vector_i_sb);
                String attr_vector_i = attr_vector_i_sb.substring(0);
                a_map_to_p.attr_vector.add(Integer.parseInt(attr_vector_i));
            }
            ciphertext.map.add(a_map_to_p);
        }

        for(int i=0;i<Integer.valueOf(ciphertext.map_size);i++)
        {
            Element c_x_i = pk_cta.P.getG1().newElement();
            offset = unserializeElement(b, offset, c_x_i);
            ciphertext.c_x.add(c_x_i.getImmutable());
        }

        /*此处不需要反序列化，仅将byte数值赋值给ciphertext.ciphertext即可*/
        offset=offset+4;
        ciphertext.ciphertext = new byte[b.length-offset];
        System.arraycopy(b,offset,ciphertext.ciphertext,0,b.length-offset);

        return ciphertext;
    }

    /*序列化 Partly_Dec_Ciphertext*/
    public static byte[] serialize_Partly_Dec_Ciphertext(Partly_Dec_Ciphertext partly_dec_ciphertext) {
        ArrayList<Byte> arrlist = new ArrayList<Byte>();

        serializeElement(arrlist, partly_dec_ciphertext.c_tilde);
        serializeElement(arrlist, partly_dec_ciphertext.ct_);

        serializeUint32(arrlist, partly_dec_ciphertext.ciphertext.length);
        byteArrListAppend(arrlist, partly_dec_ciphertext.ciphertext);

        return Byte_arr2byte_arr(arrlist);
    }

    /*反序列化 Partly_Dec_Ciphertext*/
    public static Partly_Dec_Ciphertext unserialize_Partly_Dec_Ciphertext(PK_CTA pk_cta, byte[] b) {
        int offset = 0;
        Partly_Dec_Ciphertext partly_dec_ciphertext = new Partly_Dec_Ciphertext();

        /* 反序列化两个Element */
        partly_dec_ciphertext.c_tilde = pk_cta.P.getGT().newElement();
        partly_dec_ciphertext.ct_ = pk_cta.P.getGT().newElement();
        offset = unserializeElement(b, offset, partly_dec_ciphertext.c_tilde);
        offset = unserializeElement(b, offset, partly_dec_ciphertext.ct_);

        /*此处不需要反序列化，仅将byte数值赋值给ciphertext.ciphertext即可*/
        offset=offset+4;
        partly_dec_ciphertext.ciphertext = new byte[b.length-offset];
        System.arraycopy(b,offset,partly_dec_ciphertext.ciphertext,0,b.length-offset);

        return partly_dec_ciphertext;
    }
    /* Method has been test okay */
    /* potential problem: the number to be serialize is less than 2^31 */
    private static void serializeUint32(ArrayList<Byte> arrlist, int k) {
        int i;
        byte b;

        for (i = 3; i >= 0; i--) {
            b = (byte) ((k & (0x000000ff << (i * 8))) >> (i * 8));
            arrlist.add(Byte.valueOf(b));
        }
    }

    /*
     * Usage:
     *
     * You have to do offset+=4 after call this method
     */
    /* Method has been test okay */
    public static int unserializeUint32(byte[] arr, int offset) {
        int i;
        int r = 0;

        for (i = 3; i >= 0; i--)
            r |= (byte2int(arr[offset++])) << (i * 8);
        return r;
    }

    public static int byte2int(byte b) {
        if (b >= 0)
            return b;
        return (256 + b);
    }

    public static void byteArrListAppend(ArrayList<Byte> arrlist, byte[] b) {
        int len = b.length;
        for (int i = 0; i < len; i++)
            arrlist.add(Byte.valueOf(b[i]));
    }

    public static byte[] Byte_arr2byte_arr(ArrayList<Byte> B) {
        int len = B.size();
        byte[] b = new byte[len];

        for (int i = 0; i < len; i++)
            b[i] = B.get(i).byteValue();

        return b;
    }

}
