package Main.Method;

import Bsw.Ciphertext_Attribute_Set;
import Bsw.Common;
import Bsw.SerializeUtils;
import Main.KeyAndParameters.*;
import it.unisa.dia.gas.jpbc.Element;

import java.io.File;
import java.io.IOException;

public class UserKeyGen {

    public static void main(String[] args) {

        try {
            gen_user_key("1");
            gen_user_transform_key("1");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   public static SK_GID gen_user_key(String GID)throws IOException {
       SK_GID sk_gid = new SK_GID();
       PK_CTA pk_cta =  KeyLoad.load_PK_CTA("Parameters/PK_CTA");
       SK_GID_CTA sk_gid_cta = KeyLoad.load_SK_GID_CTA("Parameters/User"+GID+"/SK_GID_CTA",pk_cta);

       sk_gid.K = sk_gid_cta.K.getImmutable();
       sk_gid.K_ = sk_gid_cta.K_.getImmutable();
       sk_gid.z = pk_cta.P.getZr().newRandomElement().getImmutable();

       File file=new File("Parameters/User"+GID+"/SK_GID_AA");
       File[] fileName= file.listFiles(pathname -> {
           if (pathname.isFile())
               return true;
           else
               return false;
       });

       /*顺序获取目录下所有SK_GID_AA*/
       for(int i=0;i<fileName.length;i++)
       {
           SK_GID_AA sk_gid_aa = KeyLoad.load_SK_GID_AA(String.valueOf(fileName[i]),pk_cta);
           for(int j = 0;j<sk_gid_aa.attr_list.size();j++)
           {
               sk_gid.attr_list.add(sk_gid_aa.attr_list.get(j));
           }
       }

       /*序列化并存储SK_GID*/
       byte[] sk_gid_byte;
       sk_gid_byte = SerializeUtils.serialize_SK_GID(sk_gid);
       Common.spitFile("Parameters/User"+GID+"/SK_GID", sk_gid_byte);

       //TEST
       SK_GID sk_gid_test = KeyLoad.load_SK_GID("Parameters/User"+GID+"/SK_GID", pk_cta);

       for(int j=0;j<sk_gid_test.attr_list.size();j++)
       {
           println("sk_gid :"+sk_gid_test.attr_list.get(j).attribute_name);
           println("sk_gid :"+sk_gid_test.attr_list.get(j).attribute_value);
       }

       return sk_gid;
   }

    public static TK_GID gen_user_transform_key(String GID)throws IOException {
        TK_GID tk_gid = new TK_GID();

        PK_CTA pk_cta =  KeyLoad.load_PK_CTA("Parameters/PK_CTA");
        SK_GID sk_gid = KeyLoad.load_SK_GID("Parameters/User"+GID+"/SK_GID",pk_cta);

        /*calculate tk_gid based on sk_gid*/
        tk_gid.K = sk_gid.K.powZn(sk_gid.z.invert());
        tk_gid.K_ = sk_gid.K_.powZn(sk_gid.z.invert());
        for(int i=0;i<sk_gid.attr_list.size();i++)
        {
            Ciphertext_Attribute_Set ciphertext_attribute_set = new Ciphertext_Attribute_Set();
            ciphertext_attribute_set.attribute_name = sk_gid.attr_list.get(i).attribute_name;
            ciphertext_attribute_set.attribute_value = sk_gid.attr_list.get(i).attribute_value.powZn(sk_gid.z.invert());
            tk_gid.attr_list.add(ciphertext_attribute_set);
        }
        /*序列化并存储TK_GID*/
        byte[] tk_gid_byte;
        tk_gid_byte = SerializeUtils.serialize_TK_GID(tk_gid);
        Common.spitFile("Parameters/User"+GID+"/TK_GID", tk_gid_byte);

        //TEST
//        TK_GID tk_gid_test = KeyLoad.load_TK_GID("Parameters/User"+GID+"/TK_GID", pk_cta);
//        println(tk_gid.K.powZn(sk_gid.z));
//        println(tk_gid.K_.powZn(sk_gid.z));

        for(int j=0;j<tk_gid.attr_list.size();j++)
        {
            println("tk_gid :"+tk_gid.attr_list.get(j).attribute_name);
            println("tk_gid :"+tk_gid.attr_list.get(j).attribute_value.powZn(sk_gid.z));
        }
//        println(tk_gid_test.K);
//        println(tk_gid_test.K_);
//
//        for(int j=0;j<tk_gid_test.attr_list.size();j++)
//        {
//            println(tk_gid_test.attr_list.get(j).attribute_name);
//            println(tk_gid_test.attr_list.get(j).attribute_value);
//        }

        return tk_gid;
    }

    private static void println(Object o) {
        System.out.println(o);
    }
}
