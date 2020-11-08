package theExplorer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class CompanionRemoveAction extends AbstractGameAction {
    private String cardID;

    public CompanionRemoveAction(String cardID) {

        this.cardID = cardID;
    }

    public void update() {
        AbstractDungeon.player.masterDeck.removeCard(this.cardID);
        AbstractDungeon.player.drawPile.removeCard(this.cardID);
        this.isDone = true;
    }
}
