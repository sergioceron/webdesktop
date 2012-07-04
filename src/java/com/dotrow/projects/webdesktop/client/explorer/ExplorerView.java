/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dotrow.projects.webdesktop.client.explorer;

import com.dotrow.projects.webdesktop.client.explorer.model.FileObjectDesktop;
import com.dotrow.projects.webdesktop.client.explorer.resources.ExplorerResources;
import com.dotrow.projects.webdesktop.client.explorer.services.FileSystemServiceAsync;
import com.dotrow.projects.webdesktop.client.services.KernelServices;
import com.extjs.gxt.desktop.client.Desktop;
import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridGroupRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupColumnData;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuBar;
import com.extjs.gxt.ui.client.widget.menu.MenuBarItem;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sxceron
 */
public class ExplorerView extends View {

    private Grid<FileObjectDesktop> grid = null;

    public ExplorerView(Controller controller) {
        super(controller);
    }

    private Container topPanel() {
        Menu nuevo = new Menu();
        nuevo.add(new MenuItem("Nuevo"));
        nuevo.add(new SeparatorMenuItem());
        nuevo.add(new MenuItem("Abrir pestaña"));
        nuevo.add(new MenuItem("Cerrar pestaña"));
        nuevo.add(new SeparatorMenuItem());
        nuevo.add(new MenuItem("Renombrar"));
        nuevo.add(new MenuItem("Mover a la papelera"));
        nuevo.add(new MenuItem("Eliminar"));
        nuevo.add(new SeparatorMenuItem());
        nuevo.add(new MenuItem("Propiedades"));
        nuevo.add(new SeparatorMenuItem());
        nuevo.add(new MenuItem("Salir"));

        Menu editar = new Menu();
        editar.add(new MenuItem("Cut"));
        editar.add(new MenuItem("Copy"));

        MenuBar bar = new MenuBar();
        bar.setBorders(true);
        bar.setStyleAttribute("borderTop", "none");

        bar.add(new MenuBarItem("File", nuevo));
        bar.add(new MenuBarItem("Edit", editar));

        ToolBar toolBar = new ToolBar();
        Button go = new Button();
        go.setIcon(ExplorerResources.ICONS.go());
        toolBar.add(go);

        LayoutContainer lc = new LayoutContainer();
        lc.add(bar);
        lc.add(toolBar);
        return lc;
    }

    private Widget centerPanel(List<FileObjectDesktop> files) {
        //ContentPanel center = new ContentPanel();

        GroupingStore<FileObjectDesktop> store = new GroupingStore<FileObjectDesktop>();
        store.add(files);

        ColumnConfig name = new ColumnConfig("nombre", "Nombre", 40);
        ColumnConfig path = new ColumnConfig("path", "Ruta", 20);
        ColumnConfig owner = new ColumnConfig("owner", "Usuario", 20);
        ColumnConfig type = new ColumnConfig("type", "Tipo", 20);
        ColumnConfig size = new ColumnConfig("size", "Tamaño", 20);
        ColumnConfig last = new ColumnConfig("lastModification", "Ultima", 20);
        ColumnConfig attrib = new ColumnConfig("attributes", "A", 20);

        last.setDateTimeFormat(DateTimeFormat.getFormat("MM/dd/y"));
        attrib.setHidden(true);
        path.setHidden(true);
        owner.setHidden(true);

        CheckBoxSelectionModel<FileObjectDesktop> sm = new CheckBoxSelectionModel<FileObjectDesktop>();
        List<ColumnConfig> config = new ArrayList<ColumnConfig>();
        config.add(sm.getColumn());
        config.add(name);
        config.add(path);
        config.add(owner);
        config.add(type);
        config.add(size);
        config.add(last);
        config.add(attrib);

        final ColumnModel cm = new ColumnModel(config);

        GroupingView view = new GroupingView();
        view.setForceFit(true);
        view.setGroupRenderer(new GridGroupRenderer() {

            public String render(GroupColumnData data) {
                String f = cm.getColumnById(data.field).getHeader();
                String l = data.models.size() == 1 ? "Item" : "Items";
                return f + ": " + data.group + " (" + data.models.size() + " " + l + ")";
            }
        });

        grid = new Grid<FileObjectDesktop>(store, cm);
        grid.setView(view);
        grid.setBorders(true);
        grid.setSelectionModel(sm);
        grid.addPlugin(sm);

        return grid;
    }

