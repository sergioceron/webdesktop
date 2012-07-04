/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dotrow.projects.webdesktop.client.explorer.services;

import com.dotrow.projects.webdesktop.client.explorer.model.FileObjectDesktop;
import com.extjs.gxt.ui.client.data.RemoteSortTreeLoadConfig;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;

/**
 *
 * @author sxceron
 */
public interface FileSystemServiceAsync {
    public void getFiles(String path, AsyncCallback<List<FileObjectDesktop>> callback);
    public void delete(List<FileObjectDesktop> f, AsyncCallback<Boolean> callback);
    public void remove(List<FileObjectDesktop> f, AsyncCallback<Boolean> callback);
    public void rename(FileObjectDesktop f, String newName, AsyncCallback<FileObjectDesktop> asyncCallback);
    public void copy(FileObjectDesktop f, String path, AsyncCallback<FileObjectDesktop> asyncCallback);
    public void update(FileObjectDesktop f1, FileObjectDesktop f2, AsyncCallback<FileObjectDesktop> asyncCallback);
    public void move(FileObjectDesktop f, String path, AsyncCallback<FileObjectDesktop> asyncCallback);

    public void getTreeFolders(FileObjectDesktop fod , AsyncCallback<List<FileObjectDesktop>> asyncCallback);
    public void getTreeFolders(RemoteSortTreeLoadConfig loadConfig, AsyncCallback<List<FileObjectDesktop>> asyncCallback);

}

