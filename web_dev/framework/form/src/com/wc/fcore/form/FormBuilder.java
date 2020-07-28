/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wc.fcore.form;

import com.wc.fcore.form.definitions.FormEnctype;
import com.wc.fcore.form.definitions.FormMethod;

/**
 *
 * @author REx
 */
public class FormBuilder {

    /**
     * 
     * @param name
     * @param id
     * @param clazz
     * @param action
     * @param method
     * @param enctype
     * @return
     */
    public static String formBegin(
            String name,
            String id,
            String clazz,
            String action,
            FormMethod method,
            FormEnctype enctype){
        if (name == null) {
            throw new IllegalArgumentException("Name Must Be An Object");
        }
        if (name.equals("")) {
            throw new IllegalArgumentException("Name Must Be Specified");
        }
        if (action == null) {
            throw new IllegalArgumentException("Action Must Be An Object");
        }
        if (action.equals("")) {
            throw new IllegalArgumentException("Action Must Be Specified");
        }
        if (method == null) {
            throw new IllegalArgumentException("Method Must Be Specified");
        }
        if (enctype == null) {
            throw new IllegalArgumentException("Enctype Must Be Specified");
        }
        return "<form "
                + "name=\""+name+"\" "
                + "method=\""+method.name()+"\" "
                + "enctype=\""+enctype.toString()+"\" "
                + "action=\""+action+"\" "
                + (id != null ? "id=\""+id+"\" " : "")
                + (clazz != null ? "class=\""+clazz+"\" " : "") + ">";
    }

    /**
     * 
     * @return
     */
    public static String formEnd(){ 
        return "</form>";
    }

    /**
     * 
     * @param name
     * @param text
     * @param id
     * @param clazz
     * @param cols
     * @param rows
     * @param readonly
     * @param disabled
     * @return
     */
    public static String textarea(
            String name,
            String text,
            String id,
            String clazz,
            int cols,
            int rows,
            boolean readonly,
            boolean disabled){
        if (name == null) {
            throw new IllegalArgumentException("Name Must Be An Object");
        }
        if (name.equals("")) {
            throw new IllegalArgumentException("Name Must Be Specified");
        }
        if (cols < 1) {
            throw new IllegalArgumentException("Cols Must Be Greater Than Zero");
        }
        if (rows < 1) {
            throw new IllegalArgumentException("Rows Must Be Greater Than Zero");
        }
        return "<textarea "
                + "name=\""+name+"\" "
                + "cols=\""+cols+"\" "
                + "rows=\""+rows+"\" "
                + (id != null ? "id=\""+id+"\" " : "")
                + (clazz != null ? "class=\""+clazz+"\" " : "")
                + (readonly ? "readonly=\"readonly\" " : "")
                + (disabled ? "disabled=\"disabled\" " : "")
                + ">"+text+"</textarea>";
    }

    /**
     * 
     * @param text
     * @param id
     * @return
     */
    public static String label(String text, String id){
        return "<label for=\""+id+"\" >"+text+"</label>";
    }

    /**
     *
     * @param name
     * @param id
     * @param clazz
     * @param size
     * @param multiple
     * @param disabled
     * @throws IllegalArgumentException throws if name is null, name equals ""
     * or size is less than 1
     * @return
     */
    public static String selectBegin(
            String name,
            String id,
            String clazz,
            int size, 
            boolean multiple, 
            boolean disabled){
        if (size < 1){ 
            throw new IllegalArgumentException("Size Must Be Greater Than 1");
        }
        if (name == null){ 
            throw new IllegalArgumentException("Name Must Be An Object");
        }
        if (name.equalsIgnoreCase("")){ 
            throw new IllegalArgumentException("Name Must Be Specified");
        }
        return "<select "
                + "name=\""+name+"\" "
                + "size=\""+size+"\" "
                + (id != null ? "id=\""+id+"\" " : "")
                + (clazz != null ? "class=\""+clazz+"\" " : "")
                + (disabled ? "disabled=\"disabled\" " : "")
                + (multiple ? "multiple=\"multiple\" " : "") + ">";
    }

    /**
     *
     * @return
     */
    public static String selectEnd(){
        return "</select>";
    }

    /**
     * 
     * @param label
     * @param disabled
     * @return
     */
    public static String optgroupBegin(
            String label,
            boolean disabled){
        return "<optgroup "
                + "label=\""+label+"\" "
                + (disabled ? "disabled=\"disabled\" " : "") + ">";
    }

    /**
     * 
     * @return
     */
    public static String optgroupEnd(){
        return "</optgroup>";
    }

    /**
     * 
     * @param text
     * @param value
     * @param selected
     * @param disabled
     * @return
     */
    public static String option(
            String text,
            String value,
            boolean selected,
            boolean disabled){
        if (text == null) {
            throw new IllegalArgumentException("Viewable Text Must Be Specified");
        }
        return "<option "
                + "value\""+value+"\" "
                + (selected ? "selected=\"selected\" " : "")
                + (disabled ? "disabled=\"disabled\" " : "")
                + ">"+text+"</option>";
    }

