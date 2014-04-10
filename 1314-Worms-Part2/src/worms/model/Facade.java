package worms.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import worms.model.equipment.weapons.Weapon;
import worms.model.world.World;
import worms.model.world.entity.Food;
import worms.model.world.entity.GameObject;
import worms.model.world.entity.Projectile;
import worms.model.world.entity.Worm;
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

	/*@Override
	public boolean canMove(Worm worm, int nbSteps) {
		return nbSteps >= 0 && Worm.getMoveCost(nbSteps, worm.getAngle()) <= worm.getCurrentActionPoints();
	}*/

	/*@Override
	public void move(Worm worm, int nbSteps) throws ModelException {
		try {
			worm.move(nbSteps);
		} catch(IllegalArgumentException ex) {
			throw new ModelException(ex.getMessage());
		}
	}*/

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

	@Override
	public void addEmptyTeam(World world, String newName) {
		world.add(new Team(newName));
	}

	@Override
	public void addNewFood(World world) {
		Position position = world.getRandomAdjacentPos(Constants.FOOD_RADIUS);
		if(position!=null)
			createFood(world, position.getX(), position.getY());
	}

	@Override
	public void addNewWorm(World world) {
		Position position = world.getRandomAdjacentPos(1);
		if(position!=null)
			createWorm(world, position.getX(), position.getY(), 0, 1, "Eric"+(int) position.getX()+(int) position.getY());

	}

	@Override
	public boolean canFall(Worm worm) {
		return worm.canFall();
	}

	@Override
	public boolean canMove(Worm worm) {
		return worm.isAlive() && worm.getMoveCost(worm.getMovePosition()) <= worm.getCurrentActionPoints();
	}

	@Override
	public Food createFood(World world, double x, double y) {
		Food newFood = new Food(world, new Position(x,y));
		world.add(newFood);
		return newFood;
	}

	@Override
	public World createWorld(double width, double height,
			boolean[][] passableMap, Random random) {
		return new World(width, height, passableMap, random);
	}

	@Override
	public Worm createWorm(World world, double x, double y, double direction,
			double radius, String name) {
		return new Worm(world, new Position(x,y), direction, radius, name);
	}

	@Override
	public void fall(Worm worm) {
		worm.fall();

	}

	@Override
	public Projectile getActiveProjectile(World world) {
		return world.getLivingProjectile();
	}

	@Override
	public Worm getCurrentWorm(World world) {
		return world.getActiveWorm();
	}

	@Override
	public Collection<Food> getFood(World world) {
		return world.getFood();
	}

	@Override
	public int getHitPoints(Worm worm) {
		return worm.getCurrentHitPoints();
	}

	@Override
	public double[] getJumpStep(Projectile projectile, double t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getJumpTime(Projectile projectile, double timeStep) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getJumpTime(Worm worm, double timeStep) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxHitPoints(Worm worm) {
		return worm.getMaximumHitPoints();
	}

	@Override
	public double getRadius(Food food) {
		return food.getRadius();
	}

	@Override
	public double getRadius(Projectile projectile) {
		return projectile.getRadius();
	}

	@Override
	public String getSelectedWeapon(Worm worm) {
		Weapon weapon = worm.getCurrentWeapon();

		if(weapon == null)
			return null;

		return worm.getCurrentWeapon().getName();
	}

	@Override
	public String getTeamName(Worm worm) {
		Team team = worm.getTeam();

		if(team == null) {
			return "";
		} else {
			return team.getName();
		}
	}

	@Override
	public String getWinner(World world) {
		return world.getWinner();
	}

	@Override
	public Collection<Worm> getWorms(World world) {
		return world.getWorms();
	}

	@Override
	public double getX(Food food) {
		return food.getPosition().getX();
	}

	@Override
	public double getX(Projectile projectile) {
		return projectile.getPosition().getX();
	}

	@Override
	public double getY(Food food) {
		return food.getPosition().getY();
	}

	@Override
	public double getY(Projectile projectile) {
		return projectile.getPosition().getY();
	}

	@Override
	public boolean isActive(Food food) {
		return food.isAlive();
	}

	@Override
	public boolean isActive(Projectile projectile) {
		return projectile.isAlive();
	}

	@Override
	public boolean isAdjacent(World world, double x, double y, double radius) {
		return world.isAdjacent(new Position(x,y), radius);
	}

	@Override
	public boolean isAlive(Worm worm) {
		return worm.isAlive();
	}

	@Override
	public boolean isGameFinished(World world) {
		return world.gameEnded();
	}

	@Override
	public boolean isImpassable(World world, double x, double y, double radius) {
		return world.isImpassable(new Position(x,y), radius);
	}

	@Override
	public void jump(Projectile projectile, double timeStep) {
		// TODO Auto-generated method stub

	}

	@Override
	public void jump(Worm worm, double timeStep) {
		// TODO Auto-generated method stub

	}

	@Override
	public void move(Worm worm) {
		worm.move();
	}

	@Override
	public void selectNextWeapon(Worm worm) {
		worm.setCurrentWeapon(worm.getNextWeapon());	
	}

	@Override
	public void shoot(Worm worm, int yield) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startGame(World world) {
		//TODO add worms & co
		world.startGame();
	}

	@Override
	public void startNextTurn(World world) {
		world.nextTurn();
	}

}
