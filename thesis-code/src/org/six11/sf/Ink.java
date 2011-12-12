package org.six11.sf;

import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.six11.util.gui.BoundingBox;
import org.six11.util.pen.ConvexHull;
import org.six11.util.pen.Pt;
import org.six11.util.pen.Sequence;

import static org.six11.util.Debug.bug;

/**
 * 
 * 
 * @author Gabe Johnson <johnsogg@cmu.edu>
 */
public class Ink {

  protected long created;
  protected Rectangle2D bounds;
  protected Area area;
  protected Path2D path;
  protected boolean analyzed;
  protected Sequence seq;
  protected Set<Guide> guides;

  public Ink(Sequence seq) {
    this.seq = seq;
    created = System.currentTimeMillis();
    this.guides = new HashSet<Guide>();
  }

  public Rectangle2D getBounds() {
    if (bounds == null) {
      BoundingBox bb = new BoundingBox(seq.getPoints());
      bounds = bb.getRectangle();
    }
    return bounds;
  }

  public Area getArea() {
    if (area == null) {
      area = new Area(seq);
    }
    return area;
  }

  public Path2D getPath() {
    if (path == null) {
      path = new GeneralPath(seq);
    }
    return path;
  }

  public boolean isClosed() {
    return false;
  }

  public Sequence getSequence() {
    return seq;
  }

  /**
   * Returns a fraction (0..1) of how many points of the stroke are in the target area.
   */
  public double getOverlap(Area target) {
    double numHits = 0;
    for (Pt pt : seq) {
      if (target.contains(pt)) {
        numHits = numHits + 1;
      }
    }
    return numHits / (double) seq.size();
  }

  public Ink copy() {
    return new Ink(seq.copy());
  }

  public void move(double dx, double dy) {
    for (Pt moveMe : seq) {
      moveMe.setLocation(moveMe.getX() + dx, moveMe.getY() + dy);
    }
  }
  
  public boolean isAnalyzed() {
    return analyzed;
  }

  public void setAnalyzed(boolean v) {
    analyzed = v;
  }

  public ConvexHull getHull() {
    ConvexHull ret = new ConvexHull(seq.getPoints());
    return ret;
  }

  @SuppressWarnings("unchecked")
  public List<Segment> getSegments() {
    return (List<Segment>) seq.getAttribute(CornerFinder.SEGMENTS);
  }

  public void setGuides(Set<Guide> retainedVisibleGuides) {
    guides.clear();
    guides.addAll(retainedVisibleGuides);
    for (Guide g : guides) {
      bug("  Guide: " + g);
    }
  }

}
