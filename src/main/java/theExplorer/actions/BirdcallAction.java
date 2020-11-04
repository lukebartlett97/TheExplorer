package theExplorer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theExplorer.cards.BrewCard;

import java.util.ArrayList;

public class BirdcallAction extends AbstractGameAction {

    private boolean upgraded;

    public BirdcallAction(boolean upgraded) {
        this.upgraded = upgraded;
    }

    @Override
    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        reduceCosts(p.hand.group);
        if(upgraded) {
            reduceCosts(p.discardPile.group);
            reduceCosts(p.drawPile.group);
        }
        this.isDone = true;
    }

    private void reduceCosts(ArrayList<AbstractCard> cards) {
        for (AbstractCard card : cards) {
            if(card.costForTurn > 0 && !card.freeToPlayOnce) {
                card.costForTurn -= 1;
                card.isCostModifiedForTurn = true;
            }
        }
    }
}
