package Main.Method;

import Bsw.Ciphertext;
import Bsw.Common;
import Bsw.Partly_Dec_Ciphertext;
import Bsw.SerializeUtils;
import Main.KeyAndParameters.*;

import java.io.IOException;

/*这个类主要用于实现一些从文件中读取Key或Parameters的方法*/
public class KeyLoad {
    /*读取并反序列化PK_CTA*/
    public static PK_CTA load_PK_CTA(String pk_cta_path) throws IOException {

        PK_CTA pk_cta = new PK_CTA();
        PK_CTA Immutable_pk = new PK_CTA();
        byte[] pk_cta_byte;

        pk_cta_byte = Common.suckFile(pk_cta_path);
        pk_cta = SerializeUtils.unserialize_PK_CTA(pk_cta_byte);

        Immutable_pk.P = pk_cta.P;
        Immutable_pk.N = pk_cta.N;
        Immutable_pk.g = pk_cta.g.getImmutable();
        Immutable_pk.g_a = pk_cta.g_a.getImmutable();
        Immutable_pk.X2 = pk_cta.X2.getImmutable();
        Immutable_pk.X4 = pk_cta.X4.getImmutable();
        Immutable_pk.Y = pk_cta.Y.getImmutable();

        return Immutable_pk;
    };

    /*读取并反序列化SK_CTA*/
    public static SK_CTA load_SK_CTA(String sk_cta_path, PK_CTA pk) throws IOException {

        SK_CTA sk_cta = new SK_CTA();
        SK_CTA Immutable_sk = new SK_CTA();
        byte[] sk_cta_byte;

        sk_cta_byte = Common.suckFile(sk_cta_path);
        sk_cta = SerializeUtils.unserialize_SK_CTA(pk, sk_cta_byte);

        Immutable_sk.a = sk_cta.a.getImmutable();
        Immutable_sk.alpha = sk_cta.alpha.getImmutable();
        Immutable_sk.X3 = sk_cta.X3.getImmutable();
        return Immutable_sk;
    };

    /*读取并反序列化PK_AA*/
    public static PK_AA load_PK_AA(String pk_aa_path, PK_CTA pk_cta) throws IOException {

        PK_AA pk_aa = new PK_AA();
        PK_AA Immutable_pk = new PK_AA();
        byte[]  pk_aa_byte;

        pk_aa_byte = Common.suckFile(pk_aa_path);
        pk_aa = SerializeUtils.unserialize_PK_AA(pk_cta,pk_aa_byte);

        Immutable_pk.H_j = pk_aa.H_j;

        return  Immutable_pk;
    };

    /*读取并反序列化SK_AA*/
    public static SK_AA load_SK_AA(String sk_aa_path, PK_CTA pk_cta) throws IOException {

        SK_AA sk_aa = new SK_AA();
        SK_AA Immutable_sk = new SK_AA();
        byte[]  sk_aa_byte;

        sk_aa_byte = Common.suckFile(sk_aa_path);
        sk_aa = SerializeUtils.unserialize_SK_AA(pk_cta,sk_aa_byte);

        Immutable_sk.h_j = sk_aa.h_j;
        Immutable_sk.Z_j = sk_aa.Z_j;
        return  Immutable_sk;
    };

    /*读取并反序列化SK_GID_CTA*/
    public static SK_GID_CTA load_SK_GID_CTA(String sk_gid_cta_path, PK_CTA pk_cta) throws IOException {

        SK_GID_CTA sk_gid_cta = new SK_GID_CTA();
        SK_GID_CTA Immutable_sk = new SK_GID_CTA();
        byte[]  sk_gid_cta_byte;

        sk_gid_cta_byte = Common.suckFile(sk_gid_cta_path);
        sk_gid_cta = SerializeUtils.unserialize_SK_GID_CTA(pk_cta,sk_gid_cta_byte);

        Immutable_sk.Gid = sk_gid_cta.Gid;
        Immutable_sk.K = sk_gid_cta.K;
        Immutable_sk.K_ = sk_gid_cta.K_;
        return  Immutable_sk;
    };

