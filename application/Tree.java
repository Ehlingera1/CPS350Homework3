
package application;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Tree {
    private Node root;

    /******************************* Implementation Here *****************************************/

    private boolean isRed(Node node) {
        return node != null && node.isRed();
    }

    private boolean contains(Node current, int value) {
        while (current != null) {
            if (value == current.getValue()) {
                return true;
            } else if (value < current.getValue()) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }
        return false;
    }

    private void rotateLeft(Node h) {
        Node x = h.getRight();

        h.setRight(x.getLeft());

        if (x.getLeft() != null) {
            x.getLeft().setParent(h);
            x.getLeft().setLeft_child_of_parent(false);
        }

        x.setParent(h.getParent());

        if (h.getParent() == null) {
            root = x;
        } else if (h.isLeft_child_of_parent()) {
            h.getParent().setLeft(x);
        } else {
            h.getParent().setRight(x);
        }

        x.setLeft(h);
        h.setParent(x);
        h.setLeft_child_of_parent(true);

        x.setLeft_child_of_parent(
            x.getParent() != null && x.getParent().getLeft() == x
        );
    }

    private void rotateRight(Node h) {
        Node x = h.getLeft();

        h.setLeft(x.getRight());

        if (x.getRight() != null) {
            x.getRight().setParent(h);
            x.getRight().setLeft_child_of_parent(true);
        }

        x.setParent(h.getParent());

        if (h.getParent() == null) {
            root = x;
        } else if (h.isLeft_child_of_parent()) {
            h.getParent().setLeft(x);
        } else {
            h.getParent().setRight(x);
        }

        x.setRight(h);
        h.setParent(x);
        h.setLeft_child_of_parent(false);

        x.setLeft_child_of_parent(
            x.getParent() != null && x.getParent().getLeft() == x
        );
    }

    private void fixInsertion(Node node) {
        while (node != root && isRed(node.getParent())) {
            Node parent = node.getParent();
            Node grandparent = parent.getParent();

            if (parent == grandparent.getLeft()) {
                Node uncle = grandparent.getRight();

                if (isRed(uncle)) {
                    parent.setColor(Node.BLACK);
                    uncle.setColor(Node.BLACK);
                    grandparent.setColor(Node.RED);
                    node = grandparent;
                } else {
                    if (node == parent.getRight()) {
                        node = parent;
                        rotateLeft(node);
                        parent = node.getParent();
                        grandparent = parent.getParent();
                    }

                    parent.setColor(Node.BLACK);
                    grandparent.setColor(Node.RED);
                    rotateRight(grandparent);
                }
            } else {
                Node uncle = grandparent.getLeft();

                if (isRed(uncle)) {
                    parent.setColor(Node.BLACK);
                    uncle.setColor(Node.BLACK);
                    grandparent.setColor(Node.RED);
                    node = grandparent;
                } else {
                    if (node == parent.getLeft()) {
                        node = parent;
                        rotateRight(node);
                        parent = node.getParent();
                        grandparent = parent.getParent();
                    }

                    parent.setColor(Node.BLACK);
                    grandparent.setColor(Node.RED);
                    rotateLeft(grandparent);
                }
            }
        }

        root.setColor(Node.BLACK);
        root.setParent(null);
        root.setLeft_child_of_parent(false);
    }

    public boolean insertNode(Node node) {
        if (node == null) {
            return false;
        }

        if (contains(root, node.getValue())) {
            return false;
        }

        node.setLeft(null);
        node.setRight(null);
        node.setParent(null);
        node.setColor(Node.RED);
        node.setLeft_child_of_parent(false);

        Node parent = null;
        Node current = root;

        while (current != null) {
            parent = current;

            if (node.getValue() < current.getValue()) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }

        node.setParent(parent);

        if (parent == null) {
            root = node;
        } else if (node.getValue() < parent.getValue()) {
            parent.setLeft(node);
            node.setLeft_child_of_parent(true);
        } else {
            parent.setRight(node);
            node.setLeft_child_of_parent(false);
        }

        fixInsertion(node);

        return true;
    }

    /******************************* End of Implementation **************************************/

    /******************************* GUI Code — unchanged ***************************************/

    private int tree_max_height;
    private int tree_max_width;
    private Canvas canvas;
    private GraphicsContext gc;
    private int canvas_width = 640;
    private int canvas_height = 480;
    private Vector<Vector<Node>> layer_nodes = new Vector<Vector<Node>>();
    private Node new_node;
    private double old_dragging_x;
    private double old_dragging_y;
    private boolean dragging = false;
    private Node selected_node;
    private double delta_x;
    private double delta_y;
    private int radius = 30;
    private int select_node_value;

    public Tree() {
    }

    public Tree(Canvas c, GraphicsContext gc) {
        canvas = c;
        this.gc = gc;
    }

    private void organizeTree() {
        layer_nodes.clear();
    }

    private void setNewProp(Node node) {
        setNewNodeProperties(node);
        if (node.getLeft() != null) setNewProp(node.getLeft());
        if (node.getRight() != null) setNewProp(node.getRight());
    }

    public void showTree(boolean insertion_occur) {
        if (insertion_occur) {
            setNewProp(root);
        }

        bfsTreeDraw(this);
    }

    private void updateTreePos(Node moving_node, double delta_x, double delta_y) {
        Queue<Node> queue = new LinkedList<Node>();
        Node current_node;

        queue.add(moving_node);

        while (!queue.isEmpty()) {
            current_node = queue.remove();

            if (current_node.getLeft() != null) {
                Point2D pos = new Point2D(
                    current_node.getLeft().getPosition().getX() + delta_x,
                    current_node.getLeft().getPosition().getY() + delta_y
                );
                current_node.getLeft().setPosition(pos);
                queue.add(current_node.getLeft());
            }

            if (current_node.getRight() != null) {
                Point2D pos = new Point2D(
                    current_node.getRight().getPosition().getX() + delta_x,
                    current_node.getRight().getPosition().getY() + delta_y
                );
                current_node.getRight().setPosition(pos);
                queue.add(current_node.getRight());
            }
        }
    }

    private void bfsTreeDraw(Tree tree) {
        Queue<Node> queue;
        Node current_node;

        if (tree.getRoot() == null) {
            return;
        }

        queue = new LinkedList<Node>();
        queue.add(tree.getRoot());

        while (!queue.isEmpty()) {
            current_node = queue.remove();
            current_node.showNode(select_node_value);

            if (current_node.getLeft() != null) {
                double start_x = current_node.getPosition().getX() + radius / 2;
                double start_y = current_node.getPosition().getY() + radius / 2;
                double end_x = current_node.getLeft().getPosition().getX() + radius / 2;
                double end_y = current_node.getLeft().getPosition().getY() + radius / 2;

                gc.setStroke(Color.BLACK);
                gc.strokeLine(start_x, start_y, end_x, end_y);
                current_node.showNode(select_node_value);
                queue.add(current_node.getLeft());
            }

            if (current_node.getRight() != null) {
                double start_x = current_node.getPosition().getX() + radius / 2;
                double start_y = current_node.getPosition().getY() + radius / 2;
                double end_x = current_node.getRight().getPosition().getX() + radius / 2;
                double end_y = current_node.getRight().getPosition().getY() + radius / 2;

                gc.setStroke(Color.BLACK);
                gc.strokeLine(start_x, start_y, end_x, end_y);
                current_node.showNode(select_node_value);
                queue.add(current_node.getRight());
            }
        }
    }

    private void setNewNodeProperties(Node target_node) {
        Queue<Node> queue;
        Node current_node;
        layer_nodes.clear();

        if (target_node == root) {
            target_node.setDepth(0);
            target_node.setParent(null);
            target_node.setPosition(new Point2D(320, 5));
            target_node.setDepth(0);

            Vector<Node> layer = new Vector<Node>();
            layer.add(target_node);
            layer_nodes.add(layer);
            target_node.setLayer_idx(0);
            return;
        }

        queue = new LinkedList<Node>();
        queue.add(getRoot());

        while (!queue.isEmpty()) {
            current_node = queue.remove();

            if (layer_nodes.size() - 1 < current_node.getDepth()) {
                Vector<Node> layer = new Vector<Node>();
                layer.add(current_node);
                layer_nodes.add(layer);
                current_node.setLayer_idx(0);
            } else {
                layer_nodes.get(current_node.getDepth()).add(current_node);
                current_node.setLayer_idx(
                    layer_nodes.get(current_node.getDepth()).size() - 1
                );
            }

            if (current_node.getLeft() == target_node) {
                target_node.setDepth(current_node.getDepth() + 1);
                target_node.setParent(current_node);
                target_node.setLeft_child_of_parent(true);
            } else if (current_node.getRight() == target_node) {
                target_node.setDepth(current_node.getDepth() + 1);
                target_node.setParent(current_node);
                target_node.setLeft_child_of_parent(false);
            }

            if (current_node.getLeft() != null) {
                queue.add(current_node.getLeft());
            }
            if (current_node.getRight() != null) {
                queue.add(current_node.getRight());
            }
        }

        setNodePosition(target_node);
    }

    void setNodePosition(Node target_node) {
        double x_left_offset =
            -(double) radius * 6.0 / target_node.getDepth() + 2.0;
        double x_right_offset =
            (double) radius * 6.0 / target_node.getDepth() + 2.0;
        int y_offset = radius * 2;

        boolean layer_crowded = false;
        boolean tree_too_high = false;

        Point2D pos;

        if (target_node.isLeft_child_of_parent()) {
            pos = new Point2D(
                target_node.getParent().getPosition().getX() + x_left_offset,
                target_node.getParent().getPosition().getY() + y_offset
            );
        } else {
            pos = new Point2D(
                target_node.getParent().getPosition().getX() + x_right_offset,
                target_node.getParent().getPosition().getY() + y_offset
            );
        }

        target_node.setPosition(pos);

        if (target_node.getLayer_idx() > 0) {
            int left_neighbor = target_node.getLayer_idx() - 1;
            Point2D left_pos =
                layer_nodes.get(target_node.getDepth())
                           .get(left_neighbor).getPosition();

            if (pos.getX() - left_pos.getX() < radius) {
                layer_crowded = true;
                System.out.println(
                    "Overlapping with left! " +
                    layer_nodes.get(target_node.getDepth())
                               .get(left_neighbor).getValue()
                );
            }
        }
    }

    public void finishNodeDragging(double x, double y) {
        dragging = false;
        delta_x = 0;
        delta_y = 0;
    }

    public void checkNodeDragging(double x, double y) {
        for (int j = 0; j < layer_nodes.size(); j++) {
            for (int i = 0; i < layer_nodes.get(j).size(); i++) {
                double node_x =
                    layer_nodes.get(j).get(i).getPosition().getX();
                double node_y =
                    layer_nodes.get(j).get(i).getPosition().getY();

                if (((node_x + radius / 2 - x) * (node_x + radius / 2 - x)
                    + (node_y + radius / 2 - y) * (node_y + radius / 2 - y))
                    < radius * radius / 4) {

                    dragging = true;
                    selected_node = layer_nodes.get(j).get(i);
                    old_dragging_x = x;
                    old_dragging_y = y;
                    select_node_value = selected_node.getValue();

                    gc.clearRect(0, 0, canvas_width, canvas_height);
                    bfsTreeDraw(this);
                    break;
                }
            }

            if (dragging) {
                break;
            }
        }

        if (!dragging) {
            select_node_value = -1;
            gc.clearRect(0, 0, canvas_width, canvas_height);
            bfsTreeDraw(this);
        }
    }

    public void doNodeDragging(double x, double y) {
        if (dragging) {
            delta_x = x - old_dragging_x;
            delta_y = y - old_dragging_y;

            Point2D pos = new Point2D(
                selected_node.getPosition().getX() + delta_x,
                selected_node.getPosition().getY() + delta_y
            );

            selected_node.setPosition(pos);
            old_dragging_x = x;
            old_dragging_y = y;

            updateTreePos(selected_node, delta_x, delta_y);
            gc.clearRect(0, 0, canvas_width, canvas_height);
            bfsTreeDraw(this);
        }
    }

    void adjustTreeWidth(int layer_idx) {
        int x_left_offset = -radius * 3;
        int x_right_offset = radius * 3;
        int y_offset = radius * 2;

        int target_layer_width =
            layer_nodes.get(layer_idx).size() * radius * 2
            + (layer_nodes.get(layer_idx).size() - 1)
            * x_right_offset * 2;

        Point2D layer_center =
            new Point2D(canvas_width / 2, new_node.getPosition().getY());

        if (target_layer_width < canvas_width) {
            if (layer_nodes.get(layer_idx).size() % 2 == 0) {
                int left_node_besides_center =
                    layer_nodes.get(layer_idx).size() / 2;
                int right_node_besides_center =
                    layer_nodes.get(layer_idx).size() / 2 + 1;

                System.out.println("center: " + layer_center.getX());

                Point2D pos = new Point2D(
                    layer_center.getX() + x_left_offset,
                    layer_center.getY()
                );

                System.out.println("left: " + pos.getX());

                layer_nodes.get(layer_idx)
                           .get(left_node_besides_center).setPosition(pos);

                for (int i = left_node_besides_center - 1; i >= 0; i--) {
                    pos = new Point2D(
                        layer_nodes.get(layer_idx).get(i + 1)
                                   .getPosition().getX() + x_left_offset * 2,
                        layer_center.getY()
                    );
                    layer_nodes.get(layer_idx).get(i).setPosition(pos);
                }

                pos = new Point2D(
                    layer_center.getX() + x_right_offset,
                    layer_center.getY()
                );

                System.out.println("Right: " + pos.getX());

                layer_nodes.get(layer_idx)
                           .get(left_node_besides_center).setPosition(pos);

                for (int i = right_node_besides_center + 1;
                     i < layer_nodes.get(layer_idx).size(); i++) {
                    pos = new Point2D(
                        layer_nodes.get(layer_idx).get(i - 1)
                                   .getPosition().getX() + x_right_offset * 2,
                        layer_center.getY()
                    );
                    layer_nodes.get(layer_idx).get(i).setPosition(pos);
                }
            } else {
                int node_at_center =
                    (layer_nodes.get(layer_idx).size() + 1) / 2;

                Point2D pos =
                    new Point2D(layer_center.getX(), layer_center.getY());

                layer_nodes.get(layer_idx).get(node_at_center)
                           .setPosition(pos);

                for (int i = node_at_center - 1; i >= 0; i--) {
                    pos = new Point2D(
                        layer_nodes.get(layer_idx).get(i + 1)
                                   .getPosition().getX() + x_left_offset * 2,
                        layer_center.getY()
                    );
                    layer_nodes.get(layer_idx).get(i).setPosition(pos);
                }

                for (int i = node_at_center + 1;
                     i < layer_nodes.get(layer_idx).size(); i++) {
                    pos = new Point2D(
                        layer_nodes.get(layer_idx).get(i - 1)
                                   .getPosition().getX() + x_right_offset * 2,
                        layer_center.getY()
                    );
                    layer_nodes.get(layer_idx).get(i).setPosition(pos);
                }
            }
        }
    }

    private void setBasicTreeProperties(Tree tree) {
        Queue<Node> queue;
        Node current_node;

        if (tree.getRoot() == null) {
            return;
        }

        tree.getRoot().setDepth(0);
        tree.getRoot().setParent(null);

        queue = new LinkedList<Node>();
        queue.add(tree.getRoot());

        while (!queue.isEmpty()) {
            current_node = queue.remove();

            if (layer_nodes.size() - 1 < current_node.getDepth()) {
                Vector<Node> layer = new Vector<Node>();
                layer.add(current_node);
                layer_nodes.add(layer);
            } else {
                layer_nodes.get(current_node.getDepth()).add(current_node);
            }

            if (current_node.getLeft() != null) {
                current_node.getLeft().setParent(current_node);
                current_node.getLeft().setLeft_child_of_parent(true);
                current_node.getLeft().setDepth(current_node.getDepth() + 1);
                queue.add(current_node.getLeft());
            }

            if (current_node.getRight() != null) {
                current_node.getRight().setParent(current_node);
                current_node.getRight().setLeft_child_of_parent(false);
                current_node.getRight().setDepth(current_node.getDepth() + 1);
                queue.add(current_node.getRight());
            }
        }
    }

    void changePropogation() {
        Node node = root;

        Point2D pos = new Point2D(320, 0);
        root.setPosition(pos);

        int x_left_offset = -radius * 3;
        int x_right_offset = radius * 3;
        int y_offset = radius * 2;

        for (int i = 1; i < layer_nodes.size(); i++) {
            for (int j = 0; j < layer_nodes.get(i).size(); j++) {
                if (layer_nodes.get(i).get(j).isLeft_child_of_parent()) {
                    pos = new Point2D(
                        layer_nodes.get(i).get(j).getParent()
                                  .getPosition().getX() + x_left_offset,
                        layer_nodes.get(i).get(j).getParent()
                                  .getPosition().getY() + y_offset
                    );
                } else {
                    pos = new Point2D(
                        layer_nodes.get(i).get(j).getParent()
                                  .getPosition().getX() + x_right_offset,
                        layer_nodes.get(i).get(j).getParent()
                                  .getPosition().getY() + y_offset
                    );
                }

                layer_nodes.get(i).get(j).setPosition(pos);
            }
        }

        for (int y = layer_nodes.size() - 1; y >= 0; y--) {
            boolean width_issue = false;
            boolean height_issue = false;

            for (int x = 0; x < layer_nodes.get(y).size(); x++) {
                if (layer_nodes.get(y).get(x).getPosition().getX() < radius / 2
                    || layer_nodes.get(y).get(x).getPosition().getX()
                       > (canvas_width - radius / 2)) {
                    width_issue = true;
                } else if (layer_nodes.get(y).get(x).getPosition().getY()
                           > (canvas_height - radius / 2)) {
                }
            }
        }
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Node getNew_node() {
        return new_node;
    }

    public void setNew_node(Node new_node) {
        this.new_node = new_node;
    }

    public int getSelect_node_value() {
        return select_node_value;
    }

    public void setSelect_node_value(int select_node_value) {
        this.select_node_value = select_node_value;
    }

    public Node getSelectedNode() {
        return selected_node;
    }

    public void setSelectedNode(Node dragged_node) {
        this.selected_node = dragged_node;
    }
}
