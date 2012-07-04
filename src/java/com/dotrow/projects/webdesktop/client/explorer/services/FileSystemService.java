/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dotrow.projects.webdesktop.client.explorer.services;

import com.dotrow.projects.webdesktop.client.explorer.model.FileObjectDesktop;
import com.extjs.gxt.ui.client.data.RemoteSortTreeLoadConfig;
import com.google.gwt.user.client.rpc.RemoteService;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author sxceron
 */
public interface FileSystemService extends RemoteService  {
    public List<FileObjectDesktop> getFiles(String path);
    public List<FileObjectDesktop> getTreeFolders(FileObjectDesktop fod );
    public List<FileObjectDesktop> getTreeFolders(RemoteSortTreeLoadConfig loadConfig);

    public boolean delete(List<FileObjectDesktop> f) throws IOException;
    public boolean remove(List<FileObjectDesktop> f) throws IOException;
    public FileObjectDesktop rename(FileObjectDesktop f, String newName) throws IOException;
    public FileObjectDesktop copy(FileObjectDesktop f, String path) throws IOException;
    public FileObjectDesktop move(FileObjectDesktop f, String path) throws IOException;
    public FileObjectDesktop update(FileObjectDesktop f1, FileObjectDesktop f2) throws IOException;
}
