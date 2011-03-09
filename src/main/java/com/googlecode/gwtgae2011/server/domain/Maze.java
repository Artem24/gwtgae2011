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

package com.googlecode.gwtgae2011.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Id;

import com.googlecode.gwtgae2011.shared.Vector;

/**
 * The domain object for a maze.
 */
public class Maze {
  @Id
  private Long id;
  
  private Integer version;
  
  private int sizeX;
  private int sizeY;
  
  @Embedded
  private List<Vector> horizontalWalls;
  
  @Embedded
  private List<Vector> verticalWalls;
  
  public Maze() {
    id = 1L;
    version = 0;
  }
  
  public Long getId() {
    return id;
  }
  
  public Integer getVersion() {
    return version++;
  }

  public Integer getSizeX() {
    return sizeX;
  }

  public Integer getSizeY() {
    return sizeY;
  }
  
  public List<Vector> getHorizontalWalls() {
    return horizontalWalls;
  }

  public List<Vector> getVerticalWalls() {
    return verticalWalls;
  }

  public static Maze findMaze(Long id) {
    return getMaze();
  }
  
  public static Maze getMaze() {
    Maze result = new Maze();
    result.sizeX = 3;
    result.sizeY = 3;
    result.horizontalWalls = new ArrayList<Vector>();
    result.horizontalWalls.add(new Vector(0,0));
    result.horizontalWalls.add(new Vector(1,0));
    result.horizontalWalls.add(new Vector(2,0));
    result.horizontalWalls.add(new Vector(0,1));
    result.horizontalWalls.add(new Vector(1,1));
    result.horizontalWalls.add(new Vector(1,2));
    result.horizontalWalls.add(new Vector(2,2));
    result.horizontalWalls.add(new Vector(0,3));
    result.horizontalWalls.add(new Vector(1,3));
    result.horizontalWalls.add(new Vector(2,3));
    result.verticalWalls = new ArrayList<Vector>();
    result.verticalWalls.add(new Vector(0,0));
    result.verticalWalls.add(new Vector(0,1));
    result.verticalWalls.add(new Vector(0,2));
    result.verticalWalls.add(new Vector(3,0));
    result.verticalWalls.add(new Vector(3,1));
    result.verticalWalls.add(new Vector(3,2));
    
    return result;
  }
}
