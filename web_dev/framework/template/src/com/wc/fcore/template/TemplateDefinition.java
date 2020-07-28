
package com.wc.fcore.template;

import com.wc.fcore.template.exception.TemplateException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author REx
 */
public class TemplateDefinition implements Serializable {

    private ArrayList<String> sections;

    public TemplateDefinition(){
        sections = new ArrayList<String>();
    }

    public void addSection(int index, String info){
        sections.add(index, info);
    }

    public void addSection(String key, String section) throws TemplateException {
        int index = sections.indexOf(key);
        if (index < 0){
            throw new TemplateException("Key Note Found", null);
        }
        sections.add(index, section);
    }

    public String getSection(int index){
        return sections.get(index);
    }

    @Override
    public String toString(){
        String result = "";
        Iterator<String> it = sections.iterator();
        while(it.hasNext()){
            String str = it.next();
            if (str != null){ result += str; }
        }
        return result;
    }

}
