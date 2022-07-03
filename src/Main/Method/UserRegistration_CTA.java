package Main.Method;

import Bsw.Common;
import Bsw.SerializeUtils;
import Main.KeyAndParameters.PK_CTA;
import Main.KeyAndParameters.SK_CTA;
import Main.KeyAndParameters.SK_GID_AA;
import Main.KeyAndParameters.SK_GID_CTA;
import it.unisa.dia.gas.jpbc.Element;

import java.io.IOException;
import java.util.ArrayList;

public class UserRegistration_CTA {

    public static void main(String[] args) {

        try {
            gen_sk_gid_cta();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SK_GID_CTA gen_sk_gid_cta() throws IOException {
        SK_GID_CTA sk_gid_cta = new SK_GID_CTA();
        sk_gid_cta.Gid = "1";

        PK_CTA pk_cta = KeyLoad.load_PK_CTA("Parameters/PK_CTA");
        SK_CTA sk_cta = KeyLoad.load_SK_CTA("Parameters/SK_CTA", pk_cta);

        /*randomly select t and calculate R R_*/
        Element t = pk_cta.P.getZr().newRandomElement().getImmutable();
        Element R = sk_cta.X3.powZn(pk_cta.P.getZr().newRandomElement()).getImmutable();
        Element R_ = sk_cta.X3.powZn(pk_cta.P.getZr().newRandomElement()).getImmutable();
        Element g_t = pk_cta.g.powZn(t).getImmutable();

        /*Then calculate K and K_*/
        sk_gid_cta.K = (pk_cta.g.powZn(sk_cta.alpha)).mul(g_t.powZn(sk_cta.a)).mul(R).getImmutable();
        sk_gid_cta.K_ = g_t.mul(R_).getImmutable();

        byte[] sk_gid_cta_byte;
        sk_gid_cta_byte = SerializeUtils.serialize_SK_GID_CTA(sk_gid_cta);
        Common.spitFile("Parameters/User"+sk_gid_cta.Gid+"/SK_GID_CTA", sk_gid_cta_byte);

        ArrayList<Byte> uid_T = new ArrayList<Byte>();
        SerializeUtils.serializeElement(uid_T, t);
        byte[] uid_t = SerializeUtils.Byte_arr2byte_arr(uid_T);
        Common.spitFile("Parameters/User"+sk_gid_cta.Gid+"-t", uid_t);

        println(t);
        SK_GID_CTA sk_gid_cta_test = KeyLoad.load_SK_GID_CTA("Parameters/User"+sk_gid_cta.Gid+"/SK_GID_CTA", pk_cta);
//        println(sk_gid_cta_test.Gid);
//        println(sk_gid_cta.Gid);
//        println(sk_gid_cta_test.K);
//        println("K:"+sk_gid_cta.K);
//        println(sk_gid_cta_test.K_);
//        println("K_"+sk_gid_cta.K_);

        return sk_gid_cta;
    }

    private static void println(Object o) {
        System.out.println(o);
    }
}
