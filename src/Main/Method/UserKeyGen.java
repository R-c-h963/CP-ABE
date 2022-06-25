package Main.Method;

import Bsw.Common;
import Bsw.SerializeUtils;
import Main.KeyAndParameters.PK_CTA;
import Main.KeyAndParameters.SK_GID;
import Main.KeyAndParameters.SK_GID_AA;
import Main.KeyAndParameters.SK_GID_CTA;

import java.io.File;
import java.io.IOException;

public class UserKeyGen {

    public static void main(String[] args) {

        try {
            gen_user_key("1");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   public static SK_GID gen_user_key(String GID)throws IOException {
       SK_GID sk_gid = new SK_GID();
       PK_CTA pk_cta =  KeyLoad.load_PK_CTA("Parameters/PK_CTA");
       SK_GID_CTA sk_gid_cta = KeyLoad.load_SK_GID_CTA("Parameters/User"+GID+"/SK_GID_CTA",pk_cta);
       sk_gid.K = sk_gid_cta.K;
       sk_gid.K_ = sk_gid_cta.K_;

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
       Common.spitFile("Parameters/User"+GID+"/SK_GID_CTA", sk_gid_byte);

       SK_GID sk_gid_test = KeyLoad.load_SK_GID("Parameters/User"+GID+"/SK_GID_CTA", pk_cta);

       println(sk_gid.K);
       println(sk_gid.K_);
       for(int j=0;j<sk_gid_test.attr_list.size();j++)
       {
           println(sk_gid_test.attr_list.get(j).attribute_name);
           println(sk_gid_test.attr_list.get(j).attribute_value);
       }

       return sk_gid;
   }

    private static void println(Object o) {
        System.out.println(o);
    }
}
