package theExplorer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class FractureAction extends AbstractGameAction {
    AbstractMonster monster;
    private DamageInfo damageInfo;
    private AttackEffect attackEffect;

    public FractureAction(AbstractMonster m, DamageInfo damageInfo, AttackEffect attackEffect) {
        this.monster = m;
        this.damageInfo = damageInfo;
        this.attackEffect = attackEffect;
    }


    @Override
    public void update() {
        if(monster.currentBlock > 0) {
            addToBot(new DamageAction(monster, damageInfo, attackEffect));
        }
        this.isDone = true;
    }
}
