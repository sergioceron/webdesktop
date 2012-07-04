package resources.src.com.extjs.gxt.samples.resources.client;

import com.google.gwt.core.client.GWT;
import resources.src.com.extjs.gxt.samples.resources.client.icons.ExampleIcons;
import resources.src.com.extjs.gxt.samples.resources.client.images.ExampleImages;

public class Resources {

  public static final ExampleImages IMAGES = GWT.create(ExampleImages.class);
  public static final ExampleIcons ICONS = GWT.create(ExampleIcons.class);

}
