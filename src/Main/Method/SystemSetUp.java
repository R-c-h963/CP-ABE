package Main.Method;

import Bsw.Common;
import Bsw.SerializeUtils;
import Main.KeyAndParameters.PK_CTA;
import Main.KeyAndParameters.SK_CTA;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1CurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.parameters.PropertiesParameters;
import it.unisa.dia.gas.plaf.jpbc.util.ElementUtils;

import java.io.*;

public class SystemSetUp {

    public static void main(String[] args) {

        CurveParametersGen(4, 30);//子群数量与子群的阶
        try {
            CTA_Key_Gen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*随机选取群，并将群参数输出至文件*/
    public static void CurveParametersGen(int numPrimes, int bits) {//子群数量与阶
        TypeA1CurveGenerator pg = new TypeA1CurveGenerator(numPrimes, bits);
        PairingParameters typeA1Params = pg.generate();
//        println(typeA1Params);
//        println(typeA1Params.getObject("n"));
        Object N = typeA1Params.getObject("n");
        try {
            File writeName = new File("Parameters/a1.properties");
            if (!writeName.exists()) {
                writeName.createNewFile(); /*创建新文件,有同名的文件的话直接覆盖*/
            }
            FileWriter writer = new FileWriter(writeName);
            BufferedWriter out = new BufferedWriter(writer);
            out.write(typeA1Params.toString());
            out.flush(); /*把缓存区内容压入文件*/
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*根据选取的群生成CTA的PK与SK并存储*/
    public static void CTA_Key_Gen() throws IOException {
        PK_CTA cta_pk = new PK_CTA();
        SK_CTA cta_sk = new SK_CTA();
        PropertiesParameters curveParams = new PropertiesParameters();
        PairingParameters typeA1Params = curveParams.load("Parameters/a1.properties");

        Pairing pairing = PairingFactory.getPairing(typeA1Params);
        Element generator = pairing.getG1().newRandomElement().getImmutable();//生成元
        /*SK_CTA*/
        cta_sk.a = pairing.getZr().newRandomElement().getImmutable();
        cta_sk.alpha = pairing.getZr().newRandomElement().getImmutable();
        cta_sk.X3 = ElementUtils.getGenerator(pairing, generator, typeA1Params, 2, 4).getImmutable();
        /*PK_CTA*/
        cta_pk.P = pairing;
        cta_pk.N = typeA1Params.getObject("n").toString();
        cta_pk.g = ElementUtils.getGenerator(pairing, generator, typeA1Params, 0, 4).getImmutable();
        cta_pk.g_a = cta_pk.g.powZn(cta_sk.a).getImmutable();
        cta_pk.X2 = ElementUtils.getGenerator(pairing, generator, typeA1Params, 1, 4).getImmutable();
        cta_pk.X4 = ElementUtils.getGenerator(pairing, generator, typeA1Params, 3, 4).getImmutable();
        cta_pk.Y = pairing.pairing(cta_pk.g, cta_pk.g.powZn(cta_sk.alpha)).getImmutable();

        /*save PK_CTA  SK_CTA*/
        byte[] cta_pk_byte, cta_sk_byte;
        cta_pk_byte = SerializeUtils.serialize_PK_CTA(cta_pk);
        Common.spitFile("Parameters/PK_CTA", cta_pk_byte);

        cta_sk_byte = SerializeUtils.serialize_SK_CTA(cta_sk);
        Common.spitFile("Parameters/SK_CTA", cta_sk_byte);

        //TEST
//        PK_CTA cta_pk_test = KeyLoad.load_PK_CTA("Parameters/PK_CTA");
//        println("g:"+cta_pk_test.g);
//        println("g:"+cta_pk.g);
//        println(cta_pk_test.P);
//        println(cta_pk.P);
//        println("g_a:"+cta_pk_test.g_a);
//        println("g_a:"+cta_pk.g_a);
//        println("X2:"+cta_pk.X2);
//        println("X2:"+cta_pk_test.X2);
//        println("X4:"+cta_pk_test.X4);
//        println("X4"+cta_pk.X4);
//        println("Y:"+cta_pk_test.Y);
//        println("Y:"+cta_pk.Y);

//        SK_CTA cta_sk_test = KeyLoad.load_SK_CTA("Parameters/SK_CTA", cta_pk);
//        println(cta_sk.a);
//        println(cta_sk_test.a);
//        println(cta_sk.alpha);
//        println(cta_sk_test.alpha);
//        println(cta_sk.X3);
//        println(cta_sk_test.X3);

    }

    private static void println(Object o) {
        System.out.println(o);
    }
}

////////////////////////测时函数
//    long startTime = System.currentTimeMillis(); //程序开始记录时间
//
//    long endTime = System.currentTimeMillis(); //程序结束记录时间
//    long TotalTime = endTime - startTime;
//    println(TotalTime);
////////////////////////////