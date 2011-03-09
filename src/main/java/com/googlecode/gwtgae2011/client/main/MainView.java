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

package com.googlecode.gwtgae2011.client.main;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

/**
 * The main view is a simple screen split horizontally. The top bar is meant to contain a 
 * navigation bar, whereas the bottom part is meant to display the central content.
 */
public class MainView extends ViewImpl implements MainPresenter.MyView {

  private static final int TOP_SIZE = 50;
  
  private final DockLayoutPanel widget = new DockLayoutPanel(Unit.PX);

  public Widget north;
  public Widget center;
  
  @Override
  public Widget asWidget() {
    return widget;
  }

  @Override
  public void setInSlot(Object slot, Widget content) {
    if(slot == MainPresenter.TOP_SLOT_TYPE) {
      removeIfNotNull(north);
      north = content;
      widget.addNorth(north, TOP_SIZE);
    } else if(slot == MainPresenter.CENTRAL_SLOT_TYPE) {
      removeIfNotNull(center);
      center = content;
      widget.add(center);
    } else {
      super.setInSlot(slot, content);
    }
  }

  private void removeIfNotNull(Widget children) {
    if (children != null) {
      widget.remove(children);
    }
  }
  
}
