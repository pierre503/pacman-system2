/******************************************************************************
 * This work is applicable to the conditions of the MIT License,              *
 * which can be found in the LICENSE file, or at                              *
 * https://github.com/philippwinter/pacman/blob/master/LICENSE                *
 *                                                                            *
 * Copyright (c) 2013 Philipp Winter, Jonas Heidecke & Niklas Kaddatz         *
 ******************************************************************************/

package model;

import model.exception.ObjectAlreadyInListException;

import java.util.Vector;
import java.util.Iterator;

public class FruitContainer implements Container<Fruit> {

    private Vector<Fruit> fruits;

    public final int max;

    public FruitContainer() {
        this.max = 4;
        this.fruits = new Vector<>(max);
    }

    public void add(Fruit fruit) {
        if (!this.fruits.contains(fruit)) {
            this.fruits.add(fruit);
        } else {
            throw new ObjectAlreadyInListException(fruits.getClass().getCanonicalName());
        }
    }

    /**
     * Adds the elements of another container of the same type.
     *
     * @param container The other container.
     */
    @Override
    public void add(Container<Fruit> container) {
        for (Fruit p : container) {
            this.add(p);
        }
    }

    public Fruit get(int i) {
        return this.fruits.get(i);
    }

    public Fruit get(Position pos) {
        for (Fruit p : this.fruits) {
            if (p.getPosition().equals(pos)) {
                return p;
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public Vector<Fruit> getAll() {
        return (Vector<Fruit>) this.fruits.clone();
    }

    /**
     * Removes an element from the container.
     *
     * @param el The element to remove.
     */
    @Override
    public void remove(Fruit el) {
        this.fruits.remove(el);
    }

    public Iterator<Fruit> iterator() {
        return fruits.iterator();
    }

    public boolean contains(Fruit p) {
        return this.fruits.contains(p);
    }

    public int size() {
        return this.fruits.size();
    }

    public void removeAll() {
        for (Fruit p : this) {
            p.deSpawn();
        }

        this.fruits.clear();
    }
}