/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dotrow.projects.webdesktop.server;

import com.dotrow.projects.webdesktop.client.explorer.model.FileObjectDesktop;
import com.dotrow.projects.webdesktop.client.explorer.services.FileSystemService;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.RemoteSortTreeLoadConfig;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.VFS;

/**
 *
 * @author sxceron
 */
public class FileSystemServiceImpl extends RemoteServiceServlet implements FileSystemService {

    public List<FileObjectDesktop> getFiles(String path) {
        //path = "/home/sxceron/Escritorio";
        System.out.println("Path: " + path);
        List<FileObjectDesktop> files = new ArrayList<FileObjectDesktop>();
        try {
            FileSystemManager fsManager = VFS.getManager();
            FileObject folder = fsManager.resolveFile(path);
            for (FileObject fo : folder.getChildren()) {
                if (fo.getType() == FileType.FILE) {
                    FileName fn = fo.getName();
                    FileContent fc = fo.getContent();

                    FileObjectDesktop f = new FileObjectDesktop(fn.getBaseName());
                    f.setLastModification(new Date(fc.getLastModifiedTime()));
                    f.setOwner("sergio");
                    f.setPath(fn.getPath());
                    f.setSize(fc.getSize() / 1024 + " Kb");
                    if (fc.getContentInfo().getContentType() != null) {
                        f.setType(fc.getContentInfo().getContentType());
                    } else {
                        f.setType("Unknow");
                    }
                    files.add(f);
                } else if (fo.getType() == FileType.FOLDER) {
                    FileName fn = fo.getName();
                    FileContent fc = fo.getContent();

                    FileObjectDesktop f = new FileObjectDesktop(fn.getBaseName());
                    f.setLastModification(new Date(fc.getLastModifiedTime()));
                    f.setType("Folder");
                    f.setOwner("sergio");
                    f.setPath(fn.getPath());
                    files.add(f);
                }
            }

        } catch (FileSystemException ex) {
            Logger.getLogger(FileSystemServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return files;
    }

    private List<FileObjectDesktop> getFiles(FileObjectDesktop fod) {
        List<FileObjectDesktop> files = new ArrayList<FileObjectDesktop>();
        if (fod != null) {
            for (FileObjectDesktop f : getFiles(fod.getPath())) {
                files.add(f);
            }
        } else {
            for (FileObjectDesktop f : getFiles("/home/sxceron/Escritorio")) {
                files.add(f);
            }
        }
        return files;
    }

    public List<FileObjectDesktop> getTreeFolders(FileObjectDesktop fod) {

        List<FileObjectDesktop> models = getFiles(fod);

        Collections.sort(models, new Comparator<FileObjectDesktop>() {

            public int compare(FileObjectDesktop o1, FileObjectDesktop o2) {
                return o1.getNombre().compareTo(o2.getNombre());
            }
        });

        return models;
    }

    public List<FileObjectDesktop> getTreeFolders(final RemoteSortTreeLoadConfig loadConfig) {
        List<FileObjectDesktop> models = getTreeFolders((FileObjectDesktop) loadConfig.getParent());
        final String prop = loadConfig.getSortField();
        final boolean desc = loadConfig.getSortDir() == SortDir.DESC;
        if (prop != null) {
            Collections.sort(models, new Comparator<FileObjectDesktop>() {

                @SuppressWarnings("unchecked")
                public int compare(FileObjectDesktop o1, FileObjectDesktop o2) {
                    boolean m1Folder = o1.getType().equals("Folder");
                    boolean m2Folder = o1.getType().equals("Folder");

                    if (m1Folder && !m2Folder) {
                        return -1;
                    } else if (!m1Folder && m2Folder) {
                        return 1;
                    }

                    Comparable v1 = o1.get(prop);
                    Comparable v2 = o2.get(prop);
                    if (v1 == null && v2 != null) {
                        return -1;
                    } else if (v1 != null && v2 == null) {
                        return 0;
                    } else if (v1 == null && v2 == null) {
                        return o1.getNombre().compareTo(o2.getNombre());
                    }
                    return desc ? v2.compareTo(v1) : v1.compareTo(v2);
                }
            });
        }

        return models;
    }

    public boolean delete(List<FileObjectDesktop> f) throws IOException {
        return true;
    }

    public boolean remove(List<FileObjectDesktop> f) throws IOException {
        return true;
    }

    public FileObjectDesktop rename(FileObjectDesktop f, String newName) throws IOException {
        return null;
    }

    public FileObjectDesktop copy(FileObjectDesktop f, String path) throws IOException {
        return null;
    }

    public FileObjectDesktop move(FileObjectDesktop f, String path) throws IOException {
        return null;
    }

    public FileObjectDesktop update(FileObjectDesktop f1, FileObjectDesktop f2) throws IOException {
        return null;
    }
}