    private ContentPanel leftPanel() {
        ContentPanel left = new ContentPanel();
        left.setLayout(new FitLayout());

        TreeLoader<FileObjectDesktop> loader;
        TreePanel<FileObjectDesktop> tree;
        final FileSystemServiceAsync service = (FileSystemServiceAsync) Registry.get(KernelServices.FS_SERVICE);

        RpcProxy<List<FileObjectDesktop>> proxy = new RpcProxy<List<FileObjectDesktop>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<List<FileObjectDesktop>> callback) {
                service.getTreeFolders((FileObjectDesktop) loadConfig, callback);
            }
        };

        loader = new BaseTreeLoader<FileObjectDesktop>(proxy) {

            @Override
            public boolean hasChildren(FileObjectDesktop parent) {
                return parent.getType().equals("Folder");
            }
        };

        TreeStore<FileObjectDesktop> store = new TreeStore<FileObjectDesktop>(loader);
        store.setKeyProvider(new ModelKeyProvider<FileObjectDesktop>() {

            public String getKey(FileObjectDesktop model) {
                return model.<String>get("nombre");
            }
        });
        store.setStoreSorter(new StoreSorter<FileObjectDesktop>() {

            @Override
            public int compare(Store<FileObjectDesktop> store, FileObjectDesktop m1, FileObjectDesktop m2, String property) {
                boolean m1Folder = m1.getType().equals("Folder");
                boolean m2Folder = m2.getType().equals("Folder");

                if (m1Folder && !m2Folder) {
                    return -1;
                } else if (!m1Folder && m2Folder) {
                    return 1;
                }

                return m1.getNombre().compareTo(m2.getNombre());
            }
        });

        tree = new TreePanel<FileObjectDesktop>(store);
        tree.setStateful(true);
        tree.setDisplayProperty("nombre");
        // statefull components need a defined id
        tree.setId("statefullasynctreepanel");
        tree.setIconProvider(new ModelIconProvider<FileObjectDesktop>() {

            public AbstractImagePrototype getIcon(FileObjectDesktop model) {
                if (!(model.getType().equals("Folder"))) {
                    String ext = model.getNombre().substring(model.getNombre().lastIndexOf(".") + 1);

                    // new feature, using image paths rather than style names
                    if ("xml".equals(ext)) {
                        return IconHelper.createPath("samples/images/icons/page_white_code.png");
                    } else if ("java".equals(ext)) {
                        return IconHelper.createPath("samples/images/icons/page_white_cup.png");
                    } else if ("html".equals(ext)) {
                        return IconHelper.createPath("samples/images/icons/html.png");
                    } else {
                        return IconHelper.createPath("samples/images/icons/page_white.png");
                    }
                }
                return null;
            }
        });

        tree.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<FileObjectDesktop>() {
            @Override
            public void selectionChanged(SelectionChangedEvent<FileObjectDesktop> se) {
                FileObjectDesktop e = se.getSelection().get(0);
                if (e != null && e instanceof FileObjectDesktop) {
                    FileObjectDesktop fod = (FileObjectDesktop)e;
                    if( !"Folder".equals(e.getType()) )
                        Info.display("Event", "File: " + e.getPath());
                    else
                        Dispatcher.forwardEvent(ExplorerEvents.CHANGE, e.getPath());
                }
            }
        });

        left.add(tree);

        return left;
    }

    public void initUI(Desktop d, List<FileObjectDesktop> files) {
        Window w = new Window();
        w.setIcon(IconHelper.createStyle("icon-grid"));
        w.setMinimizable(true);
        w.setMaximizable(true);
        w.setHeading("Grid Window");
        w.setSize(700, 500);
        w.setLayout(new FitLayout());

        w.setTopComponent(topPanel());

        Viewport viewport = new Viewport();
        viewport.setLayout(new BorderLayout());
        BorderLayoutData center = new BorderLayoutData(LayoutRegion.CENTER);
        center.setMargins(new Margins(5, 5, 5, 5));
        BorderLayoutData west = new BorderLayoutData(LayoutRegion.WEST);
        west.setMargins(new Margins(5, 5, 5, 5));
        west.setCollapsible(true);
        west.setFloatable(true);
        west.setSplit(true);

        viewport.add(centerPanel(files), center);
        viewport.add(leftPanel(), west);

        w.add(viewport);

        d.addWindow(w);
        if (w != null && !w.isVisible()) {
            w.show();
        } else {
            w.toFront();
        }
    }

    public void changeDirectory(List<FileObjectDesktop> files){
        grid.getStore().removeAll();
        grid.getStore().add(files);
    }

    @Override
    protected void handleEvent(AppEvent event) {
        if (event.getType() == ExplorerEvents.OPEN) {
            initUI((Desktop) event.getData("desktop"), (List<FileObjectDesktop>) event.getData());
        }else if(event.getType() == ExplorerEvents.CHANGE) {
            changeDirectory((List<FileObjectDesktop>) event.getData());
        }
    }
}
