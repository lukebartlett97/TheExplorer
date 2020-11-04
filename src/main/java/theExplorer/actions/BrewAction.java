package theExplorer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theExplorer.cards.BrewCard;

public class BrewAction extends AbstractGameAction {

    private AbstractPlayer player;
    private AbstractMonster monster;
    private BrewCard caller;

    public BrewAction(AbstractPlayer player, AbstractMonster monster, BrewCard caller) {
        this.player = player;
        this.monster = monster;
        this.caller = caller;
    }

    @Override
    public void update() {
        AbstractCard discardedCard = player.discardPile.getTopCard();
        switch (discardedCard.type) {
            case ATTACK:
                caller.onAttackDiscarded(player, monster);
                break;
            case SKILL:
                caller.onSkillDiscarded(player, monster);
                break;
            case POWER:
                caller.onPowerDiscarded(player, monster);
                break;
            default:
                caller.onOtherDiscarded(player, monster);
        }
        this.isDone = true;
    }
}
