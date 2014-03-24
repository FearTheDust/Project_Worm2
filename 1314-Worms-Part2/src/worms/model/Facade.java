package worms.model;

import worms.util.Position;

/**
 * @author Coosemans Brent
 * @author Derkinderen Vincent
 */
public class Facade implements IFacade {

	@Override
	public Worm createWorm(double x, double y, double direction, double radius, String name) {
		try {
			Position position = new Position(x,y);
			return new Worm(position, direction, radius, name);
		} catch(IllegalArgumentException ex) {
			throw new ModelException(ex.getMessage());
		}
	}

	@Override
	public boolean canMove(Worm worm, int nbSteps) {
		return nbSteps >= 0 && Worm.getMoveCost(nbSteps, worm.getAngle()) <= worm.getCurrentActionPoints();
	}

	@Override
	public void move(Worm worm, int nbSteps) throws ModelException {
		try {
			worm.move(nbSteps);
		} catch(IllegalArgumentException ex) {
			throw new ModelException(ex.getMessage());
		}
	}

	@Override
	public boolean canTurn(Worm worm, double angle) {
		return Worm.getTurnCost(angle) <= worm.getCurrentActionPoints();
	}

	@Override
	public void turn(Worm worm, double angle) {
		angle %= 2*Math.PI; //-180 -> 180
		
		if(angle < -Math.PI) {
			angle += 2*Math.PI;
		} else if(angle > Math.PI) {
			angle -= 2*Math.PI;
		}
		worm.turn(angle);
	}

	@Override
	public void jump(Worm worm) {
		try {
			worm.jump();
		} catch(IllegalArgumentException ex) {
			throw new ModelException(ex.getMessage());
		}
	}

	@Override
	public double getJumpTime(Worm worm) {
		return worm.jumpTime();
	}

	@Override
	public double[] getJumpStep(Worm worm, double t) {
		try {
			Position newPosition = worm.jumpStep(t);
			double[] position = { newPosition.getX(), newPosition.getY() };
			return position;
		} catch(IllegalArgumentException exc) {
			throw new ModelException(exc.getMessage());
		}
	}

	@Override
	public double getX(Worm worm) {
		return worm.getPosition().getX();
	}

	@Override
	public double getY(Worm worm) {
		return worm.getPosition().getY();
	}

	@Override
	public double getOrientation(Worm worm) {
		return worm.getAngle();
	}

	@Override
	public double getRadius(Worm worm) {
		return worm.getRadius();
	}

	@Override
	public void setRadius(Worm worm, double newRadius) throws ModelException {
		try {
			worm.setRadius(newRadius);
		} catch(IllegalArgumentException ex) {
			throw new ModelException(ex.getMessage());
		}
	}

	@Override
	public double getMinimalRadius(Worm worm) {
		return worm.getMinimumRadius();
	}

	@Override
	public int getActionPoints(Worm worm) {
		return worm.getCurrentActionPoints();
	}

	@Override
	public int getMaxActionPoints(Worm worm) {
		return worm.getMaximumActionPoints();
	}

	@Override
	public String getName(Worm worm) {
		return worm.getName();
	}

	@Override
	public void rename(Worm worm, String newName) {
		try {
			worm.setName(newName);
		} catch(IllegalArgumentException ex) {
			throw new ModelException(ex.getMessage());
		}
	}

	@Override
	public double getMass(Worm worm) {
		return worm.getMass();
	}

}
