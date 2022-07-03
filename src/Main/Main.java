package Main;


import Bsw.Plaintext_Attribute_Set;
import Main.KeyAndParameters.PK_CTA;
import Main.KeyAndParameters.SK_AA;
import Main.KeyAndParameters.SK_GID;
import Main.KeyAndParameters.TK_GID;
import Main.Method.*;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception{

        SystemSetUp.CurveParametersGen(4, 5);//子群数量与子群的阶
        SystemSetUp.CTA_Key_Gen();

        AASetUp.AA_Key_Gen("1");

        UserRegistration_CTA.gen_sk_gid_cta();
        UserRegistration_AA.user_registration();

        UserKeyGen.gen_user_key("1");
        UserKeyGen.gen_user_transform_key("1");

        Encrypt.encrypt("test123465789jj@!#","(A and (D or (B and C)))");

        PK_CTA pk_cta = KeyLoad.load_PK_CTA("Parameters/PK_CTA");
        TK_GID tk_gid = KeyLoad.load_TK_GID("Parameters/User1/TK_GID", pk_cta);
        Outsourcing_Decrypt.outsourcing_decrypt("Parameters/User1/Ciphertext",tk_gid);

        SK_GID sk_gid = KeyLoad.load_SK_GID("Parameters/User1/SK_GID",pk_cta);
        User_Decrypt.user_decrypt("Parameters/User1/Partly_Dec_Ciphertext",sk_gid);

        System.out.println("Hello World");
    }
}
