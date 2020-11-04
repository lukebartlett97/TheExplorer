package theExplorer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Iterator;

public class StockpileAction extends AbstractGameAction {
    private AbstractPlayer p;
    private boolean upgraded;

    public StockpileAction(boolean upgraded) {
        this.upgraded = upgraded;
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.p = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        Iterator var1;
        AbstractCard c;
        var1 = this.p.hand.group.iterator();

        while (var1.hasNext()) {
            c = (AbstractCard) var1.next();
            c.retain = true;
            if (c.canUpgrade() && upgraded) {
                c.upgrade();
                c.superFlash();
            }
            c.applyPowers();
        }

        this.isDone = true;
    }
}
