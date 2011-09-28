/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.datatype;

import cloud.DataType;
import cloud.exceptions.DataTypeException;
import cloud.language.LanguageStatements;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;

/**
 *
 * @author REx
 */
public class NitfDocument implements DataType<Document>
{
    private Document data;
    
    private String id;
    
    private XPath xpath;
    
    public NitfDocument()
    {
        data = null;
        id = null;
    }

    @Override
    public void init(org.w3c.dom.Document data) 
            throws DataTypeException 
    {
        this.data = data;
        this.id = null;
    }

    @Override
    public void init(String id, org.w3c.dom.Document data) 
            throws DataTypeException 
    {
        this.data = data;
        this.id = id;
    }

    @Override
    public org.w3c.dom.Document getDump() 
            throws DataTypeException
    {
        return data;
    }
    
    @Override
    public String getId() 
            throws DataTypeException 
    {
        return id;
    }

    @Override
    public String get(String key) 
            throws DataTypeException 
    {
        try
        {
            if (xpath == null)
            {
                XPathFactory factory = XPathFactory.newInstance();
                xpath = factory.newXPath();
            }
            XPathExpression expr = xpath.compile(key);
            return ((Attr)expr.evaluate(data, XPathConstants.NODE)).getValue();
        }
        catch(Exception ex)
        {
            throw new DataTypeException(LanguageStatements.NONE, ex);
        }
    }
    
}
