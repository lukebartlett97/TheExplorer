package theExplorer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.lang.reflect.InvocationTargetException;

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
