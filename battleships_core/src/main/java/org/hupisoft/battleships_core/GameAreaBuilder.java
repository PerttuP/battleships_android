package org.hupisoft.battleships_core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Implements the IGameAreaBuilder interface.
 * @see IGameAreaBuilder
 */
class GameAreaBuilder implements IGameAreaBuilder {

    private Random mRng = null;

    /**
     * Constructor.
     * @param rng Random number generator.
     */
    GameAreaBuilder(Random rng) {
        mRng = rng;
    }

    @Override
    public IGameArea createInitialGameArea(int width, int height, int[] shipLengths) {
        ArrayList<ArrayList<ISquare>> squares = createSquares(width, height);
        ArrayList<IShip> ships = createShips(shipLengths);
        IGameArea area = new GameArea(squares, ships);
        placeShips(area);

        return area;
    }

    private ArrayList<ArrayList<ISquare>> createSquares(int width, int height) {
        ArrayList<ArrayList<ISquare>> squares = new ArrayList<>();
        for (int x = 0; x < width; ++x) {
            ArrayList<ISquare> col = new ArrayList<>();
            for (int y = 0; y < height; ++y) {
                ISquare sqr = new Square(new Coordinate(x,y));
                col.add(sqr);
            }
            squares.add(col);
        }
        return squares;
    }

    private ArrayList<IShip> createShips(int[] shipLengths) {
        ArrayList<IShip> ships = new ArrayList<>();
        for (int i : shipLengths) {
            ships.add(new Ship(i));
        }
        return ships;
    }

    private void setShipOrientation(IShip ship) {
        IShip.Orientation orientation = IShip.Orientation.VERTICAL;
        if (mRng.nextBoolean()) {
            orientation = IShip.Orientation.HORISONTAL;
        }
        ship.setOrientation(orientation);
    }

    private void placeShips(IGameArea area) {
        ArrayList<IShip> ships = area.getShips();
        ArrayList<Coordinate> restrictedLocations = new ArrayList<>();

        for (IShip ship : ships) {
            boolean placingOk = false;
            while (!placingOk) {
                setShipOrientation(ship);
                int lastX = area.width();
                int lastY = area.height();
                if (ship.orientation() == IShip.Orientation.HORISONTAL) {
                    lastX = area.width() - ship.length() + 1;
                } else {
                    lastY = area.height() - ship.length() + 1;
                }
                int x = mRng.nextInt(lastX);
                int y = mRng.nextInt(lastY);
                Coordinate c = new Coordinate(x,y);
                ship.setBowCoordinates(c);

                ArrayList<Coordinate> occupied = ship.getOccupiedCoordinates();
                boolean restricted = !Collections.disjoint(occupied, restrictedLocations);

                if (!restricted) {
                    restrictedLocations.addAll(ship.getRestrictedCoordinates());
                    placingOk = true;
                }
            }
        }
    }
}
