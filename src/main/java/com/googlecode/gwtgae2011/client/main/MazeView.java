package com.googlecode.gwtgae2011.client.main;

import javax.inject.Inject;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

/**
 * The sudoku view presents a 9x9 grid of cells, each of which is editable.
 */
public class MazeView extends ViewImpl implements MazePresenter.MyView {

  static final String UPGRADE_MESSAGE = "Your browser does not support the HTML5 Canvas. "
      + "Please upgrade your browser to view this demo.";

  private final Scheduler scheduler;

  private final Composite widget;
  private final Widget content;
  private final Canvas canvas;
  private final Context2d context;

  private MazePresenter presenter;

  private int width;
  private int height;
  private int mazeSizeX;
  private int mazeSizeY;
  private Boolean[][] horizontalWalls;
  private Boolean[][] verticalWalls;

  private ScheduledCommand refreshCommand;
  
  @Inject
  public MazeView(Scheduler scheduler) {
    this.scheduler = scheduler;
    
    canvas = Canvas.createIfSupported();
    if (canvas == null) {
      content = new Label(UPGRADE_MESSAGE);
      context = null;
    } else {
      content = canvas;
      context = canvas.getContext2d();
      canvas.setWidth("90%");
      canvas.setHeight("90%");
      canvas.getElement().getStyle().setMarginLeft(5, Unit.PCT);
      canvas.getElement().getStyle().setMarginTop(5, Unit.PCT);
    }
    widget = new MyComposite();
  }

  @Override
  public Widget asWidget() {
    return widget;
  }

  @Override
  public void setPresenter(MazePresenter presenter) {
    this.presenter = presenter;
  }

  private class MyComposite extends Composite implements RequiresResize {
    MyComposite() {
      initWidget(content);
    }
    @Override
    public void onLoad() {
      super.onLoad();
      scheduler.scheduleDeferred(new ScheduledCommand(){
        @Override
        public void execute() {
          onResize();
        }
      });
    }
    
    @Override    
    public void onResize() {
      if (canvas == null || context == null) {
        return;
      }
      width = canvas.getOffsetWidth();
      height = canvas.getOffsetHeight();
      canvas.setCoordinateSpaceWidth(width);
      canvas.setCoordinateSpaceHeight(height);
      scheduleRefresh();
    }
  }

  @Override
  public void setMazeSize(int sizeX, int sizeY) {
    mazeSizeX = sizeX;
    mazeSizeY = sizeY;
    clearMaze();
  }

  @Override
  public void clearMaze() {
    horizontalWalls = new Boolean[mazeSizeX][mazeSizeY+1];
    verticalWalls = new Boolean[mazeSizeX+1][mazeSizeY];
    scheduleRefresh();
  }


  @Override
  public void addHorizontalWall(int posX, int posY) {
    horizontalWalls[posX][posY] = true;
    scheduleRefresh();
  }

  @Override
  public void addVerticalWall(int posX, int posY) {
    verticalWalls[posX][posY] = true;
    scheduleRefresh();
  }

  private void scheduleRefresh() {
    if (refreshCommand != null) {
      return;
    }
    
    refreshCommand = new ScheduledCommand(){
      @Override
      public void execute() {
        refresh();
        refreshCommand = null;
      }
    };
    scheduler.scheduleDeferred(refreshCommand);
  }
  
  private void refresh() {
    context.beginPath();
    for (int x = 0; x < mazeSizeX; ++x) {
      for (int y = 0; y <= mazeSizeY; ++y) {
        if (nonNullAndTrue(horizontalWalls[x][y])) {
          context.moveTo(xPosToPix(x), yPosToPix(y));
          context.lineTo(xPosToPix(x+1), yPosToPix(y));
        }
      }
    }
    for (int y = 0; y < mazeSizeY; ++y) {
      for (int x = 0; x <= mazeSizeX; ++x) {
        if (nonNullAndTrue(verticalWalls[x][y])) {
          context.moveTo(xPosToPix(x), yPosToPix(y));
          context.lineTo(xPosToPix(x), yPosToPix(y+1));
        }
      }
    }    
    context.stroke();
  }
  
  private boolean nonNullAndTrue(Boolean value) {
    return value != null && value;
  }

  private double xPosToPix(int xPos) {
    return xPos*width/(double)mazeSizeX;
  }
  
  private double yPosToPix(int yPos) {
    return yPos*height/(double)mazeSizeY;
  }
}
