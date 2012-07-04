/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dotrow.projects.webdesktop.client.explorer.model;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author sxceron
 */
public class FileObjectDesktop extends BaseTreeModel implements Serializable  {

    public FileObjectDesktop(){
    }
    
    public FileObjectDesktop(String nombre){
        set("nombre", nombre);
    }
    
    public void setNombre( String nombre ){
        set("nombre", nombre);
    }
    
    public void setPath( String path ){
        set("path", path);
    }
    
    public void setOwner( String uid ){
        set("owner", uid);
    }
 
    public void setType( String type ){
        set("type", type);
    }

    public void setSize( String size ){
        set("size", size);
    }

    public void setLastModification( Date mdate ){
        set("lastModification", mdate);
    }

    public void setAttributes( String attrib ){
        set("attributes", attrib);
    }

    public String getNombre() {
        return get("nombre");
    }

    public String getPath() {
        return get("path");
    }

    public String getOwner() {
        return get("owner");
    }

    public String getType() {
        return get("type");
    }

    public String getSize() {
        return get("size");
    }

    public Date getLastModification() {
        return get("lastModification");
    }

    public String getAttributes() {
        return get("attributes");
    }

}