    /*读取并反序列化SK_GID_AA*/
    public static SK_GID_AA load_SK_GID_AA(String sk_gid_aa_path, PK_CTA pk_cta) throws IOException {

        SK_GID_AA sk_gid_aa = new SK_GID_AA();
        byte[]  sk_gid_aa_byte;

        sk_gid_aa_byte = Common.suckFile(sk_gid_aa_path);
        sk_gid_aa = SerializeUtils.unserialize_SK_GID_AA(pk_cta,sk_gid_aa_byte);

        return  sk_gid_aa;
    };

    /*读取并反序列化SK_GID*/
    public static SK_GID load_SK_GID(String sk_gid_path, PK_CTA pk_cta) throws IOException {

        SK_GID sk_gid = new SK_GID();
        SK_GID Immutable_sk = new SK_GID();
        byte[]  sk_gid_byte;

        sk_gid_byte = Common.suckFile(sk_gid_path);
        sk_gid = SerializeUtils.unserialize_SK_GID(pk_cta,sk_gid_byte);

        Immutable_sk.attr_list = sk_gid.attr_list;
        Immutable_sk.K = sk_gid.K.getImmutable();
        Immutable_sk.K_= sk_gid.K_.getImmutable();
        Immutable_sk.z = sk_gid.z.getImmutable();

        return  Immutable_sk;
    };

    /*读取并反序列化TK_GID*/
    public static TK_GID load_TK_GID(String tk_gid_path, PK_CTA pk_cta) throws IOException {

        TK_GID tk_gid = new TK_GID();
        TK_GID Immutable_tk = new TK_GID();
        byte[]  tk_gid_byte;

        tk_gid_byte = Common.suckFile(tk_gid_path);
        tk_gid = SerializeUtils.unserialize_TK_GID(pk_cta,tk_gid_byte);

        Immutable_tk.attr_list= tk_gid.attr_list;
        Immutable_tk.K= tk_gid.K;
        Immutable_tk.K_= tk_gid.K_;

        return Immutable_tk;
    };

    /*读取并反序列化Ciphertext*/
    public static Ciphertext load_Ciphertext(String Ciphertext_path, PK_CTA pk_cta) throws IOException {
        Ciphertext ciphertext = new Ciphertext();
        Ciphertext Immutable_ciphertext = new Ciphertext();
        byte[]  ciphertext_byte;
        ciphertext_byte = Common.suckFile(Ciphertext_path);
        ciphertext = SerializeUtils.unserialize_Ciphertext(pk_cta,ciphertext_byte);

        Immutable_ciphertext.ciphertext =  ciphertext.ciphertext;
        Immutable_ciphertext.map =  ciphertext.map;
        Immutable_ciphertext.c_hat =  ciphertext.c_hat.getImmutable();
        Immutable_ciphertext.c_x =  ciphertext.c_x;
        Immutable_ciphertext.c_tilde =  ciphertext.c_tilde.getImmutable();
        Immutable_ciphertext.attr_vector_size =  ciphertext.attr_vector_size;
        Immutable_ciphertext.map_size =  ciphertext.map_size;
        return  Immutable_ciphertext;
    };

    public static Partly_Dec_Ciphertext load_Partly_Dec_Ciphertext(String Partly_Dec_Ciphertext_path, PK_CTA pk_cta) throws IOException {
        Partly_Dec_Ciphertext partly_dec_ciphertext = new Partly_Dec_Ciphertext();
        Partly_Dec_Ciphertext Immutable_ciphertext = new Partly_Dec_Ciphertext();
        byte[]  ciphertext_byte;
        ciphertext_byte = Common.suckFile(Partly_Dec_Ciphertext_path);
        partly_dec_ciphertext = SerializeUtils.unserialize_Partly_Dec_Ciphertext(pk_cta,ciphertext_byte);

        Immutable_ciphertext.ciphertext =  partly_dec_ciphertext.ciphertext;
        Immutable_ciphertext.c_tilde = partly_dec_ciphertext.c_tilde;
        Immutable_ciphertext.ct_ = partly_dec_ciphertext.ct_;
        return  Immutable_ciphertext;
    };
}