    /**
     *
     * @param name
     * @param id
     * @param clazz
     * @param checked
     * @param disabled
     * @return
     */
    public static String checkbox(
            String name,
            String id,
            String clazz,
            boolean checked,
            boolean disabled){
        if (name == null) {
            throw new IllegalArgumentException("A Name Object Must Be Specified");
        }
        if (name.equalsIgnoreCase("")){
            throw new IllegalArgumentException("A Name Must Be Specified");
        }
        return "<input "
                + "type=\"checkbox\" "
                + "name=\""+name+"\" "
                + "id=\""+id+"\" "
                + "class=\""+clazz+"\" "
                + (checked ? "checked=\"checked\" " : "") + ""
                + (disabled ? "disabled=\"disabled\" " : "") + " />";
    }

    /**
     *
     * @param name
     * @param id
     * @param clazz
     * @param disabled
     * @return
     */
    public static String file(
            String name,
            String id,
            String clazz,
            boolean disabled){
        if (name == null) {
            throw new IllegalArgumentException("A Name Object Must Be Specified");
        }
        if (name.equalsIgnoreCase("")){
            throw new IllegalArgumentException("A Name Must Be Specified");
        }
        return "<input "
                + "type=\"file\" "
                + "name=\""+name+"\" "
                + "id=\""+id+"\" "
                + "class=\""+clazz+"\" "
                + (disabled ? "disabled=\"disabled\" " : "") + " />";
    }

    /**
     * 
     * @param name
     * @param id
     * @param value
     * @return
     */
    public static String hidden(
            String name,
            String id,
            String value){
        if (name == null) {
            throw new IllegalArgumentException("A Name Object Must Be Specified");
        }
        if (name.equalsIgnoreCase("")){
            throw new IllegalArgumentException("A Name Must Be Specified");
        }
        return "<input "
                + "type=\"hidden\" "
                + "name=\""+name+"\" "
                + "id=\""+id+"\" "
                + "value=\""+value+"\" />";
    }

    /**
     * 
     * @param name
     * @param id
     * @param clazz
     * @param value
     * @param disabled
     * @return
     */
    public static String password(
            String name,
            String id,
            String clazz,
            String value,
            boolean disabled){
        if (name == null) {
            throw new IllegalArgumentException("A Name Object Must Be Specified");
        }
        if (name.equalsIgnoreCase("")){
            throw new IllegalArgumentException("A Name Must Be Specified");
        }
        return "<input "
                + "type=\"password\" "
                + "name=\""+name+"\" "
                + "id=\""+id+"\" "
                + "class=\""+clazz+"\" "
                + "value=\""+value+"\" "
                + (disabled ? "disabled=\"disabled\" " : "") + "/>";
    }

    /**
     * 
     * @param name
     * @param id
     * @param clazz
     * @param value
     * @param checked
     * @param disabled
     * @return
     */
    public static String radio(
            String name,
            String id,
            String clazz,
            String value,
            boolean checked,
            boolean disabled){
        if (name == null) {
            throw new IllegalArgumentException("A Name Object Must Be Specified");
        }
        if (name.equalsIgnoreCase("")){
            throw new IllegalArgumentException("A Name Must Be Specified");
        }
        return "<input "
                + "type=\"radio\" "
                + "name=\""+name+"\" "
                + "id=\""+id+"\" "
                + "class=\""+clazz+"\" "
                + "value=\""+value+"\" "
                + (checked ? "checked=\"checked\" " : "")
                + (disabled ? "disabled=\"disabled\" " : "") + "/>";
    }

    /**
     * 
     * @param value
     * @param id
     * @param clazz
     * @param disabled
     * @return
     */
    public static String reset(
            String value,
            String id,
            String clazz,
            boolean disabled){
        return "<input "
                + "type=\"reset\" "
                + "id=\""+id+"\" "
                + "class=\""+clazz+"\" "
                + "value=\""+value+"\" "
                + (disabled ? "disabled=\"disabled\" " : "") + "/>";
    }

    /**
     * 
     * @param value
     * @param id
     * @param clazz
     * @param disabled
     * @return
     */
    public static String submit(
            String value,
            String id,
            String clazz,
            boolean disabled){
        return "<input "
                + "type=\"submit\" "
                + "id=\""+id+"\" "
                + "class=\""+clazz+"\" "
                + "value=\""+value+"\" "
                + (disabled ? "disabled=\"disabled\" " : "") + "/>";
    }

    /**
     * 
     * @param name
     * @param id
     * @param clazz
     * @param value
     * @param maxLength
     * @param disabled
     * @return
     */
    public static String text(
            String name,
            String id,
            String clazz,
            String value,
            int maxLength,
            boolean disabled){
        if (name == null) {
            throw new IllegalArgumentException("A Name Object Must Be Specified");
        }
        if (name.equalsIgnoreCase("")){
            throw new IllegalArgumentException("A Name Must Be Specified");
        }
        return "<input "
                + "type=\"text\" "
                + "name=\""+name+"\" "
                + "id=\""+id+"\" "
                + "class=\""+clazz+"\" "
                + "value=\""+value+"\" "
                + "maxlength=\""+maxLength+"\" "
                + (disabled ? "disabled=\"disabled\" " : "") + "/>";
    }

}

