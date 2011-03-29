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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.inject.Provider;
import com.googlecode.gwtgae2011.client.NameTokens;
import com.googlecode.gwtgae2011.shared.Stroke;
import com.googlecode.gwtgae2011.shared.proxy.SketchProxy;
import com.googlecode.gwtgae2011.shared.proxy.StrokeProxy;
import com.googlecode.gwtgae2011.shared.service.GwtGae2011RequestFactory.SketchRequest;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

/**
 * A presenter that shows a {@link SketchView}.
 */
public class SketchPresenter extends Presenter<SketchPresenter.MyView, SketchPresenter.MyProxy> {

  private final Provider<SketchRequest> sketchRequestProvider;
  private final List<Stroke> strokesToAdd = new ArrayList<Stroke>();
  
  @Inject
  public SketchPresenter(EventBus eventBus, MyView view, MyProxy proxy,
      Provider<SketchRequest> sketchRequestProvider) {
    super(eventBus, view, proxy);
    view.setPresenter(this);
    this.sketchRequestProvider = sketchRequestProvider;
  }

  @ProxyStandard
  @NameToken(NameTokens.SKETCH)
  public interface MyProxy extends ProxyPlace<SketchPresenter> { }

  private Long id;
  private SketchProxy sketchProxy;
  private boolean callInProgress;
  private String currentTitle;
  
  public interface MyView extends View {
    public void setPresenter(SketchPresenter presenter);
    public void clear();
    public void addStroke(Stroke stroke);
    void setTitle(String title);
  }

  @Override
  protected void revealInParent() {
    RevealContentEvent.fire(this, MainPresenter.CENTRAL_SLOT_TYPE, this);
  }

  @Override
  protected void onReset() {
    super.onReset();
    sketchProxy = null;
    getView().setTitle("Untitled");
    getView().clear();
    if (id != null) {
      callInProgress = true;
      sketchRequestProvider.get().fetch(id).with("strokes").fire(new Receiver<SketchProxy>() {
        @Override
        public void onSuccess(SketchProxy sketchProxy) {
          SketchPresenter.this.sketchProxy = sketchProxy;
          draw(sketchProxy);
          callInProgress = false;
          callServerIfNeeded();
        }
      });
    }
  }

  private void draw(SketchProxy sketchProxy) {
    getView().setTitle(sketchProxy.getTitle());
    List<StrokeProxy> list = sketchProxy.getStrokes();
    for (StrokeProxy stroke : list) {
      getView().addStroke(Stroke.fromProxy(stroke));
    }
  }

  public void addNewStroke(Stroke stroke) {
    getView().addStroke(stroke);
    strokesToAdd.add(stroke);
    callServerIfNeeded();
  }

  public void setTitle(String title) {
    if (currentTitle == null || !currentTitle.equals(title)) {
      currentTitle = title;
      getView().setTitle(title);  
      callServerIfNeeded();
    }
  }
  
  private void callServerIfNeeded() {
    if (callInProgress) {
      return;
    }
    
    SketchRequest request = sketchRequestProvider.get();
    if (sketchProxy == null) {
      sketchProxy = request.create(SketchProxy.class);
    }

    if (!strokesToAdd.isEmpty() || 
        currentTitle != null && !currentTitle.equals(sketchProxy.getTitle())) {

      SketchProxy editable = request.edit(sketchProxy);
      
      List<StrokeProxy> strokeProxiesToAdd = new ArrayList<StrokeProxy>(strokesToAdd.size());
      for (Stroke stroke : strokesToAdd) {
        strokeProxiesToAdd.add(stroke.toProxy(request));
      }
      strokesToAdd.clear();
      editable.setTitle(currentTitle);

      callInProgress = true;
      request.addStrokes(editable, strokeProxiesToAdd).fire(new Receiver<SketchProxy>() {
        @Override
        public void onSuccess(SketchProxy sketchProxy) {
          SketchPresenter.this.sketchProxy = sketchProxy;
          callInProgress = false;
          callServerIfNeeded();
        }
        @Override
        public void onFailure(ServerFailure error) {
          super.onFailure(error);
          callInProgress = false;
        }
      });
    }
  }

  @Override
  public void prepareFromRequest(PlaceRequest request) {
    String idString = request.getParameter("id", "");
    try {
      id = Long.parseLong(idString);
    } catch (NumberFormatException e) {
      id = null;
    }
  }
}
