package theExplorer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class LuckyStrikeAction extends AbstractGameAction {
    private int damage;
    private AbstractMonster target;
    private DamageInfo.DamageType damageTypeForTurn;

    public LuckyStrikeAction(int damage, AbstractMonster target, DamageInfo.DamageType damageTypeForTurn) {
        this.target = target;
        this.damageTypeForTurn = damageTypeForTurn;
        this.duration = 0.0F;
        this.actionType = ActionType.WAIT;
        this.damage = damage;
    }

    public void update() {
        for (AbstractCard c : DrawCardAction.drawnCards) {
            if (c.type == AbstractCard.CardType.ATTACK) {
                addToTop(new DamageAction(target, new DamageInfo(AbstractDungeon.player, this.damage, this.damageTypeForTurn)));
                break;
            }
        }

        this.isDone = true;
    }
}
