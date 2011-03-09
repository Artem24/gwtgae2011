/**
 * Copyright 2011 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.googlecode.gwtgae2011.shared.proxy;

import java.util.List;

import com.google.gwt.requestfactory.shared.EntityProxy;
import com.google.gwt.requestfactory.shared.ProxyFor;
import com.googlecode.gwtgae2011.server.domain.Maze;

@ProxyFor(Maze.class)
public interface MazeProxy extends EntityProxy {
  Long getId();
  
  Integer getVersion();
  
  Integer getSizeX();
  Integer getSizeY();
  
  List<VectorProxy> getHorizontalWalls();
  List<VectorProxy> getVerticalWalls();
}
