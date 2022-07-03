package Main.KeyAndParameters;

import Bsw.Ciphertext_Attribute_Set;
import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;

public class TK_GID {
    public Element K;
    public Element K_;
    public ArrayList<Ciphertext_Attribute_Set> attr_list = new ArrayList<Ciphertext_Attribute_Set>();

    public Element get_element_by_attr_name(String attr_name){
        for(int i=0;i<attr_list.size();i++)
        {
            if(attr_name.equals(attr_list.get(i).attribute_name))
            {
                return attr_list.get(i).attribute_value;
            }
            else continue;
        }
        return null;
    }
}
