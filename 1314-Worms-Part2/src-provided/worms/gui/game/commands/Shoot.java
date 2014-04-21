package worms.gui.game.commands;

import worms.gui.GUIConstants;
import worms.gui.game.PlayGameScreen;
import worms.gui.game.sprites.ProjectileSprite;
import worms.gui.messages.MessageType;
import worms.model.IFacade;
import worms.model.ModelException;
import worms.model.world.entity.Projectile;
import worms.model.world.entity.Worm;

public class Shoot extends Command {
	private final Worm worm;
	private boolean finished = false;

	private final int propulsionYield;
	private Projectile projectile;
	private double totalDuration;
	private boolean hasJumped;

	public Shoot(IFacade facade, Worm worm, int propulsionYield,
			PlayGameScreen screen) {
		super(facade, screen);
		this.worm = worm;
		this.propulsionYield = propulsionYield;
	}

	@Override
	protected boolean canStart() {
		return worm != null;
	}

	@Override
	protected void doStartExecution() {
		try {
			getFacade().shoot(worm, propulsionYield);
			projectile = getFacade().getActiveProjectile(getWorld());
			if (projectile != null) {
				totalDuration = getFacade().getJumpTime(projectile,
						GUIConstants.JUMP_TIME_STEP);
				//TODO: Debug
				//System.out.println("totalDuration " + totalDuration + " calculatedX: " + projectile.jumpStep(totalDuration).getX() + " calculatedY: " + projectile.jumpStep(totalDuration).getY());
				
				ProjectileSprite sprite = new ProjectileSprite(getScreen(),
						projectile);
				sprite.setCenterLocation(
						getScreen().getScreenX(getFacade().getX(projectile)),
						getScreen().getScreenY(getFacade().getY(projectile)));
				sprite.setSize(getScreen().worldToScreenDistance(
						getFacade().getRadius(projectile)));
				getScreen().addSprite(sprite);
			} else {
				cancelExecution();
			}
		} catch (ModelException e) {
			e.printStackTrace();
			cancelExecution();
		}
	}

	@Override
	protected void afterExecutionCancelled() {
		getScreen().addMessage("This worm cannot shoot :(", MessageType.ERROR);
	}

	@Override
	protected boolean isExecutionCompleted() {
		return finished;
	}

	@Override
	protected void doUpdate(double dt) {
		try {
			if (getElapsedTime() >= totalDuration) {
				if (!hasJumped) {
					hasJumped = true;
					getFacade().jump(projectile, GUIConstants.JUMP_TIME_STEP);
					//TODO Delete DEBUG
					//System.out.println("final time " + getElapsedTime() + " at x:" + projectile.getPosition().getX() + " and y:" + projectile.getPosition().getY());
					
					finished = true;
				}
			} else {
				ProjectileSprite sprite = getScreen().getSpriteOfTypeFor(
						ProjectileSprite.class, projectile);

				double[] xy = getFacade().getJumpStep(projectile,
						getElapsedTime());
				//TODO Delete DEBUG
				//System.out.println("update time " + getElapsedTime() + " x:" + xy[0] + " y:" + xy[1]);
				
				sprite.setCenterLocation(getScreen().getScreenX(xy[0]),
						getScreen().getScreenY(xy[1]));
			}
		} catch (ModelException e) {
			e.printStackTrace();
			cancelExecution();
		}
	}
}