package com.googlecode.gwtgae2011.shared.service;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.RequestFactory;
import com.google.gwt.requestfactory.shared.Service;
import com.googlecode.gwtgae2011.server.domain.Maze;
import com.googlecode.gwtgae2011.shared.proxy.MazeProxy;

public interface GwtGae2011RequestFactory extends RequestFactory {

  /**
   * Service stub for methods in Maze
   */
  @Service(Maze.class)
  interface MazeRequest extends RequestContext
  {
    Request<MazeProxy> getMaze();
  }

  MazeRequest mazeRequest();
}
