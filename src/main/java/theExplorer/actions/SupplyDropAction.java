package theExplorer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SupplyDropAction extends AbstractGameAction {
    private int costToDraw;
    private boolean firstTime;

    public SupplyDropAction(int costToDraw, boolean firstTime) {
        this.firstTime = firstTime;
        this.duration = 0.0F;
        this.actionType = ActionType.WAIT;
        this.costToDraw = costToDraw;
    }

    public void update() {
        if (!firstTime) {
            this.costToDraw -= DrawCardAction.drawnCards.get(0).costForTurn;
        }
        if (AbstractDungeon.player.hand.size() < 10 && this.costToDraw > 0 && !(AbstractDungeon.player.drawPile.isEmpty() && AbstractDungeon.player.discardPile.isEmpty())) {
            addToBot(new DrawCardAction(1));
            addToBot(new SupplyDropAction(this.costToDraw, false));
        }

        this.isDone = true;
    }
}
