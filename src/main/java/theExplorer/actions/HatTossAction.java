package theExplorer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static com.megacrit.cardcrawl.core.Settings.ACTION_DUR_XFAST;

public class HatTossAction extends AbstractGameAction {
    private int amount;

    public HatTossAction(AbstractCreature target, int amount) {
        this.target = target;
        this.actionType = ActionType.DEBUFF;
        this.duration = ACTION_DUR_XFAST;
        this.amount = amount;
    }

    public void update() {
        if (this.target == null) {
            this.isDone = true;
        } else if (this.amount <= 0) {
            this.isDone = true;
        } else if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.clearPostCombatActions();
            this.isDone = true;
        } else {
            AbstractMonster randomMonster = AbstractDungeon.getMonsters().getRandomMonster((AbstractMonster) this.target, true, AbstractDungeon.cardRandomRng);
            if (randomMonster == null) {
                randomMonster = (AbstractMonster) this.target;
            }
            if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                addToTop(new HatTossAction(randomMonster, this.amount - 2));
            }

            if (randomMonster.currentHealth > 0) {
                addToTop(new DamageAction(randomMonster, new DamageInfo(AbstractDungeon.player, this.amount), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                addToTop(new WaitAction(0.1F));
            }

            this.isDone = true;
        }
    }
}
