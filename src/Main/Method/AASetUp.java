package Main.Method;

import Bsw.Common;
import Bsw.SerializeUtils;
import Main.KeyAndParameters.PK_AA;
import Main.KeyAndParameters.SK_AA;
import Main.KeyAndParameters.PK_CTA;

import java.io.IOException;

public class AASetUp {

    public static void main(String[] args) {

        try {
            AA_Key_Gen("1");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void AA_Key_Gen(String AA_ID)throws IOException{

        PK_AA aa_pk=new PK_AA();
        SK_AA aa_sk=new SK_AA();
        PK_CTA cta_pk = KeyLoad.load_PK_CTA("Parameters/PK_CTA");

        /*generator h_j,Z_j based on g and X4*/
        aa_sk.h_j =(cta_pk.g.powZn(cta_pk.P.getZr().newRandomElement())).getImmutable();
        aa_sk.Z_j =(cta_pk.X4.powZn(cta_pk.P.getZr().newRandomElement())).getImmutable();
        /*calculate H_j*/
        aa_pk.H_j=aa_sk.h_j.mul(aa_sk.Z_j).getImmutable();



        /*save PK_AA   SK_AA*/
        byte[] aa_pk_byte, aa_sk_byte;
        aa_pk_byte = SerializeUtils.serialize_PK_AA(aa_pk);
        Common.spitFile("Parameters/PK_AA-"+AA_ID, aa_pk_byte);

        aa_sk_byte = SerializeUtils.serialize_SK_AA(aa_sk);
        Common.spitFile("Parameters/SK_AA-"+AA_ID, aa_sk_byte);

        //TEST
//        PK_AA aa_pk_test = KeyLoad.load_PK_AA("Parameters/PK_AA-"+AA_ID,cta_pk);
//        println(aa_pk_test.H_j);
//        println(aa_pk.H_j);
//
//        SK_AA aa_sk_test = KeyLoad.load_SK_AA("Parameters/SK_AA-"+AA_ID,cta_pk);
//        println(aa_sk.h_j);
//        println(aa_sk_test.h_j);
//        println(aa_sk.Z_j);
//        println(aa_sk_test.Z_j);

    }

    private static void println(Object o) {
        System.out.println(o);
    }
}
