package theExplorer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class RandomiseCostAction extends AbstractGameAction {
    private int costToDraw;
    private boolean firstTime;

    public RandomiseCostAction() {
        this.duration = 0.0F;
        this.actionType = ActionType.WAIT;
    }

    public void update() {
        if(DrawCardAction.drawnCards.size() > 0) {
            AbstractCard card = DrawCardAction.drawnCards.get(0);
            if (card.cost >= 0) {
                int newCost = AbstractDungeon.cardRandomRng.random(3);
                if (card.cost != newCost) {
                    card.cost = newCost;
                    card.costForTurn = card.cost;
                    card.isCostModified = true;
                }
                card.freeToPlayOnce = false;
            }
        }
        this.isDone = true;
    }
}
