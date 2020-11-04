package theExplorer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theExplorer.cards.attacks.RockToss;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RockTossAction extends AbstractGameAction {

    public RockTossAction() {

    }

    @Override
    public void update() {
        AbstractDungeon.player.drawPile.group.stream()
                .filter(card -> card.cardID.equals(RockToss.ID))
                .findFirst()
                .ifPresent(this::drawCard);
        this.isDone = true;
    }

    private void drawCard(AbstractCard card) {
        if (AbstractDungeon.player.hand.size() < 10) {
            AbstractDungeon.player.hand.addToHand(card);
            card.unhover();
            card.setAngle(0.0F, true);
            card.lighten(false);
            card.drawScale = 0.12F;
            card.targetDrawScale = 0.75F;
            card.applyPowers();
            AbstractDungeon.player.drawPile.removeCard(card);
            addToBot(new RockTossAction());
        } else {
            AbstractDungeon.player.createHandIsFullDialog();
        }
        AbstractDungeon.player.hand.refreshHandLayout();
        AbstractDungeon.player.hand.glowCheck();
    }
}
