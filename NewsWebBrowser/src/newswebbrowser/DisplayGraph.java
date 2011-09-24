/*
 * Copyright (c) 2003, the JUNG Project and the Regents of the University of
 * California All rights reserved.
 * 
 * This software is open-source under the BSD license; see either "license.txt"
 * or http://jung.sourceforge.net/license.txt for a description.
 * 
 */
package newswebbrowser;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import java.util.Collection;
import javax.swing.JApplet;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.functors.ConstantTransformer;

import edu.uci.ics.jung.algorithms.layout.BalloonLayout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.Tree;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;
import edu.uci.ics.jung.visualization.transform.MutableTransformerDecorator;
import java.awt.Event;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.event.MouseInputListener;
import org.apache.commons.collections15.Transformer;

/**
 * Demonstrates the visualization of a Tree using TreeLayout
 * and BalloonLayout. An examiner lens performing a hyperbolic
 * transformation of the view is also included.
 * 
 * @author Tom Nelson
 * 
 */
@SuppressWarnings("serial")
public class DisplayGraph extends JApplet {

    /**
     * the graph
     */
    Forest<String, Integer> graph;
    Factory<DirectedGraph<String, Integer>> graphFactory =
            new Factory<DirectedGraph<String, Integer>>() {

                public DirectedGraph<String, Integer> create() {
                    return new DirectedSparseMultigraph<String, Integer>();
                }
            };
    Factory<Tree<String, Integer>> treeFactory =
            new Factory<Tree<String, Integer>>() {

                public Tree<String, Integer> create() {
                    return new DelegateTree<String, Integer>(graphFactory);
                }
            };
    Factory<Integer> edgeFactory = new Factory<Integer>() {

        int i = 0;

        public Integer create() {
            return i++;
        }
    };
    Factory<String> vertexFactory = new Factory<String>() {

        int i = 0;

        public String create() {
            return "V" + i++;
        }
    };
    /**
     * the visual component and renderer for the graph
     */
    VisualizationViewer<String, Integer> vv;
    VisualizationServer.Paintable rings;
    String root;
    BalloonLayout<String, Integer> radialLayout;
    ArrayList<ActionListener> mouseMovementListeners = new ArrayList<ActionListener>();
    ArrayList<ActionListener> mouseClickListeners = new ArrayList<ActionListener>();
    int eventID = 0;

    /**
     * provides a Hyperbolic lens for the view
     */
    public DisplayGraph() {

        // create a simple graph for the demo
        graph = new DelegateForest<String, Integer>();

        createTree();
        radialLayout = new BalloonLayout<String, Integer>(graph);
        radialLayout.setSize(new Dimension(400, 400));
        vv = new VisualizationViewer<String, Integer>(radialLayout, new Dimension(400, 400));
        vv.setBackground(Color.white);
        vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        // add a listener for ToolTips
//        vv.setVertexToolTipTransformer(new ToStringLabeller());
        vv.getRenderContext().setArrowFillPaintTransformer(new ConstantTransformer(Color.lightGray));
        rings = new Rings(radialLayout);
        vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).setToIdentity();
        vv.addPreRenderPaintable(rings);
        vv.setVertexToolTipTransformer(new Transformer() {

            @Override
            public Object transform(Object i) {
                Collection<String> vertices = graph.getVertices();
                Object source = null;
                ArrayList array = new ArrayList();
                array.addAll(vertices);
                for (Object v : array) {
                    String vertex = (String) v;
                    graph.addEdge(edgeFactory.create(), vertex, "test");
                    Point2D nodeLocation = radialLayout.getCenter("test");
                    graph.removeVertex("test");
                    if (nodeLocation.distanceSq(vv.getMousePosition()) < 100) {
                        source = vertex;
                    }
                    break;

                }
                if (source != null) {
                    //TODO retrieve the description
                }
                return source;
            }
        });

        Container content = getContentPane();
        GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
        content.add(panel);

        DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();

