package org.saiku.service;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import org.saiku.service.util.dto.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

/**
 * Created by bugg on 30/04/14.
 */
public class PlatformUtilsService {

  private static final Logger log = LoggerFactory.getLogger(PlatformUtilsService.class );
  private Resource pluginsDirectory;

  @Autowired
  public void setPluginsDirectory(Resource path) {
    this.pluginsDirectory = path;
  }

  public Resource getPluginsDirectory(){
    return pluginsDirectory;
  }


  public ArrayList getAvailablePlugins(){
    ArrayList l = new ArrayList<Plugin>(  );
    try {
		File f = pluginsDirectory.getFile();

		log.debug("lookup plugins from " + pluginsDirectory.getFile().getAbsolutePath());

		File[] directories = f.listFiles(new FilenameFilter() {
		  public boolean accept(File current, String name) {
		    return new File(current, name).isDirectory();
		  }
		});

		if(directories != null && directories.length>0) {
		  for ( File d : directories ) {
			log.debug("lookup plugin from " + d.getAbsolutePath());

		    File[] subfiles = d.listFiles();

		    /**
		     * TODO use a metadata.js file for alternative details.
		     */
		    if ( subfiles != null ) {
		      for ( File s : subfiles ) {
		        if ( s.getName().equals( "plugin.js" ) ) {
		          Plugin p = new Plugin( s.getParentFile().getName(), "", "js/saiku/plugins/" + s.getParentFile().getName() + "/plugin.js" );
		          l.add( p );
		        }
		      }
		    }
		  }
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    return l;
  }
}
