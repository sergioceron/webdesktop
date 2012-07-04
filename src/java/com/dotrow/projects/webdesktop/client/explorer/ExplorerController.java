/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dotrow.projects.webdesktop.client.explorer;

import com.dotrow.projects.webdesktop.client.explorer.model.FileObjectDesktop;
import com.dotrow.projects.webdesktop.client.explorer.services.FileSystemServiceAsync;
import com.dotrow.projects.webdesktop.client.services.KernelServices;
import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.widget.Info;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;

/**
 *
 * @author sxceron
 */
public class ExplorerController extends Controller {

    private ExplorerView explorerView;
    private FileSystemServiceAsync service;

    public ExplorerController() {
        registerEventTypes(ExplorerEvents.OPEN);
        registerEventTypes(ExplorerEvents.LIST);
        registerEventTypes(ExplorerEvents.CHANGE);
        registerEventTypes(ExplorerEvents.COPY);
        registerEventTypes(ExplorerEvents.DELETE);
        registerEventTypes(ExplorerEvents.ERROR);
        registerEventTypes(ExplorerEvents.MOVE);
        registerEventTypes(ExplorerEvents.REMOVE);
        registerEventTypes(ExplorerEvents.RENAME);
    }

    @Override
    public void handleEvent(AppEvent event) {
        EventType type = event.getType();
        if (type == ExplorerEvents.OPEN) {
            onInit(event);
        } else if (type == ExplorerEvents.COPY) {
            onCopy(event);
        } else if (type == ExplorerEvents.DELETE) {
            onDelete(event);
        } else if (type == ExplorerEvents.ERROR) {
            onError(event);
        } else if (type == ExplorerEvents.MOVE) {
            onMove(event);
        } else if (type == ExplorerEvents.REMOVE) {
            onRemove(event);
        } else if (type == ExplorerEvents.RENAME) {
            onRename(event);
        } else if (type == ExplorerEvents.CHANGE) {
            onChange(event);
        }
    }

    @Override
    public void initialize() {
        explorerView = new ExplorerView(this);
    }

    private void onInit(final AppEvent event) {

        service = (FileSystemServiceAsync) Registry.get(KernelServices.FS_SERVICE);
        service.getFiles("/home/sxceron/Escritorio", new AsyncCallback<List<FileObjectDesktop>>() {

            public void onFailure(Throwable caught) {
                Dispatcher.forwardEvent(ExplorerEvents.ERROR, caught);
            }

            public void onSuccess(List<FileObjectDesktop> result) {
                AppEvent ae = new AppEvent(event.getType(), result);
                ae.setData("desktop", event.getData("desktop"));
                forwardToView(explorerView, ae);
            }
        });
    }

    private void onCopy(AppEvent event) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void onDelete(AppEvent event) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void onChange(final AppEvent event) {
        service = (FileSystemServiceAsync) Registry.get(KernelServices.FS_SERVICE);
        service.getFiles(event.<String>getData(), new AsyncCallback<List<FileObjectDesktop>>() {

            public void onFailure(Throwable caught) {
                Dispatcher.forwardEvent(ExplorerEvents.ERROR, caught);
            }

            public void onSuccess(List<FileObjectDesktop> result) {
                AppEvent ae = new AppEvent(event.getType(), result);
                forwardToView(explorerView, ae);
            }
        });
    }

    private void onRename(AppEvent event) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void onRemove(AppEvent event) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void onMove(AppEvent event) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void onError(AppEvent event) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
