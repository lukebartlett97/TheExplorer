package theExplorer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import theExplorer.ExplorerMod;
import theExplorer.util.ApplyPowerListener;

public class AlertPowerAction extends AbstractGameAction {

    private AbstractPower powerToApply;
    private AbstractCreature target;

    public AlertPowerAction(AbstractPower powerToApply, AbstractCreature target) {
        this.powerToApply = powerToApply;
        this.target = target;
    }

    @Override
    public void update() {
        for(AbstractCard card : AbstractDungeon.player.drawPile.group) {
            if(card instanceof ApplyPowerListener) {
                ExplorerMod.logger.info("LUKE Notifying applyPower to card: " + card.cardID);
                ((ApplyPowerListener) card).onApplyPower(powerToApply, target);
            }
        }
        for(AbstractCard card : AbstractDungeon.player.hand.group) {
            if(card instanceof ApplyPowerListener) {
                ExplorerMod.logger.info("LUKE Notifying applyPower to card: " + card.cardID);
                ((ApplyPowerListener) card).onApplyPower(powerToApply, target);
            }
        }
        for(AbstractCard card : AbstractDungeon.player.discardPile.group) {
            if(card instanceof ApplyPowerListener) {
                ExplorerMod.logger.info("LUKE Notifying applyPower to card: " + card.cardID);
                ((ApplyPowerListener) card).onApplyPower(powerToApply, target);
            }
        }
        for(AbstractCard card : AbstractDungeon.player.exhaustPile.group) {
            if(card instanceof ApplyPowerListener) {
                ExplorerMod.logger.info("LUKE Notifying applyPower to card: " + card.cardID);
                ((ApplyPowerListener) card).onApplyPower(powerToApply, target);
            }
        }
        for(AbstractCard card : AbstractDungeon.player.limbo.group) {
            if(card instanceof ApplyPowerListener) {
                ExplorerMod.logger.info("LUKE Notifying applyPower to card: " + card.cardID);
                ((ApplyPowerListener) card).onApplyPower(powerToApply, target);
            }
        }
        for(AbstractRelic relic : AbstractDungeon.player.relics) {
            if(relic instanceof ApplyPowerListener) {
                ExplorerMod.logger.info("LUKE Notifying applyPower to relic: " + relic.relicId);
                ((ApplyPowerListener) relic).onApplyPower(powerToApply, target);
            }
        }
        this.isDone = true;
    }
}