        vv.setGraphMouse(graphMouse);
        vv.addKeyListener(graphMouse.getModeKeyListener());
        graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);

        vv.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent me) {
            }

            @Override
            public void mouseMoved(MouseEvent me) {
                Collection<String> vertices = graph.getVertices();
                Object source = null;
                ArrayList array = new ArrayList();
                array.addAll(vertices);
                for (Object v : array) {
                    String vertex = (String) v;
                    graph.addEdge(edgeFactory.create(), vertex, "test");
                    Point2D nodeLocation = radialLayout.getCenter("test");
                    graph.removeVertex("test");
                    if (nodeLocation.distanceSq(me.getPoint()) < 100) {
                        source = vertex;


                        ActionEvent event = new ActionEvent(source, eventID, null);
                        eventID++;
                        //passes source object, which is a string describing the node title
                        for (ActionListener al : mouseMovementListeners) {
                            al.actionPerformed(event);
                        }
                        break;
                    }
                }

            }
        });
        vv.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent me) {

                Collection<String> vertices = graph.getVertices();
                Object source = null;
                ArrayList array = new ArrayList();
                array.addAll(vertices);
                for (Object v : array) {
                    String vertex = (String) v;
                    graph.addEdge(edgeFactory.create(), vertex, "test");
                    Point2D nodeLocation = radialLayout.getCenter("test");
                    graph.removeVertex("test");
                    if (nodeLocation.distanceSq(me.getPoint()) < 100) {
                        source = vertex;
                        ActionEvent event = new ActionEvent(source, eventID, null);
                        eventID++;
                        //passes source object, which is a string describing the node title
                        for (ActionListener al : mouseMovementListeners) {
                            al.actionPerformed(event);
                        }
                        break;
                    }
                }





            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                vv.setBackground(Color.darkGray);
                vv.setForeground(Color.white);
                vv.getRenderContext().setVertexFillPaintTransformer(new Transformer() {

                    @Override
                    public Object transform(Object i) {
                        return Color.blue;
                    }
                });
                ;
                vv.repaint();
            }

            @Override
            public void mouseExited(MouseEvent me) {
                vv.setBackground(Color.white);
                vv.setForeground(Color.black);
                vv.repaint();
                vv.getRenderContext().setVertexFillPaintTransformer(new Transformer() {

                    @Override
                    public Object transform(Object i) {
                        return Color.red;
                    }
                });
            }
        });

    }

    /**
     * 
     * @param hub- The hub that you want to attach the article
     * @param title- the title of the article, may be switched out for additional metadata to generate icon image
     */
    public void addArticle(String hub, String title) {
        graph.addEdge(edgeFactory.create(), hub, title);
        radialLayout = new BalloonLayout<String, Integer>(graph);
        radialLayout.setSize(new Dimension(400, 400));
        vv.getModel().setGraphLayout(radialLayout);
        vv.repaint();
        rings = new Rings(radialLayout);
        vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).setToIdentity();
        vv.addPreRenderPaintable(rings);
    }

    public void addMouseMovementListener(ActionListener al) {
        mouseMovementListeners.add(al);
    }

    public void addMouseClickistener(ActionListener al) {
        mouseMovementListeners.add(al);
    }

    private void fireMouseMovementListener(ActionEvent evt) {
        for (ActionListener al : mouseMovementListeners) {
            al.actionPerformed(evt);
        }
    }

    public void moveViewTo(String hub) {


        MutableTransformer modelTransformer = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
        try {
            graph.addEdge(edgeFactory.create(), hub, "test");
            Point2D hubLocation = radialLayout.getCenter("test");
            graph.removeVertex("test");
            Point2D center = new Point(vv.getWidth() / 2, vv.getHeight() / 2);
//                Point2D q = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(down);
//                Point2D p = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(e.getPoint());
            float dx = (float) (center.getX() - hubLocation.getX());
            float dy = (float) (center.getY() - hubLocation.getY());

            modelTransformer.translate(dx, dy);

        } catch (RuntimeException ex) {
            ex.printStackTrace();
        }

        vv.repaint();
    }

    class Rings implements VisualizationServer.Paintable {

        BalloonLayout<String, Integer> layout;

        public Rings(BalloonLayout<String, Integer> layout) {
            this.layout = layout;
        }

        public void paint(Graphics g) {
            g.setColor(Color.gray);

            Graphics2D g2d = (Graphics2D) g;

            Ellipse2D ellipse = new Ellipse2D.Double();
            for (String v : layout.getGraph().getVertices()) {
                Double radius = layout.getRadii().get(v);
                if (radius == null) {
                    continue;
                }
                Point2D p = layout.transform(v);
                ellipse.setFrame(-radius, -radius, 2 * radius, 2 * radius);
                AffineTransform at = AffineTransform.getTranslateInstance(p.getX(), p.getY());
                Shape shape = at.createTransformedShape(ellipse);

                MutableTransformer viewTransformer =
                        vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW);

                if (viewTransformer instanceof MutableTransformerDecorator) {
                    shape = vv.getRenderContext().getMultiLayerTransformer().transform(shape);
                } else {
                    shape = vv.getRenderContext().getMultiLayerTransformer().transform(Layer.LAYOUT, shape);
                }

//                g2d.draw(shape);
            }
        }

        public boolean useTransform() {
            return true;
        }
    }

    /**
     * 
     */
    private void createTree() {
        graph.addVertex("Central Hub");
        graph.addEdge(edgeFactory.create(), "Central Hub", "Sports");
        graph.addEdge(edgeFactory.create(), "Central Hub", "Politics");
        graph.addEdge(edgeFactory.create(), "Central Hub", "Finance");
        graph.addEdge(edgeFactory.create(), "Central Hub", "Arts");
        graph.addEdge(edgeFactory.create(), "Central Hub", "World");
//        graph.addEdge(edgeFactory.create(), "Sports", "Basketball");
//        graph.addEdge(edgeFactory.create(), "Sports", "Tennis");

    }
}