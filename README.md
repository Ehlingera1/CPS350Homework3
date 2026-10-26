# CPS 350 - Homework 3: Red-Black Trees

## Overview

This project implements a red-black tree in Java. A red-black tree is a self-balancing binary search tree that maintains its structure using node colors and rotations.

## Features

- Inserts values into the tree while maintaining binary search tree ordering.
- Uses red and black node colors to maintain red-black tree properties.
- Uses left and right rotations to rebalance the tree.
- Uses recoloring to correct violations after insertion.
- Displays the tree using the provided JavaFX interface.
- Supports visualizing the tree structure as nodes are inserted.

## Red-Black Tree Properties

1. Each node is either red or black.
2. The root node is black.
3. Null leaves are treated as black.
4. A red node cannot have a red child.
5. Every path from a node to its descendant null leaves contains the same number of black nodes.

## Project Structure

- `Tree.java` — Implements tree insertion, rotations, balancing, and tree visualization.
- `Node.java` — Represents an individual tree node, including its value, color, and links to its parent and children.
- Other application files — Provide the JavaFX interface and supporting functionality.

## Technologies

- Java
- JavaFX
- Red-black tree data structure
