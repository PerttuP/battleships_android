package org.hupisoft.battleships_ai;

import org.hupisoft.battleships_core.Coordinate;
import org.hupisoft.battleships_core.IRestrictedGameArea;
import org.hupisoft.battleships_core.IShip;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

class HitProbabilityCalculator  implements IHitProbabilityCalculator {

    @Override
    public int getProbabilityFactor(IRestrictedGameArea area, Coordinate location) {
        int probabilityFactor = 0;

        if (isPossibleShipLocation(location, area)) {
            List<Integer> shipLengths = area.remainingShipLengths();
            for (Integer shipLength : shipLengths) {
                probabilityFactor += getProbabilityFactorForShip(area, location, shipLength);
            }
        }

        return  probabilityFactor;
    }

    private int getProbabilityFactorForShip(IRestrictedGameArea area,
                                            Coordinate location,
                                            int shipLength)
    {
        int spaceInLeft = calculateFreeSpaceInLeft(area, location, shipLength);
        int spaceInRight = calculateFreeSpaceInRight(area, location, shipLength);
        int spaceInUp = calculateFreeSpaceInUp(area, location, shipLength);
        int spaceInDown = calculateFreeSpaceInDown(area, location, shipLength);

        int horizontalFactor = Math.max(0, 2 + spaceInLeft + spaceInRight - shipLength);
        int verticalFactor = Math.max(0, 2 + spaceInDown + spaceInUp - shipLength);
        return horizontalFactor + verticalFactor;
    }

    private int calculateFreeSpaceInLeft(IRestrictedGameArea area, Coordinate start, int limit) {
        int space = 0;
        for (int x = start.x()-1; x > start.x() - limit; --x) {
            Coordinate test = new Coordinate(x, start.y());
            if (isPossibleShipLocation(test, area)) {
                space += 1;
            }
            else {
                break;
            }
        }
        return space;
    }

    private int calculateFreeSpaceInRight(IRestrictedGameArea area, Coordinate start, int limit){
        int space = 0;
        for (int x = start.x()+1; x < start.x() + limit; ++x) {
            Coordinate test = new Coordinate(x, start.y());
            if (isPossibleShipLocation(test, area)) {
                space += 1;
            }
            else {
                break;
            }
        }
        return space;
    }

    private int calculateFreeSpaceInUp(IRestrictedGameArea area, Coordinate start, int limit){
        int space = 0;
        for (int y = start.y()-1; y > start.y() - limit; --y) {
            Coordinate test = new Coordinate(start.x(), y);
            if (isPossibleShipLocation(test, area)) {
                space += 1;
            }
            else {
                break;
            }
        }
        return space;
    }

    private int calculateFreeSpaceInDown(IRestrictedGameArea area, Coordinate start, int limit) {
        int space = 0;
        for (int y = start.y() + 1; y < start.y() + limit; ++y) {
            Coordinate test = new Coordinate(start.x(), y);
            if (isPossibleShipLocation(test, area)) {
                space += 1;
            } else {
                break;
            }
        }
        return space;
    }

    private boolean isPossibleShipLocation(Coordinate location, IRestrictedGameArea area)
    {
        boolean result = false;
        if (isLocationInAreaBounds(location, area))
        {
            if (!area.isHit(location)) {
                Set<Coordinate> restricted = new TreeSet<>();
                List<IShip> destroyedShips = area.destroyedShips();
                for (IShip ship : destroyedShips) {
                    restricted.addAll(ship.getRestrictedCoordinates());
                }
                result = !restricted.contains(location);
            }
        }
        return result;
    }

    private boolean isLocationInAreaBounds(Coordinate location, IRestrictedGameArea area)
    {
        return location.x() >= 0 && location.x() < area.width()
                && location.y() >= 0 && location.y() < area.height();
    }
}
