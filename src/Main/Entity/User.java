package Main.Entity;

import Main.KeyAndParameters.PK_CTA;
import Main.KeyAndParameters.SK_GID;
import Main.Method.KeyLoad;
import Main.Method.UserKeyGen;

import java.io.IOException;

public class User {
    public String Gid;
    private SK_GID sk_gid;
    public void get_sk_gid()throws IOException {
        this.sk_gid= UserKeyGen.gen_user_key(this.Gid);
    }
}
