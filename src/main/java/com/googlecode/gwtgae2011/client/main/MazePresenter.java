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

import javax.inject.Inject;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.requestfactory.shared.Receiver;
import com.googlecode.gwtgae2011.client.NameTokens;
import com.googlecode.gwtgae2011.shared.proxy.MazeProxy;
import com.googlecode.gwtgae2011.shared.proxy.VectorProxy;
import com.googlecode.gwtgae2011.shared.service.GwtGae2011RequestFactory;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

/**
 * A presenter that shows a sudoku puzzle.
 */
public class MazePresenter extends Presenter<MazePresenter.MyView, MazePresenter.MyProxy> {

  private final GwtGae2011RequestFactory requestFactory;
  @Inject
  public MazePresenter(EventBus eventBus, MyView view, MyProxy proxy,
      GwtGae2011RequestFactory requestFactory) {
    super(eventBus, view, proxy);
    this.requestFactory = requestFactory;
    view.setPresenter(this);
  }

  @ProxyStandard
  @NameToken(NameTokens.SUDOKU)
  public interface MyProxy extends ProxyPlace<MazePresenter> { }

  public interface MyView extends View {
    public void setPresenter(MazePresenter presenter);
    public void setMazeSize(int sizeX, int sizeY);
    public void clearMaze();
    public void addHorizontalWall(int posX, int posY);
    public void addVerticalWall(int posX, int posY);
  }

  @Override
  protected void revealInParent() {
    RevealContentEvent.fire(this, MainPresenter.CENTRAL_SLOT_TYPE, this);
  }

  public void onReveal() {
    getView().clearMaze();
    requestFactory.mazeRequest().getMaze().with("horizontalWalls", 
        "verticalWalls").fire(new Receiver<MazeProxy>() {
      @Override
      public void onSuccess(MazeProxy maze) {
        fillMaze(maze);
      }
    });
  }

  private void fillMaze(MazeProxy maze) {
    getView().setMazeSize(maze.getSizeX(), maze.getSizeY());
    for (VectorProxy pos : maze.getHorizontalWalls()) {
      getView().addHorizontalWall(pos.getX(), pos.getY());
    }
    for (VectorProxy pos : maze.getVerticalWalls()) {
      getView().addVerticalWall(pos.getX(), pos.getY());
    }
  }
}
