package theExplorer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theExplorer.cards.BrewCard;

public class MedleyBrewAction extends AbstractGameAction {

    private AbstractPlayer player;
    private AbstractMonster monster;
    private BrewCard caller;

    public MedleyBrewAction(AbstractPlayer player, AbstractMonster monster, BrewCard caller) {
        this.player = player;
        this.monster = monster;
        this.caller = caller;
    }

    @Override
    public void update() {
        caller.onCardUse(player, monster);
        this.isDone = true;
    }
}
