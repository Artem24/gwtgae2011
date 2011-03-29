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
import java.util.Date;
import java.util.List;

import javax.persistence.Embedded;

import com.googlecode.gwtgae2011.shared.Stroke;

/**
 * The domain object for a maze.
 */
public class Sketch extends DatastoreObject {
  
  private String title = "Untitled";
  
  private Date creationDate = new Date();
  
  @Embedded
  private List<Stroke> strokes;
  
  public Sketch() {
    strokes = new ArrayList<Stroke>();
  }
  
  public String getTitle() {
    return title;
  }
  
  public void setTitle(String title) {
    this.title = title;
  }
  
  public Date getCreationDate() {
    return creationDate;
  }
  
  public List<Stroke> getStrokes() {
    return strokes;
  }
  
  public void setStrokes(List<Stroke> strokes) {
    this.strokes = strokes;
  }

  public void addStrokes(List<Stroke> strokes) {
    this.strokes.addAll(strokes);
  }
}
